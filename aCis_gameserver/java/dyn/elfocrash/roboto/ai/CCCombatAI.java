package dyn.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.model.BBBotSkill;
import dyn.elfocrash.roboto.model.HHHealingSpell;
import dyn.elfocrash.roboto.model.OOOffensiveSpell;
import dyn.elfocrash.roboto.model.SSSupportSpell;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.ZoneId;

public abstract class CCCombatAI extends FFFakePlayerAI {
	
	public CCCombatAI(FFFakePlayer character) {
		super(character);
	}
	
	protected void tryAttackingUsingMageOffensiveSkill() {
		if(_fffakePlayer.getTarget() != null)
		{
			BBBotSkill botSkill = getRandomAvaiableMageSpellForTarget();
			if(botSkill == null)
				return;
			
			L2Skill skill = _fffakePlayer.getSkill(botSkill.getSkillId());
			if(skill != null)
				castSpell(skill);
		}
	}
	
	protected void tryAttackingUsingFighterOffensiveSkill()	{
		if(_fffakePlayer.getTarget() != null && _fffakePlayer.getTarget() instanceof Creature) {
			_fffakePlayer.forceAutoAttack((Creature)_fffakePlayer.getTarget());
			if(Rnd.nextDouble() < changeOfUsingSkill()) {			
				if(getOffensiveSpells() != null && !getOffensiveSpells().isEmpty()) {
					L2Skill skill = getRandomAvaiableFighterSpellForTarget();			
					if(skill != null) {
						castSpell(skill);
					}
				}	
			}
		}
	}
	
	@Override
	public void thinkAndAct() {
		handleDeath();
	}
	
	protected int getShotId() {
		int playerLevel = _fffakePlayer.getLevel();
		if(playerLevel < 20)
			return getShotType() == ShotType.SOULSHOT ? 1835 : 3947;
		if(playerLevel >= 20 && playerLevel < 40)
			return getShotType() == ShotType.SOULSHOT ? 1463 : 3948;
		if(playerLevel >= 40 && playerLevel < 52)
			return getShotType() == ShotType.SOULSHOT ? 1464 : 3949;
		if(playerLevel >= 52 && playerLevel < 61)
			return getShotType() == ShotType.SOULSHOT ? 1465 : 3950;
		if(playerLevel >= 61 && playerLevel < 76)
			return getShotType() == ShotType.SOULSHOT ? 1466 : 3951;
		if(playerLevel >= 76)
			return getShotType() == ShotType.SOULSHOT ? 1467 : 3952;
		
		return 0;
	}
	
	protected int getArrowId() {
		int playerLevel = _fffakePlayer.getLevel();
		if(playerLevel < 20)
			return 17; // wooden arrow
		if(playerLevel >= 20 && playerLevel < 40)
			return 1341; // bone arrow
		if(playerLevel >= 40 && playerLevel < 52)
			return 1342; // steel arrow
		if(playerLevel >= 52 && playerLevel < 61)
			return 1343; // Silver arrow
		if(playerLevel >= 61 && playerLevel < 76)
			return 1344; // Mithril Arrow
		if(playerLevel >= 76)
			return 1345; // shining
		
		return 0;
	}
	
	protected void handleShots() {
		if(_fffakePlayer.getInventory().getItemByItemId(getShotId()) != null) {
			if(_fffakePlayer.getInventory().getItemByItemId(getShotId()).getCount() <= 20) {
				_fffakePlayer.getInventory().addItem("", getShotId(), 20000, _fffakePlayer, null);			
			}
		}else {
			_fffakePlayer.getInventory().addItem("", getShotId(), 20000, _fffakePlayer, null);
		}
		
		if(_fffakePlayer.getAutoSoulShot().isEmpty()) {
			_fffakePlayer.addAutoSoulShot(getShotId());
			_fffakePlayer.rechargeShots(true, true);
		}	
	}
	
	public HHHealingSpell getRandomAvaiableHealingSpellForTarget() {

		if(getHealingSpells().isEmpty())
			return null;
		
		List<HHHealingSpell> spellsOrdered = getHealingSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		BBBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);
		return (HHHealingSpell)skill;
	}	
	
	protected BBBotSkill getRandomAvaiableMageSpellForTarget() {		
		
		List<OOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		
		BBBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);	
		
		return skill;
	}

	private BBBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBBotSkill> spellsOrdered, int skillListSize) {
		int skillIndex = 0;	
		BBBotSkill botSkill = spellsOrdered.get(skillIndex);
		_fffakePlayer.getCurrentSkill().setCtrlPressed(!_fffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
		L2Skill skill = _fffakePlayer.getSkill(botSkill.getSkillId());
		
		if (skill.getCastRange() > 0)
		{
			if (!GeoEngine.getInstance().canSeeTarget(_fffakePlayer, _fffakePlayer.getTarget()))
			{
				moveToPawn(_fffakePlayer.getTarget(), 100);//skill.getCastRange()
				return null;
			}
		}
		
		while(!_fffakePlayer.checkUseMagicConditions(skill,true,false)) {			
			_isBusyThinking = true;
			if(_fffakePlayer.isDead() || _fffakePlayer.isOutOfControl()) {
				return null;
			}
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;
			}
			skill = _fffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			botSkill = spellsOrdered.get(skillIndex);
			skillIndex++;			
		}
		return botSkill;
	}
	
	protected L2Skill getRandomAvaiableFighterSpellForTarget() {	
		List<OOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillIndex = 0;
		int skillListSize = spellsOrdered.size();
		
		L2Skill skill = _fffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
		
		_fffakePlayer.getCurrentSkill().setCtrlPressed(!_fffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));		
		while(!_fffakePlayer.checkUseMagicConditions(skill,true,false)) {
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;	
			}
			skill = _fffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			skillIndex++;
		}
		
		if(!_fffakePlayer.checkUseMagicConditions(skill,true,false)) {
			_fffakePlayer.forceAutoAttack((Creature)_fffakePlayer.getTarget());
			return null;
		}			

		return skill;
	}
	
	protected void selfSupportBuffs() {
		List<Integer> activeEffects = Arrays.stream(_fffakePlayer.getAllEffects())
				.map(x->x.getSkill().getId())
				.collect(Collectors.toList()); 
		
		for(SSSupportSpell selfBuff : getSelfSupportSpells()) {
			if(activeEffects.contains(selfBuff.getSkillId()))
				continue;
			
			L2Skill skill = SkillTable.getInstance().getInfo(selfBuff.getSkillId(), _fffakePlayer.getSkillLevel(selfBuff.getSkillId()));			
			
			if(!_fffakePlayer.checkUseMagicConditions(skill,true,false))
				continue;
			
			switch(selfBuff.getCondition()) {
				case LESSHPPERCENT:
					if(Math.round(100.0 / _fffakePlayer.getMaxHp() * _fffakePlayer.getCurrentHp()) <= selfBuff.getConditionValue()) {
						castSelfSpell(skill);						
					}						
					break;
				case MISSINGCP:
					if(getMissingHealth() >= selfBuff.getConditionValue()) {
						castSelfSpell(skill);	
					}
					break;
				case NONE:
					castSelfSpell(skill);		
				default:
					break;				
			}
			
		}
	}
		
	private double getMissingHealth() {
		return _fffakePlayer.getMaxCp() - _fffakePlayer.getCurrentCp();
	}

	protected double changeOfUsingSkill() {
		return 1.0;
	}
	
	protected abstract ShotType getShotType();
	protected abstract List<OOOffensiveSpell> getOffensiveSpells();
	protected abstract List<HHHealingSpell> getHealingSpells();
	protected abstract List<SSSupportSpell> getSelfSupportSpells();
}
