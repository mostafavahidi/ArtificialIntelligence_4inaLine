package minimax.minimax;

public class Timer implements Runnable {
	private Thread parentThread;

	Timer(Thread parentThread) {
		this.parentThread = parentThread;
	}

	@Override
	public void run() {
		final long SECONDS_25 = 25000;
		try {
			Thread.sleep(SECONDS_25);
			parentThread.interrupt();
			System.out.println("Time is up, search has been interruted!");
		} catch (InterruptedException e) {
			// The timer has been canceled
		}
	}

}
