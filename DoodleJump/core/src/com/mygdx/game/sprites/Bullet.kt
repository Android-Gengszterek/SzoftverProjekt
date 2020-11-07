package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Bullet {
    var bulletTexture: Texture
    var bulletPosition: Vector2
    var bounds: Rectangle
    var velocity: Vector2
    var isReset:Boolean = false


    constructor(){
        velocity = Vector2(0f,0f)
        bulletTexture = Texture("bullet.png")
        bulletPosition = Vector2(-500f,0f)
        bounds = Rectangle(bulletPosition.x, bulletPosition.y, 50f,50f )

    }

    fun collide(bound: Rectangle):Boolean{
        return bound.overlaps(bounds)
    }

    fun update(dt:Float,player: Player){
        if(bulletPosition.y < (player.position.y + Gdx.graphics.height)) {
            if (bulletPosition.x < Gdx.graphics.width / 2) {
                velocity.x = 1000f
            } else {
                velocity.x = -1000f
            }
            velocity.y = 3000f
            velocity.scl(dt)
            bulletPosition.add(velocity.x, velocity.y)
            velocity.scl(1 / dt)
            bounds.setPosition(bulletPosition.x, bulletPosition.y)
        }
    }

    // If the bullet is not reset and the player touches the screen then the bullet will be reset at the top of the player
    fun reset(player: Player){
        if (!isReset) {
            bulletPosition.set(player.position.x + (player.playerTexture.width / 5), player.position.y + (player.playerTexture.height / 2.7f))
            bounds.setPosition(bulletPosition.x, bulletPosition.y)
        }
        isReset = true
    }
}