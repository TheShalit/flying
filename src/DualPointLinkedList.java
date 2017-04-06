/**
 * Created by shalev on 06/04/2017.
 */
public class DualPointLinkedList {
    private PointLink firstByX;
    private PointLink lastByX;
    private PointLink firstByY;
    private PointLink lastByY;
    private int size;

    public DualPointLinkedList() {
        size = 0;
        firstByX = null;
        lastByX = null;
        firstByY = null;
        lastByY = null;
    }

    public void add(Point point) {

        PointLink newPoint = new PointLink(point);
        PointLink curr, next;
        boolean ins = false;
        if (isEmpty()) {
            firstByX = newPoint;
            lastByX = newPoint;
            firstByY = newPoint;
            lastByY = newPoint;
        } else if (point.getX() <= firstByX.getPoint().getX()) {
            newPoint.setLinkNext(firstByX, true);
            firstByX.setLinkPrev(newPoint, true);
            firstByX = newPoint;
        } else {
            curr = firstByX;
            next = curr.getLinkNext(true);
            int val = newPoint.getPoint().getX();

            while (next != null) {
                if (val >= curr.getPoint().getX() && val <= next.getPoint().getX()) {
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
        }

        size++;

    }

    public boolean isEmpty() {
        return firstByX.equals(null);
    }

    public int getSize() {
        return size;
    }
}
