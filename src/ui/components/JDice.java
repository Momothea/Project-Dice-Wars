package ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.*;

// https://stackoverflow.com/a/2245
// Taken from https://github.com/kdeloach/labs/blob/master/java/yahtzee/src/Dice.java

public class JDice extends JComponent implements Serializable {
	private static final long serialVersionUID = 4150196900779792898L;
	
	private Dimension arc;
	private Dimension dot;
	
	private int face;
	private Color color;

	public JDice(int face, Color color) {
		setPreferredSize(new Dimension(28, 28));
		
		this.face = face;
		this.color = color;
		
		Dimension size = getPreferredSize();
		// Taille des points et de l'arrondi
		arc = new Dimension((int)Math.sqrt(size.width), (int)Math.sqrt(size.height));
	    dot = new Dimension((int)(size.width/3), (int)(size.height/3));
	}

	@Override
	protected void paintComponent(Graphics g) {
	    // draw rectangle
        g.setColor(color);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc.width, arc.height);
        
        int height;
        int width = height = dot.height * 2/3;

     // possible positions for each dot on the dice
        int left   = getWidth() * 1/3 - dot.width/2 - width * 1/4;
        int center = getWidth() * 2/3 - dot.width/2 - width * 1/2;
        int right  = getWidth() * 3/3 - dot.width/2 - width * 3/4;

        int top    = getHeight() * 1/3 - dot.height/2 - height * 1/4;
        int middle = getHeight() * 2/3 - dot.height/2 - height * 1/2;
        int bottom = getHeight() * 3/3 - dot.height/2 - height * 3/4;
        
     // draw the dots
        g.setColor(Color.BLACK);
        switch(face)
        {
            case 1:
                g.fillOval(center, middle, width, height);
                break;
            case 2:
                g.fillOval(right, top, width, height);
                g.fillOval(left, bottom, width, height);
                break;
            case 3:
                g.fillOval(right, top, width, height);
                g.fillOval(center, middle, width, height);
                g.fillOval(left, bottom, width, height);
                break;
            case 4:
                g.fillOval(left, top, width, height);
                g.fillOval(left, bottom, width, height);
                g.fillOval(right, top, width, height);
                g.fillOval(right, bottom, width, height);
                break;
            case 5:
                g.fillOval(left, top, width, height);
                g.fillOval(left, bottom, width, height);
                g.fillOval(right, top, width, height);
                g.fillOval(right, bottom, width, height);
                g.fillOval(center, middle, width, height);
                break;
            case 6:
                g.fillOval(left, top, width, height);
                g.fillOval(left, middle, width, height);
                g.fillOval(left, bottom, width, height);
                g.fillOval(right, top, width, height);
                g.fillOval(right, middle, width, height);
                g.fillOval(right, bottom, width, height);
                break;
            default:
            	break;
        }
	}
	
	
	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(Color.BLACK);
		
		// draw bordure
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc.width, arc.height);

        // draw inside light border
        g.setColor(Color.decode("#c0c0c0"));
        g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc.width, arc.height);
	}

	public static void main(String[] args) {
		
		  JComponent gui = new JPanel(new FlowLayout());
		  gui.add(new JDice(4, Color.RED));
		  
		  JFrame f = new JFrame("Test JDice");
		  f.add(gui);
		  // Ensures JVM closes after frame(s) closed and
		  // all non-daemon threads are finished
		  f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  // See http://stackoverflow.com/a/7143398/418556 for demo.
		  f.setLocationByPlatform(true);
		  
		  // ensures the frame is the minimum size it needs to be 
		  // in order display the components within it
		  f.pack(); // should be done last, to void flickering, moving,
		  // resizing artifacts.
		  f.setVisible(true);
		 

	}

}
