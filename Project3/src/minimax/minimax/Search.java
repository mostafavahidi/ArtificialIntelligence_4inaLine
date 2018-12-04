
package minimax.minimax;

import java.util.List;
import java.util.Random;

import player.Player;

public class Search {
	private final Random rand = new Random();

	public State a_b_search(State state) {
		if (state.getNumPieces() == 0) {
			// First turn in game -> Place piece randomly within 2 spaces of the
			// center cells
			int middle = State.N / 2;
			int row = rand.ints(middle - 2, middle + 2).findFirst().getAsInt();
			int col = rand.ints(middle - 2, middle + 2).findFirst().getAsInt();
			state.move(new Action(row, col, Player.COMPUTER));
			return state;
		}
		List<State> successors = state.getSuccessors();
		int v = maxVal(state, successors, -1000000, 1000000);

		// TODO iterative deepening and randomly select value if enough
		// successors have a very similar utility value. Try going for depth
		// instead of breadth
		for (State nextState : successors) {
			if (nextState.getV() == v) {
				Thread.interrupted(); // Clear interrupted flag just in case
										// it's raised
				return nextState;
			}
		}
		Thread.interrupted(); // Clear interrupted flag just in case it's raised
		return null;

	}

	// Returns a utility value
	public int maxVal(State state, List<State> successors, int alpha, int beta) {
		// Time has run out, return whatever the current state holds

		if (terminalTest(state)) {
			return state.utility();
		}

		state.setV(-1000000);
		int v = state.getV();
		List<State> nextSuccessors;
		for (State nextState : successors) {
			nextSuccessors = nextState.getSuccessors();
			v = Math.max(v, minVal(nextState, nextSuccessors, alpha, beta));

			// Time is out, use whatever value the method is currently at
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return v;
			}
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;

	}

	// Returns a utility value
	public int minVal(State state, List<State> successors, int alpha, int beta) {

		if (terminalTest(state)) {
			return state.utility();
		}

		state.setV(1000000);
		int v = state.getV();
		List<State> nextSuccessors;
		for (State nextState : successors) {
			nextSuccessors = nextState.getSuccessors();
			v = Math.min(v, maxVal(nextState, nextSuccessors, alpha, beta));

			// Time is out, use whatever value the method is currently at
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return v;
			}
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
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
