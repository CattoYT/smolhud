package dev.hikari.smolhud.client.Commands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class CommandSuggestions : SuggestionProvider<ServerCommandSource> {

    override fun getSuggestions(
        context: CommandContext<ServerCommandSource?>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions?>? {
        // todo
        return Suggestions.empty()
    }

}