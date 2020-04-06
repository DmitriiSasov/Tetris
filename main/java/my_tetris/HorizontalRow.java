/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris;

import java.util.ArrayList;
import my_tetris.events.HorizontalRowEvent;
import my_tetris.events.HorizontalRowListener;

/**
 *
 * @author bopoh
 */
public class HorizontalRow {
    
    private HorizontalRow higherHorizontalRow = null;
    
    private ArrayList<Element> elements = new ArrayList<>();
    
    private int maxSize;

    public HorizontalRow(int maxSize) {

        if (maxSize > 0) {

            this.maxSize = maxSize;

        } else {

            throw new HorizontalRowSizeException("Размер ряда меньше 1");
        }
    }

    public int getMaxSize() { return maxSize; }

    //Устанавливается 1 раз
    public void setHigherHorizontalRow(HorizontalRow Row) {
        
        if (higherHorizontalRow == null && Row != this) {
            
            higherHorizontalRow = Row;
        }
    }
    
    private void clear() {
        
        elements.clear();
        
        HorizontalRowEvent e = new HorizontalRowEvent(this);
        e.setScore(maxSize);
        
        fireHorizontalRowCleared(e);
    }
    
    public int elementsCount() {
        
        return elements.size();
    }
    
    private void moveElementsTo(HorizontalRow other) {

        if ( this.elementsCount() > other.maxSize) {

            throw new HorizontalRowSizeException("Размер горизонтального ряда Other меньше, чем " +
                    "количество элементов в текущем ряду");
        }

        for (Element tmp : elements) {

            tmp.setPosition(tmp.getCol(), tmp.getRow() - 1);
        }

        other.elements.addAll(this.elements);
        elements.clear();

        if (higherHorizontalRow != null && higherHorizontalRow.elementsCount() > 0) {
            
            higherHorizontalRow.moveElementsTo(this);
        }
    }
    
    public Element getElement(int index) {
        
        if (index >= 0 && index < elements.size()) {

            try {

                return elements.get(index).clone();

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    
    public boolean hasElement(Element e) {
        
        return elements.contains(e);
    }
    
    public boolean addElement(Element e) {
        
        if (elements.contains(e)) { return false; }

        try {

            elements.add(e.clone());

        } catch (CloneNotSupportedException ex) { ex.printStackTrace(); }

        if ( elements.size() == maxSize) {

            clear();

            if (higherHorizontalRow != null) { higherHorizontalRow.moveElementsTo(this); }

            HorizontalRowEvent event = new HorizontalRowEvent(this);
            fireHorizontalRowCleared(event);
        }

        return true;
    }
    
    
    //Сообщает Игре и фигуре о том, что ряд был очищен
    private ArrayList<HorizontalRowListener> listeners = new ArrayList<>();
    
    public void addHorizontalRowListener(HorizontalRowListener l) {
        
        if (!listeners.contains(l)) {
        
            listeners.add(l);
        }
    }
    
    public void removeHorizontalRowListener(HorizontalRowListener l) {
        
        listeners.remove(l);
    }
    
    private void fireHorizontalRowCleared(HorizontalRowEvent e) {
        
        for (HorizontalRowListener l : listeners) {
            
            l.HorizontalRowCleared(e);
        }
    }

    public class HorizontalRowSizeException extends IllegalArgumentException {

        public HorizontalRowSizeException(String s) { super(s); }
    }
}
