package minimax.minimax;

import player.Player;

public class Action {
	private int row;
	private int col;
	private Player player;

	public Action(int row, int col, Player player) {
		this.row = row;
		this.col = col;
		this.player = player;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Player getPlayer() {
		return player;
	}
	
	public static Action createAction(String moveToMake) {
		int actionRow = 0;
		int actionCol = 0;

		for (int i = 0; i < moveToMake.length(); i++) {
			char charToCheck = moveToMake.charAt(i);
			if (Character.isLetter(charToCheck)) {
				actionRow = (int) charToCheck - 'a';
			} else {
				actionCol = charToCheck - '0';
			}
		}

		Action actionToTake = new Action(actionRow, actionCol, Player.OPPONENT);
		return actionToTake;
	}

	@Override
	public String toString() {
		return String.valueOf((char) row + 'a') + String.valueOf(col + '0');
	}
}
