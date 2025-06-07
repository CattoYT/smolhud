package dev.hikari.smolhud.client

import dev.hikari.smolhud.client.Commands.Commands
import dev.hikari.smolhud.client.Config.SimpleConfig
import dev.hikari.smolhud.client.Renderer.RenderManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.slf4j.LoggerFactory
import java.util.*


class SmolhudClient : ClientModInitializer {

    override fun onInitializeClient() {
        logger.info("Initializing Smolhud client")

        HudRenderer.registerRenderModules()
        Commands().Commands()
        client = MinecraftClient.getInstance()

    }



    companion object {
        var client = MinecraftClient.getInstance()

        val defaultConfig = mapOf(
            "coords" to true,
            "coordsLabels" to false,

            "newline" to null,

            "surroundingEnemies" to false,
            "customPositions" to true,

            "newline" to null,

            "displayMobs" to true,
            "mobLabelLocationX" to 0,
            "mobLabelLocationY" to 0,
            "newline" to null,

            "displayPlayers" to true,
            "playerLabelLocationX" to 0,
            "playerLabelLocationY" to 0
        )

        var CONFIG: SimpleConfig = SimpleConfig.of("smolhud").provider({

            generateDefaultConfig(defaultConfig)
                .trimIndent()}).request()

        val HudRenderer = RenderManager()
        val version = FabricLoader.getInstance().getModContainer("smolhud")?.get()?.metadata?.version?.toString()

        val logger = LoggerFactory.getLogger("Smolhud")

        fun Log(message: String) {
            MinecraftClient.getInstance().player?.sendMessage(createReturnMessage(message), false)
        }

        fun Log(message: Text) {
            MinecraftClient.getInstance().player?.sendMessage(createReturnMessage(message), false)
        }

        fun generateDefaultConfig(defaultConfig: Map<String, Any?>): String {
            val defaults = StringBuilder()
            for ((key, value) in defaultConfig) {
                if (key == "newline") {
                    defaults.append("\n")
                    continue
                }
                defaults.append(key).append("=").append(value).append("\n")
            }

            return defaults.toString()
        }
        fun reloadConfig() {
            CONFIG.reloadConfig()
            HudRenderer.coords.coordsEnabled = CONFIG.getOrDefault("coords", true)
            HudRenderer.coords.coordsLabelsEnabled = CONFIG.getOrDefault("coordsLabels", false)
            HudRenderer.surroundingEnemies.surroundingEnemiesEnabled = CONFIG.getOrDefault("surroundingEnemies", false)
            HudRenderer.surroundingEnemies.displayMobs = CONFIG.getOrDefault("displayMobs", true)
            HudRenderer.surroundingEnemies.displayPlayers = CONFIG.getOrDefault("displayPlayers", true)
            HudRenderer.surroundingEnemies.reloadPositions()
//            HudRenderer.surroundingEnemies.xpos = CONFIG.getOrDefault("playerLabelLocationX", 0)
//            HudRenderer.surroundingEnemies.ypos = CONFIG.getOrDefault("playerLabelLocationY", 0)
        }

        fun createReturnMessage(vararg messages: Any): Text {
            val combinedText = Text.literal("(§3SmolHud§f) ")
            messages.forEach { text ->
                when (text) {
                    is String -> combinedText.append(Text.literal(text))
                    is Text -> combinedText.append(text)
                    else -> throw IllegalArgumentException("Only Text and String types are supported")

                }

            }
            return combinedText


        }
    }
}
