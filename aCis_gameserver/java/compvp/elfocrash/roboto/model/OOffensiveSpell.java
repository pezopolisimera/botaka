package compvp.elfocrash.roboto.model;

public class OOffensiveSpell extends BBotSkill {	
	
	public OOffensiveSpell (int skillId, SSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public OOffensiveSpell (int skillId, int priority) {
		super(skillId, SSpellUsageCondition.NONE, 0, priority);
	}
	
	public OOffensiveSpell (int skillId) {
		super(skillId);
	}		
}
