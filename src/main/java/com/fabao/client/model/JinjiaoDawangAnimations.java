package com.fabao.client.model;

import net.minecraft.client.render.entity.animation.AnimationDefinition;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/** 金角大王的 4 个动画（由 Blockbench 关键帧生成，1.21.10 AnimationDefinition API）。 */
public final class JinjiaoDawangAnimations {

	private JinjiaoDawangAnimations() {
	}

	public static final AnimationDefinition IDLE = AnimationDefinition.Builder.create(2.4f)
			.looping()
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(3f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.2f, AnimationHelper.createRotationalVector(-3f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createRotationalVector(3f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.MOVE_ORIGIN,
					new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createTranslationalVector(0f, 0.8f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createTranslationalVector(0f, 0.8f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("bone", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, -10f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.8f, AnimationHelper.createRotationalVector(2f, 0f, 2f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.6f, AnimationHelper.createRotationalVector(0f, 10f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createRotationalVector(0f, -10f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("armboneleft", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 6f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.2f, AnimationHelper.createRotationalVector(0f, 0f, -6f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createRotationalVector(0f, 0f, 6f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("armboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -6f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.2f, AnimationHelper.createRotationalVector(0f, 0f, 6f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createRotationalVector(0f, 0f, -6f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("group", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.2f, AnimationHelper.createRotationalVector(12f, 0f, 8f), Transformation.Interpolations.LINEAR),
					new Keyframe(2.4f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.build();

	public static final AnimationDefinition WALK = AnimationDefinition.Builder.create(1.0f)
			.looping()
			.addBoneAnimation("legboneleft", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(28f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(-28f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(28f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("legboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(-28f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(28f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(-28f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("armboneleft", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(-22f, 0f, 4f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(22f, 0f, -4f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(-22f, 0f, 4f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("armboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(22f, 0f, -4f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(-22f, 0f, 4f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(22f, 0f, -4f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(4f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(4f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(4f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.MOVE_ORIGIN,
					new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 1.0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1.0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("bone", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(5f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(5f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("group", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -8f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 8f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(0f, 0f, -8f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("cape", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(-14f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.25f, AnimationHelper.createRotationalVector(-20f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.5f, AnimationHelper.createRotationalVector(-14f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.75f, AnimationHelper.createRotationalVector(-20f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.0f, AnimationHelper.createRotationalVector(-14f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.build();

	public static final AnimationDefinition ATTACK = AnimationDefinition.Builder.create(0.6f)
			.addBoneAnimation("armboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.16f, AnimationHelper.createRotationalVector(-60f, 0f, -10f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createRotationalVector(35f, 0f, 5f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.16f, AnimationHelper.createRotationalVector(-8f, 0f, -4f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createRotationalVector(6f, 0f, 3f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.MOVE_ORIGIN,
					new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.16f, AnimationHelper.createTranslationalVector(0f, 0.2f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createTranslationalVector(0f, -0.3f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("bone", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.16f, AnimationHelper.createRotationalVector(-4f, -6f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createRotationalVector(4f, 8f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("legboneleft", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createRotationalVector(6f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("legboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.34f, AnimationHelper.createRotationalVector(-6f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.6f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.build();

	public static final AnimationDefinition SPECIAL = AnimationDefinition.Builder.create(1.8f)
			.addBoneAnimation("armboneright", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(100f, 0f, -6f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(100f, 0f, -6f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("group", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(18f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(18f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(-6f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(-6f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("boby", new Transformation(Transformation.Targets.MOVE_ORIGIN,
					new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createTranslationalVector(0f, 0.15f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createTranslationalVector(0f, 0.15f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createTranslationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("bone", new Transformation(Transformation.Targets.ROTATE,
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(8f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(8f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(4f, 0f, 2f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(4f, 0f, 2f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR),
					new Keyframe(0.45f, AnimationHelper.createRotationalVector(4f, 0f, -2f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.4f, AnimationHelper.createRotationalVector(4f, 0f, -2f), Transformation.Interpolations.LINEAR),
					new Keyframe(1.8f, AnimationHelper.createRotationalVector(0f, 0f, 0f), Transformation.Interpolations.LINEAR)
			))
			.build();

}