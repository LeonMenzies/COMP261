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
	@Override
	public void execute(Robot robot) {
		robot.move();
	}

	public String toString() {
		return "move;";
	}
}

class WaitNode implements RobotProgramNode {
	@Override
	public void execute(Robot robot) {
		robot.idleWait();
	}

	public String toString() {
		return "wait;";
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

	@Override
	public boolean evaluate(Robot robot) {
//		if (rpne.evaluate(robot) < val) {
//			return true;
//		} else {
//			return false;
//		}
		return true;

	}

	public String toString() {
		return "lt";
	}
}

class GreaterThanNode implements RobotProgramNodeEvaluateBoolean {

	@Override
	public boolean evaluate(Robot robot) {

//		if (rpne.evaluate(robot) > val) {
//			return true;
//		} else {
//			return false;
//		}

		return true;
	}

	public String toString() {
		return "gt";
	}
}

class EqualNode implements RobotProgramNodeEvaluateBoolean {

	@Override
	public boolean evaluate(Robot robot) {
//		if (rpne.evaluate(robot) == val) {
//			return true;
//		} else {
//			return false;
//		}

		return true;
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

	@Override
	public int evaluate(Robot robot) {
		return robot.getClosestBarrelLR();
	}

	public String toString() {
		return "barrelLR";
	}
}

class BarrelFBNode implements RobotProgramNodeEvaluateInt {

	@Override
	public int evaluate(Robot robot) {
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
