package com.devapp.drinkinggame.easteregg.sprites

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Animation {
    private var frames: ArrayList<TextureRegion>
    private var maxFrameTime: Float
    private var currentFrameTime: Float
    private var frameCount : Int
    private var currentFrame : Int

    constructor(region: TextureRegion, frameCount: Int, cycleTime: Float){
        frames = ArrayList<TextureRegion>()
        var frameWidth = region.regionWidth / frameCount

        var i = 0
        while(i < frameCount){
            frames.add(TextureRegion(region, i * frameWidth, 0, frameWidth, region.regionHeight))
            i++
        }
        this.frameCount = frameCount
        this.maxFrameTime = cycleTime / frameCount
        this.currentFrame = 0
        this.currentFrameTime = 0F
    }

    public fun update(deltaTime: Float){
        currentFrameTime += deltaTime
        if(currentFrameTime > maxFrameTime){
            currentFrame++
            currentFrameTime = 0F
        }

        if(currentFrame >= frameCount){
            currentFrame = 0
        }

    }

    public fun getFrame(): TextureRegion{
        return frames[currentFrame]
    }

}