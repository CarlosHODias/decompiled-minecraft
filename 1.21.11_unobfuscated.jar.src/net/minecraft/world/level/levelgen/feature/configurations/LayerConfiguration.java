/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class LayerConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, net.minecraft.world.level.dimension.DimensionType.Y_SIZE).fieldOf("height").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, LayerConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<LayerConfiguration> CODEC;
/*    */   public final int height;
/*    */   public final BlockState state;
/*    */   
/*    */   public LayerConfiguration(int height, BlockState state) {
/* 18 */     this.height = height;
/* 19 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/LayerConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */