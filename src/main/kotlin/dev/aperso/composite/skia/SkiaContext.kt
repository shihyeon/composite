package dev.aperso.composite.skia

import com.mojang.blaze3d.systems.RenderSystem
import org.jetbrains.skia.DirectContext

object SkiaContext {
    val directContext: DirectContext by lazy {
        DirectContext.makeGL()
    }

    fun initialize() {
        directContext
    }

    fun run(runnable: Runnable) {
        RenderSystem.assertOnRenderThread()
        runnable.run()
    }
}