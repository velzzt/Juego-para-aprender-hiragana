package aprendizaje;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HiraganaEnemigo extends JPanel implements ActionListener{
	
	private String[]caracteres= {"あ","い","う","え","お"};
	private String caracterSeleccionado;
	private int x; //posición inicial del rectangulo en el eje x
	private int y; //posicion inicial del rectangulo en el eje y
	private int altoRect=100;
	private int anchoRect=100;
	
	Timer temporizador;
	int velocidad=3;
	
	//Metodo que determina una posicion aleatoria al rectangulo (en el border derecho)
	public void asignarPosicionRect() {
		x=getWidth()-anchoRect;
		y=(int)(Math.random()*(getHeight()-altoRect));
		
	}	
	
	//Elige un caracter aleatorio del array
	public void asignarCaracter() {
		int indice= (int)(Math.random()*caracteres.length);
		caracterSeleccionado=caracteres[indice];
		
	}
	
	public HiraganaEnemigo() {
		
		setBackground(Color.black);
		setPreferredSize(new Dimension(1024,768));
		asignarCaracter();
		temporizador=new Timer(10,this);
		temporizador.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//casting para más funciones
		Graphics2D g2d = (Graphics2D) g;
		
		//Dibujar el rectangulo
		g2d.setColor(Color.gray);
		g2d.fillRect(x, y, anchoRect, altoRect);
		g2d.setColor(Color.black);
		g2d.drawRect(x, y, anchoRect, altoRect);
		
		//Para el caracter
		Font fuente=new Font("MS mincho",Font.BOLD,100); //fuente para el caracter
		g2d.setFont(fuente);
		g2d.setColor(Color.black);
		
		//conseguir las métricas del caracter para centrarlo en el rectangulo
		FontMetrics fm = g2d.getFontMetrics(fuente);
		
		int caracterx= x + (anchoRect-fm.stringWidth(caracterSeleccionado))/2;
		int caractery= y + ((altoRect-fm.getHeight())/2)+fm.getAscent() ;
		
		g2d.drawString(caracterSeleccionado, caracterx, caractery);
	}

	//para que se mueva el rectangulo
	@Override
	public void actionPerformed(ActionEvent e) {
		x--;
		
		if (x+anchoRect<0) {
			asignarPosicionRect(); //nueva posicion
			asignarCaracter(); //nuevo caracter aleatorio
			
		}
		repaint();
		
		
	}
	
}
