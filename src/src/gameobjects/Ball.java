package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCount;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCount = 0;
    }

    /**
     * Overrides onCollisionEnter to make ball make sound and change speed upon impact.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionCount++;
        super.onCollisionEnter(other, collision);
        collisionSound.play();
        setVelocity(getVelocity().flipped(collision.getNormal()));
    }

    /**
     * Getter for collisionCount
     * @return collision count of ball
     */
    int getCollisionCount(){
        return collisionCount;
    }
}
