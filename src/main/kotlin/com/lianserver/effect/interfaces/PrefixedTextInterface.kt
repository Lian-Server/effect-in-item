package com.lianserver.effect.interfaces

import com.lianserver.effect.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

interface PrefixedTextInterface {
    private fun getInstance(): Main = Main.instance

    fun clanText(s: String): TextComponent =
        Component.text("${getInstance().config.getString("clan_prefix")} $s")
    fun clanTextS(s: String): String = "${getInstance().config.getString("clan_prefix")} $s"

    fun adminText(s: String): TextComponent =
        Component.text("${getInstance().config.getString("admin_prefix")} $s")
    fun adminTextS(s: String): String = "${getInstance().config.getString("admin_prefix")} $s"

    fun countryText(s: String): TextComponent =
        Component.text("${getInstance().config.getString("country_prefix")} $s")
    fun countryTextS(s: String): String = "${getInstance().config.getString("country_prefix")} $s"

    fun userText(s: String): TextComponent =
        Component.text("${getInstance().config.getString("user_prefix")} $s")
    fun userTextS(s: String): String = "${getInstance().config.getString("user_prefix")} $s"
}