/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworksFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ListOperation.StandAlone.<T>codec(FireworkExplosion.CODEC, 256).optionalFieldOf("explosions").forGetter(()), (App)ExtraCodecs.UNSIGNED_BYTE.optionalFieldOf("flight_duration").forGetter(()))).apply((Applicative)i, SetFireworksFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetFireworksFunction> CODEC;
/* 22 */   public static final Fireworks DEFAULT_VALUE = new Fireworks(0, List.of());
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<FireworkExplosion>> explosions;
/*    */   private final Optional<Integer> flightDuration;
/*    */   
/*    */   protected SetFireworksFunction(List<LootItemCondition> predicates, Optional<ListOperation.StandAlone<FireworkExplosion>> explosions, Optional<Integer> flightDuration) {
/* 28 */     super(predicates);
/* 29 */     this.explosions = explosions;
/* 30 */     this.flightDuration = flightDuration;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 35 */     itemStack.update(net.minecraft.core.component.DataComponents.FIREWORKS, DEFAULT_VALUE, this::apply);
/* 36 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private Fireworks apply(Fireworks old) {
/* 41 */     java.util.Objects.requireNonNull(old); return new Fireworks((Integer)this.flightDuration.orElseGet(old::flightDuration), 
/* 42 */         this.explosions.<List>map(operation -> operation.apply(old.explosions())).orElse(old.explosions()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetFireworksFunction> getType() {
/* 48 */     return LootItemFunctions.SET_FIREWORKS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetFireworksFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */