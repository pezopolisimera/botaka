package dyn.elfocrash.roboto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;

public enum FFFakePlayerNameManager {
	INSTANCE;
	
	public static final Logger _log = Logger.getLogger(FFFakePlayerNameManager.class.getName());
	private List<String> _fffakePlayerNames;
	
	public void initialise() {
		loadWordlist();
	}
	
	public String getRandomAvailableName() {
		String name = getRandomNameFromWordlist();
		
		while(nameAlreadyExists(name)) {
			name = getRandomNameFromWordlist();
		}
		
		return name;
	}
	
	private String getRandomNameFromWordlist() {
		return _fffakePlayerNames.get(Rnd.get(0, _fffakePlayerNames.size() - 1));
	}
	
	public List<String> getFFFakePlayerNames() {
		return _fffakePlayerNames;
	}
	
	private void loadWordlist()
    {
        try(LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(new File("./data/fffakenamewordlist.txt"))));)
        {
            String line;
            ArrayList<String> playersList = new ArrayList<String>();
            while((line = lnr.readLine()) != null)
            {
                if(line.trim().length() == 0 || line.startsWith("#"))
                    continue;
                playersList.add(line);
            }
            _fffakePlayerNames = playersList;
            _log.log(Level.INFO, String.format("Loaded %s fffake player names.", _fffakePlayerNames.size()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	private boolean nameAlreadyExists(String name) {
		return PlayerInfoTable.getInstance().getPlayerObjectId(name) > 0;
	}
}
