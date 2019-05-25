package Robot;

public class WallDistNode implements SenNode {

	@Override
	public int execute(Robot robot) {
		return robot.getDistanceToWall();
		

	}
	@Override
	public String toString(){
		
		return "WallDist;";
	}

}
