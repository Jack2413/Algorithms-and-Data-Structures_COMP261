package Robot;

public class TurnAroundNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.turnAround();

	}
	@Override
	public String toString() {
		return "turnAround";
	}

}
