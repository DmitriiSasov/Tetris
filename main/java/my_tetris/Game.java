/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris;

import my_tetris.events.*;
import my_tetris.general_game_objects.Glass;
import my_tetris.general_game_objects.Shape;

import java.util.ArrayList;

/**
 *
 * @author bopoh
 */
public class Game {
    
    private Glass glass;
    
    private ShapeFactory shapeFactory;
    
    private int score;

    public boolean isInProgress () {

        return glass.getActiveShape() != null;
    }

    public void start() {
        
        glass = new Glass(10, 20);
        
        glass.addGlassListener(new GlassObserver());
        glass.addHorizontalRowListener(new HorizontalRowObserver());
        
        score = 0;
        
        shapeFactory = new ShapeFactory();
        shapeFactory.addShapeFactoryListener(new ShapeFactoryObserver());

        glass.setShapeFactory(shapeFactory);
    }
    
    private void changeScore(int delta) {
        
        score += delta;
    }
    
    public void ShowScore() {
        
        System.out.print("\n***\nИгра закончилась\nВаш счет: " + score +  "\n***");
    }

    public int getScore() {
        return score;
    }

    public boolean moveActiveShape(Direction d) {

        if (glass.getActiveShape() == null) {

            return false;
        }

        if (d != Direction.NORTH) {

            glass.getActiveShape().move(d);
        }

        return true;
    }

    public boolean rotateActiveShape() {

        if (glass.getActiveShape() == null) {

            return false;
        }

        glass.getActiveShape().rotate();
        return true;
    }



    //Слушает ситуацию в стакане
    private class GlassObserver implements GlassListener {

        @Override
        public void glassFilled() {
            fireGameFinished();
        }

        @Override
        public void glassContentChanged(GlassEvent e) {

            GameEvent event = new GameEvent(this);
            event.setGlassElements(e.getGlassElements());
            Shape nextActiveShape = glass.getShapeFactory().getNextShape();
            ArrayList<Element> NextActiveShapeElements = new ArrayList<>();
            for (int i = 0; i < nextActiveShape.elementsCount(); i++) {

                NextActiveShapeElements.add(nextActiveShape.getElement(i));
            }
            event.setNextActiveShape(NextActiveShapeElements);
            fireGlassContentChanged(event);
        }

    }

    //Получает следующую фигуру
    private class ShapeFactoryObserver implements ShapeFactoryListener {

        @Override
        public void nextShapeChanged(GameEvent e) {

            GameEvent event = new GameEvent(this);
            Shape nextActiveShape = glass.getShapeFactory().getNextShape();
            ArrayList<Element> NextActiveShapeElements = new ArrayList<>();
            for (int i = 0; i < nextActiveShape.elementsCount(); i++) {

                NextActiveShapeElements.add(nextActiveShape.getElement(i));
            }
            event.setNextActiveShape(NextActiveShapeElements);
            fireNextShapeChanged(event);
        }

    }

    //Получает очки от очищенного ряда
    private class HorizontalRowObserver implements HorizontalRowListener {

        @Override
        public void HorizontalRowCleared(HorizontalRowEvent e) {
            
            changeScore(e.getScore());
            fireScoreChanged();
        }
    }


    //!!!Передает события UI
    private ArrayList<GameListener> listeners = new ArrayList<>();

    public void addGameListener(GameListener l) {

        if (!listeners.contains(l)) {

            listeners.add(l);
        }
    }

    public void removeGameListener(GameListener l) {

        listeners.remove(l);
    }

    public void fireGameFinished() {

        for (GameListener l: listeners) {

            l.gameFinished();
        }
    }

    public void fireScoreChanged() {

        for (GameListener l: listeners) {

            l.scoreChanged();
        }
    }

    private void fireGlassContentChanged(GameEvent e) {

        for (GameListener l: listeners) {

            l.glassContentChanged(e);
        }
    }

    private void fireNextShapeChanged(GameEvent e) {

        for (GameListener l: listeners) {

            l.nextShapeChanged(e);
        }
    }
}
