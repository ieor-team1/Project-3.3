import lejos.nxt.Button;

public class NavigatorTest { // main class to test Navigator

	/**
	 * main class to test Navigator
	 */
	public static void main(String[] args) {
		Navigator nav = new Navigator();
		/** creates navigator class */
		Button.waitForAnyPress(); // wait to start
		nav.go();
		/** Tells the robot to go towards the light */
	}

}
