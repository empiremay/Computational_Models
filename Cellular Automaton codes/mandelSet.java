import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.util.concurrent.*;

public class mandelSet extends JFrame implements Runnable {
	private static int nHilos=Runtime.getRuntime().availableProcessors();
	private final int MAX_ITER = 100000;
	private final double ZOOM = 150;
	private static BufferedImage Imagen;	//Imagen compartida por todos los hilos
	int min;
	int max;

	private double zx, zy, cX, cY, tmp;

	public mandelSet() {
		super("Conjunto de Mandelbrot");
		setBounds(100, 100, 800, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	public mandelSet(int min, int max) {
		this.min=min;
		this.max=max;
	}

	public void run() {
		for (int y = min; y < max; y++) {		//0 a 600
			for (int x = 0; x < 800; x++) {	//0 a 800
				zx = zy = 0;
				cX = (x - 400) / ZOOM;
				cY = (y - 300) / ZOOM;
				int iter = MAX_ITER;
				while (zx * zx + zy * zy < 4 && iter > 0) {
					tmp = zx * zx - zy * zy + cX;
					zy = 2.0 * zx * zy + cY;
					zx = tmp;
					iter--;
				}
				Imagen.setRGB(x, y, iter | (iter << 8));
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(Imagen, 0, 0, this);
	}

	public static void main(String[] args) throws Exception {
		mandelSet objeto=new mandelSet();
		ExecutorService ejecutor=Executors.newFixedThreadPool(nHilos);
		int maxy=600;
		mandelSet hilo1=new mandelSet(0, maxy/4);
		mandelSet hilo2=new mandelSet(maxy/4, 2*maxy/4);
		mandelSet hilo3=new mandelSet(2*maxy/4, 3*maxy/4);
		mandelSet hilo4=new mandelSet(3*maxy/4, maxy);
		long inic=System.currentTimeMillis();
		ejecutor.execute(hilo1);
		ejecutor.execute(hilo2);
		ejecutor.execute(hilo3);
		ejecutor.execute(hilo4);
		ejecutor.shutdown();
		while(!ejecutor.isTerminated()) {}
		long fin=System.currentTimeMillis();
		objeto.setVisible(true);
		System.out.println("Tiempo transcurrido: "+(fin-inic)+" ms");
	}
}