/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class NoteBlock extends Block {
/*  37 */   public static final MapCodec<NoteBlock> CODEC = simpleCodec(NoteBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<NoteBlock> codec() {
/*  41 */     return CODEC;
/*     */   }
/*     */   
/*  44 */   public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
/*  45 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  46 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty NOTE = BlockStateProperties.NOTE;
/*     */   public static final int NOTE_VOLUME = 3;
/*     */   
/*     */   public NoteBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)INSTRUMENT, (Comparable)NoteBlockInstrument.HARP)).setValue((Property)NOTE, 0)).setValue((Property)POWERED, false));
/*     */   }
/*     */   
/*     */   private BlockState setInstrument(LevelReader level, BlockPos position, BlockState state) {
/*  55 */     NoteBlockInstrument instrumentAbove = level.getBlockState(position.above()).instrument();
/*  56 */     if (instrumentAbove.worksAboveNoteBlock()) {
/*  57 */       return (BlockState)state.setValue((Property)INSTRUMENT, (Comparable)instrumentAbove);
/*     */     }
/*     */     
/*  60 */     NoteBlockInstrument instrumentBelow = level.getBlockState(position.below()).instrument();
/*  61 */     NoteBlockInstrument newBelow = instrumentBelow.worksAboveNoteBlock() ? NoteBlockInstrument.HARP : instrumentBelow;
/*  62 */     return (BlockState)state.setValue((Property)INSTRUMENT, (Comparable)newBelow);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  67 */     return setInstrument((LevelReader)context.getLevel(), context.getClickedPos(), defaultBlockState());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  72 */     boolean neighborDirectionSetsInstrument = (directionToNeighbour.getAxis() == Direction.Axis.Y);
/*     */     
/*  74 */     if (neighborDirectionSetsInstrument) {
/*  75 */       return setInstrument(level, pos, state);
/*     */     }
/*  77 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
/*  82 */     boolean signal = level.hasNeighborSignal(pos);
/*     */     
/*  84 */     if (signal != (Boolean)state.getValue((Property)POWERED)) {
/*  85 */       if (signal) {
/*  86 */         playNote(null, state, level, pos);
/*     */       }
/*  88 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, signal), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playNote(Entity source, BlockState state, Level level, BlockPos pos) {
/*  93 */     if (((NoteBlockInstrument)state.getValue((Property)INSTRUMENT)).worksAboveNoteBlock() || level.getBlockState(pos.above()).isAir()) {
/*  94 */       level.blockEvent(pos, this, 0, 0);
/*  95 */       level.gameEvent(source, (Holder)net.minecraft.world.level.gameevent.GameEvent.NOTE_BLOCK_PLAY, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 101 */     if (itemStack.is(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS) && 
/* 102 */       hitResult.getDirection() == Direction.UP)
/*     */     {
/* 104 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 107 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 112 */     if (!level.isClientSide()) {
/* 113 */       state = (BlockState)state.cycle((Property)NOTE);
/* 114 */       level.setBlock(pos, state, 3);
/* 115 */       playNote((Entity)player, state, level, pos);
/* 116 */       player.awardStat(Stats.TUNE_NOTEBLOCK);
/*     */     } 
/* 118 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
/* 123 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     playNote((Entity)player, state, level, pos);
/* 128 */     player.awardStat(Stats.PLAY_NOTEBLOCK);
/*     */   }
/*     */   
/*     */   public static float getPitchFromNote(int twoOctaveRangeNote) {
/* 132 */     return (float)Math.pow(2.0D, (twoOctaveRangeNote - 12) / 12.0D);
/*     */   }
/*     */   
/*     */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/*     */     float pitch;
/*     */     Holder<SoundEvent> soundEvent;
/* 138 */     NoteBlockInstrument instrument = (NoteBlockInstrument)state.getValue((Property)INSTRUMENT);
/* 139 */     if (instrument.isTunable()) {
/* 140 */       int note = (Integer)state.getValue((Property)NOTE);
/* 141 */       pitch = getPitchFromNote(note);
/* 142 */       level.addParticle((ParticleOptions)ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, note / 24.0D, 0.0D, 0.0D);
/*     */     } else {
/* 144 */       pitch = 1.0F;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (instrument.hasCustomSound()) {
/* 149 */       Identifier soundId = getCustomSoundId(level, pos);
/* 150 */       if (soundId == null) {
/* 151 */         return false;
/*     */       }
/* 153 */       soundEvent = Holder.direct(SoundEvent.createVariableRangeEvent(soundId));
/*     */     } else {
/* 155 */       soundEvent = instrument.getSoundEvent();
/*     */     } 
/* 157 */     level.playSeededSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.RECORDS, 3.0F, pitch, level.random.nextLong());
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   private Identifier getCustomSoundId(Level level, BlockPos pos) {
/* 162 */     BlockEntity blockEntity = level.getBlockEntity(pos.above()); if (blockEntity instanceof SkullBlockEntity) { SkullBlockEntity head = (SkullBlockEntity)blockEntity;
/* 163 */       return head.getNoteBlockSound(); }
/*     */     
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 170 */     builder.add(new Property[] { (Property)INSTRUMENT, (Property)POWERED, (Property)NOTE });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/NoteBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */