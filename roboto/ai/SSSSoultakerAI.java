package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.addon.IIIIConsumableSpender;
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

public class SSSSoultakerAI extends CCCCombatAI implements IIIIConsumableSpender {
    private final int boneId = 2508;
    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;

    private long _lastActTime = 0;
    private long _lastTargetSwitchTime = 0;
    private long _lastCastDelayTime = 0;
    private long _lastPostKillDelayTime = 0;

    public SSSSoultakerAI(FFFFakePlayer character) {
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
        _ffffakePlayer.getFFFFakeAi().advancedThinkAndAct();
        List<Player> alliesUnderAttack = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> isSameClanOrAlliance(p) && !p.isDead())
            .filter(p -> p.getTarget() instanceof Player)
            .filter(p -> {
                Player attacker = (Player) p.getTarget();
                return attacker != null && !attacker.isDead() && !attacker.isGM()
                    && (attacker.getPvpFlag() > 0 || attacker.getKarma() > 0)
                    && !isSameClanOrAlliance(attacker);
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
                Creature creatureTarget = (Creature) target;
                if (!creatureTarget.isDead()) {
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
        handleConsumable(_ffffakePlayer, boneId);
        handleShots();
        tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject mobTarget = _ffffakePlayer.getTarget();
        if (mobTarget instanceof Creature) {
            Creature mob = (Creature) mobTarget;

            if (!mob.isDead()) {
                if (now - _lastCastDelayTime < Rnd.get(150, 400)) return;
                tryAttackingUsingMageOffensiveSkill();
                _lastCastDelayTime = now;
            } else {
                if (now - _lastPostKillDelayTime < Rnd.get(500, 1000)) return;
                _lastPostKillDelayTime = now;
            }
        }

        selfSupportBuffs();
        setBusyThinking(false);
    }

    @Override
    protected ShotType getShotType() {
        return ShotType.BLESSED_SPIRITSHOT;
    }

    @Override
    protected List<OOOOffensiveSpell> getOffensiveSpells() {
        List<OOOOffensiveSpell> spells = new ArrayList<>();
        spells.add(new OOOOffensiveSpell(1234, 1));
        spells.add(new OOOOffensiveSpell(1148, 2));
        spells.add(new OOOOffensiveSpell(1343, 3));
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
