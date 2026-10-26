package com.fabao.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * 炼器炉界面：背景贴图 + 四个操作按钮（祭炼/喂养/认主/合成）。
 * 1.21.10 的 drawGuiTexture 参数是 gui atlas 的 sprite id：
 * 贴图位于 assets/fabao/textures/gui/sprites/lianqi_lu.png → sprite id = fabao:lianqi_lu。
 */
@Environment(EnvType.CLIENT)
public class LianqiLuScreen extends HandledScreen<LianqiLuScreenHandler> {
	private static final Identifier BACKGROUND = Identifier.of("fabao", "lianqi_lu");

	public LianqiLuScreen(LianqiLuScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		int x = (this.width - this.backgroundWidth) / 2;
		int y = (this.height - this.backgroundHeight) / 2;
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("fabao.gui.button.temper"), b -> click(0))
				.dimensions(x + 100, y + 20, 64, 16).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("fabao.gui.button.feed"), b -> click(1))
				.dimensions(x + 100, y + 38, 64, 16).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("fabao.gui.button.bind"), b -> click(2))
				.dimensions(x + 100, y + 56, 64, 16).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("fabao.gui.button.craft"), b -> click(3))
				.dimensions(x + 100, y + 74, 64, 16).build());
	}

	private void click(int id) {
		var client = net.minecraft.client.MinecraftClient.getInstance();
		if (client.player != null && client.interactionManager != null) {
			client.interactionManager.clickButton(this.handler.syncId, id);
		}
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND,
				this.x, this.y, this.backgroundWidth, this.backgroundHeight);
	}
}
