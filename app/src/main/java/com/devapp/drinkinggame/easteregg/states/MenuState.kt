package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.devapp.drinkinggame.easteregg.MurcielagoGame

class MenuState : State {

    private var background: Texture
    private var playButton: Texture

    constructor(gameStateManager: GameStateManager) : super(gameStateManager) {
        camera.setToOrtho(false, MurcielagoGame.WIDTH.toFloat() / 2, MurcielagoGame.HEIGHT.toFloat() / 2)

        this.background = Texture("bg.png")
        this.playButton = Texture("playButton.png")
    }

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            gameStateManager.set(PlayState(gameStateManager))
        }
    }

    override fun update(deltaTime: Float) {
        handleInput()
    }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(background, 0F, 0F)
        spriteBatch.draw(playButton, camera.position.x - playButton.width / 2, camera.position.y)
        spriteBatch.end()

    }

    override fun dispose() {
        background.dispose()
        playButton.dispose()
        System.out.println("Menu state disposed")

    }
}