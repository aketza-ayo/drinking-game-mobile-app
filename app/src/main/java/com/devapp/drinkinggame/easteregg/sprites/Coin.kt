package com.devapp.drinkinggame.easteregg.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Coin {

    var position: Vector2
    var coinAnimation: Animation
    var texture: Texture
    var bounds: Rectangle
    var isCollected = false

    constructor(x: Float, y: Float){
        this.position = Vector2(x, y)
        texture = Texture("coinanimation_compact.png")
        coinAnimation = Animation(TextureRegion(texture), 4, 0.5F)

        bounds = Rectangle(x, y, texture.width.toFloat() / 4, texture.height.toFloat())

    }

    public fun update(deltaTime: Float){
        coinAnimation.update(deltaTime)
    }

    public fun reposition(x: Float, y: Float){
        position.set(x, y)
        bounds.setPosition(x, y)
    }


    public fun dispose(){
        texture.dispose()
    }


    public fun collides(player: Rectangle):Boolean{
        return !isCollected && player.overlaps(bounds)
    }

}