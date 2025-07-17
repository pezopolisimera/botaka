package dre.elfocrash.roboto.helpers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.model.actor.instance.Player;
import dre.elfocrash.roboto.FFFFakePlayer;

public class AttackerMemoryHelper {

    // Ξ§Ξ¬ΟΟ„Ξ·Ο‚ bot β†’ attacker β†’ hit count
    private static final Map<FFFFakePlayer, Map<Player, Integer>> attackerMemory = new ConcurrentHashMap<>();

    public static void registerAttack(FFFFakePlayer bot, Player attacker) {
        attackerMemory.putIfAbsent(bot, new ConcurrentHashMap<>());
        Map<Player, Integer> attackers = attackerMemory.get(bot);

        attackers.put(attacker, attackers.getOrDefault(attacker, 0) + 1);
    }

    public static Player getMostFrequentAttacker(FFFFakePlayer bot) {
        Map<Player, Integer> attackers = attackerMemory.get(bot);
        if (attackers == null || attackers.isEmpty()) return null;

        return attackers.entrySet().stream()
            .max((a, b) -> Integer.compare(a.getValue(), b.getValue()))
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public static void resetMemory(FFFFakePlayer bot) {
        attackerMemory.remove(bot);
    }

    public static boolean hasBeenAttackedBy(FFFFakePlayer bot, Player attacker) {
        Map<Player, Integer> attackers = attackerMemory.get(bot);
        return attackers != null && attackers.containsKey(attacker);
    }

    public static int getHitCount(FFFFakePlayer bot, Player attacker) {
        Map<Player, Integer> attackers = attackerMemory.get(bot);
        return (attackers != null) ? attackers.getOrDefault(attacker, 0) : 0;
    }
}
