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

/** Auto-generated from bear_demon.bbmodel: bone hierarchy + box UV. */
public class BearDemonModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "bear_demon"), "main");

	private final ModelPart root, g_body, g_head, g_arm_l, g_forearm_l, g_arm_r, g_forearm_r, g_leg_l, g_leg_r;

	public BearDemonModel(ModelPart root) {
		super(root);
		this.root = root;
		this.g_body = root.getChild("g_body");
		this.g_head = root.getChild("g_head");
		this.g_arm_l = root.getChild("g_arm_l");
		this.g_forearm_l = root.getChild("g_forearm_l");
		this.g_arm_r = root.getChild("g_arm_r");
		this.g_forearm_r = root.getChild("g_forearm_r");
		this.g_leg_l = root.getChild("g_leg_l");
		this.g_leg_r = root.getChild("g_leg_r");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData g_bodyData = rootData.addChild("g_body", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-5f, -5f, -4f, 10f, 10f, 8f)
				.uv(124, 0).cuboid(-3f, -1f, -4.3f, 6f, 5.5f, 0.3f)
				, ModelTransform.of(0f, 14f, 0f, 0f, 0f, 0f));

		ModelPartData g_headData = rootData.addChild("g_head", ModelPartBuilder.create()
				.uv(36, 0).cuboid(-4f, -7f, -3f, 8f, 7f, 6f)
				.uv(174, 0).cuboid(-2f, -4f, -4.5f, 4f, 3f, 1.5f)
				.uv(202, 0).cuboid(-1f, -4f, -5f, 2f, 1f, 0.5f)
				.uv(186, 0).cuboid(-4.5f, -9f, -1.5f, 2f, 2f, 2f)
				.uv(194, 0).cuboid(2.5f, -9f, -1.5f, 2f, 2f, 2f)
				.uv(208, 0).cuboid(-3f, -5.4f, -3.3f, 0.8f, 0.9f, 0.3f)
				.uv(212, 0).cuboid(2.2f, -5.4f, -3.3f, 0.8f, 0.9f, 0.3f)
				, ModelTransform.of(0f, 9f, -1f, 0f, 0f, 0f));

		ModelPartData g_arm_lData = rootData.addChild("g_arm_l", ModelPartBuilder.create()
				.uv(64, 0).cuboid(-1.5f, 0f, -2f, 3f, 9f, 4f)
				, ModelTransform.of(-6.5f, 8f, 0f, 0f, 0f, 0f));

		ModelPartData g_forearm_lData = rootData.addChild("g_forearm_l", ModelPartBuilder.create()
				.uv(138, 0).cuboid(-1.9f, 0.1f, -2.4f, 3.8f, 1.4f, 4.8f)
				, ModelTransform.of(-6.5f, 16.5f, 0f, 0f, 0f, 0f));

		ModelPartData g_arm_rData = rootData.addChild("g_arm_r", ModelPartBuilder.create()
				.uv(78, 0).cuboid(-1.5f, 0f, -2f, 3f, 9f, 4f)
				, ModelTransform.of(6.5f, 8f, 0f, 0f, 0f, 0f));

		ModelPartData g_forearm_rData = rootData.addChild("g_forearm_r", ModelPartBuilder.create()
				.uv(156, 0).cuboid(-1.9f, 0.1f, -2.4f, 3.8f, 1.4f, 4.8f)
				, ModelTransform.of(6.5f, 16.5f, 0f, 0f, 0f, 0f));

		ModelPartData g_leg_lData = rootData.addChild("g_leg_l", ModelPartBuilder.create()
				.uv(92, 0).cuboid(-1.8f, -5f, -2f, 3.5f, 5f, 4f)
				, ModelTransform.of(-3.2f, 24f, 0f, 0f, 0f, 0f));

		ModelPartData g_leg_rData = rootData.addChild("g_leg_r", ModelPartBuilder.create()
				.uv(108, 0).cuboid(-1.7f, -5f, -2f, 3.5f, 5f, 4f)
				, ModelTransform.of(3.2f, 24f, 0f, 0f, 0f, 0f));


		return TexturedModelData.of(data, TEXTURE_W, TEXTURE_H);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		float swing = state.limbSwingAnimationProgress;
		float amp = state.limbSwingAmplitude;
		float age = state.age;
		float headYawRad = state.relativeHeadYaw * 0.017453292f;
		float headPitchRad = state.pitch * 0.017453292f;
		this.g_body.pitch = (float)Math.toRadians(2.0f) * amp;
		this.g_head.yaw = headYawRad;
		this.g_head.pitch = headPitchRad;
		this.g_arm_l.pitch = MathHelper.cos(swing * 0.6662f + (float)Math.PI) * 1.0f * amp;
		this.g_forearm_l.pitch = MathHelper.cos(swing * 0.6662f) * 0.6f * amp;
		this.g_arm_r.pitch = MathHelper.cos(swing * 0.6662f) * 1.0f * amp;
		this.g_forearm_r.pitch = MathHelper.cos(swing * 0.6662f) * 0.6f * amp;
	}

	private static final int TEXTURE_W = 256;
	private static final int TEXTURE_H = 256;

	public ModelPart Body() { return this.g_body; }
	public ModelPart Head() { return this.g_head; }
	public ModelPart ArmL() { return this.g_arm_l; }
	public ModelPart ForearmL() { return this.g_forearm_l; }
	public ModelPart ArmR() { return this.g_arm_r; }
	public ModelPart ForearmR() { return this.g_forearm_r; }
	public ModelPart Lel() { return this.g_leg_l; }
	public ModelPart Ler() { return this.g_leg_r; }
}