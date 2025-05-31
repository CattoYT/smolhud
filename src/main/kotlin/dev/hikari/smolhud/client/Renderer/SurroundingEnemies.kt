package dev.hikari.smolhud.client.Renderer

import dev.hikari.smolhud.client.SmolhudClient.Companion.client
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import kotlin.collections.emptyList

class SurroundingEnemies  : HudRenderCallback {
    override fun onHudRender(p0: DrawContext?, p1: Float) {


        //should probably check this in the /smolhud enemies command before writing render code
    }

    fun getSurroundingEnemies(): Pair<List<String>, List<String>> {
        // This function should return a list of surrounding enemies

        val world = client.world ?: return Pair(emptyList(), emptyList())

        val monsters = mutableListOf<String>()
        val players = mutableListOf<String>()

        for (entity in world.entities) {
            when {
                entity is Monster -> monsters.add(entity.name.string)
                entity is PlayerEntity && entity.isAlive && entity.isAttackable && entity != client.player ->
                    players.add(entity.name.string)
            }
        }
        return Pair(players, monsters)
    }

}