package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is a basic login gui for the pokemons game
 */

public class GraphLabel extends JFrame implements ActionListener {
    JLabel jl1;
    JLabel jl2;
    JTextField tfID;
    JTextField tfLEVEL;
    JButton jbSTART;
    private int level,id;
    private boolean userIsDone=false;

    public GraphLabel(){
        super();
        this.setTitle("OOP-login to the game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//if the frame will close then all the program will be closed
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (dimension.height * 0.3);//the size will be 30% of the screen size
        int width = (int) (dimension.width * 0.3);
        this.setSize(width, height);
        tfID=new JTextField();
        tfID.setBounds(150,50,100,20);
        tfLEVEL=new JTextField();
        tfLEVEL.setBounds(150,80,100,20);
        jl1=new JLabel();
        jl1.setBounds(30,50,100,20);
        jl1.setText("insert your id:");
        jl2=new JLabel();
        jl2.setBounds(30,80,100,20);
        jl2.setText("insert level:");
        jbSTART =new JButton("Start");
        jbSTART.setBounds(130,110,80,40);
        jbSTART.setBackground(Color.cyan);
        jbSTART.addActionListener(this);
        this.add(jl1);
        this.add(jl2);
        this.add(jbSTART);
        this.add(tfID);
        this.add(tfLEVEL);
        setLayout(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idString = tfID.getText();
        String levelString = tfLEVEL.getText();
        try {//use try and catch in case that the user enter a wrong input
            level=Integer.parseInt(levelString);
            id=Integer.parseInt(idString);
            userIsDone=true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public boolean getUserIsDone() {
        return userIsDone;
    }
}
