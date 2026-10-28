package com.fabao.client.renderer;

import com.fabao.FabaoMod;
import com.fabao.client.model.JinjiaoDawangModel;
import com.fabao.entity.JinjiaoDawangEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * 金角大王渲染器。
 * 状态通过 vanilla 的 LivingEntityRenderState（pose / hurt）传递，不使用自定义 RenderState，
 * 避免与 Fabric 的 render state 扩展机制冲突。
 */
public class JinjiaoDawangRenderer extends MobEntityRenderer<JinjiaoDawangEntity, LivingEntityRenderState, JinjiaoDawangModel> {
	private static final Identifier TEXTURE = FabaoMod.id("textures/entity/jinjiao_dawang.png");

	public JinjiaoDawangRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new JinjiaoDawangModel(ctx.getPart(JinjiaoDawangModel.LAYER)), 0.7f);
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
