package Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;
import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	public static final Pattern IF = Pattern.compile("if");
	public static final Pattern ACTIONS = Pattern
			.compile("move|turnL|turnR|turnAround|shieldOn|shieldOff|takeFuel|wait");
	public static final Pattern LOOP = Pattern.compile("loop");
	public static final Pattern WHILE = Pattern.compile("while");
	public static final Pattern COND = Pattern.compile("lt|gt|eq|and|or|not");
	public static final Pattern SEN = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	public static final Pattern NUM = Pattern.compile("-?[1-9][0-9]*|0");
	public static final Pattern OP = Pattern.compile("add|sub|mul|div");
	public static final Pattern ELSE = Pattern.compile("else");
	public static final Pattern ELSEIF = Pattern.compile("elif");
	public static final Pattern VAR = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	
	public static HashMap<String, VARNode> variables = new HashMap<>();
	// public static final Pattern STATEMENT = Pattern.compile(ACTIONS|LOOP);
	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement
														// this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		if (!s.hasNext()) {
			fail("", s);
		}
		progNode progNode = new progNode();
		StmtNode child = null;
		while (s.hasNext()) {
			child = parseStmtNode(s);
			// debug(child);
			progNode.addStmtNode(child);

		}

		return progNode;
	}

	private static void debug(StmtNode child) {
		System.out.println(child);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static StmtNode parseStmtNode(Scanner s) {
		RobotProgramNode child = null;

		if (s.hasNext(ACTIONS)) { // if is an Action parseAction
			child = parseAction(s);
		} else if (s.hasNext(LOOP)) {
			child = parseLoop(s);
		} else if (s.hasNext(WHILE)) {
			child = parseWhile(s);
		} else if (s.hasNext(IF)) {
			child = parseIf(s);
		} else if (s.hasNext(VAR)) {
			child = parseAssign(s);
		} else {
			fail("Unkown statement: " + s.next(), s);
			return null; // dead code

		}
		// System.out.println(child);
		// String statement = require(STATEMENT, "Unknown Action command", s);

		return new StmtNode(child);
	}

	private static RobotProgramNode parseAssign(Scanner s) {
		String varName = require(VAR, "No valid variable found", s);

        VARNode varNode = variables.get(varName);
        if (varNode == null) {
            varNode = new VARNode(varName, 0);
            variables.put(varName, varNode);
        }

        require("=", "Needed a \"=\" after the variable name in an assignment", s);

        EXPNode expNode = parseEXP(s);

        require(";", "All assignments has to end with a \";\"", s);

        return new ASSGNNode(varNode, expNode);
   
	}

	private static IfNode parseIf(Scanner s) {

		BlockNode block = null;
		BlockNode elseblock = null;
		List<BlockNode> elseifblock = new ArrayList<BlockNode>();
		CondNode cond = null;
		List<CondNode> elseifcond = new ArrayList<CondNode>();
		String If = require(IF, "Unknown If command", s);
		if (If.equals("if")) {
			require("\\(", "If has to start with \"(\"", s);
			if (s.hasNext(COND)) {
				cond = parseCond(s);
			}
			require("\\)", "If has to end with a \")\"", s);
			block = parseBlock(s);
			while(s.hasNext(ELSEIF)){
				require(ELSEIF,  "Unknown elseIf command", s);
				require("\\(", "elseif has to start with \"(\"", s);
				if (s.hasNext(COND)) {
					elseifcond.add(parseCond(s));
				}
				require("\\)", "elseif has to end with a \")\"", s);
				elseifblock.add (parseBlock(s));
				
			}
			if (s.hasNext(ELSE)) {
				require(ELSE, "Unknown else command", s);

				elseblock = parseBlock(s);
			}

		} else {
			fail("is not if", s);
		}

		return new IfNode(cond, block, elseblock,elseifcond,elseifblock);
	}

	private static WhileNode parseWhile(Scanner s) {
		BlockNode block = null;
		CondNode cond = null;
		String whileN = require(WHILE, "Unknown While command", s);
		if (whileN.equals("while")) {
			require("\\(", "while has to start with \"(\"", s);
			if (s.hasNext(COND)) {
				cond = parseCond(s);
			}
			require("\\)", "while has to end with a \")\"", s);
			block = parseBlock(s);

		} else {
			fail("is not while", s);
		}

		return new WhileNode(cond, block);
	}

	private static CondNode parseCond(Scanner s) {

		EXPNode exp1 = null;
		EXPNode exp2 = null;
		CondNode cond1 = null;
		CondNode cond2 = null;

		String Cond = require(COND, "Unknown Cond command", s);
		require("\\(", "Cond has to start with \"(\"", s);
		switch (Cond) {
		case ("and"):
		case ("or"):
		case ("not"):
			cond1 = parseCond(s);
			break;
		case ("lt"):
		case ("gt"):
		case ("eq"):
			exp1 = parseEXP(s);
			break;
		}
		if (Cond.equals("not")) {
			require("\\)", "Cond has to end with a \")\"", s);
			return new CondNode(Cond, exp1, exp2, cond1, cond2);
		}
		require("\\,", "Cond has a \",\" between", s);
		switch (Cond) {
		case ("and"):
		case ("or"):
			cond2 = parseCond(s);
			break;
		case ("lt"):
		case ("gt"):
		case ("eq"):
			exp2 = parseEXP(s);
			break;
		}
		require("\\)", "Cond has to end with a \")\"", s);

		return new CondNode(Cond, exp1, exp2, cond1, cond2);
	}

	private static NumNode parseNum(Scanner s) {
		int child = Integer.parseInt(s.next());
		return new NumNode(child);

	}

	private static SenNode parseSen(Scanner s) {

		String sen = require(SEN, "Unknown sen command", s);
		switch (sen) {
		case ("fuelLeft"):
			return new FuelLeftNode();

		case ("oppLR"):
			return new OppLRNode();

		case ("oppFB"):
			return new OppFBNode();

		case ("numBarrels"):
			return new NumBarrels();

		case ("barrelLR"):
			if (s.hasNext("\\(")) {
				require("\\(", "EXP has to start with \"(\"", s);
				SenNode barrelLR = new BarrelLRNode(parseEXP(s));
				require("\\)", "EXP has to end with a \")\"", s);
				return barrelLR;
			} else {
				return new BarrelLRNode();
			}
			
		case ("barrelFB"):
			if (s.hasNext("\\(")) {
				require("\\(", "EXP has to start with \"(\"", s);
				SenNode barrelFB = new BarrelFBNode(parseEXP(s));
				require("\\)", "EXP has to end with a \")\"", s);
				return barrelFB;
			} else {
				return new BarrelFBNode();
			}

		case ("wallDist"):
			return new WallDistNode();

		default:
			fail("Unknown Sen command", s);
			return null;
		// dead code
		}
	}

	private static LoopNode parseLoop(Scanner s) {
		BlockNode child = null;
		String command = require(LOOP, "Unknown LOOP command", s);
		if (command.equals("loop")) {
			child = parseBlock(s);
		}

		return new LoopNode(child);
	}

	private static BlockNode parseBlock(Scanner s) {

		require("\\{", "BLOCK has to start with \"{\"", s);

		BlockNode blockNode = new BlockNode();

		while (s.hasNext() && !s.hasNext("}")) {
			blockNode.addStmtNode(parseStmtNode(s));
		}
		/*
		 * if (blockNode.isEmpty()) { fail("BLOCK cannot be empty", s); }
		 */
		require("\\}", "BLOCK has to end with a \"}\"", s);

		return blockNode;
	}

	private static ActNode parseAction(Scanner s) {
		RobotProgramNode child = null;

		String action = require(ACTIONS, "Unknown Action command1", s);
		switch (action) {
		case ("move"):
			if (s.hasNext("\\(")) {
				require("\\(", "EXP has to start with \"(\"", s);

				child = new MoveNode(parseEXP(s));

				require("\\)", "EXP has to end with a \")\"", s);
			} else {
				child = new MoveNode();
			}
			break;

		case ("turnL"):
			child = new turnLNode();
			break;
		case ("turnR"):
			child = new turnRNode();
			break;
		case ("takeFuel"):
			child = new takeFNode();
			break;
		case ("wait"):
			if (s.hasNext("\\(")) {
				require("\\(", "EXP has to start with \"(\"", s);

				child = new waitNode(parseEXP(s));

				require("\\)", "EXP has to end with a \")\"", s);
			} else {
				child = new waitNode();
			}
			break;
		case ("shieldOff"):
			child = new ShieldoffNode();
			break;
		case ("shieldOn"):
			child = new ShieldOnNode();
			break;
		case ("turnAround"):
			child = new TurnAroundNode();
			break;
		default:
			fail("Unknown Action command2", s);
			// dead code
			break;

		}

		require(";", "All Actions has to end with a \";\"", s);
		// System.out.println(child);
		return new ActNode(child);
	}

	private static EXPNode parseEXP(Scanner s) {

		if (s.hasNext(SEN)) {
			return parseSen(s);
		} else if (s.hasNext(NUM)) {
			return parseNum(s);
		} else if (s.hasNext(OP)) {
			return parseOP(s);
		} else if (s.hasNext(VAR)) {
            // VAR
            String varName = require(VAR, "Invalid variable name", s);
            VARNode var = variables.get(varName);
            if (var == null) {
                // fail("This variable \"" + varName + "\" has not been initialised", s);
                var = new VARNode(varName, 0);
                variables.put(varName, var);
            }
            return var;}
		else {
			fail("can't identify ExpNode", s);
			return null;
		}
	}

	private static OpNode parseOP(Scanner s) {
		String symbol = require(OP, "Unknown OP command", s);
		require("\\(", "OP has to start with \"(\"", s);
		EXPNode exp1 = parseEXP(s);
		require("\\,", "OP has , between EXP1 adn EXP2", s);
		EXPNode exp2 = parseEXP(s);
		require("\\)", "OP has to end with a \")\"", s);

		return new OpNode(symbol, exp1, exp2);
	}

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public
// (or private)
