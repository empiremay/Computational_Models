package paquete;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSpinner;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;

public class frameVisual extends JFrame {
	static belZabParalelo life;
	static JFrame automata = new JFrame("Juego de la Vida");
	static int nHilos=Runtime.getRuntime().availableProcessors();
	static int tamano;
	static int iteraciones;
	static float alfa, beta, gamma;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frameVisual frame = new frameVisual();
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
	public frameVisual() {
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
		
		JSeparator separator = new JSeparator();

		JPanel panel = new JPanel();
		
		JLabel lblAlfa = new JLabel("Alfa");
		
		JLabel lblBeta = new JLabel("Beta");
		
		JLabel lblGamma = new JLabel("Gamma");
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		
		JSpinner spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		
		JSpinner spinner_4 = new JSpinner();
		spinner_4.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 697, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblIteraciones)
						.addComponent(lblDimensiones)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(lblAlfa)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblGamma)
								.addComponent(lblBeta))))
					.addGap(55)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(101)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(spinner, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
								.addComponent(btnGenerar, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
								.addComponent(spinner_1, Alignment.LEADING)
								.addComponent(spinner_2)
								.addComponent(spinner_3)
								.addComponent(spinner_4))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addComponent(btnGenerar)
							.addGap(33)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDimensiones)
								.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblIteraciones))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinner_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAlfa))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinner_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblBeta))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(13)
									.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(spinner_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblGamma)))))
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

		btnGenerar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton) {
					//***** LIMPIAR PANEL
					panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());
					//*******************
					ExecutorService ejecutor=Executors.newFixedThreadPool(nHilos);
					belZabParalelo[] hilos=new belZabParalelo[nHilos];
					//DATOS MANUALES
					tamano=(Integer)spinner.getValue();
					iteraciones=(Integer)spinner_1.getValue();
					alfa=(Float)spinner_2.getValue();
					beta=(Float)spinner_3.getValue();
					gamma=(Float)spinner_4.getValue();
					//**************
					int vbase=0;
					int vtope=tamano/nHilos;
					life=new belZabParalelo(tamano, iteraciones, nHilos, (float)alfa, (float)beta, (float)gamma);
					for(int i=0; i<nHilos; i++) {
						hilos[i]=new belZabParalelo(vbase, vtope);
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
					BufferedImage bi = new BufferedImage(tamano,tamano,BufferedImage.TYPE_INT_RGB);
					//***********
					long time_start, time_end;
					
					time_start = System.currentTimeMillis();
					//***********
					for(int k=0; k<iteraciones; k++) {
						ejecutor=Executors.newFixedThreadPool(nHilos);
						for(int j=0; j<nHilos; j++) {
							ejecutor.execute(hilos[j]);
						}
						ejecutor.shutdown();
						while(!ejecutor.isTerminated()) {}
						life.nextGen();
						Color[][] color=life.devolver_Color();
						for (int x=0; x < tamano; x++) {
			    			for (int y=0; y < tamano; y++) {
				        		bi.setRGB(y, x, color[y][x].getRGB());
			    			}
						}
						panel.getGraphics().drawImage(bi, 0, 0, tamano, tamano, panel);
					}
					time_end = System.currentTimeMillis();
					System.out.println("The task has taken "+ ( time_end - time_start ) +" milliseconds");
					//JOptionPane.showMessageDialog(null, textField.getText(),"AQUI VA EL TEXTO", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
}
