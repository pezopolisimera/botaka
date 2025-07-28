package dyn.elfocrash.roboto.admincommands;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.FFFakePlayerManager;
import dyn.elfocrash.roboto.FFFakePlayerTaskManager;
import dyn.elfocrash.roboto.ai.EEEnchanterAI;
import dyn.elfocrash.roboto.ai.walker.GGGiranWalkerAI;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Elfocrash
 *
 */
public class AAAdminFFFakePlayers implements IAdminCommandHandler
{
	private final String fffakesFolder = "data/html/admin/fffakeplayers/";
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ffftakecontrol",
		"admin_fffreleasecontrol",
		"admin_fffakes",
		"admin_fffspawnrandom",
		"admin_fffdeletefake",
		"admin_fffspawnenchanter",
		"admin_fffspawnwalker"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showFakeDashboard(Player activeChar) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(fffakesFolder + "index.htm");
		html.replace("%fffakecount%", FFFakePlayerManager.INSTANCE.getFFFakePlayersCount());
		html.replace("%tttaskcount%", FFFakePlayerTaskManager.INSTANCE.getTaskCount());
		activeChar.sendPacket(html);
	}
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.startsWith("admin_fffakes"))
		{
			showFakeDashboard(activeChar);
		}
		
		if(command.startsWith("admin_fffdeletefake")) {
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFakePlayer) {
				FFFakePlayer fffakePlayer = (FFFakePlayer)activeChar.getTarget();
				fffakePlayer.despawnPlayer();
			}
			return true;
		}
		
		if(command.startsWith("admin_fffspawnwalker")) {
			if(command.contains(" ")) {
				String locationName = command.split(" ")[1];
				FFFakePlayer fffakePlayer = FFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
				switch(locationName) {
					case "giran":
						fffakePlayer.setFFFakeAi(new GGGiranWalkerAI(fffakePlayer));
					break;
				}
				return true;
			}
			
			return true;
		}
		
		if(command.startsWith("admin_fffspawnenchanter")) {
			FFFakePlayer fffakePlayer = FFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			fffakePlayer.setFFFakeAi(new EEEnchanterAI(fffakePlayer));
			return true;
		}
		
		if (command.startsWith("admin_fffspawnrandom")) {
			FFFakePlayer fffakePlayer = FFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			fffakePlayer.assignDefaultAI();
			if(command.contains(" ")) {
				String arg = command.split(" ")[1];
				if(arg.equalsIgnoreCase("htm")) {
					showFakeDashboard(activeChar);
				}
			}
			return true;
		}	
		
		if (command.startsWith("admin_ffftakecontrol"))
		{
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFakePlayer) {
				FFFakePlayer fffakePlayer = (FFFakePlayer)activeChar.getTarget();
				fffakePlayer.setUnderControl(true);
				activeChar.setPlayerUnderCCControl(fffakePlayer);
				activeChar.sendMessage("You are now controlling: " + fffakePlayer.getName());
				return true;
			}
			
			activeChar.sendMessage("You can only take control of a Fake Player");
			return true;
		}
		if (command.startsWith("admin_fffreleasecontrol"))
		{
			if(activeChar.isCCControllingFFFakePlayer()) {
				FFFakePlayer fffakePlayer = activeChar.getPlayerUnderCCControl();
				activeChar.sendMessage("You are no longer controlling: " + fffakePlayer.getName());
				fffakePlayer.setUnderControl(false);
				activeChar.setPlayerUnderCCControl(null);
				return true;
			}
			
			activeChar.sendMessage("You are not controlling a Fake Player");
			return true;
		}
		return true;
	}
}
