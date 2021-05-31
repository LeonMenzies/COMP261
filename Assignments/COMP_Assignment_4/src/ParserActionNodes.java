class TurnLNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.turnLeft();
	}

	public String toString() {
		return "turnL;";
	}
}

class TurnRNode implements RobotProgramNode {
	@Override
	public void execute(Robot robot) {
		robot.turnRight();
	}

	public String toString() {
		return "turnR;";
	}
}

class MoveNode implements RobotProgramNode {
	RobotProgramNodeEvaluateInt value;

	MoveNode(RobotProgramNodeEvaluateInt i) {
		value = i;
	}

	@Override
	public void execute(Robot robot) {
		if (value == null) {
			robot.move();
		} else {

			for (int i = 0; i < value.evaluate(robot); i++) {
				robot.move();
			}
		}

	}

	public String toString() {

		if (value == null) {
			return "move;";
		} else {
			return "move(" + value + ");";
		}

	}
}

class WaitNode implements RobotProgramNode {

	RobotProgramNodeEvaluateInt value;

	WaitNode(RobotProgramNodeEvaluateInt i) {
		value = i;
	}

	@Override
	public void execute(Robot robot) {
		if (value == null) {
			robot.move();
		} else {

			for (int i = 0; i < value.evaluate(robot); i++) {
				robot.idleWait();
			}
		}
	}

	public String toString() {
		if (value == null) {
			return "wait;";
		} else {
			return "wait(" + value + ");";
		}

	}
}

class ShieldOnNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.setShield(true);
	}

	public String toString() {
		return "shieldOn;";
	}
}

class ShieldOffNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.setShield(false);
	}

	public String toString() {
		return "shieldOff;";
	}
}

class TakeFuelNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.takeFuel();
	}

	public String toString() {
		return "takeFuel;";
	}
}

class LessThanNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateInt left;
	RobotProgramNodeEvaluateInt right;

	public LessThanNode(RobotProgramNodeEvaluateInt l, RobotProgramNodeEvaluateInt r) {
		this.left = l;
		this.right = r;
	}

	@Override
	public boolean evaluate(Robot robot) {
		if (left.evaluate(robot) < right.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "lt(" + left + ", " + right + ")";
	}
}

class GreaterThanNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateInt left;
	RobotProgramNodeEvaluateInt right;

	public GreaterThanNode(RobotProgramNodeEvaluateInt l, RobotProgramNodeEvaluateInt r) {
		this.left = l;
		this.right = r;
	}

	@Override
	public boolean evaluate(Robot robot) {
		if (left.evaluate(robot) > right.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "gt(" + left + ", " + right + ")";
	}
}

class EqualNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateInt left;
	RobotProgramNodeEvaluateInt right;

	public EqualNode(RobotProgramNodeEvaluateInt l, RobotProgramNodeEvaluateInt r) {
		this.left = l;
		this.right = r;
	}

	@Override
	public boolean evaluate(Robot robot) {
		if (left.evaluate(robot) == right.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "eq(" + left + ", " + right + ")";
	}
}

class FuelLeftNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
		return robot.getFuel();
	}

	public String toString() {
		return "fuelLeft";
	}
}

class OppLRNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
		return robot.getOpponentLR();
	}

	public String toString() {
		return "oppLR";
	}
}

class OppFBNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
		return robot.getOpponentFB();
	}

	public String toString() {
		return "oppFB";
	}
}

class NumBarrelsNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
		return robot.numBarrels();
	}

	public String toString() {
		return "numBarrels";
	}
}

class BarrelLRNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val;

	public BarrelLRNode(RobotProgramNodeEvaluateInt v) {
		val = v;
	}

	@Override
	public int evaluate(Robot robot) {

		if (val != null) {
			robot.getBarrelLR(val.evaluate(robot));
		}
		return robot.getClosestBarrelLR();
	}

	public String toString() {
		return "barrelLR";
	}
}

class BarrelFBNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val;

	public BarrelFBNode(RobotProgramNodeEvaluateInt v) {
		val = v;
	}

	@Override
	public int evaluate(Robot robot) {

		if (val != null) {
			robot.getBarrelFB(val.evaluate(robot));
		}
		return robot.getClosestBarrelFB();
	}

	public String toString() {
		return "barrelFb";
	}
}

class WallDistNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
		return robot.getDistanceToWall();
	}

	public String toString() {
		return "wallDist";
	}

}

class TurnArroundNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		robot.turnAround();
	}

	public String toString() {
		return "turnAround;";
	}
}

class AddNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val1;
	RobotProgramNodeEvaluateInt val2;

	public AddNode(RobotProgramNodeEvaluateInt v1, RobotProgramNodeEvaluateInt v2) {
		val1 = v1;
		val2 = v2;
	}

	@Override
	public int evaluate(Robot robot) {

		return val1.evaluate(robot) + val2.evaluate(robot);
	}

	public String toString() {
		return "add(" + val1 + "+" + val2 + ")";
	}
}

class SubNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val1;
	RobotProgramNodeEvaluateInt val2;

	public SubNode(RobotProgramNodeEvaluateInt v1, RobotProgramNodeEvaluateInt v2) {
		val1 = v1;
		val2 = v2;
	}

	@Override
	public int evaluate(Robot robot) {

		return val1.evaluate(robot) - val2.evaluate(robot);
	}

	public String toString() {
		return "sub(" + val1 + "-" + val2 + ")";
	}
}

class MulNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val1;
	RobotProgramNodeEvaluateInt val2;

	public MulNode(RobotProgramNodeEvaluateInt v1, RobotProgramNodeEvaluateInt v2) {
		val1 = v1;
		val2 = v2;
	}

	@Override
	public int evaluate(Robot robot) {

		return val1.evaluate(robot) * val2.evaluate(robot);
	}

	public String toString() {
		return "mul(" + val1 + "*" + val2 + ")";
	}
}

class DivNode implements RobotProgramNodeEvaluateInt {

	RobotProgramNodeEvaluateInt val1;
	RobotProgramNodeEvaluateInt val2;

	public DivNode(RobotProgramNodeEvaluateInt v1, RobotProgramNodeEvaluateInt v2) {
		val1 = v1;
		val2 = v2;
	}

	@Override
	public int evaluate(Robot robot) {

		return val1.evaluate(robot) / val2.evaluate(robot);
	}

	public String toString() {
		return "div(" + val1 + "/" + val2 + ")";
	}
}

class AndNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateBoolean con1;
	RobotProgramNodeEvaluateBoolean con2;

	public AndNode(RobotProgramNodeEvaluateBoolean c1, RobotProgramNodeEvaluateBoolean c2) {
		con1 = c1;
		con2 = c2;
	}

	@Override
	public boolean evaluate(Robot robot) {
		if (con1.evaluate(robot) && con2.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "and(" + con1 + "," + con2 + ")";
	}

}

class OrNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateBoolean con1;
	RobotProgramNodeEvaluateBoolean con2;

	public OrNode(RobotProgramNodeEvaluateBoolean c1, RobotProgramNodeEvaluateBoolean c2) {
		con1 = c1;
		con2 = c2;
	}

	@Override
	public boolean evaluate(Robot robot) {
		if (con1.evaluate(robot) || con2.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "or(" + con1 + "," + con2 + ")";
	}
}

class NotNode implements RobotProgramNodeEvaluateBoolean {

	RobotProgramNodeEvaluateBoolean con1;

	public NotNode(RobotProgramNodeEvaluateBoolean c1) {
		con1 = c1;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return !con1.evaluate(robot);
	}

	public String toString() {
		return "not(" + con1 + ")";
	}
}
