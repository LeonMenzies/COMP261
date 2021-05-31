import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	// Map for variables
	static Map<String, RobotProgramNodeEvaluateInt> variables = new HashMap<>();

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

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

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
	// static Pattern NUMPAT = Pattern.compile("-?\\d+");
	static final Pattern NUMPAT = Pattern.compile("-?(0|[1-9][0-9]*)");
	static final Pattern VARPATTERN = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");
	static final Pattern OPENPAREN = Pattern.compile("\\(");
	static final Pattern CLOSEPAREN = Pattern.compile("\\)");
	static final Pattern OPENBRACE = Pattern.compile("\\{");
	static final Pattern CLOSEBRACE = Pattern.compile("\\}");
	static final Pattern COMMAPATTERN = Pattern.compile(",");
	static final Pattern DOASSIGNPATTERN = Pattern.compile("=");
	static final Pattern SEMICOLONPATTERN = Pattern.compile(";");

	// Non-terminal patterns
	static final Pattern ACTPATTERN = Pattern.compile("move|turnL|turnR|takeFuel|wait|turnAround|shieldOn|shieldOff");
	static final Pattern STMTPATTERN = Pattern.compile(
			"move|turnL|turnR|takeFuel|wait|while|if|loop|turnAround|shieldOn|shieldOff|\\$[A-Za-z][A-Za-z0-9]*");
	static final Pattern LOOPPATTERN = Pattern.compile("loop");
	static final Pattern IFPATTERN = Pattern.compile("if");
	static final Pattern ELSEIFPATTERN = Pattern.compile("elif");
	static final Pattern ELSEPATTERN = Pattern.compile("else");
	static final Pattern WHILEPATTERN = Pattern.compile("while");
	static final Pattern RELOPPATTERN = Pattern.compile("lt|gt|eq");
	static final Pattern SENPATTERN = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static final Pattern OPERATIONPATTERN = Pattern.compile("add|sub|mul|div");
	static final Pattern EXPRESSIONPATTERN = Pattern
			.compile("add|sub|mul|div|-?(0|[1-9][0-9]*)|fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");

	// Compare patterns
	static final Pattern GTPATTERN = Pattern.compile("gt");
	static final Pattern LTPATTERN = Pattern.compile("lt");
	static final Pattern EQPATTERN = Pattern.compile("eq");

	// Operation patterns
	static final Pattern ADDPATTERN = Pattern.compile("add");
	static final Pattern SUBPATTERN = Pattern.compile("sub");
	static final Pattern MULPATTERN = Pattern.compile("mul");
	static final Pattern DIVPATTERN = Pattern.compile("div");

	// Condition patterns
	static final Pattern ANDPATTERN = Pattern.compile("and");
	static final Pattern ORPATTERN = Pattern.compile("or");
	static final Pattern NOTPATTERN = Pattern.compile("not");

	// action patterns
	static final Pattern TURNLPATTERN = Pattern.compile("turnL");
	static final Pattern TURNRPATTERN = Pattern.compile("turnR");
	static final Pattern MOVEPATTERN = Pattern.compile("move");
	static final Pattern TURNAROUNDPATTERN = Pattern.compile("turnAround");
	static final Pattern SHIELONPATTERN = Pattern.compile("shieldOn");
	static final Pattern SHIELOFFPATTERN = Pattern.compile("shieldOff");
	static final Pattern TAKEFUELPATTERN = Pattern.compile("takeFuel");
	static final Pattern WAITPATTERN = Pattern.compile("wait");

	static final Pattern FUELLEFTPATTERN = Pattern.compile("fuelLeft");
	static final Pattern OPPLRPATTERN = Pattern.compile("oppLR");
	static final Pattern OPPFBPATTERN = Pattern.compile("oppFB");
	static final Pattern NUMBARRELSPATTERN = Pattern.compile("numBarrels");
	static final Pattern BARRELLRPATTERN = Pattern.compile("barrelLR");
	static final Pattern BARRELFBPATTERN = Pattern.compile("barrelFB");
	static final Pattern WALLDISTPATTERN = Pattern.compile("wallDist");

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE

		if (!s.hasNext()) {
			fail("Empty expr", s);
		}

		ArrayList<RobotProgramNode> n = new ArrayList<>();

		while (s.hasNext()) {

			n.add(parseStatement(s));

		}

		return new ProgNode(n);
	}

	static RobotProgramNode parseStatement(Scanner s) {

		if (!s.hasNext()) {
			fail("Empty expr", s);
		}

		RobotProgramNode child = null;

		if (s.hasNext(ACTPATTERN)) {
			child = parseAction(s);
		} else if (checkFor(LOOPPATTERN, s)) {
			child = parseLoop(s);
		} else if (checkFor(IFPATTERN, s)) {
			child = parseIf(s);
		} else if (checkFor(WHILEPATTERN, s)) {
			child = parseWhile(s);
		} else if (s.hasNext(VARPATTERN)) {
			child = parseAssign(s);
		} else {
			fail("Invalid Statement", s);
		}

		return new StmtNode(child);
	}

	static RobotProgramNode parseAction(Scanner s) {

		if (!s.hasNext()) {
			fail("Empty expr", s);
		}

		RobotProgramNode child = null;

		if (checkFor(MOVEPATTERN, s)) {

			RobotProgramNodeEvaluateInt val = null;

			if (s.hasNext(OPENPAREN)) {
				s.next();

				val = parseExpNode(s);

				require(CLOSEPAREN, "Missing close peren", s);
			}

			child = new MoveNode(val);

		} else if (checkFor(TURNLPATTERN, s)) {
			child = new TurnLNode();

		} else if (checkFor(TURNRPATTERN, s)) {
			child = new TurnRNode();

		} else if (checkFor(TAKEFUELPATTERN, s)) {
			child = new TakeFuelNode();

		} else if (checkFor(WAITPATTERN, s)) {

			RobotProgramNodeEvaluateInt val = null;

			if (s.hasNext(OPENPAREN)) {
				s.next();

				val = parseExpNode(s);

				require(CLOSEPAREN, "Missing close peren", s);
			}

			child = new WaitNode(val);

		} else if (checkFor(TURNAROUNDPATTERN, s)) {
			child = new TurnArroundNode();

		} else if (checkFor(SHIELONPATTERN, s)) {
			child = new ShieldOnNode();

		} else if (checkFor(SHIELOFFPATTERN, s)) {
			child = new ShieldOffNode();

		} else {
			fail("Invalid action", s);
		}

		require(SEMICOLONPATTERN, "Invalid action", s);

		return new ActNode(child);
	}

	static RobotProgramNode parseLoop(Scanner s) {

		return new LoopNode(parseBlock(s));

	}

	static RobotProgramNode parseBlock(Scanner s) {

		ArrayList<RobotProgramNode> n = new ArrayList<>();

		require(OPENBRACE, "Missing open brace", s);

		if (!s.hasNext(STMTPATTERN)) {
			fail("Empty block", s);
		}

		while (s.hasNext(STMTPATTERN)) {
			n.add(parseStatement(s));
		}

		require(CLOSEBRACE, "Missing close brace", s);

		return new BlockNode(n);
	}

	static RobotProgramNode parseIf(Scanner s) {

		RobotProgramNodeEvaluateBoolean child = null;
		RobotProgramNode ifBlock = null;
		RobotProgramNode elseBlock = null;
		List<IfNode> elseIfBlock = new ArrayList<>();

		require(OPENPAREN, "Missing open paren", s);

		child = parseCond(s);

		require(CLOSEPAREN, "Missing close peren", s);

		ifBlock = parseBlock(s);

		while (s.hasNext(ELSEIFPATTERN)) {
			s.next();

			require(OPENPAREN, "Missing open paren", s);

			RobotProgramNodeEvaluateBoolean toAddCond = parseCond(s);

			require(CLOSEPAREN, "Missing close peren", s);

			RobotProgramNode toAddBlock = parseBlock(s);

			elseIfBlock.add(new IfNode(toAddCond, toAddBlock, null, null));

		}

		if (s.hasNext(ELSEPATTERN)) {
			s.next();

			elseBlock = parseBlock(s);

		}

		return new IfNode(child, ifBlock, elseIfBlock, elseBlock);
	}

	static RobotProgramNode parseWhile(Scanner s) {

		RobotProgramNodeEvaluateBoolean child = null;

		require(OPENPAREN, "Missing open paren", s);

		child = parseCond(s);

		require(CLOSEPAREN, "Missing close peren", s);

		return new WhileNode(child, parseBlock(s));
	}

	static RobotProgramNodeEvaluateBoolean parseCond(Scanner s) {

		RobotProgramNodeEvaluateBoolean cond = null;
		RobotProgramNodeEvaluateBoolean con1 = null;
		RobotProgramNodeEvaluateBoolean con2 = null;

		if (s.hasNext(RELOPPATTERN)) {
			return parseRelop(s);
		} else if (checkFor(ANDPATTERN, s)) {

			require(OPENPAREN, "Missing open peren", s);

			con1 = parseCond(s);

			require(COMMAPATTERN, "Missing Comma", s);

			con2 = parseCond(s);

			require(CLOSEPAREN, "Missing open peren", s);

			cond = new AndNode(con1, con2);

		} else if (checkFor(ORPATTERN, s)) {

			require(OPENPAREN, "Missing open peren", s);

			con1 = parseCond(s);

			require(COMMAPATTERN, "Missing Comma", s);

			con2 = parseCond(s);

			require(CLOSEPAREN, "Missing open peren", s);

			cond = new OrNode(con1, con2);

		} else if (checkFor(NOTPATTERN, s)) {

			require(OPENPAREN, "Missing open peren", s);

			con1 = parseCond(s);

			require(CLOSEPAREN, "Missing open peren", s);

			cond = new NotNode(con1);

		} else {
			fail("Invalid condition", s);
		}

		return new CondNode(cond);
	}

	static RobotProgramNodeEvaluateBoolean parseRelop(Scanner s) {

		RobotProgramNodeEvaluateBoolean relop = null;
		RobotProgramNodeEvaluateInt sen = null;
		RobotProgramNodeEvaluateInt num = null;

		if (checkFor(LTPATTERN, s)) {
			require(OPENPAREN, "Missing open paren", s);

			sen = parseExpNode(s);

			require(COMMAPATTERN, "Missing comma", s);

			num = parseExpNode(s);

			relop = new LessThanNode(sen, num);

		} else if (checkFor(GTPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			sen = parseExpNode(s);

			require(COMMAPATTERN, "Missing comma", s);

			num = parseExpNode(s);

			relop = new GreaterThanNode(sen, num);
		} else if (checkFor(EQPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			sen = parseExpNode(s);

			require(COMMAPATTERN, "Missing comma", s);

			num = parseExpNode(s);

			relop = new EqualNode(sen, num);
		} else {
			fail("Invalid condition", s);

		}

		require(CLOSEPAREN, "Missing close brace", s);

		return new RelopNode(relop);
	}

	static SenNode parseSen(Scanner s) {

		RobotProgramNodeEvaluateInt child = null;

		if (checkFor(FUELLEFTPATTERN, s)) {
			child = new FuelLeftNode();
		} else if (checkFor(OPPLRPATTERN, s)) {
			child = new OppLRNode();
		} else if (checkFor(OPPFBPATTERN, s)) {
			child = new OppFBNode();
		} else if (checkFor(NUMBARRELSPATTERN, s)) {
			child = new NumBarrelsNode();
		} else if (checkFor(BARRELLRPATTERN, s)) {

			RobotProgramNodeEvaluateInt val = null;

			if (s.hasNext(OPENPAREN)) {
				s.next();

				val = parseExpNode(s);

				require(CLOSEPAREN, "Missing close peren", s);
			}

			child = new BarrelLRNode(val);

		} else if (checkFor(BARRELFBPATTERN, s)) {

			RobotProgramNodeEvaluateInt val = null;

			if (s.hasNext(OPENPAREN)) {
				s.next();

				val = parseExpNode(s);

				require(CLOSEPAREN, "Missing close peren", s);
			}

			child = new BarrelFBNode(val);

		} else if (checkFor(WALLDISTPATTERN, s)) {
			child = new WallDistNode();
		} else {
			fail("Invalid sensor", s);
		}

		return new SenNode(child);
	}

	static NumNode parseNum(Scanner s) {
		return new NumNode(s.nextInt());
	}

	static RobotProgramNodeEvaluateInt parseExpNode(Scanner s) {

		RobotProgramNodeEvaluateInt child = null;

		if (s.hasNext(NUMPAT)) {
			child = parseNum(s);
		} else if (s.hasNext(SENPATTERN)) {
			child = parseSen(s);
		} else if (s.hasNext(VARPATTERN)) {

			String key = s.next();

			// Get value from map
			if (!variables.containsKey(key)) {

				// fail("Variable not assigned", s); // Part 4 check weather variables have been
				// assigned before use
				child = new NumNode(0);
			} else {
				child = variables.get(key);
			}

		} else if (s.hasNext(OPERATIONPATTERN)) {

			child = parseOp(s);
		} else {
			fail("Invalid expression", s);
		}

		return new ExpNode(child);
	}

	static RobotProgramNodeEvaluateInt parseOp(Scanner s) {

		RobotProgramNodeEvaluateInt child = null;

		if (checkFor(ADDPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			RobotProgramNodeEvaluateInt val1 = parseExpNode(s);
			require(COMMAPATTERN, "Missing comma", s);

			RobotProgramNodeEvaluateInt val2 = parseExpNode(s);
			require(CLOSEPAREN, "Missing close paren", s);

			child = new AddNode(val1, val2);
		} else if (checkFor(SUBPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			RobotProgramNodeEvaluateInt val1 = parseExpNode(s);
			require(COMMAPATTERN, "Missing comma", s);

			RobotProgramNodeEvaluateInt val2 = parseExpNode(s);
			require(CLOSEPAREN, "Missing close paren", s);

			child = new SubNode(val1, val2);
		} else if (checkFor(MULPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);
			RobotProgramNodeEvaluateInt val1 = parseExpNode(s);
			require(COMMAPATTERN, "Missing comma", s);

			RobotProgramNodeEvaluateInt val2 = parseExpNode(s);
			require(CLOSEPAREN, "Missing close paren", s);

			child = new MulNode(val1, val2);
		} else if (checkFor(DIVPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			RobotProgramNodeEvaluateInt val1 = parseExpNode(s);
			require(COMMAPATTERN, "Missing comma", s);

			RobotProgramNodeEvaluateInt val2 = parseExpNode(s);
			require(CLOSEPAREN, "Missing close paren", s);

			child = new DivNode(val1, val2);
		} else {
			fail("Incorrect opperation", s);
		}

		return new OpNode(child);
	}

	static RobotProgramNode parseAssign(Scanner s) {

		RobotProgramNodeEvaluateInt child = null;

		if (s.hasNext(VARPATTERN)) {

			String name = s.next();

			require(DOASSIGNPATTERN, "Missing = ", s);

			RobotProgramNodeEvaluateInt value = parseExpNode(s);

			require(SEMICOLONPATTERN, "missing semicolcon", s);

			child = new VarNode(name, value);

			variables.put(name, value);

		} else {
			fail("Invalid var name", s);
		}

		return new AssignNode(child);

	}

	// utility methods for the parser

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
	 * Requires that the next token matches a pattern if it matches, it consumes and
	 * returns the token, if not, it throws an exception with an error message
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
	 * Checks whether the next token in the scanner matches the specified pattern,
	 * if so, consumes the token and return true. Otherwise returns false without
	 * consuming anything.
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
