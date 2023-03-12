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
        return nodes.get(location);//TODO H2.1 - remove if implemented
    }

    /**
     * @User Ailia Syed
     * @param locationA The first {@link Location}.
     * @param locationB The second {@link Location}.
     * @return the edge of two locations
     */
    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        NodeImpl nodeA = nodes.get(locationA);
        NodeImpl nodeB = nodes.get(locationB);
        if (nodeA == null || nodeB == null) {
            return null;
        }

        // Check if edge exists between the two nodes
        Edge edge = edges.get(nodeA).get(nodeB);
        if (edge == null) {
            // Check if edge exists in reverse order
            edge = edges.get(nodeB).get(nodeA);
        }
        return edge; //TODO: H2.3 - remove if implemented
    }
    private @Nullable EdgeImpl getExistingEdge(Location locationA, Location locationB) {
        Map<Location, EdgeImpl> edgesFromLocationA = edges.get(locationA);
        if (edgesFromLocationA != null) {
            return edgesFromLocationA.get(locationB);
        }
        return null;
    }

    /**
     * @User Ailia Syed
     * @return the collection of nodes
     */
    @Override
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.values());
        //TODO H2.5 - remove if implemented
    }

    /**
     * @User Ailia Syed
     * @return the collection of allEdges
     */
    @Override
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableList(allEdges);//TODO H2.5 - remove if implemented
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
        //TODO H2.2 - remove if implemented
    }

    /**
     * @User Kristina Shigabutdinova
     * Adds the given {@link EdgeImpl} to this {@link RegionImpl}.
     * @param edge the {@link EdgeImpl} to add.
     */
    void putEdge(EdgeImpl edge) {
        NodeImpl nodeA = (NodeImpl) edge.getNodeA();
        NodeImpl nodeB = (NodeImpl) edge.getNodeB();

        // Throw IllegalArgumentException if edge or one of its nodes NOT in this Region
        if (!edge.getRegion().equals(this) || nodeA.getRegion() != this || nodeB.getRegion() != this) {
            throw new IllegalArgumentException("Edge " + edge + " has incorrect region");
        }

        // Throw IllegalArgumentException if node A or node B is null
        if (nodeA == null) {
            throw new IllegalArgumentException("Node A is not part of the region");
        }
        if (nodeB == null) {
            throw new IllegalArgumentException("Node B is not part of the region");
        }

        // Add edge to the Map of edges
        edges.computeIfAbsent(nodeA.getLocation(), k -> new HashMap<>()).put(nodeB.getLocation(), edge);
        edges.computeIfAbsent(nodeB.getLocation(), k -> new HashMap<>()).put(nodeA.getLocation(), edge);

        // Add edge to the List of allEdges
        allEdges.add(edge);

        // Sort allEdges based on the natural order of EdgeImpl
        allEdges.sort(Comparator.naturalOrder()); //TODO H2.4 - remove if implemented
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
        if (o == null || !(o instanceof RegionImpl)) {
            return false;
        }
        RegionImpl other = (RegionImpl) o;
        return Objects.equals(this.nodes, other.nodes) && Objects.equals(this.edges, other.edges);
        //TODO H2.6 - remove if implemented
    }

    /**
     * @User Ailia Syed
     * @return the set of hashcodes of nodes and allEdges
     */
    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }
    //TODO H2.7 - remove if implemented
}
