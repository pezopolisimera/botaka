package dre.elfocrash.roboto.model;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

public class HHHHealingSpell extends BBBBotSkill {

    private SkillTargetType _targetType;
    private int _skillLevel = 1;

    // Constructor με custom usage condition
    public HHHHealingSpell(int skillId, SkillTargetType targetType, SSSSpellUsageCondition condition, int conditionValue, int priority, int level) {
        super(skillId, condition, conditionValue, priority);
        _targetType = targetType;
        _skillLevel = level;
    }

    // Constructor για default healing spell με level
    public HHHHealingSpell(int skillId, SkillTargetType targetType, int conditionValue, int priority, int level) {
        super(skillId, SSSSpellUsageCondition.LESSHPPERCENT, conditionValue, priority);
        _targetType = targetType;
        _skillLevel = level;
    }

    // Constructor χωρίς level ➤ default = 1
    public HHHHealingSpell(int skillId, SkillTargetType targetType, int conditionValue, int priority) {
        this(skillId, targetType, conditionValue, priority, 1);
    }

    public SkillTargetType getTargetType() {
        return _targetType;
    }

    public int getSkillLevel() {
        return _skillLevel;
    }
}
