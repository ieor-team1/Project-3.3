import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;

public class Avoider {

	DifferentialPilot _pilot;
	Detector _dec;
	Navigator _nav;
	int _overangle = 7;

	public Avoider(DifferentialPilot p1, Detector dec, Navigator nav) {
		_pilot = p1;
		_dec = dec;
		_nav = nav;
		// _minavoid = _nav._distance;
	}

	public void avoid(int angle, int distance) { // give touch sensor is hit
		_pilot.stop();
		_pilot.travel(-20);

//		if (_nav.buttonalertR && _nav.buttonalertL == false) {
//			_pilot.rotate(35);
//			_pilot.travel(40);
//			_pilot.rotate(-35);
//			_nav.buttonalertR = false;
//			_dec._PathAngle = 0;
//		} else if (_nav.buttonalertL && _nav.buttonalertR == false) {
//			_pilot.rotate(-35);
//			_pilot.travel(40);
//			_pilot.rotate(35);
//			_nav.buttonalertL = false;
//			_dec._PathAngle = 0;
//		} else {
			_dec.checkAround();

			System.out.println("Path Angle" + _dec._PathAngle);
			if (_dec._PathAngle >= 0) {
				_dec._PathAngle = -_dec._PathAngle - _overangle;
				_pilot.rotate(_dec._PathAngle);
			} else if (_dec._PathAngle < 0) {
				_dec._PathAngle = -_dec._PathAngle + _overangle;
				_pilot.rotate(_dec._PathAngle);
			}
			System.out.println("Max distance" + _dec._maxDistance);
			if (_dec._maxDistance < 50) {
				_pilot.travel(_dec._maxDistance / 1.1, true); //B Change from 1.3
			} else if (_dec._maxDistance >= 50) {
				_pilot.travel(80, true); //B (Change from 70
			}
//			_nav.buttonalertL = false;
//			_nav.buttonalertR = false;
		//}
	}
}
