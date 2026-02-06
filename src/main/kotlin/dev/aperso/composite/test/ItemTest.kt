package dev.aperso.composite.test

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.aperso.composite.component.Components
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object ItemTest : TestCommand("item") {
    override val content: @Composable () -> Unit = {
        val verticalScroll = rememberScrollState()
        val horizontalScroll = rememberScrollState()
        var inputText by remember { mutableStateOf("input text test") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScroll)
                    .horizontalScroll(horizontalScroll)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(Color.White).padding(8.dp)
                ) {
                    Text("Input: ", modifier = Modifier.padding(end = 8.dp))
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.background(Color.LightGray)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                repeat(20) { rowIndex ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(15) { colIndex ->
                            val itemStack = when ((rowIndex + colIndex) % 3) {
                                0 -> ItemStack(Items.DIAMOND_SWORD)
                                1 -> ItemStack(Items.APPLE).copyWithCount(8)
                                else -> ItemStack(Items.GRASS_BLOCK).copyWithCount(64)
                            }
                            Components.Item(itemStack, Modifier.size(64.dp))
                        }
                    }
                }
            }
        }
    }
}
