package dev.hikari.smolhud.client.Renderer

import dev.hikari.smolhud.client.SmolhudClient
import dev.hikari.smolhud.client.SmolhudClient.Companion.CONFIG
import dev.hikari.smolhud.client.SmolhudClient.Companion.client
import dev.hikari.smolhud.client.Utils.Friends
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SurroundingEnemies  : HudRenderCallback {
    init {
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleEvents.ServerStarting {
            // Reload positions when the server starts
            reloadPositions()

        })
    }

    var textRenderer: TextRenderer? = null
    var surroundingEnemiesEnabled = CONFIG.getOrDefault("surroundingEnemies", false)
    var displayMobs = CONFIG.getOrDefault("displayMobs", true)
    var displayPlayers = CONFIG.getOrDefault("displayPlayers", true)

    var mobXPos = 0
    var mobYPos = 0
    var playerXPos = 0
    var playerYPos = 0

    var text = Text.literal("Surrounding Enemies:")

    override fun onHudRender(drawContext: DrawContext?, p1: Float) {
        if (!surroundingEnemiesEnabled) {
            return
        }
        val (players, monsters) = getSurroundingEnemies()
        // this always fails first time :/ oh well
        if (textRenderer == null) {
            this.textRenderer = client.textRenderer
            SmolhudClient.logger.error("TextRenderer is null, cannot render coordinates.")

        }
        if (!players.isEmpty() && displayPlayers) {
            var yOffset = 10

            drawContext?.drawTextWithShadow(textRenderer, text.setStyle(text.style.withUnderline(true)), playerXPos, playerYPos, 0xFFFFFF, )
            for (player in players) {
                if (Friends.isFriend(player)) {
                    drawContext?.drawTextWithShadow(textRenderer, player, playerXPos, playerYPos + yOffset, 0x00FF00)
                }
                else if (Friends.isHostile(player)) {
                    drawContext?.drawTextWithShadow(textRenderer, player, playerXPos, playerYPos + yOffset, 0xFF0000)
                }
                else {
                    drawContext?.drawTextWithShadow(textRenderer, player, playerXPos, playerYPos + yOffset, 0xFFFFFF)
                }
                yOffset += 10
            }
        }
        if (!monsters.isEmpty() && displayMobs) {
            drawContext?.drawCenteredTextWithShadow(textRenderer, "Mobs: ${monsters.size}", mobXPos, mobYPos, 0xFF0000)
        }


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

    fun reloadPositions() {
        playerXPos = if (CONFIG.getOrDefault("customPlayerPositions", false)) CONFIG.getOrDefault(
            "playerLabelLocationX",
            0
        ) else 0 //else client.window.scaledWidth / 2 - 110
        playerYPos = if (CONFIG.getOrDefault("customPlayerPositions", false)) CONFIG.getOrDefault(
            "playerLabelLocationY",
            0
        ) else 10
        mobXPos = if (CONFIG.getOrDefault("customMobPositions", false)) CONFIG.getOrDefault(
            "mobLabelLocationX",
            0
        ) else client.window.scaledWidth / 2 + 110
        mobYPos = if (CONFIG.getOrDefault("customMobPositions", false)) CONFIG.getOrDefault(
            "mobLabelLocationY",
            0
        ) else client.window.scaledHeight - 10

    }

}