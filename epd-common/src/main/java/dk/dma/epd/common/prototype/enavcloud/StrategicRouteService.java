/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.common.prototype.enavcloud;

import dk.dma.enav.model.voyage.Route;
import dk.dma.epd.common.prototype.service.EnavServiceHandlerCommon.CloudMessageStatus;

import java.awt.Color;
import java.util.Date;

import static java.util.Objects.requireNonNull;

/**
 * Maritime cloud service for exchanging strategic routes (formerly "Mona Lisa").
 * <p>
 * Defines the service initiation point along with the following classes:
 * <ul>
 *   <li>{@linkplain StrategicRouteStatus} status of the strategic route.</li>
 *   <li>{@linkplain StrategicRouteMessage} Used for sending a strategic route request.</li>
 *   <li>{@linkplain StrategicRouteReply} Used for acknowledging the request.</li>
 * </ul>
 */
public class StrategicRouteService {
    
    /** An initiation point */
    public static final TODO.ServiceInitiationPoint<StrategicRouteMessage> INIT = new TODO.ServiceInitiationPoint<>(
            StrategicRouteMessage.class);
    
    /**
     * Defines the current status of a strategic route exchange between a ship and an STCC.
     */
    public enum StrategicRouteStatus {
        PENDING(false, Color.YELLOW), 
        AGREED(true, new Color(130, 165, 80)), 
        REJECTED(true, new Color(165, 80, 80)), 
        NEGOTIATING(false, Color.YELLOW), 
        CANCELED(true, new Color(165, 80, 80));
        
        boolean complete;
        Color color;
        
        private StrategicRouteStatus(boolean complete, Color color) {
            this.complete = complete;
            this.color = color;
        }
        
        public boolean isComplete() { return complete; }
        public Color getColor()  { return color; }
    }
    
    /**
     * Used for sending a request from a ship to an STCC.
     */
    public static class StrategicRouteMessage extends
            TODO.ServiceMessage<StrategicRouteReply> {

        private boolean fromStcc;
        private long id;
        private Route route;
        private Date sentDate;
        private String message;
        private StrategicRouteStatus status;
        
        // Not sent along
        private transient CloudMessageStatus cloudMessageStatus;

        /**
         * No-arg constructor
         */
        public StrategicRouteMessage() {
        }

        /**
         * Constructor
         * 
         * @param fromStcc whether this message is from an STCC or a ship
         * @param id id of the transaction
         * @param route the route
         * @param message an additional message
         * @param status the status
         */
        public StrategicRouteMessage(boolean fromStcc, long id, Route route, String message, StrategicRouteStatus status) {
            this.fromStcc = fromStcc;
            this.id = id;
            this.route = requireNonNull(route);
            this.message = requireNonNull(message);
            this.status = status;
            this.sentDate = new Date();
        }

        /********* Getters and setters ***********/
        
        public boolean isFromStcc() {
            return fromStcc;
        }

        public void setFromStcc(boolean fromStcc) {
            this.fromStcc = fromStcc;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
        
        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getSentDate() {
            return sentDate;
        }

        public void setSentDate(Date sentDate) {
            this.sentDate = sentDate;
        }

        public StrategicRouteStatus getStatus() {
            return status;
        }

        public void setStatus(StrategicRouteStatus status) {
            this.status = status;
        }
        
        public CloudMessageStatus getCloudMessageStatus() {
            return cloudMessageStatus;
        }

        public void setCloudMessageStatus(CloudMessageStatus cloudMessageStatus) {
            this.cloudMessageStatus = cloudMessageStatus;
        }
        
        /**
         * Some of the cloud status updates may arrive out of order. Use the 
         * {@code CloudMessageStatus.combine()} method to make sure the order
         * is maintained
         * 
         * @param cloudMessageStatus the new cloud message status
         */
        public synchronized void updateCloudMessageStatus(CloudMessageStatus cloudMessageStatus) {
            if (cloudMessageStatus != null) {
                this.cloudMessageStatus = cloudMessageStatus.combine(this.cloudMessageStatus);
            }
        }
    }

    /**
     * Acknowledges receiving a strategic route request
     */
    public static class StrategicRouteReply extends TODO.ServiceMessage<Void> {

        private long id;
        private Date receivedDate;

        /**
         * No-arg constructor
         */
        public StrategicRouteReply() {
        }

        /**
         * Constructor
         * 
         * @param id id of the transaction
         */
        public StrategicRouteReply(long id) {
            this.id = id;
            this.receivedDate = new Date();
        }

        /********* Getters and setters ***********/
        
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Date getReceivedDate() {
            return receivedDate;
        }

        public void setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
        }
    }
}
