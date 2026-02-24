/*     */ package net.minecraft.client.renderer.item.properties.numeric;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CompassAngleState extends NeedleDirectionHelper {
/*     */   static {
/*  22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("wobble", true).forGetter(NeedleDirectionHelper::wobble), (App)CompassTarget.CODEC.fieldOf("target").forGetter(CompassAngleState::target)).apply((Applicative)i, CompassAngleState::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<CompassAngleState> MAP_CODEC;
/*     */   
/*     */   private final NeedleDirectionHelper.Wobbler wobbler;
/*     */   private final NeedleDirectionHelper.Wobbler noTargetWobbler;
/*     */   private final CompassTarget compassTarget;
/*  31 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   public CompassAngleState(boolean wobble, CompassTarget compassTarget) {
/*  34 */     super(wobble);
/*     */     
/*  36 */     this.wobbler = newWobbler(0.8F);
/*  37 */     this.noTargetWobbler = newWobbler(0.8F);
/*     */     
/*  39 */     this.compassTarget = compassTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float calculate(ItemStack itemStack, ClientLevel level, int seed, ItemOwner owner) {
/*  44 */     GlobalPos compassTargetPos = this.compassTarget.get(level, itemStack, owner);
/*  45 */     long gameTime = level.getGameTime();
/*     */     
/*  47 */     if (!isValidCompassTargetPos(owner, compassTargetPos)) {
/*  48 */       return getRandomlySpinningRotation(seed, gameTime);
/*     */     }
/*     */     
/*  51 */     return getRotationTowardsCompassTarget(owner, gameTime, compassTargetPos.pos());
/*     */   }
/*     */   
/*     */   private float getRandomlySpinningRotation(int seed, long gameTime) {
/*  55 */     if (this.noTargetWobbler.shouldUpdate(gameTime)) {
/*  56 */       this.noTargetWobbler.update(gameTime, this.random.nextFloat());
/*     */     }
/*  58 */     float targetRotation = this.noTargetWobbler.rotation() + hash(seed) / 2.1474836E9F;
/*  59 */     return Mth.positiveModulo(targetRotation, 1.0F);
/*     */   }
/*     */   
/*     */   private float getRotationTowardsCompassTarget(ItemOwner owner, long gameTime, BlockPos compassTargetPos) {
/*  63 */     float angleToTarget = (float)getAngleFromEntityToPos(owner, compassTargetPos);
/*  64 */     float ownerYRotation = getWrappedVisualRotationY(owner);
/*     */ 
/*     */ 
/*     */     
/*  68 */     LivingEntity entity = owner.asLivingEntity();
/*  69 */     if (entity instanceof Player) { Player player = (Player)entity; if (player.isLocalPlayer() && player.level().tickRateManager().runsNormally())
/*  70 */       { if (this.wobbler.shouldUpdate(gameTime)) {
/*  71 */           this.wobbler.update(gameTime, 0.5F - ownerYRotation - 0.25F);
/*     */         }
/*  73 */         float f = angleToTarget + this.wobbler.rotation();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  78 */         return Mth.positiveModulo(f, 1.0F); }  }  float targetRotation = 0.5F - ownerYRotation - 0.25F - angleToTarget; return Mth.positiveModulo(targetRotation, 1.0F);
/*     */   }
/*     */   
/*     */   private static boolean isValidCompassTargetPos(ItemOwner owner, GlobalPos positionToPointTo) {
/*  82 */     return (positionToPointTo != null && positionToPointTo.dimension() == owner.level().dimension() && 
/*  83 */       positionToPointTo.pos().distToCenterSqr((net.minecraft.core.Position)owner.position()) >= 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   private static double getAngleFromEntityToPos(ItemOwner owner, BlockPos position) {
/*  87 */     Vec3 target = Vec3.atCenterOf((net.minecraft.core.Vec3i)position);
/*  88 */     Vec3 ownerPosition = owner.position();
/*  89 */     return Math.atan2(target.z() - ownerPosition.z(), target.x() - ownerPosition.x()) / 6.2831854820251465D;
/*     */   }
/*     */   
/*     */   private static float getWrappedVisualRotationY(ItemOwner owner) {
/*  93 */     return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static int hash(int input) {
/*  97 */     return input * 1327217883;
/*     */   }
/*     */   
/*     */   protected CompassTarget target() {
/* 101 */     return this.compassTarget;
/*     */   }
/*     */   
/*     */   public enum CompassTarget implements StringRepresentable {
/* 105 */     NONE("none")
/*     */     {
/*     */       public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 108 */         return null;
/*     */       }
/*     */     },
/* 111 */     LODESTONE("lodestone")
/*     */     {
/*     */       public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 114 */         LodestoneTracker tracker = (LodestoneTracker)itemStack.get(net.minecraft.core.component.DataComponents.LODESTONE_TRACKER);
/* 115 */         return (tracker != null) ? tracker.target().orElse(null) : null;
/*     */       }
/*     */     },
/* 118 */     SPAWN("spawn")
/*     */     {
/*     */       public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 121 */         return level.getRespawnData().globalPos();
/*     */       }
/*     */     },
/* 124 */     RECOVERY("recovery")
/*     */     {
/*     */       public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 127 */         LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/* 128 */         Player player = (Player)entity; return (entity instanceof Player) ? player.getLastDeathLocation().orElse(null) : null;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 133 */     public static final Codec<CompassTarget> CODEC = (Codec<CompassTarget>)StringRepresentable.fromEnum(CompassTarget::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     CompassTarget(String name) {
/* 138 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 143 */       return this.name;
/*     */     }
/*     */     
/*     */     abstract GlobalPos get(ClientLevel param1ClientLevel, ItemStack param1ItemStack, ItemOwner param1ItemOwner);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/*     */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/*     */       LodestoneTracker tracker = (LodestoneTracker)itemStack.get(net.minecraft.core.component.DataComponents.LODESTONE_TRACKER);
/*     */       return (tracker != null) ? tracker.target().orElse(null) : null;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/*     */       return level.getRespawnData().globalPos();
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/*     */       LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/*     */       Player player = (Player)entity;
/*     */       return (entity instanceof Player) ? player.getLastDeathLocation().orElse(null) : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/CompassAngleState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */