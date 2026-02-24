/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public enum VaultState implements StringRepresentable {
/*  16 */   INACTIVE("inactive", LightLevel.HALF_LIT)
/*     */   {
/*     */     protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/*  19 */       sharedData.setDisplayItem(ItemStack.EMPTY);
/*  20 */       serverLevel.levelEvent(3016, pos, isOminous ? 1 : 0);
/*     */     }
/*     */   },
/*  23 */   ACTIVE("active", LightLevel.LIT)
/*     */   {
/*     */     protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/*  26 */       if (!sharedData.hasDisplayItem()) {
/*  27 */         VaultBlockEntity.Server.cycleDisplayItemFromLootTable(serverLevel, this, config, sharedData, pos);
/*     */       }
/*  29 */       serverLevel.levelEvent(3015, pos, isOminous ? 1 : 0);
/*     */     }
/*     */   },
/*  32 */   UNLOCKING("unlocking", LightLevel.LIT)
/*     */   {
/*     */     protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/*  35 */       serverLevel.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS);
/*     */     }
/*     */   },
/*  38 */   EJECTING("ejecting", LightLevel.LIT)
/*     */   {
/*     */     protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/*  41 */       serverLevel.playSound(null, pos, SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onExit(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData) {
/*  46 */       serverLevel.playSound(null, pos, SoundEvents.VAULT_CLOSE_SHUTTER, SoundSource.BLOCKS);
/*     */     }
/*     */   };
/*     */   
/*     */   private static final int UPDATE_CONNECTED_PLAYERS_TICK_RATE = 20;
/*     */   private static final int DELAY_BETWEEN_EJECTIONS_TICKS = 20;
/*     */   private static final int DELAY_AFTER_LAST_EJECTION_TICKS = 20;
/*     */   private static final int DELAY_BEFORE_FIRST_EJECTION_TICKS = 20;
/*     */   private final String stateName;
/*     */   private final LightLevel lightLevel;
/*     */   
/*     */   VaultState(String stateName, LightLevel lightLevel) {
/*  58 */     this.stateName = stateName;
/*  59 */     this.lightLevel = lightLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/*  64 */     return this.stateName;
/*     */   }
/*     */   
/*     */   public int lightLevel() {
/*  68 */     return this.lightLevel.value; } public VaultState tickAndGetNext(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/*     */     float ejectionSoundProgress;
/*     */     boolean isLastEjection;
/*     */     int ejectionDelay;
/*  72 */     switch (ordinal()) { default: throw new MatchException(null, null);
/*     */       case 0: 
/*     */       case 1: 
/*     */       case 2:
/*  76 */         serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
/*     */ 
/*     */       
/*     */       case 3:
/*  80 */         if (serverData.getItemsToEject().isEmpty()) {
/*  81 */           serverData.markEjectionFinished();
/*     */         }
/*     */ 
/*     */         
/*  85 */         ejectionSoundProgress = serverData.ejectionProgress();
/*  86 */         ejectResultItem(serverLevel, pos, serverData.popNextItemToEject(), ejectionSoundProgress);
/*  87 */         sharedData.setDisplayItem(serverData.getNextItemToEject());
/*     */         
/*  89 */         isLastEjection = serverData.getItemsToEject().isEmpty();
/*  90 */         ejectionDelay = isLastEjection ? 20 : 20;
/*  91 */         serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + ejectionDelay); }
/*  92 */      return EJECTING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static VaultState updateStateForConnectedPlayers(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, double activationRange) {
/*  98 */     sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, activationRange);
/*  99 */     serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
/* 100 */     return sharedData.hasConnectedPlayers() ? ACTIVE : INACTIVE;
/*     */   }
/*     */   
/*     */   public void onTransition(ServerLevel serverLevel, BlockPos pos, VaultState to, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/* 104 */     onExit(serverLevel, pos, config, sharedData);
/* 105 */     to.onEnter(serverLevel, pos, config, sharedData, isOminous);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {}
/*     */ 
/*     */   
/*     */   protected void onExit(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData) {}
/*     */   
/*     */   private void ejectResultItem(ServerLevel serverLevel, BlockPos pos, ItemStack itemToEject, float ejectionSoundProgress) {
/* 115 */     DefaultDispenseItemBehavior.spawnItem((Level)serverLevel, itemToEject, 2, Direction.UP, (Position)Vec3.atBottomCenterOf((Vec3i)pos).relative(Direction.UP, 1.2D));
/* 116 */     serverLevel.levelEvent(3017, pos, 0);
/* 117 */     serverLevel.playSound(null, pos, SoundEvents.VAULT_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * ejectionSoundProgress);
/*     */   }
/*     */   
/*     */   private enum LightLevel {
/* 121 */     HALF_LIT(6),
/* 122 */     LIT(12);
/*     */     
/*     */     final int value;
/*     */ 
/*     */     
/*     */     LightLevel(int value) {
/* 128 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/vault/VaultState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */