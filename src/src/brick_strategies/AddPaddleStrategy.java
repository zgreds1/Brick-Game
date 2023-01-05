package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

import static src.BrickerGameManager.BORDER_WIDTH;
import static src.gameobjects.MockPaddle.isInstantiated;

public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{

    private static final String PADDLE_PNG = "assets/paddle.png";
    private static final int ADDITIONAL_DISTANCE_FROM_EDGE = 2;
    private static final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE = 3;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private static final int MOCK_PADDLE_WIDTH = 100;
    private static final int MOCK_PADDLE_HEIGHT = 15;

    /**
     * Adds a paddle to game objects and removes it once it was hit by three objects.
     * @param toBeDecorated Collision Strategy held
     * @param imageReader Image reader
     * @param inputListener Input listener
     * @param windowDimensions Window dimensions
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                             ImageReader imageReader,
                             UserInputListener inputListener,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Add mockPaddle to gameObjects
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (!isInstantiated) {
            Renderable mockPaddleImage = imageReader.readImage(PADDLE_PNG, true);
            MockPaddle mockPaddle = new MockPaddle(windowDimensions.mult(0.5f),
                    new Vector2(MOCK_PADDLE_WIDTH, MOCK_PADDLE_HEIGHT),
                    mockPaddleImage,
                    inputListener,
                    windowDimensions,
                    getGameObjectCollection(),
                    BORDER_WIDTH + ADDITIONAL_DISTANCE_FROM_EDGE,
                    NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE);
            getGameObjectCollection().addGameObject(mockPaddle);

        }
    }
}
