package Robot;

public class BarrelFBNode implements SenNode {

	final EXPNode exp;

	public BarrelFBNode(EXPNode parseEXP) {
		this.exp = parseEXP;
	}
	public BarrelFBNode() {
		this.exp =null;
	}

	@Override
	public int execute(Robot robot) {
		if (this.exp != null) {
			return robot.getBarrelFB(exp.execute(robot));
		} else {
			return robot.getBarrelFB(0);
		}

	}

	@Override
	public String toString() {

		return "BarrelFB";
	}

}
