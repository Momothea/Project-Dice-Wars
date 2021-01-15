package ui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import core.Joueur;

public class JoueurCellRenderer<T> extends JLabel implements ListCellRenderer<T> {

	private static final long serialVersionUID = 1L;
	
	public JoueurCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends T> list, T value, int index,
			boolean isSelected, boolean cellHasFocus) {
		// taken from https://docs.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html
		setText((value == null ? "" : ((Joueur) value).infosJoueur()));

        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

        // check if this cell is selected et le colorer selon la couleur du joueur
        } else if (isSelected) {
        	Joueur j = (Joueur) value;
            background = j.getCouleur();
            foreground = Color.WHITE;

        // unselected, and not the DnD drop location
        } else {
            background = Color.WHITE;
            foreground = Color.GRAY;
        };

        setBackground(background);
        setForeground(foreground);

        return this;
	}

}
