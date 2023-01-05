package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjectCollection;

    /**
     * Creates instance of CollisionStrategy
     * @param gameObjectCollection objects in game to be able to edit
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }


    /**
     * Removes object from game upon collision.
     * @param thisObj object that got hit
     * @param otherObj object that hit
     * @param counter counter to decrease upon object removal
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if (gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            counter.decrement();
        }
    }

    /**
     * Getter for gameObjectCollection
     * @return gameObjectCollection
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }

}
