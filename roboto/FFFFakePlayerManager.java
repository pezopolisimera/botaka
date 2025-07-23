package dre.elfocrash.roboto;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public enum FFFFakePlayerManager {
    INSTANCE;

    private static final Logger _log = Logger.getLogger(FFFFakePlayerManager.class.getName());
    private static final List<Integer> FAKE_CLAN_IDS = FFFFakeHelpers.loadClanIdList("data/fakebots/clan-list-bot-farm-ffff.txt");
    private static final List<Location> SPAWN_LOCATIONS = FFFFakeHelpers.loadSpawnLocations("data/fakebots/spawn-list-bot-farm-ffff.txt");

    private static int _fakeClanTableId = 0;
    private static final List<Player> ACTIVE_FAKE_PLAYERS = new CopyOnWriteArrayList<>();
    private static final int MAX_BOTS = 50;
    private static final int BOTS_PER_CYCLE = 5;
    private static final long START_DELAY = 60 * 60 * 1000L; // 1 ώρα

    private final List<String> gmSpawnedBotNames = new ArrayList<>();
    private final List<String> autoSpawnedBotNames = new ArrayList<>();

    public List<String> getLatestGMSpawnedBotNames() {
        return gmSpawnedBotNames;
    }

    public List<String> getLatestAutoSpawnedBotNames() {
        return autoSpawnedBotNames;
    }

    public void initialise() {
        FFFFakePlayerNameManager.INSTANCE.initialise();
        FFFFakePlayerTaskManager.INSTANCE.initialise();
        ThreadPool.schedule(() -> startBotSystem(), START_DELAY);
        ThreadPool.scheduleAtFixedRate(() -> cleanGhosts(), 5 * 60 * 60 * 1000L, 5 * 60 * 60 * 1000L);
    }

    private void startBotSystem() {
        _log.info("Bot system starting after 1-hour delay.");
        spawnInitialBots();
        scheduleBotCycle();
    }

    private void spawnInitialBots() {
        _log.info("Spawning first 5 fake players...");
        spawnFakeBots(5);

        ThreadPool.scheduleAtFixedRate(() -> {
            if (ACTIVE_FAKE_PLAYERS.size() >= MAX_BOTS) return;
            int toSpawn = Math.min(5, MAX_BOTS - ACTIVE_FAKE_PLAYERS.size());
            spawnFakeBots(toSpawn);
        }, 5 * 60 * 1000L, 5 * 60 * 1000L);
    }

    private void spawnFakeBots(int count) {
        for (int i = 0; i < count; i++) {
            Location base = Rnd.get(SPAWN_LOCATIONS);
            Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
            int classId = getRandomClass();
            if (!isAllowedClass(classId)) continue;
            FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
            bot.assignDefaultAI();
            bot.setSpawnLocation(new Location(loc.getX(), loc.getY(), loc.getZ()));
            FFFFakePlayerTaskManager.INSTANCE.startTask(bot); //  fix
            ACTIVE_FAKE_PLAYERS.add(bot);
            autoSpawnedBotNames.add(bot.getName());
        }
    }

    private boolean isAllowedClass(int classId) {
        return List.of(112, 109, 114, 117, 118).contains(classId);
    }

    private void scheduleBotCycle() {
        long interval = Rnd.get(20, 30) * 60 * 1000L;
        ThreadPool.scheduleAtFixedRate(() -> {
            int removed = 0;
            for (Player bot : ACTIVE_FAKE_PLAYERS) {
                if (removed >= BOTS_PER_CYCLE) break;
                bot.deleteMe();
                ACTIVE_FAKE_PLAYERS.remove(bot);
                removed++;
            }
            for (int i = 0; i < BOTS_PER_CYCLE && ACTIVE_FAKE_PLAYERS.size() < MAX_BOTS; i++) {
                Location base = Rnd.get(SPAWN_LOCATIONS);
                Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
                int classId = getRandomClass();
                FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
                bot.assignDefaultAI();
                bot.setSpawnLocation(new Location(loc.getX(), loc.getY(), loc.getZ()));
                FFFFakePlayerTaskManager.INSTANCE.startTask(bot); //  fix
                ACTIVE_FAKE_PLAYERS.add(bot);
                autoSpawnedBotNames.add(bot.getName());
            }
        }, interval, interval);
    }

    private int getRandomClass() {
        return Rnd.get(List.of(112, 109, 114, 117, 118));
    }

    public FFFFakePlayer spawnPlayer(int x, int y, int z, int classDesired) {
        FFFFakePlayer bot = FFFFakeHelpers.createRandomFFFFakePlayer(classDesired);
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

        bot.spawnMe(x, y, z);
        bot.onPlayerEnter();
        bot.heal();

        return bot;
    }

    private static void handlePlayerClanOnSpawn(FFFFakePlayer bot) {
        Clan clan = bot.getClan();
        if (clan != null) {
            clan.getClanMember(bot.getObjectId()).setPlayerInstance(bot);
            SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN).addCharName(bot);
            PledgeShowMemberListUpdate update = new PledgeShowMemberListUpdate(bot);
            for (Player member : clan.getOnlineMembers()) {
                if (member != bot) {
                    member.sendPacket(msg);
                    member.sendPacket(update);
                }
            }
            for (Castle castle : CastleManager.getInstance().getCastles()) {
                Siege siege = castle.getSiege();
                if (!siege.isInProgress()) continue;
                SiegeSide side = siege.getSide(clan);
                if (side == SiegeSide.ATTACKER)
                    bot.setSiegeState((byte) 1);
                else if (side == SiegeSide.DEFENDER || side == SiegeSide.OWNER)
                    bot.setSiegeState((byte) 2);
            }
        }
    }

    public void deleteAllFFFFakes() {
        for (Player bot : ACTIVE_FAKE_PLAYERS) bot.deleteMe();
        ACTIVE_FAKE_PLAYERS.clear();
    }

    public boolean deleteFakeBotByName(String name) {
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            if (bot.getName().equalsIgnoreCase(name)) {
                bot.deleteMe();
                ACTIVE_FAKE_PLAYERS.remove(bot);
                gmSpawnedBotNames.remove(name);
                autoSpawnedBotNames.remove(name);
                return true;
            }
        }
        return false;
    }

    public void deleteAllFFFFakes(int count, int radius, int x, int y) {
        int deleted = 0;
        List<Player> toRemove = new ArrayList<>();
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            if (deleted >= count) break;
            double dx = bot.getX() - x;
            double dy = bot.getY() - y;
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                bot.deleteMe();
                toRemove.add(bot);
                deleted++;
            }
        }
        ACTIVE_FAKE_PLAYERS.removeAll(toRemove);
    }

    public List<FFFFakePlayer> getFFFFakePlayers() {
        return (List<FFFFakePlayer>) (List<?>) ACTIVE_FAKE_PLAYERS;
    }

    public int getFFFFakePlayersCount() {
        return ACTIVE_FAKE_PLAYERS.size();
    }

    public void spawnPlayers(int count, int radius, int x, int y, int z, int classId, boolean gmMode) {
        for (int i = 0; i < count; i++) {
            int offsetX = Rnd.get(-radius, radius);
            int offsetY = Rnd.get(-radius, radius);
            Location loc = new Location(x + offsetX, y + offsetY, z);
            FFFFakePlayer bot = gmMode ? FFFFakeHelpers.createRandomFFFFakePlayer(true, classId) : FFFFakeHelpers.createRandomFFFFakePlayer(false, classId);
            World.getInstance().addPlayer(bot);
            Clan clan = null;
            while (clan == null) clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
            clan.addClanMember(bot);
            bot.setClan(clan);
            bot.setPledgeClass(ClanMember.calculatePledgeClass(bot));
            bot.setClanPrivileges(clan.getRankPrivs(bot.getPowerGrade()));
            handlePlayerClanOnSpawn(bot);
            bot.spawnMe(loc.getX(), loc.getY(), loc.getZ());
            bot.onPlayerEnter();
            bot.heal();
            bot.assignDefaultAI();
            bot.setSpawnLocation(new Location(loc.getX(), loc.getY(), loc.getZ()));
            FFFFakePlayerTaskManager.INSTANCE.startTask(bot); // ✅ fix
            ACTIVE_FAKE_PLAYERS.add(bot);
            if (gmMode)
                gmSpawnedBotNames.add(bot.getName());
            else
                autoSpawnedBotNames.add(bot.getName());
        }
    }

    public void cleanGhosts() {
        Iterator<Player> iter = ACTIVE_FAKE_PLAYERS.iterator();
        while (iter.hasNext()) {
            Player p = iter.next();
            if (!p.isOnline() || p.isTeleporting()) {
                iter.remove();
                gmSpawnedBotNames.remove(p.getName());
                autoSpawnedBotNames.remove(p.getName());
            }
        }
    }
        public List<Player> getActiveFakePlayers() {
    	        return ACTIVE_FAKE_PLAYERS;
    	    }
    	 }
