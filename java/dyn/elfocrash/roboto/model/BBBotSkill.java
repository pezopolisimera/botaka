package dyn.elfocrash.roboto.model;

public abstract class BBBotSkill {
	protected int _skillId;
	protected SSSpellUsageCondition _condition;
	protected int _conditionValue;
	protected int _priority;
	
	public BBBotSkill(int skillId, SSSpellUsageCondition condition, int conditionValue, int priority) {
		_skillId = skillId;
		_condition = condition;
		_conditionValue = conditionValue;
	}

	public BBBotSkill(int skillId) {
		_skillId = skillId;
		_condition = SSSpellUsageCondition.NONE;
		_conditionValue = 0;
		_priority = 0;
	}
	
	public int getSkillId() {
		return _skillId;
	}	

	public SSSpellUsageCondition getCondition(){
		return _condition;
	}
	
	public int getConditionValue() {
		return _conditionValue;
	}
	
	public int getPriority() {
		return _priority;
	}
}
