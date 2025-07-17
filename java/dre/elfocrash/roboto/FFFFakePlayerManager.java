package dre.elfocrash.roboto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public enum FFFFakePlayerManager {
    INSTANCE;
	
	public boolean isNameInUse(String name) {
    return ACTIVE_FAKE_PLAYERS.stream().anyMatch(bot -> bot.getName().equalsIgnoreCase(name));
		    }
    private static final Logger _log = Logger.getLogger(FFFFakePlayerManager.class.getName());

    private static final List<Integer> FAKE_CLAN_IDS = FFFFakeHelpers.loadClanIdList("data/fakebots/clan-list-bot-farm-ffff.txt");

    private static int _fakeClanTableId = 0;

    private static final List<Location> SPAWN_LOCATIONS = FFFFakeHelpers.loadSpawnLocations("data/fakebots/spawn-list-bot-farm-ffff.txt");

    private static final List<Player> ACTIVE_FAKE_PLAYERS = new CopyOnWriteArrayList<>();
    private static final List<Player> AUTO_SPAWNED_FAKE_PLAYERS = new CopyOnWriteArrayList<>();
    private static final List<Player> GM_FAKE_PLAYERS = new CopyOnWriteArrayList<>();
    private static final int MAX_BOTS = 50;
    private static final int BOTS_PER_CYCLE = 6;
    private static final long START_DELAY = 60 * 60 * 1000L; // 1 ώρα

    public void initialise() {
        FFFFakePlayerNameManager.INSTANCE.initialise();
        FFFFakePlayerTaskManager.INSTANCE.initialise();
        ThreadPool.schedule(() -> startBotSystem(), START_DELAY);
    }

    private void startBotSystem() {
        _log.info("Bot system starting after 1-hour delay.");
        spawnInitialBots();
        scheduleBotCycle();
    }

    private void spawnInitialBots() {
    	    _log.info("Spawning first 6 fake players...");
    	    spawnFakeBots(6);
    	
    	        _log.info("Scheduling gradual bot spawn every 20–30 minutes...");
    	        long interval = Rnd.get(20, 30) * 60 * 1000L;

    	        ThreadPool.scheduleAtFixedRate(() -> {
    	           _log.info("Bot cycle triggered: refreshing 6 bots (1 per class).");

    	            List<Integer> fixedClassIds = Arrays.asList(123, 124, 125, 126, 127, 112);

    	            for (int classId : fixedClassIds) {
    	                Player toRemove = ACTIVE_FAKE_PLAYERS.stream()
    	                    .filter(bot -> bot.getClassId().getId() == classId)
    	                   .findFirst().orElse(null);

    	                if (toRemove != null) {
    	                   _log.info("Deleting bot [" + toRemove.getName() + "] of classId=" + classId);
    	                   toRemove.deleteMe();
    	                    ACTIVE_FAKE_PLAYERS.remove(toRemove);
    	                }

    	                if (AUTO_SPAWNED_FAKE_PLAYERS.size() >= MAX_BOTS) {
    	                    _log.info("Reached MAX_BOTS, skipping spawn for classId=" + classId);
    	                    continue;
    	                }

    	                Location base = Rnd.get(SPAWN_LOCATIONS);
    	                Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
    	                FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
    	                bot.assignDefaultAI();
    	                FFFFakeHelpers.registerBotStats(bot, false);
    	                ACTIVE_FAKE_PLAYERS.add(bot);
    	                AUTO_SPAWNED_FAKE_PLAYERS.add(bot);
    	                _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
    	            }
    	        }, interval, interval);
    	    }
    	
    	    private void spawnFakeBots(int count) {
    	        for (int i = 0; i < count; i++) {
    	            Location base = Rnd.get(SPAWN_LOCATIONS);
    	            Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
    	            int classId = getRandomClass();
    	            FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
    	            bot.assignDefaultAI();
    	            FFFFakeHelpers.registerBotStats(bot, false);
    	            ACTIVE_FAKE_PLAYERS.add(bot);
    	            AUTO_SPAWNED_FAKE_PLAYERS.add(bot);
    	            _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
    	        }
    }

    private void scheduleBotCycle() {
        long interval = Rnd.get(20, 30) * 60 * 1000L;
        _log.info("Scheduling bot cycle every " + (interval / 60000) + " minutes.");

        ThreadPool.scheduleAtFixedRate(() -> {
            _log.info("Bot cycle triggered: removing and spawning " + BOTS_PER_CYCLE + " bots.");

            int removed = 0;
            for (Player bot : AUTO_SPAWNED_FAKE_PLAYERS) {
                if (removed >= BOTS_PER_CYCLE)
                    break;
                _log.info("Deleting bot [" + bot.getName() + "]");
                bot.deleteMe();
                ACTIVE_FAKE_PLAYERS.remove(bot);
                AUTO_SPAWNED_FAKE_PLAYERS.remove(bot);
                removed++;
            }

            for (int i = 0; i < BOTS_PER_CYCLE; i++) {
            	if (AUTO_SPAWNED_FAKE_PLAYERS.size() >= MAX_BOTS)
                    break;
                Location base = Rnd.get(SPAWN_LOCATIONS);
                Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
                int classId = getRandomClass();
                FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
                bot.assignDefaultAI();
                ACTIVE_FAKE_PLAYERS.add(bot);
                _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
            }
        }, interval, interval);
    }

    private int getRandomClass() {
    	   
    	    List<Integer> classIds = Arrays.asList(
    	        123, // Soultaker
    	        124, // Mystic Muse
    	        125, // Storm Screamer
    	        126, // Titan
    	        127, // Grand Khavatari
    	        112  // Cardinal
    	    );
    	    return Rnd.get(classIds);
    }

    public FFFFakePlayer spawnPlayer(int x, int y, int z, int classDesired) {
    	
    	   String name = FFFFakePlayerNameManager.INSTANCE.getRandomAvailableGMName();
    	    if (name == null || name.trim().isEmpty()) {
    	        name = "GM_Bot_" + System.currentTimeMillis();
    	        _log.warning("⚠ No GM name available. Fallback assigned: " + name);
    	    }
    	    FFFFakePlayer activeChar = FFFFakeHelpers.createRandomFFFFakePlayer(classDesired);
    	    activeChar.setName(name);
        World.getInstance().addPlayer(activeChar);

        Clan clan = null;
        while (clan == null) {
        clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
         }

        clan.addClanMember(activeChar);
        activeChar.setClan(clan);
        activeChar.setPledgeClass(ClanMember.calculatePledgeClass(activeChar));
        activeChar.setClanPrivileges(clan.getRankPrivs(activeChar.getPowerGrade()));
        activeChar.setTitle("【" + clan.getName() + "】");
        handlePlayerClanOnSpawn(activeChar);

        activeChar.spawnMe(x, y, z);
        activeChar.onPlayerEnter();
        activeChar.heal();
        activeChar.broadcastUserInfo();
        
        _log.info("✅ Spawned GM bot [" + activeChar.getName() + "] with clan [" + clan.getName() + "] at (" + x + "," + y + "," + z + ")");
        return activeChar;
    }

    private static void handlePlayerClanOnSpawn(FFFFakePlayer activeChar) {
        final Clan clan = activeChar.getClan();
        if (clan != null) {
            clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);

            final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN).addCharName(activeChar);
            final PledgeShowMemberListUpdate update = new PledgeShowMemberListUpdate(activeChar);

            for (Player member : clan.getOnlineMembers()) {
                if (member == activeChar)
                    continue;
                member.sendPacket(msg);
                member.sendPacket(update);
            }

            for (Castle castle : CastleManager.getInstance().getCastles()) {
                final Siege siege = castle.getSiege();
                if (!siege.isInProgress())
                    continue;

                final SiegeSide type = siege.getSide(clan);
                if (type == SiegeSide.ATTACKER)
                    activeChar.setSiegeState((byte) 1);
                else if (type == SiegeSide.DEFENDER || type == SiegeSide.OWNER)
                    activeChar.setSiegeState((byte) 2);
            }
        }
    }

    public int getFFFFakePlayersCount() {
        return ACTIVE_FAKE_PLAYERS.size();
    }

    public void deleteAllFFFFakes() {
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            _log.info("Deleting bot [" + bot.getName() + "]");
            bot.deleteMe();
        }
        ACTIVE_FAKE_PLAYERS.clear();
        GM_FAKE_PLAYERS.clear(); //  Full clean
    }

    public void deleteAllFFFFakes(int count, int radius, int x, int y) {
        int deleted = 0;
        List<Player> toRemove = new ArrayList<>();
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            if (deleted >= count)
                break;

            double dx = bot.getX() - x;
            double dy = bot.getY() - y;
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                _log.info("Deleting bot [" + bot.getName() + "] within radius.");
                bot.deleteMe();
                GM_FAKE_PLAYERS.remove(bot); // Remove GM bot if present
                toRemove.add(bot);
                deleted++;
            }
        }
        ACTIVE_FAKE_PLAYERS.removeAll(toRemove);
    }

    public void spawnPlayers(int count, int radius, int x, int y, int z, int classId) {
        for (int i = 0; i < count; i++) {
            int offsetX = Rnd.get(-radius, radius);
            int offsetY = Rnd.get(-radius, radius);
            Location loc = new Location(x + offsetX, y + offsetY, z);
            FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
            bot.assignDefaultAI();
            GM_FAKE_PLAYERS.add(bot); //  Save GM-spawned bot
            FFFFakeHelpers.registerBotStats(bot, true); //  Give PvP stats + persist to DB
            FFFFakeHelpers.markGMBotSpawn(bot);
            ACTIVE_FAKE_PLAYERS.add(bot);
            _log.info("Spawned bot [" + bot.getName() + "] (classId=" + classId + ") at " + loc);
        }
    }

    @SuppressWarnings("unchecked")
    public List<FFFFakePlayer> getFFFFakePlayers() {
        return (List<FFFFakePlayer>) (List<?>) ACTIVE_FAKE_PLAYERS;
    }

    public List<Player> getGMFakePlayers() {
        return GM_FAKE_PLAYERS;
    }
        public List<Player> getAutoSpawnedFakePlayers() {
    	        return AUTO_SPAWNED_FAKE_PLAYERS;
    	    }

    	    public List<Player> getFakePlayers() {
    	        return ACTIVE_FAKE_PLAYERS;
    	    }
        public void deleteFFFFake(String name) {
    	        Player bot = ACTIVE_FAKE_PLAYERS.stream()
    	            .filter(p -> p.getName().equalsIgnoreCase(name))
    	            .findFirst().orElse(null);
    	
    	        if (bot != null) {
    	            bot.deleteMe();
    	            ACTIVE_FAKE_PLAYERS.remove(bot);
    	            GM_FAKE_PLAYERS.remove(bot);
    	            AUTO_SPAWNED_FAKE_PLAYERS.remove(bot);
    	            _log.info("Deleted bot [" + bot.getName() + "]");
    	        } else {
    	            _log.warning("Bot not found: " + name);
    	        }
    	    } 
            public void spawnNamedPlayer(String name, Player gm) {
            	    Location loc = new Location(gm.getX(), gm.getY(), gm.getZ()); // Spawn κοντά στον GM
        	        int classId = getRandomClass();  // Random class
        	        FFFFakePlayer bot = FFFFakeHelpers.createRandomFFFFakePlayer(classId);
        	        bot.setName(name); // Αν δεν υπάρχει setName, πες μου να το φτιάξουμε
        	        World.getInstance().addPlayer(bot);
        	
        	        Clan clan = null;
        	        while (clan == null) {
        	            clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
        	        }
        	
        	        clan.addClanMember(bot);
        	        bot.setClan(clan);
        	        bot.setPledgeClass(ClanMember.calculatePledgeClass(bot));
        	        bot.setClanPrivileges(clan.getRankPrivs(bot.getPowerGrade()));
        	
        	        handlePlayerClanOnSpawn(bot);
        	
        	        bot.spawnMe(loc.getX(), loc.getY(), loc.getZ());
        	        bot.onPlayerEnter();
        	        bot.heal();
        	        bot.assignDefaultAI();
        	        FFFFakeHelpers.registerBotStats(bot, true);
        	        FFFFakeHelpers.markGMBotSpawn(bot);
        	        ACTIVE_FAKE_PLAYERS.add(bot);
        	        GM_FAKE_PLAYERS.add(bot);
        	        _log.info("Spawned GM bot [" + bot.getName() + "] near " + gm.getName());
        	    }      
}
