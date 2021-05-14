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

	// Non-terminal patterns
	static final Pattern ACTPATTERN = Pattern.compile("move|turnL|turnR|takeFuel|wait");
	static final Pattern LOOPPATTERN = Pattern.compile("loop");
	static final Pattern IFPATTERN = Pattern.compile("if");
	static final Pattern WHILEPATTERN = Pattern.compile("while");
	static final Pattern CONDPATTERN = Pattern.compile("lt|gt|eq");

	// Compare patterns
	static final Pattern GTPATTERN = Pattern.compile("gt");
	static final Pattern LTPATTERN = Pattern.compile("lt");
	static final Pattern EQPATTERN = Pattern.compile("eq");

	// action patterns
	static final Pattern SEMICOLONPATTERN = Pattern.compile(";");
	static final Pattern TURNLPATTERN = Pattern.compile("turnL");
	static final Pattern TURNRPATTERN = Pattern.compile("turnR");
	static final Pattern MOVEPATTERN = Pattern.compile("move");
	static final Pattern WAITPATTERN = Pattern.compile("wait");
	static final Pattern TAKEFUELPATTERN = Pattern.compile("takeFuel");

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

		return child;
	}

	static RobotProgramNode parseAction(Scanner s) {

		if (!s.hasNext()) {
			fail("Empty expr", s);
		}

		RobotProgramNode child = null;

		if (checkFor(MOVEPATTERN, s)) {
			child = new ActNode(new MoveNode());

		} else if (checkFor(TURNLPATTERN, s)) {
			child = new ActNode(new TurnLNode());

		} else if (checkFor(TURNRPATTERN, s)) {
			child = new ActNode(new TurnRNode());

		} else if (checkFor(TAKEFUELPATTERN, s)) {
			child = new ActNode(new TakeFuleNode());

		} else if (checkFor(WAITPATTERN, s)) {
			child = new ActNode(new WaitNode());
		} else {
			fail("Invalid action", s);
		}

		require(SEMICOLONPATTERN, "Missing semicolon", s);

		return child;
	}

	static RobotProgramNode parseLoop(Scanner s) {

		return new LoopNode(parseBlock(s));

	}

	static RobotProgramNode parseBlock(Scanner s) {

		ArrayList<RobotProgramNode> n = new ArrayList<>();

		require(OPENBRACE, "Missing open brace", s);
		if (!s.hasNext(ACTPATTERN)) {
			fail("Empty loop", s);
		}

		while (s.hasNext(ACTPATTERN)) {
			n.add(parseStatement(s));
		}

		require(CLOSEBRACE, "Missing close brace", s);

		return new BlockNode(n);
	}

	static RobotProgramNode parseIf(Scanner s) {

		RobotProgramNode child = null;

		require(OPENPAREN, "Missing open paren", s);

		if (!s.hasNext(CONDPATTERN)) {
			fail("Empty loop", s);
		}

		while (s.hasNext(ACTPATTERN)) {
			n.add(parseStatement(s));
		}

		require(CLOSEBRACE, "Missing close brace", s);

		return new BlockNode(n);
	}

	static RobotProgramNode parseWhile(Scanner s) {

		RobotProgramNode child = null;

		require(OPENPAREN, "Missing open paren", s);
		if (!s.hasNext(ACTPATTERN)) {
			fail("Empty loop", s);
		}

		while (s.hasNext(ACTPATTERN)) {
			n.add(parseStatement(s));
		}

		require(CLOSEBRACE, "Missing close brace", s);

		return new BlockNode(n);
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
