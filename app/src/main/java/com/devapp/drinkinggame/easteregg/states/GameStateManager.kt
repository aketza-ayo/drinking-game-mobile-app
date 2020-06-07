package com.devapp.drinkinggame.easteregg.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

class GameStateManager {

    private var states: Stack<State>
    private var menuState = MenuState(this)

    constructor(){
        this.states = Stack<State>()
        this.states.push(menuState)
    }

    public fun push(state: State){
        states.push(state)
    }

    public fun pop(){
        states.pop().dispose()
    }

    public fun set(state: State){
        states.pop().dispose()
        states.push(state)
    }

    public fun update(deltaTime: Float){
        //delta time is the changing time between two renders
        states.peek().update(deltaTime)
    }

    public fun render(spriteBatch: SpriteBatch){
        states.peek().render(spriteBatch)
    }
}