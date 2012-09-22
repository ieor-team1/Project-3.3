import lejos.robotics.navigation.DifferentialPilot;


public class Avoider {
	
	DifferentialPilot pilot;

	public Avoider(DifferentialPilot p1){
		pilot = p1;
	}
	public void avoid(int angle, int distance){ //give touch sensor is hit
		pilot.stop();
		pilot.travel(-10);
		
	}
}
