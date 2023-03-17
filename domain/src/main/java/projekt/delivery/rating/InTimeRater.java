package projekt.delivery.rating;

import projekt.base.TickInterval;
import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.event.*;

import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Rates the observed {@link Simulation} based on the punctuality of the orders.<p>
 *
 * To create a new {@link InTimeRater} use {@code InTimeRater.Factory.builder()...build();}.
 */
public class InTimeRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.IN_TIME;

    private final long ignoredTicksOff;
    private final long maxTicksOff;
    private long actualTotalTicksOff = 0;
    private long maxTotalTicksOff = 0;

    /**
     * Creates a new {@link InTimeRater} instance.
     * @param ignoredTicksOff The amount of ticks this {@link InTimeRater} ignores when dealing with an {@link ConfirmedOrder} that didn't get delivered in time.
     * @param maxTicksOff The maximum amount of ticks too late/early this {@link InTimeRater} considers.
     */
    private InTimeRater(long ignoredTicksOff, long maxTicksOff) {
        if (ignoredTicksOff < 0) throw new IllegalArgumentException(String.valueOf(ignoredTicksOff));
        if (maxTicksOff <= 0) throw new IllegalArgumentException(String.valueOf(maxTicksOff));

        this.ignoredTicksOff = ignoredTicksOff;
        this.maxTicksOff = maxTicksOff;
    }

    /**
     * This Method calculates the score of the Ticks
     * This Method calculates the rating based on
     * @return score of the Ticks
     */
    @Override
    public double getScore() {
        if (maxTotalTicksOff == 0) {
            return 0;
        }
        else {
            double score = 1 - (double) actualTotalTicksOff / maxTicksOff;
            return Math.max(0, score);
        }

        // TODO: H8.2 - remove if implemented
    }

    /**
     *
     * @param events All {@link Event}s that occurred during the tick.
     * @param tick The executed tick.
     */
    @Override
    public void onTick(List<Event> events, long tick) {
        for (Event event : events) {
            if (event instanceof DeliverOrderEvent deliverEvent) {
                ConfirmedOrder order = deliverEvent.getOrder();
                TickInterval deliveryInterval = order.getDeliveryInterval();
                long actualTicksOff = tick - deliveryInterval.end();
                if (actualTicksOff > ignoredTicksOff) {
                    actualTicksOff -= ignoredTicksOff;
                    if (actualTicksOff > maxTicksOff) {
                        actualTicksOff = maxTicksOff;
                    }
                    actualTotalTicksOff += actualTicksOff;
                }
                maxTotalTicksOff += maxTicksOff;
            } else if (event instanceof OrderReceivedEvent orderReceivedEvent) {
                ConfirmedOrder order = orderReceivedEvent.getOrder();
                TickInterval deliveryInterval = order.getDeliveryInterval();
                long maxTicksOffForOrder = Math.min(deliveryInterval.getDuration(), maxTicksOff + ignoredTicksOff);
                maxTotalTicksOff += maxTicksOffForOrder;
                if (deliveryInterval.end() < tick) {
                    actualTotalTicksOff += maxTicksOffForOrder;
                } else if (deliveryInterval.start() < tick) {
                    actualTotalTicksOff += tick - deliveryInterval.start();
                }
            }
        }
        // TODO: H8.2 - remove if implemented
    }

    /**
     * A {@link Rater.Factory} for creating a new {@link InTimeRater}.
     */
    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    public static class Factory implements Rater.Factory {

        public final long ignoredTicksOff;
        public final long maxTicksOff;

        private Factory(long ignoredTicksOff, long maxTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            this.maxTicksOff = maxTicksOff;
        }

        @Override
        public InTimeRater create() {
            return new InTimeRater(ignoredTicksOff, maxTicksOff);
        }

        /**
         * Creates a new {@link InTimeRater.FactoryBuilder}.
         * @return The created {@link InTimeRater.FactoryBuilder}.
         */
        public static FactoryBuilder builder() {
            return new FactoryBuilder();
        }
    }

    /**
     * A {@link Rater.FactoryBuilder} form constructing a new {@link InTimeRater.Factory}.
     */
    public static class FactoryBuilder implements Rater.FactoryBuilder {

        public long ignoredTicksOff = 5;
        public long maxTicksOff = 25;

        private FactoryBuilder() {}

        public FactoryBuilder setIgnoredTicksOff(long ignoredTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            return this;
        }

        public FactoryBuilder setMaxTicksOff(long maxTicksOff) {
            this.maxTicksOff = maxTicksOff;
            return this;
        }

        @Override
        public Factory build() {
            return new Factory(ignoredTicksOff, maxTicksOff);
        }
    }
}
