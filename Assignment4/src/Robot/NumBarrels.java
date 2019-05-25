package Robot;

public class NumBarrels implements SenNode {

	@Override
	public int execute(Robot robot) {
		return robot.numBarrels();

	}
	@Override
	public String toString(){
		
		return "numBarrels";
	}


}
