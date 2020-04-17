package my_tetris.view;

import my_tetris.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GlassField extends JPanel {

    private int glassFieldHeight = 0;

    private int glassFieldWidth = 0;

    private final int CELL_SIZE = 20;

    private final Color EMPTY_CELL_COLOR = new Color(245, 245, 245);

    private final Color CELL_BORDER_COLOR = new Color(131, 139, 131);

    public GlassField(int glassFieldHeight, int glassFieldWidth){

        this.glassFieldHeight = glassFieldHeight;

        this.glassFieldWidth = glassFieldWidth;

        setLayout(new GridLayout(glassFieldHeight, glassFieldWidth));

        for (int row = 1; row <= glassFieldHeight; row++)
        {
            for (int col = 1; col <= glassFieldWidth; col++)
            {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE,CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(CELL_BORDER_COLOR, 1));
                cell.setBackground(EMPTY_CELL_COLOR);
                add(cell);
            }
        }
    }

    private JPanel getCell( Point pos) {

        int index = 0;
        Component widgets[] = getComponents();
        for(int i = widgets.length - 1; i >= 0; --i)
        {
            if(widgets[i] instanceof JPanel)
            {
                if(index == glassFieldWidth * (pos.y) + glassFieldWidth - 1 - pos.x)
                {
                    return (JPanel)widgets[i];
                }
                index++;
            }
        }

        return null;
    }


    public void update(ArrayList<Element> glassElements) {

        for (Component widget : getComponents()) {

            if (widget instanceof JPanel && widget.getBackground() != EMPTY_CELL_COLOR) {
                widget.setBackground(EMPTY_CELL_COLOR);
            }
        }

        for (Element tmp : glassElements) {

            getCell(new Point(tmp.getCol(), tmp.getRow())).setBackground(tmp.getColor());
        }
    }
}
