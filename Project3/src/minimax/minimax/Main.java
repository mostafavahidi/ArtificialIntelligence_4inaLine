package minimax.minimax;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	private static final List<Action> ACTIONS = new ArrayList<>();
	private static int currentAction;

	public static void main(String[] args) {
		currentAction = 0;
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
		Timer timer = new Timer(Thread.currentThread());
		Thread timerThread;
		while (true) {
			if (!aiFirst) {
				opponentMove = Action.createAction(getOpponentMove());
				ACTIONS.add(opponentMove);
				state.move(opponentMove);
				if (searcher.terminalTest(state)) {
					System.out.println("Opponent has won!");
					System.exit(0);
				}
				// Start the 25 second timer
				timerThread = new Thread(timer);
				timerThread.start(); // Start the timer
				state = searcher.getNextState(state); // Search
				timerThread.interrupt(); // Cancel the timer
				ACTIONS.add(state.getMostRecentAction());
				printBoard(state, playerAction, aiFirst);
				// If terminal state, declare it and exit
				if (searcher.terminalTest(state)) {
					System.out.println("Player has won!");
					System.exit(0);
				}
			}
			if (aiFirst) {

				// Start the 25 second timer
				timerThread = new Thread(timer);
				timerThread.start(); // Start the timer
				state = searcher.getNextState(state); // Search
				timerThread.interrupt(); // Cancel the timer
				ACTIONS.add(state.getMostRecentAction());
				printBoard(state, playerAction, aiFirst);
				// If terminal state, declare it and exit
				if (searcher.terminalTest(state)) {
					System.out.println("Player has won!");
					System.exit(0);
				}
				opponentMove = Action.createAction(getOpponentMove());
				state.move(opponentMove);
				ACTIONS.add(opponentMove);
				if (searcher.terminalTest(state)) {
					System.out.println("Opponent has won!");
					System.exit(0);
				}
			}
		}
	}

	private static void printBoard(State state, Action playerAction, boolean aiFirst) {
		System.out.print("\n\n ");
		final String TAB = "     ";
		for (int i = 1; i <= State.N; i++) {
			System.out.print(" " + i + " ");
		}
		System.out.print(TAB);
		if (aiFirst) {
			System.out.println("Player vs. Opponent");
		} else {
			System.out.println("Opponent vs. Player");
		}
		currentAction = 0;
		char[][] board = state.getBoard();
		char currentRow = 65;
		final char DEFAULT = '\u0000';
		for (int i = 0; i < State.N; i++, currentRow++) {
			System.out.print(currentRow);
			for (int j = 0; j < State.N; j++) {
				if (board[i][j] == DEFAULT) {
					System.out.print(" - ");
				} else {
					System.out.print(" " + board[i][j] + " ");
				}
			}
			for (int j = 0; j < 2 && currentAction < ACTIONS.size(); j++) {
				System.out.print(TAB);
				if (currentAction % 2 == 0) {
					System.out.print((currentAction == 0 ? 1 : currentAction) + ". ");
				}
				System.out.print(ACTIONS.get(currentAction).toString());
				currentAction++;
				if (i == State.N - 1 && currentAction < ACTIONS.size() - 1) {
					for (; currentAction < ACTIONS.size(); currentAction++) {
						if (currentAction % 2 != 0) {
							System.out.print(TAB);
						} else {
							System.out.print("\n              " + currentAction + ". ");
						}
						System.out.print(ACTIONS.get(currentAction).toString());
					}
				}
			}
			System.out.println();
		}
		System.out.println("Player's move is: " + ACTIONS.get(ACTIONS.size() - 1).toString());
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
