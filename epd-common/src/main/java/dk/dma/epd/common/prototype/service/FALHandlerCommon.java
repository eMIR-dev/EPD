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
package dk.dma.epd.common.prototype.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//import dk.dma.epd.common.prototype.model.route.StrategicRouteNegotiationData;

/**
 * Common handler class for the strategic route e-Navigation service
 */
public class FALHandlerCommon extends EnavServiceHandlerCommon {

    // protected Map<Long, StrategicRouteNegotiationData> strategicRouteNegotiationData = new ConcurrentHashMap<>();

    protected List<FALReportListener> falReportListeners = new CopyOnWriteArrayList<>();

    /**
     * Constructor
     */
    public FALHandlerCommon() {
        super(2);
    }

    /**
     * @return the falReportListeners
     */
    public List<FALReportListener> getFalReportListeners() {
        return falReportListeners;
    }

    /**
     * @param falReportListeners
     *            the falReportListeners to set
     */
    public void setFalReportListeners(List<FALReportListener> falReportListeners) {
        this.falReportListeners = falReportListeners;
    }

    /**
     * Notifies listeners about a fal report update
     */
    protected synchronized void notifyFALReportListeners() {
        for (FALReportListener listener : falReportListeners) {
            listener.FALReportCloudUpdate();
        }
    }

    /****************************************/
    /** Helper classes **/
    /****************************************/

    /**
     * Interface to be implemented by all clients wishing to be notified about updates to fal reports
     */
    public interface FALReportListener {

        /**
         * Cloud messages has changed
         */
        void FALReportCloudUpdate();

    }

}
