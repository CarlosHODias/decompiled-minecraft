/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class VaultConfig extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   private final double activationRange;
/*    */   private final double deactivationRange;
/*    */   private final net.minecraft.world.item.ItemStack keyItem;
/*    */   private final java.util.Optional<ResourceKey<LootTable>> overrideLootTableToDisplay;
/*    */   private final net.minecraft.world.level.block.entity.trialspawner.PlayerDetector playerDetector;
/*    */   private final net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.EntitySelector entitySelector;
/*    */   static final String TAG_NAME = "config";
/*    */   
/* 16 */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, net.minecraft.world.item.ItemStack keyItem, java.util.Optional<ResourceKey<LootTable>> overrideLootTableToDisplay, net.minecraft.world.level.block.entity.trialspawner.PlayerDetector playerDetector, net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.EntitySelector entitySelector) { this.lootTable = lootTable; this.activationRange = activationRange; this.deactivationRange = deactivationRange; this.keyItem = keyItem; this.overrideLootTableToDisplay = overrideLootTableToDisplay; this.playerDetector = playerDetector; this.entitySelector = entitySelector; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/vault/VaultConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/vault/VaultConfig;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public double activationRange() { return this.activationRange; } public double deactivationRange() { return this.deactivationRange; } public net.minecraft.world.item.ItemStack keyItem() { return this.keyItem; } public java.util.Optional<ResourceKey<LootTable>> overrideLootTableToDisplay() { return this.overrideLootTableToDisplay; } public net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.EntitySelector entitySelector() { return this.entitySelector; }
/*    */   
/* 18 */   static VaultConfig DEFAULT = new VaultConfig();
/*    */ 
/*    */   
/*    */   static com.mojang.serialization.Codec<VaultConfig> CODEC;
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)LootTable.KEY_CODEC.lenientOptionalFieldOf("loot_table", DEFAULT.lootTable()).forGetter(VaultConfig::lootTable), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.DOUBLE.lenientOptionalFieldOf("activation_range", DEFAULT.activationRange()).forGetter(VaultConfig::activationRange), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.DOUBLE.lenientOptionalFieldOf("deactivation_range", DEFAULT.deactivationRange()).forGetter(VaultConfig::deactivationRange), (com.mojang.datafixers.kinds.App)net.minecraft.world.item.ItemStack.lenientOptionalFieldOf("key_item").forGetter(VaultConfig::keyItem), (com.mojang.datafixers.kinds.App)LootTable.KEY_CODEC.lenientOptionalFieldOf("override_loot_table_to_display").forGetter(VaultConfig::overrideLootTableToDisplay)).apply((com.mojang.datafixers.kinds.Applicative)i, VaultConfig::new)).validate(VaultConfig::validate);
/*    */   }
/*    */   private VaultConfig() {
/* 28 */     this(net.minecraft.world.level.storage.loot.BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 4.0D, 4.5D, new net.minecraft.world.item.ItemStack((net.minecraft.world.level.ItemLike)net.minecraft.world.item.Items.TRIAL_KEY), 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 33 */         java.util.Optional.empty(), net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.INCLUDING_CREATIVE_PLAYERS, net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.EntitySelector.SELECT_FROM_LEVEL);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VaultConfig(ResourceKey<LootTable> lootTable, double activationRange, double deactivationRange, net.minecraft.world.item.ItemStack keyItem, java.util.Optional<ResourceKey<LootTable>> overrideDisplayItems) {
/* 39 */     this(lootTable, activationRange, deactivationRange, keyItem, overrideDisplayItems, 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 45 */         DEFAULT.playerDetector(), 
/* 46 */         DEFAULT.entitySelector());
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.world.level.block.entity.trialspawner.PlayerDetector playerDetector() {
/* 51 */     return net.minecraft.SharedConstants.DEBUG_VAULT_DETECTS_SHEEP_AS_PLAYERS ? net.minecraft.world.level.block.entity.trialspawner.PlayerDetector.SHEEP : this.playerDetector;
/*    */   }
/*    */   
/*    */   private com.mojang.serialization.DataResult<VaultConfig> validate() {
/* 55 */     if (this.activationRange > this.deactivationRange) {
/* 56 */       return com.mojang.serialization.DataResult.error(() -> "Activation range must (" + this.activationRange + ") be less or equal to deactivation range (" + this.deactivationRange + ")");
/*    */     }
/* 58 */     return com.mojang.serialization.DataResult.success(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/vault/VaultConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */