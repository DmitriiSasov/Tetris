package my_tetris.events;

import my_tetris.Element;

import java.util.ArrayList;
import java.util.EventObject;

public class GlassEvent extends EventObject {

    private ArrayList<Element> glassElements = null;

    public ArrayList<Element> getGlassElements() {
        return glassElements;
    }

    public void setGlassElements(ArrayList<Element> glassElements) {
        this.glassElements = glassElements;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GlassEvent(Object source) {
        super(source);
    }
}
