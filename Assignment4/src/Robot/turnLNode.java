package Robot;

public class turnLNode implements RobotProgramNode {

	@Override
    public void execute(Robot robot) {
        robot.turnLeft();
    }

    @Override
    public String toString() {
        return "turnL;";
    }
}

