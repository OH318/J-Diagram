public class Car {

	private int weight;
	private int speed;
	private boolean engineOn;
	private static boolean sideMirrorOn;
	private static boolean lightOn;
	private EngineType engineType;
	public static final int DIRECTION = 7;

	public void startEngine();

	public void stopEngine();

	public void sideMirrorTurnOn();

	public void sideMirrorTurnOff();

	public int currentSpeed();

	public void accelerate();

	public void brake();

	public void turnRight();

	public void turnLeft();
}