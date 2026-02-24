/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleType;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TorchBlock extends BaseTorchBlock {
/*    */   static {
/* 20 */     PARTICLE_OPTIONS_FIELD = net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap(type -> { SimpleParticleType simple = (SimpleParticleType)type; return (type instanceof SimpleParticleType) ? DataResult.success(simple) : DataResult.error(()); }, type -> type).fieldOf("particle_options");
/*    */     
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PARTICLE_OPTIONS_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, TorchBlock::new));
/*    */   }
/*    */   protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD;
/*    */   public static final MapCodec<TorchBlock> CODEC;
/*    */   protected final SimpleParticleType flameParticle;
/*    */   
/*    */   public MapCodec<? extends TorchBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/* 36 */     this.flameParticle = flameParticle;
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 41 */     double x = pos.getX() + 0.5D;
/* 42 */     double y = pos.getY() + 0.7D;
/* 43 */     double z = pos.getZ() + 0.5D;
/* 44 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
/* 45 */     level.addParticle((ParticleOptions)this.flameParticle, x, y, z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */