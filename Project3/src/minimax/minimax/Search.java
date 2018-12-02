package minimax;

import java.time.Instant;

import player.Player;

public class Search {

	public State a_b_search(State state) {
		Instant startTime = Instant.now();
		int v = maxVal(state, -1000000, 1000000);
		// Todo: return the action in Successors(state) with value v

		State nextState = statesList[0];
		
		for (State stateIterator : statesList) {
			if (stateIterator.getV() == v) {
				nextState = stateIterator;
			}
		}
		return nextState;

	}

	// Returns a utility value
	public int maxVal(State state, int alpha, int beta) {

		if (terminalTest(state)) {
			return utility(state, Player.COMPUTER);
		}

		state.setV(1000000);
		int v = state.getV();

		for (State successorState : statesList) {
			v = Math.max(v, minVal(successorState, alpha, beta));
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;

	}

	// Returns a utility value
	public int minVal(State state, int alpha, int beta) {

		if (terminalTest(state)) {
			return utility(state, Player.OPPONENT);
		}

		state.setV(1000000);
		int v = state.getV();

		for (State successorState : statesList) {
			v = Math.min(v, maxVal(successorState, alpha, beta));
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

	private int utility(State state) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean terminalTest(State state) {
			char[][] board = state.getBoard();
			
		    final int DIM = state.N;
		    
		    final char EMPTY_SPACE = '\u0000';
		    for (int r = 0; r < DIM; r++) {
		        for (int c = 0; c < DIM; c++) {
		             char player = board[r][c];
		            if (player == EMPTY_SPACE)
		                continue;
		            if (c + 3 < DIM &&
		                player == board[r][c+1] &&
		                player == board[r][c+2] &&
		                player == board[r][c+3])
		                return true;
		            if (r + 3 < DIM) {
		                if (player == board[r+1][c] &&
		                    player == board[r+2][c] &&
		                    player == board[r+3][c])
		                    return true;
		            }
		        }
		    }
		    return false; // no winner found
	}
	
}
