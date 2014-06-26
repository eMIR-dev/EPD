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

import dk.dma.epd.common.prototype.settings.handlers.MSIHandlerCommonSettings;

/**
 * Interface for observing an {@link MSIHandlerCommonSettings} for changes.
 * 
 * @author Janus Varmarken
 */
public interface MSIHandlerCommonSettingsListener extends
        HandlerSettingsListener {

    /**
     * Invoked when {@link MSIHandlerCommonSettings#isMsiFilter()} has
     * changed.
     * 
     * @param msiFilter
     *            See {@link MSIHandlerCommonSettings#isMsiFilter()}.
     */
    void useMsiFilterChanged(boolean msiFilter);

    /**
     * Invoked when {@link MSIHandlerCommonSettings#getMsiPollInterval()}
     * has changed.
     * 
     * @param pollInterval
     *            The MSI poll interval. See
     *            {@link MSIHandlerCommonSettings#getMsiPollInterval()} for
     *            more details.
     */
    void msiPollIntervalChanged(int pollInterval);

    /**
     * Invoked when
     * {@link MSIHandlerCommonSettings#getMsiRelevanceFromOwnShipRange()}
     * has changed.
     * 
     * @param relevanceFromOwnShipRange
     *            See
     *            {@link MSIHandlerCommonSettings#getMsiRelevanceFromOwnShipRange()}
     *            .
     */
    void msiRelevanceFromOwnShipRangeChanged(
            double relevanceFromOwnShipRange);

    /**
     * Invoked when
     * {@link MSIHandlerCommonSettings#getMsiRelevanceGpsUpdateRange()} has
     * changed.
     * 
     * @param relevanceGpsUpdateRange
     *            See
     *            {@link MSIHandlerCommonSettings#getMsiRelevanceGpsUpdateRange()}
     *            .
     */
    void msiRelevanceGpsUpdateRangeChanged(double relevanceGpsUpdateRange);
    
}