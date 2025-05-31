package dev.hikari.smolhud.client.Renderer

import dev.hikari.smolhud.client.SmolhudClient.Companion.CONFIG
import dev.hikari.smolhud.client.SmolhudClient.Companion.logger
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

class RenderManager {
    val coords = Coords()
    val surroundingEnemies = SurroundingEnemies()
    fun registerRenderModules()  {

        HudRenderCallback.EVENT.register(coords)

        //HudRenderCallback.EVENT.register(surroundingEnemies)


    }

}