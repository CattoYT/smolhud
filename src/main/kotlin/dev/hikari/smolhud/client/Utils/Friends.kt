package dev.hikari.smolhud.client.Utils

import net.fabricmc.loader.api.FabricLoader

class Friends {
    companion object {
        val friendsList: MutableList<String> = mutableListOf()
        val hostileList: MutableList<String> = mutableListOf()

        fun addFriend(name: String) {
            if (!friendsList.contains(name)) {
                friendsList.add(name)
            }
        }
        fun addHostile(name: String) {
            if (!hostileList.contains(name)) {
                hostileList.add(name)
            }
        }

        fun removeFriend(name: String) {
            friendsList.remove(name)
        }
        fun removeHostile(name: String) {
            hostileList.remove(name)
        }

        fun isFriend(name: String): Boolean {
            return friendsList.contains(name)
        }
        fun isHostile(name: String): Boolean {
            return hostileList.contains(name)
        }

        fun saveFriends() {
            val friendsFile = FabricLoader.getInstance().configDir.resolve( "smolhud/friends.txt").toFile()
            friendsFile.writeText(friendsList.joinToString("\n"))
            val enemiesFile = FabricLoader.getInstance().configDir.resolve( "smolhud/enemies.txt").toFile()
            enemiesFile.writeText(hostileList.joinToString("\n"))
        }
        fun loadFriends() {
            val friendsFile = FabricLoader.getInstance().configDir.resolve( "smolhud/friends.txt").toFile()
            if (friendsFile.exists()) {
                friendsList.clear()
                friendsList.addAll(friendsFile.readLines())
            }
            val enemiesFile = FabricLoader.getInstance().configDir.resolve( "smolhud/enemies.txt").toFile()
            if (enemiesFile.exists()) {
                hostileList.clear()
                hostileList.addAll(enemiesFile.readLines())
            }
        }
    }
}