package com.XHxinhe.aliveandwell.mixin.crafttime;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin({ScreenHandler.class})
public class ScreenHandlerMixin {
    @Final
    @Shadow public final DefaultedList<Slot> slots = DefaultedList.of();

    @Inject(method = "internalOnSlotClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/slot/Slot;takeStackRange(IILnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;"
                    ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;THROW:Lnet/minecraft/screen/slot/SlotActionType;"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/SlotActionType;PICKUP_ALL:Lnet/minecraft/screen/slot/SlotActionType;")
            ),
            cancellable = true)
    private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if(!player.getWorld().isClient){
            Slot slot3 = (Slot)this.slots.get(slotIndex);
            if(slot3 instanceof CraftingResultSlot && ((ScreenHandler)(Object)this instanceof CraftingScreenHandler || (ScreenHandler)(Object)this instanceof PlayerScreenHandler)){
                int j = button == 0 ? 1 : slot3.getStack().getCount();
                ItemStack itemStack = slot3.takeStackRange(j, Integer.MAX_VALUE, player);

                String nameItem = Registries.ITEM.getId(itemStack.getItem()).toString();
                //添加nbt标签后掉落不消失
                if ((nameItem.contains("netherite_scrap") ||nameItem.contains("blaze_rod") ||nameItem.contains("elytra")) && nameItem.contains("minecraft:")) {
                    // 如果物品已经有NBT标签
                        if (itemStack.hasNbt()) {
                        // 并且NBT里不包含我们的标记，就添加它。
                        // [注意] 这里的写法 `putString("aliveandwell", "aliveandwell")` 比较奇怪，通常会用布尔值或空的Compound。
                        if (!((NbtCompound)Objects.requireNonNull(itemStack.getNbt())).contains("aliveandwell")) {
                                itemStack.getNbt().putString("aliveandwell","aliveandwell");
                            }
                        }else {
                        // 如果物品没有NBT，就创建一个新的并添加标记。
                        this.setNbt(itemStack);
                        }
                    }


                if((nameItem.contains("argent_energy") ||nameItem.contains("argent_block")) && nameItem.contains("doom:")) {
                        if(itemStack.hasNbt()){
                        if (!((NbtCompound)Objects.requireNonNull(itemStack.getNbt())).contains("aliveandwell")) {
                            this.setNbt(itemStack);
                            }
                        }else {
                        this.setNbt(itemStack);
                    }
                }

                if (player.getInventory().insertStack(itemStack)){
                    player.getInventory().insertStack(itemStack);
                }else {
                    player.dropStack(itemStack);
                }
                // 取消原方法的执行。因为我们已经手动处理了物品的拿出和移动，
                // 所以必须取消，否则原方法会再执行一次，导致物品被复制。
                ci.cancel();
            }
        }
    }

    @Unique
    public void setNbt(ItemStack itemStack) {
        NbtCompound nbt;
        // 如果物品本身没有NBT，就创建一个新的
        if(!itemStack.hasNbt()){
            nbt = new NbtCompound();
        }else {
            // 否则获取现有的NBT
            nbt = itemStack.getNbt();
        }

        // 在物品的NBT中添加一个名为 "aliveandwell" 的子NBT标签。
        // 这里用 setSubNbt 比 putString 更标准一些。
        itemStack.setSubNbt("aliveandwell",nbt);
    }
}
