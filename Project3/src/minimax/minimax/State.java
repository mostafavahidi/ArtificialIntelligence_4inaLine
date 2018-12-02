package minimax;

import java.util.ArrayList;
import java.util.List;

import player.Player;

public class State {
	public static final int N = 8;
	private int v = 0;
	private char[][] board;

	State() {
		board = new char[N][N];
	}

	public void move(Action action) {
		board[action.getRow()][action.getCol()] = action.getPlayer().value();
	}

	private State createState(int row, int col, State currentState) {
		State newState = new State();
		char[][] newBoard = newState.getBoard();
		char[][] currentBoard = currentState.getBoard();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				newBoard[i][j] = currentBoard[i][j];
			}
		}
		newBoard[row][col] = Player.COMPUTER.value();
		return newState;
	}

	/**
	 * Return successors of current state as a list of states.
	 * 
	 * @return List of possible actions
	 */
	public List<State> getSuccessors() {
		List<State> successors = new ArrayList<>();
		final char DEFAULT = '\u0000';
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == DEFAULT) {
					successors.add(createState(i, j, this));
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
	
	public char[][] getBoard(){
		return this.board;
	}

	public char[][] getBoard() {
		return board;
	}

}
