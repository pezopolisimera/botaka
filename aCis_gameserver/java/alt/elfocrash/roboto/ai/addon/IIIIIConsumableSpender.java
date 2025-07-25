package alt.elfocrash.roboto.ai.addon;

import alt.elfocrash.roboto.FFFFFakePlayer;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public interface IIIIIConsumableSpender {

	default void handleConsumable(FFFFFakePlayer fffffakePlayer, int consumableId) {
		if(fffffakePlayer.getInventory().getItemByItemId(consumableId) != null) {
			if(fffffakePlayer.getInventory().getItemByItemId(consumableId).getCount() <= 20) {
				fffffakePlayer.getInventory().addItem("", consumableId, 20000, fffffakePlayer, null);			
				
			}
		}else {
			fffffakePlayer.getInventory().addItem("", consumableId, 20000, fffffakePlayer, null);
			ItemInstance consumable = fffffakePlayer.getInventory().getItemByItemId(consumableId);
			if(consumable.isEquipable())
				fffffakePlayer.getInventory().equipItem(consumable);
		}
	}
}
