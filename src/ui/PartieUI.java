package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import core.Joueur;
import core.Partie;
import ui.components.JAttaque;
import ui.components.JCarte;
import ui.components.JoueurCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class PartieUI {
	private JFrame frame;
	private JPanel infoTour;
	private JLabel lblInfoTour; // affichage nb Tour
	private JList<Joueur> lstTourJoueur; // affichage joueur qui doit joueur

	private Partie partie;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PartieUI window = new PartieUI(7);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PartieUI(int nbJoueurs) {
		// Création partie
		this.partie = new Partie(nbJoueurs);

		initialize();
		setTourJoueur(0);
	}

	public void setTourJoueur(int index) {
		lstTourJoueur.setSelectedIndex(index);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 * Generate Toolbar ================
		 */
		JToolBar toolBar = new JToolBar("Menu");
		toolBar.setFloatable(false);
		JPanel btnContainer = new JPanel();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		// Mettre tous les boutons à la même taille
		Dimension dimBtn = new Dimension(96, 32);

		// liste icône par défaut
		// http://en-human-begin.blogspot.com/2007/11/javas-icons-by-default.html

		Icon icnNew = UIManager.getIcon("FileView.fileIcon");
		btnContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JButton btnNew = new JButton(icnNew);
		btnNew.setToolTipText("Nouvelle partie");
		btnNew.setPreferredSize(dimBtn);
		btnNew.setText("Nouv.");
		btnContainer.add(btnNew);

		Icon icnOpen = UIManager.getIcon("FileView.directoryIcon");
		JButton btnOpen = new JButton(icnOpen);
		btnOpen.setToolTipText("Charger une partie");
		btnOpen.setPreferredSize(dimBtn);
		btnOpen.setText("Ouvrir");
		btnContainer.add(btnOpen);

		Icon icnSave = UIManager.getIcon("FileView.floppyDriveIcon");
		JButton btnSave = new JButton(icnSave);
		btnSave.setToolTipText("Sauver cette partie");
		btnSave.setPreferredSize(dimBtn);
		btnSave.setText("Sauver");
		btnContainer.add(btnSave);

		JButton btnHelp = new JButton("Aide");
		btnHelp.setToolTipText("Ouvrir l'aide");
		btnHelp.setPreferredSize(dimBtn);
		btnContainer.add(btnHelp);

		toolBar.add(btnContainer);

		/*
		 * Ajouter infos tour =================
		 */
		infoTour = new JPanel();
		infoTour.setLayout(new BorderLayout());
		frame.getContentPane().add(infoTour, BorderLayout.EAST);

		lblInfoTour = new JLabel(String.format("<html><h2>Tour: %d</h3></html>", partie.getNbTour()));
		infoTour.add(lblInfoTour, BorderLayout.NORTH);

		Joueur[] listeJoueurs = partie.getJoueurs();
		lstTourJoueur = new JList(listeJoueurs);
		lstTourJoueur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstTourJoueur.setCellRenderer(new JoueurCellRenderer<Joueur>());
		lstTourJoueur.setEnabled(false);

		JScrollPane listScrollPane = new JScrollPane(lstTourJoueur);
		infoTour.add(listScrollPane, BorderLayout.CENTER);

		/*
		 * Ajout status bar avec info attaque
		 * ==================================
		 */
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		// Ajouter info attaque
		JAttaque statusAttaque = new JAttaque();
		statusBar.add(statusAttaque, BorderLayout.CENTER);
		
		// Ajouter bouton fin Tour
		JPanel btnFinContainer = new JPanel();
		JButton btnFinTour = new JButton("Fin Tour >>");
		btnFinContainer.add(btnFinTour);
		statusBar.add(btnFinContainer, BorderLayout.EAST);

		

		/*
		 * Ajouter JCarte ==============
		 */

		JCarte pnlCarte = new JCarte(partie.getCarte());
		frame.getContentPane().add(pnlCarte, BorderLayout.CENTER);

		/*
		 * Responsive JFrame
		 */
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				Component c = (Component) e.getSource();

				// Get new size
				Dimension newSize = c.getSize();
				int cWidth = Math.max(125, (int) Math.round(0.15 * newSize.getWidth()));
				
				newSize.setSize(cWidth, -1);

				infoTour.setPreferredSize(newSize);
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Component c = (Component) e.getSource();

				// Get new size
				Dimension newSize = c.getSize();
				int cWidth = Math.max(125, (int) Math.round(0.15 * newSize.getWidth()));
				
				newSize.setSize(cWidth, -1);

				infoTour.setPreferredSize(newSize);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

}
