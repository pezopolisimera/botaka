package dre.elfocrash.roboto.admincommands;

import java.util.logging.Logger;
import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerManager;
import dre.elfocrash.roboto.FFFFakePlayerNameManager;
import dre.elfocrash.roboto.FFFFakePlayerTaskManager;
import dre.elfocrash.roboto.ai.EEEEnchanterAI;
import dre.elfocrash.roboto.ai.walker.GGGGiranWalkerAI;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.model.World;

public class AAAAdminFFFFakePlayers implements IAdminCommandHandler {
	
	private static final Logger LOGGER = Logger.getLogger(AAAAdminFFFFakePlayers.class.getName());
    private final String ffffakesFolder = "data/html/admin/ffffakeplayers/";
    private static final String[] ADMIN_COMMANDS = {
        "admin_fffftakecontrol",
        "admin_ffffreleasecontrol",
        "admin_ffffakes",
        "admin_ffffspawnrandom",
        "admin_ffffdeletefake",
        "admin_ffffspawnenchanter",
        "admin_ffffspawnwalker",
        "admin_livegm",
        "admin_deletebot_"
    };

    private static int classDesired = 0;

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }

    private void showFakeDashboard(Player activeChar) {
        final NpcHtmlMessage html = new NpcHtmlMessage(0);
        html.setFile(ffffakesFolder + "index.htm");
        html.replace("%ffffakecount%", FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount());
        html.replace("%fffftaskcount%", FFFFakePlayerTaskManager.INSTANCE.getTaskCount());
        activeChar.sendPacket(html);
    }

    @Override
    public boolean useAdminCommand(String command, Player activeChar) {
        if (command.equalsIgnoreCase("admin_livegm")) {
            sendGMBotWindow(activeChar);
            return true;
        }

        if (command.startsWith("admin_deletebot_")) {
            try {
                int objId = Integer.parseInt(command.substring("admin_deletebot_".length()));
                Player bot = World.getInstance().getPlayer(objId);
                if (bot != null) {
                    bot.deleteMe();                                    
                  FFFFakePlayerManager.INSTANCE.getGMFakePlayers().remove(bot);
                                    FFFFakePlayerManager.INSTANCE.getFFFFakePlayers().remove(bot); 
                                    FFFFakePlayerManager.INSTANCE.getAutoSpawnedFakePlayers().remove(bot);                                   
                                    activeChar.sendMessage("Bot [" + bot.getName() + "] deleted.");
                                    LOGGER.info("✅ GM bot deleted: " + bot.getName() + " [ObjectId: " + objId + "]");

                } else {
                    activeChar.sendMessage("Bot not found.");
                }
            } catch (NumberFormatException e) {
                activeChar.sendMessage("Invalid bot ID.");
            }
            return true;
        }

        if (command.startsWith("admin_ffffakes")) {
            showFakeDashboard(activeChar);
        }

        if (command.startsWith("admin_ffffdeletefake")) {
            String subStr = command.substring("admin_ffffdeletefake".length()).trim();
            boolean matchesHtm = command.matches(".*[hH][tT][mM].*");
            if (matchesHtm)
                subStr = subStr.replaceFirst(" [hH][tT][mM]", "");
            if (subStr.length() <= 0) {
                if (activeChar.getTarget() instanceof FFFFakePlayer) {
                    ((FFFFakePlayer) activeChar.getTarget()).despawnPlayer();
                }
            } else {
                if (subStr.equalsIgnoreCase("all")) {
                	    FFFFakePlayerManager.INSTANCE.deleteAllFFFFakes();
                	} else {
                	   FFFFakePlayerManager.INSTANCE.deleteFFFFake(subStr);
                }
            }
            if (matchesHtm)
                showFakeDashboard(activeChar);
            return true;
        }

        if (command.startsWith("admin_ffffspawnwalker")) {
            if (command.contains(" ")) {
                String locationName = command.split(" ")[1];
                String name = FFFFakePlayerNameManager.INSTANCE.getRandomAvailableGMName();
                FFFFakePlayer bot = FFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(), activeChar.getY(), activeChar.getZ(), classDesired);
                bot.setName(name);
                switch (locationName) {
                    case "giran":
                        bot.setFFFFakeAi(new GGGGiranWalkerAI(bot));
                        FFFFakeHelpers.registerBotStats(bot, true);
                        break;
                }
                return true;
            }
            return true;
        }

        if (command.startsWith("admin_ffffspawnenchanter")) {
        	String name = FFFFakePlayerNameManager.INSTANCE.getRandomAvailableGMName();
            FFFFakePlayer bot = FFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(), activeChar.getY(), activeChar.getZ(), classDesired);
            bot.setName(name);
            bot.setFFFFakeAi(new EEEEnchanterAI(bot));
            FFFFakeHelpers.registerBotStats(bot, true);
            return true;
        }

        if (command.startsWith("admin_ffffspawnrandom")) {
            String subStr = command.substring("admin_ffffspawnrandom".length()).trim();
            boolean matchesHtm = subStr.matches(".*[hH][tT][mM].*");
            if (matchesHtm)
            	subStr = subStr.replaceFirst(" [hH][tT][mM]", "");
                FFFFakePlayerManager.INSTANCE.spawnNamedPlayer(subStr, activeChar);
            if (matchesHtm)
                showFakeDashboard(activeChar);
            return true;
        }

        if (command.startsWith("admin_fffftakecontrol")) {
            if (activeChar.getTarget() instanceof FFFFakePlayer) {
                FFFFakePlayer bot = (FFFFakePlayer) activeChar.getTarget();
                bot.setUnderCCCControl(true);
                activeChar.setPlayerUnderCCCControl(bot);
                activeChar.sendMessage("You are now controlling: " + bot.getName());
                return true;
            }
            activeChar.sendMessage("You can only take control of a Fake Player");
            return true;
        }

        if (command.startsWith("admin_ffffreleasecontrol")) {
            if (activeChar.isCCCControllingFFFFakePlayer()) {
                FFFFakePlayer bot = activeChar.getPlayerUnderCCCControl();
                activeChar.sendMessage("You are no longer controlling: " + bot.getName());
                bot.setUnderCCCControl(false);
                activeChar.setPlayerUnderCCCControl(null);
                return true;
            }
            activeChar.sendMessage("You are not controlling a Fake Player");
            return true;
        }

        return true;
    }

    private static void sendGMBotWindow(Player admin) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body><center>");
        html.append("<table width=280 bgcolor=222222><tr><td><font color=\"LEVEL\">Live GM Bots</font></td></tr>");

        for (Player bot : FFFFakePlayerManager.INSTANCE.getGMFakePlayers()) {
            html.append("<tr><td>")
                .append("[").append(bot.getObjectId()).append("] ")
                .append(bot.getName())
                .append(" PvP: ").append(bot.getPvpKills())
                .append(" <a action=\"bypass -h admin_deletebot_").append(bot.getObjectId()).append("\">[Delete]</a>")
                .append("</td></tr>");
        }

        html.append("</table><br>");
        html.append("<button value=\"Refresh\" action=\"bypass -h admin_livegm\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        html.append("</center></body></html>");

        NpcHtmlMessage msg = new NpcHtmlMessage(0);
        msg.setHtml(html.toString());
        admin.sendPacket(msg);
    }

    // ΞΞ­ΞΈΞΏΞ΄ΞΏΟ‚ kazkaVeikiam(...) Ο€Ξ±ΟΞ±ΞΌΞ­Ξ½ΞµΞΉ Ξ―Ξ΄ΞΉΞ±.
}
