package minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import player.Player;

public class State {
	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	public static final int N = 8;
	public static final int TO_WIN = 4;
	private static final int LongestChainCompUtilWeight = 10;
	private static final int LongestChainOppUtilWeight = 10;
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
	
	public int utility() {

		if (numPieces == N * N) {
			return 0; // Returning 0 if the board is filled up and there is a
						// draw.
		}

		int utilityVal = 0;

		// Find longest chain of computer
		int longestChainValue = longestChain(Player.COMPUTER);
		// Give length of longest chain for computer a weight.
		utilityVal += longestChainValue * LongestChainCompUtilWeight;

		
		// Find longest chain of opponent
		longestChainValue = longestChain(Player.OPPONENT);
		//Give length of longest chain for opponent a weight.
		utilityVal -= longestChainValue * LongestChainOppUtilWeight;

		
		// Add preference to moves closer to the center of the board
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (board[i][j] == Player.COMPUTER.value()) {
					utilityVal -= Math.abs(4 - j);
					utilityVal -= Math.abs(4 - i);
				} else if (board[i][j] == Player.OPPONENT.value()) {
					utilityVal += Math.abs(4 - j);
					utilityVal += Math.abs(4 - i);
				}
			}
		}
		
		//Get top number of chars found in any row for comp and opp, stored in an array.
		int[] topNumCharsRowCol = getTopNumCharsRowCol();
		//Get least number of pieces comp and opp have to place to be able to win in top row and cols. 
		int[] numCharsToWin = getNumCharsToWin(topNumCharsRowCol);
		
		//Get proper value for topCharsValue based on subtracting opp from comp.
		int topCharsValue = (topNumCharsRowCol[0] + topNumCharsRowCol[1]) - (topNumCharsRowCol[2] + topNumCharsRowCol[3]);
		//Get proper value for numCharsleftValue based on subtracting opp from comp.
		int numCharsLeftValue = (numCharsToWin[0] + numCharsToWin[1]) - (numCharsToWin[2] + numCharsToWin[3]);
		
		//Adjust utilityVal to portray topCharsValue and numCharsLeftValue.
		utilityVal += (topCharsValue) + (numCharsLeftValue);
		
		return utilityVal; // Returning the utility value otherwise.
	}

	private int longestChain(Player player) {
		int longest = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				longest = Math.max(longest, lengthFromCell(i, j, player));
			}
		}
		return longest;
	}

	private int lengthFromCell(int i, int j, Player player) {
		return Math.max(
				longestChainWithDirection(i, j, Direction.UP, player)
						+ longestChainWithDirection(i, j, Direction.DOWN,
								player),
				longestChainWithDirection(i, j, Direction.LEFT, player)
						+ longestChainWithDirection(i, j, Direction.RIGHT,
								player));
	}

	private int longestChainWithDirection(int row, int col, Direction dir,
			Player player) {
		int length = 0;
		int i = row;
		int j = col;
		if (dir == Direction.UP && i == 0 || dir == Direction.DOWN
				&& i == N - 1 || dir == Direction.LEFT && j == 0
				|| dir == Direction.RIGHT && j == N - 1) {
			return length;
		}
		while (i >= 0 && i < N && j >= 0 && j < N
				&& board[i][j] == player.value()) {
			if (dir == Direction.UP) {
				i--;
			} else if (dir == Direction.DOWN) {
				i++;
			} else if (dir == Direction.LEFT) {
				j--;
			} else if (dir == Direction.RIGHT) {
				j++;
			}
			length++;
		}
		if (chainCanBeCompleted(row, col, dir, player)) {
			return length - 1;
		}
		return 0;
	}

	private boolean chainCanBeCompleted(int row, int col, Direction dir,
			Player player) {
		Player opponent = player == Player.COMPUTER ? Player.OPPONENT
				: Player.COMPUTER;
		int possibleLength = 0;
		switch (dir) {
		case UP:
		case DOWN:
			for (int i = row; i < N; i++) {
				if (board[i][col] != opponent.value()) {
					possibleLength++;
					if (possibleLength == 4) {
						return true;
					}
				}
			}
			for (int i = row - 1; i >= 0; i--) {
				if (board[i][col] != opponent.value()) {
					possibleLength++;
					if (possibleLength == 4) {
						return true;
					}
				}
			}
			break;
		case LEFT:
		case RIGHT:
			for (int i = col; i < N; i++) {
				if (board[row][i] != opponent.value()) {
					possibleLength++;
					if (possibleLength == 4) {
						return true;
					}
				}
			}
			for (int i = col - 1; i >= 0; i--) {
				if (board[row][i] != opponent.value()) {
					possibleLength++;
					if (possibleLength == 4) {
						return true;
					}
				}
			}
			break;

		}
		return false;
	}
	
	public int[] getTopNumCharsRowCol() {
		//int[0] compTopNumCharsRow, int[1] compTopNumCharsCol, int[2] oppTopNumCharsRow, int[3] oppTopNumCharsCol
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
 		
 		numCharsToWin[0] = TO_WIN - topNumCharsRowCol[0];//Comp Row Chars to Win
 		numCharsToWin[1] = TO_WIN - topNumCharsRowCol[1];//Comp Col Chars to Win
 		
 		numCharsToWin[2] = TO_WIN - topNumCharsRowCol[2];//Opp Row Chars to Win
 		numCharsToWin[3] = TO_WIN - topNumCharsRowCol[3];//Opp Col Chars to Win
 		
 		return numCharsToWin;
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
