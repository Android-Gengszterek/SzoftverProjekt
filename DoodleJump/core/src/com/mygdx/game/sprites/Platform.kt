package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

class Platform {
    var greenPlatformTexture: Texture
    var platformPosition: Vector2
    private var rand: Random
    var bounds: Rectangle
    var velocity: Vector2
    var fallSpeed:Float
    var isWood: Boolean
    var moveSpeed:Float
    var isMooving: Boolean
    var uniqueSpacingMultiplier: Float = 500f
    var width: Float = 150f
    var height: Float = 40f
    var deactivated = false

    // init platform attributes
    constructor(y: Float,texture_picture: String, isWood: Boolean){
        velocity = Vector2(0f,0f)
        greenPlatformTexture = Texture(texture_picture)
        rand = Random()
        var randNumber:Float = rand.nextFloat() * (Gdx.graphics.width-500f) + width

        platformPosition = Vector2(randNumber , y)

        bounds = Rectangle(platformPosition.x, platformPosition.y,width, height/2 )
        this.isWood = isWood
        isMooving = false
        fallSpeed = 0f
        moveSpeed = 200f

    }

    // platforms will be repositioned at the y parameter with random x koordinate
    fun reposition(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + width,y)
        bounds.set(platformPosition.x, platformPosition.y,width,height)
    }

    // reposition platform in the X koordinate
    fun repositionWoodenAlongX(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + width,y)
        bounds.set(platformPosition.x, platformPosition.y,width,height)
    }


    // returns true if the texture overlaps with the parameters bounds
    fun collide(bound: Rectangle):Boolean{
        var position: Vector2 = Vector2()
        bound.getPosition(position)
        if(bounds.overlaps(bound) &&  (position.y > platformPosition.y) && (position.y < platformPosition.y + 20)){
            return true
        }
        return false
    }

    fun platformFall(){
        if(isWood) {
            fallSpeed = -1600f
        }
    }

    fun stopFall(){
        fallSpeed = 0f
    }

    fun turnLeft(){
        moveSpeed = -200f
    }

    fun turnRight(){
        moveSpeed = 200f
    }

    fun startMove(){
        isMooving = true
    }

    fun update(dt:Float,cam: OrthographicCamera,player: Player){
        if(isWood) {
            // if the wood platform is in the screen it will move with fallSpeed velocity
            if (platformPosition.y > cam.position.y - (Gdx.graphics.height)) {
                velocity.add(0f, fallSpeed)
                velocity.scl(dt)
                platformPosition.add(0f, velocity.y)
                velocity.scl(1 / dt)
                bounds.setPosition(platformPosition.x, platformPosition.y)
                velocity.add(0f, -fallSpeed)
                // if the platform is out of the screen it will stop moving
            }else {
                stopFall()
            }
        } else {
            if(isMooving) {
                // if the platform touches the left side of the screen turns right
                if (platformPosition.x < 0) {
                    turnRight()
                }
                // if the platform touches the right side of the screen turns left
                if (platformPosition.x + bounds.width > Gdx.graphics.width) {
                    turnLeft()
                }
                velocity.set(moveSpeed, 0f)
                velocity.scl(dt)
                platformPosition.add(velocity.x, 0f)
                velocity.scl(1 / dt)
                bounds.setPosition(platformPosition.x, platformPosition.y)
            }
        }
    }
}