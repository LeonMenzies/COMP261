import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

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
	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern COMMAPATTERN = Pattern.compile(",");

	// Non-terminal patterns
	static final Pattern ACTPATTERN = Pattern.compile("move|turnL|turnR|takeFuel|wait|turnAround|shieldOn|shieldOff");
	static final Pattern STMTPATTERN = Pattern
			.compile("move|turnL|turnR|takeFuel|wait|while|if|loop|turnAround|shieldOn|shieldOff");
	static final Pattern LOOPPATTERN = Pattern.compile("loop");
	static final Pattern IFPATTERN = Pattern.compile("if");
	static final Pattern WHILEPATTERN = Pattern.compile("while");
	static final Pattern RELOPPATTERN = Pattern.compile("lt|gt|eq");
	static final Pattern SENPATTERN = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");

	// Compare patterns
	static final Pattern GTPATTERN = Pattern.compile("gt");
	static final Pattern LTPATTERN = Pattern.compile("lt");
	static final Pattern EQPATTERN = Pattern.compile("eq");

	// action patterns
	static final Pattern SEMICOLONPATTERN = Pattern.compile(";");
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
			child = new MoveNode();

		} else if (checkFor(TURNLPATTERN, s)) {
			child = new TurnLNode();

		} else if (checkFor(TURNRPATTERN, s)) {
			child = new TurnRNode();

		} else if (checkFor(TAKEFUELPATTERN, s)) {
			child = new TakeFuelNode();

		} else if (checkFor(WAITPATTERN, s)) {
			child = new WaitNode();

		} else if (checkFor(TURNAROUNDPATTERN, s)) {
			child = new TurnArroundNode();

		} else if (checkFor(SHIELONPATTERN, s)) {
			child = new ShieldOnNode();

		} else if (checkFor(SHIELOFFPATTERN, s)) {
			child = new ShieldOffNode();

		} else {
			fail("Invalid action", s);
		}

		require(SEMICOLONPATTERN, "Missing semicolon", s);

		return new ActNode(child);
	}

	static RobotProgramNode parseLoop(Scanner s) {

		return new LoopNode(parseBlock(s));

	}

	static RobotProgramNode parseBlock(Scanner s) {

		ArrayList<RobotProgramNode> n = new ArrayList<>();

		require(OPENBRACE, "Missing open brace", s);

		if (!s.hasNext(STMTPATTERN)) {
			fail("Empty loop", s);
		}

		while (s.hasNext(STMTPATTERN)) {
			n.add(parseStatement(s));
		}

		require(CLOSEBRACE, "Missing close brace", s);

		return new BlockNode(n);
	}

	static RobotProgramNode parseIf(Scanner s) {

		RobotProgramNodeEvaluateBoolean child = null;

		require(OPENPAREN, "Missing open paren", s);

		child = parseCond(s);

		require(CLOSEPAREN, "Missing close peren", s);

		return new IfNode(child, parseBlock(s));
	}

	static RobotProgramNode parseWhile(Scanner s) {

		RobotProgramNodeEvaluateBoolean child = null;

		require(OPENPAREN, "Missing open paren", s);

		child = parseCond(s);

		require(CLOSEPAREN, "Missing close peren", s);

		return new WhileNode(child, parseBlock(s));
	}

	static RobotProgramNodeEvaluateBoolean parseCond(Scanner s) {

		RobotProgramNodeEvaluateBoolean relop = null;
		RobotProgramNodeEvaluateInt sen = null;
		RobotProgramNodeEvaluateInt num = null;

		if (checkFor(LTPATTERN, s)) {
			require(OPENPAREN, "Missing open paren", s);

			sen = parseSen(s);

			require(COMMAPATTERN, "Missing comma", s);

			if (!s.hasNext(NUMPAT)) {
				fail("Missing Num", s);
			}

			num = parseNum(s);

			relop = new LessThanNode(sen, num);

		} else if (checkFor(GTPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			sen = parseSen(s);

			require(COMMAPATTERN, "Missing comma", s);

			if (!s.hasNext(NUMPAT)) {
				fail("Missing Num", s);
			}

			num = parseNum(s);

			relop = new GreaterThanNode(sen, num);
		} else if (checkFor(EQPATTERN, s)) {

			require(OPENPAREN, "Missing open paren", s);

			sen = parseSen(s);

			require(COMMAPATTERN, "Missing comma", s);

			if (!s.hasNext(NUMPAT)) {
				fail("Missing Num", s);
			}

			num = parseNum(s);

			relop = new EqualNode(sen, num);
		} else {
			fail("Invalid condition", s);

		}

		require(CLOSEPAREN, "Missing close brace", s);

		return new CondNode(relop);
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
			child = new BarrelLRNode();
		} else if (checkFor(BARRELFBPATTERN, s)) {
			child = new BarrelFBNode();
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
