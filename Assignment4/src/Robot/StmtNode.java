package Robot;

import java.util.*;

public  class StmtNode implements RobotProgramNode {
	
	private final RobotProgramNode Child;
	
	public StmtNode(RobotProgramNode Child){
		
		this.Child=Child;
	}
	@Override
	public String toString(){
		String result ="[";
	
			result += Child.toString();
		
		return result+"]";
	}
	@Override
	public void execute(Robot robot){
		Child.execute(robot);
	}

}
