package dev.aperso.composite

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.aperso.composite.hud.ComposeHudRegistryHolder
import dev.aperso.composite.hud.fabric.FabricComposeHudRegistry
import dev.aperso.composite.skia.SkiaContext
import dev.aperso.composite.test.AssetImageTest
import dev.aperso.composite.test.HudTest
import dev.aperso.composite.test.ItemTest
import dev.aperso.composite.test.TextureTest
import dev.aperso.composite.test.TranslationTest
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Composite : ClientModInitializer {
    val logger: Logger = LoggerFactory.getLogger("Composite")

    override fun onInitializeClient() {
        ComposeHudRegistryHolder.init(FabricComposeHudRegistry())
        ClientLifecycleEvents.CLIENT_STARTED.register {
            logger.info("initializing skia context")
            SkiaContext.initialize()
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                LiteralArgumentBuilder.literal<FabricClientCommandSource>("composite")
                    .then(
                        LiteralArgumentBuilder.literal<FabricClientCommandSource>("test")
                            .then(TextureTest.register())
                            .then(ItemTest.register())
                            .then(AssetImageTest.register())
                            .then(TranslationTest.register())
                            .then(HudTest.register())
                    )
            )
        }
    }
}