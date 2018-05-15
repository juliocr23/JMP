package GameState;

import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import Entity.Missile;
import Main.Background;
import Entity.Boss;
import Entity.Enemy;
import Entity.Player;
import Main.GamePanel;

public class MapState extends GameState {

	private Background bg1;
	private Background bg2;
	private Background clouds;

	private Player player;
	private ArrayList<Enemy> enemy;
	private Boss boss;
	private int score = 0;
	private int wave = 5;
	
	private static int screenOffset = 480;

	public MapState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {

		player = new Player(GamePanel.WIDTH / 2 - 10, 300);
		boss = new Boss(GamePanel.WIDTH/2,40,3);
		loadBackground();
		loadEnemy();
	}

	@Override
	public void update() {

		player.update();
		boss.update(player.x,player.width);
		
		if(boss.overlaps(player)) 
			player.setToDead();
		
		//if(boss.isDead())
			//Go to completed gameState
		
		updateEnemy();
		updateBossMissiles();
		
		updatePlayerCollision();
		updateRightMissileCollision();
		updateLeftMissileCollision();

		bg1.update();
		bg2.update();
		if (clouds.y >= 250) clouds.setPosition(0, -clouds.getHeight() + screenOffset);
		clouds.update();
		if(bg1.y >= screenOffset) bg1.setPosition(0, -bg1.getHeight()*2 + screenOffset);
		if(bg2.y >= screenOffset) bg2.setPosition(0, -bg2.getHeight()*2 + screenOffset);
		
		if(player.isDead()) {
			gsm.setState(2);
		}

	}

	@Override
	public void processInput() {
		player.moveLeft(GamePanel.input.keyDown(KeyEvent.VK_LEFT));
		player.moveRight(GamePanel.input.keyDown(KeyEvent.VK_RIGHT));
		player.moveDown(GamePanel.input.keyDown(KeyEvent.VK_DOWN));
		player.moveUp(GamePanel.input.keyDown(KeyEvent.VK_UP));
		player.shoot(GamePanel.input.keyDownOnce(KeyEvent.VK_S));
	}

	@Override
	public void draw(Graphics g) {

		bg1.draw(g);
		bg2.draw(g);
		clouds.draw(g);
		
		boss.draw(g);

		//Draw player if he is not dead
		if (!player.isDead()) player.draw(g);


		//Draw enemy
		for (int i = 0; i < enemy.size(); i++)
				enemy.get(i).draw(g);


		//Draw score of how many enemy has been shot
		g.drawString("" + score, 40, 40);

	}



	private void updateEnemy() {

		for (int i = 0; i <enemy.size(); i++) {

			enemy.get(i).update();    //Update enemy

			//The enemy will be set to dead if it is not dead
			//and is off the screen.
			if ((enemy.get(i).y >= GamePanel.HEIGHT ||
				enemy.get(i).x >= GamePanel.WIDTH) &&
				!enemy.get(i).isDead()) {

				enemy.get(i).setToDead();
				addNewEnemy();
			}

			//Remove enemy if dead animation is over.
			if (enemy.get(i).isAnimationOver()) {
				enemy.remove(i);
			}

		}
	}

	private void loadBackground(){

		bg1 	= new Background("Resources/Background/newBG.jpg", 1);
		bg2 	= new Background("Resources/Background/newBG2.jpg", 1);
		clouds 	= new Background("Resources/Background/newCloud.png", 1);

		bg1.setPosition(0, -bg1.getHeight() + screenOffset);
		bg1.setVector(0, 3);
		
		bg2.setPosition(0, -bg2.getHeight()*2 + screenOffset);
		bg2.setVector(0, 3);
		
		clouds.setPosition(150, -clouds.getHeight() + screenOffset);
		clouds.setVector(0, 0.5);
	}

	private void loadEnemy(){

		enemy = new ArrayList<>();

		Random random = new Random();     				//To start the enemy in different positions
		for (int i = 0; i < wave; i++) {

			int x = random.nextInt(GamePanel.WIDTH);  //Get a random number between the size of the window

			Enemy temp = new Enemy(x, -10*i, 1);
			enemy.add(temp);
			temp.setDirection((int)player.x,GamePanel.HEIGHT);
		}
	}

	private void updateRightMissileCollision(){

		ArrayList<Missile> rightMissile = player.getRightMissile();

		//Check collision with enemies
		for (int i = 0; i < enemy.size(); i++) {

			for (int j = 0; j < rightMissile.size(); j++) {

				if (rightMissile.get(j).overlaps(enemy.get(i)) && !enemy.get(i).isDead()) {  //If rightMissile over lap enemy
					                                               							 // set enemy to dead and remove missile

					enemy.get(i).setToDead();
					addNewEnemy();
					score += 100;
					rightMissile.remove(j);

					break;
				}

				//Remove missiles that are off screen
				if(rightMissile.get(j).x <= 0 ||
				   rightMissile.get(j).y <= 0){

					rightMissile.remove(j);
				}
			}
		}
		
		//check collision with boss
		for (int j = 0; j < rightMissile.size(); j++) {

			if (rightMissile.get(j).overlaps(boss)){  //If rightMissile over lap enemy
				                                     // set enemy to dead and remove missile
				score += 100;
				rightMissile.remove(j);
				boss.addHit();
				break;
			}

			//Remove missiles that are off screen
			if(rightMissile.get(j).x <= 0 ||
			   rightMissile.get(j).y <= 0){

				rightMissile.remove(j);
			}
		}
		
		
		
	}
	
	
	

	private void updateLeftMissileCollision(){

		ArrayList<Missile> leftMissile = player.getLeftMissile();

		for (int i = 0; i < enemy.size(); i++) {

			for (int j = 0; j < leftMissile.size(); j++) {

				if (leftMissile.get(j).overlaps(enemy.get(i))) {  //If leftMissile over lap enemy
					                                              // set enemy to dead and remove missile

					enemy.get(i).setToDead();
					addNewEnemy();
					score += 100;
					leftMissile.remove(j);

					break;
				}

				//Remove missiles that are off screen
				if(leftMissile.get(j).x <= 0 ||
					leftMissile.get(j).y <= 0){

					leftMissile.remove(j);
				}
			}
		}
	}

	private void updatePlayerCollision(){

		for (int i = 0; i < enemy.size(); i++) {

			if (enemy.get(i).overlaps(player)) {

				enemy.get(i).setToDead();
				player.setToDead();
				//gsm.setState(2);
			}
		}
	}

	private void addNewEnemy(){
		Random random = new Random();     				//Start enemy in a random x position

		int x = random.nextInt(GamePanel.WIDTH);  		//Get a random number between the size of the window

		int y = random.nextInt(20)+1;
		Enemy temp = new Enemy(x, -y, 1);
		enemy.add(temp);
		temp.setDirection((int)player.x,(int)GamePanel.HEIGHT);
	}
	
	private void updateBossMissiles() {
		checkBossMissiles(boss.getMissile1());
		checkBossMissiles(boss.getMissile2());
		checkBossMissiles(boss.getMissile3());
		checkBossMissiles(boss.getMissile4());
		checkBossMissiles(boss.getMissile5());
	}
	
	private void checkBossMissiles(ArrayList<Missile> m) {

		for (int j = 0; j < m.size(); j++) {

			if (m.get(j).overlaps(player)){  
				player.setToDead();
				break;
			}
			
			//Remove missiles that are off screen
			if(m.get(j).x <= 0 ||
				m.get(j).y <= 0){
				m.remove(j);
			}
		}
	}

}

