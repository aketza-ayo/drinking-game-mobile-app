package com.devapp.drinkinggame.easteregg

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.devapp.drinkinggame.easteregg.states.GameStateManager

class MurcielagoGame : ApplicationAdapter() {

    companion object {
        const val WIDTH = 480
        const val HEIGHT = 800
        const val TITLE = "Murcielago"
    }

    private lateinit var gameStateManager: GameStateManager
    private lateinit var batch: SpriteBatch

    private lateinit var music: Music

    override fun create() {
        batch = SpriteBatch()
        gameStateManager = GameStateManager()
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
        music.isLooping = true
        music.volume = 0.1F
        music.play()
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)

    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)  //wipes the screen and redraw the screen from fresh each interation

        gameStateManager.update(Gdx.graphics.deltaTime)
        gameStateManager.render(batch)
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        music.dispose()
    }
}