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
package dk.dma.epd.ship.gui.menuitems;

import javax.swing.JMenuItem;

import dk.dma.epd.common.prototype.gui.menuitems.event.IMapMenuAction;
import dk.dma.epd.common.prototype.model.route.RouteSuggestionData;
import dk.dma.epd.common.prototype.model.route.RoutesUpdateEvent;
import dk.dma.epd.ship.EPDShip;

/**
 * Opens the route suggestion notification
 */
public class RouteSuggestionRemove extends JMenuItem implements IMapMenuAction {

    private static final long serialVersionUID = 1L;
    private RouteSuggestionData routeSuggestion;

    public RouteSuggestionRemove(String text) {
        super();
        setText(text);
    }

    @Override
    public void doAction() {

        // routeLayer.getro
        routeSuggestion.getRoute().setVisible(false);
        EPDShip.getInstance().getRouteManager()
                .notifyListeners(RoutesUpdateEvent.ROUTE_VISIBILITY_CHANGED);
        // routeSuggestion.
        // EPDShip.getInstance()
        // .getNotificationCenter()
        // .openNotification(NotificationType.TACTICAL_ROUTE,
        // routeSuggestion.getId(), false);
    }

    public void setRouteSuggestion(RouteSuggestionData routeSuggestion) {
        this.routeSuggestion = routeSuggestion;
    }
}
