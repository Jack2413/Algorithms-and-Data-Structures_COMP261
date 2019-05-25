package Robot;

public class ShieldOnNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.setShield(true);

	}
	@Override
	public String toString() {
		return "ShieldOn";
	}


}
