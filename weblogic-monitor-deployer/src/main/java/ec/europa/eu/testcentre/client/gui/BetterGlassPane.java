package ec.europa.eu.testcentre.client.gui;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BetterGlassPane extends JPanel implements MouseListener { 
    private final JFrame frame; 
    
    private String message;

	private Font font;
    
 
    public BetterGlassPane(JFrame frame) { 
        super(null); 
        this.frame = frame; 
        setOpaque(false); 
        addMouseListener(this);
        message=" loading ... ";
        font = new Font("Serif", Font.PLAIN, 24);
    } 
 
  
 
    protected void paintComponent(Graphics g) { 
        Graphics2D g2 = (Graphics2D) g; 
        g2.setColor(Color.BLACK); 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); 
         g2.setFont(font); 
        g2.fillRect(0, 0, getWidth(), getHeight()); 
        g2.drawString(message, getWidth() / 2 , getHeight() / 2);
       
        g2.dispose(); 
    }



	public void setMessage(String message) {
		this.message = message;
		repaint();
	}



	public void mouseClicked(MouseEvent e) {
		e.consume();
		
	}


	public void mousePressed(MouseEvent e) {
		e.consume();
		
	}

	public void mouseReleased(MouseEvent e) {
		return;
		
	}


	public void mouseEntered(MouseEvent e) {
		return;
		
	}

	public void mouseExited(MouseEvent e) {
		return;
		
	} 
 
 
} 