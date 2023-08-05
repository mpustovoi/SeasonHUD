package club.iananderson.seasonhud.impl.fabricseasons;

import club.iananderson.seasonhud.config.Config;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Optional;

import static io.github.lucaargolo.seasonsextras.FabricSeasonsExtras.SEASON_CALENDAR_ITEM;

public class Calendar {
    public static boolean invCalendar;

    public static boolean curiosLoaded() {
        return FabricLoader.getInstance().isModLoaded("trinkets");
    }

    public static Item calendar = SEASON_CALENDAR_ITEM;
    public static boolean calendar() {
        if (Config.needCalendar.get()) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null) {
                Inventory inv = player.getInventory();
                int slot = findCalendar(inv, calendar) + findCuriosCalendar(player,calendar);

                invCalendar = (slot >= 0);

            }

        }
        else invCalendar = true;

        return invCalendar;
    }

    private static int findCalendar(Inventory inv, Item item) {
        for (int i = 0; i < inv.items.size(); ++i) {
            if ((!inv.items.get(i).isEmpty() && inv.items.get(i).is(item))
                    || (!inv.offhand.get(0).isEmpty() && inv.offhand.get(0).is(item))) {
                return i;
            }
        }
        return -1;
    }

    private static int findCuriosCalendar(Player player, Item item) {
        if (curiosLoaded()) {
            Optional<TrinketComponent> findCalendar = TrinketsApi.getTrinketComponent(player);
            if(findCalendar.get().isEquipped(item)){
                return 1;
            }
            else return 0;
        }
        else return 0;
    }
}


