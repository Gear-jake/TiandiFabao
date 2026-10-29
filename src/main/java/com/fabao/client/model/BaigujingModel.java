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
import net.minecraft.entity.EntityPose;
import net.minecraft.client.render.entity.animation.Animation;

/** Auto-generated from baigujing.bbmodel: bone hierarchy + box UV. */
public class BaigujingModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "baigujing"), "main");

	private final Animation idleAnim, walkAnim, attackAnim, specialAnim;
	private final ModelPart root, bone, boby, skirt, armboneleft, armboneright, legboneleft, legboneright;

	public BaigujingModel(ModelPart root) {
		super(root);
		this.root = root;
		this.bone = root.getChild("bone");
		this.boby = root.getChild("boby");
		this.skirt = root.getChild("skirt");
		this.armboneleft = root.getChild("armboneleft");
		this.armboneright = root.getChild("armboneright");
		this.legboneleft = root.getChild("legboneleft");
		this.legboneright = root.getChild("legboneright");
		this.idleAnim = BaigujingAnimations.IDLE.createAnimation(root);
		this.walkAnim = BaigujingAnimations.WALK.createAnimation(root);
		this.attackAnim = BaigujingAnimations.ATTACK.createAnimation(root);
		this.specialAnim = BaigujingAnimations.SPECIAL.createAnimation(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData boneData = rootData.addChild("bone", ModelPartBuilder.create()
				.uv(44, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f)
				.uv(72, 31).cuboid(-3f, -1.5f, -4.5f, 6f, 1.5f, 0.5f)
				.uv(86, 31).cuboid(-3f, -5f, -4.5f, 2f, 2f, 0.5f)
				.uv(92, 31).cuboid(1f, -5f, -4.5f, 2f, 2f, 0.5f)
				.uv(44, 38).cuboid(-2.5f, -4.5f, -4.6f, 1f, 1f, 0.1f)
				.uv(48, 38).cuboid(1.5f, -4.5f, -4.6f, 1f, 1f, 0.1f)
				.uv(104, 31).cuboid(-3.2f, -5.2f, -4.35f, 2.2f, 2.4f, 0.05f)
				.uv(110, 31).cuboid(1f, -5.2f, -4.35f, 2.2f, 2.4f, 0.05f)
				.uv(52, 38).cuboid(-0.5f, -2.6f, -4.5f, 1f, 0.8f, 0.5f)
				.uv(18, 38).cuboid(-3.5f, -6.2f, -4.4f, 7f, 0.8f, 0.4f)
				.uv(60, 38).cuboid(-3.9f, -3f, -4.4f, 1.2f, 1.4f, 0.4f)
				.uv(64, 38).cuboid(2.7f, -3f, -4.4f, 1.2f, 1.4f, 0.4f)
				.uv(68, 38).cuboid(-2f, -1f, -4.5f, 1f, 0.8f, 0.1f)
				.uv(72, 38).cuboid(-0.6f, -1f, -4.5f, 1.2f, 0.8f, 0.1f)
				.uv(76, 38).cuboid(1f, -1f, -4.5f, 1f, 0.8f, 0.1f)
				.uv(60, 31).cuboid(-4.3f, -4.5f, -2.5f, 0.3f, 1.5f, 1.5f)
				.uv(66, 31).cuboid(4f, -4.5f, -2.5f, 0.3f, 1.5f, 1.5f)
				.uv(92, 18).cuboid(-2.5f, -11f, -2.5f, 5f, 3f, 5f)
				.uv(0, 38).cuboid(-4f, -9.8f, -2f, 8f, 0.6f, 0.6f)
				.uv(56, 38).cuboid(-4.5f, -10f, -2.3f, 0.5f, 1f, 1.2f)
				, ModelTransform.of(0f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData bobyData = rootData.addChild("boby", ModelPartBuilder.create()
				.uv(76, 0).cuboid(-3f, -12f, -2f, 6f, 12f, 4f)
				.uv(68, 18).cuboid(-3.5f, -1f, -2.5f, 7f, 3f, 5f)
				.uv(0, 31).cuboid(-3.5f, -2f, -2.5f, 7f, 2f, 5f)
				.uv(98, 31).cuboid(-1f, -1.8f, -3f, 2f, 1.6f, 0.5f)
				.uv(24, 31).cuboid(-2.5f, -9f, -2.5f, 5f, 5f, 0.5f)
				.uv(34, 38).cuboid(-2f, -6.5f, -2.6f, 4f, 1f, 0.1f)
				, ModelTransform.of(0f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData skirtData = rootData.addChild("skirt", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-7f, 0f, -4f, 14f, 10f, 8f)
				.uv(20, 18).cuboid(-7.5f, 8.5f, -4.5f, 15f, 1.5f, 9f)
				, ModelTransform.of(0f, 14f, 0f, 0f, 0f, 0f));

		ModelPartData armboneleftData = rootData.addChild("armboneleft", ModelPartBuilder.create()
				.uv(96, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(36, 31).cuboid(-1.5f, 12f, -1.5f, 3f, 3f, 3f)
				, ModelTransform.of(-5f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData armbonerightData = rootData.addChild("armboneright", ModelPartBuilder.create()
				.uv(112, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(48, 31).cuboid(-1.5f, 12f, -1.5f, 3f, 3f, 3f)
				, ModelTransform.of(5f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData legboneleftData = rootData.addChild("legboneleft", ModelPartBuilder.create()
				.uv(0, 18).cuboid(-1f, 0f, -1.5f, 2f, 10f, 3f)
				, ModelTransform.of(-2f, 14f, 0f, 0f, 0f, 0f));

		ModelPartData legbonerightData = rootData.addChild("legboneright", ModelPartBuilder.create()
				.uv(10, 18).cuboid(-1f, 0f, -1.5f, 2f, 10f, 3f)
				, ModelTransform.of(2f, 14f, 0f, 0f, 0f, 0f));


		return TexturedModelData.of(data, TEXTURE_W, TEXTURE_H);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		this.root.resetTransform();
		this.bone.resetTransform();
		this.boby.resetTransform();
		this.skirt.resetTransform();
		this.armboneleft.resetTransform();
		this.armboneright.resetTransform();
		this.legboneleft.resetTransform();
		this.legboneright.resetTransform();

		long time = (long) (state.age * 50.0f);
		if (state.pose == EntityPose.LONG_JUMPING) {
			this.specialAnim.apply(time % 2000L, 1.0f);
		} else if (state.hurt) {
			this.attackAnim.apply(time % 600L, 1.0f);
		} else if (state.limbSwingAmplitude > 0.05f) {
			this.walkAnim.apply(time, 1.0f);
		} else {
			this.idleAnim.apply(time, 1.0f);
		}
	}

	private static final int TEXTURE_W = 128;
	private static final int TEXTURE_H = 128;

	public ModelPart Bone() { return this.bone; }
	public ModelPart Boby() { return this.boby; }
	public ModelPart Skirt() { return this.skirt; }
	public ModelPart Armboneleft() { return this.armboneleft; }
	public ModelPart Armboneright() { return this.armboneright; }
	public ModelPart Legboneleft() { return this.legboneleft; }
	public ModelPart Legboneright() { return this.legboneright; }
}