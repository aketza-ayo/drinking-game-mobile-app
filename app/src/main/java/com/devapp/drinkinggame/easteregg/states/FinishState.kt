package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.devapp.drinkinggame.easteregg.MurcielagoGame
import com.devapp.drinkinggame.easteregg.sprites.Murcielago


class FinishState: State {

    private var background: Texture
    private var playButton: Texture
    private var playButtonBounds: Rectangle
    private var murcielago: Murcielago

    private var scoreFont: BitmapFont
    private var scoreFontShadow: BitmapFont

    private var font: BitmapFont
    private var fontShadow: BitmapFont
    private val glyphLayout: GlyphLayout
    private val displayText: String

    private val score: Int
    private var highScore = 0
    private val preferences = Gdx.app.getPreferences("murcielago_game")

    constructor(gameStateManager: GameStateManager, text: String, score: Int) : super(gameStateManager) {
        camera.setToOrtho(false, MurcielagoGame.WIDTH.toFloat() / 2, MurcielagoGame.HEIGHT.toFloat() / 2)

        this.background = Texture("bg.png")

        //load and get highscore from file
        this.score = score

        this.highScore = preferences.getInteger("highscore", 0)
        if(this.score > this.highScore){
            preferences.putInteger("highscore", this.score)
            preferences.flush()
            this.highScore = this.score
        }

        this.murcielago = Murcielago(0F,150F)
        this.murcielago.velocity =  Vector2(0F, 0F)
        this.murcielago.GROUND_LIMIT = 100F

        this.displayText = text

        glyphLayout = GlyphLayout()
        fontShadow = BitmapFont(Gdx.files.internal("shadow.fnt"))
        fontShadow.data.setScale(.35F, .35F)
        font = BitmapFont(Gdx.files.internal("text.fnt"))
        font.data.setScale(.35F, .35F)

        scoreFontShadow = BitmapFont(Gdx.files.internal("shadow.fnt"))
        scoreFontShadow.data.setScale(.25F, .25F)
        scoreFont = BitmapFont(Gdx.files.internal("text.fnt"))
        scoreFont.data.setScale(.25F, .25F)

        this.playButton = Texture("playButton.png")
        playButtonBounds = Rectangle(camera.position.x - playButton.width / 2, 5F,
            playButton.width.toFloat() , playButton.height.toFloat())

    }

    override fun handleInput() {

        if(Gdx.input.justTouched()){

            if(playButtonBounds.contains(getMousePosInGameWorld()!!.x,getMousePosInGameWorld()!!.y)){
                gameStateManager.set(PlayState(gameStateManager))
            }

            murcielago.jump()
        }
    }

    override fun update(deltaTime: Float) {


        handleInput()
        murcielago.update(deltaTime)

        if(murcielago.position.x + murcielago.texture.width > camera.viewportWidth + camera.position.x) {
            murcielago.reposition(0F, murcielago.position.y)
        }

    }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(background, 0F, 0F)

        spriteBatch.draw(murcielago.murcielagoAnimation.getFrame(), murcielago.position.x, murcielago.position.y)

        drawScores(spriteBatch)

        spriteBatch.draw(playButton, camera.position.x - playButton.width / 2, 5F)

        spriteBatch.end()
    }

    private fun drawScores(spriteBatch: SpriteBatch){

        /**
         * Game Completed or Game Over
         */
        glyphLayout.setText(font,displayText)
        fontShadow.draw(spriteBatch, displayText, camera.position.x - (glyphLayout.width / 2), 370F )
        font.draw(spriteBatch, displayText, camera.position.x - (glyphLayout.width / 2), 370F )

        /**
         * High Score
         */
        glyphLayout.setText(font,"High Score\n$highScore")
        scoreFontShadow.draw(spriteBatch,
            "High Score\n$highScore", 50F, 300F )
        scoreFont.draw(spriteBatch, "High Score\n$highScore", 50F, 300F )

        /**
         * Score
         */
        glyphLayout.setText(font,"Score\n$score")
        scoreFontShadow.draw(spriteBatch, "Score\n$score", 100F, 200F )
        scoreFont.draw(spriteBatch,"Score\n$score", 100F, 200F )


        /**
         * Play Again
         */
        glyphLayout.setText(font,"Play Again")
        fontShadow.draw(spriteBatch, "Play Again", camera.position.x - (glyphLayout.width / 2), camera.position.y / 2 )
        font.draw(spriteBatch, "Play Again", camera.position.x - (glyphLayout.width / 2), camera.position.y / 2 )
    }

    private fun getMousePosInGameWorld(): Vector3? {
        return camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0F))
    }

    override fun dispose() {
        background.dispose()
        System.out.println("Finish state disposed")
    }

}