package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.YinjiaoDawangModel;
import com.fabao.entity.YinjiaoDawangEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 银角大王渲染器。
 */
public class YinjiaoDawangRenderer extends MobEntityRenderer<YinjiaoDawangEntity, LivingEntityRenderState, YinjiaoDawangModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/yinjiao_dawang.png");

	public YinjiaoDawangRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new YinjiaoDawangModel(ctx.getPart(YinjiaoDawangModel.LAYER)), 0.7f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTexture(LivingEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	protected void scale(LivingEntityRenderState state, net.minecraft.client.util.math.MatrixStack matrices) {
		matrices.scale(1.15f, 1.15f, 1.15f);
	}
}
