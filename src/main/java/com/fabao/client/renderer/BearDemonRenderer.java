package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.BearDemonModel;
import com.fabao.entity.BearDemonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 黑熊精渲染器（1.21.10 RenderState 架构）。
 */
public class BearDemonRenderer extends MobEntityRenderer<BearDemonEntity, LivingEntityRenderState, BearDemonModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/bear_demon.png");

	public BearDemonRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new BearDemonModel(ctx.getPart(BearDemonModel.LAYER)), 0.7f);
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