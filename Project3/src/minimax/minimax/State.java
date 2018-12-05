package minimax.minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import player.Player;

public class State {
	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	public static final int N = 8;
	private char[][] board;
	private int numPieces = 0;
	private Action mostRecentAction;

	State() {
		board = new char[N][N];
	}

	public void move(Action action) {
		board[action.getRow()][action.getCol()] = action.getPlayer().value();
		mostRecentAction = action;
		numPieces++;
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
		if (currentState.getMostRecentAction().getPlayer() == Player.COMPUTER) {
			newState.move(new Action(row, col, Player.OPPONENT));
		} else if (currentState.getMostRecentAction().getPlayer() == Player.OPPONENT) {
			newState.move(new Action(row, col, Player.COMPUTER));
		}

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
		Collections.shuffle(successors);
		return successors;
	}

	private int longestChain() {
		int longest = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				longest = Math.max(longest, lengthFromCell(i, j));
			}
		}
		return longest;
	}

	private int lengthFromCell(int i, int j) {
		return Math.max(longestChainWithDirection(i, j, Direction.UP) + longestChainWithDirection(i, j, Direction.DOWN),
				longestChainWithDirection(i, j, Direction.LEFT) + longestChainWithDirection(i, j, Direction.RIGHT));
	}

	private int longestChainWithDirection(int i, int j, Direction dir) {
		int length = 0;
		if (dir == Direction.UP && i == 0 || dir == Direction.DOWN && i == N - 1 || dir == Direction.LEFT && j == 0
				|| dir == Direction.RIGHT && j == N - 1) {
			return length;
		}
		while (i >= 0 && i < N && j >= 0 && j < N && board[i][j] == Player.COMPUTER.value()) {
			if (dir == Direction.UP) {
				i--;
			} else if (dir == Direction.DOWN) {
				i++;
			} else if (dir == Direction.LEFT) {
				j--;
			} else if (dir == Direction.RIGHT) {
				j++;
			} // Check if the chain is capable of being completed. If not, don't
				// increment length
			length++;
		}
		return length - 1;
	}

	public int utility() {

		if (numPieces == N * N) {
			return 0; // Returning 0 if the board is filled up and there is a
						// draw.
		}
		// Make the longest chain weigh more than the following computations
		int utilityVal = longestChain() * 10;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == Player.COMPUTER.value()) {
					utilityVal += Math.abs(3 - j);
				} else if (board[i][j] == Player.OPPONENT.value()) {
					utilityVal -= Math.abs(3 - j);
				}
			}
		}
		// System.out.println(utilityVal);
		return utilityVal; // Returning the utility value otherwise.
	}

	public char[][] getBoard() {
		return this.board;
	}

	public int getNumPieces() {
		return numPieces;
	}

	public Action getMostRecentAction() {
		return mostRecentAction;
	}

	public void setMostRecentAction(Action mostRecentAction) {
		this.mostRecentAction = mostRecentAction;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == '\u0000') {
					s += "-";
				} else {
					s += board[i][j];
				}
			}
			s += "\n";
		}
		return s;
	}

}
