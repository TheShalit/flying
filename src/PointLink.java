/**
 * Created by shalev on 06/04/2017.
 */
public class PointLink {
    private Point point;
    private PointLink nextX;
    private PointLink prevX;
    private PointLink nextY;
    private PointLink prevY;

    public PointLink(Point point) {
        this.point = point;
        nextX = null;
        nextY = null;
        prevX = null;
        prevY = null;
    }

    public PointLink(Point point, PointLink next, PointLink prev, boolean axis ) {
        this.point = point;
        if (axis){
            nextX = next;
            nextY = null;
            prevX = prev;
            prevY = null;
        }
        else {
            nextX = null;
            nextY = next;
            prevX = null;
            prevY = prev;
        }
    }

    public Point getPoint() {
        return point;
    }

    public void setLinkNext(PointLink next, boolean axis)
    {
        if(axis)
            nextX = next;
        else
            nextY = next;
    }
    /* Function to set link to previous node */

    public void setLinkPrev(PointLink prev, boolean axis)
    {
        if(axis)
            prevX = prev;
        else
            prevY = prev;
    }

    public PointLink getLinkNext(boolean axis)
    {
        if(axis)
            return nextX;
        else
            return nextY;
    }
    /* Function to set link to previous node */

    public PointLink getLinkPrev(boolean axis)
    {
        if(axis)
            return prevX;
        else
            return prevY;
    }

    public String toString() {
        return point.toString();
    }
}
