/*     */ package net.minecraft.world.item;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class AxeItem extends Item {
/*  32 */   protected static final Map<Block, Block> STRIPPABLES = (Map<Block, Block>)new ImmutableMap.Builder()
/*  33 */     .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
/*  34 */     .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
/*  35 */     .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
/*  36 */     .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
/*  37 */     .put(Blocks.PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_WOOD)
/*  38 */     .put(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG)
/*  39 */     .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
/*  40 */     .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
/*  41 */     .put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
/*  42 */     .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
/*  43 */     .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
/*  44 */     .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
/*  45 */     .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
/*  46 */     .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
/*  47 */     .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
/*  48 */     .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
/*  49 */     .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
/*  50 */     .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
/*  51 */     .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
/*  52 */     .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
/*  53 */     .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
/*  54 */     .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
/*  55 */     .put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
/*  56 */     .build();
/*     */   
/*     */   public AxeItem(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline, Item.Properties properties) {
/*  59 */     super(properties.axe(material, attackDamageBaseline, attackSpeedBaseline));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  64 */     Level level = context.getLevel();
/*  65 */     BlockPos pos = context.getClickedPos();
/*  66 */     Player player = context.getPlayer();
/*     */     
/*  68 */     if (playerHasBlockingItemUseIntent(context))
/*     */     {
/*  70 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  73 */     Optional<BlockState> newBlock = evaluateNewBlockState(level, pos, player, level.getBlockState(pos));
/*  74 */     if (newBlock.isEmpty()) {
/*  75 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/*  78 */     ItemStack itemInHand = context.getItemInHand();
/*     */     
/*  80 */     if (player instanceof ServerPlayer) {
/*  81 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, itemInHand);
/*     */     }
/*     */     
/*  84 */     level.setBlock(pos, newBlock.get(), 11);
/*  85 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, newBlock.get()));
/*     */     
/*  87 */     if (player != null) {
/*  88 */       itemInHand.hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
/*     */     }
/*     */     
/*  91 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean playerHasBlockingItemUseIntent(UseOnContext context) {
/*  95 */     Player player = context.getPlayer();
/*  96 */     return (context.getHand().equals(InteractionHand.MAIN_HAND) && 
/*  97 */       player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && 
/*  98 */       !player.isSecondaryUseActive());
/*     */   }
/*     */   
/*     */   private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, Player player, BlockState oldState) {
/* 102 */     Optional<BlockState> strippedBlock = getStripped(oldState);
/* 103 */     if (strippedBlock.isPresent()) {
/* 104 */       level.playSound((Entity)player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 105 */       return strippedBlock;
/*     */     } 
/*     */     
/* 108 */     Optional<BlockState> scrapedBlock = WeatheringCopper.getPrevious(oldState);
/* 109 */     if (scrapedBlock.isPresent()) {
/* 110 */       spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_SCRAPE, 3005);
/* 111 */       return scrapedBlock;
/*     */     } 
/*     */     
/* 114 */     Optional<BlockState> waxoffBlock = Optional.<Block>ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(oldState.getBlock())).map(b -> b.withPropertiesOf(oldState));
/* 115 */     if (waxoffBlock.isPresent()) {
/* 116 */       spawnSoundAndParticle(level, pos, player, oldState, SoundEvents.AXE_WAX_OFF, 3004);
/* 117 */       return waxoffBlock;
/*     */     } 
/*     */     
/* 120 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void spawnSoundAndParticle(Level level, BlockPos pos, Player player, BlockState oldState, SoundEvent soundEvent, int particle) {
/* 124 */     level.playSound((Entity)player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 125 */     level.levelEvent((Entity)player, particle, pos, 0);
/*     */     
/* 127 */     if (oldState.getBlock() instanceof ChestBlock && oldState.getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE) {
/* 128 */       BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
/* 129 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of((Entity)player, level.getBlockState(neighborPos)));
/* 130 */       level.levelEvent((Entity)player, particle, neighborPos, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Optional<BlockState> getStripped(BlockState state) {
/* 135 */     return Optional.<Block>ofNullable(STRIPPABLES.get(state.getBlock())).map(block -> (BlockState)block.defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, state.getValue((Property)RotatedPillarBlock.AXIS)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/AxeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */