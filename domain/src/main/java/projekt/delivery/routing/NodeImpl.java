package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.Objects;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

class NodeImpl implements Region.Node {

    protected final Set<Location> connections;
    protected final Region region;
    protected final String name;
    protected final Location location;

    /**
     * Creates a new {@link NodeImpl} instance.
     * @param region The {@link Region} this {@link NodeImpl} belongs to.
     * @param name The name of this {@link NodeImpl}.
     * @param location The {@link Location} of this {@link EdgeImpl}.
     * @param connections All {@link Location}s this {@link NeighborhoodImpl} has an {@link Region.Edge} to.
     */
    NodeImpl(
        Region region,
        String name,
        Location location,
        Set<Location> connections
    ) {
        this.region = region;
        this.name = name;
        this.location = location;
        this.connections = connections;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public Set<Location> getConnections() {
        return connections;
    }

    @Override
    public @Nullable Region.Edge getEdge(Region.Node other) {


        if (region.getNode(location).getEdge(other) != null){
            return region.getNode(location).getEdge(other);
        }
        return null;

        // TODO: H3.1 - remove if implemented

    }

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        Set<Region.Node> adjacentNodes = null;
        for(Region.Node node : region.getNodes()){
            if (region.getNode(location).getEdge(node) != null) {
                adjacentNodes.add(node);
            }
        }
        if (adjacentNodes.isEmpty()) {
            adjacentNodes.add(this);
        }
        return adjacentNodes;

        // TODO: H3.2 - remove if implemented
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {

        Set<Region.Edge> adjacentEdges = null;
        adjacentEdges.addAll(region.getEdges());
        for (Region.Edge edge : region.getEdges()) {
            if(region.getNode(location).getEdge((Region.Node) edge) != null){
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;

        // TODO: H3.3 - remove if implemented
    }

    @Override
    public int compareTo(Region.Node o) {
        return location.compareTo(o.getLocation());
         // TODO: H3.4 - remove if implemented
    }

    @Override
    public boolean equals(Object o) {
        NodeImpl other = (NodeImpl) o;
        if (o == null || !(o instanceof NodeImpl)){
            return false;
        }

        if ( o == this ||
            (Objects.equals(this.name, other. name) && Objects.equals( this.location, other.location) && Objects.equals(this.connections, other.connections))){
            return true;
        }
        else
            return false;

        // TODO: H3.5 - remove if implemented
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, connections);

        // TODO: H3.6 - remove if implemented
    }

    @Override
    public String toString() {
        return "NodeImpl(name=" +getName()+ ", location=" +getLocation()+ ", connections= " +getConnections()+ ")";
        // TODO: H3.7 - remove if implemented
    }
}
