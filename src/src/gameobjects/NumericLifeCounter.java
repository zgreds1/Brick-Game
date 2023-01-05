package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final TextRenderable renderableCounter;

    /**
     *
     * @param livesCounter  global lives counter of game.
     * @param topLeftCorner top left corner of renderable
     * @param dimensions dimensions of renderable
     * @param gameObjectCollection global game object collection
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;

        renderableCounter = new TextRenderable(Integer.toString(livesCounter.value()));
        renderableCounter.setColor(Color.RED);
        GameObject lifeCounter = new GameObject(topLeftCorner, dimensions, renderableCounter);
        gameObjectCollection.addGameObject(lifeCounter, Layer.BACKGROUND);
    }

    /**
     * Overrides update and sets renderableCounter to actual amount of lives left.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!Integer.toString(livesCounter.value()).equals(renderableCounter.renderedString())){
            renderableCounter.setString(Integer.toString(livesCounter.value()));
        }
    }
}
