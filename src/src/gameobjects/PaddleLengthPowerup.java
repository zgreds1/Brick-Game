package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class PaddleLengthPowerup extends GameObject {
    private static final int MAX_PADDLE_WIDTH = 350;
    private static final int MIN_PADDLE_WIDTH = 50;
    private final GameObjectCollection gameObjectCollection;
    private float growRatio;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public PaddleLengthPowerup(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                               GameObjectCollection gameObjectCollection, float growRatio) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.growRatio = growRatio;

    }

    /**
     * Overrides shouldCollideWith method to only collide with instance of paddle.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle;
    }

    /**
     * Overrides method onCollisionEnter to change paddle length upon collision with it. Limits how big/small the paddle
     * can get.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // validate that paddle isn't too big or too small
        if ((other.getDimensions().x() > MAX_PADDLE_WIDTH && growRatio > 1) ||
                (other.getDimensions().x() < MIN_PADDLE_WIDTH && growRatio < 1)){
            growRatio = 1;
        }
        other.setDimensions(other.getDimensions().multX(growRatio));
        gameObjectCollection.removeGameObject(this);
    }
}
