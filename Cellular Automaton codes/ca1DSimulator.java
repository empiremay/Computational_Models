import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class ca1DSimulator implements ca1DSim,Runnable {
	static int[][] celulas;
	static int[] sucesor;
	static Random random=new Random();
	static CyclicBarrier barrera;
	static int longitud;
	static int generaciones;
	static int tipo;
	int min;
	int max;
	int gen;

	public ca1DSimulator(int longitud, int vBarrera, int generaciones, int tipo, int[] primeraCelula, Boolean flag) {
		this.longitud=longitud;
		this.generaciones=generaciones;
		this.tipo=tipo;
		barrera=new CyclicBarrier(vBarrera);
		celulas=new int[generaciones+1][longitud];
		sucesor=new int[longitud];
		if(!flag) {
			for(int i=0; i<longitud; i++) {
				celulas[0][i]=random.nextInt(2);
			}
		}
		else {
			for(int i=0; i<longitud; i++) {
				celulas[0][i]=primeraCelula[i];
			}
		}
	}

	public ca1DSimulator(int min, int max) {
		this.min=min;
		this.max=max;
	}

	public void run() {
		for(int i=1; i<=generaciones; i++) {
			caComputation(i);
			try {
				Thread.sleep(2);
			} catch(InterruptedException e) {}
		}
	}

	public void nextGen() {
		switch(tipo) {
			case 1:
				for(int i=min; i<max; i++) {
					if(i==0) {
						sucesor[i]=((celulas[gen][longitud-1]*celulas[gen][i+1])+celulas[gen][i])%2;
					}
					else {
						if(i==(longitud-1)) {
							sucesor[i]=((celulas[gen][i-1]*celulas[gen][0])+celulas[gen][i])%2;
						}
						else {
							sucesor[i]=((celulas[gen][i-1]*celulas[gen][i+1])+celulas[gen][i])%2;
						}
					}		
				} break;
			case 2:
				for(int i=min; i<max; i++) {
					if(i==0 || i==(longitud-1)) {
						sucesor[i]=(celulas[gen][i])%2;
					}
					else {
						sucesor[i]=((celulas[gen][i-1]*celulas[gen][i+1])+celulas[gen][i])%2;
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
	}

	public int[][] mostrar(int generaciones) {
		return celulas;
	}
}