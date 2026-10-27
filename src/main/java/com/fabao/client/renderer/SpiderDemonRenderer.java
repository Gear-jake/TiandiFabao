package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.SpiderDemonModel;
import com.fabao.entity.SpiderDemonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 蜘蛛精渲染器（1.21.10 RenderState 架构）。
 */
public class SpiderDemonRenderer extends MobEntityRenderer<SpiderDemonEntity, LivingEntityRenderState, SpiderDemonModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/spider_demon.png");

	public SpiderDemonRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new SpiderDemonModel(ctx.getPart(SpiderDemonModel.LAYER)), 0.6f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTexture(LivingEntityRenderState state) {
		return TEXTURE;
	}

}