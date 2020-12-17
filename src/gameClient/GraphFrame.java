package gameClient;

import api.*;

import javax.swing.*;
import java.awt.*;

/**
 * This class represent a frame, the frame size is 60% from the screen size.
 * this frame class is an integral part of GraphPanel.
 *
 */
public class GraphFrame extends JFrame {
    private Arena _ar = new Arena();
    private GraphLabel gl;


    public GraphFrame(directed_weighted_graph g) {
        super();
        this.setTitle("OOP-directional weighted graph-catch the pokemons");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//if the frame will close then all the program will be closed
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();//get the screen size
        int height = (int) (dimension.height * 0.6);
        int width = (int) (dimension.width * 0.6);
        this.setSize(width, height);
        this.setVisible(true);
    }

    public void update(Arena ar) {
        this._ar = ar;
    }
}
