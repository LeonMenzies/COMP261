import java.util.ArrayList;
import java.util.List;

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
	public RobotProgramNode getIFBLOCK() {
		return IFBLOCK;
	}

	public RobotProgramNodeEvaluateBoolean getCOND() {
		return COND;
	}

	final RobotProgramNode IFBLOCK;
	final List<IfNode> ELSEIFBLOCK;
	final RobotProgramNode ELSEBLOCK;
	final RobotProgramNodeEvaluateBoolean COND;

	public IfNode(RobotProgramNodeEvaluateBoolean cond, RobotProgramNode ifBlock, List<IfNode> elseIfBlock,
			RobotProgramNode elseBlock) {
		IFBLOCK = ifBlock;
		ELSEIFBLOCK = elseIfBlock;
		ELSEBLOCK = elseBlock;
		COND = cond;

	}

	@Override
	public void execute(Robot robot) {
		if (COND.evaluate(robot) == true) {
			IFBLOCK.execute(robot);
			return;
		}

		for (IfNode rpn : ELSEIFBLOCK) {
			if (rpn.getCOND().evaluate(robot) == true) {
				rpn.getIFBLOCK().execute(robot);
				return;
			}
		}

		ELSEBLOCK.execute(robot);

	}

	public String toString() {

		String toReturn = "if(" + COND + ")" + IFBLOCK;

		if (ELSEIFBLOCK != null) {

			for (IfNode in : ELSEIFBLOCK) {
				toReturn += "elif" + in;
			}
		}

		if (ELSEBLOCK != null) {
			toReturn += "else" + ELSEBLOCK;
		}

		return toReturn;

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

		return RELOP + "";
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

		return SEN + "";
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

		return VALUE + "";
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

class VarNode implements RobotProgramNodeEvaluateInt {
	final String NAME;
	final RobotProgramNodeEvaluateInt VALUE;

	public VarNode(String name, RobotProgramNodeEvaluateInt val) {
		NAME = name;
		VALUE = val;
	}

	@Override
	public int evaluate(Robot robot) {
		return VALUE.evaluate(robot);
	}

	public String toString() {

		return NAME + "=" + VALUE;
	}
}

class AssignNode implements RobotProgramNode {

	RobotProgramNodeEvaluateInt VAR;

	public AssignNode(RobotProgramNodeEvaluateInt child) {
		VAR = child;
	}

	@Override
	public void execute(Robot robot) {
		VAR.evaluate(robot);

	}

	public String toString() {

		return "" + VAR;
	}
}
