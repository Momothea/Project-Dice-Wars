package ui.components;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class JTerritoire extends JButton {
	private static final long serialVersionUID = -5763113073933373564L;
	private final double SCALE = 1;
	private boolean isSelected = false;

	private Color couleurJoueur;
	private int force;

	public JTerritoire(int force, Color cJoueur) {
		super(Integer.toString(force) + "🎲");

		Dimension d = super.getPreferredSize();
		int fontSize = (int) (SCALE * d.getHeight() / 1.5);
		super.setFont(new Font("Sans", Font.BOLD, fontSize));

		this.force = force;
		this.couleurJoueur = cJoueur;

		// Retirer remplissage du bouton
		setContentAreaFilled(false);
		// Retirer bordure autour du label du bouton
		setFocusPainted(false);
		setMargin(new Insets(0, 0, 0, 0));

		// Set prefered size
		double height = SCALE * Math.max(d.getHeight(), d.getWidth());
		double width = height / 2 * (Math.sqrt(3));
		super.setPreferredSize(new Dimension((int) width, (int) height));
	}


	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


	public Map<String, int[]> getHexagonPoints() {
		// Utilisation de la racine 6-ième de l'unité pour générer les points de
		// l'hexagone
		// rotation de 90° en remplaçant les cos par les sin et vice verca
		// et étiration horizontal de l'hexagone
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];

		Dimension d = super.getSize();
		int width = (int) d.getWidth() / 2;
		int height = (int) d.getHeight() / 2;

		for (int i = 0; i < 6; i++) {
			xPoints[i] = width * (int) Math.round(Math.sin((2 * i * Math.PI) / 6)) + width;
			yPoints[i] = (int) Math.round(height * Math.cos((2 * i * Math.PI) / 6)) + height;
		}

		Map<String, int[]> result = new HashMap<>();
		result.put("xPoints", xPoints.clone());
		result.put("yPoints", yPoints.clone());

		return result;
	}

	// https://www.codeproject.com/Articles/62099/UI-Component-Development-in-Java-Swing-Part-1-Desi
	@Override
	public void paintComponent(Graphics graphics) {
		// création du polygone
		Map<String, int[]> hexagonPoints = getHexagonPoints();
		Polygon hexagone = new Polygon(hexagonPoints.get("xPoints"), hexagonPoints.get("yPoints"), 6);

		// Dessin de l'hexagone
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0, getHeight()),
				isSelected ? Color.BLACK : couleurJoueur.darker()));
		g2.fill(hexagone);

		// Demande à JButton de dessiner le reste
		super.setForeground(!isSelected ? Color.BLACK : couleurJoueur.brighter());
		super.paintComponent(graphics);
	}

	@Override
	protected void paintBorder(Graphics g) {
		Map<String, int[]> hexagonPoints = getHexagonPoints();
		boolean topRight = false;
		boolean right = false;
		boolean bottomRight = true;
		boolean bottomLeft = false;
		boolean left = false;
		boolean topLeft = false;

		int[] xPoints = hexagonPoints.get("xPoints");
		int[] yPoints = hexagonPoints.get("yPoints");

		// set borderWidth and border color
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g.setColor(Color.BLACK);

		// bottom Right
		if (bottomRight)
			g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);

		// right
		if (right)
			g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);

		// top right
		if (topRight)
			g.drawLine(xPoints[2], yPoints[2], xPoints[3], yPoints[3]);

		// top left
		if (topLeft)
			g.drawLine(xPoints[3], yPoints[3], xPoints[4], yPoints[4]);

		// left
		if (left)
			g.drawLine(xPoints[4], yPoints[4], xPoints[5], yPoints[5]);

		// bottom left
		if (bottomLeft)
			g.drawLine(xPoints[5], yPoints[5], xPoints[0], yPoints[0]);

	}

	public static void main(String[] args) {
		JComponent gui = new JPanel(new FlowLayout());
		gui.add(new JTerritoire(3, new Color(255, 0, 0)));

		JFrame f = new JFrame("Square Buttons");
		f.add(gui);
		// Ensures JVM closes after frame(s) closed and
		// all non-daemon threads are finished
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// See http://stackoverflow.com/a/7143398/418556 for demo.
		f.setLocationByPlatform(true);

		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		f.pack();
		// should be done last, to avoid flickering, moving,
		// resizing artifacts.
		f.setVisible(true);
	}

}
