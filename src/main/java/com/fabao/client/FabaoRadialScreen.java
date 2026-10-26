package com.fabao.client;

import com.fabao.FabaoClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

/**
 * 法宝轮盘：按住蓄力键弹出已装备的三个法宝，鼠标拖到哪个、松开就发动哪个。
 * 蓄力时长从按下开始计算（服务端 ChargeLogic），松开发动时自动带上蓄力倍率。
 */
@Environment(EnvType.CLIENT)
public class FabaoRadialScreen extends Screen {
	private static final int RADIUS = 80;
	private static final int ICON_BOX = 30;
	private static final long CHARGE_MS = 3000L;

	private final List<ItemStack> stacks;
	private final long openedAt = System.currentTimeMillis();
	private int selected;
	private boolean fired;

	public FabaoRadialScreen(List<ItemStack> stacks, int selected) {
		super(Text.translatable("fabao.radial.title"));
		this.stacks = stacks;
		this.selected = Math.clamp(selected, 0, Math.max(0, stacks.size() - 1));
	}

	public int selected() {
		return selected;
	}

	public boolean isFired() {
		return fired;
	}

	public void markFired() {
		fired = true;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	/** 只用纯色暗化，不做全屏模糊（模糊每帧重算，打开轮盘时会明显卡）。 */
	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, this.width, this.height, 0x66000000);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		// 必须先让父类绘制标准背景（暗化/模糊），否则渲染状态不完整会闪烁
		super.render(context, mouseX, mouseY, delta);
		int cx = this.width / 2;
		int cy = this.height / 2;
		int count = Math.max(1, stacks.size());
		for (int i = 0; i < stacks.size(); i++) {
			double angle = Math.toRadians(-90 + i * (360.0 / count));
			int x = (int) (cx + Math.cos(angle) * RADIUS);
			int y = (int) (cy + Math.sin(angle) * RADIUS);
			boolean sel = i == selected;
			context.fill(x - ICON_BOX, y - ICON_BOX, x + ICON_BOX, y + ICON_BOX, sel ? 0xCCFFD8A8 : 0x99303030);
			context.fill(x - ICON_BOX + 2, y - ICON_BOX + 2, x + ICON_BOX - 2, y + ICON_BOX - 2,
					sel ? 0x55303010 : 0x33000000);
			context.drawItem(stacks.get(i), x - 8, y - 8);
			Text name = stacks.get(i).getName();
			context.drawTextWithShadow(this.textRenderer, name,
					cx - this.textRenderer.getWidth(name) / 2, y + ICON_BOX + 4,
					sel ? 0xFFFFF0C0 : 0xFFCCCCCC);
		}
		Text hint = Text.translatable("fabao.radial.hint");
		context.drawTextWithShadow(this.textRenderer, hint,
				cx - this.textRenderer.getWidth(hint) / 2, cy - 12, 0xFFFFFFFF);
		long elapsed = System.currentTimeMillis() - this.openedAt;
		int percent = (int) Math.min(100L, elapsed * 100L / CHARGE_MS);
		Text charge = percent >= 100
				? Text.translatable("fabao.radial.full")
				: Text.translatable("fabao.radial.charge", percent);
		context.drawTextWithShadow(this.textRenderer, charge,
				cx - this.textRenderer.getWidth(charge) / 2, cy + 6,
				percent >= 100 ? 0xFFFFF0C0 : 0xFFCCCCCC);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		int cx = this.width / 2;
		int cy = this.height / 2;
		double dx = mouseX - cx;
		double dy = mouseY - cy;
		if (dx * dx + dy * dy < ICON_BOX * ICON_BOX) {
			return;
		}
		double angle = Math.toDegrees(Math.atan2(dy, dx));
		int count = Math.max(1, stacks.size());
		int best = selected;
		double bestDiff = Double.MAX_VALUE;
		for (int i = 0; i < stacks.size(); i++) {
			double sector = -90 + i * (360.0 / count);
			double diff = Math.abs(((angle - sector + 540.0) % 360.0) - 180.0);
			if (diff < bestDiff) {
				bestDiff = diff;
				best = i;
			}
		}
		selected = best;
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		double mouseX = click.x();
		double mouseY = click.y();
		int cx = this.width / 2;
		int cy = this.height / 2;
		int count = Math.max(1, stacks.size());
		for (int i = 0; i < stacks.size(); i++) {
			double angle = Math.toRadians(-90 + i * (360.0 / count));
			int x = (int) (cx + Math.cos(angle) * RADIUS);
			int y = (int) (cy + Math.sin(angle) * RADIUS);
			double dx = mouseX - x;
			double dy = mouseY - y;
			if (dx * dx + dy * dy < ICON_BOX * ICON_BOX) {
				FabaoClient.fireRadial(i);
				return true;
			}
		}
		return super.mouseClicked(click, doubled);
	}

	@Override
	public void close() {
		if (!fired) {
			// Esc 取消：清掉服务端蓄力记录
			FabaoClient.cancelCharge();
		}
		super.close();
	}
}
