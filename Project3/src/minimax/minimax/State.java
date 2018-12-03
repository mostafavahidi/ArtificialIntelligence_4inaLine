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

	public int utility(State state, Player player) {
		final int DIM = this.N;
	    
	    int compTopNumCharsRow = 0;
	    int compTopNumCharsCol = 0;
	    
	    int oppTopNumCharsRow = 0;
	    int oppTopNumCharsCol = 0;
	    
	    //Counting top num of chars for all rows for player and opponent.
	    for (int r = 0; r < DIM; r++) {
	    	int compNumCharsRow = 0;
	    	int oppNumCharsRow = 0;
	    	for (int c = 0; c < DIM; c++) {
	    		if (state.getBoard()[r][c] == player.value()) {
	    			compTopNumCharsRow++;
	    		} else {
	    			oppTopNumCharsRow++;
	    		}
	    	}
	    	if (compNumCharsRow > compTopNumCharsRow) {
	    		compTopNumCharsRow = compNumCharsRow;
	    	}
	    	if (oppNumCharsRow > oppTopNumCharsRow) {
	    		oppTopNumCharsRow = oppNumCharsRow;
	    	}
	    }
	    
	    //Counting top num of chars for all cols for player and opponent.
	    for (int c = 0; c < DIM; c++) {
	    	int compNumCharsCol = 0;
	    	int oppNumCharsCol = 0;
	    	for (int r = 0; r < DIM; r++) {
	    		if (state.getBoard()[r][c] == player.value()) {
	    			compTopNumCharsCol++;
	    		} else {
	    			oppTopNumCharsCol++;
	    		}
	    	}
	    	if (compNumCharsCol > compTopNumCharsCol) {
	    		compTopNumCharsCol = compNumCharsCol;
	    	}
	    	if (oppNumCharsCol > oppTopNumCharsCol) {
	    		oppTopNumCharsCol = oppNumCharsCol;
	    	}
	    }
	}
	
	public void setV(int newV) {
		this.v = newV;
	}

	public int getV() {
		return this.v;
	}

	public char[][] getBoard() {
		return board;
	}

}
