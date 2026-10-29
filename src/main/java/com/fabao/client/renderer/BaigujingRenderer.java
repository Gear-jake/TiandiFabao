package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.BaigujingModel;
import com.fabao.entity.BaigujingEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 白骨精渲染器。
 */
public class BaigujingRenderer extends MobEntityRenderer<BaigujingEntity, LivingEntityRenderState, BaigujingModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/baigujing.png");

	public BaigujingRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new BaigujingModel(ctx.getPart(BaigujingModel.LAYER)), 0.6f);
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
