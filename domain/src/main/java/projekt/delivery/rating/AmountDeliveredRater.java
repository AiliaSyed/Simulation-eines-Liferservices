package projekt.delivery.rating;

import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.simulation.Simulation;

import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Rates the observed {@link Simulation} based on the amount of delivered orders.<p>
 *
 * To create a new {@link AmountDeliveredRater} use {@code AmountDeliveredRater.Factory.builder()...build();}.
 */
public class AmountDeliveredRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.AMOUNT_DELIVERED;

    private final double factor;
    private int totalOrders;
    private int deliveredOrders;

    private AmountDeliveredRater(double factor) {
        this.factor = factor;
    }

    /**
     * This Method calculates the rating based on the number of deliveredOrders and totalOrders
     *
     * @return returns more than 0 if the proportion of orders shipped is sufficient, else returns 0
     */
    @Override
    public double getScore() {
        double undeliveredOrders = totalOrders - deliveredOrders;
         if (undeliveredOrders >= 0 && undeliveredOrders < (totalOrders * (1 - factor))){
             return (1 - undeliveredOrders /  (totalOrders * (1-factor)));
         }
         else{
            return 0;
        }

        // TODO: H8.1 - remove if implemented
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    /**
     * saves the number of totalOrders and undeliveredOrders
     *
     * @param events All {@link Event}s that occurred during the tick.
     * @param tick The executed tick.
     */
    @Override
    public void onTick(List<Event> events, long tick) {
        for(Event event : events) {
            if (event instanceof DeliverOrderEvent) {
                deliveredOrders++;
            } else if (event instanceof OrderReceivedEvent) {
                totalOrders++;
            }
        }
        // TODO: H8.1 - remove if implemented
    }

    /**
     * A {@link Rater.Factory} for creating a new {@link AmountDeliveredRater}.
     */
    public static class Factory implements Rater.Factory {

        public final double factor;

        private Factory(double factor) {
            this.factor = factor;
        }

        @Override
        public AmountDeliveredRater create() {
            return new AmountDeliveredRater(factor);
        }

        /**
         * Creates a new {@link AmountDeliveredRater.FactoryBuilder}.
         * @return The created {@link AmountDeliveredRater.FactoryBuilder}.
         */
        public static FactoryBuilder builder() {
            return new FactoryBuilder();
        }
    }

    /**
     * A {@link Rater.FactoryBuilder} form constructing a new {@link AmountDeliveredRater.Factory}.
     */
    public static class FactoryBuilder implements Rater.FactoryBuilder {

        public double factor = 0.99;

        private FactoryBuilder() {}

        @Override
        public Factory build() {
            return new Factory(factor);
        }

        public FactoryBuilder setFactor(double factor) {
            if (factor < 0 || factor > 1) {
                throw new IllegalArgumentException("factor must be between 0 and 1");
            }

            this.factor = factor;
            return this;
        }
    }
}
