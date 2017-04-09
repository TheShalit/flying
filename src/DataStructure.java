
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

    public void addPoint(Point point) {
        Container newPoint = new Container(point);
        Container curr, next;
        boolean ins = false;
        if (isEmpty()) {
            firstByX = newPoint;
            lastByX = newPoint;
            firstByY = newPoint;
            lastByY = newPoint;
        } else {
            //insert to x axis

            if (point.getX() <= firstByX.getData().getX()) {
                newPoint.setLinkNext(firstByX, true);
                firstByX.setLinkPrev(newPoint, true);
                firstByX = newPoint;
            } else {
                curr = firstByX;
                next = curr.getLinkNext(true);
                int val = newPoint.getData().getX();

                while (next != null) {
                    if (val >= curr.getData().getX() && val <= next.getData().getX()) {
                        curr.setLinkNext(newPoint, true);
                        newPoint.setLinkPrev(curr, true);
                        newPoint.setLinkNext(next, true);
                        next.setLinkPrev(newPoint, true);
                        ins = true;
                        break;
                    } else {
                        curr = next;
                        next = next.getLinkNext(true);
                    }
                }
                if (!ins) {
                    curr.setLinkNext(newPoint, true);
                    newPoint.setLinkPrev(curr, true);
                    lastByX = newPoint;

                }
            }//small else

            //insert to y axis

            if (point.getY() <= firstByY.getData().getY()) {
                newPoint.setLinkNext(firstByY, false);
                firstByY.setLinkPrev(newPoint, false);
                firstByY = newPoint;
            } else {
                curr = firstByX;
                next = curr.getLinkNext(false);
                int val = newPoint.getData().getY();

                while (next != null) {
                    if (val >= curr.getData().getY() && val <= next.getData().getY()) {
                        curr.setLinkNext(newPoint, false);
                        newPoint.setLinkPrev(curr, false);
                        newPoint.setLinkNext(next, false);
                        next.setLinkPrev(newPoint, false);
                        ins = true;
                        break;
                    } else {
                        curr = next;
                        next = next.getLinkNext(false);
                    }
                }
                if (!ins) {
                    curr.setLinkNext(newPoint, false);
                    newPoint.setLinkPrev(curr, false);
                    lastByY = newPoint;

                }
            }//small else

        } //notEmpty else

        size++;
    }

    public boolean isEmpty() {
        return firstByX.equals(null);
    }

    public int getSize() {
        return size;
    }

    private int countPointsInRange(int min, int max, boolean axis) {

        int sum = 0;
        Container curr = getFirstByAxis(axis);

        while (curr != null && curr.getPointValue(axis) <= max & curr.getPointValue(axis) >= min) {

            sum++;
            curr = curr.getLinkNext(axis);
        }

        return sum;
    }

    public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {

        Point[] output = new Point[countPointsInRange(min, max, axis)];
        Container curr = getFirstByAxis(axis);
        int index = 0;
        while (curr != null && curr.getPointValue(axis) >= min & curr.getPointValue(axis) <= max) {
            output[index] = curr.getData();
            index++;
            curr = curr.getLinkNext(axis);

        }

        return output;
    }

    @Override
    public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
        Point[] output = new Point[countPointsInRange(min, max, axis)];
        Container curr = getFirstByAxis(!axis);

        int index = 0;
        while (curr != null && curr.getPointValue(axis) <= max & curr.getPointValue(axis) >= min) {

            output[index] = curr.getData();
            index++;
            curr = curr.getLinkNext(!axis);
        }

        return output;
    }

    @Override
    public double getDensity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void narrowRange(int min, int max, Boolean axis) {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean getLargestAxis() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Container getMedian(Boolean axis) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point[] nearestPairInStrip(Container container, double width,
                                      Boolean axis) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point[] nearestPair() {
        // TODO Auto-generated method stub
        return null;
    }


    //TODO: add members, methods, etc.

}

