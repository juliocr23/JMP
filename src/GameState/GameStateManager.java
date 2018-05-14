package GameState;

import java.awt.*;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int MAP_STATE = 1;

	public GameStateManager() {
		
		gameStates = new GameState[4];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}

	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);

		if(state == MAP_STATE)
			gameStates[state] = new MapState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);

	}

	public void processInput(){
		gameStates[currentState].processInput();
	}

	public void update() {
		gameStates[currentState].update();
	}

	public void draw(Graphics g) {
		gameStates[currentState].draw(g);
	}

}
