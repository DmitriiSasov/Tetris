package my_tetris.general_game_objects;

import my_tetris.Direction;
import my_tetris.Element;
import my_tetris.HorizontalRow;
import my_tetris.ShapeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    private Shape shape;

    private boolean equalPositions(Shape other) {

        if (shape.elementsCount() != other.elementsCount()) {
            return false;
        }

        for (int i = 0; i < shape.elementsCount(); i++) {

            if (!shape.getElement(i).equals(other.getElement(i))) {
                return false;
            }
        }

        return true;
    }

    @BeforeEach
    void setUp() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(2, -2)));
        shapeElements.add(new Element(new Point(2, -1)));
        shapeElements.add(new Element(new Point(2, 0)));
        shapeElements.add(new Element(new Point(1, 0)));
        shape = new Shape(shapeElements);
    }

    @Test
    void shapeConstructor_correctElementsSize() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(2, -2)));
        shapeElements.add(new Element(new Point(2, -1)));
        shapeElements.add(new Element(new Point(2, 0)));
        shapeElements.add(new Element(new Point(1, 0)));

        boolean result = shape.elementsCount() == shapeElements.size();

        for (int i = 0; i < shape.elementsCount() && result; ++i) {

            result = result && shape.getElement(i).equals(shapeElements.get(i));
        }

        result = result && shape.getGlass() == null;

        assertTrue(result);
    }

    @Test
    void shapeConstructor_incorrectElementsSize() {

        ArrayList<Element> shapeElements = new ArrayList<>();
        try {

            var shape = new Shape(shapeElements);
            assertTrue(false);

        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    void setGlass_shapeWithoutGlass() {

        var glass = new Glass(10,20);
        shape.setGlass(glass);
        boolean result = shape.getGlass() == glass;
        result = result && glass.getActiveShape() == shape;

        assertTrue(result);
    }

    @Test
    void setGlass_shapeWithGlass() {

        var glass = new Glass(10,20);
        shape.setGlass(glass);
        var glass_2 = new Glass(10,10);
        shape.setGlass(glass_2);
        boolean result = shape.getGlass() == glass_2;
        result = result && glass_2.getActiveShape() == shape && shape.getGlass() == glass_2
                && glass.getActiveShape() == null;

        assertTrue(result);
    }

    @Test
    void unsetGlass_shapeWithoutGlass() {

        shape.unsetGlass();
        boolean result = shape.getGlass() == null;

        assertTrue(result);
    }

    @Test
    void unsetGlass_shapeWithGlass() {

        var glass = new Glass(10,10);
        shape.setGlass(glass);
        shape.unsetGlass();
        boolean result = shape.getGlass() == null && glass.getActiveShape() == null;

        assertTrue(result);
    }

    @Test
    void takeElement_elementExists() {

        ArrayList<Element> elementsBeforeTake = new ArrayList<>();
        for (int i = 0; i < shape.elementsCount(); ++i) {

            elementsBeforeTake.add(shape.getElement(i));
        }

        boolean result = shape.takeElement(2).equals(elementsBeforeTake.get(2));

        ArrayList<Element> elementsAfterTake = new ArrayList<>();
        for (int i = 0; i < shape.elementsCount(); ++i) {

            elementsAfterTake.add(shape.getElement(i));
        }

        result = result && elementsAfterTake.size() + 1 == elementsBeforeTake.size();

        for (int i = 0; i < elementsAfterTake.size() && result; ++i) {

            if (i != 2) {

                result = result && elementsAfterTake.get(i).equals(elementsBeforeTake.get(i));

            } else {

                result = result && elementsAfterTake.get(i).equals(elementsBeforeTake.get(i + 1));
            }

        }

        assertTrue(result);
    }

    @Test
    void takeElement_noElement() {

        ArrayList<Element> elementsBeforeTake = new ArrayList<>();
        for (int i = 0; i < shape.elementsCount(); ++i) {

            elementsBeforeTake.add(shape.getElement(i));
        }

        boolean result = shape.takeElement(4) == null;

        ArrayList<Element> elementsAfterTake = new ArrayList<>();
        for (int i = 0; i < shape.elementsCount(); ++i) {

            elementsAfterTake.add(shape.getElement(i));
        }

        result = result && elementsAfterTake.size() == elementsBeforeTake.size();

        for (int i = 0; i < elementsBeforeTake.size() && result; ++i) {

            result = result && elementsAfterTake.get(i).equals(elementsBeforeTake.get(i));
        }

        assertTrue(result);
    }

    @Test
    void getElement_elementExists() {

        Element result = shape.getElement(2);
        Element expResult = null;
        try {
            Field f = shape.getClass().getDeclaredField("elements");
            f.setAccessible(true);
            expResult = ((ArrayList<Element>)f.get(shape)).get(2);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        assertTrue(expResult.equals(result));
    }

    @Test
    void getElement_noElement() {

        Element result = shape.getElement(4);

        assertTrue(result == null);
    }

    @Test
    void rotate() {

        var glass = new Glass(10,10);
        shape.setGlass(glass);
        shape.setStartPosition(0, glass.getRowCount() - 1,
                0, glass.getColCount() - 1);
        shape.rotate();

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 8)));
        shapeElements.add(new Element(new Point(5, 8)));
        shapeElements.add(new Element(new Point(6, 8)));
        shapeElements.add(new Element(new Point(6, 9)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_nearOtherShapeElement() {

        var glass = new Glass(10,10);
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(7).addElement(new Element(6,7));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
        shape.setGlass(glass);
        shape.setStartPosition(0, glass.getRowCount() - 1,
                0, glass.getColCount() - 1);

        shape.rotate();

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 8)));
        shapeElements.add(new Element(new Point(5, 8)));
        shapeElements.add(new Element(new Point(6, 8)));
        shapeElements.add(new Element(new Point(6, 9)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_BarrierHindersAbove() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 9)));
        shapeElements.add(new Element(new Point(5, 9)));
        shapeElements.add(new Element(new Point(6, 9)));
        shapeElements.add(new Element(new Point(7, 9)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.rotate();

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(5, 10)));
        shapeElements.add(new Element(new Point(5, 9)));
        shapeElements.add(new Element(new Point(5, 8)));
        shapeElements.add(new Element(new Point(5, 7)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_BarrierHindersBelow() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 1)));
        shapeElements.add(new Element(new Point(5, 1)));
        shapeElements.add(new Element(new Point(6, 1)));
        shapeElements.add(new Element(new Point(7, 1)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.rotate();

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_BarrierHindersRight() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 1)));
        shapeElements.add(new Element(new Point(5, 1)));
        shapeElements.add(new Element(new Point(6, 1)));
        shapeElements.add(new Element(new Point(7, 1)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.rotate();

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_BarrierHindersLeft() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(8, 4)));
        shapeElements.add(new Element(new Point(8, 5)));
        shapeElements.add(new Element(new Point(8, 6)));
        shapeElements.add(new Element(new Point(8, 7)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.rotate();

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void rotate_shapeElementsHinder() {

        var glass = new Glass(10,10);
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(8).addElement(new Element(4,8));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
        shape.setGlass(glass);
        shape.setStartPosition(0, glass.getRowCount() - 1,
                0, glass.getColCount() - 1);

        shape.rotate();

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(5, 7)));
        shapeElements.add(new Element(new Point(5, 8)));
        shapeElements.add(new Element(new Point(5, 9)));
        shapeElements.add(new Element(new Point(4, 9)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void setStartPosition_correctBorder() {

        var glass = new Glass(10,20);
        shape.setGlass(glass);
        shape.setStartPosition(0, 19, 0, 9);

        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(5, 17)));
        shapeElements.add(new Element(new Point(5,18)));
        shapeElements.add(new Element(new Point(5,19)));
        shapeElements.add(new Element(new Point(4, 19)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void setStartPosition_incorrectBorder() {

        try {

            shape.setStartPosition(19, 0, -1, -10);
            assertTrue(false);

        } catch (RuntimeException ex) {

            assertTrue(true);
        }
    }

    @Test
    void move_up() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.NORTH);

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shapeElements.add(new Element(new Point(4, 7)));

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_down() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.SOUTH);

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 2)));
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_right() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.WEST);

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(3, 3)));
        shapeElements.add(new Element(new Point(3, 4)));
        shapeElements.add(new Element(new Point(3, 5)));
        shapeElements.add(new Element(new Point(3, 6)));

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_left() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.EAST);

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(5, 3)));
        shapeElements.add(new Element(new Point(5, 4)));
        shapeElements.add(new Element(new Point(5, 5)));
        shapeElements.add(new Element(new Point(5, 6)));

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_borderAbove() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(9, 6)));
        shapeElements.add(new Element(new Point(9, 7)));
        shapeElements.add(new Element(new Point(9, 8)));
        shapeElements.add(new Element(new Point(9, 9)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.NORTH);

        shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(9, 7)));
        shapeElements.add(new Element(new Point(9, 8)));
        shapeElements.add(new Element(new Point(9, 9)));
        shapeElements.add(new Element(new Point(9, 10)));
        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_borderBelow() {

        var glass = new Glass(10,10);
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
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(9, 0)));
        shapeElements.add(new Element(new Point(9, 1)));
        shapeElements.add(new Element(new Point(9, 2)));
        shapeElements.add(new Element(new Point(9, 3)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.SOUTH);

        boolean result = shape.elementsCount() == 0;
        result = result && glass.getActiveShape() != null && glass.getActiveShape() != shape;
        for (int i = 0; i < 4; i++) {

            result = result && rows.get(i).getElement(0).equals(shapeElements.get(i));
        }

        assertTrue(result);
    }

    @Test
    void move_border_Right() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(9, 3)));
        shapeElements.add(new Element(new Point(9, 4)));
        shapeElements.add(new Element(new Point(9, 5)));
        shapeElements.add(new Element(new Point(9, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.EAST);

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_borderLeft() {

        var glass = new Glass(10,10);
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(0, 3)));
        shapeElements.add(new Element(new Point(0, 4)));
        shapeElements.add(new Element(new Point(0, 5)));
        shapeElements.add(new Element(new Point(0, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.WEST);

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

    @Test
    void move_elementBelow() {

        var glass = new Glass(10,10);
        ArrayList<HorizontalRow> rows = null;
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(2).addElement(new Element(4,2));
            f = glass.getClass().getDeclaredField("shapeFactory");
            f.setAccessible(true);
            f.set(glass, new ShapeFactory());

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.SOUTH);

        boolean result = shape.elementsCount() == 0;
        result = result && glass.getActiveShape() != null && glass.getActiveShape() != shape;
        for (int i = 3; i < 7; i++) {

            result = result && rows.get(i).getElement(0).equals(shapeElements.get(i - 3));
        }

        assertTrue(result);
    }

    @Test
    void move_elementRight() {

        var glass = new Glass(10,10);
        try {

            Field f = glass.getClass().getDeclaredField("rows");
            f.setAccessible(true);
            ArrayList<HorizontalRow> rows = (ArrayList<HorizontalRow>) f.get(glass);
            rows.get(3).addElement(new Element(5,3));

        } catch (NoSuchFieldException | IllegalAccessException e) {

            e.printStackTrace();
        }
        ArrayList<Element> shapeElements = new ArrayList<>();
        shapeElements.add(new Element(new Point(4, 3)));
        shapeElements.add(new Element(new Point(4, 4)));
        shapeElements.add(new Element(new Point(4, 5)));
        shapeElements.add(new Element(new Point(4, 6)));
        shape = new Shape(shapeElements);
        shape.setGlass(glass);
        shape.move(Direction.EAST);

        var expectedShape = new Shape(shapeElements);

        assertTrue(equalPositions(expectedShape));
    }

}