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
package dk.dma.epd.common.prototype.enavcloud;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.voyage.Waypoint;
import dk.dma.epd.common.Heading;
import dk.dma.epd.common.prototype.ais.VesselPositionData;
import dk.dma.epd.common.prototype.model.route.Route;
import dk.dma.epd.common.prototype.model.route.RouteLeg;
import dk.dma.epd.common.prototype.model.route.RouteWaypoint;
import dk.dma.epd.common.prototype.sensor.gps.GnssTime;

public class CloudIntendedRoute extends Route {

    private static final long serialVersionUID = 1L;
    int activeWaypoint;
    protected Double routeRange;
    protected Date received;
    protected long duration;
    protected Date etaFirst;
    protected Date etaLast;
    protected Double activeWpRange;
    
    protected List<Double> ranges = new ArrayList<>();

    public CloudIntendedRoute(dk.dma.enav.model.voyage.Route cloudRouteData) {
        super();
        received = GnssTime.getInstance().getDate();
        parseRoute(cloudRouteData);
    }

    
    
    public int getActiveWaypoint() {
        return activeWaypoint;
    }



    public void setActiveWaypoint(int activeWaypoint) {
        this.activeWaypoint = activeWaypoint;
    }



    private void parseRoute(dk.dma.enav.model.voyage.Route cloudRouteData) {
//        System.out.println("Parsing route");
        this.setName("Intended Route");

        this.activeWaypoint = cloudRouteData.getActiveWaypoint();
        
        List<Waypoint> cloudRouteWaypoints = cloudRouteData.getWaypoints();

        LinkedList<RouteWaypoint> routeWaypoints = this.getWaypoints();

        for (int i = 0; i < cloudRouteWaypoints.size(); i++) {

            RouteWaypoint waypoint = new RouteWaypoint();
            Waypoint cloudWaypoint = cloudRouteWaypoints.get(i);

            waypoint.setName("WP_" + i);

            if (i != 0) {
                RouteLeg inLeg = new RouteLeg();
                inLeg.setHeading(Heading.RL);
                waypoint.setInLeg(inLeg);

                // RouteWaypoint prevWaypoint =
                // routeWaypoints.get(routeWaypoints
                // .size() - 2);
                // System.out.println("For waypoint" + i + " creating in leg");
            }

            // Outleg always has next
            if (i != cloudRouteWaypoints.size() - 1) {
                RouteLeg outLeg = new RouteLeg();
                outLeg.setHeading(Heading.RL);
                waypoint.setOutLeg(outLeg);
                // System.out.println("For waypoint" + i + " creating out leg");
            }

            // if (waypoint.getInLeg() != null) {
            // waypoint.getInLeg().setSpeed(5.0);
            // }

            // if (waypoint.getOutLeg() != null) {
            // System.out.println("SEtting stuff?");
            // waypoint.getOutLeg().setSpeed(5.0);
            // // System.out.println(waypoint.getOutLeg().getSpeed());
            // }

            Position position = Position.create(cloudWaypoint.getLatitude(),
                    cloudWaypoint.getLongitude());
            waypoint.setPos(position);

            routeWaypoints.add(waypoint);

        }

        if (routeWaypoints.size() > 1) {
            for (int i = 0; i < routeWaypoints.size(); i++) {

                // System.out.println("Looking at waypoint:" + i);
                RouteWaypoint waypoint = routeWaypoints.get(i);
                Waypoint cloudWaypoint = cloudRouteWaypoints.get(i);

                // Waypoint 0 has no in leg, one out leg... no previous
                if (i != 0) {
                    RouteWaypoint prevWaypoint = routeWaypoints.get(i - 1);

                    if (waypoint.getInLeg() != null) {
                        // System.out.println("Setting inleg prev for waypoint:"
                        // + i);
                        waypoint.getInLeg().setStartWp(prevWaypoint);
                        waypoint.getInLeg().setEndWp(waypoint);
                    }

                    if (prevWaypoint.getOutLeg() != null) {
                        // System.out.println("Setting outleg prev for waypoint:"
                        // + i);
                        prevWaypoint.getOutLeg().setStartWp(prevWaypoint);
                        prevWaypoint.getOutLeg().setEndWp(waypoint);

                    }
                }

                if (cloudWaypoint.getTurnRad() != null) {
                    waypoint.setTurnRad(cloudWaypoint.getTurnRad());
                }

                if (cloudWaypoint.getSpeed() != null) {
                    waypoint.setSpeed(cloudWaypoint.getSpeed());
                }

                if (cloudWaypoint.getRot() != null) {
                    waypoint.setRot(cloudWaypoint.getRot());
                }
            }
        }

        etas = new ArrayList<>();
        // this.calcAllWpEta();
        for (int i = 0; i < cloudRouteWaypoints.size(); i++) {
            etas.add(cloudRouteWaypoints.get(i).getEta());
        }

        
        
        
        
        
        
        
        
        
        
        // Find ranges on each leg
        routeRange = 0.0;
        ranges.add(routeRange);
        for (int i=0; i < waypoints.size() - 1; i++) {
            double dist = waypoints.get(i).getPos().rhumbLineDistanceTo(waypoints.get(i + 1).getPos()) / 1852.0;
            routeRange += dist;
            ranges.add(routeRange);
        }
        
    }

    
    
    public Double getRouteRange() {
        return routeRange;
    }
    

    public Double getRange(int index) {
        if (activeWpRange == null) {
            return null;
        }
        return activeWpRange + ranges.get(index);
    }



    public void setRouteRange(Double routeRange) {
        this.routeRange = routeRange;
    }



    public Date getReceived() {
        return received;
    }



    public void setReceived(Date received) {
        this.received = received;
    }



    public long getDuration() {
        return duration;
    }



    public void setDuration(long duration) {
        this.duration = duration;
    }



    public Date getEtaFirst() {
        return etaFirst;
    }



    public void setEtaFirst(Date etaFirst) {
        this.etaFirst = etaFirst;
    }



    public Date getEtaLast() {
        return etaLast;
    }



    public void setEtaLast(Date etaLast) {
        this.etaLast = etaLast;
    }



    public List<Double> getRanges() {
        return ranges;
    }



    public void setRanges(List<Double> ranges) {
        this.ranges = ranges;
    }


    /**
     * Update range to active WP given the targets new position
     * 
     * @param posData
     */
    public void update(VesselPositionData posData) {
        if (posData == null || posData.getPos() == null
                || waypoints.size() == 0) {
            return;
        }

        // Range to first wp

        // activeWpRange =
        // posData.getPos().rhumbLineDistanceTo(waypoints.get(0)) / 1852.0;
    }

    public double getSpeed(int id) {
        if (waypoints.get(id).getOutLeg() != null){
            return waypoints.get(id).getOutLeg().getSpeed(); 
        }
        
        return 0;
    }

    
    
}