package dre.elfocrash.roboto.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkillBookLoader {

    // Static skill list ανά classId
    private static final Map<Integer, Set<Integer>> allowedSkillsMap = new HashMap<>();

    static {
        // Mage (classId: 10)
        allowedSkillsMap.put(10, Set.of(11, 15, 20)); // Silence, Fireball, Magic Burst

        // Warrior (classId: 3)
        allowedSkillsMap.put(3, Set.of(2, 5, 7)); // Stun, Charge, Heavy Slash

        // Healer (classId: 92)
        allowedSkillsMap.put(92, Set.of(30, 33, 40)); // Heal, Mass Heal, Block Cancel

        // Default fallback
        allowedSkillsMap.put(-1, Set.of(1, 2, 3, 4, 5));
    }

    public static Set<Integer> getAllowedSkillsForClass(int classId) {
        return allowedSkillsMap.getOrDefault(classId, allowedSkillsMap.get(-1));
    }
}
