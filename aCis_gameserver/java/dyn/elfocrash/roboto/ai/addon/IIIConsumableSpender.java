package dyn.elfocrash.roboto.ai.addon;

import dyn.elfocrash.roboto.FFFakePlayer;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public interface IIIConsumableSpender {

	default void handleConsumable(FFFakePlayer fffakePlayer, int consumableId) {
		if(fffakePlayer.getInventory().getItemByItemId(consumableId) != null) {
			if(fffakePlayer.getInventory().getItemByItemId(consumableId).getCount() <= 20) {
				fffakePlayer.getInventory().addItem("", consumableId, 20000, fffakePlayer, null);			
				
			}
		}else {
			fffakePlayer.getInventory().addItem("", consumableId, 20000, fffakePlayer, null);
			ItemInstance consumable = fffakePlayer.getInventory().getItemByItemId(consumableId);
			if(consumable.isEquipable())
				fffakePlayer.getInventory().equipItem(consumable);
		}
	}
}
