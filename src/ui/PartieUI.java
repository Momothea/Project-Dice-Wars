package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;


import core.Joueur;
import core.Partie;
import ui.components.JCarte;
import ui.components.JHelp;
import ui.components.JoueurCellRenderer;

public class PartieUI {
	private JFrame frame;

	private JCarte pnlCarte;
	private JPanel infoTour;

	private JLabel lblInfoTour; // affichage nb Tour
	private JList<Joueur> lstTourJoueur; // affichage joueur qui doit joueur

	private Partie partie;
	private String savePath = "";

	private Thread updater = new UpdateUI();

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

		frame = new JFrame("DiceWars");
		frame.setBounds(100, 100, 640, 480);
		// Get scale due to hiDPI Screen
		AffineTransform scale = frame.getGraphicsConfiguration().getDefaultTransform();
		frame.setMinimumSize(
				new Dimension((int) Math.floor(640 * scale.getScaleX()), (int) Math.floor(480 * scale.getScaleY())));

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		initialize();
	}

	/*
	 * UPDATES
	 */

	private void updateTourJoueur() {
		// update text of JList
		lstTourJoueur.repaint();

		// updata Tour Joueur
		List<Joueur> listeJoueurs = Arrays.asList(partie.getJoueurs());
		Joueur jAttaquant = partie.getjAttaquant();
		lstTourJoueur.setSelectedIndex(listeJoueurs.indexOf(jAttaquant));

		// update NbTour
		lblInfoTour.setText(String.format("<html><h2>Tour: %d</h3></html>", partie.getNbTour()));
	}

	public void resetGUI() {
		// Réinitialiser la GUI
		frame.getContentPane().removeAll();
		initialize();
		frame.validate();
		frame.setVisible(true);
	}

	private class UpdateUI extends Thread {
		@Override
		public void run() {
			while (partie.getjGagnant() == null) {
				// Runs inside of the Swing UI thread
				// https://stackabuse.com/how-to-use-threads-in-java-swing/
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// update infoTour
						updateTourJoueur();
					}
				});

				try {
					java.lang.Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// arriver ici, on a un gagnant
			JOptionPane.showMessageDialog(frame, String.format("Victoire de Joueur %d", partie.getjGagnant().getId()));
		}

		@Override
		public synchronized void start() {
			if (!isAlive()) {
				super.start();
			}
		}

	}

	/*
	 * HANDLERS
	 */
	private void nouvellePartie() {
		// Choix dialogue nb Joueur
		Integer[] possibilities = { 2, 3, 4, 5, 6, 7, 8 };
		// Afficher dialogue nb Joueur
		int nbJoueur = -1;
		try {
			nbJoueur = (int) JOptionPane.showInputDialog(frame, "Nombre de joueurs", "DiceWars - Nouvelle partie",
					JOptionPane.QUESTION_MESSAGE, null, possibilities, 7);
			savePath = "";
		} catch (Exception e1) {
			nbJoueur = -1;
		}
		// Si l'user n'a pas cancel, reset la GUI et charger la nouvelle partie
		if (nbJoueur != -1) {
			partie = new Partie(nbJoueur);
			resetGUI();

			// lancer le thread qui update infoTour
			updater.start();
		}
	}

	private void importCarte() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Carte DiceWars", "csv");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String filename = file.getAbsolutePath();

			// essayer d'importer la partie
			try {
				// Chargement de la carte
				// TODO Put fonction here

				// reset GUI
				resetGUI();
				savePath = "";

				// lancer le thread qui update infoTour
				updater.start();

			} catch (Exception e1) {
				e1.printStackTrace();
				// afficher dialogue erreur
				JOptionPane.showMessageDialog(frame, e1.getMessage(),
						String.format("Erreur lors de l'import de  %s", filename), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void loadPartie() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Partie DiceWars", "ser");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String filename = file.getAbsolutePath();

			// essayer d'importer la partie
			try {
				// Chargement de la partie
				partie = new Partie(filename);

				// reset GUI
				resetGUI();
				frame.setTitle("DiceWars - " + filename);
				savePath = filename;

				// lancer le thread qui update infoTour
				updater.start();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
				// afficher dialogue erreur
				JOptionPane.showMessageDialog(frame, e1.getMessage(),
						String.format("Erreur lors de l'import de  %s", filename), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void savePartie() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Partie DiceWars", "ser");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);

		// In response to a button click:
		int returnVal = fc.showSaveDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String filename = file.getAbsolutePath();

			// https://stackoverflow.com/a/13308477
			if (!filename.endsWith("ser")) {
				filename += ".ser";
			}

			// essayer de sauvegarder partie
			try {
				partie.serialize(filename);
				
				frame.setTitle("DiceWars - " + filename);
				savePath = filename;
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, e1.getMessage(), "Erreur lors de l'enregistrement",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				JOptionPane.showMessageDialog(frame, String.format("Partie sauvegardée dans %s", filename));
			}
		}
	}
	
	private class AutoSave implements WindowListener {

		@Override
		public void windowClosing(WindowEvent e) {
				JFrame f = (JFrame)e.getSource();
			
				int reponse = JOptionPane.showConfirmDialog(f, "Sauver cette partie ?",
						"DiceWars - AutoSave", JOptionPane.YES_NO_OPTION);
				
				if(reponse == JOptionPane.YES_OPTION) {
					// Si pas de partie qui a été charger, demander endroit où la sauver
					if(savePath.equals("")) {
						savePartie();					
					} 
					// sinon, sauvegarde automatique :)
					else {
						try {
							partie.serialize(savePath);
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(frame, e1.getMessage(), "Erreur lors de l'enregistrement",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		public void windowOpened(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		
	}

	private void finTour() {
		System.out.println("Fin tour");
		partie.getjAttaquant().terminerTour();
		// update carte
		pnlCarte.repaint();

		// passer le tour au prochain joueur tirer aléatoirement
		partie.setTourJoueur();
		// update Tour Joueur
		updateTourJoueur();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		btnNew.setToolTipText("Lancer une nouvelle partie");
		btnNew.setPreferredSize(dimBtn);
		btnNew.setText("Nouv.");
		// menu du bouton
		// (https://stackoverflow.com/questions/1692677/how-to-create-a-jbutton-with-a-menu#1693326)
		JPopupMenu menuNew = new JPopupMenu();

		// ajout item nouvelle partie
		JMenuItem newEmptyPartie = new JMenuItem(new AbstractAction("Nouvelle partie") {
			private static final long serialVersionUID = -2602975436374531482L;

			@Override
			public void actionPerformed(ActionEvent e) {
				nouvellePartie();
			}
		});
		menuNew.add(newEmptyPartie);

		JMenuItem newCSVPartie = new JMenuItem(new AbstractAction("Importer une carte") {
			private static final long serialVersionUID = -7459567408890983586L;

			@Override
			public void actionPerformed(ActionEvent e) {
				importCarte();
			}
		});
		menuNew.add(newCSVPartie);

		// ajout du menu pop up au bouton
		btnNew.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				menuNew.show(e.getComponent(), 0, e.getComponent().getHeight());
			}
		});
		// ajout du bouton à la toolbar
		btnContainer.add(btnNew);

		Icon icnOpen = UIManager.getIcon("FileView.directoryIcon");
		JButton btnOpen = new JButton(icnOpen);
		btnOpen.setToolTipText("Charger une partie");
		btnOpen.setPreferredSize(dimBtn);
		btnOpen.setText("Ouvrir");
		// ajout action au bouton
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadPartie();
			}
		});
		// ajout du bouton à la toolbar
		btnContainer.add(btnOpen);

		Icon icnSave = UIManager.getIcon("FileView.floppyDriveIcon");
		JButton btnSave = new JButton(icnSave);
		btnSave.setToolTipText("Sauver cette partie");
		btnSave.setPreferredSize(dimBtn);
		btnSave.setText("Sauver");
		// ajout action au bouton
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePartie();
			}
		});
		// ajout du bouton à la toolbar
		btnContainer.add(btnSave);

		JButton btnHelp = new JButton("Aide");
		btnHelp.setToolTipText("Ouvrir l'aide");
		btnHelp.setPreferredSize(dimBtn);
		// (https://stackoverflow.com/questions/1692677/how-to-create-a-jbutton-with-a-menu#1693326)
		JPopupMenu menuHelp = new JPopupMenu();

		// ajout afficher aide
		JMenuItem helpGetHelp = new JMenuItem(new AbstractAction("Afficher l'aide") {
			private static final long serialVersionUID = 6986764940894683323L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new JHelp(frame.getBounds());
			}
		});
		menuHelp.add(helpGetHelp);

		// ajout item à propos
		JMenuItem helpAbout = new JMenuItem(new AbstractAction("A propos") {
			private static final long serialVersionUID = 9206470584933030896L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"<html>" + "<h2 style='text-align: center'>DiceWars</h2>"
								+ "<p style='text-align: center'>Projet Efrei L3 de Java</p>" + "<br>" + "<table>"
								+ "<tbody>" + "<tr>" + "<td style='border-right: 1px solid black'>(C) 2020-21</td>"
								+ "<td><p> Nil CAILLEUX, Paul GODIN,</p> <p>Moise ILOO LIANDJA</p></td>" + "</tr>"
								+ "</tbody>" + "</table>" + "</html>",
						"", JOptionPane.PLAIN_MESSAGE);

			}
		});
		menuHelp.add(helpAbout);

		// ajout du menu pop up au bouton
		btnHelp.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				menuHelp.show(e.getComponent(), 0, e.getComponent().getHeight());
			}
		});
		// ajout du bouton à la toolbar
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
		lstTourJoueur = new JList<>(listeJoueurs);
		lstTourJoueur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstTourJoueur.setCellRenderer(new JoueurCellRenderer<Joueur>());
		lstTourJoueur.setEnabled(false);
		updateTourJoueur();

		JScrollPane listScrollPane = new JScrollPane(lstTourJoueur);
		infoTour.add(listScrollPane, BorderLayout.CENTER);

		// Ajouter bouton fin Tour
		JButton btnFinTour = new JButton("Fin Tour >>");
		btnFinTour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finTour();
			}
		});
		// ajout du bouton à infoTour
		infoTour.add(btnFinTour, BorderLayout.SOUTH);

		/*
		 * Ajouter JCarte ==============
		 */

		pnlCarte = new JCarte(partie);
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
				int cWidth = Math.max(130, (int) Math.round(0.15 * newSize.getWidth()));

				newSize.setSize(cWidth, -1);

				infoTour.setPreferredSize(newSize);

				// Runs outside of the Swing UI thread
				// lancer le thread qui update infoTour
				updater.start();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Component c = (Component) e.getSource();

				// Get new size
				Dimension newSize = c.getSize();
				int cWidth = Math.max(130, (int) Math.round(0.15 * newSize.getWidth()));

				newSize.setSize(cWidth, -1);

				infoTour.setPreferredSize(newSize);
			}

			@Override
			public void componentMoved(ComponentEvent e) { }
			public void componentHidden(ComponentEvent e) { }
		});
		
		// autosave feature !
		frame.addWindowListener(new AutoSave());
	}

}
