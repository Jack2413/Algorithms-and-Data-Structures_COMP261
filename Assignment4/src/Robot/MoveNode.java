package Robot;

public class MoveNode implements RobotProgramNode {
	final EXPNode exp;
	
	public MoveNode(EXPNode parseEXP) {
		this.exp = parseEXP;
	}

	public MoveNode() {
		this.exp = null;// TODO Auto-generated constructor stub
	}

	@Override
    public void execute(Robot robot) {
		if (exp == null){
        robot.move();
		}else{
			for(int i=0;i<exp.execute(robot);i++)
			robot.move();
		}
    }

    @Override
    public String toString() {
    	if(exp==null){
        return "Move;";
    	}
    	else{
    		return "move" + "[(" + exp.toString() + ")]";
    	}
    
    }
}
