package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSpellUsageCondition;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public class TTTTitanAI extends CCCCombatAI {
    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;

    public TTTTitanAI(FFFFakePlayer character) {
        super(character);
    }

    @Override
    public void thinkAndAct() {
        handleDeath();

        // 0. Παρέμβαση για ally/clan που δέχεται επίθεση από hostile
        List<Player> alliesUnderAttack = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> isSameClanOrAlliance(p) && !p.isDead())
            .filter(p -> p.getTarget() instanceof Player)
            .filter(p -> {
                Player attacker = (Player) p.getTarget();
                return attacker != null
                    && !attacker.isDead()
                    && !attacker.isGM()
                    && !isSameClanOrAlliance(attacker)
                    && (attacker.getPvpFlag() > 0 || attacker.getKarma() > 0);
            })
            .collect(Collectors.toList());

        if (!alliesUnderAttack.isEmpty()) {
            Player ally = alliesUnderAttack.get(0);
            Player attacker = (Player) ally.getTarget();
            _ffffakePlayer.setTarget(attacker);
            tryAttackingUsingFighterOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 1. Auto-defend μόνο σε hostile flagged/Karma παίκτες
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
            tryAttackingUsingFighterOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 2. Συνέχιση μάχης ΜΟΝΟ όσο είναι flagged/PK και όχι admin/allied
        if (_inAutoDefend) {
            WorldObject target = _ffffakePlayer.getTarget();

            if (target instanceof Player) {
                Player p = (Player) target;
                if (p.isGM() || isSameClanOrAlliance(p) || (p.getPvpFlag() == 0 && p.getKarma() == 0)) {
                    _ffffakePlayer.setTarget(null);
                    _inAutoDefend = false;
                    return;
                }
            }

            if (target instanceof Creature) {
                Creature c = (Creature) target;
                if (!c.isDead())
                    tryAttackingUsingFighterOffensiveSkill();
            }

            long elapsed = System.currentTimeMillis() - _lastPvPTime;
            if (elapsed < 3000)
                return;
            else {
                _inAutoDefend = false;
                _ffffakePlayer.setTarget(null);
            }
        }

        // 3. Κανονικό farming
        setBusyThinking(true);
        applyDefaultBuffs();
        handleShots();
        selfSupportBuffs();
        tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject mobTarget = _ffffakePlayer.getTarget();
        if (mobTarget instanceof Creature) {
            Creature mob = (Creature) mobTarget;
            if (!mob.isDead())
                tryAttackingUsingFighterOffensiveSkill();
        }

        setBusyThinking(false);
    }

    @Override
    protected double changeOfUsingSkill() {
        return 0.2;
    }

    @Override
    protected ShotType getShotType() {
        return ShotType.SOULSHOT;
    }

    @Override
    protected List<OOOOffensiveSpell> getOffensiveSpells() {
        return new ArrayList<>();
    }

    @Override
    public List<SSSSupportSpell> getSelfSupportSpells() {
        List<SSSSupportSpell> buffs = new ArrayList<>();
        buffs.add(new SSSSupportSpell(139, SSSSpellUsageCondition.LESSHPPERCENT, 30, 1));
        buffs.add(new SSSSupportSpell(176, SSSSpellUsageCondition.LESSHPPERCENT, 30, 2));
        return buffs;
    }

    @Override
    protected int[][] getBuffs() {
        return FFFFakeHelpers.getFighterBuffs();
    }

    @Override
    protected List<HHHHealingSpell> getHealingSpells() {
        return Collections.emptyList();
    }
}
