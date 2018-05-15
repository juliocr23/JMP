package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Main.Animation;

public class SubBoss extends Rectangle{

	
	private BufferedImage img;
	
	private int numberOfHits;
	private int totalHits;
	private double shootTimer;
	private ArrayList<Missile> missile1;
	private Animation explosion;
	private boolean isDead;
	
	public SubBoss(double x, double y,int total, String file) {
		super(x,y,0,0);
		
		explosion = new Animation("Resources/explosion/explosion_", ".png", 38, 10);
		missile1 = new ArrayList<>();
		
		try {
			img = ImageIO.read(new File("Resources/Background/"+file));
			width = 80;
			height = 80;
		}catch(Exception  e) {
			e.printStackTrace();
		}
	}
	
	public void update(double px,double py) {
		
		shootTimer += 0.1;
	
		
       /* if (px-pw < x) 
           x--;
        else 
           x++;*/
		
		updateDirection(px,py);
        
        if(numberOfHits == totalHits)
        	isDead = true;
        
        if(shootTimer >= 1) {
        	missile1.add(new Missile(x+25,y+60,false));
			shootTimer = 0;
		}
        
        for(int i = 0; i< missile1.size(); i++)
			missile1.get(i).launch();
	}
	
	public void draw(Graphics g) {
		
		//if(isDead) {
		//	g.drawImage(explosion.nextImage(), (int)x, (int)y, null);
		//}else {
		    g.drawImage(img, (int)x,(int)y,width,height,null);
		    for(int i = 0; i< missile1.size(); i++)
				missile1.get(i).draw(g);
		//}
	}
	
	private void updateDirection(double x2, double y2){

		double deltaX = x2 - x;
		double deltaY = y2 - y;
		double angle = Math.atan2( deltaY, deltaX );

		x += 2 * Math.cos( angle );
		y += 2 * Math.sin( angle );
	}
}
