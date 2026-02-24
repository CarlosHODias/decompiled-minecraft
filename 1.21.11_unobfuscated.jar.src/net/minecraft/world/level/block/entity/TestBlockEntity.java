/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TestBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TestBlockEntity extends BlockEntity {
/*  19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_MESSAGE = "";
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   private TestBlockMode mode;
/*  24 */   private String message = "";
/*     */   private boolean powered = false;
/*     */   private boolean triggered;
/*     */   
/*     */   public TestBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  29 */     super(BlockEntityType.TEST_BLOCK, worldPosition, blockState);
/*  30 */     this.mode = (TestBlockMode)blockState.getValue((Property)TestBlock.MODE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  35 */     output.store("mode", TestBlockMode.CODEC, this.mode);
/*  36 */     output.putString("message", this.message);
/*  37 */     output.putBoolean("powered", this.powered);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  42 */     this.mode = input.read("mode", TestBlockMode.CODEC).orElse(TestBlockMode.FAIL);
/*  43 */     this.message = input.getStringOr("message", "");
/*  44 */     this.powered = input.getBooleanOr("powered", false);
/*     */   }
/*     */   
/*     */   private void updateBlockState() {
/*  48 */     if (this.level == null) {
/*     */       return;
/*     */     }
/*  51 */     BlockPos pos = getBlockPos();
/*  52 */     BlockState blockState = this.level.getBlockState(pos);
/*  53 */     if (blockState.is(Blocks.TEST_BLOCK)) {
/*  54 */       this.level.setBlock(pos, (BlockState)blockState.setValue((Property)TestBlock.MODE, (Comparable)this.mode), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  60 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  65 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/*  69 */     return this.powered;
/*     */   }
/*     */   
/*     */   public void setPowered(boolean powered) {
/*  73 */     this.powered = powered;
/*     */   }
/*     */   
/*     */   public TestBlockMode getMode() {
/*  77 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void setMode(TestBlockMode mode) {
/*  81 */     this.mode = mode;
/*  82 */     updateBlockState();
/*     */   }
/*     */   
/*     */   private Block getBlockType() {
/*  86 */     return getBlockState().getBlock();
/*     */   }
/*     */   
/*     */   public void reset() {
/*  90 */     this.triggered = false;
/*  91 */     if (this.mode == TestBlockMode.START && this.level != null) {
/*  92 */       setPowered(false);
/*  93 */       this.level.updateNeighborsAt(getBlockPos(), getBlockType());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void trigger() {
/*  98 */     if (this.mode == TestBlockMode.START && this.level != null) {
/*  99 */       setPowered(true);
/* 100 */       BlockPos pos = getBlockPos();
/* 101 */       this.level.updateNeighborsAt(pos, getBlockType());
/* 102 */       this.level.getBlockTicks().willTickThisTick(pos, getBlockType());
/* 103 */       log(); return;
/*     */     } 
/* 105 */     if (this.mode == TestBlockMode.LOG) {
/* 106 */       log();
/*     */     }
/* 108 */     this.triggered = true;
/*     */   }
/*     */   
/*     */   public void log() {
/* 112 */     if (!this.message.isBlank()) {
/* 113 */       LOGGER.info("Test {} (at {}): {}", new Object[] { this.mode.getSerializedName(), getBlockPos(), this.message });
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasTriggered() {
/* 118 */     return this.triggered;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 122 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 126 */     this.message = message;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/TestBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */