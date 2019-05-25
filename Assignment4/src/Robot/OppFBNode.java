package Robot;

public class OppFBNode implements SenNode {

	@Override
    public int execute(Robot robot) {
        return robot.getOpponentFB();
    }
	
	
    @Override
    public String toString() {
        return "OpponentFB;";
    }
}
