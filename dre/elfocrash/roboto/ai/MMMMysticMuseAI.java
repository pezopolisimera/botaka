package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public class MMMMysticMuseAI extends CCCCombatAI {
    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;

    public MMMMysticMuseAI(FFFFakePlayer character) {
        super(character);
    }

    @Override
    public void thinkAndAct() {
        handleDeath();

        // 0. Παρέμβαση για ally/clan που δέχεται επίθεση
        List<Player> alliesUnderAttack = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> isSameClanOrAlliance(p) && !p.isDead())
            .filter(p -> p.getTarget() instanceof Player)
            .filter(p -> {
                Player attacker = (Player) p.getTarget();
                return attacker != null
                    && !attacker.isDead()
                    && (attacker.getPvpFlag() > 0 || attacker.getKarma() > 0)
                    && !attacker.isGM()
                    && !isSameClanOrAlliance(attacker);
            })
            .collect(Collectors.toList());

        if (!alliesUnderAttack.isEmpty()) {
            Player ally = alliesUnderAttack.get(0);
            Player attacker = (Player) ally.getTarget();
            _ffffakePlayer.setTarget(attacker);
            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 1. Αμύνεται μόνο σε hostile παίκτες (flagged/Karma) που ΔΕΝ είναι GM ή ally
        List<Player> attackers = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> p.getTarget() == _ffffakePlayer && !p.isDead())
            .filter(p -> _ffffakePlayer.isInsideRadius(p, 1000, true, false))
            .filter(p -> (p.getPvpFlag() > 0 || p.getKarma() > 0))
            .filter(p -> !p.isGM())
            .filter(p -> !isSameClanOrAlliance(p))
            .collect(Collectors.toList());

        if (!attackers.isEmpty()) {
            Creature threat = attackers.get(0);
            _ffffakePlayer.setTarget(threat);

            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 2. Αν υπάρχει ενεργό PvP target, συνέχισε spell ΜΟΝΟ αν είναι ακόμα flagged
        if (_inAutoDefend) {
            WorldObject target = _ffffakePlayer.getTarget();

            if (target instanceof Player) {
                Player p = (Player) target;
                if (p.getPvpFlag() == 0 && p.getKarma() == 0 || p.isGM()) {
                    _ffffakePlayer.setTarget(null);
                    _inAutoDefend = false;
                    return;
                }
            }

            if (target instanceof Creature) {
                Creature creatureTarget = (Creature) target;
                if (!creatureTarget.isDead())
                    tryAttackingUsingMageOffensiveSkill();
            }

            long elapsed = System.currentTimeMillis() - _lastPvPTime;
            if (elapsed < 3000)
                return;
            else {
                _inAutoDefend = false;
                _ffffakePlayer.setTarget(null);
            }
        }

        // 3. Κανονικό farming mode
        setBusyThinking(true);
        applyDefaultBuffs();
        handleShots();
        tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject farmingTarget = _ffffakePlayer.getTarget();
        if (farmingTarget instanceof Creature) {
            Creature mob = (Creature) farmingTarget;
            if (!mob.isDead())
                tryAttackingUsingMageOffensiveSkill();
        }

        setBusyThinking(false);
    }

    @Override
    protected ShotType getShotType() {
        return ShotType.BLESSED_SPIRITSHOT;
    }

    @Override
    protected List<OOOOffensiveSpell> getOffensiveSpells() {
        List<OOOOffensiveSpell> spells = new ArrayList<>();
        spells.add(new OOOOffensiveSpell(1235, 4));
        spells.add(new OOOOffensiveSpell(1340, 3));
        spells.add(new OOOOffensiveSpell(1342, 2));
        spells.add(new OOOOffensiveSpell(1265, 1));
        return spells;
    }

    @Override
    protected int[][] getBuffs() {
        return FFFFakeHelpers.getMageBuffs();
    }

    @Override
    protected List<HHHHealingSpell> getHealingSpells() {
        return Collections.emptyList();
    }

    @Override
    protected List<SSSSupportSpell> getSelfSupportSpells() {
        return Collections.emptyList();
    }
}
