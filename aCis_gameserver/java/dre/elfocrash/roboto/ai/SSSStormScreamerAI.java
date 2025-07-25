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
import dre.elfocrash.roboto.pm.PMReplyHelper;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public class SSSStormScreamerAI extends CCCCombatAI {
    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;
    private long _lastActTime = 0;
    private long _lastTargetSwitchTime = 0;
    private long _lastCastDelayTime = 0;
    private long _lastPostKillDelayTime = 0;

    public SSSStormScreamerAI(FFFFakePlayer character) {
        super(character);       
    }

    public void onPrivateMessage(String fromPlayerName, String messageText) {
        _ffffakePlayer.onPrivateMessageReceived(fromPlayerName, messageText);
    }

    @Override
    public void thinkAndAct() {
        synchronized (_ffffakePlayer) {
            PMReplyHelper.tryReply(_ffffakePlayer);
        }

        long now = System.currentTimeMillis();

        if (now - _lastActTime < Rnd.get(500, 1500)) return;
        _lastActTime = now;

        if (now - _lastTargetSwitchTime < Rnd.get(200, 500)) return;

        handleDeath();

        List<Player> alliesUnderAttack = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> isSameClanOrAlliance(p) && !p.isDead())
            .filter(p -> p.getTarget() instanceof Player)
            .filter(p -> {
                Player attacker = (Player) p.getTarget();
                return attacker != null && !attacker.isDead() && !attacker.isGM()
                    && !isSameClanOrAlliance(attacker)
                    && (attacker.getPvpFlag() > 0 || attacker.getKarma() > 0);
            })
            .collect(Collectors.toList());

        if (!alliesUnderAttack.isEmpty()) {
            Player ally = alliesUnderAttack.get(0);
            Player attacker = (Player) ally.getTarget();
            _ffffakePlayer.setTarget(attacker);
            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = now;
            _inAutoDefend = true;
            _lastTargetSwitchTime = now;
            return;
        }

        List<Player> attackers = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> p.getTarget() == _ffffakePlayer && !p.isDead())
            .filter(p -> _ffffakePlayer.isInsideRadius(p, 1000, true, false))
            .filter(p -> !p.isGM() && !isSameClanOrAlliance(p))
            .filter(p -> p.getPvpFlag() > 0 || p.getKarma() > 0)
            .collect(Collectors.toList());

        if (!attackers.isEmpty()) {
            Creature threat = attackers.get(0);
            _ffffakePlayer.setTarget(threat);
            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = now;
            _inAutoDefend = true;
            _lastTargetSwitchTime = now;
            return;
        }

        if (_inAutoDefend) {
            WorldObject currentTarget = _ffffakePlayer.getTarget();

            if (currentTarget instanceof Player) {
                Player p = (Player) currentTarget;
                if (p.isGM() || isSameClanOrAlliance(p) || (p.getPvpFlag() == 0 && p.getKarma() == 0)) {
                    _ffffakePlayer.setTarget(null);
                    _inAutoDefend = false;
                    return;
                }
            }

            if (currentTarget instanceof Creature) {
                Creature targetCreature = (Creature) currentTarget;
                if (!targetCreature.isDead()) {
                    tryAttackingUsingMageOffensiveSkill();
                }
            }

            if (now - _lastPvPTime >= 3000) {
                _inAutoDefend = false;
                _ffffakePlayer.setTarget(null);
            } else {
                return;
            }
        }

        setBusyThinking(true);
        applyDefaultBuffs();
        handleShots();
        tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject farmTarget = _ffffakePlayer.getTarget();
        if (farmTarget instanceof Creature) {
            Creature mob = (Creature) farmTarget;

            if (!mob.isDead()) {
                if (now - _lastCastDelayTime < Rnd.get(150, 400)) return;
                tryAttackingUsingMageOffensiveSkill();
                _lastCastDelayTime = now;
            } else {
                if (now - _lastPostKillDelayTime < Rnd.get(500, 1000)) return;
                _lastPostKillDelayTime = now;
            }
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
        spells.add(new OOOOffensiveSpell(1341, 1));
        spells.add(new OOOOffensiveSpell(1343, 2));
        spells.add(new OOOOffensiveSpell(1234, 3));
        spells.add(new OOOOffensiveSpell(1239, 4));
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
