package ui.components;

import java.awt.*;
import javax.swing.*;

import core.Territoire;

import java.util.*;

public class JTerritoire extends JButton {
	private static final long serialVersionUID = -5763113073933373564L;
	private int mCurrentSize = 0;
	private Font mInitialFont = null;

	private HashMap<String, Long> voisins;
	private Territoire territoire;

	public JTerritoire(Territoire territoire, HashMap<String, Long> voisins) {
		this.territoire = territoire;
		this.voisins = voisins;
		this.mInitialFont = getFont();

		// Retirer remplissage du bouton
		setContentAreaFilled(false);
		// Retirer bordure autour du label du bouton
		setFocusPainted(false);
		setMargin(new Insets(0, 0, 0, 0));

		// set infobulle
		setToolTipText(String.format(
				"<html>" + "<p><strong>id     :</strong> %d</p>" + "<p><strong>force   :</strong> %d</p>"
						+ "<p><strong>Joueur  :</strong> %d</p>" + "<p><strong>voisins :</strong> %s</p>" + "</html>",
				territoire.getId(), territoire.getForce(), territoire.getJoueur().getId(),
				territoire.getVoisins().toString()));
	}

	public boolean isSelected() {
		return territoire.isSelected();
	}

	public void setSelected(boolean isSelected) {
		this.territoire.setSelected(isSelected);
	}

	protected Map<String, int[]> getHexagonPoints() {
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
	protected void paintComponent(Graphics graphics) {
		// création du polygone
		Map<String, int[]> hexagonPoints = getHexagonPoints();
		Polygon hexagone = new Polygon(hexagonPoints.get("xPoints"), hexagonPoints.get("yPoints"), 6);

		// Dessin de l'hexagone
		Graphics2D g2 = (Graphics2D) graphics;

		ButtonModel model = getModel();

		g2.setPaint(new GradientPaint(new Point(0, 0), Color.WHITE, new Point(0, getHeight()),
				territoire.isSelected() ? Color.BLACK
						: model.isRollover() ? territoire.getCouleurJoueur().brighter()
								: territoire.getCouleurJoueur().darker()));
		g2.fill(hexagone);

		// Demande à JButton de dessiner le reste
		super.setForeground(!territoire.isSelected() ? Color.BLACK : territoire.getCouleurJoueur());
		super.setText(Integer.toString(territoire.getForce()));

		// responsive text font size
		// https://stackoverflow.com/a/30932181
		int resizal = this.getHeight() / 2;
		if (resizal != mCurrentSize) {
			setFont(mInitialFont.deriveFont((float) resizal));
			mCurrentSize = resizal;
		}

		super.paintComponent(graphics);
	}

	@Override
	protected void paintBorder(Graphics g) {
		Map<String, int[]> hexagonPoints = getHexagonPoints();
		boolean topRight = voisins.get("top-right") != territoire.getId();
		boolean right = voisins.get("right") != territoire.getId();
		boolean bottomRight = voisins.get("bottom-right") != territoire.getId();
		boolean bottomLeft = voisins.get("bottom-left") != territoire.getId();
		boolean left = voisins.get("left") != territoire.getId();
		boolean topLeft = voisins.get("top-left") != territoire.getId();

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
		/*
		 * JComponent gui = new JPanel(new FlowLayout()); gui.add(new JTerritoire(1, 3,
		 * new Color(255, 0, 0)));
		 * 
		 * JFrame f = new JFrame("Square Buttons"); f.add(gui); // Ensures JVM closes
		 * after frame(s) closed and // all non-daemon threads are finished
		 * f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // See
		 * http://stackoverflow.com/a/7143398/418556 for demo.
		 * f.setLocationByPlatform(true);
		 * 
		 * // ensures the frame is the minimum size it needs to be // in order display
		 * the components within it f.setSize(400,400);; // should be done last, to
		 * avoid flickering, moving, // resizing artifacts. f.setVisible(true);
		 */
	}

}
