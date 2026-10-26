package com.fabao.item;

/**
 * 法宝参数表：一件法宝一行。主动数值、灵耗、常驻被动属性、「对妖特攻」倍率与武器属性都集中在这里。
 * 兵器型法宝（金箍棒/斩妖刀/降妖杵/定海神针）可手持当武器挥砍；法器型（其余）为纯饰品。
 */
public enum FabaoType {
	QIANKUN_QUAN("qiankun_quan", 10.0f, 20, Passive.ATTACK_SPEED, 0.0f, false, 0.0f, 0.0f),
	HUNTIAN_LING("huntian_ling", 8.0f, 25, Passive.MOVE_SPEED, 0.0f, false, 0.0f, 0.0f),
	RUYI_JINGU_BANG("ruyi_jingu_bang", 16.0f, 30, Passive.ATTACK_DAMAGE, 0.0f, true, 8.0f, -2.6f),
	DASHEN_BIAN("dashen_bian", 14.0f, 40, Passive.MAX_HEALTH, 0.0f, false, 0.0f, 0.0f),
	ZIJIN_HULU("zijin_hulu", 10.0f, 45, Passive.ARMOR, 0.0f, false, 0.0f, 0.0f),
	JINGANG_ZHUO("jingang_zhuo", 12.0f, 60, Passive.KNOCKBACK_RESIST, 0.0f, false, 0.0f, 0.0f),

	// 妖丹法宝：小妖丹造斩妖刀（打大妖）、大妖丹造降妖杵（打妖王）、妖王丹造定海神针（终极）
	ZHANYAO_DAO("zhanyao_dao", 12.0f, 25, Passive.ATTACK_DAMAGE, 1.5f, true, 5.0f, -1.6f),
	JIANGYAO_CHU("jiangyao_chu", 16.0f, 35, Passive.MAX_HEALTH, 2.5f, true, 10.0f, -3.2f),
	DINGHAI_SHENZHEN("dinghai_shenzhen", 22.0f, 50, Passive.KNOCKBACK_RESIST, 4.0f, true, 12.0f, -2.8f);

	private final String id;
	private final float baseDamage;
	private final int spiritCost;
	private final Passive passive;
	private final float yaoBonus;
	private final boolean weapon;
	private final float weaponDamage;
	private final float weaponSpeed;

	FabaoType(String id, float baseDamage, int spiritCost, Passive passive, float yaoBonus,
			  boolean weapon, float weaponDamage, float weaponSpeed) {
		this.id = id;
		this.baseDamage = baseDamage;
		this.spiritCost = spiritCost;
		this.passive = passive;
		this.yaoBonus = yaoBonus;
		this.weapon = weapon;
		this.weaponDamage = weaponDamage;
		this.weaponSpeed = weaponSpeed;
	}

	public String id() {
		return id;
	}

	public float baseDamage() {
		return baseDamage;
	}

	public int spiritCost() {
		return spiritCost;
	}

	public Passive passive() {
		return passive;
	}

	/** 对妖伤害加成倍率（0 = 无加成，1.5 = ×2.5，4 = ×5）。 */
	public float yaoBonus() {
		return yaoBonus;
	}

	public boolean isYaoSlayer() {
		return yaoBonus > 0.0f;
	}

	/** 是否可手持当武器挥砍。 */
	public boolean isWeapon() {
		return weapon;
	}

	public float weaponDamage() {
		return weaponDamage;
	}

	public float weaponSpeed() {
		return weaponSpeed;
	}

	/**
	 * 常驻被动主属性。attributePath 是原版属性注册表路径，
	 * value 为 0 阶基准值；multiplicative=true 时按 ADD_MULTIPLIED_BASE 理解。
	 */
	public enum Passive {
		ATTACK_SPEED("attack_speed", 0.30, false),
		MOVE_SPEED("movement_speed", 0.15, true),
		ATTACK_DAMAGE("attack_damage", 4.0, false),
		MAX_HEALTH("max_health", 8.0, false),
		ARMOR("armor", 4.0, false),
		KNOCKBACK_RESIST("knockback_resistance", 0.4, false);

		private final String attributePath;
		private final double value;
		private final boolean multiplicative;

		Passive(String attributePath, double value, boolean multiplicative) {
			this.attributePath = attributePath;
			this.value = value;
			this.multiplicative = multiplicative;
		}

		public String attributePath() {
			return attributePath;
		}

		public double value() {
			return value;
		}

		public boolean multiplicative() {
			return multiplicative;
		}
	}
}
