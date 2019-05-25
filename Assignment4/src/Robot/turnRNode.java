package Robot;

public class turnRNode implements RobotProgramNode {

	@Override
    public void execute(Robot robot) {
        robot.turnRight();
    }

    @Override
    public String toString() {
        return "turnR;";
    }

}
