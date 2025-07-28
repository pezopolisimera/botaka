package alt.elfocrash.roboto.model;

public class OOOOOffensiveSpell extends BBBBBotSkill {	
	
	public OOOOOffensiveSpell (int skillId, SSSSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public OOOOOffensiveSpell (int skillId, int priority) {
		super(skillId, SSSSSpellUsageCondition.NONE, 0, priority);
	}
	
	public OOOOOffensiveSpell (int skillId) {
		super(skillId);
	}		
}
