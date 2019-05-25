package Robot;


public class ActNode implements RobotProgramNode {
	
	private final RobotProgramNode Child;
	
	public ActNode (RobotProgramNode Child){
		
		this.Child=Child;
	}
	@Override
	public void execute(Robot robot){
		Child.execute(robot);
	}
	@Override
	public String toString(){
		
		return Child.toString();
		
	}

}
