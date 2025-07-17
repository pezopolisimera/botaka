package com.elfocrash.roboto.admincommands;

import com.elfocrash.roboto.FakePlayer;
import com.elfocrash.roboto.FakePlayerManager;
import com.elfocrash.roboto.FakePlayerTaskManager;
import com.elfocrash.roboto.ai.EnchanterAI;
import com.elfocrash.roboto.ai.walker.GiranWalkerAI;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Elfocrash
 *
 */
public class AdminFakePlayers implements IAdminCommandHandler
{
	private final String fakesFolder = "data/html/admin/fakeplayers/";
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_takecontrol",
		"admin_releasecontrol",
		"admin_fakes",
		"admin_spawnrandom",
		"admin_deletefake",
		"admin_spawnenchanter",
		"admin_spawnwalker"
	};
	
	private static int classDesired = 0;
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showFakeDashboard(Player activeChar) {
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(fakesFolder + "index.htm");
		html.replace("%fakecount%", FakePlayerManager.INSTANCE.getFakePlayersCount());
		html.replace("%taskcount%", FakePlayerTaskManager.INSTANCE.getTaskCount());
		activeChar.sendPacket(html);
	}
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.startsWith("admin_fakes"))
		{
			showFakeDashboard(activeChar);
		}
		
		if(command.startsWith("admin_deletefake")) {
			
			String subStr = command.substring(16).trim();//16 yra "admin_deletefake" simboliΕ³ kiekis
			//prie to pacio nutriname tarpus nuo krastu su trim()
			boolean matchesHtm = command.matches(".*[hH][tT][mM].*");
			if(matchesHtm)
				subStr = subStr.replaceFirst(" [hH][tT][mM]", "");//nutrinam jei netycia 
			
			if(subStr.length() <= 0)//tikriname ar komanda neturi papildomu parametru
			{
				if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FakePlayer) {
					FakePlayer fakePlayer = (FakePlayer)activeChar.getTarget();
					fakePlayer.despawnPlayer();
				}
			}else {//komanda turi papildomu parametru
				if(subStr.matches(".*[aA][lL][lL].*"))
				{
					//po komandos Δ―vestas raktinis ΕΎodis "all"
					//pvz "admin_deletefake all" "admin_deletefake dsadsad all" "admin_deletefake 4fe4f484aAll"
					//tinka visi variantai
					FakePlayerManager.INSTANCE.deleteAllFakes();//triname
				}else if(matchesHtm || subStr.length() > 0)// gal but ivedei kiek zaideju nori istrinti?
				{
					kazkaVeikiam(subStr, activeChar, false);
				}
			}
			
			if(matchesHtm)
				showFakeDashboard(activeChar);
			return true;
		}
		
		if(command.startsWith("admin_spawnwalker")) {
			if(command.contains(" ")) {
				String locationName = command.split(" ")[1];
				FakePlayer fakePlayer = FakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ(), classDesired);
				switch(locationName) {
					case "giran":
						fakePlayer.setFakeAi(new GiranWalkerAI(fakePlayer));
					break;
				}
				return true;
			}
			
			return true;
		}
		
		if(command.startsWith("admin_spawnenchanter")) {
			FakePlayer fakePlayer = FakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ(), classDesired);
			fakePlayer.setFakeAi(new EnchanterAI(fakePlayer));
			return true;
		}
		
		if (command.startsWith("admin_spawnrandom")) {
			
			String subStr = command.substring(17);
			boolean matchesHtm = subStr.matches(".*[hH][tT][mM].*");//perrasau komanda Δ― regex
			if(matchesHtm)
				subStr = subStr.replaceFirst(" [hH][tT][mM]", "");//nutrinam jei netycia 
			
			kazkaVeikiam(subStr, activeChar, true);

			if(matchesHtm)
				showFakeDashboard(activeChar);
			
			return true;
		}
		
		if (command.startsWith("admin_takecontrol"))
		{
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FakePlayer) {
				FakePlayer fakePlayer = (FakePlayer)activeChar.getTarget();
				fakePlayer.setUnderControl(true);
				activeChar.setPlayerUnderControl(fakePlayer);
				activeChar.sendMessage("You are now controlling: " + fakePlayer.getName());
				return true;
			}
			
			activeChar.sendMessage("You can only take control of a Fake Player");
			return true;
		}
		if (command.startsWith("admin_releasecontrol"))
		{
			if(activeChar.isControllingFakePlayer()) {
				FakePlayer fakePlayer = activeChar.getPlayerUnderControl();
				activeChar.sendMessage("You are no longer controlling: " + fakePlayer.getName());
				fakePlayer.setUnderControl(false);
				activeChar.setPlayerUnderControl(null);
				return true;
			}
			
			activeChar.sendMessage("You are not controlling a Fake Player");
			return true;
		}
		return true;
	}
	
	/*
	 * sitas metodas atsirenka kas yra radius ir kas yra count kai ivedi tarp html'o ir paspaudi
	 */
	private static void kazkaVeikiam(String subStr, Player activeChar, boolean spawn) {
		int fakeCount = 1, fakeRadius = 0;
		if(!spawn) {
			fakeCount = -1;
		}

		String[] subStrItems = subStr.trim().split(" ");
		if(subStrItems.length <= 0 || subStrItems.length == 1 && subStrItems[0].equals("#"))
		{
			if(spawn)
				FakePlayerManager.INSTANCE.spawnPlayers(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY(), activeChar.getZ(), classDesired);
			else
				FakePlayerManager.INSTANCE.deleteAllFakes(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY());//triname skaiciu
			
			return;
		}else if(subStrItems.length == 2 && subStrItems[0].equals("#")) {
			try {
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 2 && subStrItems[1].equals("#")) {
			try {			
				//ivestas tik kiekis
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				//blogas skaiciaus formatas, pvz ivedei raide
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#")) {
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[1].equals("#")) {
			try {			
				//ivestas tik kiekis
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				//blogas skaiciaus formatas, pvz ivedei raide
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[2].equals("#")) {
			try {
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#") && subStrItems[1].equals("#")) {
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#") && subStrItems[2].equals("#")) {
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[1].equals("#") && subStrItems[2].equals("#")) {
			try {
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
		}
		else
		{
			if(subStrItems.length > 0 && subStrItems[0].length() > 0)
			{
				try {			
					//ivestas tik kiekis
					fakeCount = Integer.parseInt(subStrItems[0]);
				}catch (NumberFormatException e) {
					//blogas skaiciaus formatas, pvz ivedei raide
					activeChar.sendMessage("You have entered the count in a wrong format '" + subStrItems[0] + "'");
				}
			}
			
			if(subStrItems.length > 1)
			{
				//int index = subStrItems.length > 3 ? 3 : 1;//mes juk norim kad eitu ir paprastai ivest komanda
				//pvz //spawnrandom 10 100
				int index = 1;
				if(subStrItems[index].length() > 0) {
					try {
						fakeRadius = Integer.parseInt(subStrItems[index]);
					}catch (NumberFormatException e) {
						activeChar.sendMessage("You have entered the radius in a wrong format '" + subStrItems[index] + "'");
					}
				}
			}
			
			if(subStrItems.length > 2)
			{
				//int index = subStrItems.length > 3 ? 3 : 1;//mes juk norim kad eitu ir paprastai ivest komanda
				//pvz //spawnrandom 10 100
				int index = 2;
				if(subStrItems[index].length() > 0) {
					try {
						fakeRadius = Integer.parseInt(subStrItems[index]);
					}catch (NumberFormatException e) {
						activeChar.sendMessage("You have entered the radius in a wrong format '" + subStrItems[index] + "'");
					}
				}
			}
		}
		
		//pagaliau spawninam
		if(spawn)
			FakePlayerManager.INSTANCE.spawnPlayers(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY(), activeChar.getZ(), classDesired);
		else//arba trinam
			FakePlayerManager.INSTANCE.deleteAllFakes(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY());//triname skaiciu
	}
}
