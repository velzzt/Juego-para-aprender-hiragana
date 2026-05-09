package aprendizaje;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JPanel;

public class PanelLeccion extends JPanel {

   private MenuPrincipal menuPrincipal;
   private Tarjeta tarjeta; //panel con CardLayout
   


    public PanelLeccion(MenuPrincipal menuPanel, ListaLeccion lista){
        this.menuPrincipal=menuPanel; //guardado como referencia
        tarjeta= new Tarjeta(lista.getLecciones()); //tarjeta obtiene la lista con objetos de Leccion
        //asiganamos un BorderLayout a PanelLeccion para dividir el contenedor en 5 regiones (de los cuales usaremos 2, centro y sur)
        setLayout(new BorderLayout(10,10)); //separación de 10px entre los componentes de PanelLeccion
        colocarComponentes();

    }


    private void colocarComponentes(){

        //Panel sur. En este panel 'filaBotones' hay una fila de botones: 'Anterior', 'Volver' y 'Siguiente'
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // usamos FlowLayout para apilar los botones en una fila
        JButton btnAnterior = new JButton("Anterior");
        JButton btnVolver = new JButton("Volver");
        JButton btnSiguiente = new JButton("Siguiente");
        filaBotones.add(btnAnterior);
        filaBotones.add(btnVolver);
        filaBotones.add(btnSiguiente);

        //Colocar el panel tarjeta y el panel filaBotones en el PanelLeccion
        add(tarjeta, BorderLayout.CENTER);
        add(filaBotones, BorderLayout.SOUTH);


        //Evento para el botonVolver
        ActionListener botonVolver = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelAprender(); // regresa a PanelAprender
            }
            
        };

        btnVolver.addActionListener(botonVolver); //añadimos el evento al boton

        //Añadir eventos que permiten cambiar de tarjetas(las que contienen el gif, audio y label de texto) al pulsar los botones 'Anterior' y 'Siguiente'
        btnAnterior.addActionListener(e -> tarjeta.mostrarAnterior());
        btnSiguiente.addActionListener(e -> tarjeta.mostrarSiguiente());    
       



    
    }
}