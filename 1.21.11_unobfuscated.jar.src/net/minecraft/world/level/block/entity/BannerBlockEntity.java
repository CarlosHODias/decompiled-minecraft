/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*     */ import net.minecraft.world.level.block.BannerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BannerBlockEntity extends BlockEntity implements Nameable {
/*     */   public static final int MAX_PATTERNS = 6;
/*     */   private static final String TAG_PATTERNS = "patterns";
/*  26 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("block.minecraft.banner");
/*     */   
/*     */   private Component name;
/*     */   private final DyeColor baseColor;
/*  30 */   private BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
/*     */   
/*     */   public BannerBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  33 */     this(worldPosition, blockState, ((AbstractBannerBlock)blockState.getBlock()).getColor());
/*     */   }
/*     */   
/*     */   public BannerBlockEntity(BlockPos worldPosition, BlockState blockState, DyeColor color) {
/*  37 */     super(BlockEntityType.BANNER, worldPosition, blockState);
/*  38 */     this.baseColor = color;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  43 */     if (this.name != null) {
/*  44 */       return this.name;
/*     */     }
/*  46 */     return DEFAULT_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getCustomName() {
/*  51 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  56 */     super.saveAdditional(output);
/*     */     
/*  58 */     if (!this.patterns.equals(BannerPatternLayers.EMPTY)) {
/*  59 */       output.store("patterns", BannerPatternLayers.CODEC, this.patterns);
/*     */     }
/*     */     
/*  62 */     output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  67 */     super.loadAdditional(input);
/*     */     
/*  69 */     this.name = parseCustomNameSafe(input, "CustomName");
/*     */     
/*  71 */     this.patterns = input.read("patterns", BannerPatternLayers.CODEC).orElse(BannerPatternLayers.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  76 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  82 */     return saveWithoutMetadata(registries);
/*     */   }
/*     */   
/*     */   public BannerPatternLayers getPatterns() {
/*  86 */     return this.patterns;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  90 */     ItemStack itemStack = new ItemStack((ItemLike)BannerBlock.byColor(this.baseColor));
/*  91 */     itemStack.applyComponents(collectComponents());
/*  92 */     return itemStack;
/*     */   }
/*     */   
/*     */   public DyeColor getBaseColor() {
/*  96 */     return this.baseColor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 101 */     super.applyImplicitComponents(components);
/* 102 */     this.patterns = (BannerPatternLayers)components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/* 103 */     this.name = (Component)components.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 108 */     super.collectImplicitComponents(components);
/* 109 */     components.set(DataComponents.BANNER_PATTERNS, this.patterns);
/* 110 */     components.set(DataComponents.CUSTOM_NAME, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 115 */     output.discard("patterns");
/* 116 */     output.discard("CustomName");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BannerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */