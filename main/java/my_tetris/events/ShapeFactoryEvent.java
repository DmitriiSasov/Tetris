package my_tetris.events;

import my_tetris.Element;

import java.util.ArrayList;
import java.util.EventObject;

public class ShapeFactoryEvent extends EventObject {

    ArrayList<Element> nextShapeElements = new ArrayList<>();

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ShapeFactoryEvent(Object source) {
        super(source);
    }

    public ArrayList<Element> getNextShapeElements() {
        return nextShapeElements;
    }

    public void setNextShapeElements(ArrayList<Element> nextShapeElements) {
        this.nextShapeElements = nextShapeElements;
    }
}
