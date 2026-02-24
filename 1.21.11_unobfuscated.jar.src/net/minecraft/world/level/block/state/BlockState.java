/*    */ package net.minecraft.world.level.block.state;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ public class BlockState
/*    */   extends BlockBehaviour.BlockStateBase
/*    */ {
/* 14 */   public static final Codec<BlockState> CODEC = codec(BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState).stable();
/*    */   
/*    */   public BlockState(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
/* 17 */     super(owner, values, propertiesCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState asState() {
/* 22 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/BlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */