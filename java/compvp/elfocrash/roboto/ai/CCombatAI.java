package compvp.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.model.BBotSkill;
import compvp.elfocrash.roboto.model.HHealingSpell;
import compvp.elfocrash.roboto.model.OOffensiveSpell;
import compvp.elfocrash.roboto.model.SSupportSpell;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.ZoneId;

public abstract class CCombatAI extends FFakePlayerAI {
	
	public CCombatAI(FFakePlayer character) {
		super(character);
	}
	
	protected void tryAttackingUsingMageOffensiveSkill() {
		if(_ffakePlayer.getTarget() != null)
		{
			BBotSkill botSkill = getRandomAvaiableMageSpellForTarget();
			if(botSkill == null)
				return;
			
			L2Skill skill = _ffakePlayer.getSkill(botSkill.getSkillId());
			if(skill != null)
				castSpell(skill);
		}
	}
	
	protected void tryAttackingUsingFighterOffensiveSkill()	{
		if(_ffakePlayer.getTarget() != null && _ffakePlayer.getTarget() instanceof Creature) {
			_ffakePlayer.forceAutoAttack((Creature)_ffakePlayer.getTarget());
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
		int playerLevel = _ffakePlayer.getLevel();
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
		int playerLevel = _ffakePlayer.getLevel();
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
		if(_ffakePlayer.getInventory().getItemByItemId(getShotId()) != null) {
			if(_ffakePlayer.getInventory().getItemByItemId(getShotId()).getCount() <= 20) {
				_ffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffakePlayer, null);			
			}
		}else {
			_ffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffakePlayer, null);
		}
		
		if(_ffakePlayer.getAutoSoulShot().isEmpty()) {
			_ffakePlayer.addAutoSoulShot(getShotId());
			_ffakePlayer.rechargeShots(true, true);
		}	
	}
	
	public HHealingSpell getRandomAvaiableHealingSpellForTarget() {

		if(getHealingSpells().isEmpty())
			return null;
		
		List<HHealingSpell> spellsOrdered = getHealingSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		BBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);
		return (HHealingSpell)skill;
	}	
	
	protected BBotSkill getRandomAvaiableMageSpellForTarget() {		
		
		List<OOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillListSize = spellsOrdered.size();
		
		BBotSkill skill = waitAndPickAvailablePrioritisedSpell(spellsOrdered, skillListSize);	
		
		return skill;
	}

	private BBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBotSkill> spellsOrdered, int skillListSize) {
		int skillIndex = 0;	
		BBotSkill botSkill = spellsOrdered.get(skillIndex);
		_ffakePlayer.getCurrentSkill().setCtrlPressed(!_ffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
		L2Skill skill = _ffakePlayer.getSkill(botSkill.getSkillId());
		
		if (skill.getCastRange() > 0)
		{
			if (!GeoEngine.getInstance().canSeeTarget(_ffakePlayer, _ffakePlayer.getTarget()))
			{
				moveToPawn(_ffakePlayer.getTarget(), 100);//skill.getCastRange()
				return null;
			}
		}
		
		while(!_ffakePlayer.checkUseMagicConditions(skill,true,false)) {			
			_isBusyThinking = true;
			if(_ffakePlayer.isDead() || _ffakePlayer.isOutOfControl()) {
				return null;
			}
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;
			}
			skill = _ffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			botSkill = spellsOrdered.get(skillIndex);
			skillIndex++;			
		}
		return botSkill;
	}
	
	protected L2Skill getRandomAvaiableFighterSpellForTarget() {	
		List<OOffensiveSpell> spellsOrdered = getOffensiveSpells().stream().sorted((o1, o2)-> Integer.compare(o1.getPriority(), o2.getPriority())).collect(Collectors.toList());
		int skillIndex = 0;
		int skillListSize = spellsOrdered.size();
		
		L2Skill skill = _ffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
		
		_ffakePlayer.getCurrentSkill().setCtrlPressed(!_ffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));		
		while(!_ffakePlayer.checkUseMagicConditions(skill,true,false)) {
			if((skillIndex < 0) || (skillIndex >= skillListSize)) {
				return null;	
			}
			skill = _ffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
			skillIndex++;
		}
		
		if(!_ffakePlayer.checkUseMagicConditions(skill,true,false)) {
			_ffakePlayer.forceAutoAttack((Creature)_ffakePlayer.getTarget());
			return null;
		}			

		return skill;
	}
	
	protected void selfSupportBuffs() {
		List<Integer> activeEffects = Arrays.stream(_ffakePlayer.getAllEffects())
				.map(x->x.getSkill().getId())
				.collect(Collectors.toList()); 
		
		for(SSupportSpell selfBuff : getSelfSupportSpells()) {
			if(activeEffects.contains(selfBuff.getSkillId()))
				continue;
			
			L2Skill skill = SkillTable.getInstance().getInfo(selfBuff.getSkillId(), _ffakePlayer.getSkillLevel(selfBuff.getSkillId()));			
			
			if(!_ffakePlayer.checkUseMagicConditions(skill,true,false))
				continue;
			
			switch(selfBuff.getCondition()) {
				case LESSHPPERCENT:
					if(Math.round(100.0 / _ffakePlayer.getMaxHp() * _ffakePlayer.getCurrentHp()) <= selfBuff.getConditionValue()) {
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
		return _ffakePlayer.getMaxCp() - _ffakePlayer.getCurrentCp();
	}

	protected double changeOfUsingSkill() {
		return 1.0;
	}
	
	protected abstract ShotType getShotType();
	protected abstract List<OOffensiveSpell> getOffensiveSpells();
	protected abstract List<HHealingSpell> getHealingSpells();
	protected abstract List<SSupportSpell> getSelfSupportSpells();
}
