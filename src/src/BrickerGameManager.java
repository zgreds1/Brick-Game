package src;

import src.brick_strategies.BrickStrategyFactory;
import src.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


import java.util.Random;

public class BrickerGameManager extends GameManager{

    public static final int BORDER_WIDTH = 10;

    private static final int FRAME_RATE = 80;

    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;

    private static final int BRICK_HEIGHT = 15;
    private static final int NUMBER_OF_ROWS = 5;
    private static final int BRICKS_PER_ROW = 8;
    private static final int BRICK_DISTANCE_FROM_SIDE_BORDERS = 5;
    private static final int BRICK_DISTANCE_FROM_TOP_BORDER = 50;
    private static final int BRICK_DISTANCE_FROM_BRICK = 1;

    private static final int BALL_SPEED = 250;
    private static final int BALL_RADIUS = 20;

    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_PLACEMENT_FROM_BOTTOM = 30;


    private static final int HEART_WIDTH = 25;
    private static final int HEART_HEIGHT = 20;
    private static final int HEART_DISTANCE_FROM_COUNTER = 5;

    private static final int LIFE_COUNTER_DISTANCE_FROM_BORDER = 5;
    private static final int LIFE_COUNTER_WIDTH = 25;
    private static final int LIFE_COUNTER_HEIGHT = 20;

    private static final int STARTING_LIVES = 4;

    private static final String HEART_PNG = "assets/heart.png";
    private static final String BACKGROUND_PNG = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_PNG = "assets/brick.png";
    private static final String BALL_PNG = "assets/ball.png";
    private static final String BLOP_WAV = "assets/blop_cut_silenced.wav";

    private static final String LOSS_PROMPT = "You lose!";
    private static final String WIN_PROMPT = "You win!";
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String WINDOW_TITLE = "Bricker";

    private Ball ball;

    private danogl.util.Counter remainingLivesCounter;
    private danogl.util.Counter brickCounter;

    private Vector2 windowDimensions;
    private WindowController windowController;

    private BrickStrategyFactory brickStrategyFactory;

    public BrickerGameManager(String windowTitle, danogl.util.Vector2 windowDimensionsString) {
        super(windowTitle, windowDimensionsString);

    }

    /**
     * Overrides initializeGame to create all the necessary objects for the game. (ball, paddle etc.)
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        windowController.setTargetFramerate(FRAME_RATE);
        windowDimensions = windowController.getWindowDimensions();
        remainingLivesCounter = new danogl.util.Counter(STARTING_LIVES);
        brickStrategyFactory = new BrickStrategyFactory(this.gameObjects(), this,
                imageReader,
                soundReader,
                inputListener,
                windowController,
                windowDimensions);

        createBall(imageReader, soundReader);
        createPaddle(imageReader, inputListener);
        createBorders();
        createBricks(imageReader);
        createBackground(imageReader);
        createNumericCounter();
        createGraphicLifeCounter(imageReader);
    }

    /**
     * Overrides update to check if game is over and update necessary fields.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        removeRedundantObjects();
        checkForGameEnd();
    }

    /**
     * Removes gameObjects which are below the screen (unless it's the ball)
     */
    private void removeRedundantObjects() {
        for (GameObject object:gameObjects())
        {
            if (object.getCenter().y() > windowDimensions.y())
            {
                if (!(object instanceof Ball) || object instanceof Puck) {
                    gameObjects().removeGameObject(object);
                }
            }
        }
    }


    /**
     * Creates an image of a heart per life left. Also saves them to an array for later deletion.
     * @param imageReader image reader
     */
    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_PNG, true);
        Vector2 firstHeartPlacement = new Vector2(BORDER_WIDTH + LIFE_COUNTER_DISTANCE_FROM_BORDER +
                LIFE_COUNTER_WIDTH + HEART_DISTANCE_FROM_COUNTER, windowDimensions.y() - HEART_HEIGHT);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(firstHeartPlacement,
                new Vector2(HEART_WIDTH, HEART_HEIGHT), remainingLivesCounter, heartImage, gameObjects(),
                STARTING_LIVES);
        this.gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);

    }


    /**
     * Creates a counter displaying how many remainingLives left.
     */
    private void createNumericCounter() {
        Vector2 counterPlacement = new Vector2(BORDER_WIDTH + LIFE_COUNTER_DISTANCE_FROM_BORDER,
                windowDimensions.y() - LIFE_COUNTER_HEIGHT);
        NumericLifeCounter numericLives = new NumericLifeCounter(remainingLivesCounter, counterPlacement,
                new Vector2(LIFE_COUNTER_WIDTH, LIFE_COUNTER_HEIGHT), gameObjects());
        this.gameObjects().addGameObject(numericLives, Layer.BACKGROUND);
    }

    /**
     * Creates the background
     * @param imageReader image reader
     */
    private void createBackground(ImageReader imageReader) {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                imageReader.readImage(BACKGROUND_PNG, false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }


    /**
     * Updates remainingLives left. If game is over, then displays a message asking user if they'd like to play again.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if(ballHeight > windowDimensions.y()) {
            remainingLivesCounter.decrement();
            if (remainingLivesCounter.value() > 0){ // lives remaining
                repositionBall(ball);
                return;
            }
            prompt = LOSS_PROMPT; // game lost
        }
        if (brickCounter.value() == 0){ // game won
            prompt = WIN_PROMPT;
        }

        if (!prompt.isEmpty()){
            prompt+= PLAY_AGAIN_PROMPT;
            if (windowController.openYesNoDialog(prompt)){
                MockPaddle.isInstantiated = false;
                windowController.resetGame();
            }
            else{
                windowController.closeWindow();
            }
        }
    }


    /**
     * Calculates size and creates bricks per constants.
     * @param imageReader image reader
     */
    private void createBricks(ImageReader imageReader) {
        brickCounter = new danogl.util.Counter(0);
        Renderable brickImage = imageReader.readImage(BRICK_PNG, false);
        float brickWidth = calcBrickWidth();
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {

            float YPlacement = BORDER_WIDTH + BRICK_DISTANCE_FROM_TOP_BORDER +
                    i * (BRICK_DISTANCE_FROM_BRICK + BRICK_HEIGHT);

            for (int j = 0; j < BRICKS_PER_ROW; j++) {

                float XPlacement = BORDER_WIDTH + BRICK_DISTANCE_FROM_SIDE_BORDERS +
                        j * (BRICK_DISTANCE_FROM_BRICK + brickWidth);

                GameObject brick = new Brick(
                        new Vector2(XPlacement, YPlacement), new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, this.brickStrategyFactory.getStrategy(), brickCounter);

                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increment();
            }
        }
    }

    /**
     * Calculates brick width based on constants.
     * @return brick width
     */
    private float calcBrickWidth() {
        return (windowDimensions.x() - 2*(BORDER_WIDTH + BRICK_DISTANCE_FROM_SIDE_BORDERS) -
                (BRICKS_PER_ROW - 1) * BRICK_DISTANCE_FROM_BRICK) / BRICKS_PER_ROW;
    }


    /**
     * Creates the ball and call the repositionBall function to put it in motion.
     * @param imageReader image reader
     * @param soundReader sounder reader
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_PNG, true);
        Sound collisionSound = soundReader.readSound(BLOP_WAV);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS,BALL_RADIUS), ballImage, collisionSound);
        this.gameObjects().addGameObject(ball);
        repositionBall(ball);
    }

    /**
     * Places the ball in the center and sets it in motion in a randomly selected diagonal direction.
     * @param ball ball to reposition
     */
    public void repositionBall(GameObject ball) {
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        if (rand.nextBoolean()){
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }


    /**
     * Creates paddle that reacts to user input from arrow keys.
     * @param imageReader image reader
     * @param inputListener gets input from user
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject Paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, BORDER_WIDTH);
        Paddle.setCenter(new Vector2(windowDimensions.x()/2,
                (int) windowDimensions.y() - PADDLE_PLACEMENT_FROM_BOTTOM));
        this.gameObjects().addGameObject(Paddle);
    }

    /**
     * Creates top/right/left borders.
     */
    private void createBorders() {
        // {right, left, top}
        int[] borderXPlacement = {0, (int) windowDimensions.x() - BORDER_WIDTH, 0};
        int[] borderWidths = {BORDER_WIDTH,  BORDER_WIDTH, (int) windowDimensions.x()};
        int[] borderHeights = {(int) windowDimensions.y(), (int) windowDimensions.y(), BORDER_WIDTH};

        for (int i = 0; i < borderHeights.length; i++) {
            gameObjects().addGameObject(new GameObject(new Vector2(borderXPlacement[i], 0),
                    new Vector2(borderWidths[i], borderHeights[i]),null));

        }
    }


    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }
}

