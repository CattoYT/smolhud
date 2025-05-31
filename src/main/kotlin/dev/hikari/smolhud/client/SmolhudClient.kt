package dev.hikari.smolhud.client

import dev.hikari.smolhud.client.Commands.Commands
import dev.hikari.smolhud.client.Config.SimpleConfig
import dev.hikari.smolhud.client.Renderer.RenderManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.slf4j.LoggerFactory


class SmolhudClient : ClientModInitializer {

    override fun onInitializeClient() {
        logger.info("Initializing Smolhud client")

        HudRenderer.registerRenderModules()
        Commands().registerCommands()


    }




    companion object {
        var client = MinecraftClient.getInstance()

        var CONFIG: SimpleConfig = SimpleConfig.of("smolhud").provider({
            """
            # Smolhud configuration file
            # Format: key=value
            coords=true
            coordsLabels=false
            surroundingEnemies=false
            """
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

        fun reloadConfig() {
            CONFIG.reloadConfig()
            HudRenderer.coords.coordsEnabled = CONFIG.getOrDefault("coords", true)
            HudRenderer.coords.coordsLabelsEnabled = CONFIG.getOrDefault("coordsLabels", false)
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
