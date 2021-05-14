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

class TakeFuleNode implements RobotProgramNode {
	@Override
	public void execute(Robot robot) {
		robot.takeFuel();
	}

	public String toString() {
		return "takeFuel;";
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

class LessThanNode implements RobotProgramNode {

	RobotProgramNode lessThan;

	@Override
	public void execute(Robot robot) {

	}

	public String toString() {
		return "lt;";
	}
}

class GreaterThan implements RobotProgramNode {

	RobotProgramNode greaterThan;

	@Override
	public void execute(Robot robot) {

	}

	public String toString() {
		return "gt;";
	}
}

class EqualNode implements RobotProgramNode {

	RobotProgramNode equal;

	@Override
	public void execute(Robot robot) {

	}

	public String toString() {
		return "eq;";
	}
}

class FuelLeftNode implements RobotProgramNode {

	int fuelLeft;

	@Override
	public void execute(Robot robot) {
		fuelLeft = robot.getFuel();
	}

	public String toString() {
		return "fuelLeft;";
	}
}

class OppLRNode implements RobotProgramNode {

	int oppLR;

	@Override
	public void execute(Robot robot) {
		oppLR = robot.getOpponentLR();
	}

	public String toString() {
		return "oppLR;";
	}
}

class OppFBNode implements RobotProgramNode {

	int oppFB;

	@Override
	public void execute(Robot robot) {
		oppFB = robot.getOpponentFB();
	}

	public String toString() {
		return "oppFB;";
	}
}

class NumBarrelsNode implements RobotProgramNode {

	int numBarrels;

	@Override
	public void execute(Robot robot) {
		numBarrels = robot.numBarrels();
	}

	public String toString() {
		return "numBarrels;";
	}
}

class BarrelLRNode implements RobotProgramNode {

	int barrelLR;

	@Override
	public void execute(Robot robot) {
		barrelLR = robot.getClosestBarrelLR();
	}

	public String toString() {
		return "barrelLR;";
	}
}

class BarrelFBNode implements RobotProgramNode {

	int barrelFB;

	@Override
	public void execute(Robot robot) {
		barrelFB = robot.getClosestBarrelFB();
	}

	public String toString() {
		return "barrelFb;";
	}
}

class WallDistNode implements RobotProgramNode {

	int wallDist;

	@Override
	public void execute(Robot robot) {
		wallDist = robot.getDistanceToWall();
	}

	public String toString() {
		return "wallDist;";
	}
}
