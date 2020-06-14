package com.devapp.drinkinggame.easteregg.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Murcielago {

    companion object {
        const val GRAVITY = -15F
        const val MOVEMENT = 100F
    }

    var GROUND_LIMIT = 0F

    var position: Vector2
    var velocity: Vector2

    var bounds: Rectangle
    var texture: Texture
    var murcielagoAnimation: Animation
    var sound: Sound

    constructor(x: Float, y: Float){
        this.position = Vector2(x, y)
        this.velocity = Vector2(0F, 0F)

        texture = Texture("murcielagoanimation2.png")
        murcielagoAnimation = Animation(TextureRegion(texture), 3, 0.5F)
        bounds = Rectangle(x, y, texture.width.toFloat() / 3, texture.height.toFloat())
        sound = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"))
    }

    public fun update(deltaTime: Float){
        murcielagoAnimation.update(deltaTime)
        if(position.y > 0){
            velocity.add(0F, GRAVITY)
        }

        velocity.scl(deltaTime)
        position.add(MOVEMENT * deltaTime, velocity.y)

        if(position.y <= GROUND_LIMIT){
            position.y = GROUND_LIMIT
        }

        velocity.scl(1/deltaTime)
        bounds.setPosition(position.x, position.y)
    }

    public fun reposition(x: Float, y: Float){
        position.set(x, y)
    }

    public fun jump(){
        velocity.y = 250F
        sound.play(0.5F)
    }

    public fun dispose(){
        texture.dispose()
        sound.dispose()
    }

}