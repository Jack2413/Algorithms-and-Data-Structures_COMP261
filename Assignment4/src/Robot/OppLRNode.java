package Robot;

public class OppLRNode implements SenNode {

	@Override
	public int execute(Robot robot) {
		return robot.getOpponentLR();

	}
	@Override
	public String toString(){
		
		return "OpponentLR";
	}


}
