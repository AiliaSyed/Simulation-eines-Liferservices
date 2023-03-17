package projekt.delivery.rating;

import projekt.delivery.event.ArrivedAtNodeEvent;
import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.routing.PathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.tudalgo.algoutils.student.Student.crash;


/**
 * Rates the observed {@link Simulation} based on the distance traveled by all vehicles.<p>
 *
 * To create a new {@link TravelDistanceRater} use {@code TravelDistanceRater.Factory.builder()...build();}.
 */
public class TravelDistanceRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.TRAVEL_DISTANCE;

    private final Region region;
    private final PathCalculator pathCalculator;
    private final double factor;
    private double worstDistance = 0.0;
    private double actualDistance = 0.0;

    private TravelDistanceRater(VehicleManager vehicleManager, double factor) {
        region = vehicleManager.getRegion();
        pathCalculator = vehicleManager.getPathCalculator();
        this.factor = factor;
    }

    @Override
    public double getScore() {
        if (actualDistance >= 0.0 && actualDistance < worstDistance * factor) {
            return 1.0 - actualDistance / (worstDistance * factor);
        }
        else {
            return 0.0;
        }

        // TODO: H8.3 - remove if implemented
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        double totalDistance = 0.0;

        // update worstDistance with the distance of all delivered orders
        for (Event event : events) {
            if (event instanceof DeliverOrderEvent) {
                DeliverOrderEvent deliverOrderEvent = (DeliverOrderEvent) event;
                double distance = getDistance(region.getNode(deliverOrderEvent.getNode().getLocation()),
                    region.getNode(deliverOrderEvent.getOrder().getLocation()));
                distance *= 2; // round-trip distance
                worstDistance += distance;
            }
        }
        // update actualDistance with the distance of all vehicles
        for (Event event : events) {
            if (event instanceof ArrivedAtNodeEvent arrivedAtNodeEvent) {
                totalDistance += arrivedAtNodeEvent.getLastEdge().getDuration();
            }
        }
        actualDistance = totalDistance;

        // TODO: H8.3 - remove if implemented
    }
    private double getDistance(Region.Node startNode, Region.Node endNode) {
        Deque<Region.Node> path = pathCalculator.getPath(startNode, endNode);
        Iterator<Region.Node> iterator = path.iterator();
        int counter = 0;
        Region.Node from = null;
        Region.Node to;
        double distance = 0.0;
        while (iterator.hasNext()) {
            if(counter == 0) {
                from = iterator.next();
                counter = 1;
            }
            else {
                to = iterator.next();
                distance += Objects.requireNonNull(region.getEdge(from, to)).getDuration();
                counter = 0;
            }
        }

        return distance;
    }

    /**
     * A {@link Rater.Factory} for creating a new {@link TravelDistanceRater}.
     */
    public static class Factory implements Rater.Factory {

        public final VehicleManager vehicleManager;
        public final double factor;

        private Factory(VehicleManager vehicleManager, double factor) {
            this.vehicleManager = vehicleManager;
            this.factor = factor;
        }

        @Override
        public TravelDistanceRater create() {
            return new TravelDistanceRater(vehicleManager, factor);
        }

        /**
         * Creates a new {@link TravelDistanceRater.FactoryBuilder}.
         * @return The created {@link TravelDistanceRater.FactoryBuilder}.
         */
        public static FactoryBuilder builder() {
            return new FactoryBuilder();
        }


    }

    /**
     * A {@link Rater.FactoryBuilder} form constructing a new {@link TravelDistanceRater.Factory}.
     */
    public static class FactoryBuilder implements Rater.FactoryBuilder {

        public VehicleManager vehicleManager;
        public double factor = 0.5;

        private FactoryBuilder() {}

        @Override
        public Factory build() {
            return new Factory(vehicleManager, factor);
        }

        public FactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public FactoryBuilder setFactor(double factor) {
            if (factor < 0) {
                throw new IllegalArgumentException("factor must be positive");
            }

            this.factor = factor;
            return this;
        }
    }

}
