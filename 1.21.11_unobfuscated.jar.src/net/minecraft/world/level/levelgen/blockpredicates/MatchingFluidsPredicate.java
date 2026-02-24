/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class MatchingFluidsPredicate extends StateTestingPredicate {
/*    */   private final HolderSet<Fluid> fluids;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MatchingFluidsPredicate::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<MatchingFluidsPredicate> CODEC;
/*    */   
/*    */   public MatchingFluidsPredicate(Vec3i offset, HolderSet<Fluid> fluids) {
/* 20 */     super(offset);
/* 21 */     this.fluids = fluids;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 26 */     return state.getFluidState().is(this.fluids);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_FLUIDS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/MatchingFluidsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */