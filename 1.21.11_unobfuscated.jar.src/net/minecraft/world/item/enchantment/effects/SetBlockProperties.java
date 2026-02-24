/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public final class SetBlockProperties extends Record implements EnchantmentEntityEffect {
/*    */   private final BlockItemStateProperties properties;
/*    */   private final Vec3i offset;
/*    */   private final Optional<Holder<GameEvent>> triggerGameEvent;
/*    */   public static final com.mojang.serialization.MapCodec<SetBlockProperties> CODEC;
/*    */   
/* 19 */   public SetBlockProperties(BlockItemStateProperties properties, Vec3i offset, Optional<Holder<GameEvent>> triggerGameEvent) { this.properties = properties; this.offset = offset; this.triggerGameEvent = triggerGameEvent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties; } public BlockItemStateProperties properties() { return this.properties; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SetBlockProperties;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3i offset() { return this.offset; } public Optional<Holder<GameEvent>> triggerGameEvent() { return this.triggerGameEvent; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockItemStateProperties.CODEC.fieldOf("properties").forGetter(SetBlockProperties::properties), (App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(SetBlockProperties::offset), (App)GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(SetBlockProperties::triggerGameEvent)).apply((com.mojang.datafixers.kinds.Applicative)i, SetBlockProperties::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SetBlockProperties(BlockItemStateProperties properties) {
/* 31 */     this(properties, Vec3i.ZERO, (Optional)Optional.of(GameEvent.BLOCK_CHANGE));
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, Entity entity, net.minecraft.world.phys.Vec3 position) {
/* 36 */     BlockPos blockPos = BlockPos.containing((net.minecraft.core.Position)position).offset(this.offset);
/* 37 */     net.minecraft.world.level.block.state.BlockState state = entity.level().getBlockState(blockPos);
/* 38 */     net.minecraft.world.level.block.state.BlockState modified = this.properties.apply(state);
/* 39 */     if (state != modified && 
/* 40 */       entity.level().setBlock(blockPos, modified, 3)) {
/* 41 */       this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, blockPos));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SetBlockProperties> codec() {
/* 48 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/SetBlockProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */