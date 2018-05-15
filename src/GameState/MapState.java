package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import Entity.Missile;
import Main.Background;
import Entity.Boss;
import Entity.Enemy;
import Entity.Player;
import Entity.SubBoss;
import Main.GamePanel;

public class MapState extends GameState {

	private Background bg1;
	private Background bg2;
	private Background clouds;

	private Player player;
	private ArrayList<Enemy> enemy;
	private Boss boss;
	
	private int score = 0;
	private int wave = 3;
	private boolean finalStage = false;
	private int level = 1;
	private SubBoss subBoss1;
	private SubBoss subBoss2;
	
	private String[] ship = {
			"alien1.png",
			"RD2N.png",
			"att2.png"
	};

	private static int screenOffset = 480;

	public MapState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {

		player = new Player(GamePanel.WIDTH / 2 - 10, 300);

		boss = new Boss(GamePanel.WIDTH/2,40,6);
		subBoss1 = new SubBoss(40,40,6,"greenship1.png");
		subBoss2 = new SubBoss(GamePanel.WIDTH/2-50,40,6,"greenship2.png");

		//score = 3000;
		loadBackground();
		loadEnemy();
	}

	@Override
	public void update() {

		player.update();

		updateBossMissiles();
		
		if(score >= 3000) {                                   //Level 1 sub boss
			subBoss1.update(player.x, player.width);
			
			if(subBoss1.overlaps(player))
				player.setToDead();
		}
		
		if(score >= 6000 && subBoss1.isDead()) {
			subBoss2.update(player.x, player.width+50);  //Level 2 sub boss
			
			if(subBoss2.overlaps(player))
				player.setToDead();
		}

		if(score >= 9000 && subBoss1.isDead() && subBoss2.isDead()) {     //Level 3 Boss
			boss.update(player.x, player.width);

			if (boss.overlaps(player))
				player.setToDead();
		}


		updateEnemy();
		updatePlayerCollision();

		updatePlayerMissiles(player.getLeftMissile());
		updatePlayerMissiles(player.getRightMissile());


		bg1.update();
		bg2.update();

		if (clouds.y >= 250)
			clouds.setPosition(0, -clouds.getHeight() + screenOffset);

		clouds.update();

		if(bg1.y >= screenOffset)
			bg1.setPosition(0, -bg1.getHeight()*2 + screenOffset);

		if(bg2.y >= screenOffset)
			bg2.setPosition(0, -bg2.getHeight()*2 + screenOffset);
		
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

		if(score >= 3000)                         //Draw sub boss 1
			subBoss1.draw(g);

		if(score >= 6000 && subBoss1.isDead())      //Draw sub boss 2
			 subBoss2.draw(g);

		if(score >= 9000 && subBoss2.isDead() && subBoss1.isDead() )     //Draw final boss
			boss.draw(g);

		for (int i = 0; i < enemy.size(); i++)
				enemy.get(i).draw(g);

		//Draw player if he is not dead
		if (!player.isDead()) player.draw(g);


		//Set font and color for level and score
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
		g.setColor(Color.red);

		//Draw score of how many enemy has been shot
		g.drawString("" + score, 40, 40);
		g.drawString("Level: " + level, 40, 60);

	}


	private void updateEnemy() {

		if(enemy.size() == 0){
		   addNewEnemy();
		   addNewEnemy();
		}

		for (int i = 0; i <enemy.size(); i++) {

			enemy.get(i).update();    //Update enemy

			//The enemy will be set to dead if it is not dead
			//and is off the screen.
			if ((enemy.get(i).y >= GamePanel.HEIGHT ||
				enemy.get(i).x >= GamePanel.WIDTH) &&
				!enemy.get(i).isDead()) {

				enemy.get(i).setToDead();

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

	private void loadEnemy() {

		enemy = new ArrayList<>();
		Random random = new Random();                    //To start the enemy in different positions
		for (int i = 0; i < wave; i++) {

			int x = random.nextInt(GamePanel.WIDTH);   //Get a random number between the size of the window

			Enemy temp = new Enemy(x, -10 * i, 1, ship[random.nextInt(3)]);
			enemy.add(temp);
			temp.setDirection((int) player.x, GamePanel.HEIGHT);
		}

	}

	private void updatePlayerCollision(){

		for (int i = 0; i < enemy.size(); i++) {

			if(!enemy.get(i).isDead()) {
				if (enemy.get(i).overlaps(player)) {

					enemy.get(i).setToDead();
					player.setToDead();
					//gsm.setState(2);
				}
			}
		}
	}

	
	private void updateBossMissiles() {
		checkBossMissiles(boss.getMissile1());
		checkBossMissiles(boss.getMissile2());
		checkBossMissiles(boss.getMissile3());
		checkBossMissiles(boss.getMissile4());
		checkBossMissiles(boss.getMissile5());

		checkBossMissiles(subBoss1.getMissile1());
		checkBossMissiles(subBoss2.getMissile1());
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

	private void addNewEnemy() {


		if(score == 3000) {
			level++;
			wave += 2;
		}
		else if(score == 6000) {
			level++;
			wave += 2;
		}

		if(score == 9000) 
			finalStage = true;


		Random random = new Random(); // Start enemy in a random x position

		int x = random.nextInt(GamePanel.WIDTH); // Get a random number between the size of the window
		int y = random.nextInt(20) + 1;
		
		
		Enemy temp = new Enemy(x, -y, 1,ship[random.nextInt(3)]);
		enemy.add(temp);
		temp.setDirection((int) player.x, (int) GamePanel.HEIGHT);

		/*Enemy temp = army.get(0);
		temp.setDirection((int) player.x, GamePanel.HEIGHT);
		enemy.add(temp);
		army.remove(0);*/
	}


	private void updatePlayerMissiles(ArrayList<Missile> missile){


		for (int i = 0; i < enemy.size(); i++) {

			for (int j = 0; j < missile.size(); j++) {

				if (missile.get(j).overlaps(enemy.get(i))) {  //If leftMissile over lap enemy
															// set enemy to dead and remove missile

					enemy.get(i).setToDead();

					if(!finalStage)
						addNewEnemy();

					score += 100;
					missile.remove(j);

					break;
				}
			}

		}

		//Remove missiles that are off screen
		for(int j = 0; j<missile.size(); j++){
			if(missile.get(j).x <= 0 || missile.get(j).y <= 0)
				missile.remove(j);
		}


		//check collision with bosses
		for (int j = 0; j < missile.size(); j++) {

			if (missile.get(j).overlaps(boss)){

				score += 100;
				missile.remove(j);
				boss.addHit();
				break;
			}

			if( !subBoss1.isDead() && score >= 3000 && missile.get(j).overlaps(subBoss1)) {
				score += 100;
				subBoss1.addHit();
				missile.remove(j);
				break;

			}

			if(!subBoss2.isDead() && score >= 6000 && missile.get(j).overlaps(subBoss2)) {
				subBoss2.addHit();
				score += 100;
				missile.remove(j);
				break;
			}
		}
	}



}

