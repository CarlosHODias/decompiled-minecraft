/*    */ package net.minecraft.world.item;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class HoeItem extends Item {
/* 26 */   protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES = Maps.newHashMap((Map)ImmutableMap.of(Blocks.GRASS_BLOCK, 
/* 27 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT_PATH, 
/* 28 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT, 
/* 29 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.COARSE_DIRT, 
/* 30 */         Pair.of(HoeItem::onlyIfAirAbove, changeIntoState(Blocks.DIRT.defaultBlockState())), Blocks.ROOTED_DIRT, 
/* 31 */         Pair.of(context -> true, changeIntoStateAndDropItem(Blocks.DIRT.defaultBlockState(), Items.HANGING_ROOTS))));
/*    */ 
/*    */   
/*    */   public HoeItem(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline, Item.Properties properties) {
/* 35 */     super(properties.hoe(material, attackDamageBaseline, attackSpeedBaseline));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 40 */     Level level = context.getLevel();
/* 41 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 43 */     Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = TILLABLES.get(level.getBlockState(pos).getBlock());
/*    */     
/* 45 */     if (logicPair == null) {
/* 46 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */     
/* 49 */     Predicate<UseOnContext> predicate = (Predicate<UseOnContext>)logicPair.getFirst();
/* 50 */     Consumer<UseOnContext> action = (Consumer<UseOnContext>)logicPair.getSecond();
/*    */     
/* 52 */     if (predicate.test(context)) {
/* 53 */       Player player = context.getPlayer();
/* 54 */       level.playSound((Entity)player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */       
/* 56 */       if (!level.isClientSide()) {
/* 57 */         action.accept(context);
/* 58 */         if (player != null) {
/* 59 */           context.getItemInHand().hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot());
/*    */         }
/*    */       } 
/* 62 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 65 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static Consumer<UseOnContext> changeIntoState(BlockState state) {
/* 69 */     return context -> {
/*    */         context.getLevel().setBlock(context.getClickedPos(), state, 11);
/*    */         context.getLevel().gameEvent((Holder)GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of((Entity)context.getPlayer(), state));
/*    */       };
/*    */   }
/*    */   
/*    */   public static Consumer<UseOnContext> changeIntoStateAndDropItem(BlockState state, ItemLike item) {
/* 76 */     return context -> {
/*    */         context.getLevel().setBlock(context.getClickedPos(), state, 11);
/*    */         context.getLevel().gameEvent((Holder)GameEvent.BLOCK_CHANGE, context.getClickedPos(), GameEvent.Context.of((Entity)context.getPlayer(), state));
/*    */         Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(item));
/*    */       };
/*    */   }
/*    */   
/*    */   public static boolean onlyIfAirAbove(UseOnContext context) {
/* 84 */     return (context.getClickedFace() != Direction.DOWN && context.getLevel().getBlockState(context.getClickedPos().above()).isAir());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/HoeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */