package projekt.delivery.routing;

import projekt.base.Location;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.SpawnEvent;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

class VehicleManagerImpl implements VehicleManager {

    final Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> occupiedNodes;
    final Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges;
    private final Region region;
    private final PathCalculator pathCalculator;
    private final List<VehicleImpl> vehiclesToSpawn = new ArrayList<>();
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final EventBus eventBus = new EventBus();

    VehicleManagerImpl(
        Region region,
        PathCalculator pathCalculator
    ) {
        this.region = region;
        this.pathCalculator = pathCalculator;
        occupiedNodes = toOccupiedNodes(region.getNodes());
        occupiedEdges = toOccupiedEdges(region.getEdges());
    }

    /**
     * It creates a map with all nodes and their occupied nodes
     * @User Ailia Syed
     * @param nodes
     * @return
     */
    private Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> toOccupiedNodes(Collection<Region.Node> nodes) {
        Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> finalMap = new HashMap<>();
        for(Region.Node currentNode : nodes){
            if(currentNode instanceof Region.Restaurant){
                OccupiedRestaurantImpl newNode = new OccupiedRestaurantImpl((Region.Restaurant) currentNode, this);
                finalMap.put(currentNode,newNode);
            }
            else if(currentNode instanceof Region.Neighborhood){
                OccupiedNeighborhoodImpl newNode = new OccupiedNeighborhoodImpl((Region.Neighborhood) currentNode, this);
                finalMap.put(currentNode,newNode);
            }
            else {
                OccupiedNodeImpl newNode = new OccupiedNodeImpl(currentNode, this);
                finalMap.put(currentNode, newNode);
            }
        }
        return Collections.unmodifiableMap(finalMap);
    } //TODO H6.1 - remove if implemented

    /**
     * It creates a map with all edges and their occupied edges
     * @User Ailia Syed
     * @param edges
     * @return unmodifiable map with all edges and their occupied edges
     */
    private Map<Region.Edge, OccupiedEdgeImpl> toOccupiedEdges(Collection<Region.Edge> edges) {
        Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges = new HashMap<>();
        for (Region.Edge edge : edges) {
            OccupiedEdgeImpl occupiedEdge = new OccupiedEdgeImpl(edge, this);
            occupiedEdges.put(edge, occupiedEdge);
        }
        return Collections.unmodifiableMap(occupiedEdges);
    } //TODO H6.1 - remove if implemented

    /**
     *
     * @User Ailia Syed
     * @return unmodified set with all attributes of occupiedNodes und occupiedEdges
     */
    private Set<AbstractOccupied<?>> getAllOccupied() {
        Set<AbstractOccupied<?>> allOccupied = new HashSet<>();
        allOccupied.addAll(occupiedNodes.values());
        allOccupied.addAll(occupiedEdges.values());
        return Collections.unmodifiableSet(allOccupied);
    }//TODO H6.2 - remove if implemented

    private OccupiedNodeImpl<? extends Region.Node> getOccupiedNode(Location location) {
        return occupiedNodes.values().stream()
            .filter(node -> node.getComponent().getLocation().equals(location))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find node with given predicate"));
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public PathCalculator getPathCalculator() {
        return pathCalculator;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return unmodifiableVehicles;
    }

    @Override
    public Collection<Vehicle> getAllVehicles() {
        Collection<Vehicle> allVehicles = new ArrayList<>(getVehicles());
        allVehicles.addAll(vehiclesToSpawn);
        return allVehicles;
    }

    @Override
    public <C extends Region.Component<C>> AbstractOccupied<C> getOccupied(C component) {
        Objects.requireNonNull(component, "Component is null!");
        if (component instanceof Region.Node) {
            Collection<Occupied<? extends Region.Node>> occupiedNodes = getOccupiedNodes();
            for (Occupied<? extends Region.Node> node : occupiedNodes) {
                if (node.getComponent().equals(component)) {
                    return (AbstractOccupied<C>) node;
                }
            }
            throw new IllegalArgumentException("Could not find occupied node for " + component.toString());
        } else if (component instanceof Region.Edge) {
            Collection<Occupied<? extends Region.Edge>> occupiedEdges = getOccupiedEdges();
            for (Occupied<? extends Region.Edge> edge : occupiedEdges) {
                if (edge.getComponent().equals(component)) {
                    return (AbstractOccupied<C>) edge;
                }
            }
            throw new IllegalArgumentException("Could not find occupied edge for " + component.toString());
        } else {
            throw new IllegalArgumentException("Component is not of recognized subtype: " + component.getClass().getName());
        }
    }// TODO: H6.3 - remove if implemented


    @Override
    public List<OccupiedRestaurant> getOccupiedRestaurants() {
        return occupiedNodes.values().stream()
            .filter(OccupiedRestaurant.class::isInstance)
            .map(OccupiedRestaurant.class::cast)
            .toList();
    }

    @Override
    public OccupiedRestaurant getOccupiedRestaurant(Region.Node node) {
        if(node == null)
        {
            throw new NullPointerException("Node is null!");
        }
        if(!occupiedNodes.containsKey(node) || !(occupiedNodes.get(node) instanceof OccupiedRestaurant)) {
            throw new IllegalArgumentException("Node " + node.toString() + " is not a restaurant");
        }
        return (OccupiedRestaurant) occupiedNodes.get(node);
    }//TODO H6.4 - remove if implemented

    @Override
    public Collection<OccupiedNeighborhood> getOccupiedNeighborhoods() {
        return occupiedNodes.values().stream()
            .filter(OccupiedNeighborhood.class::isInstance)
            .map(OccupiedNeighborhood.class::cast)
            .toList();
    }

    @Override
    public OccupiedNeighborhood getOccupiedNeighborhood(Region.Node node) {
        if(node == null)
        {
            throw new NullPointerException("Node is null!");
        }
        if(!occupiedNodes.containsKey(node) || !(occupiedNodes.get(node) instanceof OccupiedNeighborhood)) {
            throw new IllegalArgumentException("Node " + node.toString() + " is not a neighborhood");
        }
        return (OccupiedNeighborhood) occupiedNodes.get(node);
    } //TODO H6.4 - remove if implemented

    @Override
    public Collection<Occupied<? extends Region.Node>> getOccupiedNodes() {
        return Collections.unmodifiableCollection(occupiedNodes.values());
    }

    @Override
    public Collection<Occupied<? extends Region.Edge>> getOccupiedEdges() {
        return Collections.unmodifiableCollection(occupiedEdges.values());
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public List<Event> tick(long currentTick) {
        for (VehicleImpl vehicle : vehiclesToSpawn) {
            spawnVehicle(vehicle, currentTick);
        }
        vehiclesToSpawn.clear();
        // It is important that nodes are ticked before edges
        // This only works because edge ticking is idempotent
        // Otherwise, there may be two state changes in a single tick.
        // For example, a node tick may move a vehicle onto an edge.
        // Ticking this edge afterwards does not move the vehicle further along the edge
        // compared to a vehicle already on the edge.
        occupiedNodes.values().forEach(occupiedNode -> occupiedNode.tick(currentTick));
        occupiedEdges.values().forEach(occupiedEdge -> occupiedEdge.tick(currentTick));
        return eventBus.popEvents(currentTick);
    }

    public void reset() {
        for (AbstractOccupied<?> occupied : getAllOccupied()) {
            occupied.reset();
        }

        for (Vehicle vehicle : getAllVehicles()) {
            vehicle.reset();
        }

        vehiclesToSpawn.addAll(getVehicles().stream()
            .map(VehicleImpl.class::cast)
            .toList());

        vehicles.clear();
    }

    @SuppressWarnings("UnusedReturnValue")
    Vehicle addVehicle(
        Location startingLocation,
        double capacity
    ) {
        OccupiedNodeImpl<? extends Region.Node> occupied = getOccupiedNode(startingLocation);

        if (!(occupied instanceof OccupiedRestaurant)) {
            throw new IllegalArgumentException("Vehicles can only spawn at restaurants!");
        }

        final VehicleImpl vehicle = new VehicleImpl(
            vehicles.size() + vehiclesToSpawn.size(),
            capacity,
            this,
            (OccupiedRestaurant) occupied);
        vehiclesToSpawn.add(vehicle);
        vehicle.setOccupied(occupied);
        return vehicle;
    }

    private void spawnVehicle(VehicleImpl vehicle, long currentTick) {
        vehicles.add(vehicle);
        OccupiedRestaurantImpl warehouse = (OccupiedRestaurantImpl) vehicle.getOccupied();
        warehouse.vehicles.put(vehicle, new AbstractOccupied.VehicleStats(currentTick, null));
        getEventBus().queuePost(SpawnEvent.of(currentTick, vehicle, warehouse.getComponent()));
    }
}
