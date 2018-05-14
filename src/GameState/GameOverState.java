package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Main.Background;
import Main.GamePanel;

public class GameOverState extends GameState {
	private Background bg;

	private int currentChoice = 0;
	private String[] options = {"Retry", "Quit"};

	private Color titleColor;
	private Font titleFont;
	private Font titleFont2;
	private Font selectionFont;
	
	
	public GameOverState(GameStateManager gsm) {

		this.gsm = gsm;
		try {

			bg = new Background("Resources/Background/menubg.jpg", 1);

			titleColor 		= new Color(255, 0, 0);
			titleFont 		= new Font("Century Gothic", Font.BOLD, 50);
			titleFont2		= new Font("Century Gothic", Font.PLAIN, 26);
			selectionFont 	= new Font("Arial", Font.BOLD, 24);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		bg.update();
		
	}

	@Override
	public void processInput() {
		// TODO Auto-generated method stub
		if(GamePanel.input.keyDownOnce(KeyEvent.VK_ENTER)) {
			select();
		}
		if(GamePanel.input.keyDownOnce(KeyEvent.VK_UP)) {
			currentChoice--;
			if(currentChoice <= -1) {
				currentChoice = 0;
			}
		}
		if(GamePanel.input.keyDownOnce(KeyEvent.VK_DOWN)) {
			currentChoice++;
			if(currentChoice >= 2) {
				currentChoice = 1;
			}
		}
		
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		// draw bg
				bg.draw(g,GamePanel.WIDTH,GamePanel.HEIGHT);

				// draw title
				g.setColor(titleColor);
				
				g.setFont(titleFont);
				g.drawString("Game Over", GamePanel.WIDTH/2-130, 80);
				
				g.setFont(titleFont2);
				g.drawString("Your plane has crashed!", GamePanel.WIDTH/2-125, 150);

				// draw menu options
				g.setFont(selectionFont);
				for(int i = 0; i < options.length; i++) {
					if(i == currentChoice) {
						g.setColor(Color.BLACK);
					}
					else {
						g.setColor(Color.GRAY);
					}
					g.drawString(options[i], GamePanel.WIDTH/2-25, 310 + i * 25);
				}
		
	}
	
	public void select() {

		if(currentChoice == 0) {
			// Retry
			gsm.setState(1);

		}
		if(currentChoice == 1) {
			// quit
			System.exit(0);

		}
	}

}
