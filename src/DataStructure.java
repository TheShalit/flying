
public class DataStructure implements DT {
    private Container firstByX;
    private Container lastByX;
    private Container firstByY;
    private Container lastByY;
    int size;

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

        while (curr != null) {
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

        while (curr != null) {
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
        for (int i = 0; i < size / 2 - 1; i++)
            curr = curr.getNext(axis);

        return curr;
    }

    @Override
    public Point[] nearestPairInStrip(Container container, double width,
                                      Boolean axis) {
        return null;
    }

    @Override
    public Point[] nearestPair() {
        // TODO Auto-generated method stub
        return null;
    }
}

