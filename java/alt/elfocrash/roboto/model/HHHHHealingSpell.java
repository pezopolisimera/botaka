package alt.elfocrash.roboto.model;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

public class HHHHHealingSpell extends BBBBBotSkill {
	
	private SkillTargetType _targetType;
	
	public HHHHHealingSpell (int skillId, SkillTargetType targetType, SSSSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);		
		_targetType = targetType;	
	}
	
	public HHHHHealingSpell (int skillId, SkillTargetType targetType, int conditionValue, int priority) {
		super(skillId, SSSSSpellUsageCondition.LESSHPPERCENT, conditionValue, priority);
		_targetType = targetType;	
	}
	
	public SkillTargetType getTargetType() {
		return _targetType;
	}
}
