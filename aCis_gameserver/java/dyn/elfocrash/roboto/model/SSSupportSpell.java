package dyn.elfocrash.roboto.model;

public class SSSupportSpell extends BBBotSkill {

	public SSSupportSpell(int skillId, SSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public SSSupportSpell(int skillId, int priority) {
		super(skillId, SSSpellUsageCondition.NONE, 0, priority);
	}
	
}
