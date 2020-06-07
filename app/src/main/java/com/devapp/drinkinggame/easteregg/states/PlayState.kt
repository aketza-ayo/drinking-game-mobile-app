package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.devapp.drinkinggame.easteregg.MurcielagoGame
import com.devapp.drinkinggame.easteregg.sprites.Murcielago
import com.devapp.drinkinggame.easteregg.sprites.Tube

import kotlin.collections.ArrayList

class PlayState: State {

    companion object {
        const val TUBE_SPACING = 125
        const val TUBE_TOTAL_COUNT = 4
        const val GROUND_Y_OFFSET = -50F
    }

    private var murcielago: Murcielago
    private var background: Texture
    private var ground: Texture
    private lateinit var groundPos1: Vector2
    private lateinit var groundPos2: Vector2

    private lateinit var tubes: ArrayList<Tube>

    constructor(gameStateManager: GameStateManager) : super(gameStateManager){
        this.background = Texture("bg.png")
        this.ground = Texture("ground.png")

        this.murcielago = Murcielago(5F,150F)
        camera.setToOrtho(false, MurcielagoGame.WIDTH.toFloat() / 2, MurcielagoGame.HEIGHT.toFloat() / 2)

        groundPos1 = Vector2(camera.position.x - (camera.viewportWidth / 2), GROUND_Y_OFFSET)
        groundPos2 = Vector2((camera.position.x - camera.viewportWidth / 2) + ground.width , GROUND_Y_OFFSET)

        tubes = ArrayList<Tube>()
        var i = 0
        while(i < TUBE_TOTAL_COUNT){
            i++
            tubes.add(Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)))
        }
    }

    override fun handleInput() {
        if(Gdx.input.justTouched()){
            murcielago.jump()
        }
    }

    override fun update(deltaTime: Float) {
        handleInput()
        updateGround()
        murcielago.update(deltaTime)

        camera.position.x = murcielago.position.x + 80

        var i = 0
        for(tube in tubes){
            if(camera.position.x - (camera.viewportWidth / 2) > tube.posTopTube.x + tube.topTube.width){
                tube.reposition(tube.posTopTube.x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_TOTAL_COUNT))
            }
//            Gdx.app.log("#INFO", "murcielago="+murcielago.bounds);
//            Gdx.app.log("#INFO", "tube_" + i + "=" + tube.posTopTube.x + " " + tube.posTopTube.y );
            if(tube.collides(murcielago.bounds)){
                gameStateManager.set(PlayState(gameStateManager))
            }
            i++
        }

        if(murcielago.position.y <= ground.height + GROUND_Y_OFFSET){
            gameStateManager.set(PlayState(gameStateManager))
        }
        camera.update()
     }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(background, camera.position.x - (camera.viewportWidth / 2), 0F)
        spriteBatch.draw(murcielago.murcielagoAnimation.getFrame(), murcielago.position.x, murcielago.position.y)

        for(tube in tubes){
            spriteBatch.draw(tube.topTube, tube.posTopTube.x, tube.posTopTube.y )
            spriteBatch.draw(tube.bottomTube, tube.posBottomTube.x, tube.posBottomTube.y )
        }
        spriteBatch.draw(ground, groundPos1.x, groundPos1.y)
        spriteBatch.draw(ground, groundPos2.x, groundPos2.y)

        spriteBatch.end()

    }

    override fun dispose() {
        background.dispose()
        murcielago.dispose()
        ground.dispose()
        for(tube in tubes){
            tube.dispose()
        }
        System.out.println("Play state disposed")
    }

    private fun updateGround(){
        if(camera.position.x - (camera.viewportWidth / 2) > groundPos1.x + ground.width ){
            groundPos1.add(ground.width * 2F, 0F)
        }
        if(camera.position.x - (camera.viewportWidth / 2) > groundPos2.x + ground.width ){
            groundPos2.add(ground.width * 2F, 0F)
        }
    }


}