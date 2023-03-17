package projekt.delivery.service;

import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import static org.tudalgo.algoutils.student.Student.crash;

public class OurDeliveryService extends AbstractDeliveryService {

    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();
    private long currentTime;

    public OurDeliveryService(VehicleManager vehicleManager) {
        super(vehicleManager);
    }
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


            //moving to each point
            for (Vehicle vehicle : vehicleManager.getVehicles()) {
                VehicleManager.OccupiedNeighborhood neighborhood = vehicleManager.getOccupiedNeighborhood(vehicle.getStartingNode().getComponent());
                for(ConfirmedOrder order : newOrders) {

                    neighborhood.deliverOrder(vehicle, order, currentTick);

                }
                if(vehicle.getOrders().isEmpty()){
                    vehicle.moveQueued(neighborhood.getComponent());
                }
            }
        }



        return r;
    }//TODO H9.2 -    remove if implemented
    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return pendingOrders;
    }

    @Override
    public void reset() {
        super.reset();
        pendingOrders.clear();
        currentTime = 0;
    }

    public interface Factory extends DeliveryService.Factory {

        OurDeliveryService create(VehicleManager vehicleManager);
    }
}
