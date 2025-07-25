package alt.elfocrash.roboto.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.FFFFFakePlayerNameManager;
import alt.elfocrash.roboto.ai.AAAAAdventurerAI;
import alt.elfocrash.roboto.ai.AAAAArchmageAI;
import alt.elfocrash.roboto.ai.CCCCCardinalAI;
import alt.elfocrash.roboto.ai.DDDDDominatorAI;
import alt.elfocrash.roboto.ai.DDDDDreadnoughtAI;
import alt.elfocrash.roboto.ai.DDDDDuelistAI;
import alt.elfocrash.roboto.ai.FFFFFakePlayerAI;
import alt.elfocrash.roboto.ai.FFFFFallbackAI;
import alt.elfocrash.roboto.ai.GGGGGhostHunterAI;
import alt.elfocrash.roboto.ai.GGGGGhostSentinelAI;
import alt.elfocrash.roboto.ai.GGGGGrandKhavatariAI;
import alt.elfocrash.roboto.ai.MMMMMoonlightSentinelAI;
import alt.elfocrash.roboto.ai.MMMMMysticMuseAI;
import alt.elfocrash.roboto.ai.SSSSSaggitariusAI;
import alt.elfocrash.roboto.ai.SSSSSoultakerAI;
import alt.elfocrash.roboto.ai.SSSSStormScreamerAI;
import alt.elfocrash.roboto.ai.TTTTTitanAI;
import alt.elfocrash.roboto.ai.WWWWWindRiderAI;

import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.data.xml.PlayerData;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.ClassRace;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.model.base.Sex;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class FFFFFakeHelpers {
	
	public static int[][] getFighterBuffs() {
		return new int[][] { 
			{ 264, 1 },
			{ 265, 1 },
			{ 266, 1 },
			{ 267, 1 },
			{ 268, 1 },
			{ 269, 1 },
			{ 270, 1 },
			{ 271, 1 },
			{ 272, 1 },
			{ 273, 1 },
			{ 274, 1 },
			{ 275, 1 },
			{ 276, 1 },
			{ 277, 1 },
			{ 304, 1 },
			{ 305, 1 },
			{ 306, 1 },
			{ 307, 1 },
			{ 308, 1 },
			{ 309, 1 },
			{ 310, 1 },
			{ 311, 1 },
			{ 1363, 1 },
			{ 1068, 3 },
			{ 1182, 3 },
			{ 1189, 3 },
			{ 1191, 3 },
			{ 1204, 2 },
			{ 1240, 3 },
			{ 1242, 3 },
			{ 1243, 6 },
			{ 1259, 4 },
			{ 1392, 3 },
			{ 1393, 3 },
			{ 1352, 1 },
			{ 1353, 1 },
			{ 1354, 1 },
			{ 1036, 2 },
			{ 1040, 3 },
			{ 1043, 1 },
			{ 1045, 6 },
			{ 1048, 6 },
			{ 1059, 3 },
			{ 1062, 2 },
			{ 1077, 3 },
			{ 1078, 6 },
			{ 1085, 3 },
			{ 1086, 2 },
			{ 1087, 3 },
			{ 4703, 13 },
			{ 4699, 13 },
			{ 7265, 1 },	
		};
	}

	public static int[][] getMageBuffs() {
		return new int[][] {
			{ 264, 1 },
			{ 265, 1 },
			{ 266, 1 },
			{ 267, 1 },
			{ 268, 1 },
			{ 269, 1 },
			{ 270, 1 },
			{ 271, 1 },
			{ 272, 1 },
			{ 273, 1 },
			{ 274, 1 },
			{ 275, 1 },
			{ 276, 1 },
			{ 277, 1 },
			{ 304, 1 },
			{ 305, 1 },
			{ 306, 1 },
			{ 307, 1 },
			{ 308, 1 },
			{ 309, 1 },
			{ 310, 1 },
			{ 311, 1 },
			{ 1363, 1 },
			{ 1068, 3 },
			{ 1182, 3 },
			{ 1189, 3 },
			{ 1191, 3 },
			{ 1204, 2 },
			{ 1240, 3 },
			{ 1242, 3 },
			{ 1243, 6 },
			{ 1259, 4 },
			{ 1392, 3 },
			{ 1393, 3 },
			{ 1352, 1 },
			{ 1353, 1 },
			{ 1354, 1 },
			{ 1036, 2 },
			{ 1040, 3 },
			{ 1043, 1 },
			{ 1045, 6 },
			{ 1048, 6 },
			{ 1059, 3 },
			{ 1062, 2 },
			{ 1077, 3 },
			{ 1078, 6 },
			{ 1085, 3 },
			{ 1086, 2 },
			{ 1087, 3 },
			{ 4703, 13 },
			{ 4699, 13 },
			{ 7265, 1 },	
		};
	}
	

	public static Class<? extends Creature> getTestTargetClass() {
		return Monster.class;
	}

	public static int getTestTargetRange() {
		return 2000;
	}
	
	public static FFFFFakePlayer createRandomFFFFFakePlayer() {
		
		int objectId = IdFactory.getInstance().getNextId();
		String accountName = "AutoPilot";
		String playerName = FFFFFakePlayerNameManager.INSTANCE.getRandomAvailableName();

		ClassId classId = getThirdClasses().get(Rnd.get(0, getThirdClasses().size() - 1));

		final PlayerTemplate template = PlayerData.getInstance().getTemplate(classId);
		PcAppearance app = getRandomAppearance(template.getRace());
		FFFFFakePlayer player = new FFFFFakePlayer(objectId, template, accountName, app);

		player.setName(playerName);	
		player.setAccessLevel(Config.DEFAULT_ACCESS_LEVEL);
		PlayerInfoTable.getInstance().addPlayer(objectId, accountName, playerName, player.getAccessLevel().getLevel());
		player.setBaseClass(player.getClassId());
		setLevel(player, 81);
		//player.setTitle("");
		player.rewardSkills();
		giveArmorsByClass(player, true);
		giveWeaponsByClass(player,true);
		player.heal();
				
		return player;
	}

	public static void giveArmorsByClass(FFFFFakePlayer player, boolean randomlyEnchant) {
		List<Integer> itemIds = new ArrayList<>();
		switch (player.getClassId()) {
		case ARCHMAGE:
		case SOULTAKER:
		case HIEROPHANT:
		case ARCANA_LORD:
		case CARDINAL:
		case MYSTIC_MUSE:
		case ELEMENTAL_MASTER:
		case EVAS_SAINT:
		case STORM_SCREAMER:
		case SPECTRAL_MASTER:
		case SHILLIEN_SAINT:
		case DOMINATOR:
		case DOOMCRYER:
			itemIds = Arrays.asList(2407, 512, 5767, 5779, 9529, 9532, 9531, 9533, 9530);
			break;
		case DUELIST:
		case DREADNOUGHT:
		case PHOENIX_KNIGHT:
		case SWORD_MUSE:
		case HELL_KNIGHT:
		case SPECTRAL_DANCER:
		case EVAS_TEMPLAR:
		case SHILLIEN_TEMPLAR:
		case TITAN:
		case MAESTRO:		
			itemIds = Arrays.asList(6373, 6374, 6378, 6375, 6376, 9529, 9532, 9531, 9533, 9530);
			break;
		case SAGGITARIUS:
		case ADVENTURER:
		case WIND_RIDER:
		case MOONLIGHT_SENTINEL:
		case GHOST_HUNTER:
		case GHOST_SENTINEL:
		case FORTUNE_SEEKER:
		case GRAND_KHAVATARI:
			itemIds = Arrays.asList(6379, 6382, 6380, 6381, 9529, 9532, 9531, 9533, 9530);
			break;
		default:
			break;
		}
		for (int id : itemIds) {
			player.getInventory().addItem("Armors", id, 1, player, null);
			ItemInstance item = player.getInventory().getItemByItemId(id);

			if (randomlyEnchant) {
				item.setEnchantLevel(Rnd.get(40, 48));
			}				
			
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
			player.broadcastCharInfo();
		}
	}

	public static void giveWeaponsByClass(FFFFFakePlayer player, boolean randomlyEnchant) {
		List<Integer> itemIds = new ArrayList<>();
		switch (player.getClassId()) {
		case FORTUNE_SEEKER:
		case GHOST_HUNTER:
		case WIND_RIDER:
		case ADVENTURER:
			itemIds = Arrays.asList(10668);
			break;
		case SAGGITARIUS:
		case MOONLIGHT_SENTINEL:
		case GHOST_SENTINEL:
			itemIds = Arrays.asList(10669);
			break;
		case PHOENIX_KNIGHT:
		case SWORD_MUSE:
		case HELL_KNIGHT:
		case EVAS_TEMPLAR:
		case SHILLIEN_TEMPLAR:
			itemIds = Arrays.asList(10666, 6377);
			break;
		case MAESTRO:
			itemIds = Arrays.asList(10668, 6377);
			break;
		case TITAN:
			itemIds = Arrays.asList(6372);
			break;
		case DUELIST:
		case SPECTRAL_DANCER:
			itemIds = Arrays.asList(10673);
			break;
		case DREADNOUGHT:
			itemIds = Arrays.asList(10670);
			break;
		case ARCHMAGE:
		case SOULTAKER:
		case HIEROPHANT:
		case ARCANA_LORD:
		case CARDINAL:
		case MYSTIC_MUSE:
		case ELEMENTAL_MASTER:
		case EVAS_SAINT:
		case STORM_SCREAMER:
		case SPECTRAL_MASTER:
		case SHILLIEN_SAINT:
		case DOMINATOR:
		case DOOMCRYER:
			itemIds = Arrays.asList(6579);
			break;
		case GRAND_KHAVATARI:
			itemIds = Arrays.asList(6371);
			break;
		default:
			break;
		}
		for (int id : itemIds) {
			player.getInventory().addItem("Weapon", id, 1, player, null);
			ItemInstance item = player.getInventory().getItemByItemId(id);
			if(randomlyEnchant)
			item.setEnchantLevel(Rnd.get(40, 48));
			player.getInventory().equipItemAndRecord(item);
			player.getInventory().reloadEquippedItems();
		}
	}

	public static List<ClassId> getThirdClasses() {
		// removed summoner classes because fuck those guys
		List<ClassId> classes = new ArrayList<>();

		/*
		 * classes.add(ClassId.EVAS_SAINT); classes.add(ClassId.SHILLIEN_TEMPLAR);
		 * classes.add(ClassId.SPECTRAL_DANCER); classes.add(ClassId.GHOST_HUNTER);
		 * 
		 * classes.add(ClassId.PHOENIX_KNIGHT);
		 * classes.add(ClassId.HELL_KNIGHT);
		 * 
		 * classes.add(ClassId.HIEROPHANT); classes.add(ClassId.EVAS_TEMPLAR);
		 * classes.add(ClassId.SWORD_MUSE);
		 * 
		 * classes.add(ClassId.DOOMCRYER); classes.add(ClassId.FORTUNE_SEEKER);
		 * classes.add(ClassId.MAESTRO);
		 */

		// classes.add(ClassId.ARCANA_LORD);
		// classes.add(ClassId.ELEMENTAL_MASTER);
		// classes.add(ClassId.SPECTRAL_MASTER);
		// classes.add(ClassId.SHILLIEN_SAINT);

		//classes.add(ClassId.SAGGITARIUS);
		//classes.add(ClassId.ARCHMAGE);
		classes.add(ClassId.SOULTAKER);
		classes.add(ClassId.MYSTIC_MUSE);
		classes.add(ClassId.STORM_SCREAMER);
		//classes.add(ClassId.MOONLIGHT_SENTINEL);
		//classes.add(ClassId.GHOST_SENTINEL);
		//classes.add(ClassId.ADVENTURER);
		//classes.add(ClassId.WIND_RIDER);
		classes.add(ClassId.DOMINATOR);
		classes.add(ClassId.TITAN);
		//classes.add(ClassId.CARDINAL);
		//classes.add(ClassId.DUELIST);
		classes.add(ClassId.GRAND_KHAVATARI);
		//classes.add(ClassId.DREADNOUGHT);
		
		return classes;
	}

	public static Map<ClassId, Class<? extends FFFFFakePlayerAI>> getAllAIs() {
		Map<ClassId, Class<? extends FFFFFakePlayerAI>> ais = new HashMap<>();
		ais.put(ClassId.STORM_SCREAMER, SSSSStormScreamerAI.class);
		ais.put(ClassId.MYSTIC_MUSE, MMMMMysticMuseAI.class);
		ais.put(ClassId.ARCHMAGE, AAAAArchmageAI.class);
		ais.put(ClassId.SOULTAKER, SSSSSoultakerAI.class);
		ais.put(ClassId.SAGGITARIUS, SSSSSaggitariusAI.class);
		ais.put(ClassId.MOONLIGHT_SENTINEL, MMMMMoonlightSentinelAI.class);
		ais.put(ClassId.GHOST_SENTINEL, GGGGGhostSentinelAI.class);
		ais.put(ClassId.ADVENTURER, AAAAAdventurerAI.class);
		ais.put(ClassId.WIND_RIDER, WWWWWindRiderAI.class);
		ais.put(ClassId.GHOST_HUNTER, GGGGGhostHunterAI.class);
		ais.put(ClassId.DOMINATOR, DDDDDominatorAI.class);
		ais.put(ClassId.TITAN, TTTTTitanAI.class);
		ais.put(ClassId.CARDINAL, CCCCCardinalAI.class);
		ais.put(ClassId.DUELIST, DDDDDuelistAI.class);
		ais.put(ClassId.GRAND_KHAVATARI, GGGGGrandKhavatariAI.class);
		ais.put(ClassId.DREADNOUGHT, DDDDDreadnoughtAI.class);
		return ais;
	}

	public static PcAppearance getRandomAppearance(ClassRace race) {

		Sex randomSex = Rnd.get(1, 2) == 1 ? Sex.MALE : Sex.FEMALE;
		int hairStyle = Rnd.get(0, randomSex == Sex.MALE ? 4 : 6);
		int hairColor = Rnd.get(0, 3);
		int faceId = Rnd.get(0, 2);

		return new PcAppearance((byte) faceId, (byte) hairColor, (byte) hairStyle, randomSex);
	}

	public static void setLevel(FFFFFakePlayer player, int level) {
		if (level >= 1 && level <= Experience.MAX_LEVEL) {
			long pXp = player.getExp();
			long tXp = Experience.LEVEL[81];

			if (pXp > tXp)
				player.removeExpAndSp(pXp - tXp, 0);
			else if (pXp < tXp)
				player.addExpAndSp(tXp - pXp, 0);
		}
	}

	public static Class<? extends FFFFFakePlayerAI> getAIbyClassId(ClassId classId) {
		Class<? extends FFFFFakePlayerAI> ai = getAllAIs().get(classId);
		if (ai == null)
			return FFFFFallbackAI.class;

		return ai;
	}
}
