package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.Collection;
import java.util.HashSet;
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
        if (!region.getNodes().contains(this) || !region.getNodes().contains(other)) {
            return null;
        }

        for (Region.Edge edge : region.getEdges()) {
            if (edge.getNodeA().equals(this) && edge.getNodeB().equals(other)) {
                return edge;
            }
            if (edge.getNodeA().equals(other) && edge.getNodeB().equals(this)) {
                return edge;
            }
        }

        return null;
    } // TODO: H3.1 - remove if implemented

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        Set<Region.Node> adjacentNodes = new HashSet<Region.Node>();
        Collection<Region.Edge> allEdges = region.getEdges();
        for(Region.Edge edge : allEdges){
            if(edge.getNodeA() == region.getNode(location))
                adjacentNodes.add(edge.getNodeB());
            if(edge.getNodeB() == region.getNode(location))
                adjacentNodes.add(edge.getNodeA());
        }
        return adjacentNodes;
         // TODO: H3.2 - remove if implemented
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {
        Set<Region.Edge> adjacentEdges = new HashSet<Region.Edge>();
        Collection<Region.Edge> allEdges = region.getEdges();
        for(Region.Edge edge : allEdges){
            if(edge.getNodeA() == region.getNode(location))
                adjacentEdges.add(edge);
            if(edge.getNodeB() == region.getNode(location))
                adjacentEdges.add(edge);
        }
        return adjacentEdges;
        // TODO: H3.3 - remove if implemented
    }

    @Override
    public int compareTo(Region.Node o) {
         return this.getLocation().compareTo(o.getLocation());// TODO: H3.4 - remove if implemented
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof NodeImpl))
            return false;
        if(o == this)
            return true;
        return (Objects.equals(this.name, ((NodeImpl) o).name)
            && Objects.equals(this.location, ((NodeImpl) o).location)
            && Objects.equals(this.connections, ((NodeImpl) o).connections));
        // TODO: H3.5 - remove if implemented
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, connections); // TODO: H3.6 - remove if implemented
    }

    @Override
    public String toString() {
        return "NodeImpl(name='"+ name + "', location='" + location +"', connections='" + connections +"')";
        // TODO: H3.7 - remove if implemented
    }
}
