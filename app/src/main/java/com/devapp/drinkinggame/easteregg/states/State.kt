package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

public abstract class State {
    protected lateinit var camera: OrthographicCamera
    protected lateinit var mouse: Vector2
    protected lateinit var gameStateManager: GameStateManager

    constructor(gameStateManager: GameStateManager) {
        this.gameStateManager = gameStateManager
        this.camera = OrthographicCamera()
        this.mouse = Vector2()
    }

    protected abstract fun handleInput()
    public abstract fun update(deltaTime: Float)
    public abstract fun render(spriteBatch: SpriteBatch)
    public abstract fun dispose()
}