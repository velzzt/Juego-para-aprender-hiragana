package aprendizaje;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*

Para entender el codigo recomiendo leer en el siguiente orden:
Leccion -> ListaLeccion -> MenuPrincipal -> PanelAprender -> PanelLeccion -> Tarjeta

*/
//Panel principal que contiene los botones 'Aprender' y 'Jugar'
public class MenuPrincipal extends JFrame {

    public JPanel panel;
    private ListaLeccion lista1,lista2,lista3,lista4, lista5;
    
    public MenuPrincipal(){

        setSize(1280,800);
        setResizable(false);
        setTitle("Aprender hiragana");
        setDefaultCloseOperation(EXIT_ON_CLOSE); //para terminar el proceso 
        setLocationRelativeTo(null); //ventana en el centro de la pantalla
        iniciarComponentes();
    }

    private void iniciarComponentes(){
        colocarPanel();
        colocarComponentes();
        inicializarLecciones();

    }

    private void colocarPanel(){

        panel = new JPanel(); //se crea un JPanel panel
        panel.setLayout(null); //Desactivamos el layout por defecto para darle una posición fija a los componentes
        getContentPane().add(panel); //añade 'panel' al frame
    }
    private void colocarComponentes(){
        //Coloca los componentes(label,botones) a 'panel'

        JLabel titulo = new JLabel("Aprende Hiragana");
        titulo.setBounds(390,150,500,80); //asignamos posicion y tamaño
        titulo.setHorizontalAlignment(SwingConstants.CENTER); //Centramos el texto en el label
        titulo.setFont(new Font("SansSerif", Font.BOLD,50)); //en estilos puede ser 0,1,2,3 en vez de usar  Font.BOLD O Font.ITALIC, etc
        panel.add(titulo); //agregamos a 'panel'

        JButton btnAprender = new JButton("Aprender");
        btnAprender.setBounds(540, 350, 200, 40); //asignamos posicion y tamaño
        panel.add(btnAprender); //agregamos a 'panel'

        JButton btnJugar = new JButton("Jugar");
        btnJugar.setBounds(540, 450, 200, 40); //asignamos posicion y tamaño
        panel.add(btnJugar); //agregamos a 'panel'

        //Evento del boton 'Aprender'
        ActionListener e = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //guardamos una referencia de la ventana para usarlo en PanelAprender (boton volver)
                PanelAprender panelAprender = new PanelAprender(MenuPrincipal.this); //se crea un panelAprender y se guarda una referencia a este MenuPrincipal con su 'panel'
                getContentPane().removeAll();//se borra el contenido del frame (osea 'panel')
                getContentPane().add(panelAprender); //se añade panelAprender al frame
                getContentPane().revalidate(); //recalcula la posicion y tamaño de los componentes de 'panelAprender' según su layout
                getContentPane().repaint(); //pinta los componentes de 'panelAprender'
            }
            
        };
        btnAprender.addActionListener(e);

        //Evento del boton 'Jugar'
        ActionListener a = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            HiraganaEnemigo ah= new HiraganaEnemigo(); //se crea y añade el panel del juego al frame
            getContentPane().removeAll();
            getContentPane().add(ah);
            getContentPane().revalidate();
            getContentPane().repaint();
            }};

        btnJugar.addActionListener(a);
    }


    private void inicializarLecciones(){

        lista1=new ListaLeccion();
        lista1.agregarLeccion(new Leccion("/a.gif", "/audio/a.wav", "a")); //agregar lecciones (a, i , u)

        lista2=new ListaLeccion();
        lista2.agregarLeccion(new Leccion("/e.gif", "/audio/e.wav", "e")); //cambiar y agregar lecciones (e, o, ka)

        lista3=new ListaLeccion();
        lista3.agregarLeccion(new Leccion("/i.gif", "/audio/i.wav", "i")); //cambiar y agregar lecciones  (sa, ki, shi, ku)

        lista4=new ListaLeccion();
        lista4.agregarLeccion(new Leccion("/o.gif", "/audio/o.wav", "o")); //cambiar y agregar lecciones (su, ke, se, ko, so)
        
        lista5=new ListaLeccion();
        lista5.agregarLeccion(new Leccion("/u.gif", "/audio/u.wav", "u"));
    }


    //metodo que hace que los botones de PanelAprender muestren las lecciones que les corresponden
     public ListaLeccion obtenerListaLeccion(int numero) {
        switch(numero) {
            case 1: return lista1;
            case 2: return lista2;
            case 3: return lista3;
            case 4: return lista4;
            default: return lista1;
        }
    }

        //Método que restaura la vista del menú principal (el que se guardo como referencia en el constructor) en PanelAprender
        public void mostrarMenu() {
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
        //Método que restaura la vista del panelAprender (se utiliza en PanelLeccion)
        public void mostrarPanelAprender() {
        getContentPane().removeAll();
        getContentPane().add(new PanelAprender(this));
        getContentPane().revalidate();
        getContentPane().repaint();
    }
    
    
}

