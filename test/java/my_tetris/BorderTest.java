package my_tetris;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BorderTest {

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testConstructorValidValues() {
        System.out.println("isPositionOutsideBorder");

        try {

            Border instance = new Border(-4, -5, 1);
            assertTrue(true);
        }
        catch(RuntimeException ex) {

            assertTrue(false);
        }
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testConstructorInvalidValues() {
        System.out.println("isPositionOutsideBorder");

        try {

            Border instance = new Border(-1, 0, 0);
            assertTrue(false);
        }
        catch(RuntimeException ex) {

            assertTrue(true);
        }
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_lower() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(0, -1);
        Border instance = new Border(0, 0, 9);
        boolean expResult = true;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_higher() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(0, 20);
        Border instance = new Border(0, 0, 9);
        boolean expResult = false;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_right() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(10, 0);
        Border instance = new Border(0, 0, 9);
        boolean expResult = true;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_left() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(-1, 0);
        Border instance = new Border(0, 0, 9);
        boolean expResult = true;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_leftTopPoint() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(0, 19);
        Border instance = new Border(0, 0, 9);
        boolean expResult = false;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }

    /**
     * Test of isPositionOutsideBorder method, of class Border.
     */
    @Test
    public void testIsPositionOutsideBorder_RightBottomPoint() {
        System.out.println("isPositionOutsideBorder");

        Point position = new Point(9, 0);
        Border instance = new Border(0, 0, 9);
        boolean expResult = false;
        boolean result = instance.isPositionOutsideBorder(position);

        assertEquals(expResult, result);
    }
}