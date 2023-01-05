package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class MockPaddle extends Paddle{
    public static boolean isInstantiated;
    private final GameObjectCollection gameObjectCollection;
    private final int numCollisionsToDisappear;
    private int collisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object. Can be null, in which case
     * @param inputListener       Get input from user which way to move paddle
     * @param windowDimensions    dimensions of game window.
     * @param minDistanceFromEdge border for paddle movement.
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
               Vector2 windowDimensions, GameObjectCollection gameObjectCollection, int minDistanceFromEdge,
               int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        isInstantiated = true;
    }

    /**
     * Overrides onCollisionEnter to increment the MockPaddle collision counter and remove the object if the counter
     * reaches numCollisionsToDisappear.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
        if (collisionCounter == numCollisionsToDisappear){
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }
}
