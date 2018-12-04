package minimax.minimax;

import java.util.ArrayList;
import java.util.List;

import player.Player;

public class State {
	public static final int N = 8;
	public static final int TO_WIN = 4;
	private int v = 0;
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
		return successors;
	}

	public int utility() {

		if (numPieces == N * N) {
			return 0; // Returning 0 if the board is filled up and there is a
						// draw.
		}

		final int DIM = N;
		int topCharsValueWeight = 100;
		int numCharsLeftValueWeight = 200;

		int[] topNumCharsRowCol = getTopNumCharsRowCol();
		int[] numCharsToWin = getNumCharsToWin(topNumCharsRowCol);

		int topCharsValue = (topNumCharsRowCol[0] + topNumCharsRowCol[1])
				- (topNumCharsRowCol[2] + topNumCharsRowCol[3]);
		int numCharsLeftValue = (numCharsToWin[0] + numCharsToWin[1]) - (numCharsToWin[2] + numCharsToWin[3]);

		// System.out.println("comp Row: " + compTopNumCharsRow + "\n comp Col:
		// " + compTopNumCharsCol);
		// System.out.println("opp Row: " + oppTopNumCharsRow + "\n opp Col: " +
		// oppTopNumCharsCol);

		int utilityVal = (topCharsValueWeight * topCharsValue) + (numCharsLeftValueWeight * numCharsLeftValue);

		// System.out.println(toString());
		// System.out.println(utilityVal);

		return utilityVal; // Returning the utility value otherwise.
	}

	public int[] getTopNumCharsRowCol() {
		// int[0] compTopNumCharsRow, int[1] compTopNumCharsCol, int[2]
		// oppTopNumCharsRow, int[3] oppTopNumCharsCol
		int[] topNumCharsRowCol = new int[4];

		int compTopNumCharsRow = 0;
		int compTopNumCharsCol = 0;

		int oppTopNumCharsRow = 0;
		int oppTopNumCharsCol = 0;

		// Counting top num of chars for all rows for player and opponent.
		for (int r = 0; r < N; r++) {
			int compNumCharsRow = 0;
			int oppNumCharsRow = 0;
			for (int c = 0; c < N; c++) {
				if (getBoard()[r][c] == Player.COMPUTER.value()) {
					compNumCharsRow++;
				}
				if (getBoard()[r][c] == Player.OPPONENT.value()) {
					oppNumCharsRow++;
				}
			}
			if (compNumCharsRow > compTopNumCharsRow) {
				compTopNumCharsRow = compNumCharsRow;
			}
			if (oppNumCharsRow > oppTopNumCharsRow) {
				oppTopNumCharsRow = oppNumCharsRow;
			}
		}

		// Counting top num of chars for all cols for player and opponent.
		for (int c = 0; c < N; c++) {
			int compNumCharsCol = 0;
			int oppNumCharsCol = 0;
			for (int r = 0; r < N; r++) {
				if (getBoard()[r][c] == Player.COMPUTER.value()) {
					compNumCharsCol++;
				}
				if (getBoard()[r][c] == Player.OPPONENT.value()) {
					oppNumCharsCol++;
				}
			}
			if (compNumCharsCol > compTopNumCharsCol) {
				compTopNumCharsCol = compNumCharsCol;
			}
			if (oppNumCharsCol > oppTopNumCharsCol) {
				oppTopNumCharsCol = oppNumCharsCol;
			}
		}

		topNumCharsRowCol[0] = compTopNumCharsRow;
		topNumCharsRowCol[1] = compTopNumCharsCol;
		topNumCharsRowCol[2] = oppTopNumCharsRow;
		topNumCharsRowCol[3] = oppTopNumCharsCol;

		return topNumCharsRowCol;

	}

	public int[] getNumCharsToWin(int[] topNumCharsRowCol) {
		int[] numCharsToWin = new int[4];

		numCharsToWin[0] = TO_WIN - topNumCharsRowCol[0];// Comp Row Chars to
															// Win
		numCharsToWin[1] = TO_WIN - topNumCharsRowCol[1];// Comp Col Chars to
															// Win

		numCharsToWin[2] = TO_WIN - topNumCharsRowCol[2];// Opp Row Chars to Win
		numCharsToWin[3] = TO_WIN - topNumCharsRowCol[3];// Opp Col Chars to Win

		return numCharsToWin;
	}

	public void setV(int newV) {
		this.v = newV;
	}

	public int getV() {
		return this.v;
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
