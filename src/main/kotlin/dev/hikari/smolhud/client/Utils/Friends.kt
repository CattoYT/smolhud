package dev.hikari.smolhud.client.Utils

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
    }
}