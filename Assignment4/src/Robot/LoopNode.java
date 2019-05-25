package Robot;

public class LoopNode implements RobotProgramNode {
	
    private BlockNode blockNode;

    public LoopNode(BlockNode blockNode) {
        this.blockNode = blockNode;
    }

    @Override
    public void execute(Robot robot) {
        while (true) {
            blockNode.execute(robot);
        }
    }

    @Override
    public String toString() {
        return "loop " + blockNode.toString();
    }

	
	
	

}
