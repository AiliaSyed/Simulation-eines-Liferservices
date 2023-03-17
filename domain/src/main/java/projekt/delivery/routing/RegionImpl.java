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

        //TODO H2.1 - remove if implemented
    }

    /**
     * @User Ailia Syed
     * @param locationA The first {@link Location}.
     * @param locationB The second {@link Location}.
     * @return the edge of two locations
     */
    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        Map<Location,EdgeImpl> tmpEdge = locationA.compareTo(locationB) <= 0? edges.get(locationA):edges.get(locationB);
        return tmpEdge == null? null : (locationA.compareTo(locationB)) <= 0? tmpEdge.get(locationB):tmpEdge.get(locationA);

        /*NodeImpl nodeA = nodes.get(locationA);
        NodeImpl nodeB = nodes.get(locationB);
        if (nodeA == null || nodeB == null) {
            return null;
        }
        if(edges.get(nodeA) == null
                || edges.get(nodeA).get(nodeB) == null){
            return null;
        }

        // Check if edge exists between the two nodes without suspicious calls
        Edge edge = edges.get(nodeA).get(nodeB);
        if (edge == null) {
            if(edges.get(nodeB) == null
                    || edges.get(nodeB).get(nodeA) == null){
                return null;
            }
            // Check if edge exists in reverse order
            edge = edges.get(nodeB).get(nodeA);
        }
        return edge;
         */

        //TODO: H2.3 - remove if implemented


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
        /**
         * put given edge to edges and allEdges
         * if edge is not in Region this then IllegalArgumentException with message "Edge "+edge.toString()+" has incorrect region"
         * if edge.getNodeA or edge.getNodeB is null then IllegalArgumentException with message "Node{A,B} " + location.toString() + " is not part of the region"
         * edges should be sorted. So using the constructor of EdgeImpl you need to check if
         * nodeA.getLocation().compareTo(nodeB.getLocation()) <= 0. If not, then swap the nodes.
         */
       if(!this.equals(edge.getRegion())){
           throw new IllegalArgumentException("Edge " + edge.toString() + " has incorrect region");
       }else if (edge.getNodeA() == null){
           throw new IllegalArgumentException("NodeA " + edge.getLocationA().toString() + " is not part of the region");

       } else if (edge.getNodeB() == null) {
           throw new IllegalArgumentException("NodeB " + edge.getLocationB().toString() + " is not part of the region");
       }else{
           allEdges.add(edge);
           Map<Location,EdgeImpl> insideLocation = Map.of(edge.getLocationA().compareTo(edge.getLocationB()) <= 0? edge.getLocationB() : edge.getLocationA(),edge);
           edges.put(edge.getLocationA().compareTo(edge.getLocationB()) <= 0 ? edge.getLocationA() : edge.getLocationB(),insideLocation);
       }

           /*
            if(!edge.getRegion().equals(this)){
                throw new IllegalArgumentException("Edge "+edge.toString()+" has incorrect region");
            }
            if(edge.getNodeA() == null ){
                throw new IllegalArgumentException("NodeA " + edge.getNodeA().getLocation().toString() + " is not part of the region");
            }
            if(edge.getNodeB() == null){
                throw new IllegalArgumentException("NodeB " + edge.getNodeB().getLocation().toString() + " is not part of the region");
            }
            if(edge.getNodeA().getLocation().compareTo(edge.getNodeB().getLocation()) <= 0){
                // Add edge to the Map of edges
                edges.computeIfAbsent(edge.getNodeA().getLocation(), k -> new HashMap<>()).put(edge.getNodeB().getLocation(), edge);

            }
            else{
                edges.computeIfAbsent(edge.getNodeB().getLocation(), k -> new HashMap<>()).put(edge.getNodeA().getLocation(), edge);
            }
            allEdges.add(edge);
            */
//TODO H2.4 - remove if implemented
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
