/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.JukeboxSongPlayer;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.JukeboxBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ public class JukeboxBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
/*     */   public static final String SONG_ITEM_TAG_ID = "RecordItem";
/*  29 */   private ItemStack item = ItemStack.EMPTY; public static final String TICKS_SINCE_SONG_STARTED_TAG_ID = "ticks_since_song_started";
/*  30 */   private final JukeboxSongPlayer jukeboxSongPlayer = new JukeboxSongPlayer(this::onSongChanged, getBlockPos());
/*     */   
/*     */   public JukeboxBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  33 */     super(BlockEntityType.JUKEBOX, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public JukeboxSongPlayer getSongPlayer() {
/*  37 */     return this.jukeboxSongPlayer;
/*     */   }
/*     */   
/*     */   public void onSongChanged() {
/*  41 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/*  42 */     setChanged();
/*     */   }
/*     */   
/*     */   private void notifyItemChangedInJukebox(boolean wasInserted) {
/*  46 */     if (this.level == null || this.level.getBlockState(getBlockPos()) != getBlockState()) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     this.level.setBlock(getBlockPos(), (BlockState)getBlockState().setValue((Property)JukeboxBlock.HAS_RECORD, wasInserted), 2);
/*  51 */     this.level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
/*     */   }
/*     */   
/*     */   public void popOutTheItem() {
/*  55 */     if (this.level == null || this.level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     BlockPos pos = getBlockPos();
/*  60 */     ItemStack itemBeforePoppingOut = getTheItem();
/*  61 */     if (itemBeforePoppingOut.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  65 */     removeTheItem();
/*     */     
/*  67 */     Vec3 itemPos = Vec3.atLowerCornerWithOffset((Vec3i)pos, 0.5D, 1.01D, 0.5D).offsetRandomXZ(this.level.random, 0.7F);
/*  68 */     ItemStack itemStack = itemBeforePoppingOut.copy();
/*     */     
/*  70 */     ItemEntity entity = new ItemEntity(this.level, itemPos.x(), itemPos.y(), itemPos.z(), itemStack);
/*  71 */     entity.setDefaultPickUpDelay();
/*  72 */     this.level.addFreshEntity((Entity)entity);
/*  73 */     onSongChanged();
/*     */   }
/*     */   
/*     */   public static void tick(Level level, BlockPos blockPos, BlockState blockState, JukeboxBlockEntity jukebox) {
/*  77 */     jukebox.jukeboxSongPlayer.tick((LevelAccessor)level, blockState);
/*     */   }
/*     */   
/*     */   public int getComparatorOutput() {
/*  81 */     return (Integer)JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), this.item).map(Holder::value).map(JukeboxSong::comparatorOutput).orElse(0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  86 */     super.loadAdditional(input);
/*     */     
/*  88 */     ItemStack newItem = input.read("RecordItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*  89 */     if (!this.item.isEmpty() && !ItemStack.isSameItemSameComponents(newItem, this.item)) {
/*  90 */       this.jukeboxSongPlayer.stop((LevelAccessor)this.level, getBlockState());
/*     */     }
/*  92 */     this.item = newItem;
/*     */     
/*  94 */     input.getLong("ticks_since_song_started").ifPresent(ticksSinceSongStarted -> JukeboxSong.fromStack(input.lookup(), this.item).ifPresent(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 101 */     super.saveAdditional(output);
/*     */     
/* 103 */     if (!getTheItem().isEmpty()) {
/* 104 */       output.store("RecordItem", ItemStack.CODEC, getTheItem());
/*     */     }
/*     */     
/* 107 */     if (this.jukeboxSongPlayer.getSong() != null) {
/* 108 */       output.putLong("ticks_since_song_started", this.jukeboxSongPlayer.getTicksSinceSongStarted());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getTheItem() {
/* 114 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack splitTheItem(int count) {
/* 119 */     ItemStack retrievedItem = this.item;
/* 120 */     setTheItem(ItemStack.EMPTY);
/* 121 */     return retrievedItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTheItem(ItemStack itemStack) {
/* 126 */     this.item = itemStack;
/*     */     
/* 128 */     boolean itemWasInserted = !this.item.isEmpty();
/* 129 */     Optional<Holder<JukeboxSong>> maybeSong = JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), this.item);
/*     */     
/* 131 */     notifyItemChangedInJukebox(itemWasInserted);
/* 132 */     if (itemWasInserted && maybeSong.isPresent()) {
/* 133 */       this.jukeboxSongPlayer.play((LevelAccessor)this.level, maybeSong.get());
/*     */     } else {
/* 135 */       this.jukeboxSongPlayer.stop((LevelAccessor)this.level, getBlockState());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemoved() {
/* 141 */     super.setRemoved();
/* 142 */     this.level.gameEvent((Holder)GameEvent.JUKEBOX_STOP_PLAY, getBlockPos(), GameEvent.Context.of(getBlockState()));
/* 143 */     this.level.levelEvent(1011, getBlockPos(), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStackSize() {
/* 148 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getContainerBlockEntity() {
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 158 */     return (itemStack.has(DataComponents.JUKEBOX_PLAYABLE) && getItem(slot).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(Container into, int slot, ItemStack itemStack) {
/* 163 */     return into.hasAnyMatching(ItemStack::isEmpty);
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 168 */     popOutTheItem();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSongItemWithoutPlaying(ItemStack itemStack) {
/* 173 */     this.item = itemStack;
/* 174 */     JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), itemStack).ifPresent(song -> this.jukeboxSongPlayer.setSongWithoutPlaying(song, 0L));
/* 175 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/* 176 */     setChanged();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void tryForcePlaySong() {
/* 181 */     JukeboxSong.fromStack((HolderLookup.Provider)this.level.registryAccess(), getTheItem()).ifPresent(song -> this.jukeboxSongPlayer.play((LevelAccessor)this.level, song));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/JukeboxBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */