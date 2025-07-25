package alt.elfocrash.roboto.admincommands;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.FFFFFakePlayerManager;
import alt.elfocrash.roboto.FFFFFakePlayerTaskManager;
import alt.elfocrash.roboto.ai.EEEEEnchanterAI;
import alt.elfocrash.roboto.ai.walker.GGGGGiranWalkerAI;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Elfocrash
 *
 */
public class AAAAAdminFFFFFakePlayers implements IAdminCommandHandler
{
	private final String fffffakesFolder = "data/html/admin/fffffakeplayers/";
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ffffftakecontrol",
		"admin_fffffreleasecontrol",
		"admin_fffffakes",
		"admin_fffffspawnrandom",
		"admin_fffffdeletefake",
		"admin_fffffspawnenchanter",
		"admin_fffffspawnwalker"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showFakeDashboard(Player activeChar) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(fffffakesFolder + "index.htm");
		html.replace("%fffffakecount%", FFFFFakePlayerManager.INSTANCE.getFFFFFakePlayersCount());
		html.replace("%tttttaskcount%", FFFFFakePlayerTaskManager.INSTANCE.getTaskCount());
		activeChar.sendPacket(html);
	}
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.startsWith("admin_fffffakes"))
		{
			showFakeDashboard(activeChar);
		}
		
		if(command.startsWith("admin_fffffdeletefake")) {
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFFFakePlayer) {
				FFFFFakePlayer fffffakePlayer = (FFFFFakePlayer)activeChar.getTarget();
				fffffakePlayer.despawnPlayer();
			}
			return true;
		}
		
		if(command.startsWith("admin_fffffspawnwalker")) {
			if(command.contains(" ")) {
				String locationName = command.split(" ")[1];
				FFFFFakePlayer fffffakePlayer = FFFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
				switch(locationName) {
					case "giran":
						fffffakePlayer.setFFFFFakeAi(new GGGGGiranWalkerAI(fffffakePlayer));
					break;
				}
				return true;
			}
			
			return true;
		}
		
		if(command.startsWith("admin_fffffspawnenchanter")) {
			FFFFFakePlayer fffffakePlayer = FFFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			fffffakePlayer.setFFFFFakeAi(new EEEEEnchanterAI(fffffakePlayer));
			return true;
		}
		
		if (command.startsWith("admin_fffffspawnrandom")) {
			FFFFFakePlayer fffffakePlayer = FFFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ());
			fffffakePlayer.assignDefaultAI();
			if(command.contains(" ")) {
				String arg = command.split(" ")[1];
				if(arg.equalsIgnoreCase("htm")) {
					showFakeDashboard(activeChar);
				}
			}
			return true;
		}	
		
		if (command.startsWith("admin_ffffftakecontrol"))
		{
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFFFakePlayer) {
				FFFFFakePlayer fffffakePlayer = (FFFFFakePlayer)activeChar.getTarget();
				fffffakePlayer.setUnderControl(true);
				activeChar.setPlayerUnderCCCCControl(fffffakePlayer);
				activeChar.sendMessage("You are now controlling: " + fffffakePlayer.getName());
				return true;
			}
			
			activeChar.sendMessage("You can only take control of a Fake Player");
			return true;
		}
		if (command.startsWith("admin_fffffreleasecontrol"))
		{
			if(activeChar.isCCCCControllingFFFFFakePlayer()) {
				FFFFFakePlayer fffffakePlayer = activeChar.getPlayerUnderCCCCControl();
				activeChar.sendMessage("You are no longer controlling: " + fffffakePlayer.getName());
				fffffakePlayer.setUnderControl(false);
				activeChar.setPlayerUnderCCCCControl(null);
				return true;
			}
			
			activeChar.sendMessage("You are not controlling a Fake Player");
			return true;
		}
		return true;
	}
}
