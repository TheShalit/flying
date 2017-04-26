//Don't change the class name
public class Container {
    private Point data;//Don't delete or change this field;
    private Container nextX;
    private Container prevX;
    private Container nextY;
    private Container prevY;
    private Container copyTemp;

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

    public static void setNextAndPrev(Container next, Container prev, boolean axis) {
        if (axis) {
            if (next != null)
                next.prevX = prev;
            if (prev != null)
                prev.nextX = next;
        } else {
            if (next != null)
                next.prevY = prev;
            if (prev != null)
                prev.nextY = next;
        }
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

    public Container setNextByMedian(Container median, boolean axis) {
        if (getNext(!axis) == null){
            if (getPointValue(axis) < median.getPointValue(axis))
                return this;
            else
                return null;
        }

        if (getPointValue(axis) < median.getPointValue(axis)) {
            setNextAndPrev(getNext(!axis).setNextByMedian(median, axis), this, !axis);
            return this;
        } else {
            return getNext(!axis).setNextByMedian(median, axis);
        }
    }

    public Container setPrevByMedian(Container median, boolean axis) {
        if (getPrev(!axis) == null){
            if (getPointValue(axis) >= median.getPointValue(axis))
                return this;
            else
                return null;
        }

        if (getPointValue(axis) >= median.getPointValue(axis)) {
            setNextAndPrev(this, getPrev(!axis).setPrevByMedian(median, axis), !axis);
            return this;
        } else {
            return getPrev(!axis).setPrevByMedian(median, axis);
        }
    }

    public Container getLastByAxis(boolean axis) {
        if (getNext(axis) == null)
            return this;
        return getNext(axis).getLastByAxis(axis);
    }

    public Container getFirstByAxis(boolean axis) {
        if (getPrev(axis) == null)
            return this;
        return getPrev(axis).getFirstByAxis(axis);
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
    public  void setCopyTemp(Container cont){
        this.copyTemp = cont;
    }
    public Container getCopyTemp(){
        return copyTemp;
    }

    // toString
    public String toString() {
        return data.toString();
    }
}
