package Robot;

public class ASSGNNode implements RobotProgramNode {
	
	final VARNode var;
	final EXPNode exp;

	public ASSGNNode(VARNode varNode, EXPNode expNode) {
		this.var = varNode;
		this.exp = expNode;
	}

	@Override
	public void execute(Robot robot) {
		 var.setValue(exp.execute(robot));

	}
	@Override
    public String toString() {
        return var.toString() + " = " + exp.toString() + ";";
    }

}
