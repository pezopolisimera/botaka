package dyn.elfocrash.roboto.model;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

public class HHHealingSpell extends BBBotSkill {
	
	private SkillTargetType _targetType;
	
	public HHHealingSpell (int skillId, SkillTargetType targetType, SSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);		
		_targetType = targetType;	
	}
	
	public HHHealingSpell (int skillId, SkillTargetType targetType, int conditionValue, int priority) {
		super(skillId, SSSpellUsageCondition.LESSHPPERCENT, conditionValue, priority);
		_targetType = targetType;	
	}
	
	public SkillTargetType getTargetType() {
		return _targetType;
	}
}
