package projekt.delivery.routing;

import org.jetbrains.annotations.NotNull;
import projekt.base.Location;

import java.util.Comparator;
import java.util.Objects;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Represents a weighted edge in a graph.
 */
@SuppressWarnings("ClassCanBeRecord")
class EdgeImpl implements Region.Edge {

    private final Region region;
    private final String name;
    private final Location locationA;
    private final Location locationB;
    private final long duration;

    /**
     * Creates a new {@link EdgeImpl} instance.
     * @param region The {@link Region} this {@link EdgeImpl} belongs to.
     * @param name The name of this {@link EdgeImpl}.
     * @param locationA The start of this {@link EdgeImpl}.
     * @param locationB The end of this {@link EdgeImpl}.
     * @param duration The length of this {@link EdgeImpl}.
     */
    EdgeImpl(
        Region region,
        String name,
        Location locationA,
        Location locationB,
        long duration
    ) {
        this.region = region;
        this.name = name;
        // locations must be in ascending order
        if (locationA.compareTo(locationB) > 0) {
            throw new IllegalArgumentException(String.format("locationA %s must be <= locationB %s", locationA, locationB));
        }
        this.locationA = locationA;
        this.locationB = locationB;
        this.duration = duration;
    }

    /**
     * Returns the start of this {@link EdgeImpl}.
     * @return The start of this {@link EdgeImpl}.
     */
    public Location getLocationA() {
        return locationA;
    }

    /**
     * Returns the end of this {@link EdgeImpl}.
     * @return The end of this {@link EdgeImpl}.
     */
    public Location getLocationB() {
        return locationB;
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
    public long getDuration() {
        return duration;
    }

    @Override
    /**
     * returns the node of the location A of region
     * @return node of location A
     */
    public Region.Node getNodeA() {

        return region.getNode(locationA);
    }

    @Override
    /**
     * @User Kristina Shigabutdinova
     *
     * returns the node of the location B of  region
     * @return node of location B
     */
    public Region.Node getNodeB() {
        return region.getNode(locationB);
    }

    @Override
    /**
     * @User Kristina Shigabutdinova
     *
     * uses comparator to compare the actual edge to parameter and returns the result as int number
     *
     * @param o the edge to compare to
     * @return result of comparison as number
     */
    public int compareTo(Region.@NotNull Edge o) {
        Comparator<Region.Edge> compNodeA = Comparator.comparing(Region.Edge::getNodeA).thenComparing((Region.Edge::getNodeB));

        return compNodeA.compare(getNodeA().getEdge(getNodeB()), o);
    }
    @Override
    /**
     * @User Kristina Shigabutdinova
     * checks if the given Object is to this Object equal
     *
     * @param o the object to compare to
     * @return true if the Objects are equal
     *         false if not
     */
    public boolean equals(Object o) {
        if(o == null)
            return false;
        else if(!(o instanceof EdgeImpl))
            return false;
        else if(this == o)
            return true;
        else {

            return (Objects.equals(this.name, ((EdgeImpl) o).name)) &&
                Objects.equals(this.locationA, ((EdgeImpl) o).locationA) &&
                Objects.equals(this.locationB, ((EdgeImpl) o).locationB) &&
                Objects.equals(this.duration, ((EdgeImpl) o).duration);

        }
    }

    @Override
    /**
     * @User Kristina Shigabutdinova
     *
     * hashes name, location A, location B, duration
     */
    public int hashCode() {

        return Objects.hash(name, locationA, locationB, duration);
    }

    @Override
    /**
     * @User Kristina Shigabutdinova
     *
     * returns String with values of name, locationA, locationB, duration
     *
     * @return String with values of name, locationA, locationB, duration
     */
    public String toString() {

        return "EdgeImpl(name='%s', locationA='%s', locationB='%s', duration='%s')"+name+locationA+locationB+duration;
    }
}
