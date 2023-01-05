package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private final GameObject[] livesArr;
    private int numOfLives;
    private static final int HEART_DISTANCE_FROM_HEART = 2;

    /**
     *
     * @param widgetTopLeftCorner - top left corner of left most life widgets. Other widgets will be displayed to its
     *                           right, aligned in height.
     * @param widgetDimensions - dimensions of widgets to be displayed.
     * @param livesCounter - global lives counter of game.
     * @param widgetRenderable - image to use for widgets.
     * @param gameObjectsCollection - global game object collection managed by game manager.
     * @param numOfLives - global setting of number of lives a player will have in a game.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        livesArr = new GameObject[numOfLives];
        this.numOfLives = numOfLives;
        for (int i = 0; i < numOfLives; i++) {
            float XPlacement = widgetTopLeftCorner.x() + i * (widgetDimensions.x() + HEART_DISTANCE_FROM_HEART);
            GameObject life = new GameObject(new Vector2(XPlacement, widgetTopLeftCorner.y()),
                    new Vector2(widgetDimensions.x(), widgetDimensions.y()), widgetRenderable);
            gameObjectsCollection.addGameObject(life, Layer.BACKGROUND);
            livesArr[i] = life;
        }
    }

    /**
     * Overrides update and deletes a displayed heart if a life has been lost.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives){
            numOfLives--;
            gameObjectsCollection.removeGameObject(livesArr[numOfLives], Layer.BACKGROUND);
        }
    }
}
