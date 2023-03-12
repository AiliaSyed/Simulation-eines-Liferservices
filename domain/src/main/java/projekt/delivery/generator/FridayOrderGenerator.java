package projekt.delivery.generator;

import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.Serializable;

/**
 * An implementation of an {@link OrderGenerator} that represents the incoming orders on an average friday evening.
 * The incoming orders follow a normal distribution.<p>
 *
 * To create a new {@link FridayOrderGenerator} use {@code FridayOrderGenerator.Factory.builder()...build();}.
 */
public class FridayOrderGenerator implements OrderGenerator, Serializable {

    private final Random random;
    private final int orderCount;
    private final VehicleManager vehicleManager;
    private final int deliveryInterval;
    private final double maxWeight;
    private final double standardDeviation;
    private final long lastTick;
    private final int seed;
    private final List<Integer> tickList;
    private Map<Integer, List<ConfirmedOrder>> ordersByTick;

    /**
     * Creates a new {@link FridayOrderGenerator} with the given parameters.
     * @param orderCount The total amount of orders this {@link OrderGenerator} will create. It is equal to the sum of
     *                   the size of the lists that are returned for every positive long value.
     * @param vehicleManager The {@link VehicleManager} this {@link OrderGenerator} will create orders for.
     * @param deliveryInterval The amount of ticks between the start and end tick of the deliveryInterval of the created orders.
     * @param maxWeight The maximum weight of a created order.
     * @param standardDeviation The standardDeviation of the normal distribution.
     * @param lastTick The last tick this {@link OrderGenerator} can return a non-empty list.
     * @param seed The seed for the used {@link Random} instance. If negative a random seed will be used.
     */
    private FridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double standardDeviation, long lastTick, int seed) {
        random = seed < 0 ? new Random() : new Random(seed);
        this.orderCount = orderCount;
        this.vehicleManager = vehicleManager;
        this.deliveryInterval = deliveryInterval;
        this.maxWeight = maxWeight;
        this.standardDeviation = standardDeviation;
        this.lastTick = lastTick;
        this.seed = seed;
        this.ordersByTick = new HashMap<>();
        // Generate the list of ticks for each order
        double mean = (lastTick - 0) / 2.0;
        tickList = new ArrayList<>(orderCount);
        for (int i = 0; i < orderCount; i++) {
            double scaledValue = random.nextGaussian(mean, standardDeviation) * (lastTick - 0) / (2 * standardDeviation) + (lastTick + 0) / 2.0;
            int tick = (int) Math.round(scaledValue);
            tickList.add(tick);
        }

        // TODO: H7.1 - remove if implemented
    }

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        if (tick < 0) {
            throw new IndexOutOfBoundsException(tick);
        }

        // Return pre-generated orders if the tick matches
        if(ordersByTick != null) {
            if (ordersByTick.containsKey((int) tick)) {
                return ordersByTick.get((int) tick);
            }
        }

        List<ConfirmedOrder> orders = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            int tickValue = tickList.get(i);
            if (tickValue == tick) {
                Location location = Objects.requireNonNull(vehicleManager.getOccupiedNeighborhoods().stream()
                    .skip((int) (vehicleManager.getOccupiedNeighborhoods().size() * random.nextDouble()))
                    .findFirst().orElse(null)).getComponent().getLocation();
                VehicleManager.OccupiedRestaurant restaurant = vehicleManager.getOccupiedRestaurants().stream()
                    .skip((int) (vehicleManager.getOccupiedNeighborhoods().size() * random.nextDouble()))
                    .findFirst().orElse(null);
                List<String> foodList = restaurant != null ? Stream.of(restaurant.getComponent().getAvailableFood())
                    .flatMap(list -> list.stream()).toList()
                    : Collections.emptyList();
                int numFoods = random.nextInt(9) + 1;
                List<String> foods = new ArrayList<>();
                for (int k = 0; k < numFoods; k++) {
                    String food = foodList.get(random.nextInt(foodList.size()));
                    foods.add(food);
                }
                double weight = random.nextDouble() * maxWeight;
                TickInterval deliveryInterval = new TickInterval(tick, tick + this.deliveryInterval);
                orders.add(new ConfirmedOrder(location, restaurant, deliveryInterval, foodList, weight));
            }
        }
        //put new orders by tick in Map
        ordersByTick.put((int) tick, orders);
        return orders;
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
        public final double standardDeviation;
        public final long lastTick;
        public final int seed;

        private Factory(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double standardDeviation, long lastTick, int seed) {
            this.orderCount = orderCount;
            this.vehicleManager = vehicleManager;
            this.deliveryInterval = deliveryInterval;
            this.maxWeight = maxWeight;
            this.standardDeviation = standardDeviation;
            this.lastTick = lastTick;
            this.seed = seed;
        }

        @Override
        public OrderGenerator create() {
            return new FridayOrderGenerator(orderCount, vehicleManager, deliveryInterval, maxWeight, standardDeviation, lastTick, seed);
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
        public double standardDeviation = 0.5;
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

        public FactoryBuilder setStandardDeviation(double standardDeviation) {
            this.standardDeviation = standardDeviation;
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
            return new Factory(orderCount, vehicleManager, deliveryInterval, maxWeight, standardDeviation, lastTick, seed);
        }
    }
}
