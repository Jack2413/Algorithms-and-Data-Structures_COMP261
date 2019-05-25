package Robot;

import java.util.*;

public class progNode implements RobotProgramNode {

	private final List<StmtNode> Stmts = new ArrayList<StmtNode>();

	public void execute(Robot r) {
		for (StmtNode stmt : Stmts) {
			stmt.execute(r);
		}
	}
	
	public void addStmtNode(StmtNode Stmt) {
		Stmts.add(Stmt);
	}
	@Override
	public String toString() {
		String result = "";
		for (RobotProgramNode n : Stmts) {
			result += n.toString()+"\n";
		}
		return result;
	}
}
