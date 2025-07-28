package dyn.elfocrash.roboto.ai.addon;

import java.util.List;
import java.util.stream.Collectors;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.ai.CCCombatAI;
import dyn.elfocrash.roboto.model.HHHealingSpell;

import net.sf.l2j.gameserver.model.actor.Creature;

public interface IIIHealer {
	
	default void tryTargetingLowestHpTargetInRadius(FFFakePlayer player, Class<? extends Creature> creatureClass, int radius) {
		if(player.getTarget() == null) {
			List<Creature> targets = player.getKnownTypeInRadius(creatureClass, radius).stream()
					.filter(x->!x.isDead())					
					.collect(Collectors.toList());
			
			if(!player.isDead())
				targets.add(player);		
			
			List<Creature> sortedTargets = targets.stream().sorted((x1, x2) -> Double.compare(x1.getCurrentHp(), x2.getCurrentHp())).collect(Collectors.toList());
			
			if(!sortedTargets.isEmpty()) {
				Creature target = sortedTargets.get(0);
				player.setTarget(target);				
			}
		}else {
			if(((Creature)player.getTarget()).isDead())
				player.setTarget(null);
		}	
	}
	
	default void tryHealingTarget(FFFakePlayer player) {
		
		if(player.getTarget() != null && player.getTarget() instanceof Creature)
		{
			Creature target = (Creature) player.getTarget();
			if(player.getFFFakeAi() instanceof CCCombatAI) {
				HHHealingSpell skill = ((CCCombatAI)player.getFFFakeAi()).getRandomAvaiableHealingSpellForTarget();
				if(skill != null) {
					switch(skill.getCondition()){
						case LESSHPPERCENT:
							double currentHpPercentage = Math.round(100.0 / target.getMaxHp() * target.getCurrentHp());
							if(currentHpPercentage <= skill.getConditionValue()) {
								player.getFFFakeAi().castSpell(player.getSkill(skill.getSkillId()));						
							}						
							break;				
						default:
							break;							
					}
					
				}
			}
		}
	}
}
