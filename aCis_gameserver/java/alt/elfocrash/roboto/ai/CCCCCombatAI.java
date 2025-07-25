package alt.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.model.BBBBBotSkill;
import alt.elfocrash.roboto.model.HHHHHealingSpell;
import alt.elfocrash.roboto.model.OOOOOffensiveSpell;
import alt.elfocrash.roboto.model.SSSSSupportSpell;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.ZoneId;

public abstract class CCCCCombatAI extends FFFFFakePlayerAI {
	
	public CCCCCombatAI(FFFFFakePlayer character) {
		super(character);
	}
	
	protected void tryAttackingUsingMageOffensiveSkill() {
		if(_fffffakePlayer.getTarget() != null)
		{
			BBBBBotSkill botSkill = getRandomAvaiableMageSpellForTarget();
			if(botSkill == null)
				return;
			
			L2Skill skill = _fffffakePlayer.getSkill(botSkill.getSkillId());
			if(skill != null)
				castSpell(skill);
		}
	}
	
	protected void tryAttackingUsingFighterOffensiveSkill()	{
		if(_fffffakePlayer.getTarget() != null && _fffffakePlayer.getTarget() instanceof Creature) {
			_fffffakePlayer.forceAutoAttack((Creature)_fffffakePlayer.getTarget());
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
		int playerLevel = _fffffakePlayer.getLevel();
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
		int playerLevel = _fffffakePlayer.getLevel();
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
		if(_fffffakePlayer.getInventory().getItemByItemId(getShotId()) != null) {
			if(_fffffakePlayer.getInventory().getItemByItemId(getShotId()).getCount() <= 20) {
				_fffffakePlayer.getInventory().addItem("", getShotId(), 20000, _fffffakePlayer, null);			
			}
		}else {
			_fffffakePlayer.getInventory().addItem("", getShotId(), 20000, _fffffakePlayer, null);
		}
		
		if(_fffffakePlayer.getAutoSoulShot().isEmpty()) {
			_fffffakePlayer.addAutoSoulShot(getShotId());
			_fffffakePlayer.rechargeShots(true, true);
		}	
	}
	
	public HHHHHealingSpell getRandomAvaiableHealingSpellForTarget() {

		if(getHealingSpells().isEmpty())
			return null;
		
		List<HHHHHealingSpell> spellsOrdered = getHealingSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		BBBBBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);
		return (HHHHHealingSpell)skill;
	}	
	
	protected BBBBBotSkill getRandomAvaiableMageSpellForTarget() {		
		
		List<OOOOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		
		BBBBBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);	
		
		return skill;
	}

	private BBBBBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBBBBotSkill> spellsOrdered, int skillListSize) {
		int skillIndex = 0;	
		BBBBBotSkill botSkill = spellsOrdered.get(skillIndex);
		_fffffakePlayer.getCurrentSkill().setCtrlPressed(!_fffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
		L2Skill skill = _fffffakePlayer.getSkill(botSkill.getSkillId());
		
		if (skill.getCastRange() > 0)
		{
			if (!GeoEngine.getInstance().canSeeTarget(_fffffakePlayer, _fffffakePlayer.getTarget()))
			{
				moveToPawn(_fffffakePlayer.getTarget(), 100);//skill.getCastRange()
				return null;
			}
		}
		
		while(!_fffffakePlayer.checkUseMagicConditions(skill,true,false)) {			
			_isBusyThinking = true;
			if(_fffffakePlayer.isDead() || _fffffakePlayer.isOutOfControl()) {
				return null;
			}
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;
			}
			skill = _fffffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			botSkill = spellsOrdered.get(skillIndex);
			skillIndex++;			
		}
		return botSkill;
	}
	
	protected L2Skill getRandomAvaiableFighterSpellForTarget() {	
		List<OOOOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillIndex = 0;
		int skillListSize = spellsOrdered.size();
		
		L2Skill skill = _fffffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
		
		_fffffakePlayer.getCurrentSkill().setCtrlPressed(!_fffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));		
		while(!_fffffakePlayer.checkUseMagicConditions(skill,true,false)) {
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;	
			}
			skill = _fffffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			skillIndex++;
		}
		
		if(!_fffffakePlayer.checkUseMagicConditions(skill,true,false)) {
			_fffffakePlayer.forceAutoAttack((Creature)_fffffakePlayer.getTarget());
			return null;
		}			

		return skill;
	}
	
	protected void selfSupportBuffs() {
		List<Integer> activeEffects = Arrays.stream(_fffffakePlayer.getAllEffects())
				.map(x->x.getSkill().getId())
				.collect(Collectors.toList()); 
		
		for(SSSSSupportSpell selfBuff : getSelfSupportSpells()) {
			if(activeEffects.contains(selfBuff.getSkillId()))
				continue;
			
			L2Skill skill = SkillTable.getInstance().getInfo(selfBuff.getSkillId(), _fffffakePlayer.getSkillLevel(selfBuff.getSkillId()));			
			
			if(!_fffffakePlayer.checkUseMagicConditions(skill,true,false))
				continue;
			
			switch(selfBuff.getCondition()) {
				case LESSHPPERCENT:
					if(Math.round(100.0 / _fffffakePlayer.getMaxHp() * _fffffakePlayer.getCurrentHp()) <= selfBuff.getConditionValue()) {
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
		return _fffffakePlayer.getMaxCp() - _fffffakePlayer.getCurrentCp();
	}

	protected double changeOfUsingSkill() {
		return 1.0;
	}
	
	protected abstract ShotType getShotType();
	protected abstract List<OOOOOffensiveSpell> getOffensiveSpells();
	protected abstract List<HHHHHealingSpell> getHealingSpells();
	protected abstract List<SSSSSupportSpell> getSelfSupportSpells();
}
