package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

public class BallCollisionCountdownAgent extends GameObject {
    int initialCount;
    private final ChangeCameraStrategy owner;
    private final int countDownValue;
    private final Ball ball;

    /**
     * An object of this class is instantiated on collision of ball with a brick with a change camera strategy.
     * It checks ball's collision counter every frame, and once it finds the ball has collided countDownValue times
     * since instantiation, it calls the strategy to reset the camera to normal.
     * @param ball Ball object whose collisions are to be counted.
     * @param owner Object asking for countdown notification.
     * @param countDownValue Number of ball collisions. Notify caller object that the ball collided countDownValue
     *                      times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue){
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ball = ball;

        initialCount = ball.getCollisionCount();
        this.owner = owner;
        this.countDownValue = countDownValue;
    }

    /**
     * Overrides update method to turn off camera if ball had coundDownValue collisions since object was created
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() - initialCount - 1 == countDownValue){
            owner.turnOffCameraChange();
        }
    }
}
