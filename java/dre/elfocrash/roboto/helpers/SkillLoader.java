package dre.elfocrash.roboto.helpers;

import java.util.List;
import java.util.ArrayList;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.data.SkillTable;

public class SkillLoader {

    public static List<L2Skill> loadSkillsForClass(String aiClassName) {
        List<L2Skill> skills = new ArrayList<>();

        switch (aiClassName) {
            case "SSSSoultakerAI":
                skills.add(SkillTable.getInstance().getInfo(121, 1)); // Death Spike
                skills.add(SkillTable.getInstance().getInfo(122, 1)); // Vampiric Claw
                skills.add(SkillTable.getInstance().getInfo(123, 1)); // Curse of Doom
                break;

            case "MMMMysticMuseAI":
                skills.add(SkillTable.getInstance().getInfo(140, 1)); // Ice Vortex
                skills.add(SkillTable.getInstance().getInfo(141, 1)); // Hydro Blast
                skills.add(SkillTable.getInstance().getInfo(142, 1)); // Raging Waves
                break;

            case "SSSStormScreamerAI":
                skills.add(SkillTable.getInstance().getInfo(150, 1)); // Wind Vortex
                skills.add(SkillTable.getInstance().getInfo(151, 1)); // Cyclone
                skills.add(SkillTable.getInstance().getInfo(152, 1)); // Arcane Chaos
                break;

            case "TTTTitanAI":
                skills.add(SkillTable.getInstance().getInfo(200, 1)); // Frenzy
                skills.add(SkillTable.getInstance().getInfo(201, 1)); // Guts
                skills.add(SkillTable.getInstance().getInfo(202, 1)); // Rage
                break;

            case "GGGGrandKhavatariAI":
                skills.add(SkillTable.getInstance().getInfo(220, 1)); // Raging Force
                skills.add(SkillTable.getInstance().getInfo(221, 1)); // Force Barrier
                skills.add(SkillTable.getInstance().getInfo(222, 1)); // Symbol of Energy
                break;

            default:
                // Fallback generic skills
                skills.add(SkillTable.getInstance().getInfo(5, 1)); // Generic Stun
                break;
        }

        return skills;
    }
}
