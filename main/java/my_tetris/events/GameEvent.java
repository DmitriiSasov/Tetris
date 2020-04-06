package my_tetris.events;

import my_tetris.Element;

import java.util.ArrayList;
import java.util.EventObject;

public class GameEvent extends EventObject {

    private int scoreDelta = 0;

    private ArrayList<Element> glassElements;

    private ArrayList<Element> nextActiveShape;

    public ArrayList<Element> getNextActiveShape() {

        return nextActiveShape;
    }

    public void setNextActiveShape(ArrayList<Element> nextActiveShape) {

        this.nextActiveShape = nextActiveShape;
    }
//private ArrayList<Point>

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GameEvent(Object source) {
        super(source);
    }

    public int getScoreDelta() {
        return scoreDelta;
    }

    public void setScoreDelta(int scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public ArrayList<Element> getGlassElements() {
        return glassElements;
    }

    public void setGlassElements(ArrayList<Element> glassElements) {
        this.glassElements = glassElements;
    }
}
