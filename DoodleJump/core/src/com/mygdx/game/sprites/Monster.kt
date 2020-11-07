package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Monster {

    var monsterTexture: Texture
    var monsterPosition: Vector2
    var velocity: Vector2
    var bounds: Rectangle
    var width: Float = 400f
    var height: Float = 270f
    var moveSpeed = 200f
    private var monsterStartScore = 200
    private var monsterSpacingAsScore = 500

    constructor(){
        velocity = Vector2(0f,0f)
        monsterTexture = Texture("green_monster.png")
        monsterPosition = Vector2((Gdx.graphics.width - width)/2,monsterStartScore * 100f)
        bounds = Rectangle(monsterPosition.x+100f, monsterPosition.y+50f, width-160f, height-100f )

    }

    fun collide(player: Player):Boolean{
        if( player.bounds.overlaps(bounds) && !player.isImmune){
            // if the player touches the monster from below or from the sides then die
            if(player.bounds.y < monsterPosition.y+height-100f) {
                return true
            } else {
                // if the player touches the monster from the top then the character will jump and the monster will be raplaced
                player.jump()
                replaceMonster()
                return false
            }
        }
        return false
    }

    // The monster will be replaced by spacing
    fun replaceMonster(){
        monsterPosition.y = monsterPosition.y + (monsterSpacingAsScore*100f)
        bounds.x = monsterPosition.x
        bounds.y = monsterPosition.y
    }

    fun update(dt:Float){
        velocity.set(moveSpeed, 0f)
        velocity.scl(dt)
        monsterPosition.add(velocity.x, 0f)
        velocity.scl(1 / dt)
        bounds.setPosition(monsterPosition.x, monsterPosition.y)
    }

    fun turnLeft(){
        moveSpeed = -200f
    }

    fun turnRight(){
        moveSpeed = 200f
    }

}