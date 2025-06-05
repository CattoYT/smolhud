package dev.hikari.smolhud.client.Commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import dev.hikari.smolhud.Smolhud
import dev.hikari.smolhud.client.Renderer.RenderManager
import dev.hikari.smolhud.client.SmolhudClient
import dev.hikari.smolhud.client.SmolhudClient.Companion.HudRenderer
import dev.hikari.smolhud.client.SmolhudClient.Companion.Log
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class Commands {
    fun registerCommands() {
        modInfo()
    } // TODO: Make the commands allow for using @ as the prefix cuz / doesnt work on servers
    fun modInfo() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource>, _, _ ->
            dispatcher.register(
                CommandManager.literal("smolhud")
                    .executes {
                        Log("Smolhud v${
                            SmolhudClient.version} by Hikari")
                        0
                    }
                    .then(
                        CommandManager.argument("arg1", StringArgumentType.string())
                            .executes { context ->
                                when (StringArgumentType.getString(context, "arg1")) {
                                    "reload" -> {
                                        Log("Reloading Smolhud config...")
                                        SmolhudClient.reloadConfig()
                                    }
                                    "debug" -> {
                                        Log("Coords enabled: ${HudRenderer.coords.coordsEnabled}")
                                        Log("Should be: ${SmolhudClient.CONFIG.getOrDefault("coords", null)}")
                                        Log("Coords labels enabled: ${HudRenderer.coords.coordsLabelsEnabled}")
                                        Log("Should be: ${SmolhudClient.CONFIG.getOrDefault("coordsLabels", null)}")
                                        Log("mobXPos: ${HudRenderer.surroundingEnemies.mobXPos}")
                                        Log("mobYPos: ${HudRenderer.surroundingEnemies.mobYPos}")
                                    }
                                    "enemies" -> {
                                        Log("${HudRenderer.surroundingEnemies.getSurroundingEnemies()}")
                                    }
                                    else -> {
                                        Log("Unknown command. Use /smolhud reload to reload the config.")
                                    }
                                }
                                0
                            }
            ))
        })
    }

}