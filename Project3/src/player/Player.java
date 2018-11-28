package player;

public enum Player {
	OPPONENT('O'), COMPUTER('C');

	private char value;

	Player(char value) {
		this.value = value;
	}

	public char value() {
		return value;
	}
}
