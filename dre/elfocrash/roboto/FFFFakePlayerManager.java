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

    private static final Logger _log = Logger.getLogger(FFFFakePlayerManager.class.getName());

    private static final List<Integer> FAKE_CLAN_IDS = FFFFakeHelpers.loadClanIdList("data/fakebots/clan-list-bot-farm-ffff.txt");

    private static int _fakeClanTableId = 0;

    private static final List<Location> SPAWN_LOCATIONS = FFFFakeHelpers.loadSpawnLocations("data/fakebots/spawn-list-bot-farm-ffff.txt");

    private static final List<Player> ACTIVE_FAKE_PLAYERS = new CopyOnWriteArrayList<>();
    private static final int MAX_BOTS = 50;
    private static final int BOTS_PER_CYCLE = 5;
    private static final long START_DELAY = 60 * 60 * 1000L; // 1 þñá

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
    	        _log.info("Spawning first 5 fake players...");
    	        spawnFakeBots(5);
    	
    	        _log.info("Scheduling gradual bot spawn every 5 minutes until MAX_BOTS (" + MAX_BOTS + ")...");
    	        ThreadPool.scheduleAtFixedRate(() -> {
    	            if (ACTIVE_FAKE_PLAYERS.size() >= MAX_BOTS) {
    	                _log.info("Reached MAX_BOTS (" + MAX_BOTS + "), stopping gradual spawn.");
    	                return;
    	            }
    	            int toSpawn = Math.min(5, MAX_BOTS - ACTIVE_FAKE_PLAYERS.size());
    	            spawnFakeBots(toSpawn);
    	        }, 5 * 60 * 1000L, 5 * 60 * 1000L);
    	    }
    	
    	    private void spawnFakeBots(int count) {
    	        for (int i = 0; i < count; i++) {
    	            Location base = Rnd.get(SPAWN_LOCATIONS);
    	            Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
    	            int classId = getRandomClass();
    	            FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
    	            bot.assignDefaultAI();
    	            ACTIVE_FAKE_PLAYERS.add(bot);
    	            _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
    	        }
    }

    private void scheduleBotCycle() {
        long interval = Rnd.get(20, 30) * 60 * 1000L;
        _log.info("Scheduling bot cycle every " + (interval / 60000) + " minutes.");

        ThreadPool.scheduleAtFixedRate(() -> {
            _log.info("Bot cycle triggered: removing and spawning " + BOTS_PER_CYCLE + " bots.");

            int removed = 0;
            for (Player bot : ACTIVE_FAKE_PLAYERS) {
                if (removed >= BOTS_PER_CYCLE)
                    break;
                _log.info("Deleting bot [" + bot.getName() + "]");
                bot.deleteMe();
                ACTIVE_FAKE_PLAYERS.remove(bot);
                removed++;
            }

            for (int i = 0; i < BOTS_PER_CYCLE; i++) {
                if (ACTIVE_FAKE_PLAYERS.size() >= MAX_BOTS)
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
        List<Integer> classIds = Arrays.asList(112, 109, 114, 117, 118);
        return Rnd.get(classIds);
    }

    public FFFFakePlayer spawnPlayer(int x, int y, int z, int classDesired) {
        FFFFakePlayer activeChar = FFFFakeHelpers.createRandomFFFFakePlayer(classDesired);
        World.getInstance().addPlayer(activeChar);

        Clan clan = null;
        while (clan == null) {
        clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
         }

        clan.addClanMember(activeChar);
        activeChar.setClan(clan);
        activeChar.setPledgeClass(ClanMember.calculatePledgeClass(activeChar));
        activeChar.setClanPrivileges(clan.getRankPrivs(activeChar.getPowerGrade()));

        handlePlayerClanOnSpawn(activeChar);

        activeChar.spawnMe(x, y, z);
        activeChar.onPlayerEnter();
        activeChar.heal();

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
            ACTIVE_FAKE_PLAYERS.add(bot);
            _log.info("Spawned bot [" + bot.getName() + "] (classId=" + classId + ") at " + loc);
        }
    }

    @SuppressWarnings("unchecked")
    public List<FFFFakePlayer> getFFFFakePlayers() {
        return (List<FFFFakePlayer>) (List<?>) ACTIVE_FAKE_PLAYERS;
    }
}
