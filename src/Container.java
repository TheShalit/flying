//Don't change the class name
public class Container {
    private Point data;//Don't delete or change this field;
    private Container nextX;
    private Container prevX;
    private Container nextY;
    private Container prevY;

    public Container(Point point) {
        data = point;
        nextX = null;
        nextY = null;
        prevX = null;
        prevY = null;
    }

    //Don't delete or change this function
    public Point getData() {
        return data;
    }

    // get x or y by axis
    public int getPointValue(boolean axis) {
        if (axis)
            return data.getX();
        else
            return data.getY();
    }

    // set next by axis
    public void setNext(Container next, boolean axis) {
        if (axis)
            nextX = next;
        else
            nextY = next;
    }

    // set previous by axis
    public void setPrev(Container prev, boolean axis) {
        if (axis)
            prevX = prev;
        else
            prevY = prev;
    }

    // get next by axis
    public Container getNext(boolean axis) {
        if (axis)
            return nextX;
        else
            return nextY;
    }

    // get previous by axis
    public Container getPrev(boolean axis) {
        if (axis)
            return prevX;
        else
            return prevY;
    }

    // recursively sets container to the correct place in the structure
    public Container addPointTo(Container container, boolean axis) {
        // if self value is bigger than container value, set container as previous of self
        if (getPointValue(axis) > container.getPointValue(axis)) {
            setPrev(container, axis);
            container.setNext(this, axis);
            return container;
        } else {
            if (getNext(axis) != null) { // if is there a next element
                Container curr = getNext(axis).addPointTo(container, axis); // get the correct next for self
                setNext(curr, axis);
                curr.setPrev(this, axis);
            } else { // if reached the end of the collection, set container as last element
                setNext(container, axis);
                container.setPrev(this, axis);
            }
            return this;
        }
    }

    // remove self from collection by connecting next and previous containers
    public void removeSelf() {
        if (prevX != null)
            prevX.setNext(nextX, true);
        if (prevY != null)
            prevY.setNext(nextY, false);

        if (nextX != null)
            nextX.setPrev(prevX, true);
        if (nextY != null)
            nextY.setPrev(prevY, false);
    }

    // toString
    public String toString() {
        return data.toString();
    }
}
