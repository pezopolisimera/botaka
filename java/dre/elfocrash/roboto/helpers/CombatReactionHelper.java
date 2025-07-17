package dre.elfocrash.roboto.helpers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import dre.elfocrash.roboto.FFFFakePlayer;

import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public class CombatReactionHelper {

    public static void reactToAttack(FFFFakePlayer bot, Player attacker) {
        if (bot == null || attacker == null || bot.isDead())
            return;

        // π¨ Boost Ο€ΟΞΏΟƒΟ‰ΟΞΉΞ½Ξ®Ο‚ Ξ¬ΞΌΟ…Ξ½Ξ±Ο‚
        double boostedHp = Math.min(bot.getMaxHp() * 1.2, bot.getMaxHp());
        bot.setCurrentHp(boostedHp);

        double boostedMp = Math.min(bot.getMaxMp() * 1.2, bot.getMaxMp());
        bot.setCurrentMp(boostedMp);

        // π― Ξ•Ξ½ΞµΟΞ³ΞΏΟ€ΞΏΞ―Ξ·ΟƒΞ· Ξ±ΞΌΟ…Ξ½Ο„ΞΉΞΊΞΏΟ skill Ξ±Ξ½ Ξ­Ο‡ΞµΞΉ Ξ΄ΞΉΞ±ΞΈΞ­ΟƒΞΉΞΌΞΏ
        L2Skill defensiveSkill = SkillTable.getInstance().getInfo(3, 1); // Ο€.Ο‡. Shield Stun Ξ® Protect

        if (defensiveSkill != null && !bot.isSkillDisabled(defensiveSkill) && bot.canCast(defensiveSkill, attacker)) {
            bot.getFFFFakeAi().castSpell(defensiveSkill);
        }

        // π® Ξ£Ο„ΟΟ‡ΞµΟ…ΟƒΞ· ΞΊΞ±ΞΉ ΞµΞ½ΞµΟΞ³ΞΏΟ€ΞΏΞ―Ξ·ΟƒΞ· ΞΌΞ¬Ο‡Ξ·Ο‚
        bot.setTarget(attacker);
        bot.getAI().setIntention(CtrlIntention.ATTACK, attacker);
    }
}
