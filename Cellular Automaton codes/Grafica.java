import java.io.*;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;
import java.util.Arrays;

public class Grafica {
	public void drawGraphic(JPanel mainPanel, double[] array, String xString, String yString)
	{
		class drawGraphic extends JComponent
		{
	        //private static final long serialVersionUID = 1L;
	        public drawGraphic(){setPreferredSize(mainPanel.getPreferredSize());}
	        @Override
	        public void paintComponent(Graphics g)
	        {
	        	Dimension thisDim = mainPanel.getPreferredSize();
   				int[] p = new int[2];
				int frame = 10,
					margin= 40,
					panelHeight=(int)thisDim.getHeight(),
					panelWidth =(int)thisDim.getWidth(),
					hLines = panelHeight/10,
					wLines = panelWidth/10,
					gridWidth	= panelWidth-(margin+frame),
					gridHeight	= panelHeight-(margin+frame);
				double[] aux = new double[array.length];
				for(int i=0; i<array.length; i++)
					aux[i] = array[i];
				Arrays.sort(aux);
				double	aMax	= aux[aux.length-1],
						aMin	= aux[0],
						aRange	= aMax-aMin,
						nElem	= array.length,
						aMean	= aMin+aRange/2;
	    //Draw surf:
	    /////////////////////////////////////
	        //Painting Frame
	            mainPanel.setBackground(new Color(219, 230, 246)); 
	        //Painting Grid White Panel
	            g.setColor(new Color(246, 250, 253)); 
	            g.fillRect(margin, frame, gridWidth, gridHeight);
	            p[0] = margin;					//sets x in Grid down left corner
				p[1] = panelHeight-margin-1;	//sets y in Grid down left corner

		//Drawing the grid
		///////////////////
		g.setColor(new Color(200, 220, 255));
			//drawing basic horizontal lines
				for(int y=p[1]; y>=frame; y=y-10)
					g.drawLine(margin,y,	panelWidth-frame-1,y);
			//drawing basic vertical lines
				for(int x=margin; x<=panelWidth-frame; x=x+10)
					g.drawLine(x, frame,	x, p[1]);


			//Draw Graphic Axis
	            g.setColor(new Color(55, 70, 120));
				//Y Axis
					g.drawLine(margin, frame,	margin, p[1]);
					g.drawLine(margin-1, frame,	margin-1, p[1]+1);
				//X Axis
					g.drawLine(margin, p[1],	panelWidth-frame-1, p[1]);
					g.drawLine(margin, p[1]+1,	panelWidth-frame-1, p[1]+1);
				//Draw Leters
				g.setFont(new Font("Arial", 0, 18));
				g.setColor(new Color(0,0,0));
				g.drawString("y", margin-13, frame+26);
				g.drawString("x", panelWidth-(frame+2), panelHeight-(margin-15));
				g.setFont(new Font("Arial", 0, 10));
				g.drawString(String.format("%.2f",aMin),	margin-25, panelHeight-margin);
				g.drawString(String.format("%.2f",aMean),	margin-25, panelHeight-margin-10*(int)(gridHeight/20));
				g.drawString(String.format("%.2f",aMax),	margin-25, panelHeight-margin-10*(int)(gridHeight/10));

			//Draw graph:
			/////////////////////////////////////////
				g.setColor(new Color(255,0,0));
				double yper, xinc;
				int yp1, yp2, xp;
				xinc = ((gridWidth+margin)/nElem);
				xp=p[0];
				yper = (array[0]-aMin)*gridHeight/aRange;
				yp1 = (int)(yper);
				yp2=yp1;

				for(int i=0; i<(nElem-1); i++)
				{
					yper = (array[i+1]-aMin)*gridHeight/aRange;
					yp2 = (int)(yper);


					if(i==0)
					{
						g.setColor(new Color(255,255,0));
						g.fillOval(xp-3,p[1]-yp1-3,7,7);
						g.setColor(new Color(255,0,0));
						g.fillOval(xp-2,p[1]-yp1-2,5,5);
					}
					else
						g.fillOval(xp-2,p[1]-yp1-2,5,5);
					g.drawLine(xp,p[1]-yp1,(int)(xp+xinc),p[1]-yp2);
					g.setColor(new Color(0,0,0));
					g.drawString(""+i+"",	xp, panelHeight-(margin-15));
					g.setColor(new Color(255,0,0));

					xp = (int)(xp+xinc);
					yp1 = yp2;
				}
				g.setColor(new Color(255,255,50));
				g.fillOval(xp-3,p[1]-yp2-3,7,7);
				g.setColor(new Color(255,0,0));
				g.fillOval(xp-2,p[1]-yp2-2,5,5);
		    }
		}
		mainPanel.add(new drawGraphic());
	}

	/*public static void main(String[] args)
	{
		Grafica miObjeto = new Grafica();
		JFrame pWindow = new JFrame("Titulo de la grafica");
		JPanel miPanel = new JPanel();
		Dimension dim = new Dimension(600,200);
		int WinLocation = 0;
		double[] array = new double[40];
		for(int i=0; i<40; ++i)
		{
			if(i%2==0)
				array[i]=i+1;
			else
				array[i]=i-2;
		}
		miPanel.setOpaque(true);
	    miPanel.setPreferredSize(dim);
		miObjeto.drawGraphic(miPanel, array, "String Nombre Eje X", "String Nombre Eje Y");
		pWindow.add(miPanel);
		pWindow.pack();
		pWindow.setLocation(0, WinLocation);
		pWindow.setVisible(true);
		pWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
}