package Robot;

import java.util.List;

public class CondNode {
	final CondNode cond1;
	final CondNode cond2;
	final EXPNode exp1;
	final EXPNode exp2;
	final String symbol;

	public CondNode(String symbol, EXPNode exp1, EXPNode exp2, CondNode cond1, CondNode cond2) {
		this.cond1 = cond1;
		this.exp1 = exp1;
		this.exp2 = exp2;
		this.cond2 = cond2;
		this.symbol = symbol;

	}

	public boolean execute(Robot robot) {
		switch (symbol) {
		case ("lt"):
			return exp1.execute(robot) < exp2.execute(robot);
		case ("gt"):
			return exp1.execute(robot) > exp2.execute(robot);
		case ("eq"):
			return exp1.execute(robot) == exp2.execute(robot);
		case ("and"):
			return cond1.execute(robot) && cond2.execute(robot);
		case ("or"):
			return cond1.execute(robot) || cond2.execute(robot);
		case ("not"):
			return !cond1.execute(robot);
		}
		return false;
	}

	public String toString() {
		switch (symbol) {
		case ("lt"):
		case ("gt"):
		case ("eq"):
			return "(" + symbol + exp1.toString() + "," + exp2.toString() + ")";
		case ("and"):
		case ("or"):
			return "(" + symbol + cond1.toString() + "," + cond2.toString() + ")";
		case ("not"):
			return "(" + symbol + cond1.toString() + ")";

		}
		return "";
	}
}
