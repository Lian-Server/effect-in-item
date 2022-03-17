package com.lianserver.effect.command

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import com.lianserver.effect.interfaces.EffectInterface
import com.lianserver.effect.interfaces.KommandInterface
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class EffectMenuKommand: KommandInterface {
    fun namedItemStack(m: Material, t: Component, l: List<Component> = listOf()): ItemStack {
        val st = ItemStack(m)
        val meta = st.itemMeta

        meta.displayName(t.decoration(TextDecoration.ITALIC, false))
        meta.lore(l.map { it.decoration(TextDecoration.ITALIC, false) })

        ItemFlag.values().forEach { meta.addItemFlags(it) }

        st.itemMeta = meta

        return st
    }

    override fun kommand() {
        getInstance().kommand {
            register("effects") {
                executes {
                    val guiEffectList = ChestGui(4, "이펙트 목록")
                    guiEffectList.setOnGlobalClick { e: InventoryClickEvent ->
                        e.isCancelled = true
                    }
                    val paneItem = PaginatedPane(1, 1, 7, 2)
                    paneItem.populateWithGuiItems(
                        getInstance().userEffDB.getStringList(player.uniqueId.toString()).map {
                            val eff = getInstance().loadedEffects[it]!! as EffectInterface

                            GuiItem(
                                namedItemStack(Material.ENCHANTED_BOOK, text(eff.name).color(NamedTextColor.GOLD), eff.description.map {
                                    text(it).color(NamedTextColor.AQUA)
                                })
                            ){
                                getInstance().playerEffects[player.uniqueId.toString()] = eff.id
                                it.isCancelled = true
                            }
                        }
                    )
                    val navigation = StaticPane(1, 3, 7, 1)

                    val rw = ItemStack(Material.RED_WOOL)
                    var meta = rw.itemMeta
                    meta.displayName(text("이전").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                    rw.itemMeta = meta

                    navigation.addItem(
                        GuiItem(
                            rw
                        ) { event: InventoryClickEvent ->
                            event.isCancelled = true

                            if (paneItem.page > 0) {
                                paneItem.page = paneItem.page - 1
                                guiEffectList.update()
                            }
                        }, 0, 0
                    )

                    val gw = ItemStack(Material.GREEN_WOOL)
                    meta = gw.itemMeta
                    meta.displayName(Component.text("다음").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                    gw.itemMeta = meta

                    navigation.addItem(
                        GuiItem(
                            gw
                        ) { event: InventoryClickEvent ->
                            event.isCancelled = true

                            if (paneItem.page < paneItem.pages - 1) {
                                paneItem.page = paneItem.page + 1
                                guiEffectList.update()
                            }
                        }, 6, 0
                    )

                    val br = ItemStack(Material.BARRIER)
                    meta = br.itemMeta
                    meta.displayName(Component.text("닫기").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                    br.itemMeta = meta

                    navigation.addItem(
                        GuiItem(
                            br
                        ) { event: InventoryClickEvent ->
                            event.isCancelled = true

                            event.whoClicked.closeInventory()
                        }, 3, 0
                    )

                    guiEffectList.addPane(navigation)
                    val background = OutlinePane(0, 0, 9, 4)
                    val stack = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
                    meta = stack.itemMeta
                    meta.displayName(text(""))
                    stack.itemMeta = meta

                    background.addItem(GuiItem(stack))
                    background.setRepeat(true)
                    background.priority = Pane.Priority.LOWEST
                    background.setOnClick { event: InventoryClickEvent ->
                        event.isCancelled = true
                    }
                    guiEffectList.addPane(paneItem)
                    guiEffectList.addPane(background)

                    guiEffectList.update()
                    guiEffectList.show(player)
                }
            }
        }
    }

    init {}
}