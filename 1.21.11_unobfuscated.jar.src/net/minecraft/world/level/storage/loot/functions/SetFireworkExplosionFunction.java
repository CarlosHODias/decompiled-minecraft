/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworkExplosionFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)FireworkExplosion.Shape.CODEC.optionalFieldOf("shape").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("colors").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("fade_colors").forGetter(()), (App)Codec.BOOL.optionalFieldOf("trail").forGetter(()), (App)Codec.BOOL.optionalFieldOf("twinkle").forGetter(()))).apply((Applicative)i, SetFireworkExplosionFunction::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetFireworkExplosionFunction> CODEC;
/*    */ 
/*    */   
/* 25 */   public static final FireworkExplosion DEFAULT_VALUE = new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL, IntList.of(), IntList.of(), false, false);
/*    */   
/*    */   final Optional<FireworkExplosion.Shape> shape;
/*    */   final Optional<IntList> colors;
/*    */   final Optional<IntList> fadeColors;
/*    */   final Optional<Boolean> trail;
/*    */   final Optional<Boolean> twinkle;
/*    */   
/*    */   public SetFireworkExplosionFunction(List<LootItemCondition> predicates, Optional<FireworkExplosion.Shape> shape, Optional<IntList> colors, Optional<IntList> fadeColors, Optional<Boolean> hasTrail, Optional<Boolean> hasTwinkle) {
/* 34 */     super(predicates);
/* 35 */     this.shape = shape;
/* 36 */     this.colors = colors;
/* 37 */     this.fadeColors = fadeColors;
/* 38 */     this.trail = hasTrail;
/* 39 */     this.twinkle = hasTwinkle;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 44 */     itemStack.update(net.minecraft.core.component.DataComponents.FIREWORK_EXPLOSION, DEFAULT_VALUE, this::apply);
/* 45 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private FireworkExplosion apply(FireworkExplosion original) {
/* 50 */     Objects.requireNonNull(original);
/* 51 */     Objects.requireNonNull(original);
/* 52 */     Objects.requireNonNull(original);
/* 53 */     Objects.requireNonNull(original);
/* 54 */     Objects.requireNonNull(original); return new FireworkExplosion(this.shape.orElseGet(original::shape), this.colors.orElseGet(original::colors), this.fadeColors.orElseGet(original::fadeColors), (Boolean)this.trail.orElseGet(original::hasTrail), (Boolean)this.twinkle.orElseGet(original::hasTwinkle));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetFireworkExplosionFunction> getType() {
/* 60 */     return LootItemFunctions.SET_FIREWORK_EXPLOSION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetFireworkExplosionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */