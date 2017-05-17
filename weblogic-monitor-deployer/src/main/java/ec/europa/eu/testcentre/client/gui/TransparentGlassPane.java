package ec.europa.eu.testcentre.client.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingConstants;

public class TransparentGlassPane extends JComponent {
	

	
	


	
	
	public TransparentGlassPane() {
		setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		
		
		InputStream in = getClass().getResourceAsStream
			    ("loading-gif-animation.gif");
			byte buffer[]= null;
			try {
				buffer = new byte[in.available()];
				for (int i = 0, n = in.available(); i<n; i++)
					  buffer[i] = (byte)in.read();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		Image warnImage = Toolkit.getDefaultToolkit().createImage(buffer);
	
	    Icon warnIcon = new ImageIcon(warnImage);
		JLabel lblNewLabel = new JLabel(warnIcon);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		
	
		
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
