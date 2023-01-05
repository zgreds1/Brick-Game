package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {
    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private final WindowController windowController;
    private final BrickerGameManager gameManager;
    private BallCollisionCountdownAgent ballCollisionCountdownAgent;

    /**
     * Object that changes camera angle.
     * @param toBeDecorated CollisionStrategy to hold
     * @param windowController window controller
     * @param gameManager game manager
     */
    ChangeCameraStrategy(CollisionStrategy toBeDecorated,
                         WindowController windowController,
                         BrickerGameManager gameManager){
        super(toBeDecorated);
                this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * Change the camera if it's not already changed and creates countdown until camera gets reverted.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (gameManager.getCamera() != null)
        {
            return;
        }
        if (otherObj instanceof Ball && !(otherObj instanceof Puck)) {
            gameManager.setCamera(
                    new Camera(
                            otherObj,            //object to follow
                            Vector2.ZERO,    //follow the center of the object
                            windowController.getWindowDimensions().mult(1.2f),  //widen the frame a bit
                            windowController.getWindowDimensions()   //share the window dimensions
                    )
            );
            ballCollisionCountdownAgent = new BallCollisionCountdownAgent(
                    (Ball) otherObj, this, NUM_BALL_COLLISIONS_TO_TURN_OFF);
            getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
        }

    }

    /**
     * Revert camera to default and remove Countdown object
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(ballCollisionCountdownAgent);

    }
}
