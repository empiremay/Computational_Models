import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.util.concurrent.atomic.*;

public class LifeCalculoParalelo implements Runnable {
	static Scanner teclado=new Scanner(System.in);
	static int[][] celulas;
	static int[][] sucesor;
	static int generaciones;
	static Random random=new Random();
	static CyclicBarrier barrera;
	static int nHilos;
	static int tamano;	//La matriz sera cuadrada
	static int iteraciones;
	static AtomicInteger vivas;
	static AtomicInteger muertas;
	int min;
	int max;

	public LifeCalculoParalelo(int tamano, int iteraciones, int vBarrera) {
		this.tamano=tamano;
		this.iteraciones=iteraciones;
		vivas=new AtomicInteger(0);
		muertas=new AtomicInteger(0);
		nHilos=vBarrera;
		barrera=new CyclicBarrier(vBarrera);
		celulas=new int[tamano][tamano];
		sucesor=new int[tamano][tamano];
		//Asignar primera configuracion (Completamente aleatoria)
		for(int i=0; i<tamano; i++) {
			for(int j=0; j<tamano; j++) {
				celulas[i][j]=random.nextInt(2);
			}
		}
		/*celulas[1][2]=1;
		celulas[1][3]=1;
		celulas[1][4]=1;*/
		/*celulas[4][0]=1;
		celulas[5][0]=1;
		celulas[4][1]=1;
		celulas[5][1]=1;
		celulas[4][10]=1;
		celulas[5][10]=1;
		celulas[6][10]=1;
		celulas[3][11]=1;
		celulas[7][11]=1;
		celulas[2][12]=1;
		celulas[8][12]=1;
		celulas[2][13]=1;
		celulas[8][13]=1;
		celulas[5][14]=1;
		celulas[3][15]=1;
		celulas[7][15]=1;
		celulas[4][16]=1;
		celulas[5][16]=1;
		celulas[6][16]=1;
		celulas[5][17]=1;
		celulas[2][20]=1;
		celulas[3][20]=1;
		celulas[4][20]=1;
		celulas[2][21]=1;
		celulas[3][21]=1;
		celulas[4][21]=1;
		celulas[1][22]=1;
		celulas[5][22]=1;
		celulas[0][24]=1;
		celulas[1][24]=1;
		celulas[5][24]=1;
		celulas[6][24]=1;
		celulas[2][34]=1;
		celulas[3][34]=1;
		celulas[2][35]=1;
		celulas[3][35]=1;*/
	}

	public LifeCalculoParalelo(int min, int max) {
		this.min=min;
		this.max=max;
	}

	public void run() {
		caComputation();
		for(int a=0; a<tamano; a++) {
			for(int b=0; b<tamano; b++) {
				celulas[a][b]=sucesor[a][b];
			}
		}
	}

	public int[][] devolverCelulas() {
		return sucesor;//celulas;
	}

	public void resetAtomic() {
		vivas.set(0);
		muertas.set(0);
	}

	public int devolverVivas() {
		return vivas.get();
	}

	public int devolverMuertas() {
		return muertas.get();
	}

	public void caComputation() {
		nextGen();
		try {
			barrera.await();
		} catch(Exception ex) {}
	}

	public void nextGen() {
		for(int i=0; i<tamano; i++) {
			for(int j=min; j<max; j++) {
				int numVivas=0;
				if(i==0) {
					if(j==0) {
						numVivas=celulas[tamano-1][tamano-1]+celulas[tamano-1][j]+celulas[tamano-1][j+1]+celulas[i][tamano-1]+celulas[i][j+1]+celulas[i+1][tamano-1]+celulas[i+1][j]+celulas[i+1][j+1];
					}
					else if(j==(tamano-1)) {
						numVivas=celulas[tamano-1][j-1]+celulas[tamano-1][j]+celulas[tamano-1][0]+celulas[i][j-1]+celulas[i][0]+celulas[i+1][j-1]+celulas[i+1][j]+celulas[i+1][0];
					}
					else {
						numVivas=celulas[tamano-1][j-1]+celulas[tamano-1][j]+celulas[tamano-1][j+1]+celulas[i][j-1]+celulas[i][j+1]+celulas[i+1][j-1]+celulas[i+1][j]+celulas[i+1][j+1];
					}
				}
				else if(i==(tamano-1)) {
					if(j==0) {
						numVivas=celulas[i-1][tamano-1]+celulas[i-1][j]+celulas[i-1][j+1]+celulas[i][tamano-1]+celulas[i][j+1]+celulas[0][tamano-1]+celulas[0][j]+celulas[0][j+1];
					}
					else if(j==(tamano-1)) {
						numVivas=celulas[i-1][j-1]+celulas[i-1][j]+celulas[i-1][0]+celulas[i][j-1]+celulas[i][0]+celulas[0][j-1]+celulas[0][j]+celulas[0][0];
					}
					else {
						numVivas=celulas[i-1][j-1]+celulas[i-1][j]+celulas[i-1][j+1]+celulas[i][j-1]+celulas[i][j+1]+celulas[0][j-1]+celulas[0][j]+celulas[0][j+1];
					}
				}
				else {
					if(j==0) {
						numVivas=celulas[i-1][tamano-1]+celulas[i-1][j]+celulas[i-1][j+1]+celulas[i][tamano-1]+celulas[i][j+1]+celulas[i+1][tamano-1]+celulas[i+1][j]+celulas[i+1][j+1];
					}
					else if(j==(tamano-1)) {
						numVivas=celulas[i-1][j-1]+celulas[i-1][j]+celulas[i-1][0]+celulas[i][j-1]+celulas[i][0]+celulas[i+1][j-1]+celulas[i+1][j]+celulas[i+1][0];
					}
					else {
						numVivas=celulas[i-1][j-1]+celulas[i-1][j]+celulas[i-1][j+1]+celulas[i][j-1]+celulas[i][j+1]+celulas[i+1][j-1]+celulas[i+1][j]+celulas[i+1][j+1];
					}
				}
				//Analisis del numero de numVivas
				if(numVivas<2) {
					sucesor[i][j]=0;
				}
				if(numVivas==2 || numVivas==3) {
					sucesor[i][j]=celulas[i][j];
				}
				if(numVivas>3) {
					sucesor[i][j]=0;
				}
				if(numVivas==3 && celulas[i][j]==0) {
					sucesor[i][j]=1;
				}
				if(sucesor[i][j]==1) {
					vivas.incrementAndGet();
				}
				else {
					muertas.incrementAndGet();
				}
			}
		}
		/*int[][] aux=new int[tamano][tamano];
		for(int a=0; a<tamano; a++) {
			for(int b=min; b<max; b++) {
				aux[a][b]=celulas[a][b];
			}
		}
		for(int a=0; a<tamano; a++) {
			for(int b=min; b<max; b++) {
				celulas[a][b]=sucesor[a][b];
			}
		}
		for(int a=0; a<tamano; a++) {
			for(int b=min; b<max; b++) {
				sucesor[a][b]=aux[a][b];
			}
		}*/
	}

	/*public void run() {
		for(int i=1; i<=generaciones; i++) {
			caComputation(i);
			try {
				Thread.sleep(1);
			} catch(InterruptedException e) {}
		}
	}

	public void nextGen() {	//Seguirá las reglas de Moore
		switch(condicionBarrera) {
			case 2: for(int i=min; i<max; i++) {
						if(i==0 || i==(longitud-1)) {
							sucesor[i]=0;
						}
						else {
							if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[7];}
							if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[6];}
							if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[5];}
							if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[4];}
							if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[3];}
							if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[2];}
							if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[1];}
							if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[0];}
						}
					} break;
			case 1: for(int i=min; i<max; i++) {
						if(i==0) {
							if(celulas[gen][longitud-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[7];}
							if(celulas[gen][longitud-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[6];}
							if(celulas[gen][longitud-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[5];}
							if(celulas[gen][longitud-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[4];}
							if(celulas[gen][longitud-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[3];}
							if(celulas[gen][longitud-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[2];}
							if(celulas[gen][longitud-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[1];}
							if(celulas[gen][longitud-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[0];}
						}
						else {
							if(i==(longitud-1)) {
								if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][0]==0) {sucesor[i]=[7];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][0]==1) {sucesor[i]=[6];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][0]==0) {sucesor[i]=[5];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][0]==1) {sucesor[i]=[4];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][0]==0) {sucesor[i]=[3];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][0]==1) {sucesor[i]=[2];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][0]==0) {sucesor[i]=[1];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][0]==1) {sucesor[i]=[0];}
							}
							else {
								if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[7];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[6];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[5];}
								if(celulas[gen][i-1]==0 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[4];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==0) {sucesor[i]=[3];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==0 && celulas[gen][i+1]==1) {sucesor[i]=[2];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==0) {sucesor[i]=[1];}
								if(celulas[gen][i-1]==1 && celulas[gen][i]==1 && celulas[gen][i+1]==1) {sucesor[i]=[0];}
							}
						}
						
					} break;
		}
		
	}

	public void caComputation(int nGen) {
		gen=nGen-1;
		nextGen();
		try {
			barrera.await();
		} catch(Exception ex) {}
		for(int j=0; j<longitud; j++) {
			celulas[nGen][j]=sucesor[j];
		}
	}*/
}