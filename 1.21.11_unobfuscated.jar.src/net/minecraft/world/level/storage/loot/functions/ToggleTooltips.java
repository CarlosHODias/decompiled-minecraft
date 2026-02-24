/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ToggleTooltips extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Codec.unboundedMap(DataComponentType.CODEC, (Codec)Codec.BOOL).fieldOf("toggles").forGetter(())).apply((Applicative)i, ToggleTooltips::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ToggleTooltips> CODEC;
/*    */   private final Map<DataComponentType<?>, Boolean> values;
/*    */   
/*    */   private ToggleTooltips(List<LootItemCondition> predicates, Map<DataComponentType<?>, Boolean> values) {
/* 24 */     super(predicates);
/* 25 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 30 */     itemStack.update(net.minecraft.core.component.DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT, display -> {
/*    */           for (Map.Entry<DataComponentType<?>, Boolean> entry : this.values.entrySet()) {
/*    */             boolean shown = (Boolean)entry.getValue();
/*    */             display = display.withHidden(entry.getKey(), !shown);
/*    */           } 
/*    */           return display;
/*    */         });
/* 37 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<ToggleTooltips> getType() {
/* 42 */     return LootItemFunctions.TOGGLE_TOOLTIPS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/ToggleTooltips.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */