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

/** Auto-generated from fox_demon.bbmodel: bone hierarchy + box UV. */
public class FoxDemonModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "fox_demon"), "main");

	private final ModelPart root, g_head, g_arm_l, g_arm_r, g_leg_l, g_leg_r, g_tail, g_body;

	public FoxDemonModel(ModelPart root) {
		super(root);
		this.root = root;
		this.g_head = root.getChild("g_head");
		this.g_arm_l = root.getChild("g_arm_l");
		this.g_arm_r = root.getChild("g_arm_r");
		this.g_leg_l = root.getChild("g_leg_l");
		this.g_leg_r = root.getChild("g_leg_r");
		this.g_tail = root.getChild("g_tail");
		this.g_body = root.getChild("g_body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData g_headData = rootData.addChild("g_head", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4f, -6f, -3f, 8f, 6f, 6f)
				.uv(150, 0).cuboid(-2f, -4f, -5f, 4f, 3f, 2f)
				.uv(192, 0).cuboid(-0.8f, -3.4f, -5.3f, 1.6f, 0.8f, 0.2f)
				.uv(198, 0).cuboid(-3f, -4.6f, -3.3f, 1f, 1f, 0.25f)
				.uv(202, 0).cuboid(2f, -4.6f, -3.3f, 1f, 1f, 0.25f)
				.uv(134, 0).cuboid(-4.2f, -10.5f, -2.2f, 2.2f, 4.5f, 2.2f)
				.uv(162, 0).cuboid(-3.8f, -9.6f, -1.9f, 1.4f, 3f, 1.5f)
				.uv(142, 0).cuboid(2f, -10.5f, -2.2f, 2.2f, 4.5f, 2.2f)
				.uv(168, 0).cuboid(2.4f, -9.6f, -1.9f, 1.4f, 3f, 1.5f)
				.uv(174, 0).cuboid(-4f, -6.5f, -3.4f, 8f, 0.9f, 0.6f)
				, ModelTransform.of(0f, 12f, -1f, 0f, 0f, 0f));

		ModelPartData g_arm_lData = rootData.addChild("g_arm_l", ModelPartBuilder.create()
				.uv(76, 0).cuboid(-1.5f, 0f, -1.4f, 3f, 6f, 2.8f)
				, ModelTransform.of(-4.5f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData g_arm_rData = rootData.addChild("g_arm_r", ModelPartBuilder.create()
				.uv(88, 0).cuboid(-1.5f, 0f, -1.4f, 3f, 6f, 2.8f)
				, ModelTransform.of(4.5f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData g_leg_lData = rootData.addChild("g_leg_l", ModelPartBuilder.create()
				.uv(100, 0).cuboid(-1.5f, 0f, -1.4f, 3f, 4f, 2.8f)
				, ModelTransform.of(-1.5f, 20f, 0f, 0f, 0f, 0f));

		ModelPartData g_leg_rData = rootData.addChild("g_leg_r", ModelPartBuilder.create()
				.uv(112, 0).cuboid(-1.5f, 0f, -1.4f, 3f, 4f, 2.8f)
				, ModelTransform.of(1.5f, 20f, 0f, 0f, 0f, 0f));

		ModelPartData g_tailData = rootData.addChild("g_tail", ModelPartBuilder.create()
				.uv(48, 0).cuboid(-1.5f, -6f, 0f, 3.2f, 6f, 4f)
				.uv(62, 0).cuboid(-0.9f, -10f, 3.6f, 3f, 6f, 3.8f)
				, ModelTransform.of(1.5f, 19f, 2f, 0f, 0f, 0f));

		ModelPartData g_bodyData = rootData.addChild("g_body", ModelPartBuilder.create()
				.uv(28, 0).cuboid(-3f, -8f, -2f, 6f, 8f, 4f)
				.uv(124, 0).cuboid(-2f, -5f, -2.6f, 4f, 4.8f, 0.6f)
				, ModelTransform.of(0f, 20f, 0f, 0f, 0f, 0f));


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
		this.g_arm_l.pitch = MathHelper.cos(swing * 0.6662f + (float)Math.PI) * 1.0f * amp;
		this.g_arm_r.pitch = MathHelper.cos(swing * 0.6662f) * 1.0f * amp;
		this.g_tail.roll = MathHelper.sin(age * 0.08f) * 0.25f;
		this.g_tail.pitch = MathHelper.sin(age * 0.05f) * 0.08f;
		this.g_body.pitch = (float)Math.toRadians(2.0f) * amp;
	}

	private static final int TEXTURE_W = 256;
	private static final int TEXTURE_H = 256;

	public ModelPart Head() { return this.g_head; }
	public ModelPart ArmL() { return this.g_arm_l; }
	public ModelPart ArmR() { return this.g_arm_r; }
	public ModelPart Lel() { return this.g_leg_l; }
	public ModelPart Ler() { return this.g_leg_r; }
	public ModelPart Tail() { return this.g_tail; }
	public ModelPart Body() { return this.g_body; }
}