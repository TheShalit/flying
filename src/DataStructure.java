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

    // O(1)
    // get firstByX or firstByY
    public Container getFirstByAxis(boolean axis) {
        if (axis)
            return firstByX;
        else
            return firstByY;
    }

    // O(1)
    // get lastByX or lastByY
    public Container getLastByAxis(boolean axis) {
        if (axis)
            return lastByX;
        else
            return lastByY;
    }

    // O(1)
    // set firstByX or firstByY
    public void setFirstByAxis(Container first, boolean axis) {
        if (axis)
            firstByX = first;
        else
            firstByY = first;
    }

    // O(1)
    // set lastByX or lastByY
    public void setLastByAxis(Container last, boolean axis) {
        if (axis)
            lastByX = last;
        else
            lastByY = last;
    }

    // O(n)
    // add point to the ordered place in collection(by x and y)
    public void addPoint(Point point) {
        Container newPoint = new Container(point);

        if (isEmpty()) { // if it is the first element
            firstByX = newPoint;
            lastByX = newPoint;
            firstByY = newPoint;
            lastByY = newPoint;
        } else {
            // set first by recursively function addPointTo that adds point the correct place for X axis
            firstByX = firstByX.addPointTo(newPoint, true); // set first by recursively function addPointTo that adds point the correct place
            if (newPoint.getNext(true) == null) // the new Point has no next, its because it is the last point
                lastByX = newPoint;

            // set first by recursively function addPointTo that adds point the correct place for Y axis
            firstByY = firstByY.addPointTo(newPoint, false);
            if (newPoint.getNext(false) == null) // the new Point has no next, its because it is the last point
                lastByY = newPoint;
        }

        size++; // increase size
    }

    // O(1)
    // return if no elements in collection
    public boolean isEmpty() {
        return firstByX == null;
    }

    // O(n)
    // return number of points from min to max by axis
    private int countPointsInRange(int min, int max, boolean axis) {
        int counter = 0;
        Container curr = getFirstByAxis(axis);

        while (curr != null && curr.getPointValue(axis) <= max) {
            if (curr.getPointValue(axis) >= min)
                counter++;
            curr = curr.getNext(axis);
        }

        return counter;
    }

    // O(n)
    // return points from min to max by axis
    public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
        Point[] output = new Point[countPointsInRange(min, max, axis)];
        Container curr = getFirstByAxis(axis);
        int index = 0;

        while (curr != null && curr.getPointValue(axis) <= max) { // while the point is smaller than max
            if (curr.getPointValue(axis) >= min)
                output[index++] = curr.getData();

            curr = curr.getNext(axis);
        }

        return output;
    }

    // O(n)
    // return points from min to max by axis ordered by opposite axis
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

    // O(1)
    // size / ((max(x) - min(x)) * (max(y) - min(y)))
    public double getDensity() {
        return size /
                ((lastByX.getPointValue(true) - firstByX.getPointValue(true)) *
                        (lastByY.getPointValue(false) - firstByY.getPointValue(false)));
    }

    // O(|A|) -> A = removed points
    // removes points that smaller than min or bigger than max
    public void narrowRange(int min, int max, Boolean axis) {
        int counter = 0;

        // remove by min
        Container curr = getFirstByAxis(axis);
        while (curr != null && curr.getPointValue(axis) < min) {
            counter++;
            curr.removeSelf();
            updatePositions(curr, axis);
            updatePositions(curr, !axis);
            curr = curr.getNext(axis);
        }
        setFirstByAxis(curr, axis);

        // remove by max
        curr = getLastByAxis(axis);
        while (curr != null && curr.getPointValue(axis) > max) {
            counter++;
            curr.removeSelf();
            updatePositions(curr, axis);
            updatePositions(curr, !axis);
            curr = curr.getPrev(axis);
        }
        setLastByAxis(curr, axis);

        size -= counter; // subtract deleted points from size
    }

    // O(1)
    // update first and last
    public void updatePositions(Container container, boolean axis) {
        if (container.equals(getFirstByAxis(axis)))
            setFirstByAxis(container.getNext(axis), axis);

        if (container.equals(getLastByAxis(axis)))
            setLastByAxis(container.getPrev(axis), axis);
    }

    // O(1)
    // return the axis with largest axis distance between max and min points
    public Boolean getLargestAxis() {
        return (lastByX.getPointValue(true) - firstByX.getPointValue(true)) >
                (lastByY.getPointValue(false) - firstByY.getPointValue(false));
    }

    // O(n)
    // returns the container in the middle of the axis
    public Container getMedian(Boolean axis) {
        Container curr = getFirstByAxis(axis);
        for (int i = 0; i < size / 2; i++)
            curr = curr.getNext(axis);

        return curr;
    }

    // O(k) -> k = number of points from container to container with less-equals max value
    // returns number of points from container to container with less-equals max value
    private int numOfPointsTillMax(Container container, int max, Boolean axis) {
        int counter = 0;
        while (container != null && container.getPointValue(axis) <= max) {
            counter++;
            container = container.getNext(axis);
        }
        return counter;
    }

    // O(k) -> k = number of points from container to container with bigger-equals min value
    // returns number of points from container to container with less-equals min value
    private int numOfPointsTillMin(Container container, int min, Boolean axis) {
        int counter = 0;
        while (container != null && container.getPointValue(axis) >= min) {
            counter++;
            container = container.getPrev(axis);
        }
        return counter;
    }

    // O(min(|B|log|B|, n) -> B = number of points in strip
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

            Arrays.sort(points, 0, points.length, new PointsComparator(axis));
        }
        Point[] result = new Point[2];
        double shortestDistance = Double.POSITIVE_INFINITY;
        double distance;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < Math.min(i + 7, points.length); j++) {
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

    // O(nlogn)
    // return two closest points
    public Point[] nearestPair() {
        boolean axis = getLargestAxis();
        return nearestPair(getFirstByAxis(axis), getLastByAxis(axis), axis);
    }

    // O(nlogn)
    // return recursively two closest points from @fromCont to @toCont in the @axis
    private Point[] nearestPair(Container fromCont, Container toCont, boolean axis) {
        if (fromCont.equals(toCont))
            return new Point[]{};
        else if (fromCont.getNext(axis).equals(toCont))
            return new Point[]{fromCont.getData(), toCont.getData()};
        else {
            Container median = getMedian(fromCont, toCont, axis);
            // recursively nearestPair from middle(included) to @toCont
            Point[] nearestRight = nearestPair(median, toCont, axis);
            // recursively nearestPair from @fromCont  to middle(not included)
            Point[] nearestLeft = nearestPair(fromCont, median.getPrev(axis), axis);
            double rightDist = Double.POSITIVE_INFINITY; // set default to infinity for less than 2 points
            double leftDist = Double.POSITIVE_INFINITY; // set default to infinity for less than 2 points
            if (nearestRight.length == 2) // if at least two points, calculate distance
                rightDist = getDistance(nearestRight[0], nearestRight[1]);
            if (nearestLeft.length == 2) // if at least two points, calculate distance
                leftDist = getDistance(nearestLeft[0], nearestLeft[1]);

            double minDist = Math.min(rightDist, leftDist); // minimum distance from left and right
            // call nearestPairInStrip in 2 * minDist strip
            Point[] nearestInStrip = nearestPairInStrip(median, 2 * minDist, axis);
            // if found return it
            if (nearestInStrip.length == 2 && getDistance(nearestInStrip[0], nearestInStrip[1]) < minDist)
                return nearestInStrip;
            else if (minDist == rightDist) // else return minimum from left and right
                return nearestRight;
            else
                return nearestLeft;
        }
    }

    // O(|C|) - C = points from @fromCont to @toCont
    // get the median container from @fromCont to @toCont
    private Container getMedian(Container fromCont, Container toCont, boolean axis) {
        while (!fromCont.equals(toCont) & !fromCont.getNext(axis).equals(toCont)) {
            fromCont = fromCont.getNext(axis);
            toCont = toCont.getPrev(axis);
        }
        return fromCont;
    }

    public DataStructure copyDataStructure() {
        DataStructure dl = new DataStructure();
        Container firstCont = new Container(this.firstByX.getData());
        dl.firstByX = firstCont;
        firstByX.setCopyTemp(firstCont);


        Container otherCurr = firstByX.getNext(true);
        Container myCurr = firstCont;
        while (otherCurr != null) {
            myCurr.setNext(new Container(otherCurr.getData()), true);
            myCurr.getNext(true).setPrev(myCurr, true);
            otherCurr.setCopyTemp(myCurr.getNext(true));
            myCurr = myCurr.getNext(true);
            otherCurr = otherCurr.getNext(true);
        }
        otherCurr = this.firstByX;
        while (otherCurr != null) {
            if ((otherCurr.getNext(false) != null))
                otherCurr.getCopyTemp().setNext(otherCurr.getNext(false).getCopyTemp(), false);
            if ((otherCurr.getPrev(false) != null))
                otherCurr.getCopyTemp().setPrev(otherCurr.getPrev(false).getCopyTemp(), false);

            otherCurr = otherCurr.getNext(false);
        }
        dl.firstByY = firstByY.getCopyTemp();
        dl.lastByX = lastByX.getCopyTemp();
        dl.firstByY = lastByY.getCopyTemp();
        while (otherCurr != null) {
            otherCurr.setCopyTemp(null);
            otherCurr = otherCurr.getNext(true);
        }
        return dl;
    }

    // O(n)
    public DataStructure[] split(boolean axis) {
        Container median = getMedian(axis);

        DataStructure leftDS = new DataStructure();
        DataStructure rightDS = new DataStructure();

        leftDS.setFirstByAxis(getFirstByAxis(axis), axis);
        leftDS.setLastByAxis(median.getPrev(axis), axis);
        rightDS.setFirstByAxis(median, axis);
        rightDS.setLastByAxis(getLastByAxis(axis), axis);

        leftDS.setFirstByAxis(getFirstByAxis(!axis).setNextByMedian(median, axis), !axis);
        leftDS.setLastByAxis(leftDS.getFirstByAxis(!axis).getLastByAxis(!axis), !axis);
        rightDS.setLastByAxis(getLastByAxis(!axis).setPrevByMedian(median, axis), !axis);
        rightDS.setFirstByAxis(rightDS.getLastByAxis(!axis).getFirstByAxis(!axis), !axis);

        median.getPrev(axis).setNext(null, axis);
        median.setPrev(null, axis);

        return new DataStructure[]{leftDS, rightDS};
    }

    public void printSides(boolean axis) {
        Container fromCont = getFirstByAxis(axis);
        Container toCont = getLastByAxis(axis);
        Container curr = fromCont;
        while (curr != null && !curr.equals(toCont)) {
            System.out.print(curr + "->" + curr.getNext(axis) + ", ");
            curr = curr.getNext(axis);
        }
        System.out.println(toCont + "->null");

        curr = toCont;
        while (curr != null && !curr.equals(fromCont)) {
            System.out.print(curr + "<-" + curr.getPrev(axis) + ", ");
            curr = curr.getPrev(axis);
        }
        System.out.println(fromCont + "<-null");
    }
    // O(1)
    // return sqrt(power(x-x) + power(y-y))
    private static double getDistance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
    }
}

