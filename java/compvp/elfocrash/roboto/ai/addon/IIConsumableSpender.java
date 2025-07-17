package compvp.elfocrash.roboto.ai.addon;

import compvp.elfocrash.roboto.FFakePlayer;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public interface IIConsumableSpender {

	default void handleConsumable(FFakePlayer ffakePlayer, int consumableId) {
		if(ffakePlayer.getInventory().getItemByItemId(consumableId) != null) {
			if(ffakePlayer.getInventory().getItemByItemId(consumableId).getCount() <= 20) {
				ffakePlayer.getInventory().addItem("", consumableId, 20000, ffakePlayer, null);			
				
			}
		}else {
			ffakePlayer.getInventory().addItem("", consumableId, 20000, ffakePlayer, null);
			ItemInstance consumable = ffakePlayer.getInventory().getItemByItemId(consumableId);
			if(consumable.isEquipable())
				ffakePlayer.getInventory().equipItem(consumable);
		}
	}
}
