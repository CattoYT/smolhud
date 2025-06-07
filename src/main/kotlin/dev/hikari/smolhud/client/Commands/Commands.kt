package dev.hikari.smolhud.client.Commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import dev.hikari.smolhud.client.SmolhudClient
import dev.hikari.smolhud.client.SmolhudClient.Companion.HudRenderer
import dev.hikari.smolhud.client.SmolhudClient.Companion.Log
import dev.hikari.smolhud.client.Utils.Friends
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.util.Formatting


class Commands {

    // TODO: OMG THIS FILE IS SO BAD
    fun Commands() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource>, _ ->
            dispatcher.register(
                ClientCommandManager.literal("smolhud")
                    .executes {
                        Log(
                            "Smolhud v${
                                SmolhudClient.version
                            } by Hikari"
                        )
                        0
                    }
                    .then(
                        ClientCommandManager.literal("enemies")
                            .then(
                                ClientCommandManager.literal("add")
                                    .then(
                                        ClientCommandManager.argument("player", PlayerListEntryArgumentType.create())
                                            .executes { context ->
                                                val profile = PlayerListEntryArgumentType.get(context).getProfile()

                                                if (Friends.isHostile(profile.name)) {
                                                    Log("You are already enemies with ${profile.name}.")
                                                    return@executes 0

                                                }
                                                Friends.addHostile(profile.name)
                                                Log("Added ${profile.name} to enemies list.")
                                                0
                                            }

                                    )

                            ).then(
                                ClientCommandManager.literal("remove")
                                    .then(
                                        ClientCommandManager.argument("player", EnemyArgumentType.create())
                                            .executes { context ->
                                                val enemy = EnemyArgumentType.get(context)

                                                if (!Friends.isHostile(enemy)) {
                                                    Log("You are not enemies with ${enemy}.")
                                                    return@executes 0
                                                }
                                                Friends.removeHostile(enemy)
                                                Log("Removed ${enemy} from enemies list.")
                                                0
                                            }

                                    )
                            ).then(
                                ClientCommandManager.literal("list")
                                    .executes { context ->
                                        if (Friends.hostileList.isEmpty()) {
                                            Log("You have no enemies :D")
                                            return@executes 0
                                        }
                                        Log("Your enemies: \n${Friends.hostileList.joinToString("\n")}")
                                        0
                                    }
                            )
                    )
                    .then(
                        // separated cuz the friends command requires a few more args

                        ClientCommandManager.literal("friend")
                            .then(
                                ClientCommandManager.literal("add")
                                    .then(
                                        ClientCommandManager.argument("player", PlayerListEntryArgumentType.create())
                                            .executes { context ->
                                                val profile = PlayerListEntryArgumentType.get(context).getProfile()

                                                if (Friends.isFriend(profile.name)) {
                                                    Log("You are already friends with ${profile.name}.")
                                                    return@executes 0

                                                }
                                                Friends.addFriend(profile.name)
                                                Log("Added ${profile.name} to friends list.")
                                                0
                                            }

                                    )

                            ).then(
                                ClientCommandManager.literal("remove")
                                    .then(
                                        ClientCommandManager.argument("player", FriendArgumentType.create())
                                            .executes { context ->
                                                val friend = FriendArgumentType.get(context)

                                                if (!Friends.isFriend(friend)) {
                                                    Log("You are not friends with ${friend}.")
                                                    return@executes 0
                                                }
                                                Friends.removeFriend(friend)
                                                Log("Removed ${friend} from friends list.")
                                                0
                                            }

                                    )
                            ).then(
                                ClientCommandManager.literal("list")
                                    .executes { context ->
                                        if (Friends.friendsList.isEmpty()) {
                                            Log("You have no friends. o7")
                                            return@executes 0
                                        }
                                        Log("Your friends: \n${Friends.friendsList.joinToString("\n")}")
                                        0
                                    }
                            )
                    )
                    .then(
                        ClientCommandManager.literal("reload")
                            .executes { context ->
                                Log("Reloading Smolhud config...")
                                SmolhudClient.reloadConfig()
                                0
                            }
                    )
                    .then(
                        ClientCommandManager.literal("debug")
                            .executes { context ->
                                Log("Debugging Smolhud...")
                                Log("Coords enabled: ${HudRenderer.coords.coordsEnabled}")
                                Log("Coords labels enabled: ${HudRenderer.coords.coordsLabelsEnabled}")
                                Log("Surrounding enemies enabled: ${HudRenderer.surroundingEnemies.surroundingEnemiesEnabled}")
                                Log("Display mobs: ${HudRenderer.surroundingEnemies.displayMobs}")
                                Log("Display players: ${HudRenderer.surroundingEnemies.displayPlayers}")
                                0
                            }
                    )
                    .then(
                        ClientCommandManager.literal("enemies")
                            .executes { context ->
                                val (players, monsters) = HudRenderer.surroundingEnemies.getSurroundingEnemies()
                                Log("Surrounding players: ${players.joinToString(", ")}")
                                Log("Surrounding monsters: ${monsters.joinToString(", ")}")
                                0
                            }
                    )


            )
        })
    }


}