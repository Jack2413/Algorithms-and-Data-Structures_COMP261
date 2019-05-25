package Robot;

import java.util.ArrayList;
import java.util.List;

public class BlockNode implements RobotProgramNode {
	
	private final List<StmtNode> Stmts = new ArrayList<StmtNode>();
	  @Override
	    public void execute(Robot robot) {
	        
		  for (StmtNode stmt : Stmts) {
				stmt.execute( robot);
			}
	        
	    }

	    @Override
	    public String toString() {
	    	
	    	String result = "{\n";
			for (RobotProgramNode n : Stmts) {
				result += n.toString()+"\n";
			}
			return result + "}";
		
	    }

		public void addStmtNode(StmtNode stmt) {
			Stmts.add(stmt);
			
		}

		
	    
}
