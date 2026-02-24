/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public final class ReplaceDisk extends Record implements EnchantmentEntityEffect {
/*    */   private final LevelBasedValue radius;
/*    */   private final LevelBasedValue height;
/*    */   private final Vec3i offset;
/*    */   private final Optional<BlockPredicate> predicate;
/*    */   private final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState;
/*    */   private final Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent;
/*    */   public static final com.mojang.serialization.MapCodec<ReplaceDisk> CODEC;
/*    */   
/* 21 */   public ReplaceDisk(LevelBasedValue radius, LevelBasedValue height, Vec3i offset, Optional<BlockPredicate> predicate, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState, Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent) { this.radius = radius; this.height = height; this.offset = offset; this.predicate = predicate; this.blockState = blockState; this.triggerGameEvent = triggerGameEvent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 21 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk; } public LevelBasedValue radius() { return this.radius; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceDisk;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue height() { return this.height; } public Vec3i offset() { return this.offset; } public Optional<BlockPredicate> predicate() { return this.predicate; } public net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState() { return this.blockState; } public Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent() { return this.triggerGameEvent; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 29 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("radius").forGetter(ReplaceDisk::radius), (App)LevelBasedValue.CODEC.fieldOf("height").forGetter(ReplaceDisk::height), (App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceDisk::offset), (App)BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceDisk::predicate), (App)net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceDisk::blockState), (App)net.minecraft.world.level.gameevent.GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceDisk::triggerGameEvent)).apply((com.mojang.datafixers.kinds.Applicative)i, ReplaceDisk::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 40 */     BlockPos centerBlock = BlockPos.containing((net.minecraft.core.Position)position).offset(this.offset);
/* 41 */     net.minecraft.util.RandomSource random = entity.getRandom();
/* 42 */     int dist = (int)this.radius.calculate(enchantmentLevel);
/* 43 */     int height = (int)this.height.calculate(enchantmentLevel);
/* 44 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(centerBlock.offset(-dist, 0, -dist), centerBlock.offset(dist, Math.min(height - 1, 0), dist))) {
/* 45 */       if (pos.distToCenterSqr(position.x(), pos.getY() + 0.5D, position.z()) < net.minecraft.util.Mth.square(dist) && (Boolean)this.predicate.<Boolean>map(p -> p.test(serverLevel, pos)).orElse(true) && 
/* 46 */         serverLevel.setBlockAndUpdate(pos, this.blockState.getState(random, pos))) {
/* 47 */         this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, pos));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ReplaceDisk> codec() {
/* 55 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/ReplaceDisk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */