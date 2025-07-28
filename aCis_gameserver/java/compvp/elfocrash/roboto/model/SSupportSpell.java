package compvp.elfocrash.roboto.model;

public class SSupportSpell extends BBotSkill {

	public SSupportSpell(int skillId, SSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public SSupportSpell(int skillId, int priority) {
		super(skillId, SSpellUsageCondition.NONE, 0, priority);
	}
	
}
