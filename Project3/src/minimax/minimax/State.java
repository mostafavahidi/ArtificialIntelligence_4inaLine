package minimax;

import java.util.ArrayList;
import java.util.List;

import player.Player;

public class State {
	private static final int N = 8;
	private int v = 0;
	char[][] board;

	State() {
		board = new char[N][N];
	}

	public void move(Action action) {
		board[action.getRow()][action.getCol()] = action.getPlayer().value();
	}

	/**
	 * Return successors of current state as a list of actions.
	 * 
	 * @return List of possible actions
	 */
	public List<Action> getSuccessors() {
		List<Action> successors = new ArrayList<>();
		final char DEFAULT = '\u0000';
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == DEFAULT) {
					successors.add(new Action(i, j, Player.COMPUTER));
				}
			}
		}
		return successors;
	}
	
	public void setV(int newV) {
		this.v = newV;
	}
	
	public int getV() {
		return this.v;
	}

}
