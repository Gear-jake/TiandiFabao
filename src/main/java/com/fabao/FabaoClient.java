package com.fabao;

import com.fabao.client.FabaoParticles;
import com.fabao.client.FabaoRadialScreen;
import com.fabao.equipment.FabaoEquipment;
import com.fabao.net.FabaoKeyPayload;
import com.fabao.registry.ModSounds;
import com.fabao.screen.LianqiLuScreen;
import com.fabao.screen.ModScreenHandlers;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * 客户端入口：
 * - 唯一按键「法宝·蓄力释放」（默认 R，可在 选项→控制→封神西游法宝 里自由修改）：
 *   按住弹出法宝轮盘并开始蓄力，拖拽选择、松开发动。
 * 注意：打开任意 Screen 时 Minecraft 会 KeyBinding.unpressAll()，
 * 所以轮盘打开期间必须跳过 isPressed 轮询，并用 armed 标志防止关闭瞬间重开（否则每 tick 开关一次 = 闪烁）。
 */
@Environment(EnvType.CLIENT)
public class FabaoClient implements ClientModInitializer {
	private static KeyBinding triggerKey;
	private static boolean armed = true;
	private static int chantCooldown = 0;
	/** 轮盘上次选择（客户端本地缓存，不依赖服务端 attachment 同步）。 */
	private static int lastSelected = 0;

	@Override
	public void onInitializeClient() {
		FabaoParticles.init();
		HandledScreens.register(ModScreenHandlers.LIANQI_LU, LianqiLuScreen::new);

		KeyBinding.Category category = KeyBinding.Category.create(FabaoMod.id("fabao"));
		triggerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.fabao.trigger", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) {
				armed = true;
				return;
			}

			// 轮盘打开期间完全交给 Screen 自己处理（选择/发动/取消），不再轮询按键
			if (client.currentScreen instanceof FabaoRadialScreen) {
				armed = false;
				return;
			}

			if (!triggerKey.isPressed()) {
				armed = true;
			}
			if (armed && triggerKey.wasPressed()) {
				armed = false;
				FabaoMod.LOGGER.info("[Fabao/client] trigger pressed, screen={}",
						client.currentScreen == null ? "none" : client.currentScreen.getClass().getSimpleName());
				ClientPlayNetworking.send(new FabaoKeyPayload(FabaoKeyPayload.Action.PRESS));
				openRadial(client);
			}

			// 诵经环境音：装配着法宝时，每 12 秒低吟一次
			if (--chantCooldown <= 0) {
				chantCooldown = 240;
				if (FabaoEquipment.equippedCount(client.player) > 0) {
					client.getSoundManager().play(
							PositionedSoundInstance.ambient(ModSounds.SONGJING_AMBIENT, 1.0f, 0.35f));
				}
			}
		});
	}

	private static void openRadial(MinecraftClient client) {
		List<SlotEntryReference> equipped = FabaoEquipment.equipped(client.player);
		FabaoMod.LOGGER.info("[Fabao/client] openRadial: equipped={} (client capability={})",
				equipped.size(), io.wispforest.accessories.api.AccessoriesCapability.get(client.player) != null);
		if (equipped.isEmpty()) {
			// 客户端查不到装备时仍让松开逻辑发普通 RELEASE（服务端权威数据可能恢复得晚）
			client.player.sendMessage(net.minecraft.text.Text.translatable("fabao.radial.no_equipped"), false);
			return;
		}
		List<ItemStack> stacks = equipped.stream().map(SlotEntryReference::stack).toList();
		int selected = Math.clamp(lastSelected, 0, stacks.size() - 1);
		FabaoRadialScreen radial = new FabaoRadialScreen(stacks, selected);
		client.setScreen(radial);
		ScreenKeyboardEvents.beforeKeyRelease(radial).register((screen, key) -> {
			int bound = InputUtil.fromTranslationKey(triggerKey.getBoundKeyTranslationKey()).getCode();
			if (key != null && key.key() == bound) {
				fireRadial(radial.selected());
			}
		});
	}

	/** 发动轮盘选中的法宝（发送带槽位的释放包并关闭轮盘）。 */
	public static void fireRadial(int slot) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.currentScreen instanceof FabaoRadialScreen radial) {
			if (radial.isFired()) {
				return;
			}
			radial.markFired();
		}
		lastSelected = Math.max(0, slot);
		ClientPlayNetworking.send(new FabaoKeyPayload(FabaoKeyPayload.Action.RELEASE, slot));
		client.setScreen(null);
	}

	/** Esc 取消轮盘：清掉服务端蓄力记录。 */
	public static void cancelCharge() {
		ClientPlayNetworking.send(new FabaoKeyPayload(FabaoKeyPayload.Action.CANCEL));
	}
}
