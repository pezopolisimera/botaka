package dre.elfocrash.roboto.model;

public abstract class BBBBotSkill {
	protected int _skillId;
	protected int _level;
	protected SSSSpellUsageCondition _condition;
	protected int _conditionValue;
	protected int _priority;
	
	public BBBBotSkill(int skillId, SSSSpellUsageCondition condition, int conditionValue, int priority) {
	this(skillId, 1, condition, conditionValue, priority);
    }
	
   public BBBBotSkill(int skillId, int level, SSSSpellUsageCondition condition, int conditionValue, int priority) {
       _skillId = skillId;
       _level = level;
       _condition = condition;
       _conditionValue = conditionValue;
       _priority = priority;
   }
	public BBBBotSkill(int skillId) {
		 this(skillId, 1, SSSSpellUsageCondition.NONE, 0, 0);
	}
	   public int getLevel() {
		       return _level;
		   }
	   
	public int getSkillId() {
		return _skillId;
	}	

	public SSSSpellUsageCondition getCondition(){
		return _condition;
	}
	
	public int getConditionValue() {
		return _conditionValue;
	}
	
	public int getPriority() {
		return _priority;
	}
}
