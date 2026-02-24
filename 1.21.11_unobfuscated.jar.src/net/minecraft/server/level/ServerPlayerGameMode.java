/*     */ package net.minecraft.server.level;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Abilities;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerPlayerGameMode {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final double FLIGHT_DISABLE_RANGE = 1.0D;
/*     */   protected ServerLevel level;
/*     */   protected final ServerPlayer player;
/*  43 */   private GameType gameModeForPlayer = GameType.DEFAULT_MODE;
/*     */   
/*     */   private GameType previousGameModeForPlayer;
/*     */   private boolean isDestroyingBlock;
/*     */   private int destroyProgressStart;
/*  48 */   private BlockPos destroyPos = BlockPos.ZERO;
/*     */   
/*     */   private int gameTicks;
/*     */   private boolean hasDelayedDestroy;
/*  52 */   private BlockPos delayedDestroyPos = BlockPos.ZERO;
/*     */   private int delayedTickStart;
/*  54 */   private int lastSentState = -1;
/*     */   
/*     */   public ServerPlayerGameMode(ServerPlayer player) {
/*  57 */     this.player = player;
/*  58 */     this.level = player.level();
/*     */   }
/*     */   
/*     */   public boolean changeGameModeForPlayer(GameType gameModeForPlayer) {
/*  62 */     if (gameModeForPlayer == this.gameModeForPlayer) {
/*  63 */       return false;
/*     */     }
/*     */     
/*  66 */     Abilities abilities = this.player.getAbilities();
/*  67 */     setGameModeForPlayer(gameModeForPlayer, this.gameModeForPlayer);
/*     */     
/*  69 */     if (abilities.flying && gameModeForPlayer != GameType.SPECTATOR && isInRangeOfGround()) {
/*  70 */       abilities.flying = false;
/*     */     }
/*     */     
/*  73 */     this.player.onUpdateAbilities();
/*  74 */     this.level.getServer().getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, this.player));
/*  75 */     this.level.updateSleepingPlayerList();
/*     */     
/*  77 */     if (gameModeForPlayer == GameType.CREATIVE) {
/*  78 */       this.player.resetCurrentImpulseContext();
/*     */     }
/*     */     
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   protected void setGameModeForPlayer(GameType gameModeForPlayer, GameType previousGameModeForPlayer) {
/*  85 */     this.previousGameModeForPlayer = previousGameModeForPlayer;
/*  86 */     this.gameModeForPlayer = gameModeForPlayer;
/*     */     
/*  88 */     Abilities abilities = this.player.getAbilities();
/*  89 */     gameModeForPlayer.updatePlayerAbilities(abilities);
/*     */   }
/*     */   
/*     */   private boolean isInRangeOfGround() {
/*  93 */     List<VoxelShape> clipping = Entity.collectAllColliders((Entity)this.player, this.level, this.player.getBoundingBox());
/*  94 */     return (clipping.isEmpty() && this.player.getAvailableSpaceBelow(1.0D) < 1.0D);
/*     */   }
/*     */   
/*     */   public GameType getGameModeForPlayer() {
/*  98 */     return this.gameModeForPlayer;
/*     */   }
/*     */   
/*     */   public GameType getPreviousGameModeForPlayer() {
/* 102 */     return this.previousGameModeForPlayer;
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/* 106 */     return this.gameModeForPlayer.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean isCreative() {
/* 110 */     return this.gameModeForPlayer.isCreative();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 114 */     this.gameTicks++;
/*     */     
/* 116 */     if (this.hasDelayedDestroy) {
/* 117 */       BlockState blockState = this.level.getBlockState(this.delayedDestroyPos);
/* 118 */       if (blockState.isAir()) {
/* 119 */         this.hasDelayedDestroy = false;
/*     */       } else {
/* 121 */         float destroyProgress = incrementDestroyProgress(blockState, this.delayedDestroyPos, this.delayedTickStart);
/*     */         
/* 123 */         if (destroyProgress >= 1.0F) {
/* 124 */           this.hasDelayedDestroy = false;
/* 125 */           destroyBlock(this.delayedDestroyPos);
/*     */         } 
/*     */       } 
/* 128 */     } else if (this.isDestroyingBlock) {
/* 129 */       BlockState blockState = this.level.getBlockState(this.destroyPos);
/*     */       
/* 131 */       if (blockState.isAir()) {
/* 132 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 133 */         this.lastSentState = -1;
/* 134 */         this.isDestroyingBlock = false;
/*     */       } else {
/* 136 */         incrementDestroyProgress(blockState, this.destroyPos, this.destroyProgressStart);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float incrementDestroyProgress(BlockState blockState, BlockPos delayedDestroyPos, int destroyStartTick) {
/* 142 */     int ticksSpentDestroying = this.gameTicks - destroyStartTick;
/* 143 */     float destroyProgress = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), delayedDestroyPos) * (ticksSpentDestroying + 1);
/* 144 */     int state = (int)(destroyProgress * 10.0F);
/*     */     
/* 146 */     if (state != this.lastSentState) {
/* 147 */       this.level.destroyBlockProgress(this.player.getId(), delayedDestroyPos, state);
/* 148 */       this.lastSentState = state;
/*     */     } 
/* 150 */     return destroyProgress;
/*     */   }
/*     */   
/*     */   private void debugLogging(BlockPos pos, boolean allGood, int sequence, String message) {
/* 154 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 155 */       LOGGER.debug("Server ACK {} {} {} {}", new Object[] { sequence, pos, allGood, message });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int maxY, int sequence) {
/* 161 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D)) {
/* 162 */       debugLogging(pos, false, sequence, "too far");
/*     */       return;
/*     */     } 
/* 165 */     if (pos.getY() > maxY) {
/* 166 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 167 */       debugLogging(pos, false, sequence, "too high");
/*     */       
/*     */       return;
/*     */     } 
/* 171 */     if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
/* 172 */       if (!this.level.mayInteract((Entity)this.player, pos)) {
/* 173 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 174 */         debugLogging(pos, false, sequence, "may not interact");
/*     */         return;
/*     */       } 
/* 177 */       if ((this.player.getAbilities()).instabuild) {
/* 178 */         destroyAndAck(pos, sequence, "creative destroy");
/*     */         
/*     */         return;
/*     */       } 
/* 182 */       if (this.player.blockActionRestricted(this.level, pos, this.gameModeForPlayer)) {
/* 183 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 184 */         debugLogging(pos, false, sequence, "block action restricted");
/*     */         
/*     */         return;
/*     */       } 
/* 188 */       this.destroyProgressStart = this.gameTicks;
/* 189 */       float progress = 1.0F;
/* 190 */       BlockState blockState = this.level.getBlockState(pos);
/* 191 */       if (!blockState.isAir()) {
/* 192 */         EnchantmentHelper.onHitBlock(this.level, this.player.getMainHandItem(), (LivingEntity)this.player, (Entity)this.player, EquipmentSlot.MAINHAND, Vec3.atCenterOf((Vec3i)pos), blockState, item -> this.player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
/* 193 */         blockState.attack(this.level, pos, this.player);
/* 194 */         progress = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), pos);
/*     */       } 
/*     */       
/* 197 */       if (!blockState.isAir() && progress >= 1.0F) {
/* 198 */         destroyAndAck(pos, sequence, "insta mine");
/*     */       } else {
/* 200 */         if (this.isDestroyingBlock) {
/* 201 */           this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(this.destroyPos, this.level.getBlockState(this.destroyPos)));
/* 202 */           debugLogging(pos, false, sequence, "abort destroying since another started (client insta mine, server disagreed)");
/*     */         } 
/* 204 */         this.isDestroyingBlock = true;
/* 205 */         this.destroyPos = pos.immutable();
/*     */         
/* 207 */         int state = (int)(progress * 10.0F);
/* 208 */         this.level.destroyBlockProgress(this.player.getId(), pos, state);
/* 209 */         debugLogging(pos, true, sequence, "actual start of destroying");
/* 210 */         this.lastSentState = state;
/*     */       } 
/* 212 */     } else if (action == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
/*     */       
/* 214 */       if (pos.equals(this.destroyPos)) {
/* 215 */         int ticksSpentDestroying = this.gameTicks - this.destroyProgressStart;
/*     */         
/* 217 */         BlockState state = this.level.getBlockState(pos);
/* 218 */         if (!state.isAir()) {
/* 219 */           float destroyProgress = state.getDestroyProgress(this.player, (BlockGetter)this.player.level(), pos) * (ticksSpentDestroying + 1);
/* 220 */           if (destroyProgress >= 0.7F) {
/* 221 */             this.isDestroyingBlock = false;
/* 222 */             this.level.destroyBlockProgress(this.player.getId(), pos, -1);
/* 223 */             destroyAndAck(pos, sequence, "destroyed"); return;
/*     */           } 
/* 225 */           if (!this.hasDelayedDestroy) {
/* 226 */             this.isDestroyingBlock = false;
/* 227 */             this.hasDelayedDestroy = true;
/* 228 */             this.delayedDestroyPos = pos;
/* 229 */             this.delayedTickStart = this.destroyProgressStart;
/*     */           } 
/*     */         } 
/*     */       } 
/* 233 */       debugLogging(pos, true, sequence, "stopped destroying");
/* 234 */     } else if (action == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
/* 235 */       this.isDestroyingBlock = false;
/* 236 */       if (!Objects.equals(this.destroyPos, pos)) {
/* 237 */         LOGGER.warn("Mismatch in destroy block pos: {} {}", this.destroyPos, pos);
/* 238 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 239 */         debugLogging(pos, true, sequence, "aborted mismatched destroying");
/*     */       } 
/*     */       
/* 242 */       this.level.destroyBlockProgress(this.player.getId(), pos, -1);
/* 243 */       debugLogging(pos, true, sequence, "aborted destroying");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyAndAck(BlockPos pos, int sequence, String exitId) {
/* 248 */     if (destroyBlock(pos)) {
/* 249 */       debugLogging(pos, true, sequence, exitId);
/*     */     } else {
/* 251 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 252 */       debugLogging(pos, false, sequence, exitId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos) {
/* 261 */     BlockState state = this.level.getBlockState(pos);
/* 262 */     if (!this.player.getMainHandItem().canDestroyBlock(state, this.level, pos, this.player)) {
/* 263 */       return false;
/*     */     }
/*     */     
/* 266 */     BlockEntity blockEntity = this.level.getBlockEntity(pos);
/* 267 */     Block block = state.getBlock();
/*     */ 
/*     */     
/* 270 */     if (block instanceof net.minecraft.world.level.block.GameMasterBlock && !this.player.canUseGameMasterBlocks()) {
/* 271 */       this.level.sendBlockUpdated(pos, state, state, 3);
/* 272 */       return false;
/*     */     } 
/*     */     
/* 275 */     if (this.player.blockActionRestricted(this.level, pos, this.gameModeForPlayer)) {
/* 276 */       return false;
/*     */     }
/*     */     
/* 279 */     BlockState adjustedState = block.playerWillDestroy(this.level, pos, state, this.player);
/*     */ 
/*     */     
/* 282 */     boolean changed = this.level.removeBlock(pos, false);
/* 283 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 284 */       LOGGER.info("server broke {} {} -> {}", new Object[] { pos, adjustedState, this.level.getBlockState(pos) });
/*     */     }
/* 286 */     if (changed) {
/* 287 */       block.destroy((LevelAccessor)this.level, pos, adjustedState);
/*     */     }
/*     */     
/* 290 */     if (this.player.preventsBlockDrops()) {
/* 291 */       return true;
/*     */     }
/*     */     
/* 294 */     ItemStack itemStack = this.player.getMainHandItem();
/*     */     
/* 296 */     ItemStack destroyedWith = itemStack.copy();
/* 297 */     boolean canDestroy = this.player.hasCorrectToolForDrops(adjustedState);
/* 298 */     itemStack.mineBlock(this.level, adjustedState, pos, this.player);
/* 299 */     if (changed && canDestroy) {
/* 300 */       block.playerDestroy(this.level, this.player, pos, adjustedState, blockEntity, destroyedWith);
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */   public InteractionResult useItem(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand) {
/*     */     ItemStack resultStack;
/* 306 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 307 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 309 */     if (player.getCooldowns().isOnCooldown(itemStack)) {
/* 310 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 313 */     int oldCount = itemStack.getCount();
/* 314 */     int oldDamage = itemStack.getDamageValue();
/* 315 */     InteractionResult result = itemStack.use(level, player, hand);
/*     */ 
/*     */     
/* 318 */     if (result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result;
/* 319 */       resultStack = Objects.<ItemStack>requireNonNullElse(success.heldItemTransformedTo(), player.getItemInHand(hand)); }
/*     */     else
/* 321 */     { resultStack = player.getItemInHand(hand); }
/*     */ 
/*     */     
/* 324 */     if (resultStack == itemStack && resultStack.getCount() == oldCount && resultStack.getUseDuration((LivingEntity)player) <= 0 && resultStack.getDamageValue() == oldDamage) {
/* 325 */       return result;
/*     */     }
/*     */     
/* 328 */     if (result instanceof InteractionResult.Fail && resultStack.getUseDuration((LivingEntity)player) > 0 && !player.isUsingItem()) {
/* 329 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 333 */     if (itemStack != resultStack) {
/* 334 */       player.setItemInHand(hand, resultStack);
/*     */     }
/* 336 */     if (resultStack.isEmpty()) {
/* 337 */       player.setItemInHand(hand, ItemStack.EMPTY);
/*     */     }
/* 339 */     if (!player.isUsingItem()) {
/* 340 */       player.inventoryMenu.sendAllDataToRemote();
/*     */     }
/* 342 */     return result;
/*     */   }
/*     */   public InteractionResult useItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, BlockHitResult hitResult) {
/*     */     InteractionResult success;
/* 346 */     BlockPos pos = hitResult.getBlockPos();
/*     */     
/* 348 */     BlockState state = level.getBlockState(pos);
/* 349 */     if (!state.getBlock().isEnabled(level.enabledFeatures())) {
/* 350 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/* 353 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 354 */       MenuProvider menuProvider = state.getMenuProvider(level, pos);
/* 355 */       if (menuProvider != null) {
/* 356 */         player.openMenu(menuProvider);
/*     */         
/* 358 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       } 
/* 360 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/*     */     
/* 363 */     boolean haveSomethingInOurHands = (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty());
/* 364 */     boolean suppressUsingBlock = (player.isSecondaryUseActive() && haveSomethingInOurHands);
/* 365 */     ItemStack usedItemStack = itemStack.copy();
/*     */     
/* 367 */     if (!suppressUsingBlock) {
/* 368 */       InteractionResult itemUse = state.useItemOn(player.getItemInHand(hand), level, player, hand, hitResult);
/* 369 */       if (itemUse.consumesAction()) {
/* 370 */         CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, usedItemStack);
/* 371 */         return itemUse;
/*     */       } 
/*     */       
/* 374 */       if (itemUse instanceof InteractionResult.TryEmptyHandInteraction && hand == InteractionHand.MAIN_HAND) {
/* 375 */         InteractionResult use = state.useWithoutItem(level, player, hitResult);
/* 376 */         if (use.consumesAction()) {
/* 377 */           CriteriaTriggers.DEFAULT_BLOCK_USE.trigger(player, pos);
/* 378 */           return use;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 383 */     if (itemStack.isEmpty() || player.getCooldowns().isOnCooldown(itemStack)) {
/* 384 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 387 */     UseOnContext context = new UseOnContext(player, hand, hitResult);
/*     */     
/* 389 */     if (player.hasInfiniteMaterials()) {
/*     */       
/* 391 */       int count = itemStack.getCount();
/* 392 */       success = itemStack.useOn(context);
/* 393 */       itemStack.setCount(count);
/*     */     } else {
/* 395 */       success = itemStack.useOn(context);
/*     */     } 
/* 397 */     if (success.consumesAction()) {
/* 398 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, usedItemStack);
/*     */     }
/* 400 */     return success;
/*     */   }
/*     */   
/*     */   public void setLevel(ServerLevel newLevel) {
/* 404 */     this.level = newLevel;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ServerPlayerGameMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */