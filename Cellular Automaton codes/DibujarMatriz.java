import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class DibujarMatriz extends JPanel {

    public static final Color Azul = Color.BLACK;
    public static final Color Rojo = Color.WHITE;
    public static final Color Relleno = new Color(0,0,0);


    public static int filas;
    public static int col;
    private final Color[][] datos;
    private int[] vivas;
    private int[] muertas;

    public DibujarMatriz(int[][] datos){
        filas = datos.length;
        col = datos[0].length;
        this.datos = new Color[filas][col];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < col; j++) {
                Color color;
                if(datos[i][j]==0){
                    color = Azul;
                    this.datos[i][j] = color;
                }
                if(datos[i][j]==1){
                    color = Rojo;
                    this.datos[i][j] = color;
                }
            }
        }
        int ancho = filas * 1;//
        int altura = col * 1;//
        setPreferredSize(new Dimension(ancho, altura));
    }
    
    public DibujarMatriz(int[] vivas, int[] muertas){
    	this.datos = new Color[filas][col];
    	this.vivas=vivas;
    	this.muertas=muertas;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.clearRect(0, 0, 1*16, 1*8);
        int ancho = 1;
        int alto =  1;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < col; j++) {
                int x = i * ancho;
                int y = j * alto;
                Color color = datos[i][j];
                g.setColor(color);
                g.fillRect(x, y, ancho, alto);
                g.setColor(Relleno);
            }
        }
    }

    public void paintComponent2(Graphics g, JPanel panel) throws Exception  {
        super.paintComponent(g);
        XYSeries plotvivas = new XYSeries("Cantidad de c\u00e9lulas vivas"); 
        XYSeries plotmuertas = new XYSeries("Cantidad de c\u00e9lulas muertas");
        for(int i=0; i<vivas.length;++i){
        	plotvivas.add(i,vivas[i]);
        	plotmuertas.add(i, muertas[i]);
        }
        XYSeriesCollection datos1 = new XYSeriesCollection();
        datos1.addSeries(plotvivas);
        datos1.addSeries(plotmuertas);
        JFreeChart vivas = ChartFactory.createXYLineChart("Poblaci\u00f3n del automata 2D", "Generaci\u00f3n", "N\u00famero",datos1);
        ChartPanel grafico1 = new ChartPanel(vivas);
        grafico1.setDomainZoomable(true);
        panel.updateUI();
        panel.setLayout(new BorderLayout());
        panel.add(grafico1,BorderLayout.CENTER);
        

    }
}