import java.util.Comparator;

public class PointsComparator implements Comparator<Point> {
    private boolean axis;

    public PointsComparator(boolean axis) {
        this.axis = axis;
    }

    // get point value by @axis
    private int getValue(Point point) {
        if (axis)
            return point.getX();
        else
            return point.getY();
    }

    // compare points by axis value
    public int compare(Point point1, Point point2) {
        return Integer.compare(getValue(point1), getValue(point2));
    }
}
