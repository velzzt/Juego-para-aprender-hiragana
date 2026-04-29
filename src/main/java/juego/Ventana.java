package juego;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class Ventana extends JFrame{

	public Ventana() {
		
		setBackground(Color.black);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(new HiraganaEnemigo(), BorderLayout.CENTER);
		setTitle("Hiragana Enemigo");
		setSize(980,600);
		setLocationRelativeTo(null); //ventana en el centro
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new Ventana();
		
		
	}

}
