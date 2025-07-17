package dre.elfocrash.roboto.helpers;

import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import dre.elfocrash.roboto.FFFFakePlayer;

public class CombatStrategyHelper {

    public static void reactStrategically(FFFFakePlayer bot, Player attacker) {
    	    if (bot == null || attacker == null || bot.isDead())
    		        return;
    		
    	    int classId = attacker.getClassId().getId(); // Corrected: from ClassId object to int
    		
    		    switch (classId) {
    		        case 10: // Mage
    		            useSkill(bot, attacker, 11, 1); // Silence
    		            break;
    		        case 3: // Warrior
    		            useSkill(bot, attacker, 2, 1); // Shield Stun
    		            break;
    		        case 92: // Healer
    		            useSkill(bot, attacker, 30, 1); // Block cancel
    		            break;
    		        default:
    		            useSkill(bot, attacker, 5, 1); // Generic stun
    		            break;
    		    }
    		
    		    bot.setTarget(attacker);
    		
    		        if (bot.getAI() != null) {
    		    	        bot.getAI().setIntention(CtrlIntention.ATTACK, attacker);
    		    	    } else {
    		    	        //  Safe fallback if AI is null β€” cast skill directly
    		    	        L2Skill fallbackSkill = SkillTable.getInstance().getInfo(5, 1);
    		    	        if (fallbackSkill != null && !bot.isSkillDisabled(fallbackSkill) && bot.canCast(fallbackSkill, attacker)) {
    		    	            bot.getFFFFakeAi().castSpell(fallbackSkill);
    		    	       }
    		    	    }
    }

    private static void useSkill(FFFFakePlayer bot, Player target, int skillId, int level) {
    	   L2Skill skill = SkillTable.getInstance().getInfo(skillId, level);
    	    if (skill != null && !bot.isSkillDisabled(skill) && bot.canCast(skill, target)) {
    	        bot.getFFFFakeAi().castSpell(skill);
    	    }
    }
}

