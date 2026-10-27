package com.fabao.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/** Auto-generated from spider_demon.bbmodel: bone hierarchy + box UV. */
public class SpiderDemonModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "spider_demon"), "main");

	private final ModelPart root, g_head, g_thorax, g_abdomen, g_leg_l1, g_leg_l2, g_leg_l3, g_leg_l4, g_leg_r1, g_leg_r2, g_leg_r3, g_leg_r4;

	public SpiderDemonModel(ModelPart root) {
		super(root);
		this.root = root;
		this.g_head = root.getChild("g_head");
		this.g_thorax = root.getChild("g_thorax");
		this.g_abdomen = root.getChild("g_abdomen");
		this.g_leg_l1 = root.getChild("g_leg_l1");
		this.g_leg_l2 = root.getChild("g_leg_l2");
		this.g_leg_l3 = root.getChild("g_leg_l3");
		this.g_leg_l4 = root.getChild("g_leg_l4");
		this.g_leg_r1 = root.getChild("g_leg_r1");
		this.g_leg_r2 = root.getChild("g_leg_r2");
		this.g_leg_r3 = root.getChild("g_leg_r3");
		this.g_leg_r4 = root.getChild("g_leg_r4");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData g_headData = rootData.addChild("g_head", ModelPartBuilder.create()
				.uv(48, 0).cuboid(-4f, -3f, -4f, 8f, 6f, 8f)
				.uv(36, 21).cuboid(-3f, -1.4f, -4.4f, 1.4f, 1.4f, 0.4f)
				.uv(40, 21).cuboid(1.6f, -1.4f, -4.4f, 1.4f, 1.4f, 0.4f)
				.uv(44, 21).cuboid(-3.4f, 0.4f, -4.4f, 1.4f, 1.4f, 0.4f)
				.uv(48, 21).cuboid(2f, 0.4f, -4.4f, 1.4f, 1.4f, 0.4f)
				.uv(52, 21).cuboid(-1.6f, 2.6f, -4.2f, 1f, 1.2f, 0.6f)
				.uv(56, 21).cuboid(0.6f, 2.6f, -4.2f, 1f, 1.2f, 0.6f)
				, ModelTransform.of(0f, 16f, -10f, 0f, 0f, 0f));

		ModelPartData g_thoraxData = rootData.addChild("g_thorax", ModelPartBuilder.create()
				.uv(80, 0).cuboid(-4f, -2f, -4f, 8f, 4f, 8f)
				, ModelTransform.of(0f, 17f, -2f, 0f, 0f, 0f));

		ModelPartData g_abdomenData = rootData.addChild("g_abdomen", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-6f, -5f, -6f, 12f, 9f, 12f)
				.uv(160, 0).cuboid(-3f, -5.5f, -4f, 6f, 1f, 6f)
				, ModelTransform.of(0f, 16f, 8f, 0f, 0f, 0f));

		ModelPartData g_leg_l1Data = rootData.addChild("g_leg_l1", ModelPartBuilder.create()
				.uv(200, 0).cuboid(-4f, 0f, 0f, 4f, 1f, 2f)
				.uv(112, 0).cuboid(-6f, 0f, 0f, 2f, 8f, 2f)
				, ModelTransform.of(-4f, 16f, -6f, 0f, 0f, 0f));

		ModelPartData g_leg_l2Data = rootData.addChild("g_leg_l2", ModelPartBuilder.create()
				.uv(0, 21).cuboid(-3f, -1f, 0f, 3f, 1f, 2f)
				.uv(144, 0).cuboid(-5f, -1f, 0f, 2f, 6f, 2f)
				, ModelTransform.of(-4f, 17f, -3f, 0f, 0f, 0f));

		ModelPartData g_leg_l3Data = rootData.addChild("g_leg_l3", ModelPartBuilder.create()
				.uv(20, 21).cuboid(-2.5f, -1f, -1f, 2.5f, 1f, 2f)
				.uv(184, 0).cuboid(-4.5f, -1f, -1f, 2f, 5f, 2f)
				, ModelTransform.of(-4f, 17f, 1f, 0f, 0f, 0f));

		ModelPartData g_leg_l4Data = rootData.addChild("g_leg_l4", ModelPartBuilder.create()
				.uv(212, 0).cuboid(-3.5f, -2f, -2f, 3.5f, 1f, 2f)
				.uv(128, 0).cuboid(-5.5f, -2f, -2f, 2f, 7f, 2f)
				, ModelTransform.of(-4f, 18f, 5f, 0f, 0f, 0f));

		ModelPartData g_leg_r1Data = rootData.addChild("g_leg_r1", ModelPartBuilder.create()
				.uv(224, 0).cuboid(0f, 0f, 0f, 4f, 1f, 2f)
				.uv(120, 0).cuboid(4f, 0f, 0f, 2f, 8f, 2f)
				, ModelTransform.of(4f, 16f, -6f, 0f, 0f, 0f));

		ModelPartData g_leg_r2Data = rootData.addChild("g_leg_r2", ModelPartBuilder.create()
				.uv(10, 21).cuboid(0f, -1f, 0f, 3f, 1f, 2f)
				.uv(152, 0).cuboid(3f, -1f, 0f, 2f, 6f, 2f)
				, ModelTransform.of(4f, 17f, -3f, 0f, 0f, 0f));

		ModelPartData g_leg_r3Data = rootData.addChild("g_leg_r3", ModelPartBuilder.create()
				.uv(28, 21).cuboid(0f, -1f, -1f, 2.5f, 1f, 2f)
				.uv(192, 0).cuboid(2.5f, -1f, -1f, 2f, 5f, 2f)
				, ModelTransform.of(4f, 17f, 1f, 0f, 0f, 0f));

		ModelPartData g_leg_r4Data = rootData.addChild("g_leg_r4", ModelPartBuilder.create()
				.uv(236, 0).cuboid(0f, -2f, -2f, 3.5f, 1f, 2f)
				.uv(136, 0).cuboid(3.5f, -2f, -2f, 2f, 7f, 2f)
				, ModelTransform.of(4f, 18f, 5f, 0f, 0f, 0f));


		return TexturedModelData.of(data, TEXTURE_W, TEXTURE_H);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		float swing = state.limbSwingAnimationProgress;
		float amp = state.limbSwingAmplitude;
		float age = state.age;
		float headYawRad = state.relativeHeadYaw * 0.017453292f;
		float headPitchRad = state.pitch * 0.017453292f;
		this.g_head.yaw = headYawRad;
		this.g_head.pitch = headPitchRad;
		this.g_thorax.pitch = (float)Math.toRadians(2.0f) * amp;
		this.g_abdomen.roll = MathHelper.sin(age * 0.08f) * 0.25f;
		this.g_abdomen.pitch = MathHelper.sin(age * 0.05f) * 0.08f;
	}

	private static final int TEXTURE_W = 256;
	private static final int TEXTURE_H = 256;

	public ModelPart Head() { return this.g_head; }
	public ModelPart Thorax() { return this.g_thorax; }
	public ModelPart Abdomen() { return this.g_abdomen; }
	public ModelPart Lel1() { return this.g_leg_l1; }
	public ModelPart Lel2() { return this.g_leg_l2; }
	public ModelPart Lel3() { return this.g_leg_l3; }
	public ModelPart Lel4() { return this.g_leg_l4; }
	public ModelPart Ler1() { return this.g_leg_r1; }
	public ModelPart Ler2() { return this.g_leg_r2; }
	public ModelPart Ler3() { return this.g_leg_r3; }
	public ModelPart Ler4() { return this.g_leg_r4; }
}