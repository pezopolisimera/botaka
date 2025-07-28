package dyn.elfocrash.roboto.model;

public class OOOffensiveSpell extends BBBotSkill {	
	
	public OOOffensiveSpell (int skillId, SSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public OOOffensiveSpell (int skillId, int priority) {
		super(skillId, SSSpellUsageCondition.NONE, 0, priority);
	}
	
	public OOOffensiveSpell (int skillId) {
		super(skillId);
	}		
}
