import java.util.Comparator;

/**
 * Created by shalev on 13/04/2017.
 */
public class PointsComparator implements Comparator<Point> {
    private boolean axis;

    public PointsComparator(boolean axis) {
        this.axis = axis;
    }

    private int getValue(Point point) {
        if (axis)
            return point.getX();
        else
            return point.getY();
    }

    public int compare(Point point1, Point point2) {
        return Integer.compare(getValue(point1), getValue(point2));
    }
}
