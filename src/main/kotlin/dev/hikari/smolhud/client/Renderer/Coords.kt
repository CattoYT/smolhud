package dev.hikari.smolhud.client.Renderer

import dev.hikari.smolhud.Smolhud
import dev.hikari.smolhud.client.SmolhudClient
import dev.hikari.smolhud.client.SmolhudClient.Companion.client
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import kotlin.collections.getOrDefault

class Coords : HudRenderCallback {
    var textRenderer: TextRenderer? = null
    var coordsEnabled = SmolhudClient.CONFIG.getOrDefault("coords", true)
    var coordsLabelsEnabled = SmolhudClient.CONFIG.getOrDefault("coordsLabels", false)

    override fun onHudRender(drawContext: DrawContext?, tickDelta: Float) {
        if (!coordsEnabled) {
            return
        }
        if (textRenderer == null) {
            this.textRenderer = client.textRenderer
        }

        var posX = parseCoords(client.player?.pos?.x.toString())
        var posY = parseCoords(client.player?.pos?.y.toString())
        var posZ = parseCoords(client.player?.pos?.z.toString())

        if (!coordsLabelsEnabled) {
            drawContext?.drawCenteredTextWithShadow(
                textRenderer,
                "$posX $posY $posZ",
                MinecraftClient.getInstance().window.scaledWidth / 2,
                MinecraftClient.getInstance().window.scaledHeight - 50,
                0xFFFFFF
            )
        } else {
            drawContext?.drawCenteredTextWithShadow(
                textRenderer,
                "x: $posX y: $posY z: $posZ",
                MinecraftClient.getInstance().window.scaledWidth / 2,
                MinecraftClient.getInstance().window.scaledHeight - 50,
                0xFFFFFF
            )
        }
    }

    fun parseCoords(coord: String): String? {
        var formattedCoord : String = coord // just in case shit gets messed up
        for (i in 0..coord.length) {
            try {
                if (coord[i] == '.') {
                    formattedCoord = coord.substring(0, i) + "." + (coord.substring(i + 1, i + 3))
                }
            } catch (e: Exception) {
                // Ignore the exception, it means we are out of bounds
            }
        }
        return formattedCoord
    }
}