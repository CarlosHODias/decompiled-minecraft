/*    */ package net.minecraft.client.resources.model;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockStateDefinitions {
/* 16 */   private static final StateDefinition<Block, BlockState> ITEM_FRAME_FAKE_DEFINITION = createItemFrameFakeState();
/* 17 */   private static final StateDefinition<Block, BlockState> GLOW_ITEM_FRAME_FAKE_DEFINITION = createItemFrameFakeState();
/*    */   
/* 19 */   private static final Identifier GLOW_ITEM_FRAME_LOCATION = Identifier.withDefaultNamespace("glow_item_frame");
/* 20 */   private static final Identifier ITEM_FRAME_LOCATION = Identifier.withDefaultNamespace("item_frame");
/*    */   
/* 22 */   private static final Map<Identifier, StateDefinition<Block, BlockState>> STATIC_DEFINITIONS = Map.of(ITEM_FRAME_LOCATION, ITEM_FRAME_FAKE_DEFINITION, GLOW_ITEM_FRAME_LOCATION, GLOW_ITEM_FRAME_FAKE_DEFINITION);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static StateDefinition<Block, BlockState> createItemFrameFakeState() {
/* 28 */     return new StateDefinition.Builder(Blocks.AIR).add(new Property[] { (Property)BlockStateProperties.MAP }).create(Block::defaultBlockState, BlockState::new);
/*    */   }
/*    */   
/*    */   public static BlockState getItemFrameFakeState(boolean isGlowing, boolean map) {
/* 32 */     return (BlockState)((BlockState)(isGlowing ? GLOW_ITEM_FRAME_FAKE_DEFINITION : ITEM_FRAME_FAKE_DEFINITION).any()).setValue((Property)BlockStateProperties.MAP, map);
/*    */   }
/*    */   
/*    */   static Function<Identifier, StateDefinition<Block, BlockState>> definitionLocationToBlockStateMapper() {
/* 36 */     Map<Identifier, StateDefinition<Block, BlockState>> result = new HashMap<>(STATIC_DEFINITIONS);
/* 37 */     for (Block block : (Iterable<Block>)BuiltInRegistries.BLOCK) {
/* 38 */       result.put(block.builtInRegistryHolder().key().identifier(), block.getStateDefinition());
/*    */     }
/* 40 */     Objects.requireNonNull(result); return result::get;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/BlockStateDefinitions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */