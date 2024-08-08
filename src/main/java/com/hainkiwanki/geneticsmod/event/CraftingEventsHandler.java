package com.hainkiwanki.geneticsmod.event;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GeneticsMod.MOD_ID)
public class CraftingEventsHandler {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getPlayer();
        ItemStack craftedItem = event.getCrafting();
        Item item = craftedItem.getItem();

        if (ModItems.ITEMS_REQUIRE_RESEARCH.containsKey(item)) {
//            player.getCapability(ModCapabilities.PLAYER_RESEARCH).ifPresent(researchCap -> {
//                if (!researchCap.hasResearch("yourmodid:example_research")) {
//                    event.setCanceled(true); // Cancel crafting if the research is not unlocked
//                    player.sendMessage(new TextComponent("You have not unlocked this recipe yet!"), Util.NIL_UUID);
//                }
//            });
        }
    }
}
