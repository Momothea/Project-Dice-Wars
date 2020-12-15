package ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JCarte extends JPanel {
	// https://stackoverflow.com/questions/44012684/how-to-make-hexagonal-jbuttons
	// REMOVE "magic" number from StackOverflow answer
	
	private static final long serialVersionUID = 5558698877882870989L;
	private final static int ROWS = 7;
	private final static int COLUMNS = 7;
	
	static int width =  95;
	static int height = 95;
	
    private JTerritoire[][] hexButtons = new JTerritoire[ROWS][COLUMNS];
	
	 public JCarte() {
	        setLayout(null);
	        initGUI();
    }
	
	public void initGUI()  {
        Color[] colors = { Color.GREEN,Color.BLUE,Color.PINK,Color.ORANGE,Color.MAGENTA,Color.RED, Color.YELLOW };
        int offsetY = 0;
        
        for(int row = 0; row < ROWS; row++) {
        	
        	// ajouter marging pour décaler les boutons 1 colonne sur 2
        	int offsetX = (row%2 == 0) ? 0 : (int) Math.round(width/2);
            for(int col = 0; col < COLUMNS; col++){
            	hexButtons[row][col] = new JTerritoire(new Random().nextInt(7) +1, colors[new Random().nextInt(7)]);
            	hexButtons[row][col].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JTerritoire clickedButton = (JTerritoire) e.getSource();
						clickedButton.setSelected(!clickedButton.isSelected());
					}
				});
                add(hexButtons[row][col]);
                
                hexButtons[row][col].setBounds(offsetX, offsetY, width, height);
                offsetX += width;
            };
            
            offsetY += (int) (height*0.75);
        }
	}

	public static void main(String[] args) {
		JCarte hexPattern = new JCarte();
        JFrame frame = new JFrame();
        frame.setTitle("Hexagon Pattern");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width*(COLUMNS+1), height*(ROWS));
        frame.add(hexPattern);
        frame.setVisible(true);
	}
}
