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
package dk.dma.epd.common.prototype.gui.views;

import java.awt.Point;
import java.util.List;
import java.util.Objects;

import com.bbn.openmap.Layer;
import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.gui.OMComponentPanel;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.layer.shape.MultiShapeLayer;
import com.bbn.openmap.proj.Proj;
import com.bbn.openmap.proj.Projection;
import com.bbn.openmap.proj.coords.LatLonPoint;

import dk.dma.enav.model.geometry.Position;
import dk.dma.epd.common.prototype.EPD;
import dk.dma.epd.common.prototype.event.HistoryListener;
import dk.dma.epd.common.prototype.event.mouse.CommonDragMouseMode;
import dk.dma.epd.common.prototype.event.mouse.CommonNavigationMouseMode;
import dk.dma.epd.common.prototype.event.mouse.CommonRouteEditMouseMode;
import dk.dma.epd.common.prototype.gui.util.DraggableLayerMapBean;
import dk.dma.epd.common.prototype.layers.ais.AisLayerCommon;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteLayerCommon;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteTCPALayer;
import dk.dma.epd.common.prototype.layers.msi.MsiLayerCommon;
import dk.dma.epd.common.prototype.layers.route.RouteLayerCommon;
import dk.dma.epd.common.prototype.layers.routeedit.NewRouteContainerLayer;
import dk.dma.epd.common.prototype.layers.routeedit.RouteEditLayerCommon;
import dk.dma.epd.common.prototype.layers.util.LayerVisibilityAdapter;
import dk.dma.epd.common.prototype.layers.wms.WMSLayer;
import dk.dma.epd.common.prototype.settings.gui.MapCommonSettings;

/**
 * The panel with chart. Initializes all layers to be shown on the map.
 * 
 * @author Jens Tuxen (mail@jenstuxen.com)
 */
public abstract class ChartPanelCommon extends OMComponentPanel {
    
    private static final long serialVersionUID = 1L;

//    protected int maxScale = 5000;
    
    // Mouse modes
    protected String mouseMode;
    protected MouseDelegator mouseDelegator;
    protected CommonNavigationMouseMode mapNavMouseMode;
    protected CommonDragMouseMode dragMouseMode;
    protected CommonRouteEditMouseMode routeEditMouseMode;
    
    // Layers and handlers
    protected MapHandler mapHandler;
    protected LayerHandler layerHandler;
    protected DraggableLayerMapBean map;
    protected OMGraphicHandlerLayer encLayer;
    protected MultiShapeLayer bgLayer;
    protected WMSLayer wmsLayer;
    protected AisLayerCommon<?> aisLayer;
    protected RouteLayerCommon routeLayer;
    protected RouteEditLayerCommon routeEditLayer;
    protected NewRouteContainerLayer newRouteContainerLayer;
    protected MsiLayerCommon msiLayer;
    protected IntendedRouteLayerCommon intendedRouteLayer;
    protected IntendedRouteTCPALayer intendedRouteTCPALayer;
    
    protected LayerVisibilityAdapter encVisibilityAdapter = new LayerVisibilityAdapter();
    protected HistoryListener historyListener;
    
    private MapCommonSettings<?> mapSettings;
    
    /**
     * Constructor
     */
    protected ChartPanelCommon(MapCommonSettings<?> mapSettings) {
        this.mapSettings = Objects.requireNonNull(mapSettings);
//        maxScale = EPD.getInstance().getSettings().getMapSettings().getMaxScale();
    }
    
    protected MapCommonSettings<?> getMapSettings() {
        return mapSettings;
    }
    
    /**
     * Save chart settings for workspace
     */
    public void saveSettings() {
//        MapSettings mapSettings = EPD.getInstance().getSettings().getMapSettings();
        mapSettings.setCenter((LatLonPoint) map.getCenter());
        mapSettings.setInitialMapScale(map.getScale());
    }

    /**
     * Force an update in the AIS layer
     */
    public void forceAisLayerUpdate() {
        if (aisLayer != null) {
            aisLayer.forceLayerUpdate();
        }
    }
    
    /**
     * Returns the current mouse mode
     * @return the current mouse mode
     */
    public String getMouseMode() {
        return mouseMode;
    }
    
    /**
     * Change the mouse mode.
     * 
     * @param mode The mode ID of the mouse mode to swap to (e.g.
     *            DistanceCircleMouseMode.MODE_ID).
     */
    public abstract void setMouseMode(String modeID);


    /*******************************/
    /** Zooming and panning       **/
    /*******************************/
    
    /**
     * Changes the current center of the map to a new position.
     * @param position Position to change to center.
     */
    public void goToPosition(Position position) {
        getMap().setCenter(position.getLatitude(), position.getLongitude());
        forceAisLayerUpdate();
    }
    
    /**
     * Given a set of points scale and center so that all points are contained in the view
     * 
     * @param waypoints
     */
    public void zoomTo(List<Position> waypoints) {
        if (waypoints.size() == 0) {
            return;
        }

        if (waypoints.size() == 1) {
            map.setCenter(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude());
            forceAisLayerUpdate();
            return;
        }

        // Find bounding box
        double maxLat = -91;
        double minLat = 91;
        double maxLon = -181;
        double minLon = 181;
        for (Position pos : waypoints) {
            if (pos.getLatitude() > maxLat) {
                maxLat = pos.getLatitude();
            }
            if (pos.getLatitude() < minLat) {
                minLat = pos.getLatitude();
            }
            if (pos.getLongitude() > maxLon) {
                maxLon = pos.getLongitude();
            }
            if (pos.getLongitude() < minLon) {
                minLon = pos.getLongitude();
            }
        }

        double centerLat = (maxLat + minLat) / 2.0;
        double centerLon = (maxLon + minLon) / 2.0;
        map.setCenter(centerLat, centerLon);
        forceAisLayerUpdate();
    }
    
    /**
     * Change zoom level on map
     * 
     * @param factor
     */
    public void doZoom(float factor) {
        float newScale = map.getScale() * factor;
        if (newScale < mapSettings.getMinMapScale()) {
            newScale = mapSettings.getMinMapScale();
        }
        map.setScale(newScale);
        forceAisLayerUpdate();
    }
    
    /**
     * Pans the map in the given direction
     * 
     * @param direction
     *            1 == Up 2 == Down 3 == Left 4 == Right
     * 
     *            Moving by 100 units in each direction Map center is [745, 445]
     */
    public void pan(int direction) {
        Point point = null;
        Projection projection = map.getProjection();

        int width = projection.getWidth();
        int height = projection.getHeight();

        switch (direction) {
        case 1:
            point = new Point(width / 2, height / 2 - 100);
            break;
        case 2:
            point = new Point(width / 2, height / 2 + 100);
            break;
        case 3:
            point = new Point(width / 2 - 100, height / 2);
            break;
        case 4:
            point = new Point(width / 2 + 100, height / 2);
            break;
        }

        Proj p = (Proj) projection;
        LatLonPoint llp = projection.inverse(point);
        p.setCenter(llp);
        map.setProjection(p);

        forceAisLayerUpdate();
    }
    
    @Override
    public void findAndInit(Object obj) {
        super.findAndInit(obj);
        if(obj instanceof AisLayerCommon<?>) {
            /*
             * Toggle layer visibility according to layer settings.
             * This is required as layers must be visible in order
             * to be added to the map bean. Hence we must wait until
             * the layer is properly added to the map bean before we
             * set its visibility.
             */
            AisLayerCommon<?> alc = (AisLayerCommon<?>) obj;
            alc.setVisible(alc.getSettings().isVisible());
        }
    }
    
    /*******************************/
    /** Layer visibility          **/
    /*******************************/
    
    /**
     * Sets AIS layer visibility
     * 
     * @param visible the visibility
     */
    public void aisVisible(boolean visible) {
        if (aisLayer != null) {
            aisLayer.getSettings().setVisible(visible);
        }
    }

    /**
     * Sets ENC layer visibility
     * 
     * @param visible the visibility
     */
    public void encVisible(boolean visible) {
        encVisible(visible, true);
    }
    
    /**
     * Sets ENC layer visibility
     * 
     * @param visible the visibility
     * @param persist persist the change to the settings
     */
    public void encVisible(boolean visible, boolean persist) {
        // Note: After upgrading to OpenMap 5.0.3, there seemed to be a minor problem
        // with having multiple background layers installed (EPD-186), causing the 
        // background layer to turn blank after launch.
        // Hence instead of adding both background layers and toggling the visibility,
        // the strategy was changed to add and remove the background layers:
        
        if (encLayer != null) {
            if (visible) {
                mapHandler.remove(bgLayer);
                // TODO update to encLayer.getSettings().setVisible(visible)...
                encLayer.setVisible(true);
                mapHandler.add(encLayer);
                encLayer.doPrepare();
                encVisibilityAdapter.notifyVisibilityListeners(encLayer);                
            } else {
                mapHandler.remove(encLayer);
                mapHandler.add(bgLayer);
                // TODO update to encLayer.getSettings().setVisible(visible)...
                encLayer.setVisible(false);
                bgLayer.doPrepare();
                encVisibilityAdapter.notifyVisibilityListeners(encLayer);                
            }
            if (persist) {
                // TODO update to encLayer.getSettings().setEncVisible(visible);
                EPD.getInstance().getSettings().getENCLayerSettings().setVisible(visible);
            }
        } else {
            bgLayer.setVisible(true);
        }
    }

    /**
     * Returns if the ENC layer is visible or not
     * @return if the ENC layer is visible or not
     */
    public boolean isEncVisible() {
        return encLayer != null && encLayer.isVisible() && mapHandler.contains(encLayer);
    }
    
    /**
     * Sets WMS layer visibility
     * 
     * @param visible the visibility
     */
    public void wmsVisible(boolean visible) {
        if (wmsLayer != null) {
            // TODO Layer should observe its settings.
            wmsLayer.getSettings().setVisible(visible);
        }
    }

    /**
     * Sets Intended Route layer visibility
     * 
     * @param visible the visibility
     */
    public void intendedRouteLayerVisible(boolean visible) {
        if (intendedRouteLayer != null) {
            // TODO Layer should observe its settings.
            intendedRouteLayer.getSettings().setVisible(visible);
        }
    }

    
    /*******************************/
    /** Getters and setters       **/
    /*******************************/
    
    public DraggableLayerMapBean getMap() {
        return map;
    }
    
    public MapHandler getMapHandler() {
        return mapHandler;
    }
    
    public MultiShapeLayer getBgLayer() {
        return bgLayer;
    }
    
    public AisLayerCommon<?> getAisLayer() {
        return aisLayer;
    }
    
    public RouteLayerCommon getRouteLayer() {
        return routeLayer;
    }

    public RouteEditLayerCommon getRouteEditLayer() {
        return routeEditLayer;
    }

    public NewRouteContainerLayer getNewRouteContainerLayer() {
        return newRouteContainerLayer;
    }

    public MsiLayerCommon getMsiLayer() {
        return msiLayer;
    }

    public WMSLayer getWmsLayer() {
        return wmsLayer;
    }

    public Layer getEncLayer() {
        return encLayer;
    }
    
    public HistoryListener getHistoryListener() {
        return historyListener;
    }
    
    public void setHistoryListener(HistoryListener historyListener2) {
        this.historyListener = historyListener2;
    }
    
    public MouseDelegator getMouseDelegator() {
        return mouseDelegator;
    }

    public LayerVisibilityAdapter getEncVisibilityAdapter() {
        return encVisibilityAdapter;
    }
}
