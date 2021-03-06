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
package dk.dma.epd.common.prototype.model.identity;

import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbn.openmap.util.PropUtils;

/**
 * Identity used for the maritime cloud, containing all relevant info
 * 
 * @author David Andersen Camre
 * 
 */
public class MaritimeIdentity {
    private static final Logger LOG = LoggerFactory.getLogger(MaritimeIdentity.class);

    /**
     * The Maritime ID,
     */
    long maritimeID;

    /**
     * Role of the actor
     */
    MCRole role;

    /**
     * Affliation of the actor
     */
    String affiliation;

    /**
     * Actors name
     */
    String name;

    /**
     * Short description of actor
     */
    String description;

    ArrayList<MCService> services = new ArrayList<>();

    private static final String SERVICE_PREFIX = "service.";

    /**
     * Generate a maritime Identity from a pre-saved properties file
     * 
     * @param identity
     */
    public MaritimeIdentity(Properties identity) {
        try {

            maritimeID = PropUtils.longFromProperties(identity, "maritimeID", maritimeID);
            role = MCRole.valueOf(identity.getProperty("role"));
            affiliation = identity.getProperty("affiliation");
            name = identity.getProperty("name");
            description = identity.getProperty("description");

            String servicesName = identity.getProperty(SERVICE_PREFIX + "name");
            String serviceDescriptions = identity.getProperty(SERVICE_PREFIX + "description");
            String serviceLat = identity.getProperty(SERVICE_PREFIX + "lat");
            String serviceLon = identity.getProperty(SERVICE_PREFIX + "lon");

            String[] servicesNamesList = servicesName.split(";");
            String[] serviceDescriptionsList = serviceDescriptions.split(";");
            String[] serviceLatList = serviceLat.split(";");
            String[] serviceLonList = serviceLon.split(";");

            for (int i = 0; i < servicesNamesList.length; i++) {
                MCService service = new MCService(servicesNamesList[i], serviceDescriptionsList[i]);

                String[] serviceLats = serviceLatList[i].split(" ");
                String[] serviceLons = serviceLonList[i].split(" ");

                try {
                    for (int j = 0; j < serviceLats.length; j++) {
                        service.addPosition(Double.parseDouble(serviceLats[j]), Double.parseDouble(serviceLons[j]));
                    }
                    services.add(service);
                } catch (Exception e) {
                    LOG.error("Error loading service positions");
                }
            }
        } catch (Exception e) {
            LOG.error("FAILED TO LOAD IDENTITY " + identity);
        }
    }

    /**
     * @return the maritimeID
     */
    public long getMaritimeID() {
        return maritimeID;
    }

    /**
     * @return the role
     */
    public MCRole getRole() {
        return role;
    }

    /**
     * @return the affiliation
     */
    public String getAffiliation() {
        return affiliation;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the services
     */
    public ArrayList<MCService> getServices() {
        return services;
    }

}
