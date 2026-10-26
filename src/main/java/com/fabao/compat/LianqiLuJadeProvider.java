package com.fabao.compat;

import com.fabao.FabaoMod;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/** 炼器炉的 Jade 提示行：祭炼 / 喂养 / 认主 / 合成。 */
public enum LianqiLuJadeProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		tooltip.add(Text.translatable("fabao.jade.lianqi_lu.temper"));
		tooltip.add(Text.translatable("fabao.jade.lianqi_lu.feed"));
		tooltip.add(Text.translatable("fabao.jade.lianqi_lu.bind"));
		tooltip.add(Text.translatable("fabao.jade.lianqi_lu.craft"));
	}

	@Override
	public Identifier getUid() {
		return FabaoMod.id("lianqi_lu");
	}
}
