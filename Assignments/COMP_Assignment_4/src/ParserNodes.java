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
	final RobotProgramNode BLOCK1;
	final RobotProgramNode BLOCK2;
	final RobotProgramNodeEvaluateBoolean COND;

	public IfNode(RobotProgramNodeEvaluateBoolean cond, RobotProgramNode ifBlock, RobotProgramNode elseIfBlock) {
		BLOCK1 = ifBlock;
		BLOCK2 = elseIfBlock;
		COND = cond;

	}

	@Override
	public void execute(Robot robot) {
		if (COND.evaluate(robot) == true) {
			BLOCK1.execute(robot);
		} else {
			BLOCK2.execute(robot);
		}
	}

	public String toString() {

		if (BLOCK2 == null) {
			return "if(" + COND + ")" + BLOCK1;

		} else {
			return "if(" + COND + ")" + BLOCK1 + "else" + BLOCK2;
		}

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

		return "while(" + COND + ")" + BLOCK;
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

class ExpNode implements RobotProgramNodeEvaluateInt {
	final RobotProgramNodeEvaluateInt VALUE;

	public ExpNode(RobotProgramNodeEvaluateInt v) {
		this.VALUE = v;
	}

	@Override
	public int evaluate(Robot robot) {
		return VALUE.evaluate(robot);
	}

	public String toString() {

		return VALUE + "";
	}

}

class OpNode implements RobotProgramNodeEvaluateInt {
	final RobotProgramNodeEvaluateInt VALUE;

	public OpNode(RobotProgramNodeEvaluateInt v) {
		this.VALUE = v;
	}

	@Override
	public int evaluate(Robot robot) {
		return VALUE.evaluate(robot);
	}

	public String toString() {

		return VALUE + "";
	}

}

class RelopNode implements RobotProgramNodeEvaluateBoolean {
	final RobotProgramNodeEvaluateBoolean VALUE;

	public RelopNode(RobotProgramNodeEvaluateBoolean v) {
		this.VALUE = v;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return VALUE.evaluate(robot);
	}

	public String toString() {

		return VALUE + "";
	}

}
