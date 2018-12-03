package minimax.minimax;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	private static final List<Action> ACTIONS = new ArrayList<>();

	public static void main(String[] args) {
		Search searcher = new Search();
		State state = new State();
		System.out.print("Who goes first, player or opponent? [p/o]: ");
		String whoGoesFirst = scanner.nextLine();
		boolean aiFirst = true;
		if (whoGoesFirst.matches("[pP]")) {
			aiFirst = true;
		} else if (whoGoesFirst.matches("[oO]")) {
			aiFirst = false;
		}
		run(state, searcher, aiFirst);
	}

	private static void run(State state, Search searcher, boolean aiFirst) {
		Action opponentMove = null;
		Action playerAction = null;
		Timer timer;
		while (!searcher.terminalTest(state)) {
			// Start the 25 second timer
			
			if (!aiFirst) {
				opponentMove = Action.createAction(getOpponentMove());
				ACTIONS.add(opponentMove);
				state.move(opponentMove);
				state = searcher.a_b_search(state);
				ACTIONS.add(state.getMostRecentAction());
				printBoard(state, playerAction, aiFirst);
			}
			if (aiFirst) {
				state = searcher.a_b_search(state);
				ACTIONS.add(state.getMostRecentAction());
				printBoard(state, playerAction, aiFirst);
				opponentMove = Action.createAction(getOpponentMove());
				state.move(opponentMove);
				ACTIONS.add(opponentMove);
			}
		}
	}

	private static void printBoard(State state, Action playerAction, boolean aiFirst) {
		System.out.print("\n\n ");
		final String TAB = "     ";
		for (int i = 1; i <= State.N; i++) {
			System.out.print(i);
		}
		System.out.print(TAB);
		if (aiFirst) {
			System.out.println("Player vs. Opponent");
		} else {
			System.out.print("Opponent vs. Player");
		}
		char[][] board = state.getBoard();
		char currentRow = 65;
		int currentAction = 0;
		for (int i = 0; i < State.N; i++, currentRow++) {
			System.out.println(currentRow);
			for (int j = 0; j < State.N; j++) {
				System.out.println(board[i][j]);
			}
			System.out.print(TAB);
			System.out.print(ACTIONS.get(currentAction).toString());
			currentAction++;
			System.out.print(TAB);
			System.out.println(ACTIONS.get(currentAction).toString());
			currentAction++;
		}
		System.out.print("Player's move is: ");
	}

	private static String getOpponentMove() {
		System.out.print("Enter opponent's next move: ");
		String move = scanner.nextLine();
		System.out.println();
		final String MOVE_FORMAT = "[a-h][1-8]";
		while (!move.matches(MOVE_FORMAT)) {
			System.out.println("Invalid input, try again.");
			System.out.print("Enter opponents next move: ");
			move = scanner.nextLine();
		}
		return move;
	}
}
