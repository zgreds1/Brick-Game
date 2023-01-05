package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.PaddleLengthPowerup;

import java.util.Random;

public class PaddleLengthStrategy extends RemoveBrickStrategyDecorator{
    private static final float GROW_RATIO = 1.2f;
    private static final float SHRINK_RATIO = 0.8f;
    private static final int FALLING_SPEED = 200;
    private static final String BUFF_NARROW_PNG = "assets/buffNarrow.png";
    private static final String BUFF_WIDEN_PNG = "assets/buffWiden.png";
    private final ImageReader imageReader;

    /**
     * Add PaddleLengthPowerup to game on collision and delegate to held CollisionStrategy.
     * @param toBeDecorated Held CollisionStrategy
     * @param imageReader ImageReader
     */
    public PaddleLengthStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader) {
        super(toBeDecorated);
        this.imageReader = imageReader;
    }

    /**
     * Create widen/narrow powerup and have it fall towards bottom.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Random rand = new Random();
        String imagePath = BUFF_NARROW_PNG;
        float growRatio = SHRINK_RATIO;
        // widen happens 1/4
        if (rand.nextBoolean() && rand.nextBoolean()){
            imagePath = BUFF_WIDEN_PNG;
            growRatio = GROW_RATIO;
        }
        Renderable statusImage = imageReader.readImage(imagePath, false);
        PaddleLengthPowerup statusDefinition = new PaddleLengthPowerup(thisObj.getTopLeftCorner(),
                thisObj.getDimensions(),
                statusImage,
                getGameObjectCollection(), growRatio);
        statusDefinition.setVelocity(new Vector2(0, FALLING_SPEED));

        getGameObjectCollection().addGameObject(statusDefinition);

    }
}
