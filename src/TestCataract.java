import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import hypermedia.video.*;

public class TestCataract extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final short[] invertTable;

	public void paint(Graphics g) {
		Image img = (Image)processImage( loadImage());
		g.drawImage(img, 20,20,this);
	}

	private BufferedImage loadImage(){
		
		BufferedImage image = null;
		try {
			//File input = new File("cataract.jpg");
			File input = new File("1.jpg");
			image = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return image;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new TestCataract());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
	
	private Image processImage( BufferedImage img ){
		img = invertImage(img);
		img = grayScale(img);
		img = threshold(img, 220);
		
		
		
	
		return img;
	}
	
	private BufferedImage grayScale( BufferedImage image ){
		
        for(int i=0; i<image.getHeight(); i++){
        
           for(int j=0; j<image.getWidth(); j++){
           
              Color c = new Color(image.getRGB(j, i));
              int red = (int)(c.getRed() * 0.299);
              int green = (int)(c.getGreen() * 0.587);
              int blue = (int)(c.getBlue() *0.114);
              Color newColor = new Color(red+green+blue,
              
              red+green+blue,red+green+blue);
              
              image.setRGB(j,i,newColor.getRGB());
           }
        }
        return image;
	}
	
	private BufferedImage invertImage(final BufferedImage src) {
		final int w = src.getWidth();
		final int h = src.getHeight();
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
		return invertOp.filter(src, dst);
	}
	
	static {
		invertTable = new short[256];
		for (int i = 0; i < 256; i++) {
			invertTable[i] = (short) (255 - i);
		}
	}
	
	private BufferedImage threshold(BufferedImage img,int requiredThresholdValue) {
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage finalThresholdImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB) ;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		for (int x = 0; x < width; x++) {
			try {

				for (int y = 0; y < height; y++) {
					int color = img.getRGB(x, y);

					red = this.getRed(color);
					green = this.getGreen(color);
					blue = this.getBlue(color);

					if((red+green+green)/3 < (int) (requiredThresholdValue)) {
							finalThresholdImage.setRGB(x,y,this.mixColor(0, 0,0));
						}
						else {
							finalThresholdImage.setRGB(x,y,this.mixColor(255, 255,255));
						}
					
				}
			} catch (Exception e) {
				 e.getMessage();
			}
		}
		
		return finalThresholdImage;
	}
	
	private int mixColor(int red, int green, int blue) {
		return red<<16|green<<8|blue;
	}

	public int getRed(int color) {
		return (color & 0x00ff0000)  >> 16;
	}
	
	public int getGreen(int color) {
		return	(color & 0x0000ff00)  >> 8;
	}
	
	public int getBlue(int color) {
		return (color & 0x000000ff)  >> 0;
		
	}
}