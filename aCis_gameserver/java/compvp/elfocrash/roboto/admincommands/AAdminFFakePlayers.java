package compvp.elfocrash.roboto.admincommands;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.FFakePlayerManager;
import compvp.elfocrash.roboto.FFakePlayerTaskManager;
import compvp.elfocrash.roboto.ai.EEnchanterAI;
import compvp.elfocrash.roboto.ai.walker.GGiranWalkerAI;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Elfocrash
 *
 */
public class AAdminFFakePlayers implements IAdminCommandHandler
{
	private final String ffakesFolder = "data/html/admin/ffakeplayers/";
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ftakecontrol",
		"admin_freleasecontrol",
		"admin_ffakes",
		"admin_fspawnrandom",
		"admin_fdeletefake",
		"admin_fspawnenchanter",
		"admin_fspawnwalker"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showFakeDashboard(Player activeChar) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(ffakesFolder + "index.htm");
		html.replace("%ffakecount%", FFakePlayerManager.INSTANCE.getFFakePlayersCount());
		html.replace("%ttaskcount%", FFakePlayerTaskManager.INSTANCE.getTaskCount());
		activeChar.sendPacket(html);
	}
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.startsWith("admin_ffakes"))
		{
			showFakeDashboard(activeChar);
		}
		
		if(command.startsWith("admin_fdeletefake")) {
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFakePlayer) {
				FFakePlayer ffakePlayer = (FFakePlayer)activeChar.getTarget();
				ffakePlayer.despawnPlayer();
			}
			return true;
		}
		
		if(command.startsWith("admin_fspawnwalker")) {
			if(command.contains(" ")) {
				String locationName = command.split(" ")[1];
				FFakePlayer ffakePlayer = FFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
				switch(locationName) {
					case "giran":
						ffakePlayer.setFFakeAi(new GGiranWalkerAI(ffakePlayer));
					break;
				}
				return true;
			}
			
			return true;
		}
		
		if(command.startsWith("admin_fspawnenchanter")) {
			FFakePlayer ffakePlayer = FFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			ffakePlayer.setFFakeAi(new EEnchanterAI(ffakePlayer));
			return true;
		}
		
		if (command.startsWith("admin_fspawnrandom")) {
			FFakePlayer ffakePlayer = FFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			ffakePlayer.assignDefaultAI();
			if(command.contains(" ")) {
				String arg = command.split(" ")[1];
				if(arg.equalsIgnoreCase("htm")) {
					showFakeDashboard(activeChar);
				}
			}
			return true;
		}	
		
		if (command.startsWith("admin_ftakecontrol"))
		{
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFakePlayer) {
				FFakePlayer ffakePlayer = (FFakePlayer)activeChar.getTarget();
				ffakePlayer.setUnderControl(true);
				activeChar.setPlayerUnderCControl(ffakePlayer);
				activeChar.sendMessage("You are now controlling: " + ffakePlayer.getName());
				return true;
			}
			
			activeChar.sendMessage("You can only take control of a Fake Player");
			return true;
		}
		if (command.startsWith("admin_freleasecontrol"))
		{
			if(activeChar.isCControllingFFakePlayer()) {
				FFakePlayer ffakePlayer = activeChar.getPlayerUnderCControl();
				activeChar.sendMessage("You are no longer controlling: " + ffakePlayer.getName());
				ffakePlayer.setUnderControl(false);
				activeChar.setPlayerUnderCControl(null);
				return true;
			}
			
			activeChar.sendMessage("You are not controlling a Fake Player");
			return true;
		}
		return true;
	}
}
