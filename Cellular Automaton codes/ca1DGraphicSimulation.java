import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class ca1DGraphicSimulation {
	static ca1DSimulator objeto;
	static int nHilos=Runtime.getRuntime().availableProcessors();
	static Scanner teclado=new Scanner(System.in);
	static int generaciones;

	private static double log2(double x) { return (Math.log(x)/Math.log(2));}

	private static int Hamming(int[] vectorA, int[] vectorB) {
		int calculo=0;
		for(int i=0; i<vectorA.length; i++) {
			if(vectorA[i]!=vectorB[i]) {
				calculo++;
			}
		}
		return calculo;
	}

	private static double Entropia(int[] vector) {
		double p1=0;
		double p0=0;
		double entropia=0;
		double tamano=vector.length;
		double contadorUnos=0;
		double contadorCeros=0;
		for(int i=0; i<tamano; i++) {
			if(vector[i]==1) {
				contadorUnos++;
			}
			if(vector[i]==0) {
				contadorCeros++;
			}
		}
		p1=(double)contadorUnos/tamano;
		p0=(double)contadorCeros/tamano;
		if(p0==0) {
			entropia=-1*(p1*log2(p1));
		}
		else {
			if(p1==0) {
				entropia=-1*(p0*log2(p0));
			}
			else {
				entropia=-1*(p0*log2(p0)+p1*log2(p1));
			}
		}
		return entropia;
	}

	public static void main(String[] args) throws Exception {
		ExecutorService ejecutor=Executors.newFixedThreadPool(nHilos);
		System.out.print(" Introduzca la longitud de la celula: ");
		int longitud=teclado.nextInt();
		System.out.println(" Elegir: (1.- Celula generada aleatoriamente  2.- Celula insertada manualmente)");
		int elegir=teclado.nextInt();
		Boolean flag=false;
		int[] primeraCelula=new int[longitud];
		if(elegir==2) {
			flag=true;
			for(int i=0; i<longitud; i++) {
				System.out.print(" Introduzca elemento "+i+" de la celula: ");
				primeraCelula[i]=teclado.nextInt();
			}
		}
		System.out.print(" Introduzca el numero de generaciones: ");
		generaciones=teclado.nextInt();
		System.out.print(" Introduzca la condicion de barrera (1.-Ciclica\t2.-Nula): ");
		int tipo=teclado.nextInt();
		System.out.print(" Introduce el indice de la celula a la que calcularle la entropia temporal\n\t(de 0 a "+(longitud-1)+"): ");
		int indice=teclado.nextInt();
		ca1DSimulator[] hilos=new ca1DSimulator[nHilos];
		int vbase=0;
		int vtope=longitud/nHilos;
		objeto=new ca1DSimulator(longitud, nHilos, generaciones, tipo, primeraCelula, flag);
		for(int i=0; i<nHilos; i++) {
			hilos[i]=new ca1DSimulator(vbase, vtope);
			vbase=vtope;
			if(vtope<longitud) {
				if(longitud<nHilos) {
					vtope++;
				}
				else {
					if((longitud%nHilos)==0) {
						vtope+=longitud/nHilos;
					}
					else {
						if(i==(nHilos-2)) {
							vtope+=(longitud/nHilos)+(longitud%nHilos);
						}
						else {
							vtope+=longitud/nHilos;
						}
					}
				}
			}
		}
		for(int j=0; j<nHilos; j++) {
			ejecutor.execute(hilos[j]);
		}
		ejecutor.shutdown();
		while(!ejecutor.isTerminated()) {}
		int[][] res=objeto.mostrar(generaciones);
		//CALCULAR HAMMING ******************************************
		int[] y=new int[generaciones];
		for(int k=0; k<generaciones; k++) {
			int[] vec1=new int[longitud];
			int[] vec2=new int[longitud];
			for(int l=0; l<longitud; l++) {
				vec1[l]=res[k][l];
				vec2[l]=res[k+1][l];
			}
			y[k]=Hamming(vec1, vec2);
		}
		//CALCULAR ENTROPIA ESPACIAL ******************************************
		double[] e=new double[generaciones+1];
		for(int k=0; k<=generaciones; k++) {
			int[] vec3=new int[longitud];
			for(int l=0; l<longitud; l++) {
				vec3[l]=res[k][l];
			}
			e[k]=Entropia(vec3);
		}
		//CALCULAR ENTROPIA TEMPORAL ******************************************
		System.out.println();
		int[] vectorTemporal=new int[generaciones+1];
		for(int i=0; i<=generaciones; i++) {
			vectorTemporal[i]=res[i][indice];
		}
		double temporal=Entropia(vectorTemporal);
		//MOSTRAR MATRIZ ********************************************
		/*for(int i=0; i<=generaciones; i++) {
			for(int j=0; j<longitud; j++) {
				System.out.print(" "+res[i][j]);
			}
			System.out.println();
		}
		System.out.println();*/
		//MOSTRAR HAMMING *******************************************
		/*for(int m=0; m<generaciones; m++) {
			System.out.print(" "+y[m]);
		}
		System.out.println("\n");*/
		//MOSTRAR ENTROPIA *******************************************
		/*for(int n=0; n<=generaciones; n++) {
			System.out.print(" "+e[n]);
		}
		System.out.println("\n");*/
		//MOSTRAR ENTROPIA TEMPORAL **********************************
		System.out.println("ENTROPIA TEMPORAL RESULTANTE: "+temporal);
		//************************************************************
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Automata Celular");
                int[][] resT = new int[res[0].length][res.length];
      			for (int x=0; x < res.length; x++) {
        			for (int y=0; y < res[x].length; y++) {
            			resT[y][x] = res[x][y];
        			}
    			}
                DibujarMatriz dibujo = new DibujarMatriz(resT);
                frame.add(dibujo);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
		//***********************************************************
		//MOSTRAR GRAFICA HAMMING *******************************************
		Grafica miObjeto = new Grafica();
		JFrame pWindow = new JFrame("HAMMING");
		JPanel miPanel = new JPanel();
		Dimension dim = new Dimension(1200,800);
		int WinLocation = 0;
		double[] arrayY = new double[y.length];
		for(int i=0; i<y.length; ++i)
		{
			arrayY[i]=y[i];
		}
		miPanel.setOpaque(true);
	    miPanel.setPreferredSize(dim);
		miObjeto.drawGraphic(miPanel, arrayY, "String Nombre Eje X", "String Nombre Eje Y");
		pWindow.add(miPanel);
		pWindow.pack();
		pWindow.setLocation(0, WinLocation);
		pWindow.setVisible(true);
		pWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//MOSTRAR GRAFICA ENTROPIA *****************************************
		Grafica ObjetoEntropia = new Grafica();
		JFrame Windowentropia = new JFrame("ENTROPIA ESPACIAL");
		JPanel PanelEntropia = new JPanel();
		PanelEntropia.setOpaque(true);
	    PanelEntropia.setPreferredSize(dim);
		ObjetoEntropia.drawGraphic(PanelEntropia, e, "String Nombre Eje X", "String Nombre Eje Y");
		Windowentropia.add(PanelEntropia);
		Windowentropia.pack();
		Windowentropia.setLocation(0, WinLocation);
		Windowentropia.setVisible(true);
		Windowentropia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}