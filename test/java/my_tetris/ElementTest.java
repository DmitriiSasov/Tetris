package my_tetris;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {

    /**
     * Test of getCol method, of class Element.
     */
    @org.junit.jupiter.api.Test
    public void testElementConstructor_1() {
        System.out.println("ElementConstructor_1");

        Element instance = new Element(new Point(1,3));
        int[] expResult = {1,3};
        int[] result = {instance.getCol(), instance.getRow()};

        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCol method, of class Element.
     */
    @org.junit.jupiter.api.Test
    public void testElementConstructor_2() {
        System.out.println("ElementConstructor_2");

        Element instance = new Element(-1, 3);
        int[] expResult = {-1, 3};
        int[] result = {instance.getCol(), instance.getRow()};

        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCol method, of class Element.
     */
    @org.junit.jupiter.api.Test
    public void testGetCol() {
        System.out.println("getCol");

        Element instance = new Element(-1, 3);
        int expResult = -1;
        int result = instance.getCol();

        assertEquals(expResult, result);
    }

    /**
     * Test of getRow method, of class Element.
     */
    @org.junit.jupiter.api.Test
    public void testGetRow() {
        System.out.println("getRow");

        Element instance = new Element(-1, 3);
        int expResult = 3;
        int result = instance.getRow();

        assertEquals(expResult, result);
    }

    /**
     * Test of setPosition method, of class Element.
     */
    @org.junit.jupiter.api.Test
    public void testSetPosition() {
        System.out.println("setPosition");

        Element instance = new Element(-1, 3);
        int[] expResult = {2, -10};
        instance.setPosition(2, -10);
        int [] result = {instance.getCol(), instance.getRow()};

        assertArrayEquals(expResult, result);
    }
}