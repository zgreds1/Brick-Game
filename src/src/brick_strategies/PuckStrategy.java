package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;


public class PuckStrategy extends RemoveBrickStrategyDecorator{
    private static final int EXTRA_PUCKS = 3;
    private static final float PUCK_SPEED = 200;
    private static final int PUCK_SIZE = 20;
    private static final String MOCK_BALL_PNG = "assets/mockBall.png";
    private static final String AUDIO_PATH = "assets/Bubble5_4.wav";
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    /**
     * Concrete class extending abstract RemoveBrickStrategyDecorator.
     * Introduces several pucks instead of brick once removed.
     * @param toBeDecorated CollisionStrategy to hold
     * @param imageReader Image reader
     * @param soundReader Sound reader
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * Add pucks to game on collision and delegate to held CollisionStrategy.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);

        Renderable puckImage = imageReader.readImage(MOCK_BALL_PNG, true);
        Sound collisionSound = soundReader.readSound(AUDIO_PATH);
        for (int i = 0; i < EXTRA_PUCKS; i++) {
            Puck puck = new Puck(Vector2.ZERO, new Vector2(PUCK_SIZE, PUCK_SIZE), puckImage, collisionSound);
            puck.setCenter(thisObj.getCenter());
            Vector2 speed = new Vector2(PUCK_SPEED, PUCK_SPEED);
            Random rand = new Random();
            speed = speed.rotated(rand.nextInt());

            puck.setVelocity(speed);
            getGameObjectCollection().addGameObject(puck);
        }


    }


}
