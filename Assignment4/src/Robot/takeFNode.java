package Robot;

public class takeFNode implements RobotProgramNode {

	@Override
    public void execute(Robot robot) {
        robot.takeFuel();
    }

    @Override
    public String toString() {
        return "takeFuel;";
    }

}
