/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris.general_game_objects;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import my_tetris.Direction;
import my_tetris.Element;
import my_tetris.events.HorizontalRowEvent;
import my_tetris.events.HorizontalRowListener;

import javax.swing.*;

/**
 *
 * @author bopoh
 */
public class Shape implements Cloneable{

    private Glass glass = null;

    private ArrayList<Element> elements = new ArrayList<>();

    private HorizontalRowObserver rowObserver = new HorizontalRowObserver();

    private Timer timer = new Timer(1000, new MovingShapeTask());

    public Shape(ArrayList<Element> elements) {
        
        if (elements.size() > 0) {

            for (Element tmp : elements) {
                
                try {
                    
                    this.elements.add(tmp.clone());
                    
                } catch (CloneNotSupportedException ex) {
                    
                    Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            timer.setRepeats(true);

        } else {
            
            throw new ShapeSizeException("В фигуре должно быть не меньше 1 элемента");
        }        
    }
        
    void setGlass(Glass glass) {

        if (this.glass != null) {

            this.glass.unsetActiveShape();
        }

        this.glass = glass;
        this.glass.addHorizontalRowListener(rowObserver);

        if (glass.getActiveShape() != this) {
            
            glass.setActiveShape(this);
        }
    }
    
    Glass getGlass(){
        return glass;
    }
    
    void unsetGlass(){

        var glass = this.glass;
        this.glass = null;
        timer.stop();
        if (glass != null && glass.getActiveShape() != null) {

            glass.removeHorizontalRowListener(rowObserver);
            glass.unsetActiveShape();
        }
    }
    
    public Element getElement(int index) {
        
        if (index < elements.size() && index >= 0){

            try {

                return elements.get(index).clone();

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    
    Element takeElement(int index) {
        
        if (index < elements.size() && index >= 0){
            
            Element tmp = elements.get(index);
            elements.remove(index);
            return tmp;
            
        } else {
            return null;
        }
    }
    
    public void rotate() {
        
        ArrayList<Element> rotatedElements = new ArrayList<>();
        
        for (Element tmp : elements) {
            
            try {
            
                rotatedElements.add(tmp.clone());
            
            } catch (CloneNotSupportedException ex) {
                
                Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        rotateElements(rotatedElements);

        if (isCorrectPosition(rotatedElements)) {
            
            System.err.println("Фигура повернулась");//Для вывода на экран
            elements.clear();
            elements.addAll(rotatedElements);
        }
        
        glass.updateGlassContent();//Для вывода на экран
    }
    
    private void rotateElements(ArrayList<Element> elements) {
        
        Element rotateCenter = elements.get(1);
        for (Element tmp : elements) {
            
            int col = tmp.getRow() - rotateCenter.getRow();
            int row = tmp.getCol() - rotateCenter.getCol();
            tmp.setPosition(rotateCenter.getCol() + col, rotateCenter.getRow() - row);
        }
    }
    
    void setStartPosition(int minRowIndex, int maxRowIndex, int minColIndex, 
            int maxColIndex) {
        
        if (maxRowIndex - minRowIndex > 5 && maxColIndex - minColIndex > 5) {
            
            for (Element tmp : elements) {
                
                tmp.setPosition(tmp.getCol() + (maxColIndex - minColIndex) / 2 - 1,
                        tmp.getRow() + maxRowIndex);
            }

            timer.start();

            System.out.println("Фигура установлена в верхушке стакана");//Для вывода на экран
            glass.updateGlassContent();//Для вывода на экран

        } else {
            
            throw new ShapeStartPositionException("Границы стакана заданы неверно");
        }
        
    }

    public boolean hasElements() {

        return elementsCount() > 0;
    }

    public int elementsCount() {
        
        return elements.size();
    }
    
    synchronized public void move(Direction d) {
        
        int X_Shift = 0;
        int Y_Shift = 0;
        
        String dir = ""; //Для вывода на экран
        
        if (d == Direction.WEST) {
            
            X_Shift--;
            dir = "запад";//Для вывода на экран
        } else if (d == Direction.EAST) {
            
            X_Shift++;
            dir = "восток";//Для вывода на экран
        } else if (d == Direction.NORTH) {
            
            Y_Shift++;
            dir = "север";//Для вывода на экран
        } else {
            
            Y_Shift--;
            dir = "юг";//Для вывода на экран
        }
        
        ArrayList<Element> movedElements = new ArrayList<>();
        
        for (Element tmp : elements) {
            
            try {
            
                movedElements.add(tmp.clone());
            
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        moveElements(movedElements, X_Shift, Y_Shift);
        
        boolean canMove = isCorrectPosition(movedElements);
        
        if (canMove) {
            
            elements.clear();
            elements.addAll(movedElements);
            System.err.println("Фигура передвинулась на 1 клетку на " + dir);//Для вывода на экран
            glass.updateGlassContent();//Для вывода на экран
        }
        
        if (d == Direction.SOUTH && !canMove) {

            System.err.println("Фигура остановилась");//Для вывода на экран
            timer.stop();
            glass.absorbActiveShape();
        }
        

    }
    
    private void moveElements(ArrayList<Element> elements, int X_Shift, int Y_Shift) {
        
        for (Element tmp: elements) {
           
            tmp.setPosition(tmp.getCol() + X_Shift, tmp.getRow() + Y_Shift);
        }
        
    }
    
    private boolean isCorrectPosition(ArrayList<Element> elements) {
        
        boolean correctPosition = true;
        for (int i = 0 ; i < elements.size() && correctPosition; ++i) {
            
            correctPosition = correctPosition && glass.isFreePosition(new Point(elements.get(i).getCol(), 
                    elements.get(i).getRow()));
        }
        
        return correctPosition;
    }

    @Override
    public Shape clone() {

        ArrayList<Element> elementsClone = new ArrayList<>();
        for (Element tmp : elements) {

            try {

                elementsClone.add(tmp.clone());

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        var shapeClone = new Shape(elementsClone);
        shapeClone.glass = glass;
        return shapeClone;
    }

    //Для вывода на экран
    @Override
    public String toString() {

        String res = "Позиция фигуры: ";

        for (Element el : elements) {

            res += el.toString();
        }

        return res;
    }


    //Слушает события от таймера и двигает фигуру вниз
    private class MovingShapeTask implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Shape.this.move(Direction.SOUTH);
        }
    }

    //Слушает горизонтальный ряд и перемещается вниз, если ряд внизу очистился
    private class HorizontalRowObserver implements HorizontalRowListener {

        @Override
        public void HorizontalRowCleared(HorizontalRowEvent e) {
            
            if (elementsCount() != 0 && e.getScore() == 0) {

                System.out.println("Фигура падает из-за очищения ряда под ней");//Для вывода на экран
                move(Direction.SOUTH); 
            }
        }
    }

    public class ShapeSizeException extends IllegalArgumentException {

        public ShapeSizeException(String s) { super(s); }
    }

    public class ShapeStartPositionException extends IllegalArgumentException {

        public ShapeStartPositionException(String s) { super(s); }
    }
}
