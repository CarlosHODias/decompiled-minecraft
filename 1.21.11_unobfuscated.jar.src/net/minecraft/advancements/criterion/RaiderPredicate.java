/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public final class RaiderPredicate extends Record implements EntitySubPredicate {
/*    */   private final boolean hasRaid;
/*    */   private final boolean isCaptain;
/*    */   public static final com.mojang.serialization.MapCodec<RaiderPredicate> CODEC;
/*    */   
/* 12 */   public RaiderPredicate(boolean hasRaid, boolean isCaptain) { this.hasRaid = hasRaid; this.isCaptain = isCaptain; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/RaiderPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate; } public boolean hasRaid() { return this.hasRaid; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/RaiderPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/RaiderPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public boolean isCaptain() { return this.isCaptain; } static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("has_raid", false).forGetter(RaiderPredicate::hasRaid), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("is_captain", false).forGetter(RaiderPredicate::isCaptain)).apply((com.mojang.datafixers.kinds.Applicative)i, RaiderPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final RaiderPredicate CAPTAIN_WITHOUT_RAID = new RaiderPredicate(false, true);
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<RaiderPredicate> codec() {
/* 22 */     return EntitySubPredicates.RAIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.entity.Entity entity, net.minecraft.server.level.ServerLevel level, net.minecraft.world.phys.Vec3 position) {
/* 27 */     if (entity instanceof Raider) { Raider raider = (Raider)entity;
/* 28 */       return (raider.hasRaid() == this.hasRaid && raider.isCaptain() == this.isCaptain); }
/*    */     
/* 30 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/RaiderPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */