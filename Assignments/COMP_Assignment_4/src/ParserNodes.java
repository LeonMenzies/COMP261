import java.util.ArrayList;

class ProgNode implements RobotProgramNode {
	final ArrayList<RobotProgramNode> STMT;

	public ProgNode(ArrayList<RobotProgramNode> n) {
		STMT = n;
	}

	@Override
	public void execute(Robot robot) {
		for (RobotProgramNode rpn : STMT) {
			rpn.execute(robot);
		}
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("\n");

		for (RobotProgramNode r : STMT) {
			sb.append(r);
			sb.append("\n");
		}

		return sb.toString();
	}
}

class StmtNode implements RobotProgramNode {
	final RobotProgramNode ACT;

	public StmtNode(RobotProgramNode n) {
		ACT = n;
	}

	@Override
	public void execute(Robot robot) {
		ACT.execute(robot);
	}

	public String toString() {
		return ACT.toString();
	}
}

class ActNode implements RobotProgramNode {
	RobotProgramNode node;

	public ActNode(RobotProgramNode action) {
		node = action;
	}

	@Override
	public void execute(Robot robot) {
		node.execute(robot);
	}

	@Override
	public String toString() {

		return node.toString();
	}
}

class LoopNode implements RobotProgramNode {
	final RobotProgramNode BLOCK;

	public LoopNode(RobotProgramNode n) {
		BLOCK = n;
	}

	@Override
	public void execute(Robot robot) {
		while (true) {
			BLOCK.execute(robot);
		}
	}

	public String toString() {

		return "loop" + BLOCK;
	}
}

class BlockNode implements RobotProgramNode {
	final ArrayList<RobotProgramNode> STMT;

	public BlockNode(ArrayList<RobotProgramNode> n) {
		STMT = n;
	}

	@Override
	public void execute(Robot robot) {
		for (RobotProgramNode rpn : STMT) {
			rpn.execute(robot);
		}
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("{\n");

		for (RobotProgramNode r : STMT) {

			sb.append(r);
			sb.append("\n");
		}

		sb.append("}");

		return sb.toString();

	}
}

class IfNode implements RobotProgramNode {
	final RobotProgramNode BLOCK;
	final RobotProgramNodeEvaluateBoolean COND;

	public IfNode(RobotProgramNodeEvaluateBoolean cond, RobotProgramNode block) {
		BLOCK = block;
		COND = cond;

	}

	@Override
	public void execute(Robot robot) {
		if (COND.evaluate(robot) == true) {
			BLOCK.execute(robot);
		}
	}

	public String toString() {

		return "if" + COND + BLOCK;
	}
}

class WhileNode implements RobotProgramNode {
	final RobotProgramNode BLOCK;
	final RobotProgramNodeEvaluateBoolean COND;

	public WhileNode(RobotProgramNodeEvaluateBoolean cond, RobotProgramNode block) {
		BLOCK = block;
		COND = cond;

	}

	@Override
	public void execute(Robot robot) {
		System.out.println("gets");
		while (COND.evaluate(robot) == true) {
			BLOCK.execute(robot);
		}
	}

	public String toString() {

		return "while" + COND + BLOCK;
	}
}

class CondNode implements RobotProgramNodeEvaluateBoolean {
	final RobotProgramNodeEvaluateBoolean RELOP;

	public CondNode(RobotProgramNodeEvaluateBoolean relop) {
		RELOP = relop;
	}

	@Override
	public boolean evaluate(Robot robot) {

		return RELOP.evaluate(robot);

	}

	public String toString() {

		return "(" + RELOP + ")";
	}
}

class SenNode implements RobotProgramNodeEvaluateInt {
	final RobotProgramNodeEvaluateInt SEN;

	public SenNode(RobotProgramNodeEvaluateInt sensor) {
		SEN = sensor;
	}

	@Override
	public int evaluate(Robot robot) {

		return SEN.evaluate(robot);
	}

	public String toString() {

		return "(" + SEN;
	}

}

class NumNode implements RobotProgramNodeEvaluateInt {
	final int VALUE;

	public NumNode(int v) {
		this.VALUE = v;
	}

	@Override
	public int evaluate(Robot robot) {
		return VALUE;
	}

	public String toString() {

		return VALUE + ")";
	}

}