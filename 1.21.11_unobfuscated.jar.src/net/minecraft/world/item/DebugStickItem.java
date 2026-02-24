/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.DebugStickState;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class DebugStickItem
/*     */   extends Item
/*     */ {
/*     */   public DebugStickItem(Item.Properties properties) {
/*  26 */     super(properties);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
/*  31 */     if (!level.isClientSide() && user instanceof Player) { Player player = (Player)user;
/*  32 */       handleInteraction(player, state, (LevelAccessor)level, pos, false, itemStack); }
/*     */ 
/*     */     
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  40 */     Player player = context.getPlayer();
/*  41 */     Level level = context.getLevel();
/*     */     
/*  43 */     if (!level.isClientSide() && player != null) {
/*  44 */       BlockPos pos = context.getClickedPos();
/*  45 */       if (!handleInteraction(player, level.getBlockState(pos), (LevelAccessor)level, pos, true, context.getItemInHand())) {
/*  46 */         return (InteractionResult)InteractionResult.FAIL;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private boolean handleInteraction(Player player, BlockState state, LevelAccessor level, BlockPos pos, boolean cycle, ItemStack itemStackInHand) {
/*  54 */     if (!player.canUseGameMasterBlocks()) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     Holder<Block> block = state.getBlockHolder();
/*  59 */     StateDefinition<Block, BlockState> definition = ((Block)block.value()).getStateDefinition();
/*  60 */     Collection<Property<?>> properties = definition.getProperties();
/*     */     
/*  62 */     if (properties.isEmpty()) {
/*  63 */       message(player, (Component)Component.translatable(this.descriptionId + ".empty", new Object[] { block.getRegisteredName() }));
/*  64 */       return false;
/*     */     } 
/*     */     
/*  67 */     DebugStickState debugStickState = (DebugStickState)itemStackInHand.get(DataComponents.DEBUG_STICK_STATE);
/*  68 */     if (debugStickState == null) {
/*  69 */       return false;
/*     */     }
/*     */     
/*  72 */     Property<?> property = (Property)debugStickState.properties().get(block);
/*  73 */     if (cycle) {
/*  74 */       if (property == null) {
/*  75 */         property = properties.iterator().next();
/*     */       }
/*     */       
/*  78 */       BlockState newState = cycleState(state, property, player.isSecondaryUseActive());
/*  79 */       level.setBlock(pos, newState, 18);
/*  80 */       message(player, (Component)Component.translatable(this.descriptionId + ".update", new Object[] { property.getName(), getNameHelper(newState, property) }));
/*     */     } else {
/*  82 */       property = getRelative((Iterable)properties, property, player.isSecondaryUseActive());
/*  83 */       itemStackInHand.set(DataComponents.DEBUG_STICK_STATE, debugStickState.withProperty(block, property));
/*  84 */       message(player, (Component)Component.translatable(this.descriptionId + ".select", new Object[] { property.getName(), getNameHelper(state, property) }));
/*     */     } 
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> BlockState cycleState(BlockState state, Property<T> property, boolean backward) {
/*  90 */     return (BlockState)state.setValue(property, getRelative(property.getPossibleValues(), state.getValue(property), backward));
/*     */   }
/*     */   
/*     */   private static <T> T getRelative(Iterable<T> collection, T current, boolean backward) {
/*  94 */     return backward ? (T)Util.findPreviousInIterable(collection, current) : (T)Util.findNextInIterable(collection, current);
/*     */   }
/*     */   
/*     */   private static void message(Player player, Component message) {
/*  98 */     ((ServerPlayer)player).sendSystemMessage(message, true);
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> String getNameHelper(BlockState state, Property<T> property) {
/* 102 */     return property.getName(state.getValue(property));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/DebugStickItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */