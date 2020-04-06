/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris.general_game_objects;
import java.awt.Point;
import java.util.ArrayList;

import my_tetris.*;
import my_tetris.events.GlassEvent;
import my_tetris.events.GlassListener;
import my_tetris.events.HorizontalRowEvent;
import my_tetris.events.HorizontalRowListener;

/**
 *
 * @author bopoh
 */
public class Glass {
    
    public final int MIN_ROW_COUNT = 6;

    public final int MIN_COL_COUNT = 6;

    private ArrayList<Element> elements = new ArrayList<>(); // Список заполненных элементов на поле
    
    private Shape activeShape;
    
    private int rowCount;
    
    private int colCount;
    
    private ShapeFactory shapeFactory;

    private ArrayList<HorizontalRow> rows = new ArrayList<>();
    
    private Border border;
    
    public Glass(int colCount, int rowCount) {
        
        if (colCount < MIN_COL_COUNT || rowCount < MIN_ROW_COUNT) {
            
            throw new GlassSizeException("Размеры стакана слишком маленькие");
        }
        
        this.rowCount = rowCount;
        this.colCount = colCount;

        for (int i = 0; i < this.rowCount; ++i) {
            
            HorizontalRow row = new HorizontalRow(this.colCount);
            if (i > 0) { rows.get(i - 1).setHigherHorizontalRow(row); }
            row.addHorizontalRowListener(new HorizontalRowObserver());
            rows.add(row);
        }
        
        border = new Border(0, 0, this.colCount -1);

        elements.clear();//Для вывода на экран
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public Border getBorder() {
        return border;
    }

    public Shape getActiveShape() {
        
        return activeShape;
    }
    
    void setActiveShape(Shape shape) {

        if (activeShape != null) {

            activeShape.unsetGlass();
        }

        activeShape = shape;
        
        if (activeShape.getGlass() != this) {
            activeShape.setGlass(this);
        }
        
    }
    
    void unsetActiveShape() {

        var shape = activeShape;
        activeShape = null;

        if (shape != null && shape.getGlass() != null) {
            shape.unsetGlass();
        }
    }

    public void setShapeFactory(ShapeFactory shapeFactory) {
        
        this.shapeFactory = shapeFactory;
        setActiveShape(this.shapeFactory.createShape());
        activeShape.setStartPosition(0, rowCount - 1, 0, colCount - 1);

        boolean isCorrectStartPosition = true;
        for (int i = 0; i < activeShape.elementsCount() && isCorrectStartPosition; ++i) {

            Point tmpPosition = new Point(activeShape.getElement(i).getCol(), activeShape.getElement(i).getRow());
            isCorrectStartPosition = isCorrectStartPosition && isFreePosition(tmpPosition);
        }

        if (!isCorrectStartPosition) {

            unsetActiveShape();
            fireGlassFilled();
        }
    }

    public ShapeFactory getShapeFactory() {
        return shapeFactory;
    }

    public boolean isFreePosition(Point position) {

        return !border.isPositionOutsideBorder(position) && (position.getY() >= rowCount ||
                !rows.get((int)position.getY()).hasElement(new Element(position)));
    }
        
    void absorbActiveShape() {
        
        while(activeShape.hasElements()) {
            
            int lowestShapeElementIndex = 0;
            for (int i = 1; i < activeShape.elementsCount(); ++i) {
                
                if (activeShape.getElement(lowestShapeElementIndex).getRow() > activeShape.getElement(i).getRow()) {
                    
                    lowestShapeElementIndex = i;
                }              
            }
            
            Element movedElement = activeShape.takeElement(lowestShapeElementIndex);

            //Если элемент фигуры находится ниже уровня стакана при появлении
            if (movedElement.getRow() < rowCount) {

                boolean isAdded = rows.get(movedElement.getRow()).addElement(movedElement);
                if (!isAdded) {

                    throw new EqualElementsInGlassException("Элемент фигуры не может быть поглощен стаканом, т.к. в " +
                            "стакане уже есть элемент с такой же позицией");
                }
            }

        }

        System.out.println("Фигура поглощена");//Для вывода на экран
        updateGlassContent();//Для вывода на экран

        unsetActiveShape();
        if (isHighestRowFilled()) {

            setActiveShape(shapeFactory.createShape());
            activeShape.setStartPosition(0, this.rowCount - 1, 0, this.colCount -1);

        } else {

            fireGlassFilled();
        }
            
    }

    private boolean isHighestRowFilled() {
        
        return rows.get(rows.size() - 1).elementsCount() == 0;
    }
    
    //Для вывода в UI
    public void updateGlassContent() {

        elements.clear();

        //Заполнить клетки, которые есть в рядах
        for (HorizontalRow row : rows) {

            for (int i = 0; i < row.elementsCount(); ++i) {

                elements.add(row.getElement(i));
            }
        }

        //Заполнить текущую позицию фигуры
        for (int i = 0; i < activeShape.elementsCount(); ++i) {

            if (activeShape.getElement(i).getRow() < rowCount) {

                elements.add(activeShape.getElement(i));
            }
        }

        GlassEvent e = new GlassEvent(this);
        e.setGlassElements(elements);
        fireGlassContentChanged(e);
    }



    //Сообщает игре об перемещении фигуры и обновлении рядов, также о заполненности
    private ArrayList<GlassListener> glassListeners = new ArrayList<>();
    
    public void addGlassListener(GlassListener l) {
        
        if (!glassListeners.contains(l)) {
            
            glassListeners.add(l);
        }        
    }
    
    public void removeGlassListener(GlassListener l) {
        
        glassListeners.remove(l);
    }
    
    public void fireGlassFilled() {
        
        for (GlassListener l : glassListeners) {

            System.out.println("Стакан заполнен");//Для вывода на экран
            l.glassFilled();
        }        
    }

    public void fireGlassContentChanged(GlassEvent e) {

        for (GlassListener l : glassListeners) {

            l.glassContentChanged(e);
        }
    }
    
    
    //Стакан просто передает сообщение о том, что ряд очистился фигуре и игре
    private class HorizontalRowObserver implements HorizontalRowListener {

        @Override
        public void HorizontalRowCleared(HorizontalRowEvent e) {

            fireHorizontalRowCleared(e);
        }
        
    }
    
    private ArrayList<HorizontalRowListener> rowListeners = new ArrayList<>();
    
    public void addHorizontalRowListener(HorizontalRowListener l) {
        
        if (!rowListeners.contains(l)) {
            
            rowListeners.add(l);
        }
    }
    
    public void removeHorizontalRowListener(HorizontalRowListener l) {
       
        rowListeners.remove(l);
    }
    
    public void fireHorizontalRowCleared(HorizontalRowEvent e) {
        
        for (HorizontalRowListener l : rowListeners) {
            
            l.HorizontalRowCleared(e);
        }        
    }




    public class GlassSizeException extends IllegalArgumentException {

        public GlassSizeException(String s) { super(s); }
    }

    public class EqualElementsInGlassException extends RuntimeException {

        public EqualElementsInGlassException(String s) { super(s); }
    }
}
