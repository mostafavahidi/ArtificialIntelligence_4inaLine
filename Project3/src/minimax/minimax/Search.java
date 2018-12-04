package minimax.minimax;

import java.util.List;
import java.util.Random;

import player.Player;

public class Search {
	private final Random rand = new Random();
	private final int WIN_CONST = 200;
	private final long TIME_LIMIT = 25000;
	private long startTime;
	private boolean stop;

	public State getNextState(State state) {
		if (state.getNumPieces() == 0) {
			// First turn in game -> Place piece randomly within 2 spaces of the
			// center cells
			int middle = State.N / 2;
			int row = rand.ints(middle - 2, middle + 2).findFirst().getAsInt();
			int col = rand.ints(middle - 2, middle + 2).findFirst().getAsInt();
			state.move(new Action(row, col, Player.COMPUTER));
			return state;
		}
		// Clear interrupted flag in case it's still set
		Thread.interrupted();
		stop = false;
		State bestState = null;
		int best = Integer.MIN_VALUE;
		int currentScore = 0;
		List<State> successors = state.getSuccessors();
		long searchTime = TIME_LIMIT / successors.size();
		for (State successor : successors) {
			if (!stop) {
				currentScore = iterativeSearch(successor, searchTime);
			}
			if (currentScore > best) {
				best = currentScore;
				bestState = successor;
			}
			if (best >= WIN_CONST) {
				return bestState;
			}
		}
		return bestState;
	}

	public int iterativeSearch(State state, long searchTime) {
		int depth = 1;
		int best = 0;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		startTime = System.currentTimeMillis();
		long endTime = startTime + searchTime;
		while (true) {
			long current = System.currentTimeMillis();
			if (current >= endTime) {
				break;
			}
			best = maxVal(state, alpha, beta, depth, endTime - current);
			// System.out.println(best);
			if (best >= WIN_CONST || stop) {
				break;
			}

			depth++;

		}
		return best;
	}

	// Returns a utility value
	public int maxVal(State state, int alpha, int beta, int depth, long timeLimit) {
		// Time has run out, return whatever the current state holds
		long current = System.currentTimeMillis();
		long elapsed = current - startTime;
		if (terminalTest(state) || depth == 0 || elapsed >= timeLimit) {
			return state.utility();
		}
		// System.out.println(depth);
		List<State> successors = state.getSuccessors();
		for (State nextState : successors) {
			alpha = Math.max(alpha, minVal(nextState, alpha, beta, depth - 1, timeLimit));

			// Time is out, use whatever value the method is currently at
			if (Thread.interrupted()) {
				stop = true;
				return state.utility();
			}
			if (alpha >= beta) {
				break;
			}
		}
		return alpha;
	}

	// Returns a utility value
	public int minVal(State state, int alpha, int beta, int depth, long timeLimit) {
		long current = System.currentTimeMillis();
		long elapsed = current - startTime;
		if (terminalTest(state) || depth == 0 || elapsed >= timeLimit) {
			return state.utility();
		}
		// System.out.println(depth);
		List<State> successors = state.getSuccessors();
		for (State nextState : successors) {
			beta = Math.min(beta, maxVal(nextState, alpha, beta, depth - 1, timeLimit));

			// Time is out, use whatever value the method is currently at
			if (Thread.interrupted()) {
				stop = true;
				return state.utility();
			}
			if (beta <= alpha) {
				break;
			}
		}
		return beta;
	}

	public boolean terminalTest(State state) {

		char[][] board = state.getBoard();

		final int DIM = State.N;

		final char EMPTY_SPACE = '\u0000';
		for (int r = 0; r < DIM; r++) {
			for (int c = 0; c < DIM; c++) {
				char player = board[r][c];
				if (player == EMPTY_SPACE)
					continue;
				if (c + 3 < DIM && player == board[r][c + 1] && player == board[r][c + 2] && player == board[r][c + 3])
					return true;
				if (r + 3 < DIM) {
					if (player == board[r + 1][c] && player == board[r + 2][c] && player == board[r + 3][c])
						return true;
				}
			}
		}
		return false; // no winner found

	}

}
