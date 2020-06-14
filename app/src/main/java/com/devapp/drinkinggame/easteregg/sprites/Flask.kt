package com.devapp.drinkinggame.easteregg.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Flask {
    var position: Vector2
    var texture: Texture
    var bounds: Rectangle
    var isCollected = false

    constructor(x: Float, y: Float){
        this.position = Vector2(x, y)
        texture = Texture("flask_yellow.png")

        bounds = Rectangle(x, y, texture.width.toFloat(), texture.height.toFloat())
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