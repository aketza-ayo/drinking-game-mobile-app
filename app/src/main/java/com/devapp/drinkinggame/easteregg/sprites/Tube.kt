package com.devapp.drinkinggame.easteregg.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import java.util.*

class Tube {
    companion object {
        const val FLUCTUATION = 130
        const val TUB_GAP = 100F
        const val LOWEST_OPENING = 100F
        const val TUBE_WIDTH = 52F
    }

    var topTube: Texture
    var bottomTube: Texture
    var posBottomTube: Vector2
    var posTopTube: Vector2

    var boundsTop: Rectangle
    var boundsBottom: Rectangle

    private var random: Random

    constructor(x: Float){
        this.topTube = Texture("toptube.png")
        this.bottomTube = Texture("bottomtube.png")
        random = Random()

        posTopTube = Vector2(x, random.nextInt(FLUCTUATION) + TUB_GAP + LOWEST_OPENING)
        posBottomTube = Vector2(x, posTopTube.y - TUB_GAP - bottomTube.height)

        boundsTop = Rectangle(posTopTube.x, posTopTube.y, topTube.width.toFloat(), topTube.height.toFloat())
        boundsBottom = Rectangle(posBottomTube.x, posBottomTube.y, bottomTube.width.toFloat(), bottomTube.height.toFloat())
    }

    public fun reposition(x: Float){
        posTopTube.set(x, random.nextInt(FLUCTUATION) + TUB_GAP + LOWEST_OPENING)
        posBottomTube.set(x, posTopTube.y - TUB_GAP - bottomTube.height)

        boundsTop.setPosition(posTopTube.x, posTopTube.y)
        boundsBottom.setPosition(posBottomTube.x, posBottomTube.y)
    }

    public fun collides(player: Rectangle):Boolean{
        return player.overlaps(boundsTop) || player.overlaps(boundsBottom)
    }

    public fun dispose(){
        topTube.dispose()
        bottomTube.dispose()
    }


}