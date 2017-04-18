import java.util.Arrays;

public class DataStructure implements DT {
    private Container firstByX;
    private Container lastByX;
    private Container firstByY;
    private Container lastByY;
    private int size;

    //////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
    public DataStructure() {
        size = 0;
        firstByX = null;
        lastByX = null;
        firstByY = null;
        lastByY = null;
    }

    public Container getFirstByAxis(boolean axis) {
        if (axis)
            return firstByX;
        else
            return firstByY;
    }

    public Container getLastByAxis(boolean axis) {
        if (axis)
            return lastByX;
        else
            return lastByY;
    }

    public void setFirstByAxis(Container first, boolean axis) {
        if (axis)
            firstByX = first;
        else
            firstByY = first;
    }

    public void setLastByAxis(Container last, boolean axis) {
        if (axis)
            lastByX = last;
        else
            lastByY = last;
    }

    public void addPoint(Point point) {
        Container newPoint = new Container(point);

        if (isEmpty()) {
            firstByX = newPoint;
            lastByX = newPoint;
            firstByY = newPoint;
            lastByY = newPoint;
        } else {
            firstByX = firstByX.addPointTo(newPoint, true);
            if (newPoint.getNext(true) == null)
                lastByX = newPoint;

            firstByY = firstByY.addPointTo(newPoint, false);
            if (newPoint.getNext(false) == null)
                lastByY = newPoint;
        }

        size++;
    }

    public boolean isEmpty() {
        return firstByX == null;
    }

    private int countPointsInRange(int min, int max, boolean axis) {
        int sum = 0;
        Container curr = getFirstByAxis(axis);

        while (curr != null && curr.getPointValue(axis) <= max) {
            if (curr.getPointValue(axis) >= min & curr.getPointValue(axis) <= max)
                sum++;
            curr = curr.getNext(axis);
        }

        return sum;
    }

    public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
        Point[] output = new Point[countPointsInRange(min, max, axis)];
        Container curr = getFirstByAxis(axis);
        int index = 0;

        while (curr != null && curr.getPointValue(axis) <= max) {
            if (curr.getPointValue(axis) >= min & curr.getPointValue(axis) <= max)
                output[index++] = curr.getData();

            curr = curr.getNext(axis);
        }

        return output;
    }

    @Override
    public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
        Point[] output = new Point[countPointsInRange(min, max, axis)];
        Container curr = getFirstByAxis(!axis);
        int index = 0;

        while (curr != null) {
            if (curr.getPointValue(axis) >= min & curr.getPointValue(axis) <= max)
                output[index++] = curr.getData();
            curr = curr.getNext(!axis);
        }

        return output;
    }

    @Override
    public double getDensity() {
        return size /
                ((lastByX.getPointValue(true) - firstByX.getPointValue(true)) *
                        (lastByY.getPointValue(false) - firstByY.getPointValue(false)));
    }

    @Override
    public void narrowRange(int min, int max, Boolean axis) {
        int counter = 0;

        // remove by min
        Container curr = getFirstByAxis(axis);
        while (curr.getPointValue(axis) <= min) {
            counter++;
            curr.removeSelf();
            updatePositions(curr, !axis);
            curr = curr.getNext(axis);
        }
        setFirstByAxis(curr, axis);

        // remove by max
        curr = getLastByAxis(axis);
        while (curr.getPointValue(axis) >= max) {
            counter++;
            curr.removeSelf();
            updatePositions(curr, !axis);
            curr = curr.getPrev(axis);
        }
        setLastByAxis(curr, axis);

        size -= counter;
    }

    public void updatePositions(Container container, boolean axis) {
        if (container.equals(getFirstByAxis(axis)))
            setFirstByAxis(container.getNext(axis), axis);

        if (container.equals(getLastByAxis(axis)))
            setLastByAxis(container.getPrev(axis), axis);
    }

    @Override
    public Boolean getLargestAxis() {
        return (lastByX.getPointValue(true) - firstByX.getPointValue(true)) >
                (lastByY.getPointValue(false) - firstByY.getPointValue(false));
    }

    @Override
    public Container getMedian(Boolean axis) {
        Container curr = getFirstByAxis(axis);
        for (int i = 0; i < size / 2; i++)
            curr = curr.getNext(axis);

        return curr;
    }

    private int numOfPointsTillMax(Container container, int max, Boolean axis) {
        int counter = 0;
        while (container != null && container.getPointValue(axis) <= max) {
            counter++;
            container = container.getNext(axis);
        }
        return counter;
    }

    private int numOfPointsTillMin(Container container, int min, Boolean axis) {
        int counter = 0;
        while (container != null && container.getPointValue(axis) >= min) {
            counter++;
            container = container.getPrev(axis);
        }
        return counter;
    }

    @Override
    public Point[] nearestPairInStrip(Container container, double width,
                                      Boolean axis) {
        int minAxis = (int) (container.getPointValue(axis) - width);
        int maxAxis = (int) (container.getPointValue(axis) + width);
        Point[] points;
        int pointsLength = numOfPointsTillMax(container, maxAxis, axis) +
                numOfPointsTillMin(container.getPrev(axis), minAxis, axis);
        if (pointsLength * Math.log(pointsLength) > size) {
            points = getPointsInRangeOppAxis(minAxis, maxAxis, axis);
        } else {
            points = new Point[pointsLength];

            int idx = 0;
            Container curr = container;
            while (curr != null && curr.getPointValue(axis) <= maxAxis) {
                points[idx++] = curr.getData();
                curr = curr.getNext(axis);
            }
            curr = container.getPrev(axis);
            while (curr != null && curr.getPointValue(axis) >= minAxis) {
                points[idx++] = curr.getData();
                curr = curr.getPrev(axis);
            }

            Arrays.sort(points, 0, points.length - 1, new PointsComparator(axis));
        }
        Point[] result = new Point[2];
        double shortestDistance = Double.POSITIVE_INFINITY;
        double distance;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < Math.min(i + 6, points.length - i); j++) {
                distance = getDistance(points[i], points[j]);

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    result = new Point[]{points[i], points[j]};
                }
            }
        }

        if (shortestDistance == Double.POSITIVE_INFINITY)
            return new Point[0];
        else
            return result;
    }

    @Override
    public Point[] nearestPair() {
        boolean axis = getLargestAxis();
        return nearestPair(getFirstByAxis(axis), getLastByAxis(axis), axis);
    }

    private Point[] nearestPair(Container fromCont, Container toCont, boolean axis) {
        if (fromCont.equals(toCont))
            return new Point[]{};
        else if (fromCont.getNext(axis).equals(toCont))
            return new Point[]{fromCont.getData(), toCont.getData()};
        else {
            Container median = getMedian(fromCont, toCont, axis);
            Point[] nearestRight = nearestPair(median, toCont, axis);
            Point[] nearestLeft = nearestPair(fromCont, median.getPrev(axis), axis);
            double rightDist = Double.POSITIVE_INFINITY;
            double leftDist = Double.POSITIVE_INFINITY;
            if (nearestRight.length == 2)
                rightDist = getDistance(nearestRight[0], nearestRight[1]);
            if (nearestLeft.length == 2)
                leftDist = getDistance(nearestLeft[0], nearestLeft[1]);

            double minDist = Math.min(rightDist, leftDist);
            Point[] nearestInStrip = nearestPairInStrip(median, 2 * minDist, axis);
            if (nearestInStrip.length == 2 && getDistance(nearestInStrip[0], nearestInStrip[1]) < minDist)
                return nearestInStrip;
            else if (minDist == rightDist)
                return nearestRight;
            else
                return nearestLeft;
        }
    }

    private Container getMedian(Container fromCont, Container toCont, boolean axis) {
        while (!fromCont.equals(toCont) & !fromCont.getNext(axis).equals(toCont)) {
            fromCont = fromCont.getNext(axis);
            toCont = toCont.getPrev(axis);
        }
        return fromCont;
    }

    private static double getDistance(Point point1, Point point2) {
        return Math.abs(Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2)));
    }
}

