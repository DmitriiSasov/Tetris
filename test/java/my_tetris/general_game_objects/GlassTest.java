package my_tetris.general_game_objects;

import my_tetris.Element;
import my_tetris.HorizontalRow;
import my_tetris.ShapeFactory;
import my_tetris.events.GlassEvent;
import my_tetris.events.GlassListener;
import my_tetris.events.HorizontalRowEvent;
import my_tetris.events.HorizontalRowListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class GlassTest {

    private Glass glass;

    @BeforeEach
    void setUp() {

        glass = new Glass(10,10);
    }

    @Test
    void constructor_smallBorder() {

        try {

            glass = new Glass(5,5);
            assertTrue(false);

        } catch(RuntimeException ex) {

            assertTrue(true);
        }
    }

    @Test
    void setShapeFactory_elementOnGlassTop() {

        TestGlassObserver observer = new TestGlassObserver();
        glass.addGlassListener(observer);

        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(9).addElement(new Element(5,9));
            glass.setShapeFactory(new ShapeFactory());

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        boolean result = observer.callsCount == 1 && glass.getActiveShape() == null;

        assertTrue(result);
    }

    @Test
    void setShapeFactory_GlassIsEmpty() {

        TestGlassObserver observer = new TestGlassObserver();
        glass.addGlassListener(observer);
        glass.setShapeFactory(new ShapeFactory());

        boolean result = observer.callsCount == 0 && glass.getActiveShape() != null;

        assertTrue(result);
    }

    @Test
    void setActiveShape_noShape() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(0, -1)));
        shapeElements.add(new Element(new Point(1, -1)));
        shapeElements.add(new Element(new Point(2, -1)));
        shapeElements.add(new Element(new Point(2, 0)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);

        boolean result = shape.getGlass() == glass && glass.getActiveShape() == shape;

        assertTrue(result);
    }

    @Test
    void setActiveShape_thereIsShape() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(0, -1)));
        shapeElements.add(new Element(new Point(1, -1)));
        shapeElements.add(new Element(new Point(2, -1)));
        shapeElements.add(new Element(new Point(2, 0)));
        var shape_1 = new Shape(shapeElements);
        glass.setActiveShape(shape_1);

        shapeElements.remove(3);
        shapeElements.add(new Element(new Point(3, -1)));
        var shape_2 = new Shape(shapeElements);
        glass.setActiveShape(shape_2);

        boolean result = shape_2.getGlass() == glass && glass.getActiveShape() == shape_2
                && shape_1.getGlass() == null;

        assertTrue(result);

    }

    @Test
    void unsetActiveShape_noShape() {

        glass.unsetActiveShape();
        boolean result = glass.getActiveShape() == null;

        assertTrue(result);
    }

    @Test
    void unsetActiveShape_thereIsShape() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(0, -1)));
        shapeElements.add(new Element(new Point(1, -1)));
        shapeElements.add(new Element(new Point(2, -1)));
        shapeElements.add(new Element(new Point(2, 0)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);

        glass.unsetActiveShape();
        boolean result = glass.getActiveShape() == null && shape.getGlass() == null;

        assertTrue(result);
    }

    @Test
    void isFreePosition_posOutOfBorder() {

        assertFalse(glass.isFreePosition(new Point(-1, 10)));
    }

    @Test
    void isFreePosition_posHigherMaxRowIndex() {

        assertTrue(glass.isFreePosition(new Point(0, 11)));
    }

    @Test
    void isFreePosition_thereIsElementInPos() {

        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(0).addElement(new Element(0,0));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        assertFalse(glass.isFreePosition(new Point(0,0)));
    }

    @Test
    void isFreePosition_posIsFree() {

        assertTrue(glass.isFreePosition(new Point(0, 0)));
    }

    @Test
    void absorbActiveShape_rowsAreNotCleared() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(5, 3)));
        shapeElements.add(new Element(new Point(6, 3)));
        shapeElements.add(new Element(new Point(6, 2)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);
        try {

            Field f = glass.getClass().getDeclaredField("shapeFactory");
            f.setAccessible(true);
            f.set(glass, new ShapeFactory());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        glass.absorbActiveShape();

        boolean result = shape.getGlass() == null && glass.getActiveShape() != shape
                && glass.getActiveShape() != null;

        ArrayList<HorizontalRow> rows = null;

        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(0).addElement(new Element(0,0));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        result = result && rows.get(2).elementsCount() == 1 && rows.get(3).elementsCount() == 3
                && shape.elementsCount() == 0;

        for (int i = 0; i < rows.get(3).elementsCount() && result; i++) {

            result = result && rows.get(3).getElement(i).equals(shapeElements.get(i));
        }
        result = result && rows.get(2).getElement(0).equals(shapeElements.get(3));

        assertTrue(result);
    }

    @Test
    void absorbActiveShape_oneRowCleared() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(5, 3)));
        shapeElements.add(new Element(new Point(6, 3)));
        shapeElements.add(new Element(new Point(6, 2)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);
        TestHorizontalRowObserver rowObserver = new TestHorizontalRowObserver();
        glass.addHorizontalRowListener(rowObserver);

        ArrayList<HorizontalRow> rows = null;
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            rows = (ArrayList<HorizontalRow>) f.get(glass);
            f = glass.getClass().getDeclaredField("shapeFactory");
            f.setAccessible(true);
            f.set(glass, new ShapeFactory());

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < glass.getColCount(); i++) {

            if (i != 6) {

                rows.get(2).addElement(new Element(i, 2));
            }
        }
        rows.get(3).addElement(new Element(3, 3));
        rows.get(4).addElement(new Element(3, 4));

        glass.absorbActiveShape();

        boolean result = rowObserver.callsCount == 1;

        result = result && rows.get(2).elementsCount() == 4 && rows.get(3).elementsCount() == 1
                && rows.get(4).elementsCount() == 0 && shape.elementsCount() == 0;

        result = result && rows.get(2).getElement(0).equals(new Element(3, 2));
        for (int i = 0; i < rows.get(2).elementsCount() - 1; i++) {

            Element tmp = new Element (shapeElements.get(i).getCol(), shapeElements.get(i).getRow() - 1);
            rows.get(2).getElement(i + 1).equals(tmp);
        }
        result = result && rows.get(3).getElement(0).equals(new Element(3, 3));

        assertTrue(result);
    }

    @Test
    void absorbActiveShape_twoRowCleared() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(5, 3)));
        shapeElements.add(new Element(new Point(6, 3)));
        shapeElements.add(new Element(new Point(6, 2)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);
        TestHorizontalRowObserver rowObserver = new TestHorizontalRowObserver();
        glass.addHorizontalRowListener(rowObserver);

        ArrayList<HorizontalRow> rows = null;
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            rows = (ArrayList<HorizontalRow>) f.get(glass);
            f = glass.getClass().getDeclaredField("shapeFactory");
            f.setAccessible(true);
            f.set(glass, new ShapeFactory());

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < glass.getColCount(); i++) {

            if (i != 6) { rows.get(2).addElement(new Element(i, 2)); }
        }
        for (int i = 0; i < glass.getColCount(); i++) {

            if (i < 4 || i > 6) { rows.get(3).addElement(new Element(i, 3)); }
        }
        rows.get(4).addElement(new Element(3, 4));

        glass.absorbActiveShape();

        boolean result = rowObserver.callsCount == 2;

        result = result && rows.get(2).elementsCount() == 1 && rows.get(3).elementsCount() == 0
                && rows.get(4).elementsCount() == 0 && shape.elementsCount() == 0;

        result = result && rows.get(2).getElement(0).equals(new Element(3, 2));

        assertTrue(result);
    }

    @Test
    void absorbActiveShape_lastRowFilled() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 9)));
        shapeElements.add(new Element(new Point(5, 9)));
        shapeElements.add(new Element(new Point(6, 9)));
        shapeElements.add(new Element(new Point(7, 9)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);
        TestGlassObserver glassObserver = new TestGlassObserver();
        glass.addGlassListener(glassObserver);
        glass.absorbActiveShape();

        boolean result = glassObserver.callsCount == 1;

        ArrayList<HorizontalRow> rows = null;
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            rows = (ArrayList<HorizontalRow>) f.get(glass);

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }

        result = result && rows.get(9).elementsCount() == 4 && shape.elementsCount() == 0;

        for (int i = 0; i < rows.get(9).elementsCount() && result; i++) {

            result = result && rows.get(9).getElement(i).equals(shapeElements.get(i));
        }

        assertTrue(result);
    }

    @Test
    void absorbActiveShape_onPlaceOfShapeElementThereIsOtherShapeElement() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(5, 3)));
        shapeElements.add(new Element(new Point(6, 3)));
        shapeElements.add(new Element(new Point(6, 2)));
        var shape = new Shape(shapeElements);
        glass.setActiveShape(shape);
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(2).addElement(new Element(6,2));
            f = glass.getClass().getDeclaredField("shapeFactory");
            f.setAccessible(true);
            f.set(glass, new ShapeFactory());

            glass.absorbActiveShape();
            assertTrue(false);

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();

        } catch (RuntimeException ex) {

            assertTrue(true);
        }


        //При поглощении фигуры в ряд добавить элемент с такой же позицией, как и у одного из элементов фигуры
    }

    //Класс для тестирования сигналов, испущенных стаканом
    private class TestGlassObserver implements GlassListener {

        private int callsCount = 0;

        @Override
        public void glassFilled() {

            ++callsCount;
        }

        @Override
        public void glassContentChanged(GlassEvent e) {

        }
    }

    //Класс для тестирования сигналов, испущенных стаканом
    private class TestHorizontalRowObserver implements HorizontalRowListener {

        private int callsCount = 0;

        @Override
        public void HorizontalRowCleared(HorizontalRowEvent e) {

            if (e.getScore() == glass.getColCount()) {

                ++callsCount;
            }
        }
    }

}