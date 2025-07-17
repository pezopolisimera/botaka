package compvp.elfocrash.roboto.model;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

public class HHealingSpell extends BBotSkill {
	
	private SkillTargetType _targetType;
	
	public HHealingSpell (int skillId, SkillTargetType targetType, SSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);		
		_targetType = targetType;	
	}
	
	public HHealingSpell (int skillId, SkillTargetType targetType, int conditionValue, int priority) {
		super(skillId, SSpellUsageCondition.LESSHPPERCENT, conditionValue, priority);
		_targetType = targetType;	
	}
	
	public SkillTargetType getTargetType() {
		return _targetType;
	}
}
