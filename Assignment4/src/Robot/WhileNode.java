package Robot;

public class WhileNode  implements RobotProgramNode {
	
    private BlockNode blockNode;
    private CondNode condNode;

    public WhileNode(CondNode condNode, BlockNode blockNode) {
        this.blockNode = blockNode;
        this.condNode = condNode;
    }

    @Override
    public void execute(Robot robot) {
        while (condNode.execute(robot)) {
            blockNode.execute(robot);
        }
    }

    @Override
    public String toString() {
        return "While " +condNode.toString() + blockNode.toString();
    }


}
