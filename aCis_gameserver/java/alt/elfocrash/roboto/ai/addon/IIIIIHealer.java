package alt.elfocrash.roboto.ai.addon;

import java.util.List;
import java.util.stream.Collectors;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.ai.CCCCCombatAI;
import alt.elfocrash.roboto.model.HHHHHealingSpell;

import net.sf.l2j.gameserver.model.actor.Creature;

public interface IIIIIHealer {
	
	default void tryTargetingLowestHpTargetInRadius(FFFFFakePlayer player, Class<? extends Creature> creatureClass, int radius) {
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
	
	default void tryHealingTarget(FFFFFakePlayer player) {
		
		if(player.getTarget() != null && player.getTarget() instanceof Creature)
		{
			Creature target = (Creature) player.getTarget();
			if(player.getFFFFFakeAi() instanceof CCCCCombatAI) {
				HHHHHealingSpell skill = ((CCCCCombatAI)player.getFFFFFakeAi()).getRandomAvaiableHealingSpellForTarget();
				if(skill != null) {
					switch(skill.getCondition()){
						case LESSHPPERCENT:
							double currentHpPercentage = Math.round(100.0 / target.getMaxHp() * target.getCurrentHp());
							if(currentHpPercentage <= skill.getConditionValue()) {
								player.getFFFFFakeAi().castSpell(player.getSkill(skill.getSkillId()));						
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
