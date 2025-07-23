package dre.elfocrash.roboto.ai;

import java.util.*;
import java.util.stream.Collectors;

import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.commons.random.Rnd;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.model.*;

public abstract class CCCCombatAI extends FFFFakePlayerAI {
    private final Map<Integer, Location> _lastPlayerPositions = new HashMap<>();
    private boolean _canFarm = false;

    public CCCCombatAI(FFFFakePlayer character) {
        super(character);
    }

    @Override
    public void thinkAndAct() {
    	
        handleDeath();

         List<Player> nearbyPlayers = _ffffakePlayer.getKnownType(Player.class).stream()
        	      .filter(p -> !p.isGM())
        	      .filter(p -> !p.isDead())
        	      .filter(p -> !_ffffakePlayer.equals(p))
        	      .filter(p -> _ffffakePlayer.isInsideRadius(p, 1000, true, false))
        	      .collect(Collectors.toList());

        boolean teleportDetected = false;

        for (Player p : nearbyPlayers) {
            Location currentLoc = p.getPosition();
            Location lastLoc = _lastPlayerPositions.get(p.getObjectId());

            if (lastLoc != null) {
                double dx = currentLoc.getX() - lastLoc.getX();
                double dy = currentLoc.getY() - lastLoc.getY();
                double dz = currentLoc.getZ() - lastLoc.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (distance > 600) {
                    teleportDetected = true;
                    break;
                }
            }

            _lastPlayerPositions.put(p.getObjectId(), currentLoc);
        }

        Set<Integer> currentIds = nearbyPlayers.stream().map(Player::getObjectId).collect(Collectors.toSet());
        _lastPlayerPositions.keySet().removeIf(id -> !currentIds.contains(id));

        if (teleportDetected) {
            _canFarm = true;
            System.out.println("[Bot] Detected teleport nearby — resuming farm.");
        }

         if (!_canFarm && nearbyPlayers.isEmpty()) {
        	     System.out.println("[Bot] No players nearby — suspending farm.");
            returnToSafePoint();
        	     return;
        	 }

        tryAttackingUsingMageOffensiveSkill();
        tryAttackingUsingFighterOffensiveSkill();
    }
    protected void tryAttackingUsingMageOffensiveSkill() {
        if (!_canFarm) {
            System.out.println("[Bot] Farm is OFF — skipping mage attack.");
            return;
        }

        Creature target = getValidAttackTarget();
        if (target == null)
            return;

        BBBBotSkill botSkill = getRandomAvaiableMageSpellForTarget();
        if (botSkill == null) return;

        L2Skill skill = _ffffakePlayer.getSkill(botSkill.getSkillId());
        if (skill != null) castSpell(skill);
    }

    protected void tryAttackingUsingFighterOffensiveSkill() {
        if (!_canFarm) {
            System.out.println("[Bot] Farm is OFF — skipping fighter attack.");
            return;
        }

        Creature target = getValidAttackTarget();
        if (target == null)
            return;

        _ffffakePlayer.forceAutoAttack(target);

        if (Rnd.nextDouble() < changeOfUsingSkill()) {
            List<OOOOffensiveSpell> spells = getOffensiveSpells();
            if (spells != null && !spells.isEmpty()) {
                L2Skill skill = getRandomAvaiableFighterSpellForTarget();
                if (skill != null)
                    castSpell(skill);
            }
        }
    }

    private Creature getValidAttackTarget() {
        if (!(_ffffakePlayer.getTarget() instanceof Creature))
            return null;

        Creature t = (Creature) _ffffakePlayer.getTarget();

        if (t == null || t.isInsideZone(ZoneId.PEACE))
            return null;

        if (t instanceof Player) {
            Player p = (Player) t;
            if (p.isGM() || isSameClanOrAlliance(p))
                return null;

            if (!_ffffakePlayer.isPvpEnabled()) {
                if (!p.isInCombat())
                    return null;
            }
        }

        if ((t instanceof net.sf.l2j.gameserver.model.actor.Npc) && t.isInsideZone(ZoneId.PEACE))
            return null;

        return t;
    }

    protected double changeOfUsingSkill() {
        return 1.0;
    }

    protected boolean isSameClanOrAlliance(Creature target) {
        if (target == null)
            return false;

        if (_ffffakePlayer.getActingPlayer() == null || target.getActingPlayer() == null)
            return false;

        Player bot = _ffffakePlayer.getActingPlayer();
        Player other = target.getActingPlayer();

        return (bot.getClanId() > 0) && (
               bot.getClanId() == other.getClanId() ||
               (bot.getAllyId() > 0 && bot.getAllyId() == other.getAllyId()));
    }
    
    private void returnToSafePoint() {
        Location spawnLoc = _ffffakePlayer.getSpawnLocation();
        if (spawnLoc == null)
            return;

        int offsetX = Rnd.get(-150, 150);
        int offsetY = Rnd.get(-150, 150);
        int x = spawnLoc.getX() + offsetX;
        int y = spawnLoc.getY() + offsetY;
        int z = spawnLoc.getZ();

        Location safeReturnLocation = new Location(x, y, z);

        // Περπάτημα μέσω AI intention
        if (_ffffakePlayer.getAI() != null)
            _ffffakePlayer.getAI().setIntention(CtrlIntention.MOVE_TO, safeReturnLocation);

        System.out.println("[Bot] Returning near spawn for idle — walking.");
    }

    protected void selfSupportBuffs() {
        List<Integer> activeEffects = Arrays.stream(_ffffakePlayer.getAllEffects())
            .map(e -> e.getSkill().getId())
            .collect(Collectors.toList());

        for (SSSSupportSpell selfBuff : getSelfSupportSpells()) {
            if (activeEffects.contains(selfBuff.getSkillId()))
                continue;

            L2Skill skill = SkillTable.getInstance().getInfo(
                selfBuff.getSkillId(),
                _ffffakePlayer.getSkillLevel(selfBuff.getSkillId()));

            if (!_ffffakePlayer.checkUseMagicConditions(skill, true, false))
                continue;

            switch (selfBuff.getCondition()) {
                case LESSHPPERCENT:
                    double hpPercent = 100.0 * _ffffakePlayer.getCurrentHp() / _ffffakePlayer.getMaxHp();
                    if (hpPercent <= selfBuff.getConditionValue())
                        castSelfSpell(skill);
                    break;
                case MISSINGCP:
                    if (getMissingHealth() >= selfBuff.getConditionValue())
                        castSelfSpell(skill);
                    break;
                case NONE:
                    castSelfSpell(skill);
                    break;
            }
        }
    }

    private double getMissingHealth() {
        return _ffffakePlayer.getMaxCp() - _ffffakePlayer.getCurrentCp();
    }

    protected int getShotId() {
        int level = _ffffakePlayer.getLevel();
        if (level < 20) return getShotType() == ShotType.SOULSHOT ? 1835 : 3947;
        if (level < 40) return getShotType() == ShotType.SOULSHOT ? 1463 : 3948;
        if (level < 52) return getShotType() == ShotType.SOULSHOT ? 1464 : 3949;
        if (level < 61) return getShotType() == ShotType.SOULSHOT ? 1465 : 3950;
        if (level < 76) return getShotType() == ShotType.SOULSHOT ? 1466 : 3951;
        return getShotType() == ShotType.SOULSHOT ? 1467 : 3952;
    }

    protected int getArrowId() {
        int level = _ffffakePlayer.getLevel();
        if (level < 20) return 17;
        if (level < 40) return 1341;
        if (level < 52) return 1342;
        if (level < 61) return 1343;
        if (level < 76) return 1344;
        return 1345;
    }

    protected void handleShots() {
        if (_ffffakePlayer.getInventory().getItemByItemId(getShotId()) != null) {
            if (_ffffakePlayer.getInventory().getItemByItemId(getShotId()).getCount() <= 20) {
                _ffffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffffakePlayer, null);
            }
        } else {
            _ffffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffffakePlayer, null);
        }

        if (_ffffakePlayer.getAutoSoulShot().isEmpty()) {
            _ffffakePlayer.addAutoSoulShot(getShotId());
            _ffffakePlayer.rechargeShots(true, true);
        }
    }

    public HHHHealingSpell getRandomAvaiableHealingSpellForTarget() {
        if (getHealingSpells().isEmpty()) return null;

        List<HHHHealingSpell> spells = getHealingSpells().stream()
            .sorted(Comparator.comparingInt(HHHHealingSpell::getPriority))
            .collect(Collectors.toList());

        BBBBotSkill chosen = waitAndPickAvailablePrioritisedSpell(spells, spells.size());
        return (HHHHealingSpell) chosen;
    }

    protected BBBBotSkill getRandomAvaiableMageSpellForTarget() {
        List<OOOOffensiveSpell> spells = getOffensiveSpells().stream()
            .sorted(Comparator.comparingInt(OOOOffensiveSpell::getPriority))
            .collect(Collectors.toList());

        return waitAndPickAvailablePrioritisedSpell(spells, spells.size());
    }

    protected L2Skill getRandomAvaiableFighterSpellForTarget() {
        List<OOOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream()
            .sorted(Comparator.comparingInt(OOOOffensiveSpell::getPriority))
            .collect(Collectors.toList());

        int i = 0;
        L2Skill skill = _ffffakePlayer.getSkill(spellsOrdered.get(i).getSkillId());
        _ffffakePlayer.getCurrentSkill().setCtrlPressed(!_ffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));

        while (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            i++;
            if (i >= spellsOrdered.size())
                return null;

            skill = _ffffakePlayer.getSkill(spellsOrdered.get(i).getSkillId());
        }

        if (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            Creature fallbackTarget = getValidAttackTarget();
            if (fallbackTarget != null)
                _ffffakePlayer.forceAutoAttack(fallbackTarget);
            return null;
        }

        return skill;
    }

    private BBBBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBBBotSkill> spells, int size) {
        int i = 0;
        BBBBotSkill chosen = spells.get(i);
        _ffffakePlayer.getCurrentSkill().setCtrlPressed(!_ffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
        L2Skill skill = _ffffakePlayer.getSkill(chosen.getSkillId());

        if (skill.getCastRange() > 0 && !GeoEngine.getInstance().canSeeTarget(_ffffakePlayer, _ffffakePlayer.getTarget())) {
            moveToPawn(_ffffakePlayer.getTarget(), 100);
            return null;
        }

        while (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            _isBusyThinking = true;
            if (_ffffakePlayer.isDead() || _ffffakePlayer.isOutOfControl() || i >= size) return null;
            chosen = spells.get(i);
            skill = _ffffakePlayer.getSkill(chosen.getSkillId());
            i++;
        }

        return chosen;
    }
    
    public void checkReturnToSpawn() {
        Location spawn = _ffffakePlayer.getSpawnLocation();
        if (spawn == null)
            return;

        if (!_ffffakePlayer.isInsideRadius(spawn, 1000, true, false)) {
            if (!_ffffakePlayer.isMoving()) {
                _ffffakePlayer.getAI().setIntention(CtrlIntention.MOVE_TO, spawn);
                System.out.println("[Bot] Out of range — returning to spawn.");
            }
        }
    }
    
    // Abstract methods
    protected abstract ShotType getShotType();
    protected abstract List<OOOOffensiveSpell> getOffensiveSpells();
    protected abstract List<HHHHealingSpell> getHealingSpells();
    protected abstract List<SSSSupportSpell> getSelfSupportSpells();
}
