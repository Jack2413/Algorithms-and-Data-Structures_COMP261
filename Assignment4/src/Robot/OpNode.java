package Robot;

public class OpNode implements EXPNode {
	String symbol;
	EXPNode exp1;
	EXPNode exp2;

	public OpNode(String symbol,EXPNode exp1, EXPNode exp2) {
		this.symbol = symbol;
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	public int execute(Robot robot) {
		switch (symbol) {
		case ("div"):
			return exp1.execute(robot)/exp2.execute(robot);
		case ("add"):
			return exp1.execute(robot)+exp2.execute(robot);
		case ("sub"):
			return exp1.execute(robot)-exp2.execute(robot);
		case ("mul"):
			return exp1.execute(robot)*exp2.execute(robot);
		default:
		return 0;

		}
	}

	@Override
	public String toString() {
		return symbol + "(" + exp1 + "," + exp2 + ")";
	}

}
