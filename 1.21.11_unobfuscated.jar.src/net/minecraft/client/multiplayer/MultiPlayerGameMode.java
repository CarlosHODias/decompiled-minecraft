/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.primitives.Shorts;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
/*     */ import net.minecraft.client.multiplayer.prediction.PredictiveAction;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.HashedStack;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Input;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ClickType;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiPlayerGameMode
/*     */ {
/*  71 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ClientPacketListener connection;
/*  76 */   private BlockPos destroyBlockPos = new BlockPos(-1, -1, -1);
/*  77 */   private ItemStack destroyingItem = ItemStack.EMPTY;
/*     */   private float destroyProgress;
/*     */   private float destroyTicks;
/*     */   private int destroyDelay;
/*     */   private boolean isDestroying;
/*  82 */   private GameType localPlayerMode = GameType.DEFAULT_MODE; private GameType previousLocalPlayerMode;
/*     */   private int carriedIndex;
/*     */   
/*     */   public MultiPlayerGameMode(Minecraft minecraft, ClientPacketListener connection) {
/*  86 */     this.minecraft = minecraft;
/*  87 */     this.connection = connection;
/*     */   }
/*     */   
/*     */   public void adjustPlayer(Player player) {
/*  91 */     this.localPlayerMode.updatePlayerAbilities(player.getAbilities());
/*     */   }
/*     */   
/*     */   public void setLocalMode(GameType mode, GameType previousMode) {
/*  95 */     this.localPlayerMode = mode;
/*  96 */     this.previousLocalPlayerMode = previousMode;
/*  97 */     this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.getAbilities());
/*     */   }
/*     */   
/*     */   public void setLocalMode(GameType mode) {
/* 101 */     if (mode != this.localPlayerMode) {
/* 102 */       this.previousLocalPlayerMode = this.localPlayerMode;
/*     */     }
/* 104 */     this.localPlayerMode = mode;
/* 105 */     this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.getAbilities());
/*     */   }
/*     */   
/*     */   public boolean canHurtPlayer() {
/* 109 */     return this.localPlayerMode.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos) {
/* 113 */     if (this.minecraft.player.blockActionRestricted(this.minecraft.level, pos, this.localPlayerMode)) {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     Level level = this.minecraft.level;
/* 118 */     BlockState oldState = level.getBlockState(pos);
/* 119 */     if (!this.minecraft.player.getMainHandItem().canDestroyBlock(oldState, level, pos, (Player)this.minecraft.player)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     Block oldBlock = oldState.getBlock();
/*     */     
/* 125 */     if (oldBlock instanceof net.minecraft.world.level.block.GameMasterBlock && !this.minecraft.player.canUseGameMasterBlocks()) {
/* 126 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 130 */     if (oldState.isAir()) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     oldBlock.playerWillDestroy(level, pos, oldState, (Player)this.minecraft.player);
/* 135 */     FluidState fluidState = level.getFluidState(pos);
/*     */     
/* 137 */     boolean changed = level.setBlock(pos, fluidState.createLegacyBlock(), 11);
/* 138 */     if (changed) {
/* 139 */       oldBlock.destroy((LevelAccessor)level, pos, oldState);
/*     */     }
/* 141 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 142 */       LOGGER.error("client broke {} {} -> {}", new Object[] { pos, oldState, level.getBlockState(pos) });
/*     */     }
/*     */     
/* 145 */     return changed;
/*     */   }
/*     */   
/*     */   public boolean startDestroyBlock(BlockPos pos, Direction direction) {
/* 149 */     if (this.minecraft.player.blockActionRestricted(this.minecraft.level, pos, this.localPlayerMode)) {
/* 150 */       return false;
/*     */     }
/*     */     
/* 153 */     if (!this.minecraft.level.getWorldBorder().isWithinBounds(pos)) {
/* 154 */       return false;
/*     */     }
/*     */     
/* 157 */     if ((this.minecraft.player.getAbilities()).instabuild) {
/* 158 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 159 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 1.0F);
/* 160 */       if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 161 */         LOGGER.info("Creative start {} {}", pos, state);
/*     */       }
/* 163 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             destroyBlock(pos);
/*     */             
/*     */             return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, pos, direction);
/*     */           });
/* 168 */       this.destroyDelay = 5;
/* 169 */     } else if (!this.isDestroying || !sameDestroyTarget(pos)) {
/* 170 */       if (this.isDestroying) {
/* 171 */         if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 172 */           LOGGER.info("Abort old break {} {}", pos, this.minecraft.level.getBlockState(pos));
/*     */         }
/* 174 */         this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, direction));
/*     */       } 
/* 176 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 177 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 0.0F);
/* 178 */       if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 179 */         LOGGER.info("Start break {} {}", pos, state);
/*     */       }
/*     */       
/* 182 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             boolean notAir = !state.isAir();
/*     */             if (notAir && this.destroyProgress == 0.0F) {
/*     */               state.attack(this.minecraft.level, state, (Player)this.minecraft.player);
/*     */             }
/*     */             if (notAir && state.getDestroyProgress((Player)this.minecraft.player, (BlockGetter)this.minecraft.player.level(), state) >= 1.0F) {
/*     */               destroyBlock(state);
/*     */             } else {
/*     */               this.isDestroying = true;
/*     */               this.destroyBlockPos = state;
/*     */               this.destroyingItem = this.minecraft.player.getMainHandItem();
/*     */               this.destroyProgress = 0.0F;
/*     */               this.destroyTicks = 0.0F;
/*     */               this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, getDestroyStage());
/*     */             } 
/*     */             return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, state, pos, direction);
/*     */           });
/*     */     } 
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   public void stopDestroyBlock() {
/* 204 */     if (this.isDestroying) {
/* 205 */       BlockState state = this.minecraft.level.getBlockState(this.destroyBlockPos);
/* 206 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, state, -1.0F);
/* 207 */       if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 208 */         LOGGER.info("Stop dest {} {}", this.destroyBlockPos, state);
/*     */       }
/* 210 */       this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN));
/* 211 */       this.isDestroying = false;
/* 212 */       this.destroyProgress = 0.0F;
/* 213 */       this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
/* 214 */       this.minecraft.player.resetAttackStrengthTicker();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean continueDestroyBlock(BlockPos pos, Direction direction) {
/* 219 */     ensureHasSentCarriedItem();
/*     */     
/* 221 */     if (this.destroyDelay > 0) {
/* 222 */       this.destroyDelay--;
/* 223 */       return true;
/*     */     } 
/*     */     
/* 226 */     if ((this.minecraft.player.getAbilities()).instabuild && this.minecraft.level.getWorldBorder().isWithinBounds(pos)) {
/* 227 */       this.destroyDelay = 5;
/* 228 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 229 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 1.0F);
/* 230 */       if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 231 */         LOGGER.info("Creative cont {} {}", pos, state);
/*     */       }
/* 233 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             destroyBlock(pos);
/*     */             return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, pos, direction);
/*     */           });
/* 237 */       return true;
/*     */     } 
/*     */     
/* 240 */     if (sameDestroyTarget(pos)) {
/* 241 */       BlockState state = this.minecraft.level.getBlockState(pos);
/*     */       
/* 243 */       if (state.isAir()) {
/* 244 */         this.isDestroying = false;
/* 245 */         return false;
/*     */       } 
/*     */       
/* 248 */       this.destroyProgress += state.getDestroyProgress((Player)this.minecraft.player, (BlockGetter)this.minecraft.player.level(), pos);
/*     */       
/* 250 */       if (this.destroyTicks % 4.0F == 0.0F) {
/* 251 */         SoundType soundType = state.getSoundType();
/* 252 */         this.minecraft.getSoundManager().play((SoundInstance)new SimpleSoundInstance(
/* 253 */               soundType.getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), pos));
/*     */       } 
/*     */ 
/*     */       
/* 257 */       this.destroyTicks++;
/*     */       
/* 259 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, Mth.clamp(this.destroyProgress, 0.0F, 1.0F));
/* 260 */       if (this.destroyProgress >= 1.0F) {
/* 261 */         this.isDestroying = false;
/* 262 */         if (SharedConstants.DEBUG_BLOCK_BREAK) {
/* 263 */           LOGGER.info("Finished breaking {} {}", pos, state);
/*     */         }
/* 265 */         startPrediction(this.minecraft.level, sequence -> {
/*     */               destroyBlock(pos);
/*     */               
/*     */               return new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, pos, direction);
/*     */             });
/* 270 */         this.destroyProgress = 0.0F;
/* 271 */         this.destroyTicks = 0.0F;
/* 272 */         this.destroyDelay = 5;
/*     */       } 
/*     */       
/* 275 */       this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, getDestroyStage());
/*     */     } else {
/* 277 */       return startDestroyBlock(pos, direction);
/*     */     } 
/* 279 */     return true;
/*     */   }
/*     */   
/*     */   private void startPrediction(ClientLevel level, PredictiveAction predictiveAction) {
/* 283 */     BlockStatePredictionHandler prediction = level.getBlockStatePredictionHandler().startPredicting(); 
/* 284 */     try { int sequence = prediction.currentSequence();
/* 285 */       Packet<ServerGamePacketListener> packetConcludingPrediction = predictiveAction.predict(sequence);
/* 286 */       this.connection.send(packetConcludingPrediction);
/* 287 */       if (prediction != null) prediction.close();  } catch (Throwable throwable) { if (prediction != null)
/*     */         try { prediction.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 291 */      } public void tick() { ensureHasSentCarriedItem();
/*     */     
/* 293 */     if (this.connection.getConnection().isConnected()) {
/* 294 */       this.connection.getConnection().tick();
/*     */     } else {
/* 296 */       this.connection.getConnection().handleDisconnection();
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sameDestroyTarget(BlockPos pos) {
/* 303 */     ItemStack selected = this.minecraft.player.getMainHandItem();
/* 304 */     return (pos.equals(this.destroyBlockPos) && ItemStack.isSameItemSameComponents(selected, this.destroyingItem));
/*     */   }
/*     */   
/*     */   private void ensureHasSentCarriedItem() {
/* 308 */     int index = this.minecraft.player.getInventory().getSelectedSlot();
/* 309 */     if (index != this.carriedIndex) {
/* 310 */       this.carriedIndex = index;
/* 311 */       this.connection.send((Packet<?>)new ServerboundSetCarriedItemPacket(this.carriedIndex));
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult useItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit) {
/* 316 */     ensureHasSentCarriedItem();
/*     */     
/* 318 */     if (!this.minecraft.level.getWorldBorder().isWithinBounds(blockHit.getBlockPos())) {
/* 319 */       return (InteractionResult)InteractionResult.FAIL;
/*     */     }
/*     */     
/* 322 */     MutableObject<InteractionResult> result = new MutableObject();
/* 323 */     startPrediction(this.minecraft.level, sequence -> {
/*     */           result.setValue(performUseItemOn(result, result, player));
/*     */           
/*     */           return new ServerboundUseItemOnPacket(result, player, blockHit);
/*     */         });
/* 328 */     return (InteractionResult)result.get();
/*     */   }
/*     */   private InteractionResult performUseItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit) {
/*     */     InteractionResult success;
/* 332 */     BlockPos pos = blockHit.getBlockPos();
/* 333 */     ItemStack itemStack = player.getItemInHand(hand);
/* 334 */     if (this.localPlayerMode == GameType.SPECTATOR) {
/* 335 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/*     */ 
/*     */     
/* 339 */     boolean haveSomethingInOurHands = (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty());
/* 340 */     boolean suppressUsingBlock = (player.isSecondaryUseActive() && haveSomethingInOurHands);
/*     */     
/* 342 */     if (!suppressUsingBlock) {
/* 343 */       BlockState blockState = this.minecraft.level.getBlockState(pos);
/* 344 */       if (!this.connection.isFeatureEnabled(blockState.getBlock().requiredFeatures())) {
/* 345 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       }
/* 347 */       InteractionResult itemUse = blockState.useItemOn(player.getItemInHand(hand), this.minecraft.level, (Player)player, hand, blockHit);
/* 348 */       if (itemUse.consumesAction()) {
/* 349 */         return itemUse;
/*     */       }
/* 351 */       if (itemUse instanceof InteractionResult.TryEmptyHandInteraction && hand == InteractionHand.MAIN_HAND) {
/* 352 */         InteractionResult use = blockState.useWithoutItem(this.minecraft.level, (Player)player, blockHit);
/* 353 */         if (use.consumesAction()) {
/* 354 */           return use;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 359 */     if (itemStack.isEmpty() || player.getCooldowns().isOnCooldown(itemStack)) {
/* 360 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */ 
/*     */     
/* 364 */     UseOnContext context = new UseOnContext((Player)player, hand, blockHit);
/*     */     
/* 366 */     if (player.hasInfiniteMaterials()) {
/*     */       
/* 368 */       int count = itemStack.getCount();
/* 369 */       success = itemStack.useOn(context);
/* 370 */       itemStack.setCount(count);
/*     */     } else {
/* 372 */       success = itemStack.useOn(context);
/*     */     } 
/* 374 */     return success;
/*     */   }
/*     */   
/*     */   public InteractionResult useItem(Player player, InteractionHand hand) {
/* 378 */     if (this.localPlayerMode == GameType.SPECTATOR) {
/* 379 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 381 */     ensureHasSentCarriedItem();
/* 382 */     MutableObject<InteractionResult> interactionResult = new MutableObject();
/* 383 */     startPrediction(this.minecraft.level, sequence -> {
/*     */           ItemStack result;
/*     */           
/*     */           ServerboundUseItemPacket packet = new ServerboundUseItemPacket(hand, interactionResult, hand.getYRot(), hand.getXRot());
/*     */           
/*     */           ItemStack itemStack = hand.getItemInHand(hand);
/*     */           
/*     */           if (hand.getCooldowns().isOnCooldown(itemStack)) {
/*     */             hand.setValue(InteractionResult.PASS);
/*     */             
/*     */             return packet;
/*     */           } 
/*     */           InteractionResult resultHolder = itemStack.use(this.minecraft.level, hand, hand);
/*     */           if (resultHolder instanceof InteractionResult.Success) {
/*     */             InteractionResult.Success success = (InteractionResult.Success)resultHolder;
/*     */             result = Objects.<ItemStack>requireNonNullElseGet(success.heldItemTransformedTo(), ());
/*     */           } else {
/*     */             result = hand.getItemInHand(hand);
/*     */           } 
/*     */           if (result != itemStack) {
/*     */             hand.setItemInHand(hand, result);
/*     */           }
/*     */           hand.setValue(resultHolder);
/*     */           return packet;
/*     */         });
/* 408 */     return (InteractionResult)interactionResult.get();
/*     */   }
/*     */   
/*     */   public LocalPlayer createPlayer(ClientLevel level, StatsCounter stats, ClientRecipeBook recipeBook) {
/* 412 */     return createPlayer(level, stats, recipeBook, Input.EMPTY, false);
/*     */   }
/*     */   
/*     */   public LocalPlayer createPlayer(ClientLevel level, StatsCounter stats, ClientRecipeBook recipeBook, Input lastSentInput, boolean wasSprinting) {
/* 416 */     return new LocalPlayer(this.minecraft, level, this.connection, stats, recipeBook, lastSentInput, wasSprinting);
/*     */   }
/*     */   
/*     */   public void attack(Player player, Entity entity) {
/* 420 */     ensureHasSentCarriedItem();
/* 421 */     this.connection.send((Packet<?>)ServerboundInteractPacket.createAttackPacket(entity, player.isShiftKeyDown()));
/* 422 */     if (this.localPlayerMode != GameType.SPECTATOR) {
/* 423 */       player.attack(entity);
/* 424 */       player.resetAttackStrengthTicker();
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, Entity entity, InteractionHand hand) {
/* 429 */     ensureHasSentCarriedItem();
/* 430 */     this.connection.send((Packet<?>)ServerboundInteractPacket.createInteractionPacket(entity, player.isShiftKeyDown(), hand));
/* 431 */     if (this.localPlayerMode == GameType.SPECTATOR) {
/* 432 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 434 */     return player.interactOn(entity, hand);
/*     */   }
/*     */   
/*     */   public InteractionResult interactAt(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand) {
/* 438 */     ensureHasSentCarriedItem();
/* 439 */     Vec3 location = hitResult.getLocation().subtract(entity.getX(), entity.getY(), entity.getZ());
/* 440 */     this.connection.send((Packet<?>)ServerboundInteractPacket.createInteractionPacket(entity, player.isShiftKeyDown(), hand, location));
/* 441 */     if (this.localPlayerMode == GameType.SPECTATOR) {
/* 442 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 444 */     return entity.interactAt(player, location, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleInventoryMouseClick(int containerId, int slotNum, int buttonNum, ClickType clickType, Player player) {
/* 449 */     AbstractContainerMenu containerMenu = player.containerMenu;
/* 450 */     if (containerId != containerMenu.containerId) {
/* 451 */       LOGGER.warn("Ignoring click in mismatching container. Click in {}, player has {}.", containerId, containerMenu.containerId);
/*     */       return;
/*     */     } 
/* 454 */     NonNullList<Slot> slots = containerMenu.slots;
/* 455 */     int slotCount = slots.size();
/* 456 */     List<ItemStack> itemsBeforeClick = Lists.newArrayListWithCapacity(slotCount);
/* 457 */     for (Slot slot : slots) {
/* 458 */       itemsBeforeClick.add(slot.getItem().copy());
/*     */     }
/*     */     
/* 461 */     containerMenu.clicked(slotNum, buttonNum, clickType, player);
/*     */     
/* 463 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 464 */     for (int i = 0; i < slotCount; i++) {
/* 465 */       ItemStack before = itemsBeforeClick.get(i);
/* 466 */       ItemStack after = ((Slot)slots.get(i)).getItem();
/* 467 */       if (!ItemStack.matches(before, after)) {
/* 468 */         int2ObjectOpenHashMap.put(i, HashedStack.create(after, this.connection.decoratedHashOpsGenenerator()));
/*     */       }
/*     */     } 
/* 471 */     HashedStack carriedItem = HashedStack.create(containerMenu.getCarried(), this.connection.decoratedHashOpsGenenerator());
/*     */ 
/*     */     
/* 474 */     this.connection.send((Packet<?>)new ServerboundContainerClickPacket(containerId, containerMenu.getStateId(), Shorts.checkedCast(slotNum), SignedBytes.checkedCast(buttonNum), clickType, (Int2ObjectMap)int2ObjectOpenHashMap, carriedItem));
/*     */   }
/*     */   
/*     */   public void handlePlaceRecipe(int containerId, RecipeDisplayId recipe, boolean useMaxItems) {
/* 478 */     this.connection.send((Packet<?>)new ServerboundPlaceRecipePacket(containerId, recipe, useMaxItems));
/*     */   }
/*     */   
/*     */   public void handleInventoryButtonClick(int containerId, int buttonId) {
/* 482 */     this.connection.send((Packet<?>)new ServerboundContainerButtonClickPacket(containerId, buttonId));
/*     */   }
/*     */   
/*     */   public void handleCreativeModeItemAdd(ItemStack clicked, int slot) {
/* 486 */     if (this.minecraft.player.hasInfiniteMaterials() && this.connection.isFeatureEnabled(clicked.getItem().requiredFeatures())) {
/* 487 */       this.connection.send((Packet<?>)new ServerboundSetCreativeModeSlotPacket(slot, clicked));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCreativeModeItemDrop(ItemStack clicked) {
/* 493 */     boolean hasOtherInventoryOpen = (this.minecraft.screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen && !(this.minecraft.screen instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen));
/*     */     
/* 495 */     if (this.minecraft.player.hasInfiniteMaterials() && !hasOtherInventoryOpen && !clicked.isEmpty() && this.connection.isFeatureEnabled(clicked.getItem().requiredFeatures())) {
/* 496 */       this.connection.send((Packet<?>)new ServerboundSetCreativeModeSlotPacket(-1, clicked));
/* 497 */       this.minecraft.player.getDropSpamThrottler().increment();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseUsingItem(Player player) {
/* 502 */     ensureHasSentCarriedItem();
/* 503 */     this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
/* 504 */     player.releaseUsingItem();
/*     */   }
/*     */   
/*     */   public void piercingAttack(PiercingWeapon weapon) {
/* 508 */     ensureHasSentCarriedItem();
/* 509 */     this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STAB, BlockPos.ZERO, Direction.DOWN));
/* 510 */     this.minecraft.player.onAttack();
/* 511 */     this.minecraft.player.lungeForwardMaybe();
/* 512 */     weapon.makeSound((Entity)this.minecraft.player);
/*     */   }
/*     */   
/*     */   public boolean hasExperience() {
/* 516 */     return this.localPlayerMode.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean hasMissTime() {
/* 520 */     return !this.localPlayerMode.isCreative();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isServerControlledInventory() {
/* 529 */     return (this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof net.minecraft.world.entity.HasCustomInventoryScreen);
/*     */   }
/*     */   
/*     */   public boolean isSpectator() {
/* 533 */     return (this.localPlayerMode == GameType.SPECTATOR);
/*     */   }
/*     */   
/*     */   public GameType getPreviousPlayerMode() {
/* 537 */     return this.previousLocalPlayerMode;
/*     */   }
/*     */   
/*     */   public GameType getPlayerMode() {
/* 541 */     return this.localPlayerMode;
/*     */   }
/*     */   
/*     */   public boolean isDestroying() {
/* 545 */     return this.isDestroying;
/*     */   }
/*     */   
/*     */   public int getDestroyStage() {
/* 549 */     return (this.destroyProgress > 0.0F) ? (int)(this.destroyProgress * 10.0F) : -1;
/*     */   }
/*     */   
/*     */   public void handlePickItemFromBlock(BlockPos pos, boolean includeData) {
/* 553 */     this.connection.send((Packet<?>)new ServerboundPickItemFromBlockPacket(pos, includeData));
/*     */   }
/*     */   
/*     */   public void handlePickItemFromEntity(Entity entity, boolean includeData) {
/* 557 */     this.connection.send((Packet<?>)new ServerboundPickItemFromEntityPacket(entity.getId(), includeData));
/*     */   }
/*     */   
/*     */   public void handleSlotStateChanged(int slotId, int containerId, boolean newState) {
/* 561 */     this.connection.send((Packet<?>)new ServerboundContainerSlotStateChangedPacket(slotId, containerId, newState));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/MultiPlayerGameMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */