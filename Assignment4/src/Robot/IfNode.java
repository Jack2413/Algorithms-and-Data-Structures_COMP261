package Robot;

import java.util.ArrayList;
import java.util.List;

public class IfNode implements RobotProgramNode {

	private final CondNode condNode;
	private final BlockNode blockNode;

	private List<CondNode> elseifConds = new ArrayList<CondNode>();
	private List<BlockNode> elseifblocks = new ArrayList<BlockNode>();
	private final BlockNode elseblock;

	public IfNode(CondNode condNode, BlockNode blockNode, BlockNode elseblock, List<CondNode> elseifconds,
			List<BlockNode> elseifblocks) {
		this.blockNode = blockNode;
		this.condNode = condNode;
		this.elseblock = elseblock;
		this.elseifConds = elseifconds;
		this.elseifblocks = elseifblocks;
	}

	@Override
	public void execute(Robot robot) {
		
		if (condNode.execute(robot)) {
			blockNode.execute(robot);
		} else {
			if (!elseifConds.isEmpty() && !elseifblocks.isEmpty()) {
				boolean perform = false;
				for (int i = 0; i < elseifConds.size(); i++) {
					if (elseifConds.get(i).execute(robot)) {
						elseifblocks.get(i).execute(robot);
						perform = true;
						break;
					}
				}
				if (!perform) {
					if (elseblock != null) {
						elseblock.execute(robot);
					}
				}
			} else {
				if ( elseblock != null) {
					elseblock.execute(robot);
				}
			}
		}
	}

	@Override
	public String toString() {
		String result = "If " + condNode.toString() + blockNode.toString();
		if (!elseifConds.isEmpty() && !elseifblocks.isEmpty()) {
			for (int i = 0; i < elseifConds.size(); i++) {
				result += elseifConds.get(i).toString() + elseifblocks.get(i).toString();
			}
		}

		if (elseblock != null) {
			result += elseblock.toString();
		}
		return result;
	}

}
