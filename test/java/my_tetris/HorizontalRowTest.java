package my_tetris;

import my_tetris.events.HorizontalRowEvent;
import my_tetris.events.HorizontalRowListener;
import my_tetris.general_game_objects.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HorizontalRowTest {

    private HorizontalRow row_1;


    @BeforeEach
    void setUp() {

        row_1 = new HorizontalRow(4);
    }

    @Test
    void constructor_ErrorMaxSize() {

        try {

            var row_1 = new HorizontalRow(-1);
            assertTrue(false);

        } catch (RuntimeException ex) {

            assertTrue(true);
        }
    }

    @Test
    void getElement_correctIndex() {

        var element_1 = new Element(1,1);
        row_1.addElement(element_1);
        var element_2 = new Element(2,1);
        row_1.addElement(element_2);
        boolean result = row_1.getElement(1).equals(element_2);

        assertTrue(result);
    }

    @Test
    void getElement_incorrectIndex() {

       boolean result = row_1.getElement(1) == null;

        assertTrue(result);
    }

    @Test
    void hasElement_thereIsElement() {

        var element = new Element(1,1);
        row_1.addElement(element);
        boolean result = row_1.hasElement(element);

        assertTrue(result);
    }

    @Test
    void hasElement_noElement() {

        var element = new Element(1,1);
        boolean result = row_1.hasElement(element);

        assertFalse(result);
    }

    @Test
    void addElement() {

        var element = new Element(1,1);
        row_1.addElement(element);

        Element addedElement = null;
        int elementCount = 0;
        try {

            Field f = row_1.getClass().getDeclaredField("elements");
            f.setAccessible(true);
            addedElement = ((ArrayList<Element>)f.get(row_1)).get(0);
            elementCount = ((ArrayList<Element>)f.get(row_1)).size();

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        boolean result = elementCount == 1 && addedElement.equals(element);

        assertTrue(result);
    }

    @Test
    void addElement_repeatedElement() {

        var element_1 = new Element(1,1); //Создание непустого ряда до теста
        row_1.addElement(element_1);
        var element_2 = new Element(1,1); //Создание непустого ряда до теста

        boolean result = !row_1.addElement(element_2);
        result = result && row_1.elementsCount() == 1 && row_1.getElement(0).equals(element_1);

        assertTrue(result);
    }

    @Test
    void addElement_rowBecameFull_nextRowIsEmpty() {

        HorizontalRow row_2 = new HorizontalRow(4);
        TestHorizontalRowObserver observer_1 = new TestHorizontalRowObserver();
        TestHorizontalRowObserver observer_2 = new TestHorizontalRowObserver();
        row_1.addHorizontalRowListener(observer_1);
        row_2.addHorizontalRowListener(observer_2);

        int row_1_MaxSize = 4;
        for (int i = 0; i < row_1_MaxSize - 1; i++) {

            row_1.addElement(new Element(1, i));
        }

        row_1.setHigherHorizontalRow(row_2);
        row_1.addElement(new Element(1, 3));

        boolean result = row_1.elementsCount() == 0 && 0 == row_2.elementsCount() && observer_1.callsCount == 1
                && observer_2.callsCount == 0;

        assertTrue(result);
    }

    @Test
    void addElement_rowBecameFull_nextRowIsNoEmpty() {

        int row_1_MaxSize = 4;
        HorizontalRow row_2 = new HorizontalRow(4);
        ArrayList<Element> row_2_ElementsBeforeAdd = new ArrayList<>();
        row_1.setHigherHorizontalRow(row_2);
        for (int i = 0; i < row_1_MaxSize - 1; i++) {

            row_1.addElement(new Element(1, i));
            row_2.addElement(new Element(2, i + 1));
            row_2_ElementsBeforeAdd.add(new Element(2, i + 1));
        }

        row_1.addElement(new Element(1, 3));

        boolean result = row_1.elementsCount() == row_2_ElementsBeforeAdd.size()
                && row_2.elementsCount() == 0;

        for (int i = 0; i < row_1.elementsCount() && result; i++) {

            Element tmp = new Element(row_2_ElementsBeforeAdd.get(i).getCol(),
                    row_2_ElementsBeforeAdd.get(i).getRow() - 1);
            result = result && row_1.getElement(i).equals(tmp);
        }

        assertTrue(result);
    }

    @Test
    void addElement_rowBecameFull_allRowsAreNoEmpty() {

        int rowsMaxSize = 4;
        HorizontalRow row_2 = new HorizontalRow(4);
        HorizontalRow row_3 = new HorizontalRow(4);

        TestHorizontalRowObserver observer_1 = new TestHorizontalRowObserver();
        TestHorizontalRowObserver observer_2 = new TestHorizontalRowObserver();
        TestHorizontalRowObserver observer_3 = new TestHorizontalRowObserver();
        row_1.addHorizontalRowListener(observer_1);
        row_2.addHorizontalRowListener(observer_2);
        row_3.addHorizontalRowListener(observer_3);

        ArrayList<Element> row_2_ElementsBeforeAdd = new ArrayList<>();
        ArrayList<Element>  row_3_ElementsBeforeAdd = new ArrayList<>();
        row_1.setHigherHorizontalRow(row_2);
        row_2.setHigherHorizontalRow(row_3);

        for (int i = 0; i < rowsMaxSize - 1; i++) {

            row_1.addElement(new Element(1, i));
            row_2.addElement(new Element(2, i + 1));
            row_2_ElementsBeforeAdd.add(new Element(2, i + 1));
            row_3.addElement(new Element(3, i + 2));
            row_3_ElementsBeforeAdd.add(new Element(3, i + 2));
        }

        row_1.addElement(new Element(1, 3));

        boolean result = row_1.elementsCount() == row_2_ElementsBeforeAdd.size()
                && row_2.elementsCount() == row_3_ElementsBeforeAdd.size() && row_3.elementsCount() == 0;

        result = result && observer_1.callsCount == 1 && observer_2.callsCount == 0 && observer_3.callsCount == 0;

        for (int i = 0; i < row_1.elementsCount() && result; i++) {

            Element tmp = new Element(row_2_ElementsBeforeAdd.get(i).getCol(),
                    row_2_ElementsBeforeAdd.get(i).getRow() - 1);
            result = result && row_1.getElement(i).equals(tmp);

            tmp = new Element(row_3_ElementsBeforeAdd.get(i).getCol(),
                    row_3_ElementsBeforeAdd.get(i).getRow() - 1);
            result = result && row_2.getElement(i).equals(tmp);
        }

        assertTrue(result);
    }

    @Test
    void addElement_RowsHaveDifferentSize() {

        int row_1_MaxSize = 4;
        ArrayList<Element> row_1_ElementsBeforeAdd = new ArrayList<>();
        for (int i = 0; i < row_1_MaxSize - 2; i++) {
            row_1_ElementsBeforeAdd.add(new Element(1, i));
            row_1.addElement(new Element(1, i));
        }

        var row_0 = new HorizontalRow(2);
        row_0.setHigherHorizontalRow(row_1);
        for (int i = 0; i < row_1_MaxSize - 2; i++) {

            row_0.addElement(new Element(0, i));
        }

        boolean result = row_0.elementsCount() == row_1_ElementsBeforeAdd.size() && row_1.elementsCount() == 0;

        for (int i = 0; i < row_0.elementsCount() && result; i++) {

            Element tmp = new Element(row_1_ElementsBeforeAdd.get(i).getCol(),
                    row_1_ElementsBeforeAdd.get(i).getRow() - 1);
            result = result && row_0.getElement(i).equals(tmp);
        }

        assertTrue(result);
    }

    @Test
    void addElement_RowMoreThenOther() {

        int row_1_MaxSize = 4;
        for (int i = 0; i < row_1_MaxSize - 1; i++) {

            row_1.addElement(new Element(1, i));
        }

        var row_0 = new HorizontalRow(1);
        row_0.setHigherHorizontalRow(row_1);

        try {

            row_0.addElement(new Element(1,2));
            assertTrue(false);

        } catch (RuntimeException ex) {

            assertTrue(true);
        }
    }

    //Класс для тестирования сигналов, испущенных горизонтальным рядом
    private class TestHorizontalRowObserver implements HorizontalRowListener {

        private int callsCount = 0;

        @Override
        public void HorizontalRowCleared(HorizontalRowEvent e) {

            if (e.getScore() == row_1.getMaxSize()) {

                ++callsCount;
            }
        }
    }
}