/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBannerPatternFunction extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)BannerPatternLayers.CODEC.fieldOf("patterns").forGetter(()), (App)Codec.BOOL.fieldOf("append").forGetter(()))).apply((Applicative)i, SetBannerPatternFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetBannerPatternFunction> CODEC;
/*    */   private final BannerPatternLayers patterns;
/*    */   private final boolean append;
/*    */   
/*    */   private SetBannerPatternFunction(List<LootItemCondition> predicates, BannerPatternLayers patterns, boolean append) {
/* 27 */     super(predicates);
/* 28 */     this.patterns = patterns;
/* 29 */     this.append = append;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 34 */     if (this.append) {
/* 35 */       itemStack.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY, this.patterns, (base, appended) -> new BannerPatternLayers.Builder().addAll(base).addAll(appended).build());
/*    */     }
/*    */     else {
/*    */       
/* 39 */       itemStack.set(DataComponents.BANNER_PATTERNS, this.patterns);
/*    */     } 
/* 41 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetBannerPatternFunction> getType() {
/* 46 */     return LootItemFunctions.SET_BANNER_PATTERN;
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 50 */     private final BannerPatternLayers.Builder patterns = new BannerPatternLayers.Builder();
/*    */     private final boolean append;
/*    */     
/*    */     private Builder(boolean append) {
/* 54 */       this.append = append;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 59 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 64 */       return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append);
/*    */     }
/*    */     
/*    */     public Builder addPattern(Holder<BannerPattern> pattern, DyeColor color) {
/* 68 */       this.patterns.add(pattern, color);
/* 69 */       return this;
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setBannerPattern(boolean append) {
/* 74 */     return new Builder(append);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetBannerPatternFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */