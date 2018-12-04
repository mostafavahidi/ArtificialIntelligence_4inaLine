package minimax;

public class Timer implements Runnable {
	private Thread parentThread;

	Timer(Thread parentThread) {
		this.parentThread = parentThread;
	}

	@Override
	public void run() {
		final long SECONDS_25 = 25000;
		try {
			System.out.println("Timer is going to sleep for 25 seconds...");
			Thread.sleep(SECONDS_25);
			parentThread.interrupt();
			System.out.println("Time is up, parent thread interrupted!");
		} catch (InterruptedException e) {
			// The timer has been canceled
		}
	}

}
