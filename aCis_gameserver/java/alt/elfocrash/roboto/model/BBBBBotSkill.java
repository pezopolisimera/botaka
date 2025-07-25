package alt.elfocrash.roboto.model;

public abstract class BBBBBotSkill {
	protected int _skillId;
	protected SSSSSpellUsageCondition _condition;
	protected int _conditionValue;
	protected int _priority;
	
	public BBBBBotSkill(int skillId, SSSSSpellUsageCondition condition, int conditionValue, int priority) {
		_skillId = skillId;
		_condition = condition;
		_conditionValue = conditionValue;
	}

	public BBBBBotSkill(int skillId) {
		_skillId = skillId;
		_condition = SSSSSpellUsageCondition.NONE;
		_conditionValue = 0;
		_priority = 0;
	}
	
	public int getSkillId() {
		return _skillId;
	}	

	public SSSSSpellUsageCondition getCondition(){
		return _condition;
	}
	
	public int getConditionValue() {
		return _conditionValue;
	}
	
	public int getPriority() {
		return _priority;
	}
}
