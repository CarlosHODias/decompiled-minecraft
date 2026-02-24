/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class GeodeBlockSettings
/*    */ {
/*    */   public final BlockStateProvider fillingProvider;
/*    */   public final BlockStateProvider innerLayerProvider;
/*    */   public final BlockStateProvider alternateInnerLayerProvider;
/*    */   public final BlockStateProvider middleLayerProvider;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("filling_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(()), (App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter(()), (App)TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(())).apply((Applicative)i, GeodeBlockSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public final BlockStateProvider outerLayerProvider;
/*    */   
/*    */   public final List<BlockState> innerPlacements;
/*    */   
/*    */   public final TagKey<Block> cannotReplace;
/*    */   public final TagKey<Block> invalidBlocks;
/*    */   public static final Codec<GeodeBlockSettings> CODEC;
/*    */   
/*    */   public GeodeBlockSettings(BlockStateProvider fillingProvider, BlockStateProvider innerLayerProvider, BlockStateProvider alternateInnerLayerProvider, BlockStateProvider middleLayerProvider, BlockStateProvider outerLayerProvider, List<BlockState> innerPlacements, TagKey<Block> cannotReplace, TagKey<Block> invalidBlocks) {
/* 37 */     this.fillingProvider = fillingProvider;
/* 38 */     this.innerLayerProvider = innerLayerProvider;
/* 39 */     this.alternateInnerLayerProvider = alternateInnerLayerProvider;
/* 40 */     this.middleLayerProvider = middleLayerProvider;
/* 41 */     this.outerLayerProvider = outerLayerProvider;
/* 42 */     this.innerPlacements = innerPlacements;
/* 43 */     this.cannotReplace = cannotReplace;
/* 44 */     this.invalidBlocks = invalidBlocks;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/GeodeBlockSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */