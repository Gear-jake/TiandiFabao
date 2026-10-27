package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.FoxDemonModel;
import com.fabao.entity.FoxDemonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 狐妖渲染器（1.21.10 RenderState 架构）。
 */
public class FoxDemonRenderer extends MobEntityRenderer<FoxDemonEntity, LivingEntityRenderState, FoxDemonModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/fox_demon.png");

	public FoxDemonRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new FoxDemonModel(ctx.getPart(FoxDemonModel.LAYER)), 0.4f);
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