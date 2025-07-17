package dre.elfocrash.roboto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;

import java.nio.charset.StandardCharsets;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;

public enum FFFFakePlayerNameManager {
    INSTANCE;

    public static final Logger _log = Logger.getLogger(FFFFakePlayerNameManager.class.getName());

    private List<String> _ffffakePlayerNames;

    private List<String> GM_NAMES;

    public void initialise() {
    	        loadWordlist(); //  διαβάζει ffffakenamewordlist.txt
    	        GM_NAMES = FFFFakeHelpers.loadNameList("data/fakebots/gm-bot-ffff.txt");
    	        _log.info("✅ Loaded " + GM_NAMES.size() + " GM bot names.");
    }

    public String getRandomAvailableName() {
        String name = getRandomNameFromWordlist();
        while (nameAlreadyExists(name)) {
            name = getRandomNameFromWordlist();
        }
        return name;
    }

    private String getRandomNameFromWordlist() {
        return _ffffakePlayerNames.get(Rnd.get(0, _ffffakePlayerNames.size() - 1));
    }

    public List<String> getFFFFakePlayerNames() {
        return _ffffakePlayerNames;
    }

    private void loadWordlist() {
    	        try (LineNumberReader lnr = new LineNumberReader(
    		    new BufferedReader(
    		    new InputStreamReader(
    		    new FileInputStream("./data/ffffakenamewordlist.txt"), StandardCharsets.UTF_8)))) {

            String line;
            ArrayList<String> playersList = new ArrayList<>();
            while ((line = lnr.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                playersList.add(line.trim());
            }
            _ffffakePlayerNames = playersList;
            _log.log(Level.INFO, String.format("Loaded %s ffffake player names.", _ffffakePlayerNames.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean nameAlreadyExists(String name) {
        return PlayerInfoTable.getInstance().getPlayerObjectId(name) > 0;
    }

       public String getRandomAvailableGMName() {
    	           if (GM_NAMES == null || GM_NAMES.isEmpty()) {
    		               _log.warning("⚠ GM name list is empty or null. Using fallback.");
    		               return "GM_Bot_" + System.currentTimeMillis();
    		           }
    		           for (String name : GM_NAMES) {
    		               _log.info("🔍 Trying GM name: " + name);
    		               if (!FFFFakePlayerManager.INSTANCE.isNameInUse(name)) {
    		                   _log.info("✅ Selected GM name: " + name);
    		                   return name;
    		               }
    		           }
    		           _log.warning("⚠ All GM names are in use. Using fallback.");
    		           return "GM_Bot_" + System.currentTimeMillis();
    }

    private static boolean isNameTaken(String name) {
        return PlayerInfoTable.getInstance().getPlayerObjectId(name) > 0;
    }
    }
