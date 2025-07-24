package dre.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.l2j.gameserver.model.location.Location;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.model.BBBBotSkill;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public abstract class CCCCombatAI extends FFFFakePlayerAI {

    public CCCCombatAI(FFFFakePlayer character) {
        super(character);
    }

    protected void tryAttackingUsingMageOffensiveSkill() {
        if (_ffffakePlayer.getTarget() != null) {
            BBBBotSkill botSkill = getRandomAvaiableMageSpellForTarget();
            if (botSkill == null)
                return;

            L2Skill skill = _ffffakePlayer.getSkill(botSkill.getSkillId());
            if (skill != null)
                castSpell(skill);
        }
    }

    protected void tryAttackingUsingFighterOffensiveSkill() {
        if (_ffffakePlayer.getTarget() != null && _ffffakePlayer.getTarget() instanceof Creature) {
            _ffffakePlayer.forceAutoAttack((Creature) _ffffakePlayer.getTarget());
            if (Rnd.nextDouble() < changeOfUsingSkill()) {
                if (getOffensiveSpells() != null && !getOffensiveSpells().isEmpty()) {
                    L2Skill skill = getRandomAvaiableFighterSpellForTarget();
                    if (skill != null) {
                        castSpell(skill);
                    }
                }
            }
        }
    }

     @Override
     public void thinkAndAct() {
          handleDeath();
         
          List<Player> nearbyPlayers = _ffffakePlayer.getKnownType(Player.class).stream()
               .filter(p -> !_ffffakePlayer.equals(p))
               .filter(p -> _ffffakePlayer.isInsideRadius(p, 1500, true, false))
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
         
                   if (distance > 1200) { 
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
              return;
          }
         
          tryAttackingUsingMageOffensiveSkill();
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
            .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
            .collect(Collectors.toList());

        BBBBotSkill chosen = waitAndPickAvailablePrioritisedSpell(spells, spells.size());
        return (HHHHealingSpell) chosen;
    }

    protected BBBBotSkill getRandomAvaiableMageSpellForTarget() {
        List<OOOOffensiveSpell> spells = getOffensiveSpells().stream()
            .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
            .collect(Collectors.toList());

        // Προσθήκη ελέγχου εδώ, αν και η main λύση είναι στη waitAndPickAvailablePrioritisedSpell
        // if (spells.isEmpty()) return null; 

        return waitAndPickAvailablePrioritisedSpell(spells, spells.size());
    }

    private BBBBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBBBotSkill> spells, int size) {
        // **ΛΥΣΗ ΤΟΥ ΣΦΑΛΜΑΤΟΣ: java.lang.IndexOutOfBoundsException**
        // Ελέγχουμε αν η λίστα 'spells' είναι κενή πριν προσπαθήσουμε να αποκτήσουμε πρόσβαση σε αυτήν.
        // Αν είναι κενή, επιστρέφουμε null για να αποφύγουμε το σφάλμα.
        if (spells.isEmpty()) {
            return null;
        }

        int i = 0;
        BBBBotSkill chosen = spells.get(i); // Αυτή η γραμμή (162) ήταν η αιτία του σφάλματος
        _ffffakePlayer.getCurrentSkill().setCtrlPressed(!_ffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
        L2Skill skill = _ffffakePlayer.getSkill(chosen.getSkillId());

        if (skill.getCastRange() > 0 && !GeoEngine.getInstance().canSeeTarget(_ffffakePlayer, _ffffakePlayer.getTarget())) {
            moveToPawn(_ffffakePlayer.getTarget(), 100);
            return null;
        }

        while (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            _isBusyThinking = true;
            i++; // Προσπαθούμε την επόμενη ικανότητα
            if (_ffffakePlayer.isDead() || _ffffakePlayer.isOutOfControl() || i >= size) {
                // Αν ο bot είναι νεκρός, εκτός ελέγχου ή δεν υπάρχουν άλλες ικανότητες
                return null;
            }
            // Ενημερώνουμε την επιλεγμένη ικανότητα και το L2Skill
            chosen = spells.get(i);
            skill = _ffffakePlayer.getSkill(chosen.getSkillId());
        }

        return chosen;
    }
    protected L2Skill getRandomAvaiableFighterSpellForTarget() {
        List<OOOOffensiveSpell> spellsOrdered = getOffensiveSpells().stream()
            .sorted((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()))
            .collect(Collectors.toList());

        // Προσθήκη ελέγχου εδώ για να αποφευχθεί το σφάλμα αν η λίστα είναι κενή
        if (spellsOrdered.isEmpty()) {
            return null;
        }

        int skillIndex = 0;
        int skillListSize = spellsOrdered.size();

        L2Skill skill = _ffffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
        _ffffakePlayer.getCurrentSkill().setCtrlPressed(!_ffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));

        while (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            skillIndex++; // Δοκιμάζουμε την επόμενη ικανότητα
            if (skillIndex >= skillListSize) // Ελέγχουμε αν εξαντλήθηκαν οι ικανότητες
                return null;

            skill = _ffffakePlayer.getSkill(spellsOrdered.get(skillIndex).getSkillId());
        }

        if (!_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
            _ffffakePlayer.forceAutoAttack((Creature) _ffffakePlayer.getTarget());
            return null;
        }

        return skill;
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

            if (skill == null || !_ffffakePlayer.checkUseMagicConditions(skill, true, false)) // Προσθήκη ελέγχου για skill == null
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
    
    private final Map<Integer, Location> _lastPlayerPositions = new HashMap<>();
    private boolean _canFarm = false;

    private double getMissingHealth() {
        return _ffffakePlayer.getMaxCp() - _ffffakePlayer.getCurrentCp();
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


    // Abstract methods
    protected abstract ShotType getShotType();
    protected abstract List<OOOOffensiveSpell> getOffensiveSpells();
    protected abstract List<HHHHealingSpell> getHealingSpells();
    protected abstract List<SSSSupportSpell> getSelfSupportSpells();
}