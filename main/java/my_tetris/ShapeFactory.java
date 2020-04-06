/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my_tetris;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import my_tetris.general_game_objects.Shape;

/**
 *
 * @author bopoh
 */
public class ShapeFactory {

    /* Шаблон фигуры представлен в системе координат XY (первой указывается X)
     */
    private ArrayList<ArrayList<Element> > shapeTemplates = new ArrayList<>();

    private ArrayList<Color> colors = new ArrayList<>();

    private Shape nextShape;

    public ShapeFactory() {

        ArrayList<Element> shape = new ArrayList<>();
        //Создаем фигуру L
        shape.add(new Element(new Point(1, 0)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(3, 0)));
        shape.add(new Element(new Point(3, 1)));
        shapeTemplates.add(shape);

        //Создаем фигуру J
        shape = new ArrayList<>();
        shape.add(new Element(new Point(3, 0)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(1, 0)));
        shape.add(new Element(new Point(1, 1)));
        shapeTemplates.add(shape);

        //Создаем фигуру I
        shape = new ArrayList<>();
        shape.add(new Element(new Point(0, 0)));
        shape.add(new Element(new Point(1, 0)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(3, 0)));
        shapeTemplates.add(shape);

        //Создаем фигуру S
        shape = new ArrayList<>();
        shape.add(new Element(new Point(3, 1)));
        shape.add(new Element(new Point(2, 1)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(1, 0)));
        shapeTemplates.add(shape);

        //Создаем фигуру Z
        shape = new ArrayList<>();
        shape.add(new Element(new Point(1, 1)));
        shape.add(new Element(new Point(2, 1)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(3, 0)));
        shapeTemplates.add(shape);

        //Создаем фигуру O
        shape = new ArrayList<>();
        shape.add(new Element(new Point(1, 1)));
        shape.add(new Element(new Point(2, 1)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(1, 0)));
        shapeTemplates.add(shape);

        //Создаем фигуру T
        shape = new ArrayList<>();
        shape.add(new Element(new Point(3, 0)));
        shape.add(new Element(new Point(2, 0)));
        shape.add(new Element(new Point(2, 1)));
        shape.add(new Element(new Point(1, 0)));
        shapeTemplates.add(shape);

        colors.add(new Color(144,0,255));
        colors.add(new Color(0,255,212));
        colors.add(new Color(65, 105, 225));
        colors.add(new Color(4,188,254));
        colors.add(new Color(254,0,150));
        colors.add(new Color(254,232,0));
        colors.add(new Color(255, 255, 0));
        colors.add(new Color(255, 140, 0));
        colors.add(new Color(0, 139, 69));
        colors.add(new Color(0, 238, 0));

        ArrayList<Element> elements = shapeTemplates.get(calcNextShapeType());
        setColor(elements);
        nextShape = new Shape(elements);

    }

    public Shape getNextShape() { return nextShape.clone(); }

    private void setColor(ArrayList<Element> elements) {

        Color randomColor = colors.get(calcNextShapeColor());
        for (Element tmp : elements) {

            tmp.setColor(new Color(randomColor.getRGB()));
        }
    }

    private int calcNextShapeColor() {

        return (int)Math.round(Math.random() * (colors.size() - 1));
    }

    private int calcNextShapeType() {

        return (int)Math.round(Math.random() * (shapeTemplates.size() - 1));
    }

    public Shape createShape() {

        var createdShape = nextShape;

        ArrayList<Element> elements = shapeTemplates.get(calcNextShapeType());
        setColor(elements);
        nextShape = new Shape(elements);
        return createdShape;
    }
}



