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

import dk.dma.epd.common.prototype.model.route.IntendedRoute;


/**
 * Interface to implement for classes wanting to receive Intended Route updates
 */
public interface IIntendedRouteListener {

    /**
     * Called when an event regarding intended routes has occured
     * such as added, removed or updated
     * @param intendedRoute the intended route
     */
    void intendedRouteEvent(IntendedRoute intendedRoute);
    
}
