
package Entity;
import Audio.Sound;
import Main.GamePanel;
import Main.Animation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Player extends Rectangle {

    private BufferedImage player;
	
	private boolean moveLeft;
	private boolean moveRight;
	private boolean moveUp;
	private boolean moveDown;
	private boolean shoot;
	private boolean isDead;

	private int dx = 4;
	private int dy = 4;
	
	private Animation explosion;
	private ArrayList<Missile> rightMissile;
	private ArrayList<Missile> leftMissile;

	private Sound shootingSound;

	public Player(int x, int y) {

		super(x,y,0,0);
		explosion = new Animation("Resources/explosion/explosion_", ".png", 38, 10);
		isDead = false;

		shootingSound = new Sound("Resources/sound/shooting.wav");

		try {
			player = ImageIO.read(new File("Resources/Background/player.png"));
			width = player.getWidth();
			height = player.getHeight();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		leftMissile = new ArrayList<>();
		rightMissile = new ArrayList<>();

	}
	
	public void moveLeft(boolean b) { moveLeft = b; }
	public void moveRight(boolean b) { moveRight = b; }
	public void moveUp(boolean b)  { moveUp = b; }
	public void moveDown(boolean b) {moveDown = b;}

	public void shoot(boolean b) {
		shoot = b;
	}
	
	public void update() {

		if(moveLeft &&(x-dx)>0)                         //Move to the left as long as
			x -= dx;                                     // it is not at the left end of the screen

		if(moveRight && (x+dx)<(GamePanel.WIDTH-width))   //Move to the right as long as it is not at the right
			x += dx;                                      //end of the  screen

		if(moveUp && (y-dy)>0)                            //Move up as long as it is not at the above edge of the screen
			y -= dy;

		if(moveDown && (y+dy)<(GamePanel.HEIGHT-height))  //Move down as long as it not at the below edge of the screen
			y += dy;


		if(shoot){
			rightMissile.add(new Missile(x+5,y-3,true));
			leftMissile.add(new Missile(x+width-8,y-3,true));
			shootingSound.play();
		}

		launchMissile(rightMissile);
		launchMissile(leftMissile);
	}
	 
	public void draw(Graphics g) {


		if(!explosion.isAnimationOver()) {
			
			if(!isDead) {
				g.drawImage(player, (int)x, (int)y,width,height, null);
				drawMissiles(g);
			}
			else {
				g.drawImage(explosion.nextImage(), (int)x, (int)y, null);
			}
		}
		
	}
	
	public ArrayList<Missile> getRightMissile() {
		return rightMissile;
	}
	
	public ArrayList<Missile> getLeftMissile() {
		return leftMissile;
	}
	
	public void setToDead() {
		isDead = true;
	}
	
	public boolean isDead() {
		return isDead;
	}

	public boolean isMoving(){
		return moveLeft || moveRight || moveDown || moveUp;
	}

	public boolean isAnimationOver(){
		return explosion.isAnimationOver();
	}

	private void drawMissiles(Graphics g){

		for(int i = 0; i< rightMissile.size(); i++)
			rightMissile.get(i).draw(g);

		for(int i = 0; i< leftMissile.size(); i++)
			leftMissile.get(i).draw(g);

	}

	private void launchMissile(ArrayList<Missile> m){

		for(int i = 0; i< m.size(); i++)
			m.get(i).launch();

	}

}
