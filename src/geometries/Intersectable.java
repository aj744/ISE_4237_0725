package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * interface for intersectable objects
 */
public interface Intersectable {
    /**
     * find all the intersection points
     *
     * @param ray the ray that comes through the objects
     * @return list of points where the ray intersect the object
     */
    List<Point> findIntersections(Ray ray);
}
