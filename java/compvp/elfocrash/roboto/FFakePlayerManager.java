package compvp.elfocrash.roboto;

import java.util.List;
import java.util.stream.Collectors;

import compvp.elfocrash.roboto.helpers.FFakeHelpers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Elfocrash
 *
 */
public enum FFakePlayerManager {
	INSTANCE;

	private FFakePlayerManager() {

	}

	public void initialise() {
		FFakePlayerNameManager.INSTANCE.initialise();
		FFakePlayerTaskManager.INSTANCE.initialise();
	}

	public FFakePlayer spawnPlayer(int x, int y, int z) {
		FFakePlayer activeChar = FFakeHelpers.createRandomFFakePlayer();
		World.getInstance().addPlayer(activeChar);
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

	public void despawnFFakePlayer(int objectId) {
		Player player = World.getInstance().getPlayer(objectId);
		if (player instanceof FFakePlayer) {
			FFakePlayer ffakePlayer = (FFakePlayer) player;
			ffakePlayer.despawnPlayer();
		}
	}

	private static void handlePlayerClanOnSpawn(FFakePlayer activeChar) {
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

	public int getFFakePlayersCount() {
		return getFFakePlayers().size();
	}

	public List<FFakePlayer> getFFakePlayers() {
		return World.getInstance().getPlayers().stream().filter(x -> x instanceof FFakePlayer).map(x -> (FFakePlayer) x)
				.collect(Collectors.toList());
	}
}
