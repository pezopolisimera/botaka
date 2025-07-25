package com.elfocrash.roboto;

import com.elfocrash.roboto.helpers.FakeHelpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
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

/**
 * @author Elfocrash
 *
 */
public enum FakePlayerManager {
	INSTANCE;
	
	private final List<Integer> _fakeClanIds = Arrays.asList(268476671, 268477196, 268477201, 268480038); // <------- Fake clans ids 
	private static int _fakeClanTableId = 0;

	private FakePlayerManager() {

	}

	public void initialise() {
		FakePlayerNameManager.INSTANCE.initialise();
		FakePlayerTaskManager.INSTANCE.initialise();
	}

	public FakePlayer spawnPlayer(int x, int y, int z, int classDesired) {
		FakePlayer activeChar = FakeHelpers.createRandomFakePlayer(classDesired);
		World.getInstance().addPlayer(activeChar);
		
		// ======================================================================================================================
		//
		// Add a bot to a clan
		//
		Clan clan = null;
		
		while ( clan == null ) {
			clan = ClanTable.getInstance().getClan( _fakeClanIds.get(_fakeClanTableId++ % _fakeClanIds.size()) );
		}
		clan.addClanMember(activeChar);
		activeChar.setClan(clan);
		activeChar.setPledgeClass(ClanMember.calculatePledgeClass(activeChar));
		activeChar.setClanPrivileges(clan.getRankPrivs(activeChar.getPowerGrade()));
		// ======================================================================================================================
		
		handlePlayerClanOnSpawn(activeChar);
		
		if (Config.PLAYER_SPAWN_PROTECTION > 0)
			activeChar.setSpawnProtection(true);
		
		activeChar.spawnMe(x, y, z);
		activeChar.onPlayerEnter();
		
		if (!activeChar.isGM() && (!activeChar.isInSiege() || activeChar.getSiegeState() < 2)
				&& activeChar.isInsideZone(ZoneId.SIEGE))
			activeChar.teleToLocation(TeleportType.TOWN);

		activeChar.heal();
		return activeChar;
	}
	
	//vuhu kokia, ahujena komanda!
	public void spawnPlayers(int count, int radius, int x, int y, int z, int classDesired) {
		ThreadPool.execute(new Runnable() {//kuriame atskira thread'a, kad neuzstrigtu tavo charas kai paleisi komanda
			
			@Override
			public void run() {
				for(int i = 0; i < count; i++)
				{
					int xCoord = x + Rnd.get(-radius, radius);
					int yCoord = y + Rnd.get(-radius, radius);
					FakePlayer fakePlayer = spawnPlayer(xCoord, yCoord, z+15, classDesired);//+15, jei kazkur zemiau spawnintu :?
					fakePlayer.assignDefaultAI();
				}
			}
		});
	}

	public void despawnFakePlayer(int objectId) {
		Player player = World.getInstance().getPlayer(objectId);
		if (player instanceof FakePlayer) {
			FakePlayer fakePlayer = (FakePlayer) player;
			fakePlayer.despawnPlayer();
		}
	}

	private static void handlePlayerClanOnSpawn(FakePlayer activeChar) {
		final Clan clan = activeChar.getClan();
		if (clan != null) {
			clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);

			final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN)
					.addCharName(activeChar);
			final PledgeShowMemberListUpdate update = new PledgeShowMemberListUpdate(activeChar);

			// Send packets to others members.
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

	public int getFakePlayersCount() {
		return getFakePlayers().size();
	}

	public List<FakePlayer> getFakePlayers() {
		return World.getInstance().getPlayers().stream().filter(x -> x instanceof FakePlayer).map(x -> (FakePlayer) x)
				.collect(Collectors.toList());
	}
	
	//delet ze all fek players
	public void deleteAllFakes() {
		World.getInstance().getPlayers().stream().filter(x -> x instanceof FakePlayer).forEach(x -> ((FakePlayer) x).despawnPlayer());
	}
	
	//hehe neprasytas feature'as
	//hehe kuriam threada
	public void deleteAllFakes(int count, int radius, int xc, int yc) {
		ThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				Stream<Player> players = World.getInstance().getPlayers().stream().filter(x -> x instanceof FakePlayer
						&& (radius <= 0 || radius > 0 && x.isInsideRadius(xc, yc, radius, false)));
				if(count > 0)
				{
					players = players.limit(count);
				}
				
				players.forEach(x -> ((FakePlayer) x).despawnPlayer());
				players = null;
			}
		});
	}
}
