package paquete;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.util.concurrent.atomic.*;

public class belZabParalelo implements Runnable {
	static Scanner teclado=new Scanner(System.in);
	static float[][][] a;
	static float[][][] b;
	static float[][][] c;
	static float alfa;		//1.2 default
	static float beta;		//1.0 default
	static float gamma;		//1.0 default

	static Random random=new Random();
	static CyclicBarrier barrera;
	static int nHilos;
	static int tamano;	//La matriz en cuadrada
	static int iteraciones;
	static Color[][] color;
	int p=0;
	int q=1;
	int min;
	int max;

	public belZabParalelo(int tamano, int iteraciones, int vBarrera, float alfa, float beta, float gamma) {
		this.tamano=tamano;
		this.iteraciones=iteraciones;
		this.alfa=alfa;
		this.beta=beta;
		this.gamma=gamma;
		nHilos=vBarrera;
		barrera=new CyclicBarrier(vBarrera);
		
		a=new float[tamano][tamano][2];
		b=new float[tamano][tamano][2];
		c=new float[tamano][tamano][2];
		color=new Color[tamano][tamano];
		
		//Asignar primera configuracion (Completamente aleatoria)
		for(int i=0; i<tamano; i++) {
			for(int j=0; j<tamano; j++) {
				a[i][j][p]=random.nextFloat();
				b[i][j][p]=random.nextFloat();
				c[i][j][p]=random.nextFloat();
			}
		}
	}
	
	public float constrain(float valor, float min, float max){
        return Math.min(Math.max(valor, min),max); 
	}

	public belZabParalelo(int min, int max) {
		this.min=min;
		this.max=max;
	}

	public void run() {
		caComputation();
	}

	public Color[][] devolver_Color() {
		return color;
	}

	public void caComputation() {
		nextGen();
		try {
			barrera.await();
		} catch(Exception ex) {}
	}

	public void nextGen() {
		for(int x=0; x<tamano; x++) {
			for(int y=min; y<max; y++) {
				float c_a=0.0f;
				float c_b=0.0f;
				float c_c=0.0f;
				for (int i = x-1; i <= x+1; i++){
					for (int j = y - 1; j <= y +1; j++) {
						c_a += a[(i+ tamano)%tamano][(j+tamano)%tamano][p];
						c_b += b[(i+ tamano)%tamano][(j+tamano)%tamano][p];
						c_c += c[(i+ tamano)%tamano][(j+tamano)%tamano][p];
					}
				}
				c_a /= 9.0;
				c_b /= 9.0;
				c_c /= 9.0;
				a[x][y][q] = constrain(c_a+c_a*(alfa*c_b-gamma*c_c),0.0f,1.0f);
				b[x][y][q] = constrain(c_b+c_b*(beta*c_c-alfa*c_a),0.0f,1.0f);
				c[x][y][q] = constrain(c_c+c_c*(gamma*c_a-beta*c_b),0.0f,1.0f);
				
				int ab=(int)(255*a[x][y][q]);
    			int bb=(int)(255*b[x][y][q]);
    			int cb=(int)(255*c[x][y][q]);
				
				color[x][y]=new Color(a[x][y][q],b[x][y][q],c[x][y][q]);
			}
		}
		if(p==0) {
			p=1;
			q=0;
		}
		else {
			p=0;
			q=1;
		}
	}
}