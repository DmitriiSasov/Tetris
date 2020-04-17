package my_tetris.view;

import my_tetris.Direction;
import my_tetris.Element;
import my_tetris.Game;
import my_tetris.events.GameEvent;
import my_tetris.events.GameListener;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JFrame{

    private final int GLASS_HEIGHT = 20;

    private final int GLASS_WIDTH = 10;

    private final int NEXT_SHAPE_SPACE_HEIGHT = 2;

    private final int NEXT_SHAPE_SPACE_WIDTH = 4;

    private final int CELL_SIZE = 20;

    private final Color EMPTY_CELL_COLOR = new Color(245, 245, 245);

    private final Color CELL_BORDER_COLOR = new Color(131, 139, 131);


    private Game game = null;

    /**
     * Creates new form GamePanel
     */
    public GamePanel() {

        game = new Game();
        game.addGameListener(new GameObserver());

        initComponents();

    }

    private void createGlassSpace(){

        glassPanel = new JPanel();
        glassPanel.setLayout(new GridLayout(GLASS_HEIGHT, GLASS_WIDTH));

        for (int row = 1; row <= GLASS_HEIGHT; row++)
        {
            for (int col = 1; col <= GLASS_WIDTH; col++)
            {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE,CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(CELL_BORDER_COLOR, 1));
                cell.setBackground(EMPTY_CELL_COLOR);
                glassPanel.add(cell);
            }
        }
        content.add(glassPanel, BorderLayout.WEST);
    }

    private void createNextShapeSpace() {

        nextShapeLabel = new JLabel("    Следующая фигура");
        nextShapeLabel.setPreferredSize(new Dimension(150, 25));
        leftSidePanel.add(nextShapeLabel);

        nextShapePanel = new JPanel();
        nextShapePanel.setLayout(new GridLayout(NEXT_SHAPE_SPACE_HEIGHT, NEXT_SHAPE_SPACE_WIDTH));
        for (int row = 1; row <= NEXT_SHAPE_SPACE_HEIGHT; row++)
        {
            for (int col = 1; col <= NEXT_SHAPE_SPACE_WIDTH; col++)
            {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE,CELL_SIZE));
                cell.setBorder(BorderFactory.createLineBorder(CELL_BORDER_COLOR, 1));
                cell.setBackground(EMPTY_CELL_COLOR);
                nextShapePanel.add(cell);
            }

        }
        leftSidePanel.add(nextShapePanel);
    }

    private JPanel getCell(JPanel panel, Point pos, int panelWidth) {

        int index = 0;
        Component widgets[] = panel.getComponents();
        for(int i = widgets.length - 1; i >= 0; --i)
        {
            if(widgets[i] instanceof JPanel)
            {
                if(index == panelWidth * (pos.y) + panelWidth - 1 - pos.x)
                {
                    return (JPanel)widgets[i];
                }
                index++;
            }
        }

        return null;
    }


    private void initComponents() {

        setTitle("Test_GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                glassPanelKeyPressed(evt);
            }
        });

        content = (JPanel) this.getContentPane();
        content.setPreferredSize(new Dimension(360,400));
        setResizable(false);

        createGlassSpace();

        leftSidePanel = new JPanel();
        leftSidePanel.setPreferredSize(new Dimension(150, 380));
        leftSidePanel.setLayout(new FlowLayout());

        scoreLabel = new JLabel("Счет");
        scoreLabel.setPreferredSize(new Dimension(120, 25));
        leftSidePanel.add(scoreLabel);

        scoreField = new JTextField();
        scoreField.setEditable(false);
        scoreField.setFocusable(false);
        scoreField.setPreferredSize(new Dimension(130, 25));
        leftSidePanel.add(scoreField);

        var emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(150, 120));
        leftSidePanel.add(emptyPanel);

        createNextShapeSpace();

        emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(150, 90));
        leftSidePanel.add(emptyPanel);

        startGameButton = new JButton("Начать игру");
        startGameButton.setPreferredSize(new Dimension(120, 25));
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStartActionPerformed(evt);
            }
        });
        leftSidePanel.add(startGameButton);

        content.add(leftSidePanel, BorderLayout.EAST);
        pack();
        setVisible(true);
        setFocusable(true);



    }

    private JPanel content;
    private JPanel leftSidePanel;
    private javax.swing.JButton startGameButton;
    private javax.swing.JLabel scoreLabel;
    private javax.swing.JLabel nextShapeLabel;
    private javax.swing.JPanel glassPanel;
    private javax.swing.JPanel nextShapePanel;
    private javax.swing.JTextField scoreField;

    //Слушает нажатие кнопок и по ним сообщает игре, как нужно переместить/повернуть фигуру
    private void glassPanelKeyPressed(java.awt.event.KeyEvent evt) {

        if (game != null && game.isInProgress()) {
            if (evt.getKeyCode() == evt.VK_RIGHT) {
                game.moveActiveShape(Direction.EAST);
            } else if (evt.getKeyCode() == evt.VK_LEFT) {
                game.moveActiveShape(Direction.WEST);
            }  else if (evt.getKeyCode() == evt.VK_DOWN) {
                game.moveActiveShape(Direction.SOUTH);
            } else if (evt.getKeyCode() == evt.VK_UP) {
                game.rotateActiveShape();
            }

        }
    }

    private void bStartActionPerformed(java.awt.event.ActionEvent evt) {

        startGameButton.setEnabled(false);
        game.start();
    }


    //Слушает события от игры, изменилась ли обстановка, заполнился ли стакан, какой текущий счет
    private class GameObserver implements GameListener {

        @Override
        public void gameFinished() {

            startGameButton.setEnabled(true);
            String str = "Игра закончилась.\nВаш счет: " + game.getScore();
            JOptionPane.showMessageDialog(null, str, "Конец игры", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public void scoreChanged() {
            scoreField.setText(game.getScore() + "");
        }

        @Override
        public void glassContentChanged(GameEvent e) {

            for (Component widget : glassPanel.getComponents()) {

                if (widget instanceof JPanel && widget.getBackground() != EMPTY_CELL_COLOR) {
                    widget.setBackground(EMPTY_CELL_COLOR);
                }
            }

            for (Component widget : nextShapePanel.getComponents()) {

                if (widget instanceof JPanel && widget.getBackground() != EMPTY_CELL_COLOR) {
                    widget.setBackground(EMPTY_CELL_COLOR);
                }
            }

            for (Element tmp : e.getGlassElements()) {

                getCell(glassPanel, new Point(tmp.getCol(), tmp.getRow()),
                        GLASS_WIDTH).setBackground(tmp.getColor());
            }

            for (Element tmp : e.getNextActiveShape()) {

                getCell(nextShapePanel, new Point(tmp.getCol(), tmp.getRow()),
                        NEXT_SHAPE_SPACE_WIDTH).setBackground(tmp.getColor());
            }
        }
    }

}
