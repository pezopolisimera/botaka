package compvp.elfocrash.roboto.model;

public abstract class BBotSkill {
	protected int _skillId;
	protected SSpellUsageCondition _condition;
	protected int _conditionValue;
	protected int _priority;
	
	public BBotSkill(int skillId, SSpellUsageCondition condition, int conditionValue, int priority) {
		_skillId = skillId;
		_condition = condition;
		_conditionValue = conditionValue;
	}

	public BBotSkill(int skillId) {
		_skillId = skillId;
		_condition = SSpellUsageCondition.NONE;
		_conditionValue = 0;
		_priority = 0;
	}
	
	public int getSkillId() {
		return _skillId;
	}	

	public SSpellUsageCondition getCondition(){
		return _condition;
	}
	
	public int getConditionValue() {
		return _conditionValue;
	}
	
	public int getPriority() {
		return _priority;
	}
}
