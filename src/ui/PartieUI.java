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

import core.Joueur;
import core.Partie;
import ui.components.JCarte;
import ui.components.JoueurCellRenderer;

public class PartieUI {
	private JFrame frame;

	private JCarte pnlCarte;
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

		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		// Get scale due to hiDPI Screen
		AffineTransform scale = frame.getGraphicsConfiguration().getDefaultTransform();
		frame.setMinimumSize(
				new Dimension((int) Math.floor(640 * scale.getScaleX()), (int) Math.floor(480 * scale.getScaleY())));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initialize();
	}

	/*
	 * UPDATES
	 */

	public void updateTourJoueur() {
		// update text of JList
		//lstTourJoueur.ensureIndexIsVisible(partie.getJoueurs().length);
		lstTourJoueur.updateUI();

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
				// Choix dialogue nb Joueur
				Integer[] possibilities = { 2, 3, 4, 5, 6, 7, 8 };
				// Afficher dialogue nb Joueur
				int nbJoueur = -1;
				try {
					nbJoueur = (int) JOptionPane.showInputDialog(frame, "Nombre de joueurs",
							"DiceWars - Nouvelle partie", JOptionPane.QUESTION_MESSAGE, null, possibilities, 7);
				} catch (Exception e1) {
					nbJoueur = -1;
				}
				// Si l'user n'a pas cancel, reset la GUI et charger la nouvelle partie
				if (nbJoueur != -1) {
					PartieUI.this.partie = new Partie(nbJoueur);
					PartieUI.this.resetGUI();
				}
			}
		});
		menuNew.add(newEmptyPartie);
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
				System.out.println("Chargement fichier");

				// Create a file chooser
				final JFileChooser fc = new JFileChooser();
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
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
						// afficher dialogue erreur
						JOptionPane.showMessageDialog(frame, e1.getMessage(),
								String.format("Erreur lors de l'import de  %s", filename), JOptionPane.ERROR_MESSAGE);
					}

				}

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
				System.out.println("Sauver partie");

				// Create a file chooser
				final JFileChooser fc = new JFileChooser();
				// In response to a button click:
				int returnVal = fc.showSaveDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String filename = file.getAbsolutePath();

					// essayer de sauvegarder partie
					try {
						partie.serialize(filename);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Erreur lors de l'enregistrement",
								JOptionPane.ERROR_MESSAGE);
					} finally {
						JOptionPane.showMessageDialog(frame, String.format("Partie sauvegardée dans %s", filename));
					}

				}
			}
		});
		// ajout du bouton à la toolbar
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
				System.out.println("Fin tour");
				partie.getjAttaquant().terminerTour();
				// update carte
				pnlCarte.repaint();

				// passer le tour au prochain joueur tirer aléatoirement
				partie.setTourJoueur();
				

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

				// lancer le thread qui update infoTour
				// Runs outside of the Swing UI thread
				new Thread(new Runnable() {
					public void run() {
						while (true) {

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
					}
				}).start();
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
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

}
