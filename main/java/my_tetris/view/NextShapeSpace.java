package my_tetris.view;

import my_tetris.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NextShapeSpace extends JPanel {

    private int spaceHeight = 0;

    private int spaceWidth = 0;

    private final int CELL_SIZE = 20;

    private final Color EMPTY_CELL_COLOR = new Color(245, 245, 245);

    private final Color CELL_BORDER_COLOR = new Color(131, 139, 131);

    public NextShapeSpace(int spaceHeight, int spaceWidth) {

        this.spaceHeight = spaceHeight;

        this.spaceWidth = spaceWidth;

        setLayout(new GridLayout(spaceHeight, spaceWidth));
        for (int row = 1; row <= spaceHeight; row++)
        {
            for (int col = 1; col <= spaceWidth; col++)
            {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE,CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(CELL_BORDER_COLOR, 1));
                cell.setBackground(EMPTY_CELL_COLOR);
                add(cell);
            }

        }
    }

    private JPanel getCell(Point pos) {

        int index = 0;
        Component widgets[] = getComponents();
        for(int i = widgets.length - 1; i >= 0; --i)
        {
            if(widgets[i] instanceof JPanel)
            {
                if(index == spaceWidth * (pos.y) + spaceWidth - 1 - pos.x)
                {
                    return (JPanel)widgets[i];
                }
                index++;
            }
        }

        return null;
    }

    public void update(ArrayList<Element> nextShapeElements) {

        for (Component widget : getComponents()) {

            if (widget instanceof JPanel && widget.getBackground() != EMPTY_CELL_COLOR) {
                widget.setBackground(EMPTY_CELL_COLOR);
            }
        }

        for (Element tmp : nextShapeElements) {

            getCell(new Point(tmp.getCol(), tmp.getRow())).setBackground(tmp.getColor());
        }
    }
}
