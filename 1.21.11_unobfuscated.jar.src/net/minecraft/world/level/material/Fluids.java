/*    */ package net.minecraft.world.level.material;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class Fluids {
/*  7 */   public static final Fluid EMPTY = register("empty", new EmptyFluid());
/*  8 */   public static final FlowingFluid FLOWING_WATER = register("flowing_water", new WaterFluid.Flowing());
/*  9 */   public static final FlowingFluid WATER = register("water", new WaterFluid.Source());
/* 10 */   public static final FlowingFluid FLOWING_LAVA = register("flowing_lava", new LavaFluid.Flowing());
/* 11 */   public static final FlowingFluid LAVA = register("lava", new LavaFluid.Source());
/*    */   
/*    */   private static <T extends Fluid> T register(String name, T fluid) {
/* 14 */     return (T)Registry.register((Registry)BuiltInRegistries.FLUID, name, fluid);
/*    */   }
/*    */   
/*    */   static {
/* 18 */     for (Fluid fluid : (Iterable<Fluid>)BuiltInRegistries.FLUID) {
/* 19 */       for (UnmodifiableIterator<FluidState> unmodifiableIterator = fluid.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { FluidState state = unmodifiableIterator.next();
/* 20 */         Fluid.FLUID_STATE_REGISTRY.add(state); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/Fluids.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */