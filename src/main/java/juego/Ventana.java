package juego;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class Ventana extends JFrame{
	
	HiraganaEnemigo panel = new HiraganaEnemigo();
		
	public Ventana() {
		
		setBackground(Color.black);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel, BorderLayout.CENTER);
		setTitle("Hiragana Enemigo");
		pack();
		setResizable(false);
		setLocationRelativeTo(null); //ventana en el centro
		setVisible(true);
		panel.asignarPosicionRect();
	}
	
	public static void main(String[] args) {
		new Ventana();
		
		
	}

}
