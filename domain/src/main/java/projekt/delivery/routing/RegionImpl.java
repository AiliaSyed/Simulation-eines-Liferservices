package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

class RegionImpl implements Region {

    private final Map<Location, NodeImpl> nodes = new HashMap<>();
    private final Map<Location, Map<Location, EdgeImpl>> edges = new HashMap<>();
    private final List<EdgeImpl> allEdges = new ArrayList<>();
    private final DistanceCalculator distanceCalculator;

    /**
     * Creates a new, empty {@link RegionImpl} instance using a {@link EuclideanDistanceCalculator}.
     */
    public RegionImpl() {
        this(new EuclideanDistanceCalculator());
    }

    /**
     * Creates a new, empty {@link RegionImpl} instance using the given {@link DistanceCalculator}.
     */
    public RegionImpl(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    /**
     * @User Ailia Syed
     * @param location The {@link Location} of the returned {@link Region.Node}.
     * @return the node of a given location on the map
     */
    @Override
    public @Nullable Node getNode(Location location) {
        return nodes.get(location);
    }

    /**
     * @User Ailia Syed
     * @param locationA The first {@link Location}.
     * @param locationB The second {@link Location}.
     * @return the edge of two locations
     */
    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        EdgeImpl edge = null;
        Map<Location, EdgeImpl> edgesFromLocationA = edges.get(locationA);
        if (edgesFromLocationA != null) {
            edge = edgesFromLocationA.get(locationB);
        }
        if (edge == null) {
            Map<Location, EdgeImpl> edgesFromLocationB = edges.get(locationB);
            if (edgesFromLocationB != null) {
                edge = edgesFromLocationB.get(locationA);
            }
        }
        return edge;
    }

    /**
     * @User Ailia Syed
     * @return the collection of nodes
     */
    @Override
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
    }

    /**
     * @User Ailia Syed
     * @return the collection of allEdges
     */
    @Override
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(allEdges);
    }

    @Override
    public DistanceCalculator getDistanceCalculator() {
        return distanceCalculator;
    }

    /**
     * @User Kristina Shigabutdinova
     * Adds the given {@link NodeImpl} to this {@link RegionImpl}.
     * @param node the {@link NodeImpl} to add.
     */
    void putNode(NodeImpl node) {

        //if node not in Region then IllegalArgumentException
        if(!node.getRegion().equals(this)){
            throw new IllegalArgumentException("Node "+node.toString()+" has incorrect region");
        }
        //else put node to Map nodes
        else{
            nodes.put(node.getLocation(), node);
        }
    }

    /**
     * @User Kristina Shigabutdinova
     * Adds the given {@link EdgeImpl} to this {@link RegionImpl}.
     * @param edge the {@link EdgeImpl} to add.
     */
    void putEdge(EdgeImpl edge) {
        //if edge or one of its nodes NOT in Region then IllegalArgumentException

        if(!edge.getRegion().equals(this) || !edge.getNodeA().getRegion().equals(this) || !edge.getNodeB().getRegion().equals(this)){
            throw new IllegalArgumentException("Edge "+ edge +" has incorrect region");
        }
        //if node A or node B is null then IllegalArgumentException
        //(maybe in one if instead of two *see H2.4)
        else if(edge.getNodeA() == null){
            throw new IllegalArgumentException("Node {A,B} "+edge.getLocationA()+ " is not part of the region");
        }
        else if(edge.getNodeB() == null){
            throw new IllegalArgumentException("Node {A,B} "+edge.getLocationB()+ " is not part of the region");
        }

        //create the Map of edges
        Map<Location, EdgeImpl> nodeAEdges = this.edges.get(edge.getNodeA().getLocation());
        Map<Location, EdgeImpl> nodeBEdges = this.edges.get(edge.getNodeB().getLocation());

        //adding to Map of edges and to allEdges
        nodeAEdges.put(edge.getNodeB().getLocation(), edge);
        nodeBEdges.put(edge.getNodeA().getLocation(), edge);
        allEdges.add(edge);

        //Sort allEdges based on the natural order of EdgeImpl
        allEdges.sort(Comparator.naturalOrder());
    }

    /**
     * @User Ailia Syed
     * @param o
     * @return if the param o equals to this or this.nodes & thus.edges
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegionImpl)) {
            return false;
        }
        RegionImpl other = (RegionImpl) o;
        return Objects.equals(this.nodes, other.nodes) && Objects.equals(this.edges, other.edges);
    }

    /**
     * @User Ailia Syed
     * @return the set of hashcodes of nodes and allEdges
     */
    @Override
    public int hashCode() {
        return Objects.hash(nodes, allEdges);
    }
}
