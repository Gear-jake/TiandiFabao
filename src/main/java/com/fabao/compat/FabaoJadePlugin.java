package com.fabao.compat;

import com.fabao.block.LianqiLuBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Jade（玉）联动插件：仅当玩家安装了 Jade 时才由 "jade" entrypoint 触发，不装无影响。
 * 在炼器炉的悬浮信息里追加四行操作指南。
 */
@WailaPlugin("fabao")
public class FabaoJadePlugin implements IWailaPlugin {
	@Override
	public void register(IWailaCommonRegistration registration) {
		// 炼器炉无状态，无需服务端数据同步
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(LianqiLuJadeProvider.INSTANCE, LianqiLuBlock.class);
	}
}
