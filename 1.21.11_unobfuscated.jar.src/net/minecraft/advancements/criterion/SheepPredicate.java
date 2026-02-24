/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ 
/*    */ public final class SheepPredicate extends Record implements EntitySubPredicate {
/*    */   private final Optional<Boolean> sheared;
/*    */   public static final com.mojang.serialization.MapCodec<SheepPredicate> CODEC;
/*    */   
/* 14 */   public SheepPredicate(Optional<Boolean> sheared) { this.sheared = sheared; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SheepPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate; } public Optional<Boolean> sheared() { return this.sheared; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SheepPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SheepPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("sheared").forGetter(SheepPredicate::sheared)).apply((com.mojang.datafixers.kinds.Applicative)i, SheepPredicate::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SheepPredicate> codec() {
/* 21 */     return EntitySubPredicates.SHEEP;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, net.minecraft.server.level.ServerLevel level, net.minecraft.world.phys.Vec3 position) {
/* 26 */     if (entity instanceof Sheep) { Sheep sheep = (Sheep)entity;
/* 27 */       if (this.sheared.isPresent() && sheep.isSheared() != (Boolean)this.sheared.get()) {
/* 28 */         return false;
/*    */       }
/* 30 */       return true; }
/*    */     
/* 32 */     return false;
/*    */   }
/*    */   
/*    */   public static SheepPredicate hasWool() {
/* 36 */     return new SheepPredicate(Optional.of(false));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/SheepPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */