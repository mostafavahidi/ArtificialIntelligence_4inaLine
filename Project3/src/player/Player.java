package player;

public enum Player {
	OPPONENT('O'), COMPUTER('C');

	private char val;

	Player(char val) {
		this.val = val;
	}

	public char getVal() {
		return val;
	}
}
