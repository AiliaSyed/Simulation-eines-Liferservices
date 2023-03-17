package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import javax.swing.text.Position;
import java.util.*;
import java.util.function.BiConsumer;

import static org.tudalgo.algoutils.student.Student.crash;

class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<ConfirmedOrder> orders = new ArrayList<>();
    private final VehicleManagerImpl vehicleManager;
    private final Deque<PathImpl> moveQueue = new LinkedList<>();
    private final VehicleManager.OccupiedRestaurant startingNode;
    private AbstractOccupied<?> occupied;

    public VehicleImpl(
        int id,
        double capacity,
        VehicleManagerImpl vehicleManager,
        VehicleManager.OccupiedRestaurant startingNode) {
        this.id = id;
        this.capacity = capacity;
        this.occupied = (AbstractOccupied<?>) startingNode;
        this.vehicleManager = vehicleManager;
        this.startingNode = startingNode;
    }

    @Override
    public VehicleManager.Occupied<?> getOccupied() {
        return occupied;
    }

    @Override
    public @Nullable VehicleManager.Occupied<?> getPreviousOccupied() {
        AbstractOccupied.VehicleStats stats = occupied.vehicles.get(this);
        return stats == null ? null : stats.previous;
    }

    @Override
    public List<? extends Path> getPaths() {
        return new LinkedList<>(moveQueue);
    }

    void setOccupied(AbstractOccupied<?> occupied) {
        this.occupied = occupied;
    }
    public Region.Node getCurrentNode() {
        if (!moveQueue.isEmpty()) {
            return moveQueue.getLast().nodes().getLast();
        }
        return startingNode.getComponent();
    }

    /**
     * This method returns the next node that the vehicle will move to
     * @User Ailia Syed
     * @return the next connected node of the current node
     */
    private Region.Node getNextNode() {
        Region.Node currentNode = getCurrentNode();
        Set<Region.Node> connectedNodes = currentNode.getAdjacentNodes();
        for (Region.Node node : connectedNodes) {
            if (!node.equals(currentNode)) {
                return node;
            }
        }
        // If no next node is found, return null
        return null;
    }

    /**
     *
     * @User Ailia Syed
     * @param node
     * @param arrivalAction
     */
    @Override
    public void moveDirect(Region.Node node, BiConsumer<? super Vehicle, Long> arrivalAction) {
        if (node.equals(this.getCurrentNode())) {
            throw new IllegalArgumentException("Vehicle 1 cannot move to own node NodeImpl(name='E', location='(4,4)', connections='[(3,3)]')");
        }

        // If vehicle is currently on an edge, add the next node to moveQueue before moving to the target node
        if (!moveQueue.isEmpty() && !vehicleManager.getRegion().getNodes().contains(moveQueue.getLast().nodes().getLast())) {
            Region.Node nextNode = getNextNode();
            if (nextNode != null) {
                moveQueue.add(new PathImpl(vehicleManager.getPathCalculator().getPath(getCurrentNode(), nextNode), arrivalAction));
            }
        }

        moveQueued(node, arrivalAction);
    }
    //TODO H5.4 - remove if implemented

    /**
     * moves the queue to the next destination for the vehicle
     * @User Ailia Syed
     * @param node
     * @param arrivalAction
     */
    @Override
    public void moveQueued(Region.Node node, BiConsumer<? super Vehicle, Long> arrivalAction) {
        // Check if the given node is valid
        if (!moveQueue.isEmpty() && !node.equals(moveQueue.getLast().nodes().getLast())) {
            throw new IllegalArgumentException("Vehicle 1 cannot move to own node NodeImpl(name='E', location='(4,4)', connections='[(3,3)]')");
        }
        moveQueue.add(new PathImpl(vehicleManager.getPathCalculator().getPath(startingNode.getVehicleManager().getRegion().getNode(this.getCurrentNode().getLocation()), node), arrivalAction));
    } //TODO H5.3 - remove if implemented


    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public VehicleManager.Occupied<? extends Region.Node> getStartingNode() {
        return startingNode;
    }

    @Override
    public Collection<ConfirmedOrder> getOrders() {
        return orders;
    }

    @Override
    public void reset() {
        occupied = (AbstractOccupied<?>) startingNode;
        moveQueue.clear();
        orders.clear();
    }

    private void checkMoveToNode(Region.Node node) {
        if (occupied.component.equals(node) && moveQueue.isEmpty()) {
            throw new IllegalArgumentException("Vehicle " + getId() + " cannot move to own node " + node);
        }
    }

    void move(long currentTick) {
        final Region region = vehicleManager.getRegion();
        if (moveQueue.isEmpty()) {
            return;
        }
        final PathImpl path = moveQueue.peek();
        if (path.nodes().isEmpty()) {
            moveQueue.pop();
            final @Nullable BiConsumer<? super Vehicle, Long> action = path.arrivalAction();
            if (action == null) {
                move(currentTick);
            } else {
                action.accept(this, currentTick);
            }
        } else {
            Region.Node next = path.nodes().peek();
            if (occupied instanceof OccupiedNodeImpl) {
                vehicleManager.getOccupied(region.getEdge(((OccupiedNodeImpl<?>) occupied).getComponent(), next)).addVehicle(this, currentTick);
            } else if (occupied instanceof OccupiedEdgeImpl) {
                vehicleManager.getOccupied(next).addVehicle(this, currentTick);
                path.nodes().pop();
            } else {
                throw new AssertionError("Component must be either node or component");
            }
        }
    }

    /**
     * checks if the weight of the new order exceeds the max weight of vehicle
     * @User Ailia Syed
     * @param order
     * @throws VehicleOverloadedException
     */
    void loadOrder(ConfirmedOrder order) throws VehicleOverloadedException {
        double currentLoad = orders.stream().mapToDouble(ConfirmedOrder::getWeight).sum();
        double newLoad = currentLoad + order.getWeight();
        if (newLoad > capacity) {
            throw new VehicleOverloadedException(this,newLoad);
        }
        orders.add(order);
        //TODO H5.2 - remove if implemented
    }

    /**
     * removes the given order from the main order
     * @User Ailia Syed
     * @param order
     */
    void unloadOrder(ConfirmedOrder order) {
        orders.remove(order);
    }
    //TODO H5.2 - remove if implemented

    @Override
    public int compareTo(Vehicle o) {
        return Integer.compare(getId(), o.getId());
    }

    @Override
    public String toString() {
        return "VehicleImpl("
            + "id=" + id
            + ", capacity=" + capacity
            + ", orders=" + orders
            + ", component=" + occupied.component
            + ')';
    }

    private record PathImpl(Deque<Region.Node> nodes, BiConsumer<? super Vehicle, Long> arrivalAction) implements Path {

    }
}
