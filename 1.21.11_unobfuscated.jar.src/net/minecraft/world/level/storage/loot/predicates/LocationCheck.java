/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.LocationPredicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ public final class LocationCheck extends Record implements LootItemCondition {
/*    */   private final Optional<LocationPredicate> predicate;
/*    */   private final BlockPos offset;
/*    */   private static final com.mojang.serialization.MapCodec<BlockPos> OFFSET_CODEC;
/*    */   public static final com.mojang.serialization.MapCodec<LocationCheck> CODEC;
/*    */   
/* 16 */   public LocationCheck(Optional<LocationPredicate> predicate, BlockPos offset) { this.predicate = predicate; this.offset = offset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck; } public Optional<LocationPredicate> predicate() { return this.predicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos offset() { return this.offset; }
/*    */ 
/*    */   
/*    */   static {
/* 20 */     OFFSET_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.INT.optionalFieldOf("offsetX", 0).forGetter(Vec3i::getX), (App)com.mojang.serialization.Codec.INT.optionalFieldOf("offsetY", 0).forGetter(Vec3i::getY), (App)com.mojang.serialization.Codec.INT.optionalFieldOf("offsetZ", 0).forGetter(Vec3i::getZ)).apply((com.mojang.datafixers.kinds.Applicative)i, BlockPos::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LocationPredicate.CODEC.optionalFieldOf("predicate").forGetter(LocationCheck::predicate), (App)OFFSET_CODEC.forGetter(LocationCheck::offset)).apply((com.mojang.datafixers.kinds.Applicative)i, LocationCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 33 */     return LootItemConditions.LOCATION_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(net.minecraft.world.level.storage.loot.LootContext context) {
/* 38 */     net.minecraft.world.phys.Vec3 pos = (net.minecraft.world.phys.Vec3)context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
/* 39 */     return (pos != null && (this.predicate.isEmpty() || ((LocationPredicate)this.predicate.get()).matches(context.getLevel(), pos.x() + this.offset.getX(), pos.y() + this.offset.getY(), pos.z() + this.offset.getZ())));
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 44 */     return java.util.Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate) {
/* 48 */     return () -> new LocationCheck(Optional.of(predicate.build()), BlockPos.ZERO);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate, BlockPos offset) {
/* 52 */     return () -> new LocationCheck(Optional.of(predicate.build()), offset);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LocationCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */