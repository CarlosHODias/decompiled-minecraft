/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public final class FluidState
/*     */   extends StateHolder<Fluid, FluidState>
/*     */ {
/*  30 */   public static final Codec<FluidState> CODEC = codec(BuiltInRegistries.FLUID.byNameCodec(), Fluid::defaultFluidState).stable();
/*     */   public static final int AMOUNT_MAX = 9;
/*     */   public static final int AMOUNT_FULL = 8;
/*     */   
/*     */   public FluidState(Fluid owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<FluidState> propertiesCodec) {
/*  35 */     super(owner, values, propertiesCodec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fluid getType() {
/*  42 */     return (Fluid)this.owner;
/*     */   }
/*     */   
/*     */   public boolean isSource() {
/*  46 */     return getType().isSource(this);
/*     */   }
/*     */   
/*     */   public boolean isSourceOfType(Fluid fluidType) {
/*  50 */     return (this.owner == fluidType && ((Fluid)this.owner).isSource(this));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  54 */     return getType().isEmpty();
/*     */   }
/*     */   
/*     */   public float getHeight(BlockGetter level, BlockPos pos) {
/*  58 */     return getType().getHeight(this, level, pos);
/*     */   }
/*     */   
/*     */   public float getOwnHeight() {
/*  62 */     return getType().getOwnHeight(this);
/*     */   }
/*     */   
/*     */   public int getAmount() {
/*  66 */     return getType().getAmount(this);
/*     */   }
/*     */   
/*     */   public boolean shouldRenderBackwardUpFace(BlockGetter level, BlockPos above) {
/*  70 */     for (int ox = -1; ox <= 1; ox++) {
/*  71 */       for (int oz = -1; oz <= 1; oz++) {
/*  72 */         BlockPos offset = above.offset(ox, 0, oz);
/*  73 */         FluidState fluidState = level.getFluidState(offset);
/*  74 */         if (!fluidState.getType().isSame(getType()) && !level.getBlockState(offset).isSolidRender()) {
/*  75 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, BlockPos pos, BlockState blockState) {
/*  83 */     getType().tick(level, pos, blockState, this);
/*     */   }
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, RandomSource random) {
/*  87 */     getType().animateTick(level, pos, this, random);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTicking() {
/*  91 */     return getType().isRandomlyTicking();
/*     */   }
/*     */   
/*     */   public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
/*  95 */     getType().randomTick(level, pos, this, random);
/*     */   }
/*     */   
/*     */   public Vec3 getFlow(BlockGetter level, BlockPos pos) {
/*  99 */     return getType().getFlow(level, pos, this);
/*     */   }
/*     */   
/*     */   public BlockState createLegacyBlock() {
/* 103 */     return getType().createLegacyBlock(this);
/*     */   }
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 107 */     return getType().getDripParticle();
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<Fluid> tag) {
/* 111 */     return getType().builtInRegistryHolder().is(tag);
/*     */   }
/*     */   
/*     */   public boolean is(HolderSet<Fluid> set) {
/* 115 */     return set.contains((Holder)getType().builtInRegistryHolder());
/*     */   }
/*     */   
/*     */   public boolean is(Fluid fluid) {
/* 119 */     return (getType() == fluid);
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 123 */     return getType().getExplosionResistance();
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedWith(BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 127 */     return getType().canBeReplacedWith(this, level, pos, other, direction);
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockGetter level, BlockPos pos) {
/* 131 */     return getType().getShape(this, level, pos);
/*     */   }
/*     */   
/*     */   public AABB getAABB(BlockGetter level, BlockPos pos) {
/* 135 */     return getType().getAABB(this, level, pos);
/*     */   }
/*     */   
/*     */   public Holder<Fluid> holder() {
/* 139 */     return (Holder<Fluid>)((Fluid)this.owner).builtInRegistryHolder();
/*     */   }
/*     */   
/*     */   public Stream<TagKey<Fluid>> getTags() {
/* 143 */     return ((Fluid)this.owner).builtInRegistryHolder().tags();
/*     */   }
/*     */   
/*     */   public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/* 147 */     getType().entityInside(level, pos, entity, effectApplier);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/FluidState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */