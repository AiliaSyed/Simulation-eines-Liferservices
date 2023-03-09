package projekt.delivery.service;

import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A very simple delivery service that distributes orders to compatible vehicles in a FIFO manner.
 */
public class BasicDeliveryService extends AbstractDeliveryService {

    // List of orders that have not yet been loaded onto delivery vehicles
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public BasicDeliveryService(
        VehicleManager vehicleManager
    ) {
        super(vehicleManager);
    }


    /**
     * This method gets the current tick of the simulation and the newly added orders. It manages all the vehicles to
     * restaurants that next to them are (may to be changed)
     * @param currentTick The tick to execute.
     * @param newOrders All new {@link ConfirmedOrder}s that have been ordered during the last tick.
     * @return r The list of all the movements of vehicles
     */

    @Override
    protected List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {

        vehicleManager.tick(currentTick);
        List<Event> r = new ArrayList<>();

        r.addAll(vehicleManager.tick(currentTick));

        pendingOrders.addAll(newOrders);

        pendingOrders.sort(Comparator.comparingLong(o -> o.getDeliveryInterval().start()));

        //managing orders to restaurants
        for (VehicleManager.OccupiedRestaurant restaurant : vehicleManager.getOccupiedRestaurants()) {
            Collection<Vehicle> vehicles = restaurant.getVehicles();
            for (Vehicle vehicle : vehicles) {
                while (vehicle.getCapacity() > 0 && !pendingOrders.isEmpty()) {
                    pendingOrders.remove(0);
                }

            }

        }
            return r;
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return pendingOrders;
    }

    @Override
    public void reset() {
        super.reset();
        pendingOrders.clear();
    }

    public interface Factory extends DeliveryService.Factory {

        BasicDeliveryService create(VehicleManager vehicleManager);
    }
}







































































































