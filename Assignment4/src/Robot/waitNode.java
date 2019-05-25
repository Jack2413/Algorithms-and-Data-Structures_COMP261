package Robot;

public class waitNode implements RobotProgramNode {
	final EXPNode exp;

	public waitNode(EXPNode parseEXP) {
		exp = parseEXP;
		// TODO Auto-generated constructor stub
	}

	public waitNode() {
		this.exp = null;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Robot robot) {
		if (exp == null) {
			robot.idleWait();
		} else {
			for (int i = 0; i < exp.execute(robot); i++) {
				robot.idleWait();
			}
		}
	}

	@Override
	public String toString() {
		if (exp == null) {
			return "wait;";
		}
		return "wait" + "[(" + exp.toString() + ")]";
	}
}
