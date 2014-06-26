/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.epd.common.prototype.settings.observers;

import dk.dma.epd.common.prototype.settings.layers.WMSLayerCommonSettings;

/**
 * Interface for observing a {@link WMSLayerCommonSettings} for changes.
 * 
 * @author Janus Varmarken
 */
public interface WMSLayerCommonSettingsListener extends LayerSettingsListener {

    /**
     * Invoked when {@link WMSLayerCommonSettings#isUseWms()} has changed.
     * 
     * @param useWms
     *            The update value. Refer to
     *            {@link WMSLayerCommonSettings#isUseWms()} for its
     *            interpretation.
     * @param source
     *            The instance that fired this event.
     */
    void isUseWmsChanged(WMSLayerCommonSettings<?> source, boolean useWms);

    /**
     * Invoked when {@link WMSLayerCommonSettings#getWmsQuery()} has changed.
     * 
     * @param wmsQuery
     *            The updated value. Refer to
     *            {@link WMSLayerCommonSettings#getWmsQuery()} for its
     *            interpretation.
     * @param source
     *            The instance that fired this event.
     */
    void wmsQueryChanged(WMSLayerCommonSettings<?> source, String wmsQuery);
}