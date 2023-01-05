package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class RemoveBrickStrategyDecorator implements CollisionStrategy{
    private final CollisionStrategy toBeDecorated;

    /**
     * Collision strategy decorator so we can apply the onCollison of the decorated.
     * @param toBeDecorated CollisionStrategy to be decorated
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated){
        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Overrides onCollision method to apply onCollision method of decorated strategy.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        toBeDecorated.onCollision(thisObj, otherObj, counter);
    }

    /**
     * Getter for gameObjectCollection
     * @return decorated gameObjectCollection
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return toBeDecorated.getGameObjectCollection();
    }


}
