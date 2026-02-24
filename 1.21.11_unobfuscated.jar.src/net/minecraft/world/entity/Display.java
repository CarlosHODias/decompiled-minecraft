/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.math.Transformation;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Brightness;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Display
/*     */   extends Entity {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   public static final int NO_BRIGHTNESS_OVERRIDE = -1;
/*     */ 
/*     */   
/*  51 */   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  53 */   private static final EntityDataAccessor<Integer> DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  55 */   private static final EntityDataAccessor<Integer> DATA_POS_ROT_INTERPOLATION_DURATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  57 */   private static final EntityDataAccessor<Vector3fc> DATA_TRANSLATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
/*     */   
/*  59 */   private static final EntityDataAccessor<Vector3fc> DATA_SCALE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.VECTOR3);
/*     */   
/*  61 */   private static final EntityDataAccessor<Quaternionfc> DATA_LEFT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
/*     */   
/*  63 */   private static final EntityDataAccessor<Quaternionfc> DATA_RIGHT_ROTATION_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.QUATERNION);
/*     */   
/*  65 */   private static final EntityDataAccessor<Byte> DATA_BILLBOARD_RENDER_CONSTRAINTS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.BYTE);
/*     */   
/*  67 */   private static final EntityDataAccessor<Integer> DATA_BRIGHTNESS_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  69 */   private static final EntityDataAccessor<Float> DATA_VIEW_RANGE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  71 */   private static final EntityDataAccessor<Float> DATA_SHADOW_RADIUS_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  73 */   private static final EntityDataAccessor<Float> DATA_SHADOW_STRENGTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  75 */   private static final EntityDataAccessor<Float> DATA_WIDTH_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  77 */   private static final EntityDataAccessor<Float> DATA_HEIGHT_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.FLOAT);
/*     */   
/*  79 */   private static final EntityDataAccessor<Integer> DATA_GLOW_COLOR_OVERRIDE_ID = SynchedEntityData.defineId(Display.class, EntityDataSerializers.INT);
/*     */   
/*  81 */   private static final IntSet RENDER_STATE_IDS = IntSet.of(new int[] {
/*  82 */         DATA_TRANSLATION_ID.id(), 
/*  83 */         DATA_SCALE_ID.id(), 
/*  84 */         DATA_LEFT_ROTATION_ID.id(), 
/*  85 */         DATA_RIGHT_ROTATION_ID.id(), 
/*  86 */         DATA_BILLBOARD_RENDER_CONSTRAINTS_ID.id(), 
/*  87 */         DATA_BRIGHTNESS_OVERRIDE_ID.id(), 
/*  88 */         DATA_SHADOW_RADIUS_ID.id(), 
/*  89 */         DATA_SHADOW_STRENGTH_ID.id() }); private static final int INITIAL_TRANSFORMATION_INTERPOLATION_DURATION = 0; private static final int INITIAL_TRANSFORMATION_START_INTERPOLATION = 0; private static final int INITIAL_POS_ROT_INTERPOLATION_DURATION = 0; private static final float INITIAL_SHADOW_RADIUS = 0.0F; private static final float INITIAL_SHADOW_STRENGTH = 1.0F; private static final float INITIAL_VIEW_RANGE = 1.0F; private static final float INITIAL_WIDTH = 0.0F; private static final float INITIAL_HEIGHT = 0.0F; private static final int NO_GLOW_COLOR_OVERRIDE = -1;
/*     */   public static final String TAG_POS_ROT_INTERPOLATION_DURATION = "teleport_duration";
/*     */   public static final String TAG_TRANSFORMATION_INTERPOLATION_DURATION = "interpolation_duration";
/*     */   public static final String TAG_TRANSFORMATION_START_INTERPOLATION = "start_interpolation";
/*     */   public static final String TAG_TRANSFORMATION = "transformation";
/*     */   public static final String TAG_BILLBOARD = "billboard";
/*     */   public static final String TAG_BRIGHTNESS = "brightness";
/*     */   public static final String TAG_VIEW_RANGE = "view_range";
/*     */   public static final String TAG_SHADOW_RADIUS = "shadow_radius";
/*     */   public static final String TAG_SHADOW_STRENGTH = "shadow_strength";
/*     */   public static final String TAG_WIDTH = "width";
/*     */   public static final String TAG_HEIGHT = "height";
/*     */   public static final String TAG_GLOW_COLOR_OVERRIDE = "glow_color_override";
/*     */   
/* 103 */   public enum BillboardConstraints implements StringRepresentable { FIXED((byte)0, "fixed"),
/* 104 */     VERTICAL((byte)1, "vertical"),
/* 105 */     HORIZONTAL((byte)2, "horizontal"),
/* 106 */     CENTER((byte)3, "center");
/*     */ 
/*     */     
/* 109 */     public static final Codec<BillboardConstraints> CODEC = (Codec<BillboardConstraints>)StringRepresentable.fromEnum(BillboardConstraints::values);
/* 110 */     public static final IntFunction<BillboardConstraints> BY_ID = ByIdMap.continuous(BillboardConstraints::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/*     */     private final byte id;
/*     */     private final String name;
/*     */     
/*     */     BillboardConstraints(byte id, String name) {
/* 116 */       this.name = name;
/* 117 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 122 */       return this.name;
/*     */     }
/*     */     
/*     */     private byte getId() {
/* 126 */       return this.id;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   private long interpolationStartClientTick = -2147483648L;
/*     */   
/*     */   private int interpolationDuration;
/*     */   
/*     */   private float lastProgress;
/*     */   
/*     */   private AABB cullingBoundingBox;
/*     */   private boolean noCulling = true;
/*     */   protected boolean updateRenderState;
/*     */   private boolean updateStartTick;
/*     */   private boolean updateInterpolationDuration;
/*     */   private RenderState renderState;
/* 158 */   private final InterpolationHandler interpolation = new InterpolationHandler(this, 0);
/*     */   
/*     */   public Display(EntityType<?> type, Level level) {
/* 161 */     super(type, level);
/* 162 */     this.noPhysics = true;
/* 163 */     this.cullingBoundingBox = getBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 168 */     super.onSyncedDataUpdated(accessor);
/*     */     
/* 170 */     if (DATA_HEIGHT_ID.equals(accessor) || DATA_WIDTH_ID.equals(accessor)) {
/* 171 */       updateCulling();
/*     */     }
/*     */     
/* 174 */     if (DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID.equals(accessor)) {
/* 175 */       this.updateStartTick = true;
/*     */     }
/*     */     
/* 178 */     if (DATA_POS_ROT_INTERPOLATION_DURATION_ID.equals(accessor)) {
/* 179 */       this.interpolation.setInterpolationLength(getPosRotInterpolationDuration());
/*     */     }
/*     */     
/* 182 */     if (DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID.equals(accessor)) {
/* 183 */       this.updateInterpolationDuration = true;
/*     */     }
/*     */     
/* 186 */     if (RENDER_STATE_IDS.contains(accessor.id())) {
/* 187 */       this.updateRenderState = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   private static Transformation createTransformation(SynchedEntityData entityData) {
/* 197 */     Vector3fc translation = (Vector3fc)entityData.get(DATA_TRANSLATION_ID);
/* 198 */     Quaternionfc leftRotation = (Quaternionfc)entityData.get(DATA_LEFT_ROTATION_ID);
/* 199 */     Vector3fc scale = (Vector3fc)entityData.get(DATA_SCALE_ID);
/* 200 */     Quaternionfc rightRotation = (Quaternionfc)entityData.get(DATA_RIGHT_ROTATION_ID);
/* 201 */     return new Transformation(translation, leftRotation, scale, rightRotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 206 */     Entity vehicle = getVehicle();
/* 207 */     if (vehicle != null && vehicle.isRemoved()) {
/* 208 */       stopRiding();
/*     */     }
/*     */     
/* 211 */     if (level().isClientSide()) {
/* 212 */       if (this.updateStartTick) {
/* 213 */         this.updateStartTick = false;
/*     */         
/* 215 */         int interpolationStartDelta = getTransformationInterpolationDelay();
/* 216 */         this.interpolationStartClientTick = (this.tickCount + interpolationStartDelta);
/*     */       } 
/*     */       
/* 219 */       if (this.updateInterpolationDuration) {
/* 220 */         this.updateInterpolationDuration = false;
/* 221 */         this.interpolationDuration = getTransformationInterpolationDuration();
/*     */       } 
/*     */       
/* 224 */       if (this.updateRenderState) {
/* 225 */         this.updateRenderState = false;
/*     */         
/* 227 */         boolean shouldInterpolate = (this.interpolationDuration != 0);
/* 228 */         if (shouldInterpolate && this.renderState != null) {
/* 229 */           this.renderState = createInterpolatedRenderState(this.renderState, this.lastProgress);
/*     */         } else {
/* 231 */           this.renderState = createFreshRenderState();
/*     */         } 
/* 233 */         updateRenderSubState(shouldInterpolate, this.lastProgress);
/*     */       } 
/*     */       
/* 236 */       this.interpolation.interpolate();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 242 */     return this.interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void updateRenderSubState(boolean paramBoolean, float paramFloat);
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 249 */     entityData.define(DATA_POS_ROT_INTERPOLATION_DURATION_ID, 0);
/* 250 */     entityData.define(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, 0);
/* 251 */     entityData.define(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, 0);
/* 252 */     entityData.define(DATA_TRANSLATION_ID, new Vector3f());
/* 253 */     entityData.define(DATA_SCALE_ID, new Vector3f(1.0F, 1.0F, 1.0F));
/* 254 */     entityData.define(DATA_RIGHT_ROTATION_ID, new Quaternionf());
/* 255 */     entityData.define(DATA_LEFT_ROTATION_ID, new Quaternionf());
/* 256 */     entityData.define(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, BillboardConstraints.FIXED.getId());
/* 257 */     entityData.define(DATA_BRIGHTNESS_OVERRIDE_ID, -1);
/* 258 */     entityData.define(DATA_VIEW_RANGE_ID, 1.0F);
/* 259 */     entityData.define(DATA_SHADOW_RADIUS_ID, 0.0F);
/* 260 */     entityData.define(DATA_SHADOW_STRENGTH_ID, 1.0F);
/* 261 */     entityData.define(DATA_WIDTH_ID, 0.0F);
/* 262 */     entityData.define(DATA_HEIGHT_ID, 0.0F);
/* 263 */     entityData.define(DATA_GLOW_COLOR_OVERRIDE_ID, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 268 */     setTransformation(input.read("transformation", Transformation.EXTENDED_CODEC).orElse(Transformation.identity()));
/*     */     
/* 270 */     setTransformationInterpolationDuration(input.getIntOr("interpolation_duration", 0));
/* 271 */     setTransformationInterpolationDelay(input.getIntOr("start_interpolation", 0));
/*     */     
/* 273 */     int teleportDuration = input.getIntOr("teleport_duration", 0);
/*     */     
/* 275 */     setPosRotInterpolationDuration(Mth.clamp(teleportDuration, 0, 59));
/*     */     
/* 277 */     setBillboardConstraints(input.read("billboard", BillboardConstraints.CODEC).orElse(BillboardConstraints.FIXED));
/*     */     
/* 279 */     setViewRange(input.getFloatOr("view_range", 1.0F));
/* 280 */     setShadowRadius(input.getFloatOr("shadow_radius", 0.0F));
/* 281 */     setShadowStrength(input.getFloatOr("shadow_strength", 1.0F));
/* 282 */     setWidth(input.getFloatOr("width", 0.0F));
/* 283 */     setHeight(input.getFloatOr("height", 0.0F));
/*     */     
/* 285 */     setGlowColorOverride(input.getIntOr("glow_color_override", -1));
/*     */     
/* 287 */     setBrightnessOverride(input.read("brightness", Brightness.CODEC).orElse(null));
/*     */   }
/*     */   
/*     */   private void setTransformation(Transformation transformation) {
/* 291 */     this.entityData.set(DATA_TRANSLATION_ID, transformation.getTranslation());
/* 292 */     this.entityData.set(DATA_LEFT_ROTATION_ID, transformation.getLeftRotation());
/* 293 */     this.entityData.set(DATA_SCALE_ID, transformation.getScale());
/* 294 */     this.entityData.set(DATA_RIGHT_ROTATION_ID, transformation.getRightRotation());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 299 */     output.store("transformation", Transformation.EXTENDED_CODEC, createTransformation(this.entityData));
/* 300 */     output.store("billboard", BillboardConstraints.CODEC, getBillboardConstraints());
/* 301 */     output.putInt("interpolation_duration", getTransformationInterpolationDuration());
/* 302 */     output.putInt("teleport_duration", getPosRotInterpolationDuration());
/* 303 */     output.putFloat("view_range", getViewRange());
/* 304 */     output.putFloat("shadow_radius", getShadowRadius());
/* 305 */     output.putFloat("shadow_strength", getShadowStrength());
/* 306 */     output.putFloat("width", getWidth());
/* 307 */     output.putFloat("height", getHeight());
/* 308 */     output.putInt("glow_color_override", getGlowColorOverride());
/* 309 */     output.storeNullable("brightness", Brightness.CODEC, getBrightnessOverride());
/*     */   }
/*     */   
/*     */   public AABB getBoundingBoxForCulling() {
/* 313 */     return this.cullingBoundingBox;
/*     */   }
/*     */   
/*     */   public boolean affectedByCulling() {
/* 317 */     return !this.noCulling;
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 322 */     return PushReaction.IGNORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 327 */     return true;
/*     */   }
/*     */   
/*     */   public RenderState renderState() {
/* 331 */     return this.renderState;
/*     */   }
/*     */   
/*     */   private void setTransformationInterpolationDuration(int duration) {
/* 335 */     this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID, duration);
/*     */   }
/*     */   
/*     */   private int getTransformationInterpolationDuration() {
/* 339 */     return (Integer)this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID);
/*     */   }
/*     */   
/*     */   private void setTransformationInterpolationDelay(int ticks) {
/* 343 */     this.entityData.set(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID, ticks, true);
/*     */   }
/*     */   
/*     */   private int getTransformationInterpolationDelay() {
/* 347 */     return (Integer)this.entityData.get(DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID);
/*     */   }
/*     */   
/*     */   private void setPosRotInterpolationDuration(int duration) {
/* 351 */     this.entityData.set(DATA_POS_ROT_INTERPOLATION_DURATION_ID, duration);
/*     */   }
/*     */   
/*     */   private int getPosRotInterpolationDuration() {
/* 355 */     return (Integer)this.entityData.get(DATA_POS_ROT_INTERPOLATION_DURATION_ID);
/*     */   }
/*     */   
/*     */   private void setBillboardConstraints(BillboardConstraints constraints) {
/* 359 */     this.entityData.set(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, constraints.getId());
/*     */   }
/*     */   
/*     */   private BillboardConstraints getBillboardConstraints() {
/* 363 */     return BillboardConstraints.BY_ID.apply((Byte)this.entityData.get(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID));
/*     */   }
/*     */   
/*     */   private void setBrightnessOverride(Brightness brightness) {
/* 367 */     this.entityData.set(DATA_BRIGHTNESS_OVERRIDE_ID, (brightness != null) ? brightness.pack() : -1);
/*     */   }
/*     */   
/*     */   private Brightness getBrightnessOverride() {
/* 371 */     int value = (Integer)this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID);
/* 372 */     return (value != -1) ? Brightness.unpack(value) : null;
/*     */   }
/*     */   
/*     */   private int getPackedBrightnessOverride() {
/* 376 */     return (Integer)this.entityData.get(DATA_BRIGHTNESS_OVERRIDE_ID);
/*     */   }
/*     */   
/*     */   private void setViewRange(float range) {
/* 380 */     this.entityData.set(DATA_VIEW_RANGE_ID, range);
/*     */   }
/*     */   
/*     */   private float getViewRange() {
/* 384 */     return (Float)this.entityData.get(DATA_VIEW_RANGE_ID);
/*     */   }
/*     */   
/*     */   private void setShadowRadius(float size) {
/* 388 */     this.entityData.set(DATA_SHADOW_RADIUS_ID, size);
/*     */   }
/*     */   
/*     */   private float getShadowRadius() {
/* 392 */     return (Float)this.entityData.get(DATA_SHADOW_RADIUS_ID);
/*     */   }
/*     */   
/*     */   private void setShadowStrength(float strength) {
/* 396 */     this.entityData.set(DATA_SHADOW_STRENGTH_ID, strength);
/*     */   }
/*     */   
/*     */   private float getShadowStrength() {
/* 400 */     return (Float)this.entityData.get(DATA_SHADOW_STRENGTH_ID);
/*     */   }
/*     */   
/*     */   private void setWidth(float width) {
/* 404 */     this.entityData.set(DATA_WIDTH_ID, width);
/*     */   }
/*     */   
/*     */   private float getWidth() {
/* 408 */     return (Float)this.entityData.get(DATA_WIDTH_ID);
/*     */   }
/*     */   
/*     */   private void setHeight(float width) {
/* 412 */     this.entityData.set(DATA_HEIGHT_ID, width);
/*     */   }
/*     */   
/*     */   private int getGlowColorOverride() {
/* 416 */     return (Integer)this.entityData.get(DATA_GLOW_COLOR_OVERRIDE_ID);
/*     */   }
/*     */   
/*     */   private void setGlowColorOverride(int value) {
/* 420 */     this.entityData.set(DATA_GLOW_COLOR_OVERRIDE_ID, value);
/*     */   }
/*     */   
/*     */   public float calculateInterpolationProgress(float partialTickTime) {
/* 424 */     int duration = this.interpolationDuration;
/* 425 */     if (duration <= 0) {
/* 426 */       return 1.0F;
/*     */     }
/*     */     
/* 429 */     float ticksSinceUpdate = (float)(this.tickCount - this.interpolationStartClientTick);
/* 430 */     float partialTicksSinceLastUpdate = ticksSinceUpdate + partialTickTime;
/* 431 */     float result = Mth.clamp(Mth.inverseLerp(partialTicksSinceLastUpdate, 0.0F, duration), 0.0F, 1.0F);
/* 432 */     this.lastProgress = result;
/* 433 */     return result;
/*     */   }
/*     */   
/*     */   private float getHeight() {
/* 437 */     return (Float)this.entityData.get(DATA_HEIGHT_ID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 442 */     super.setPos(x, y, z);
/* 443 */     updateCulling();
/*     */   }
/*     */   
/*     */   private void updateCulling() {
/* 447 */     float width = getWidth();
/* 448 */     float height = getHeight();
/*     */     
/* 450 */     this.noCulling = (width == 0.0F || height == 0.0F);
/*     */     
/* 452 */     float w = width / 2.0F;
/* 453 */     double x = getX();
/* 454 */     double y = getY();
/* 455 */     double z = getZ();
/* 456 */     this.cullingBoundingBox = new AABB(x - w, y, z - w, x + w, y + height, z + w);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distanceSqr) {
/* 461 */     return (distanceSqr < Mth.square(getViewRange() * 64.0D * getViewScale()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTeamColor() {
/* 466 */     int glowColorOverride = getGlowColorOverride();
/* 467 */     return (glowColorOverride != -1) ? glowColorOverride : super.getTeamColor();
/*     */   }
/*     */   
/*     */   private RenderState createFreshRenderState() {
/* 471 */     return new RenderState(
/* 472 */         GenericInterpolator.constant(createTransformation(this.entityData)), 
/* 473 */         getBillboardConstraints(), 
/* 474 */         getPackedBrightnessOverride(), 
/* 475 */         FloatInterpolator.constant(getShadowRadius()), 
/* 476 */         FloatInterpolator.constant(getShadowStrength()), 
/* 477 */         getGlowColorOverride());
/*     */   }
/*     */ 
/*     */   
/*     */   private RenderState createInterpolatedRenderState(RenderState previousState, float progress) {
/* 482 */     Transformation currentTransform = previousState.transformation.get(progress);
/* 483 */     float currentShadowRadius = previousState.shadowRadius.get(progress);
/* 484 */     float currentShadowStrength = previousState.shadowStrength.get(progress);
/*     */     
/* 486 */     return new RenderState(new TransformationInterpolator(currentTransform, 
/* 487 */           createTransformation(this.entityData)), 
/* 488 */         getBillboardConstraints(), 
/* 489 */         getPackedBrightnessOverride(), new LinearFloatInterpolator(currentShadowRadius, 
/* 490 */           getShadowRadius()), new LinearFloatInterpolator(currentShadowStrength, 
/* 491 */           getShadowStrength()), 
/* 492 */         getGlowColorOverride());
/*     */   }
/*     */   public static final class RenderState extends Record { private final Display.GenericInterpolator<Transformation> transformation; private final Display.BillboardConstraints billboardConstraints; private final int brightnessOverride; private final Display.FloatInterpolator shadowRadius; private final Display.FloatInterpolator shadowStrength; private final int glowColorOverride;
/*     */     
/* 496 */     public RenderState(Display.GenericInterpolator<Transformation> transformation, Display.BillboardConstraints billboardConstraints, int brightnessOverride, Display.FloatInterpolator shadowRadius, Display.FloatInterpolator shadowStrength, int glowColorOverride) { this.transformation = transformation; this.billboardConstraints = billboardConstraints; this.brightnessOverride = brightnessOverride; this.shadowRadius = shadowRadius; this.shadowStrength = shadowStrength; this.glowColorOverride = glowColorOverride; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$RenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #496	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$RenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #496	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$RenderState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$RenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #496	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$RenderState;
/* 496 */       //   0	8	1	o	Ljava/lang/Object; } public Display.GenericInterpolator<Transformation> transformation() { return this.transformation; } public Display.BillboardConstraints billboardConstraints() { return this.billboardConstraints; } public int brightnessOverride() { return this.brightnessOverride; } public Display.FloatInterpolator shadowRadius() { return this.shadowRadius; } public Display.FloatInterpolator shadowStrength() { return this.shadowStrength; } public int glowColorOverride() { return this.glowColorOverride; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ItemDisplay
/*     */     extends Display
/*     */   {
/*     */     private static final String TAG_ITEM = "item";
/*     */ 
/*     */     
/*     */     private static final String TAG_ITEM_DISPLAY = "item_display";
/*     */     
/* 510 */     private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.ITEM_STACK);
/*     */     
/* 512 */     private static final EntityDataAccessor<Byte> DATA_ITEM_DISPLAY_ID = SynchedEntityData.defineId(ItemDisplay.class, EntityDataSerializers.BYTE);
/*     */     
/* 514 */     private final SlotAccess slot = SlotAccess.of(this::getItemStack, this::setItemStack);
/*     */     
/*     */     private ItemRenderState itemRenderState;
/*     */     
/*     */     public ItemDisplay(EntityType<?> type, Level level) {
/* 519 */       super(type, level);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 524 */       super.defineSynchedData(entityData);
/* 525 */       entityData.define(DATA_ITEM_STACK_ID, ItemStack.EMPTY);
/* 526 */       entityData.define(DATA_ITEM_DISPLAY_ID, ItemDisplayContext.NONE.getId());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 531 */       super.onSyncedDataUpdated(accessor);
/*     */       
/* 533 */       if (DATA_ITEM_STACK_ID.equals(accessor) || DATA_ITEM_DISPLAY_ID.equals(accessor)) {
/* 534 */         this.updateRenderState = true;
/*     */       }
/*     */     }
/*     */     
/*     */     private ItemStack getItemStack() {
/* 539 */       return (ItemStack)this.entityData.get(DATA_ITEM_STACK_ID);
/*     */     }
/*     */     
/*     */     private void setItemStack(ItemStack item) {
/* 543 */       this.entityData.set(DATA_ITEM_STACK_ID, item);
/*     */     }
/*     */     
/*     */     private void setItemTransform(ItemDisplayContext transform) {
/* 547 */       this.entityData.set(DATA_ITEM_DISPLAY_ID, transform.getId());
/*     */     }
/*     */     
/*     */     private ItemDisplayContext getItemTransform() {
/* 551 */       return ItemDisplayContext.BY_ID.apply((Byte)this.entityData.get(DATA_ITEM_DISPLAY_ID));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 556 */       super.readAdditionalSaveData(input);
/* 557 */       setItemStack(input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 558 */       setItemTransform(input.read("item_display", ItemDisplayContext.CODEC).orElse(ItemDisplayContext.NONE));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 563 */       super.addAdditionalSaveData(output);
/* 564 */       ItemStack itemStack = getItemStack();
/* 565 */       if (!itemStack.isEmpty()) {
/* 566 */         output.store("item", ItemStack.CODEC, itemStack);
/*     */       }
/* 568 */       output.store("item_display", ItemDisplayContext.CODEC, getItemTransform());
/*     */     }
/*     */ 
/*     */     
/*     */     public SlotAccess getSlot(int slot) {
/* 573 */       if (slot == 0) {
/* 574 */         return this.slot;
/*     */       }
/* 576 */       return null;
/*     */     }
/*     */     
/*     */     public ItemRenderState itemRenderState() {
/* 580 */       return this.itemRenderState;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 585 */       ItemStack itemStack = getItemStack();
/* 586 */       itemStack.setEntityRepresentation(this);
/* 587 */       this.itemRenderState = new ItemRenderState(itemStack, getItemTransform());
/*     */     }
/*     */     public static final class ItemRenderState extends Record { private final ItemStack itemStack; private final ItemDisplayContext itemTransform;
/* 590 */       public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) { this.itemStack = itemStack; this.itemTransform = itemTransform; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #590	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #590	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #590	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/* 590 */         //   0	8	1	o	Ljava/lang/Object; } public ItemStack itemStack() { return this.itemStack; } public ItemDisplayContext itemTransform() { return this.itemTransform; } } } public static final class ItemRenderState extends Record { private final ItemStack itemStack; private final ItemDisplayContext itemTransform; public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) { this.itemStack = itemStack; this.itemTransform = itemTransform; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #590	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #590	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #590	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/* 590 */       //   0	8	1	o	Ljava/lang/Object; } public ItemStack itemStack() { return this.itemStack; } public ItemDisplayContext itemTransform() { return this.itemTransform; }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BlockDisplay
/*     */     extends Display
/*     */   {
/*     */     public static final String TAG_BLOCK_STATE = "block_state";
/*     */     
/* 600 */     private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(BlockDisplay.class, EntityDataSerializers.BLOCK_STATE);
/*     */     
/*     */     private BlockRenderState blockRenderState;
/*     */     
/*     */     public BlockDisplay(EntityType<?> type, Level level) {
/* 605 */       super(type, level);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 610 */       super.defineSynchedData(entityData);
/* 611 */       entityData.define(DATA_BLOCK_STATE_ID, Blocks.AIR.defaultBlockState());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 616 */       super.onSyncedDataUpdated(accessor);
/*     */       
/* 618 */       if (accessor.equals(DATA_BLOCK_STATE_ID)) {
/* 619 */         this.updateRenderState = true;
/*     */       }
/*     */     }
/*     */     
/*     */     private BlockState getBlockState() {
/* 624 */       return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
/*     */     }
/*     */     
/*     */     private void setBlockState(BlockState blockState) {
/* 628 */       this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 633 */       super.readAdditionalSaveData(input);
/* 634 */       setBlockState(input.read("block_state", BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState()));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 639 */       super.addAdditionalSaveData(output);
/* 640 */       output.store("block_state", BlockState.CODEC, getBlockState());
/*     */     }
/*     */     
/*     */     public BlockRenderState blockRenderState() {
/* 644 */       return this.blockRenderState;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 649 */       this.blockRenderState = new BlockRenderState(getBlockState());
/*     */     }
/*     */     public static final class BlockRenderState extends Record { private final BlockState blockState;
/* 652 */       public BlockRenderState(BlockState blockState) { this.blockState = blockState; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #652	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #652	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #652	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/* 652 */         //   0	8	1	o	Ljava/lang/Object; } public BlockState blockState() { return this.blockState; } } } public static final class BlockRenderState extends Record { private final BlockState blockState; public BlockRenderState(BlockState blockState) { this.blockState = blockState; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #652	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #652	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #652	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/* 652 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState blockState() { return this.blockState; }
/*     */      }
/*     */   public static class TextDisplay extends Display { public static final String TAG_TEXT = "text"; private static final String TAG_LINE_WIDTH = "line_width"; private static final String TAG_TEXT_OPACITY = "text_opacity"; private static final String TAG_BACKGROUND_COLOR = "background"; private static final String TAG_SHADOW = "shadow"; private static final String TAG_SEE_THROUGH = "see_through"; private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background"; private static final String TAG_ALIGNMENT = "alignment"; public static final byte FLAG_SHADOW = 1; public static final byte FLAG_SEE_THROUGH = 2; public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4; public static final byte FLAG_ALIGN_LEFT = 8;
/*     */     public static final byte FLAG_ALIGN_RIGHT = 16;
/*     */     private static final byte INITIAL_TEXT_OPACITY = -1;
/*     */     public static final int INITIAL_BACKGROUND = 1073741824;
/*     */     private static final int INITIAL_LINE_WIDTH = 200;
/*     */     
/* 660 */     public enum Align implements StringRepresentable { CENTER("center"),
/* 661 */       LEFT("left"),
/* 662 */       RIGHT("right");
/*     */       
/* 664 */       public static final Codec<Align> CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values);
/*     */       
/*     */       private final String name;
/*     */       
/*     */       Align(String name) {
/* 669 */         this.name = name;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getSerializedName() {
/* 674 */         return this.name;
/*     */       } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 697 */     private static final EntityDataAccessor<Component> DATA_TEXT_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.COMPONENT);
/* 698 */     private static final EntityDataAccessor<Integer> DATA_LINE_WIDTH_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/* 699 */     private static final EntityDataAccessor<Integer> DATA_BACKGROUND_COLOR_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.INT);
/* 700 */     private static final EntityDataAccessor<Byte> DATA_TEXT_OPACITY_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/* 701 */     private static final EntityDataAccessor<Byte> DATA_STYLE_FLAGS_ID = SynchedEntityData.defineId(TextDisplay.class, EntityDataSerializers.BYTE);
/*     */     
/* 703 */     private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(new int[] {
/* 704 */           DATA_TEXT_ID.id(), 
/* 705 */           DATA_LINE_WIDTH_ID.id(), 
/* 706 */           DATA_BACKGROUND_COLOR_ID.id(), 
/* 707 */           DATA_TEXT_OPACITY_ID.id(), 
/* 708 */           DATA_STYLE_FLAGS_ID.id()
/*     */         });
/*     */     
/*     */     private CachedInfo clientDisplayCache;
/*     */     
/*     */     private TextRenderState textRenderState;
/*     */     
/*     */     public TextDisplay(EntityType<?> type, Level level) {
/* 716 */       super(type, level);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 721 */       super.defineSynchedData(entityData);
/* 722 */       entityData.define(DATA_TEXT_ID, Component.empty());
/* 723 */       entityData.define(DATA_LINE_WIDTH_ID, 200);
/* 724 */       entityData.define(DATA_BACKGROUND_COLOR_ID, 1073741824);
/* 725 */       entityData.define(DATA_TEXT_OPACITY_ID, (byte)-1);
/* 726 */       entityData.define(DATA_STYLE_FLAGS_ID, (byte)0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 731 */       super.onSyncedDataUpdated(accessor);
/*     */       
/* 733 */       if (TEXT_RENDER_STATE_IDS.contains(accessor.id())) {
/* 734 */         this.updateRenderState = true;
/*     */       }
/*     */     }
/*     */     
/*     */     private Component getText() {
/* 739 */       return (Component)this.entityData.get(DATA_TEXT_ID);
/*     */     }
/*     */     
/*     */     private void setText(Component text) {
/* 743 */       this.entityData.set(DATA_TEXT_ID, text);
/*     */     }
/*     */     
/*     */     private int getLineWidth() {
/* 747 */       return (Integer)this.entityData.get(DATA_LINE_WIDTH_ID);
/*     */     }
/*     */     
/*     */     private void setLineWidth(int width) {
/* 751 */       this.entityData.set(DATA_LINE_WIDTH_ID, width);
/*     */     }
/*     */     
/*     */     private byte getTextOpacity() {
/* 755 */       return (Byte)this.entityData.get(DATA_TEXT_OPACITY_ID);
/*     */     }
/*     */     
/*     */     private void setTextOpacity(byte opacity) {
/* 759 */       this.entityData.set(DATA_TEXT_OPACITY_ID, opacity);
/*     */     }
/*     */     
/*     */     private int getBackgroundColor() {
/* 763 */       return (Integer)this.entityData.get(DATA_BACKGROUND_COLOR_ID);
/*     */     }
/*     */     
/*     */     private void setBackgroundColor(int color) {
/* 767 */       this.entityData.set(DATA_BACKGROUND_COLOR_ID, color);
/*     */     }
/*     */     
/*     */     private byte getFlags() {
/* 771 */       return (Byte)this.entityData.get(DATA_STYLE_FLAGS_ID);
/*     */     }
/*     */     
/*     */     private void setFlags(byte flags) {
/* 775 */       this.entityData.set(DATA_STYLE_FLAGS_ID, flags);
/*     */     }
/*     */     
/*     */     private static byte loadFlag(byte flags, ValueInput input, String id, byte mask) {
/* 779 */       if (input.getBooleanOr(id, false)) {
/* 780 */         return (byte)(flags | mask);
/*     */       }
/* 782 */       return flags;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void readAdditionalSaveData(ValueInput input) {
/* 787 */       super.readAdditionalSaveData(input);
/*     */       
/* 789 */       setLineWidth(input.getIntOr("line_width", 200));
/* 790 */       setTextOpacity(input.getByteOr("text_opacity", (byte)-1));
/* 791 */       setBackgroundColor(input.getIntOr("background", 1073741824));
/*     */ 
/*     */       
/* 794 */       byte flags = loadFlag((byte)0, input, "shadow", (byte)1);
/* 795 */       flags = loadFlag(flags, input, "see_through", (byte)2);
/* 796 */       flags = loadFlag(flags, input, "default_background", (byte)4);
/*     */       
/* 798 */       Optional<Align> alignment = input.read("alignment", Align.CODEC);
/* 799 */       if (alignment.isPresent()) {
/* 800 */         switch (((Align)alignment.get()).ordinal()) { default: throw new MatchException(null, null);
/*     */           case 0: 
/*     */           case 1: 
/* 803 */           case 2: break; }  flags = (byte)(flags | 0x10);
/*     */       } 
/*     */ 
/*     */       
/* 807 */       setFlags(flags);
/*     */       
/* 809 */       Optional<Component> text = input.read("text", ComponentSerialization.CODEC);
/* 810 */       if (text.isPresent()) {
/*     */         try {
/* 812 */           Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 813 */             CommandSourceStack context = createCommandSourceStackForNameResolution(serverLevel).withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/* 814 */             MutableComponent mutableComponent = ComponentUtils.updateForEntity(context, text.get(), this, 0);
/* 815 */             setText((Component)mutableComponent); }
/*     */           else
/* 817 */           { setText((Component)Component.empty()); }
/*     */         
/* 819 */         } catch (Exception e) {
/* 820 */           Display.LOGGER.warn("Failed to parse display entity text {}", text, e);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     private static void storeFlag(byte flags, ValueOutput output, String id, byte mask) {
/* 826 */       output.putBoolean(id, ((flags & mask) != 0));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(ValueOutput output) {
/* 831 */       super.addAdditionalSaveData(output);
/* 832 */       output.store("text", ComponentSerialization.CODEC, getText());
/* 833 */       output.putInt("line_width", getLineWidth());
/* 834 */       output.putInt("background", getBackgroundColor());
/* 835 */       output.putByte("text_opacity", getTextOpacity());
/*     */       
/* 837 */       byte flags = getFlags();
/* 838 */       storeFlag(flags, output, "shadow", (byte)1);
/* 839 */       storeFlag(flags, output, "see_through", (byte)2);
/* 840 */       storeFlag(flags, output, "default_background", (byte)4);
/* 841 */       output.store("alignment", Align.CODEC, getAlign(flags));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 846 */       if (shouldInterpolate && this.textRenderState != null) {
/* 847 */         this.textRenderState = createInterpolatedTextRenderState(this.textRenderState, progress);
/*     */       } else {
/* 849 */         this.textRenderState = createFreshTextRenderState();
/*     */       } 
/* 851 */       this.clientDisplayCache = null;
/*     */     }
/*     */     
/*     */     public TextRenderState textRenderState() {
/* 855 */       return this.textRenderState;
/*     */     }
/*     */     
/*     */     private TextRenderState createFreshTextRenderState() {
/* 859 */       return new TextRenderState(
/* 860 */           getText(), 
/* 861 */           getLineWidth(), 
/* 862 */           Display.IntInterpolator.constant(getTextOpacity()), 
/* 863 */           Display.IntInterpolator.constant(getBackgroundColor()), 
/* 864 */           getFlags());
/*     */     }
/*     */ 
/*     */     
/*     */     private TextRenderState createInterpolatedTextRenderState(TextRenderState previous, float progress) {
/* 869 */       int currentBackground = previous.backgroundColor.get(progress);
/* 870 */       int currentOpacity = previous.textOpacity.get(progress);
/*     */       
/* 872 */       return new TextRenderState(
/* 873 */           getText(), 
/* 874 */           getLineWidth(), new Display.LinearIntInterpolator(currentOpacity, 
/* 875 */             getTextOpacity()), new Display.ColorInterpolator(currentBackground, 
/* 876 */             getBackgroundColor()), 
/* 877 */           getFlags());
/*     */     }
/*     */     public static final class CachedLine extends Record { private final FormattedCharSequence contents; private final int width;
/*     */       
/* 881 */       public CachedLine(FormattedCharSequence contents, int width) { this.contents = contents; this.width = width; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #881	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #881	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #881	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/* 881 */         //   0	8	1	o	Ljava/lang/Object; } public FormattedCharSequence contents() { return this.contents; } public int width() { return this.width; }
/*     */        } public static final class CachedInfo extends Record { private final List<Display.TextDisplay.CachedLine> lines; private final int width;
/* 883 */       public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) { this.lines = lines; this.width = width; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #883	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #883	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #883	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/* 883 */         //   0	8	1	o	Ljava/lang/Object; } public List<Display.TextDisplay.CachedLine> lines() { return this.lines; } public int width() { return this.width; }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedInfo cacheDisplay(LineSplitter splitter) {
/* 891 */       if (this.clientDisplayCache == null) {
/* 892 */         if (this.textRenderState != null) {
/* 893 */           this.clientDisplayCache = splitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
/*     */         } else {
/* 895 */           this.clientDisplayCache = new CachedInfo(List.of(), 0);
/*     */         } 
/*     */       }
/*     */       
/* 899 */       return this.clientDisplayCache;
/*     */     }
/*     */     
/*     */     public static Align getAlign(byte flags) {
/* 903 */       if ((flags & 0x8) != 0) {
/* 904 */         return Align.LEFT;
/*     */       }
/* 906 */       if ((flags & 0x10) != 0) {
/* 907 */         return Align.RIGHT;
/*     */       }
/* 909 */       return Align.CENTER;
/*     */     }
/*     */     public static final class TextRenderState extends Record { private final Component text; private final int lineWidth; private final Display.IntInterpolator textOpacity; private final Display.IntInterpolator backgroundColor; private final byte flags;
/* 912 */       public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) { this.text = text; this.lineWidth = lineWidth; this.textOpacity = textOpacity; this.backgroundColor = backgroundColor; this.flags = flags; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #912	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #912	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #912	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/* 912 */         //   0	8	1	o	Ljava/lang/Object; } public Component text() { return this.text; } public int lineWidth() { return this.lineWidth; } public Display.IntInterpolator textOpacity() { return this.textOpacity; } public Display.IntInterpolator backgroundColor() { return this.backgroundColor; } public byte flags() { return this.flags; } } @FunctionalInterface public static interface LineSplitter { Display.TextDisplay.CachedInfo split(Component param2Component, int param2Int); } } public enum Align implements StringRepresentable { CENTER("center"), LEFT("left"), RIGHT("right"); public static final Codec<Align> CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values); private final String name; Align(String name) { this.name = name; } public String getSerializedName() { return this.name; } } public static final class CachedLine extends Record { private final FormattedCharSequence contents; private final int width; public CachedLine(FormattedCharSequence contents, int width) { this.contents = contents; this.width = width; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #881	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedLine;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public FormattedCharSequence contents() { return this.contents; } public int width() { return this.width; } } public static final class CachedInfo extends Record { private final List<Display.TextDisplay.CachedLine> lines; private final int width; public CachedInfo(List<Display.TextDisplay.CachedLine> lines, int width) { this.lines = lines; this.width = width; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #883	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$CachedInfo;
/* 912 */       //   0	8	1	o	Ljava/lang/Object; } public List<Display.TextDisplay.CachedLine> lines() { return this.lines; } public int width() { return this.width; } } public static final class TextRenderState extends Record { private final Component text; private final int lineWidth; public TextRenderState(Component text, int lineWidth, Display.IntInterpolator textOpacity, Display.IntInterpolator backgroundColor, byte flags) { this.text = text; this.lineWidth = lineWidth; this.textOpacity = textOpacity; this.backgroundColor = backgroundColor; this.flags = flags; } private final Display.IntInterpolator textOpacity; private final Display.IntInterpolator backgroundColor; private final byte flags; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #912	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;
/* 912 */       //   0	8	1	o	Ljava/lang/Object; } public Component text() { return this.text; } public int lineWidth() { return this.lineWidth; } public Display.IntInterpolator textOpacity() { return this.textOpacity; } public Display.IntInterpolator backgroundColor() { return this.backgroundColor; } public byte flags() { return this.flags; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface GenericInterpolator<T>
/*     */   {
/*     */     static <T> GenericInterpolator<T> constant(T value) {
/* 926 */       return progress -> value;
/*     */     }
/*     */     T get(float param1Float); }
/*     */   private static final class TransformationInterpolator extends Record implements GenericInterpolator<Transformation> { private final Transformation previous;
/*     */     private final Transformation current;
/*     */     
/* 932 */     private TransformationInterpolator(Transformation previous, Transformation current) { this.previous = previous; this.current = current; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #932	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #932	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$TransformationInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #932	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$TransformationInterpolator;
/* 932 */       //   0	8	1	o	Ljava/lang/Object; } public Transformation previous() { return this.previous; } public Transformation current() { return this.current; }
/*     */     
/*     */     public Transformation get(float progress) {
/* 935 */       if (progress >= 1.0D) {
/* 936 */         return this.current;
/*     */       }
/* 938 */       return this.previous.slerp(this.current, progress);
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IntInterpolator {
/*     */     static IntInterpolator constant(int value) {
/* 945 */       return progress -> value;
/*     */     }
/*     */     int get(float param1Float); }
/*     */   private static final class LinearIntInterpolator extends Record implements IntInterpolator { private final int previous;
/*     */     private final int current;
/*     */     
/* 951 */     private LinearIntInterpolator(int previous, int current) { this.previous = previous; this.current = current; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #951	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #951	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$LinearIntInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #951	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$LinearIntInterpolator;
/* 951 */       //   0	8	1	o	Ljava/lang/Object; } public int previous() { return this.previous; } public int current() { return this.current; }
/*     */     
/*     */     public int get(float progress) {
/* 954 */       return Mth.lerpInt(progress, this.previous, this.current);
/*     */     } }
/*     */   private static final class ColorInterpolator extends Record implements IntInterpolator { private final int previous; private final int current;
/*     */     
/* 958 */     private ColorInterpolator(int previous, int current) { this.previous = previous; this.current = current; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #958	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ColorInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #958	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ColorInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #958	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$ColorInterpolator;
/* 958 */       //   0	8	1	o	Ljava/lang/Object; } public int previous() { return this.previous; } public int current() { return this.current; }
/*     */     
/*     */     public int get(float progress) {
/* 961 */       return ARGB.srgbLerp(progress, this.previous, this.current);
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface FloatInterpolator {
/*     */     static FloatInterpolator constant(float value) {
/* 968 */       return progress -> value;
/*     */     }
/*     */     float get(float param1Float); }
/*     */   private static final class LinearFloatInterpolator extends Record implements FloatInterpolator { private final float previous;
/*     */     private final float current;
/*     */     
/* 974 */     private LinearFloatInterpolator(float previous, float current) { this.previous = previous; this.current = current; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #974	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #974	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #974	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$LinearFloatInterpolator;
/* 974 */       //   0	8	1	o	Ljava/lang/Object; } public float previous() { return this.previous; } public float current() { return this.current; }
/*     */     
/*     */     public float get(float progress) {
/* 977 */       return Mth.lerp(progress, this.previous, this.current);
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LineSplitter {
/*     */     Display.TextDisplay.CachedInfo split(Component param1Component, int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/Display.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */