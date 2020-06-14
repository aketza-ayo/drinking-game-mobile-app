package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.devapp.drinkinggame.easteregg.MurcielagoGame
import com.devapp.drinkinggame.easteregg.sprites.Coin
import com.devapp.drinkinggame.easteregg.sprites.Flask
import com.devapp.drinkinggame.easteregg.sprites.Murcielago
import com.devapp.drinkinggame.easteregg.sprites.Tube
import java.util.*
import kotlin.collections.ArrayList


class PlayState: State {

    companion object {
        const val TUBE_SPACING = 125
        const val COIN_SPACING = 177F
        const val TUBE_TOTAL_COUNT = 4
        const val COIN_TOTAL_COUNT = 4
        const val GROUND_Y_OFFSET = 0F //-50F
    }

    private var murcielago: Murcielago
    private var background: Texture


    private var tubes: ArrayList<Tube>
    private var coins: ArrayList<Coin>
    private var flask: Flask

    private var highScore = 0
    private var score = 0
    private var isScored = false
    private var coinCounter = 0
    private var healthCounter = 0
    private var font: BitmapFont
    private var fontShadow: BitmapFont
    private val glyphLayout: GlyphLayout
    private var soundCoin: Sound
    private var soundHealth: Sound

    private var health = 0F // 0 = dead, 1 = full health
    private var blankHealth: Texture

    var random: Random = Random()

    constructor(gameStateManager: GameStateManager) : super(gameStateManager){
        this.background = Texture("bg.png")
        this.soundCoin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"))
        this.soundHealth = Gdx.audio.newSound(Gdx.files.internal("healthUp.wav"))

        camera.setToOrtho(false, MurcielagoGame.WIDTH.toFloat() / 2, MurcielagoGame.HEIGHT.toFloat() / 2)

        this.highScore = 0
        this.score = 0
        this.coinCounter = 0
        this.healthCounter = 0
        this.health = 0F

        blankHealth = Texture("blank.png")
        /**
         * Position player
         */
        this.murcielago = Murcielago(5F,150F)

        /**
         * Position Tubes
         */
        tubes = ArrayList<Tube>()
        var i = 0
        var index = 0
        while(i < TUBE_TOTAL_COUNT){
            index = i++
            tubes.add(Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH), index))
        }

        /**
         * Position Coins
         */
        coins = ArrayList<Coin>()
        var j = 0
        while(j < COIN_TOTAL_COUNT){
            coins.add(Coin(j * COIN_SPACING + 100 , if(j % 2 == 0) 100F else 300F))
            // places the coin in the middle of the gap of the two tubes
//            coins.add(Coin(tubes[j].posTopTube.x + 15 ,  tubes[j].posTopTube.y - 50))
            j++
        }

        /**
         * Position Flask
         */
        flask = Flask(50F,100F)

        /**
         * Position data texts
         */
        glyphLayout = GlyphLayout()
        fontShadow = BitmapFont(Gdx.files.internal("shadow.fnt"))
        fontShadow.data.setScale(.50F, .50F)
        font = BitmapFont(Gdx.files.internal("text.fnt"))
        font.data.setScale(.50F, .50F)

    }

    override fun handleInput() {
        if(Gdx.input.justTouched()){
            murcielago.jump()
        }
    }

    override fun update(deltaTime: Float) {
        handleInput()
//        updateGround()
        murcielago.update(deltaTime)
        for(coin in coins){
            coin.update(deltaTime)
        }

        camera.position.x = murcielago.position.x + 80

        for(tube in tubes){
            tube.isHidden = false
            if(camera.position.x - (camera.viewportWidth / 2) > tube.posTopTube.x + tube.topTube.width){
//                flask.isCollected = false

                tube.reposition(tube.posTopTube.x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_TOTAL_COUNT))
                isScored = false
            }

            if(tube.collides(murcielago.bounds)){
                //check for highscore
//                checkHighScore()
                gameStateManager.set(PlayState(gameStateManager))
            }

            if(!isScored && tube.posTopTube.x + (tube.topTube.width / 2)  < murcielago.bounds.x + murcielago.bounds.width ){
                incrementScore(1)
                isScored = true
            }
        }

        var j = 0
        for(coin in coins){
            if(camera.position.x - (camera.viewportWidth / 2) > coin.position.x ){
                coin.reposition(coin.position.x +  (COIN_SPACING * COIN_TOTAL_COUNT), coin.position.y)
                if(j == 3){
                    if(random.nextBoolean()){
                        flask.reposition(coins[0].position.x , coins[1].position.y)
                    }else{
                        flask.reposition(coins[1].position.x , coins[0].position.y)
                    }
                    flask.isCollected = false
                }

                // repositions the coing in between the tube gap
//                coin.reposition(tubes[j].posTopTube.x + 15 , tubes[j].posTopTube.y - 50)
                coin.isCollected = false
            }

            if(!coin.isCollected && coin.collides(murcielago.bounds)){
                collectCoin(1)
                coin.isCollected = true
            }
            j++
        }

        if(!flask.isCollected && flask.collides(murcielago.bounds)){
            collectFlask(1)
            flask.isCollected = true
        }

        if(murcielago.position.y <= GROUND_Y_OFFSET){
            gameStateManager.set(FinishState(gameStateManager, "Game Over", coinCounter))
        }

        if(health >= 1.0){
            gameStateManager.set(FinishState(gameStateManager, "Game Completed", coinCounter))
        }

        camera.update()
     }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = camera.combined

        spriteBatch.begin()

        spriteBatch.draw(background, camera.position.x - (camera.viewportWidth / 2), 0F)
        spriteBatch.draw(murcielago.murcielagoAnimation.getFrame(), murcielago.position.x, murcielago.position.y)

        for(tube in tubes){

            spriteBatch.draw(tube.topTube, tube.posTopTube.x, tube.posTopTube.y, tube.topTube.width.toFloat(), tube.topTube.height.toFloat())
            spriteBatch.draw(tube.bottomTube, tube.posBottomTube.x, tube.posBottomTube.y )

            if(tube.posTopTube.x + (tube.topTube.width / 2)  < murcielago.bounds.x + murcielago.bounds.width ){
                spriteBatch.draw(tube.getTopTexture(health), tube.posTopTube.x, tube.posTopTube.y, tube.topTube.width.toFloat(), tube.topTube.height.toFloat())
                spriteBatch.draw(tube.getBottomTexture(health), tube.posBottomTube.x, tube.posBottomTube.y )
//                tubes[(tube.index + 1) % TUBE_TOTAL_COUNT].isHidden = true
            }
        }

        for(coin in coins){
            if(!coin.isCollected){
                spriteBatch.draw(coin.coinAnimation.getFrame(), coin.position.x, coin.position.y)
            }
        }

        if(!flask.isCollected){
            spriteBatch.draw(flask.texture, flask.position.x, flask.position.y)
        }

        spriteBatch.color = Color.YELLOW
        spriteBatch.draw(blankHealth, murcielago.position.x - 40, 0F,
            (MurcielagoGame.WIDTH.toFloat() / 2) * health, 5F)
        spriteBatch.color = Color.WHITE

        glyphLayout.setText(font,coinCounter.toString())
        fontShadow.draw(spriteBatch, coinCounter.toString(), camera.position.x - glyphLayout.width, 40F)
        font.draw(spriteBatch, coinCounter.toString(), camera.position.x - glyphLayout.width, 40F )

        glyphLayout.setText(font,score.toString())
        fontShadow.draw(spriteBatch, score.toString(), camera.position.x - (glyphLayout.width / 2), (MurcielagoGame.HEIGHT.toFloat() / 2) - 30F )
        font.draw(spriteBatch, score.toString(), camera.position.x - (glyphLayout.width / 2), (MurcielagoGame.HEIGHT.toFloat() / 2) - 30F )

        spriteBatch.end()

    }

    override fun dispose() {
        background.dispose()
        murcielago.dispose()
        font.dispose()
        fontShadow.dispose()
        blankHealth.dispose()
        soundCoin.dispose()
        soundHealth.dispose()
        flask.dispose()
        for(coin in coins){
            coin.dispose()
        }
        for(tube in tubes){
            tube.dispose()
        }

        System.out.println("Play state disposed")
    }

    private fun incrementScore(increment: Int){
        score += increment

    }

    private fun collectCoin(increment: Int){
        coinCounter += increment
        soundCoin.play(0.1F)
    }

    private fun collectFlask(increment: Int){
        healthCounter += increment
        health += 0.2F
        soundHealth.play(0.5F)
        System.out.println("healthCounter=$healthCounter")
    }

    private fun checkHighScore(){
        val preferences = Gdx.app.getPreferences("murcielago_game")
        this.highScore = preferences.getInteger("highscore", 0)
        if(this.score > highScore){
            preferences.putInteger("highscore", this.score)
            preferences.flush()
        }
    }
}