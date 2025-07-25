package dyn.elfocrash.roboto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dyn.elfocrash.roboto.helpers.FFFakeHelpers;

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
public enum FFFakePlayerManager {
	INSTANCE;
	
	private final List<Integer> _fffakeClanIds = Arrays.asList(268480038, 268476676, 268477179, 268477184, 268477191, 268477196, 268477201, 268474815, 268477215, 268477220, 268477225, 268477230); // <------- Fake clans ids 
	private static int _fffakeClanTableId = 0;

	private FFFakePlayerManager() {

	}

	public void initialise() {
		FFFakePlayerNameManager.INSTANCE.initialise();
		FFFakePlayerTaskManager.INSTANCE.initialise();
	}

	public FFFakePlayer spawnPlayer(int x, int y, int z) {
		FFFakePlayer activeChar = FFFakeHelpers.createRandomFFFakePlayer();
		World.getInstance().addPlayer(activeChar);
		
		// ======================================================================================================================
		//
		// Add a bot to a clan
		//
		Clan clan = null;
				
		while ( clan == null ) {
			clan = ClanTable.getInstance().getClan( _fffakeClanIds.get(_fffakeClanTableId++ % _fffakeClanIds.size()) );
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

	public void despawnFFFakePlayer(int objectId) {
		Player player = World.getInstance().getPlayer(objectId);
		if (player instanceof FFFakePlayer) {
			FFFakePlayer fffakePlayer = (FFFakePlayer) player;
			fffakePlayer.despawnPlayer();
		}
	}

	private static void handlePlayerClanOnSpawn(FFFakePlayer activeChar) {
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

	public int getFFFakePlayersCount() {
		return getFFFakePlayers().size();
	}

	public List<FFFakePlayer> getFFFakePlayers() {
		return World.getInstance().getPlayers().stream().filter(x -> x instanceof FFFakePlayer).map(x -> (FFFakePlayer) x)
				.collect(Collectors.toList());
	}
}
