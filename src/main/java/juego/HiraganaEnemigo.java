package juego;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HiraganaEnemigo extends JPanel implements ActionListener,Cloneable{
	
	private Timer timer;
	int x=920;
	int y=0;
	int velocidadX=3;
	
	HiraganaEnemigo(){
		this.setPreferredSize(new Dimension(800,600));
		timer=new Timer(20,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Crear objeto que defina la fuente del hiragana
		Font font = new Font("MS Mincho",1,48);
		g.setFont(font);
		
		//Dibujar el rectángulo
		g.setColor(Color.gray);
		g.fillRect(x, y, 80, 80);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x, y, 80, 80);
		
		//Caracter en el centro del rectangulo
		String hiragana="あ";
		FontMetrics fm= g.getFontMetrics();
		int anchoCaracter=fm.stringWidth(hiragana);
		int altoCaracter=fm.getAscent();
	    int caracterX = x + (80 - anchoCaracter) / 2;
	    int caracterY = y + (80 + altoCaracter) / 2 - fm.getDescent();	
		g.drawString(hiragana,caracterX, caracterY);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		x=x-velocidadX;
		
		repaint();
		
	}
	
		
	
}
