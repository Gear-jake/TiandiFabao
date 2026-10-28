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

/** Auto-generated from jinjiao_dawang.bbmodel: bone hierarchy + box UV. */
public class JinjiaoDawangModel extends EntityModel<LivingEntityRenderState> {
	public static final net.minecraft.client.render.entity.model.EntityModelLayer LAYER =
			new net.minecraft.client.render.entity.model.EntityModelLayer(Identifier.of("fabao", "jinjiao_dawang"), "main");

	private final Animation idleAnim, walkAnim, attackAnim, specialAnim;
	private final ModelPart root, bone, boby, cape, armboneleft, armboneright, group, legboneleft, legboneright;

	public JinjiaoDawangModel(ModelPart root) {
		super(root);
		this.root = root;
		this.bone = root.getChild("bone");
		this.boby = root.getChild("boby");
		this.cape = root.getChild("cape");
		this.armboneleft = root.getChild("armboneleft");
		this.armboneright = root.getChild("armboneright");
		this.group = this.armboneright.getChild("group");
		this.idleAnim = JinjiaoDawangAnimations.IDLE.createAnimation(root);
		this.walkAnim = JinjiaoDawangAnimations.WALK.createAnimation(root);
		this.attackAnim = JinjiaoDawangAnimations.ATTACK.createAnimation(root);
		this.specialAnim = JinjiaoDawangAnimations.SPECIAL.createAnimation(root);
		this.legboneleft = root.getChild("legboneleft");
		this.legboneright = root.getChild("legboneright");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = new ModelData();
		ModelPartData rootData = data.getRoot();

		ModelPartData boneData = rootData.addChild("bone", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f)
				.uv(52, 45).cuboid(-4f, -11f, -2.5f, 3f, 3f, 3f)
				.uv(12, 54).cuboid(-5.2f, -13.2f, -3.2f, 2f, 2.2f, 2f)
				.uv(6, 60).cuboid(-6f, -14.4f, -3.9f, 1f, 1.2f, 1f)
				.uv(0, 54).cuboid(1f, -11f, -2.5f, 3f, 3f, 3f)
				.uv(20, 54).cuboid(3.2f, -13.2f, -3.2f, 2f, 2.2f, 2f)
				.uv(10, 60).cuboid(5f, -14.4f, -3.9f, 1f, 1.2f, 1f)
				.uv(36, 54).cuboid(-3f, -5f, -5f, 2f, 2f, 1f)
				.uv(42, 54).cuboid(1f, -5f, -5f, 2f, 2f, 1f)
				.uv(48, 54).cuboid(-3f, -6f, -5f, 6f, 1f, 1f)
				.uv(0, 60).cuboid(-1f, -2f, -5f, 2f, 1f, 1f)
				, ModelTransform.of(0f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData bobyData = rootData.addChild("boby", ModelPartBuilder.create()
				.uv(32, 0).cuboid(-4f, -12f, -2f, 8f, 12f, 4f)
				.uv(38, 45).cuboid(-3f, -9f, -3f, 6f, 6f, 1f)
				.uv(12, 45).cuboid(-4f, -2f, -2.5f, 8f, 2f, 5f)
				, ModelTransform.of(0f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData capeData = rootData.addChild("cape", ModelPartBuilder.create()
				.uv(0, 32).cuboid(-3.5f, 0f, -0.4f, 7f, 12.5f, 0.5f)
				, ModelTransform.of(0f, 1.5f, 3f, 0f, 0f, 0f));

		ModelPartData armboneleftData = rootData.addChild("armboneleft", ModelPartBuilder.create()
				.uv(0, 16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(16, 32).cuboid(-3f, 0f, -3f, 6f, 3f, 6f)
				, ModelTransform.of(-6f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData armbonerightData = rootData.addChild("armboneright", ModelPartBuilder.create()
				.uv(16, 16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				.uv(40, 32).cuboid(-3f, 0f, -3f, 6f, 3f, 6f)
				, ModelTransform.of(6f, 0f, 0f, 0f, 0f, 0f));

		ModelPartData groupData = armbonerightData.addChild("group", ModelPartBuilder.create()
				.uv(0, 45).cuboid(-1.5f, 0f, -1.5f, 3f, 6f, 3f)
				.uv(28, 54).cuboid(-1f, -2f, -1f, 2f, 2f, 2f)
				, ModelTransform.of(6.5f, 12f, 1f, 0f, 0f, 0f));

		ModelPartData legboneleftData = rootData.addChild("legboneleft", ModelPartBuilder.create()
				.uv(32, 16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				, ModelTransform.of(-2f, 12f, 0f, 0f, 0f, 0f));

		ModelPartData legbonerightData = rootData.addChild("legboneright", ModelPartBuilder.create()
				.uv(48, 16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f)
				, ModelTransform.of(2f, 12f, 0f, 0f, 0f, 0f));


		return TexturedModelData.of(data, TEXTURE_W, TEXTURE_H);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		// Animation.apply 是叠加式的，每帧必须先复位
		this.root.resetTransform();
		this.bone.resetTransform();
		this.boby.resetTransform();
		this.cape.resetTransform();
		this.armboneleft.resetTransform();
		this.armboneright.resetTransform();
		this.group.resetTransform();
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

	private static final int TEXTURE_W = 64;
	private static final int TEXTURE_H = 64;

	public ModelPart Bone() { return this.bone; }
	public ModelPart Boby() { return this.boby; }
	public ModelPart Cape() { return this.cape; }
	public ModelPart Armboneleft() { return this.armboneleft; }
	public ModelPart Armboneright() { return this.armboneright; }
	public ModelPart Group() { return this.group; }
	public ModelPart Legboneleft() { return this.legboneleft; }
	public ModelPart Legboneright() { return this.legboneright; }
}