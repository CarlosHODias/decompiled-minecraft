/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ 
/*    */ public final class FishingHookPredicate extends Record implements EntitySubPredicate {
/*    */   private final Optional<Boolean> inOpenWater;
/*    */   
/* 14 */   public FishingHookPredicate(Optional<Boolean> inOpenWater) { this.inOpenWater = inOpenWater; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/FishingHookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/FishingHookPredicate; } public Optional<Boolean> inOpenWater() { return this.inOpenWater; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/FishingHookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/FishingHookPredicate; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/FishingHookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/FishingHookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 17 */   } public static final FishingHookPredicate ANY = new FishingHookPredicate(Optional.empty()); public static final MapCodec<FishingHookPredicate> CODEC;
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("in_open_water").forGetter(FishingHookPredicate::inOpenWater)).apply((com.mojang.datafixers.kinds.Applicative)i, FishingHookPredicate::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static FishingHookPredicate inOpenWater(boolean requirement) {
/* 24 */     return new FishingHookPredicate(Optional.of(requirement));
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<FishingHookPredicate> codec() {
/* 29 */     return EntitySubPredicates.FISHING_HOOK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, net.minecraft.server.level.ServerLevel level, net.minecraft.world.phys.Vec3 position) {
/* 34 */     if (this.inOpenWater.isEmpty()) {
/* 35 */       return true;
/*    */     }
/* 37 */     if (entity instanceof FishingHook) { FishingHook hook = (FishingHook)entity;
/* 38 */       return ((Boolean)this.inOpenWater.get() == hook.isOpenWaterFishing()); }
/*    */     
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/FishingHookPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */