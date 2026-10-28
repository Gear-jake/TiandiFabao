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

/** Auto-generated from yinjiao_dawang.bbmodel: bone hierarchy + box UV. */
public class YinjiaoDawangModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "yinjiao_dawang"), "main");

	private final Animation idleAnim, walkAnim, attackAnim, specialAnim;
	private final ModelPart root, bone, boby, cape, armboneleft, vase, armboneright, legboneleft, legboneright;

	public YinjiaoDawangModel(ModelPart root) {
		super(root);
		this.root = root;
		this.bone = root.getChild("bone");
		this.boby = root.getChild("boby");
		this.cape = root.getChild("cape");
		this.armboneleft = root.getChild("armboneleft");
		this.vase = this.armboneleft.getChild("vase");
		this.idleAnim = YinjiaoDawangAnimations.IDLE.createAnimation(root);
		this.walkAnim = YinjiaoDawangAnimations.WALK.createAnimation(root);
		this.attackAnim = YinjiaoDawangAnimations.ATTACK.createAnimation(root);
		this.specialAnim = YinjiaoDawangAnimations.SPECIAL.createAnimation(root);
		this.armboneright = root.getChild("armboneright");
		this.legboneleft = root.getChild("legboneleft");
		this.legboneright = root.getChild("legboneright");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData boneData = rootData.addChild("bone", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f)
				.uv(116, 16).cuboid(-3.5f, -11f, -2f, 3f, 3f, 3f)
				.uv(12, 29).cuboid(-4.2f, -13f, -2.5f, 2f, 2f, 2f)
				.uv(74, 29).cuboid(-4.6f, -14f, -3f, 1f, 1f, 1f)
				.uv(0, 29).cuboid(0.5f, -11f, -2f, 3f, 3f, 3f)
				.uv(20, 29).cuboid(2.2f, -13f, -2.5f, 2f, 2f, 2f)
				.uv(78, 29).cuboid(3.6f, -14f, -3f, 1f, 1f, 1f)
				.uv(60, 29).cuboid(-3f, -6f, -5f, 6f, 1f, 1f)
				.uv(42, 29).cuboid(-3f, -5f, -5f, 2f, 2f, 1f)
				.uv(48, 29).cuboid(1f, -5f, -5f, 2f, 2f, 1f)
				, ModelTransform.of(0f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData bobyData = rootData.addChild("boby", ModelPartBuilder.create()
				.uv(32, 0).cuboid(-4f, -12f, -2f, 8f, 12f, 4f)
				.uv(102, 16).cuboid(-3f, -9f, -3f, 6f, 6f, 1f)
				.uv(76, 16).cuboid(-4f, -2f, -2.5f, 8f, 2f, 5f)
				.uv(54, 29).cuboid(-1f, -2f, -3f, 2f, 2f, 1f)
				, ModelTransform.of(0f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData capeData = rootData.addChild("cape", ModelPartBuilder.create()
				.uv(0, 16).cuboid(-3.5f, 0f, -1f, 7f, 12.5f, 0.5f)
				, ModelTransform.of(0f, 1.5f, 3f, 0f, 0f, 0f));

		ModelPartData armboneleftData = rootData.addChild("armboneleft", ModelPartBuilder.create()
				.uv(56, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(16, 16).cuboid(-3f, 0f, -3f, 6f, 3f, 6f)
				, ModelTransform.of(-6f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData vaseData = armboneleftData.addChild("vase", ModelPartBuilder.create()
				.uv(64, 16).cuboid(-1.5f, 0f, -1.5f, 3f, 6f, 3f)
				.uv(28, 29).cuboid(-1f, -2f, -1f, 2f, 2f, 2f)
				.uv(36, 29).cuboid(-0.5f, -4f, -1f, 1f, 2f, 2f)
				, ModelTransform.of(0.5f, 12f, 1f, 0f, 0f, 0f));

		ModelPartData armbonerightData = rootData.addChild("armboneright", ModelPartBuilder.create()
				.uv(72, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(40, 16).cuboid(-3f, 0f, -3f, 6f, 3f, 6f)
				, ModelTransform.of(6f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData legboneleftData = rootData.addChild("legboneleft", ModelPartBuilder.create()
				.uv(88, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				, ModelTransform.of(-2f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData legbonerightData = rootData.addChild("legboneright", ModelPartBuilder.create()
				.uv(104, 0).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				, ModelTransform.of(2f, 12f, 0f, 0f, 0f, 0f));


		return TexturedModelData.of(data, TEXTURE_W, TEXTURE_H);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		this.root.resetTransform();
		this.bone.resetTransform();
		this.boby.resetTransform();
		this.cape.resetTransform();
		this.armboneleft.resetTransform();
		this.armboneright.resetTransform();
		this.vase.resetTransform();
		this.legboneleft.resetTransform();
		this.legboneright.resetTransform();

		long time = (long) (state.age * 50.0f);
		if (state.pose == EntityPose.LONG_JUMPING) {
			this.specialAnim.apply(time % 1800L, 1.0f);
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
	public ModelPart Cape() { return this.cape; }
	public ModelPart Armboneleft() { return this.armboneleft; }
	public ModelPart Vase() { return this.vase; }
	public ModelPart Armboneright() { return this.armboneright; }
	public ModelPart Legboneleft() { return this.legboneleft; }
	public ModelPart Legboneright() { return this.legboneright; }
}