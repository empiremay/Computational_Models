import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import java.awt.TextArea;


public class frame extends JFrame {
	static LifeCalculoParalelo life;
	static JFrame automata = new JFrame("Juego de la Vida");
	static int nHilos=Runtime.getRuntime().availableProcessors();
	static int tamano;
	static int iteraciones;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame frame = new frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btnGenerar = new JButton("Generar");
		
		JLabel lblDimensiones = new JLabel("Dimensiones");
		
		JSpinner spinner = new JSpinner();	//Dimensiones
		
		JLabel lblIteraciones = new JLabel("Iteraciones");
		
		JSpinner spinner_1 = new JSpinner();
		
		JLabel lblClulasMuertas = new JLabel("C\u00E9lulas Muertas");
		
		JSeparator separator = new JSeparator();

		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnGenerar, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addGap(24)
							.addComponent(lblDimensiones)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 589, Short.MAX_VALUE)
							.addComponent(lblIteraciones)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
							.addGap(380))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 650, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 604, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(215)
							.addComponent(lblClulasMuertas))
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(207)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnGenerar)
							.addComponent(lblDimensiones)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblClulasMuertas)
							.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblIteraciones))))
		);
		contentPane.setLayout(gl_contentPane);

		btnGenerar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton) {
					//***** LIMPIAR PANEL
					panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());
					//*******************
					ExecutorService ejecutor=Executors.newFixedThreadPool(nHilos);
					LifeCalculoParalelo[] hilos=new LifeCalculoParalelo[nHilos];
					//DATOS MANUALES
					tamano=(Integer)spinner.getValue();
					iteraciones=(Integer)spinner_1.getValue();
					//**************
					int vbase=0;
					int vtope=tamano/nHilos;
					life=new LifeCalculoParalelo(tamano, iteraciones, nHilos);
					for(int i=0; i<nHilos; i++) {
						hilos[i]=new LifeCalculoParalelo(vbase, vtope);
						vbase=vtope;
						if(vtope<tamano) {
							if(tamano<nHilos) {
								vtope++;
							}
							else {
								if((tamano%nHilos)==0) {
									vtope+=tamano/nHilos;
								}
								else {
									if(i==(nHilos-2)) {
										vtope+=(tamano/nHilos)+(tamano%nHilos);
									}
									else {
										vtope+=tamano/nHilos;
									}
								}
							}
						}
					}
					int[] vectorVivas=new int[iteraciones];
					int[] vectorMuertas=new int[iteraciones];
					for(int k=0; k<iteraciones; k++) {
						life.resetAtomic();
						ejecutor=Executors.newFixedThreadPool(nHilos);
						for(int j=0; j<nHilos; j++) {
							ejecutor.execute(hilos[j]);
						}
						ejecutor.shutdown();
						while(!ejecutor.isTerminated()) {}
						//System.out.println("Terminado");
						int[][] celulas=life.devolverCelulas();
						int[][] resT = new int[celulas[0].length][celulas.length];
			  			for (int x=0; x < celulas.length; x++) {
			    			for (int y=0; y < celulas[x].length; y++) {
			        			resT[y][x] = celulas[x][y];
			    			}
						}
			            DibujarMatriz dibujo = new DibujarMatriz(resT);
			            dibujo.paintComponent(panel.getGraphics());
			            vectorVivas[k]=life.devolverVivas();
			            vectorMuertas[k]=life.devolverMuertas();
			            
			            System.out.println("Generacion "+k+"\tVivas: "+life.devolverVivas()+"\tMuertas: "+life.devolverMuertas());
			        	try {
			        		Thread.sleep(60);
			        	} catch(Exception ex) {}
					}
					DibujarMatriz grafica=new DibujarMatriz(vectorVivas, vectorMuertas);
		            try {
		            	grafica.paintComponent2(panel_1.getGraphics(), panel_1);
		            } catch(Exception exx) {}
				}
			}
		});
	}
}
