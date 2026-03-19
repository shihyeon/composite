package dev.aperso.composite.core

import androidx.compose.runtime.Composable
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor

/**
 * A HUD element backed by a Compose UI.
 *
 * Register with:
 * ```
 * HudElementRegistry.addLast(
 *     Identifier.fromNamespaceAndPath("modid", "my_hud"),
 *     composeHud
 * )
 * ```
 *
 * Call [close] when unregistering to release Compose and GPU resources.
 */
open class ComposeHud(content: @Composable () -> Unit) : HudElement {
    val gui = ComposeGui(content)

    override fun extractRenderState(graphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker) {
        gui.init()
        gui.extractRenderState(graphics, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(true))
    }

    fun close() {
        gui.onClose()
    }
}
