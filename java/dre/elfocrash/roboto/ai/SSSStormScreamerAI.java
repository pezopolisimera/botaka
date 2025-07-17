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
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;

public class SSSStormScreamerAI extends CCCCombatAI {
    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;

    public SSSStormScreamerAI(FFFFakePlayer character) {
        super(character);       
    }

    @Override
    public void thinkAndAct() {
    synchronized (_ffffakePlayer) {
     PMReplyHelper.tryReply(_ffffakePlayer);
         }    
        handleDeath();

        // 0. Ξ Ξ±ΟΞ­ΞΌΞ²Ξ±ΟƒΞ· Ο…Ο€Ξ­Ο ally/clan Ο€ΞΏΟ… Ξ΄Ξ­Ο‡ΞµΟ„Ξ±ΞΉ ΞµΟ€Ξ―ΞΈΞµΟƒΞ·
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
            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 1. Auto-defend ΟƒΞµ hostile Ο€Ξ±Ξ―ΞΊΟ„ΞµΟ‚, exclude allies/GMs
        List<Player> attackers = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> p.getTarget() == _ffffakePlayer && !p.isDead())
            .filter(p -> _ffffakePlayer.isInsideRadius(p, 1000, true, false))
            .filter(p -> (p.getPvpFlag() > 0 || p.getKarma() > 0))
            .filter(p -> !isSameClanOrAlliance(p) && !p.isGM())
            .collect(Collectors.toList());

        if (!attackers.isEmpty()) {
            Creature threat = attackers.get(0);
            _ffffakePlayer.setTarget(threat);

            tryAttackingUsingMageOffensiveSkill();
            _lastPvPTime = System.currentTimeMillis();
            _inAutoDefend = true;
            return;
        }

        // 2. Ξ£Ο…Ξ½Ξ­Ο‡ΞΉΟƒΞ· PvP ΞµΟ€Ξ―ΞΈΞµΟƒΞ·Ο‚ ΞΞΞΞ Ξ±Ξ½ ΟƒΟ„ΟΟ‡ΞΏΟ‚ Ο€Ξ±ΟΞ±ΞΌΞ­Ξ½ΞµΞΉ flagged ΞΊΞ±ΞΉ ΞµΟ‡ΞΈΟΞΉΞΊΟΟ‚
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
                if (!targetCreature.isDead())
                    tryAttackingUsingMageOffensiveSkill();
            }

            if (System.currentTimeMillis() - _lastPvPTime >= 3000) {
    	        _inAutoDefend = false;
    	        _ffffakePlayer.setTarget(null);
    	    } else {
    	        return;
    	    }
}

        // 3. Farm mode
        setBusyThinking(true);
        applyDefaultBuffs();
        handleShots();
        tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject farmTarget = _ffffakePlayer.getTarget();
        if (farmTarget instanceof Creature) {
            Creature mob = (Creature) farmTarget;
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
