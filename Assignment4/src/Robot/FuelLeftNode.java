package Robot;

public class FuelLeftNode implements SenNode {

	@Override
	public int execute(Robot robot) {
		return robot.getFuel();

	}
	@Override
	public String toString(){
		
		return "FuelLeft";
	}

}
