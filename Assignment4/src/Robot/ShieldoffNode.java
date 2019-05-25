package Robot;

public class ShieldoffNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.setShield(false);

	}
	@Override
	public String toString() {
		return "Shieldoff";
	}


}
