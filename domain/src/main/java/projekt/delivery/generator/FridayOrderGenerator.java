package projekt.delivery.generator;

import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An implementation of an {@link OrderGenerator} that represents the incoming orders on an average friday evening.
 * The incoming orders follow a normal distribution.<p>
 *
 * To create a new {@link FridayOrderGenerator} use {@code FridayOrderGenerator.Factory.builder()...build();}.
 */
public class FridayOrderGenerator implements OrderGenerator {

    private final Random random;

    /**
     * Creates a new {@link FridayOrderGenerator} with the given parameters.
     * @param orderCount The total amount of orders this {@link OrderGenerator} will create. It is equal to the sum of
     *                   the size of the lists that are returned for every positive long value.
     * @param vehicleManager The {@link VehicleManager} this {@link OrderGenerator} will create orders for.
     * @param deliveryInterval The amount of ticks between the start and end tick of the deliveryInterval of the created orders.
     * @param maxWeight The maximum weight of a created order.
     * @param variance The variance of the normal distribution.
     * @param lastTick The last tick this {@link OrderGenerator} can return a non-empty list.
     * @param seed The seed for the used {@link Random} instance. If negative a random seed will be used.
     */
    private FridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double variance, long lastTick, int seed) {
        random = seed < 0 ? new Random() : new Random(seed);
        orderCount = (int) random.nextGaussian(0,lastTick);

       // (int)random.nextGaussian(0,variance) ;
        // double weight = random.nextGaussian(0,maxWeight);

            // TODO: H7.1 - remove if implemented
    }

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        if (tick < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            Location location = vehicleManager.getOccupiedNeighborhoods();
            VehicleManager.OccupiedRestaurant restaurant = vehicleManager.getOccupiedRestaurants();
            TickInterval interval = new TickInterval(tick, tick + deliveryInterval);
            List<String> foodlist = new ArrayList<>();
            foodlist.set(random.nextInt(0, 10), restaurant.getComponent().getAvailableFood().toString());
            //String invalidFood = foodList.stream().filter(food -> !restaurant.getComponent().getAvailableFood().contains(food)).findFirst().orElse(null);
            double weight = random.nextDouble(0, maxWeight);
            return List.of(location, restaurant, interval, foodlist, weight);
        }
        // TODO: H7.1 - remove if implemented
    }

    /**
     * A {@link OrderGenerator.Factory} for creating a new {@link FridayOrderGenerator}.
     */
    public static class Factory implements OrderGenerator.Factory {

        public final int orderCount;
        public final VehicleManager vehicleManager;
        public final int deliveryInterval;
        public final double maxWeight;
        public final double variance;
        public final long lastTick;
        public final int seed;

        private Factory(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double variance, long lastTick, int seed) {
            this.orderCount = orderCount;
            this.vehicleManager = vehicleManager;
            this.deliveryInterval = deliveryInterval;
            this.maxWeight = maxWeight;
            this.variance = variance;
            this.lastTick = lastTick;
            this.seed = seed;
        }

        @Override
        public OrderGenerator create() {
            return new FridayOrderGenerator(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick, seed);
        }

        /**
         * Creates a new {@link FridayOrderGenerator.FactoryBuilder}.
         * @return The created {@link FridayOrderGenerator.FactoryBuilder}.
         */
        public static FridayOrderGenerator.FactoryBuilder builder() {
            return new FridayOrderGenerator.FactoryBuilder();
        }
    }


    /**
     * A {@link OrderGenerator.FactoryBuilder} form constructing a new {@link FridayOrderGenerator.Factory}.
     */
    public static class FactoryBuilder implements OrderGenerator.FactoryBuilder {

        public int orderCount = 1000;
        public VehicleManager vehicleManager = null;
        public int deliveryInterval = 15;
        public double maxWeight = 0.5;
        public double variance = 0.5;
        public long lastTick = 480;
        public int seed = -1;

        private FactoryBuilder() {}

        public FactoryBuilder setOrderCount(int orderCount) {
            this.orderCount = orderCount;
            return this;
        }

        public FactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public FactoryBuilder setDeliveryInterval(int deliveryInterval) {
            this.deliveryInterval = deliveryInterval;
            return this;
        }

        public FactoryBuilder setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public FactoryBuilder setVariance(double variance) {
            this.variance = variance;
            return this;
        }

        public FactoryBuilder setLastTick(long lastTick) {
            this.lastTick = lastTick;
            return this;
        }

        public FactoryBuilder setSeed(int seed) {
            this.seed = seed;
            return this;
        }

        @Override
        public Factory build() {
            Objects.requireNonNull(vehicleManager);
            return new Factory(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick, seed);
        }
    }
}
