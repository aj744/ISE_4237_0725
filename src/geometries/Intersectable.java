package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;
import java.util.Objects;

/**
 * interface for intersectable objects
 */
public abstract class Intersectable {

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Intersection intersection)) return false; // Ensures type safety
            return geometry == ((Intersection) o).geometry && Objects.equals(o, this.point);
        }
        @Override
        public String toString() {
            return "Geometry: " + geometry + point.toString();
        }
    }


    /**
     * find all the intersection points
     *
     * @param ray the ray that comes through the objects
     * @return list of points where the ray intersect the object
     */
    public List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null
                : list.stream().map(intersection -> intersection.point).toList();
    }

    public final List<Intersection> calculateIntersections(Ray ray){
        return calculateIntersectionsHelper(ray);
    }

    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

}
