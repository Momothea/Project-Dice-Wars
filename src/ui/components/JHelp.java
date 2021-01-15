package ui.components;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;


public class JHelp {
	// How to create a simple Swing HTML viewer with Java
	// https://alvinalexander.com/blog/post/jfc-swing/how-create-simple-swing-html-viewer-browser-java/

	// Autoriser qu'un affichage de JHelp
	private static int countJHelp = 0;
	
	public JHelp(Rectangle partieUIBounds) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (countJHelp == 0) {
					countJHelp++;
					
					// create jeditorpane
					JEditorPane jEditorPane = new JEditorPane();

					// make it read-only
					jEditorPane.setEditable(false);

					// create a scrollpane; modify its attributes as desired
					JScrollPane scrollPane = new JScrollPane(jEditorPane);

					// add an html editor kit
					HTMLEditorKit kit = new HTMLEditorKit();
					jEditorPane.setEditorKit(kit);

					// add some styles to the html
					StyleSheet styleSheet = kit.getStyleSheet();
					styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
					styleSheet.addRule("h1 {color: blue;}");
					styleSheet.addRule("h2 {color: #ff0000;}");
					styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

					// create some simple html as a string
					Charset charset = Charset.forName("UTF-8");
					String htmlString = null;
					try {
						List<String> lines = Files.readAllLines(Path.of("help.html"), charset);
						htmlString = String.join("\n", lines);
					} catch (IOException x) {
						System.err.format("IOException: %s%n", x);
					}

					// create a document, set it on the jeditorpane, then add the html
					Document doc = kit.createDefaultDocument();
					jEditorPane.setDocument(doc);
					jEditorPane.setText(htmlString);
					
					// scroll to top
					jEditorPane.setCaretPosition(0);

					// now add it all to a frame
					JFrame j = new JFrame("Aide DiceWars");
					j.getContentPane().add(scrollPane, BorderLayout.CENTER);

					// make it easy to close the application
					j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

					// gestion nbJHelp
					j.addWindowListener(new WindowListener() {
						public void windowOpened(WindowEvent e) {}
						public void windowIconified(WindowEvent e) {}
						public void windowDeiconified(WindowEvent e) {}
						public void windowDeactivated(WindowEvent e) {}
						public void windowClosing(WindowEvent e) {}
						public void windowActivated(WindowEvent e) {}
						
						@Override
						public void windowClosed(WindowEvent e) {
							countJHelp--;
						}
					});
					
					// display the frame
					j.setSize(300, partieUIBounds.height);
					


					// https://stackoverflow.com/questions/23039600/how-to-set-a-jframe-location-beside-another-jframe
					j.setLocation(partieUIBounds.x + partieUIBounds.width, partieUIBounds.y);
					j.setVisible(true);
				}
				
			}
		});
	}

}
