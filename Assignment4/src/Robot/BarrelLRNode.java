package Robot;

public class BarrelLRNode implements SenNode {

	final EXPNode exp;

	public BarrelLRNode(EXPNode parseEXP) {
		this.exp = parseEXP;
	}

	public BarrelLRNode() {
		this.exp = null;
	}

	@Override
	public int execute(Robot robot) {
		if (this.exp != null) {
			return robot.getBarrelLR(exp.execute(robot));
		} else {
			return robot.getBarrelLR(0);
		}

	}

	@Override
	public String toString() {

		return "BarrelLR";
	}

}
