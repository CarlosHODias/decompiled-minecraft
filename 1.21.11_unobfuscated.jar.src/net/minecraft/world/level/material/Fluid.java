/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public abstract class Fluid
/*     */ {
/*  29 */   public static final IdMapper<FluidState> FLUID_STATE_REGISTRY = new IdMapper();
/*     */   
/*     */   protected final StateDefinition<Fluid, FluidState> stateDefinition;
/*     */   private FluidState defaultFluidState;
/*  33 */   private final Holder.Reference<Fluid> builtInRegistryHolder = BuiltInRegistries.FLUID.createIntrusiveHolder(this);
/*     */   
/*     */   protected Fluid() {
/*  36 */     StateDefinition.Builder<Fluid, FluidState> builder = new StateDefinition.Builder(this);
/*  37 */     createFluidStateDefinition(builder);
/*  38 */     this.stateDefinition = builder.create(Fluid::defaultFluidState, FluidState::new);
/*  39 */     registerDefaultState((FluidState)this.stateDefinition.any());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {}
/*     */   
/*     */   public StateDefinition<Fluid, FluidState> getStateDefinition() {
/*  46 */     return this.stateDefinition;
/*     */   }
/*     */   
/*     */   protected final void registerDefaultState(FluidState state) {
/*  50 */     this.defaultFluidState = state;
/*     */   }
/*     */   
/*     */   public final FluidState defaultFluidState() {
/*  54 */     return this.defaultFluidState;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Item getBucket();
/*     */ 
/*     */   
/*     */   protected void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {}
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState) {}
/*     */ 
/*     */   
/*     */   protected void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {}
/*     */   
/*     */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {}
/*     */   
/*     */   protected ParticleOptions getDripParticle() {
/*  72 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean canBeReplacedWith(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Fluid paramFluid, Direction paramDirection);
/*     */   
/*     */   protected abstract Vec3 getFlow(BlockGetter paramBlockGetter, BlockPos paramBlockPos, FluidState paramFluidState);
/*     */   
/*     */   public abstract int getTickDelay(LevelReader paramLevelReader);
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isEmpty() {
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract float getExplosionResistance();
/*     */   
/*     */   public abstract float getHeight(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*     */   
/*     */   public abstract float getOwnHeight(FluidState paramFluidState);
/*     */   
/*     */   protected abstract BlockState createLegacyBlock(FluidState paramFluidState);
/*     */   
/*     */   public abstract boolean isSource(FluidState paramFluidState);
/*     */   
/*     */   public abstract int getAmount(FluidState paramFluidState);
/*     */   
/*     */   public boolean isSame(Fluid other) {
/* 102 */     return (other == this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean is(TagKey<Fluid> tag) {
/* 110 */     return this.builtInRegistryHolder.is(tag);
/*     */   }
/*     */   
/*     */   public abstract VoxelShape getShape(FluidState paramFluidState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*     */   
/*     */   public AABB getAABB(FluidState state, BlockGetter level, BlockPos pos) {
/* 116 */     if (isEmpty()) {
/* 117 */       return null;
/*     */     }
/* 119 */     float height = state.getHeight(level, pos);
/* 120 */     return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, (pos.getY() + height), pos.getZ() + 1.0D);
/*     */   }
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 124 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<Fluid> builtInRegistryHolder() {
/* 132 */     return this.builtInRegistryHolder;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/Fluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */