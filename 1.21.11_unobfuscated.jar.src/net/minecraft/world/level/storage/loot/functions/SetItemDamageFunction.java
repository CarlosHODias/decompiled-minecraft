/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SetItemDamageFunction extends LootItemConditionalFunction {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<SetItemDamageFunction> CODEC;
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("damage").forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(false).forGetter(()))).apply((Applicative)i, SetItemDamageFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   private final NumberProvider damage;
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemDamageFunction(List<LootItemCondition> predicates, NumberProvider damage, boolean add) {
/* 31 */     super(predicates);
/* 32 */     this.damage = damage;
/* 33 */     this.add = add;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemDamageFunction> getType() {
/* 38 */     return LootItemFunctions.SET_DAMAGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 43 */     return this.damage.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 48 */     if (itemStack.isDamageableItem()) {
/* 49 */       int maxDamage = itemStack.getMaxDamage();
/* 50 */       float base = this.add ? (1.0F - itemStack.getDamageValue() / maxDamage) : 0.0F;
/* 51 */       float pct = 1.0F - Mth.clamp(this.damage.getFloat(context) + base, 0.0F, 1.0F);
/* 52 */       itemStack.setDamageValue(Mth.floor(pct * maxDamage));
/*    */     } else {
/* 54 */       LOGGER.warn("Couldn't set damage of loot item {}", itemStack);
/*    */     } 
/* 56 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value) {
/* 60 */     return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider value, boolean add) {
/* 64 */     return simpleBuilder(conditions -> new SetItemDamageFunction(conditions, value, add));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */