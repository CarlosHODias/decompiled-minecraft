/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public final class ReplaceBlock extends Record implements EnchantmentEntityEffect {
/*    */   private final Vec3i offset;
/*    */   private final Optional<BlockPredicate> predicate;
/*    */   private final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState;
/*    */   private final Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent;
/*    */   public static final com.mojang.serialization.MapCodec<ReplaceBlock> CODEC;
/*    */   
/* 18 */   public ReplaceBlock(Vec3i offset, Optional<BlockPredicate> predicate, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState, Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent) { this.offset = offset; this.predicate = predicate; this.blockState = blockState; this.triggerGameEvent = triggerGameEvent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock; } public Vec3i offset() { return this.offset; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<BlockPredicate> predicate() { return this.predicate; } public net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider blockState() { return this.blockState; } public Optional<Holder<net.minecraft.world.level.gameevent.GameEvent>> triggerGameEvent() { return this.triggerGameEvent; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 24 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceBlock::offset), (App)BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceBlock::predicate), (App)net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceBlock::blockState), (App)net.minecraft.world.level.gameevent.GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceBlock::triggerGameEvent)).apply((com.mojang.datafixers.kinds.Applicative)i, ReplaceBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 33 */     BlockPos pos = BlockPos.containing((net.minecraft.core.Position)position).offset(this.offset);
/* 34 */     if ((Boolean)this.predicate.<Boolean>map(p -> p.test(serverLevel, pos)).orElse(true) && 
/* 35 */       serverLevel.setBlockAndUpdate(pos, this.blockState.getState(entity.getRandom(), pos))) {
/* 36 */       this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, pos));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ReplaceBlock> codec() {
/* 43 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/ReplaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */