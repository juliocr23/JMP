package Entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Main.Animation;

public class Boss extends Rectangle{
	
	private BufferedImage img;
	
	private int numberOfHits;
	private int totalHits;
	private double shootTimer;
	private ArrayList<Missile> missile1;
	private ArrayList<Missile> missile2;
	private ArrayList<Missile> missile3;
	private ArrayList<Missile> missile4;
	private ArrayList<Missile> missile5;
	private Animation explosion;
	private boolean isDead;
	
	
	public Boss(double x, double y,int total) {
		super(x,y,0,0);
		
		explosion = new Animation("Resources/explosion/explosion_", ".png", 38, 10);
		numberOfHits = 0;
		totalHits = total;
		isDead = false;
		missile1 = new ArrayList<>();
		missile2 = new ArrayList<>();
		missile3 = new ArrayList<>();
		missile4 = new ArrayList<>();
		missile5 = new ArrayList<>();
		
	
		try {
			img = ImageIO.read(new File("Resources/Background/Boss.png"));
			width = 150;
			height = 200;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(double px,int pw) {
		
		shootTimer += 0.1;
	
		
        if (px-pw < x) 
           x--;
        else 
           x++;
        
        if(numberOfHits == totalHits)
        	isDead = true;
        
        if(shootTimer >= 1) {
        	
        	
        	Random rand = new Random();
        	
        	int random1 = rand.nextInt(5)+1;
        	//int random2 = rand.nextInt(5)+1;
        	//int random3 = rand.nextInt(5)+1;
        	
        	if(random1 == 1)
        		missile1.add(new Missile(x+20,y+40,false));
        	
        	if(random1 == 2)
        		missile2.add(new Missile(x+width-55,y+15,false));
        	
        	if(random1 == 3)
        		missile3.add(new Missile(x+20,y+150,false));
        	
        	if(random1 == 4)
        		missile4.add(new Missile(x+width-55,y+180,false));
        	
        	if(random1 == 5)
        		missile5.add(new Missile(x+width-10,y+88,false));
			
			
			shootTimer = 0;
		}
        
        launchMissile(missile1);
        launchMissile(missile2);
        launchMissile(missile3);
        launchMissile(missile4);
        launchMissile(missile5);
	}
	
	
	public void draw(Graphics g) {
		
		if(isDead) {
			g.drawImage(explosion.nextImage(), (int)x, (int)y, null);
		}else {
		    g.drawImage(img, (int)x,(int)y,150,200,null);
			drawMissiles(g);
		}
	}
	
	
	private void drawMissiles(Graphics g){

		drawM(missile1,g);
		drawM(missile2,g);
		drawM(missile3,g);
		drawM(missile4,g);
		drawM(missile5,g);

	}

	private void launchMissile(ArrayList<Missile> m){

		for(int i = 0; i< m.size(); i++)
			m.get(i).launch();

	}
	
	private void drawM(ArrayList<Missile> m,Graphics g){
		for(int i = 0; i< m.size(); i++)
			m.get(i).draw(g);
	}

	public ArrayList<Missile> getMissile1() {
		return missile1;
	}

	public void setMissile1(ArrayList<Missile> missile1) {
		this.missile1 = missile1;
	}

	public ArrayList<Missile> getMissile2() {
		return missile2;
	}

	public void setMissile2(ArrayList<Missile> missile2) {
		this.missile2 = missile2;
	}

	public ArrayList<Missile> getMissile3() {
		return missile3;
	}

	public void setMissile3(ArrayList<Missile> missile3) {
		this.missile3 = missile3;
	}

	public ArrayList<Missile> getMissile4() {
		return missile4;
	}

	public void setMissile4(ArrayList<Missile> missile4) {
		this.missile4 = missile4;
	}

	public ArrayList<Missile> getMissile5() {
		return missile5;
	}

	public void setMissile5(ArrayList<Missile> missile5) {
		this.missile5 = missile5;
	}
	
	public void addHit() {
		numberOfHits++;
	}
	
	public boolean isDead() {
		return explosion.isAnimationOver();
	}
	

}
