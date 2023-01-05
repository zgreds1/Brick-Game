package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

public class BrickStrategyFactory {
    public static final int NUMBER_OF_UNIQUE_STRATEGIES = 4;
    private final GameObjectCollection gameObjectCollection;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;

    /**
     * Factory to choose a brick strategy
     * @param gameObjectCollection game object collection
     * @param gameManager game manager
     * @param imageReader image reader
     * @param soundReader sound reader
     * @param inputListener input listener
     * @param windowController window controller
     * @param windowDimensions window dimensions
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                BrickerGameManager gameManager,
                                danogl.gui.ImageReader imageReader,
                                danogl.gui.SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Calls helper to get a strategy
     * @return Strategy to apply to brick
     */
    public CollisionStrategy getStrategy() {
        RemoveBrickStrategy removeBrickStrategy = new RemoveBrickStrategy(this.gameObjectCollection);
        return getStrategyHelper(removeBrickStrategy, NUMBER_OF_UNIQUE_STRATEGIES + 2);

    }

    /**
     * Randomly chooses a strategy to apply to brick. If a double strategy was chosen, recursively calls itself to
     * choose two new strategies. If one of those is again a double strategy, we repeat but now without the option
     * to choose another double strategy.
     * @param toBeDecorated CollisionStrategy to decorate, could include multiple
     * @param bound upper limit of number of strategies to choose from
     * @return strategy picked
     */
    private CollisionStrategy getStrategyHelper(CollisionStrategy toBeDecorated,int bound){
        Random rand = new Random();

        int strategyToReturn = rand.nextInt(bound);
        if (strategyToReturn == 0){
            return new AddPaddleStrategy(toBeDecorated, imageReader, inputListener,windowDimensions);
        }
        if (strategyToReturn == 1){
            return new PaddleLengthStrategy(toBeDecorated, imageReader);
        }
        if (strategyToReturn == 2){
            return new PuckStrategy(toBeDecorated, imageReader, soundReader);
        }
        if (strategyToReturn == 3){
            return new ChangeCameraStrategy(toBeDecorated, windowController, gameManager);
        }
        if (strategyToReturn == 4){
            return getStrategyHelper(getStrategyHelper(toBeDecorated, NUMBER_OF_UNIQUE_STRATEGIES), bound -1);

        }
        return toBeDecorated;
    }

}
