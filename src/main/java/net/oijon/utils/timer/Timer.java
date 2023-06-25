package net.oijon.utils.timer;

/**
 * Allows actions to be timed.
 * @author alex
 *
 */
public class Timer {

	private long start = 0;
	private long end = 0;
	
	/**
	 * Creates a timer. The timer does not need any inputs to work.
	 */
	public Timer() {
		
	}
	
	/**
	 * Starts the timer.
	 */
	public void start() {
		start = System.nanoTime();
	}
	
	/**
	 * Stops the timer.
	 */
	public void stop() {
		end = System.nanoTime();
	}
	
	/**
	 * Gets the time elapsed of the timer. Will not give time elapsed if the timer has either not
	 * been started or if the timer is currently running.
	 * @return The time between the start and stop points
	 */
	public String timeElapsed() {
		if (start == 0) {
			return "Timer has not started!";
		} else if (end < start) {
			// The timer, unlike a traditional stopwatch, does not constantly measure the time.
			// Instead, it used start and end points, then gets the time elapsed from them.
			// If start < end, the timer was given a start() but not a stop(), as time moves in
			// one direction.
			return "Timer is running!";
		} else {
			double nano = start - end;
			double micro = nano / 1000D;
			double milli = micro / 1000D;
			double second = milli / 1000D;
			double minute = second / 60D;
			double hour = minute / 60D;
			double day = hour / 24D;
			
			// long if else :/
			if (day < 1) {
				return String.format("%.0f", day) + " days, " +
						String.format("%.0f", hour) + " hours, " +
						String.format("%.0f", minute) + " minutes.";
			} else if (hour < 1) {
				return String.format("%.0f", hour) + " hours, " +
						String.format("%.0f", minute) + " minutes, " +
						String.format("%.0f", second) + " seconds.";
			} else if (minute < 1) {
				return String.format("%.0f", minute) + " minutes, " +
						String.format("%.0f", second) + " seconds.";
			} else if (second < 1) {
				return String.format("%.3f", second) + " seconds.";
			} else if (milli < 1) {
				return String.format("%.3f", milli) + " milliseconds.";
			} else if (micro < 1) {
				return String.format("%.3f", micro) + " microseconds.";
			} else {
				return nano + " nanoseconds.";
			}
		}
	}
	
}
