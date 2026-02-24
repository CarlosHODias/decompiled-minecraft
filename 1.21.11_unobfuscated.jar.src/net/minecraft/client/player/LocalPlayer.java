/*      */ package net.minecraft.client.player;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.stream.StreamSupport;
/*      */ import net.minecraft.client.ClientRecipeBook;
/*      */ import net.minecraft.client.KeyMapping;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.inventory.BookEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.SignEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestInstanceBlockEditScreen;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.resources.sounds.AmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
/*      */ import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.RidingEntitySoundInstance;
/*      */ import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundHandler;
/*      */ import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.StatsCounter;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.TickThrottler;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.player.Abilities;
/*      */ import net.minecraft.world.entity.player.Input;
/*      */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*      */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*      */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.UseEffects;
/*      */ import net.minecraft.world.item.component.WritableBookContent;
/*      */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.block.Portal;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class LocalPlayer
/*      */   extends AbstractClientPlayer {
/*  111 */   public static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int POSITION_REMINDER_INTERVAL = 20;
/*      */   
/*      */   private static final int WATER_VISION_MAX_TIME = 600;
/*      */   private static final int WATER_VISION_QUICK_TIME = 100;
/*      */   private static final float WATER_VISION_QUICK_PERCENT = 0.6F;
/*      */   private static final double SUFFOCATING_COLLISION_CHECK_SCALE = 0.35D;
/*      */   private static final double MINOR_COLLISION_ANGLE_THRESHOLD_RADIAN = 0.13962633907794952D;
/*      */   public final ClientPacketListener connection;
/*      */   private final StatsCounter stats;
/*      */   private final ClientRecipeBook recipeBook;
/*  123 */   private final TickThrottler dropSpamThrottler = new TickThrottler(20, 1280);
/*      */   
/*  125 */   private final List<AmbientSoundHandler> ambientSoundHandlers = Lists.newArrayList();
/*      */   
/*  127 */   private PermissionSet permissions = PermissionSet.NO_PERMISSIONS;
/*      */   
/*      */   private double xLast;
/*      */   
/*      */   private double yLast;
/*      */   private double zLast;
/*      */   private float yRotLast;
/*      */   private float xRotLast;
/*      */   private boolean lastOnGround;
/*      */   private boolean lastHorizontalCollision;
/*      */   private boolean crouching;
/*      */   private boolean wasSprinting;
/*      */   private int positionReminder;
/*      */   private boolean flashOnSetHealth;
/*  141 */   public ClientInput input = new ClientInput();
/*      */   private Input lastSentInput;
/*      */   protected final Minecraft minecraft;
/*      */   protected int sprintTriggerTime;
/*      */   private static final int EXPERIENCE_DISPLAY_UNREADY_TO_SET = -2147483648;
/*      */   private static final int EXPERIENCE_DISPLAY_READY_TO_SET = -2147483647;
/*  147 */   public int experienceDisplayStartTick = Integer.MIN_VALUE;
/*      */   
/*      */   public float yBob;
/*      */   
/*      */   public float xBob;
/*      */   
/*      */   public float yBobO;
/*      */   
/*      */   public float xBobO;
/*      */   
/*      */   private int jumpRidingTicks;
/*      */   
/*      */   private float jumpRidingScale;
/*      */   
/*      */   public float portalEffectIntensity;
/*      */   public float oPortalEffectIntensity;
/*      */   private boolean startedUsingItem;
/*      */   private InteractionHand usingItemHand;
/*      */   private boolean handsBusy;
/*      */   private boolean autoJumpEnabled = true;
/*      */   private int autoJumpTime;
/*      */   private boolean wasFallFlying;
/*      */   private int waterVisionTime;
/*      */   private boolean showDeathScreen = true;
/*      */   private boolean doLimitedCrafting = false;
/*      */   
/*      */   public LocalPlayer(Minecraft minecraft, ClientLevel level, ClientPacketListener connection, StatsCounter stats, ClientRecipeBook recipeBook, Input lastSentInput, boolean wasSprinting) {
/*  174 */     super(level, connection.getLocalGameProfile());
/*  175 */     this.minecraft = minecraft;
/*  176 */     this.connection = connection;
/*  177 */     this.stats = stats;
/*  178 */     this.recipeBook = recipeBook;
/*  179 */     this.lastSentInput = lastSentInput;
/*  180 */     this.wasSprinting = wasSprinting;
/*  181 */     this.ambientSoundHandlers.add(new UnderwaterAmbientSoundHandler(this, minecraft.getSoundManager()));
/*  182 */     this.ambientSoundHandlers.add(new BubbleColumnAmbientSoundHandler(this));
/*  183 */     this.ambientSoundHandlers.add(new BiomeAmbientSoundsHandler(this, minecraft.getSoundManager()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void heal(float heal) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/*  193 */     if (!super.startRiding(entity, force, sendEventAndTriggers)) {
/*  194 */       return false;
/*      */     }
/*  196 */     if (entity instanceof AbstractMinecart) { AbstractMinecart minecart = (AbstractMinecart)entity;
/*      */ 
/*      */ 
/*      */       
/*  200 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingMinecartSoundInstance(this, minecart, true, SoundEvents.MINECART_INSIDE_UNDERWATER, 0.0F, 0.75F, 1.0F));
/*  201 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingMinecartSoundInstance(this, minecart, false, SoundEvents.MINECART_INSIDE, 0.0F, 0.75F, 1.0F)); }
/*  202 */     else if (entity instanceof HappyGhast) { HappyGhast happyGhast = (HappyGhast)entity;
/*  203 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingEntitySoundInstance(this, (Entity)happyGhast, false, SoundEvents.HAPPY_GHAST_RIDING, happyGhast.getSoundSource(), 0.0F, 1.0F, 5.0F)); }
/*  204 */     else if (entity instanceof AbstractNautilus) { AbstractNautilus nautilus = (AbstractNautilus)entity;
/*  205 */       this.minecraft.getSoundManager().play((SoundInstance)new RidingEntitySoundInstance(this, (Entity)nautilus, true, SoundEvents.NAUTILUS_RIDING, nautilus.getSoundSource(), 0.0F, 1.0F, 5.0F)); }
/*      */     
/*  207 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeVehicle() {
/*  212 */     super.removeVehicle();
/*  213 */     this.handsBusy = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getViewXRot(float a) {
/*  218 */     return getXRot();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getViewYRot(float a) {
/*  223 */     if (isPassenger()) {
/*  224 */       return super.getViewYRot(a);
/*      */     }
/*  226 */     return getYRot();
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  231 */     if (!this.connection.hasClientLoaded()) {
/*      */       return;
/*      */     }
/*      */     
/*  235 */     this.dropSpamThrottler.tick();
/*  236 */     super.tick();
/*      */     
/*  238 */     if (!this.lastSentInput.equals(this.input.keyPresses)) {
/*  239 */       this.connection.send((Packet)new ServerboundPlayerInputPacket(this.input.keyPresses));
/*  240 */       this.lastSentInput = this.input.keyPresses;
/*      */     } 
/*      */     
/*  243 */     if (isPassenger()) {
/*  244 */       this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  245 */       Entity vehicle = getRootVehicle();
/*  246 */       if (vehicle != this && vehicle.isLocalInstanceAuthoritative()) {
/*  247 */         this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(vehicle));
/*  248 */         sendIsSprintingIfNeeded();
/*      */       } 
/*      */     } else {
/*  251 */       sendPosition();
/*      */     } 
/*      */     
/*  254 */     for (AmbientSoundHandler soundHandler : this.ambientSoundHandlers) {
/*  255 */       soundHandler.tick();
/*      */     }
/*      */   }
/*      */   
/*      */   public float getCurrentMood() {
/*  260 */     for (AmbientSoundHandler ambientSoundHandler : this.ambientSoundHandlers) {
/*  261 */       if (ambientSoundHandler instanceof BiomeAmbientSoundsHandler) {
/*  262 */         return ((BiomeAmbientSoundsHandler)ambientSoundHandler).getMoodiness();
/*      */       }
/*      */     } 
/*  265 */     return 0.0F;
/*      */   }
/*      */   
/*      */   private void sendPosition() {
/*  269 */     sendIsSprintingIfNeeded();
/*      */     
/*  271 */     if (isControlledCamera()) {
/*  272 */       double deltaX = getX() - this.xLast;
/*  273 */       double deltaY = getY() - this.yLast;
/*  274 */       double deltaZ = getZ() - this.zLast;
/*      */       
/*  276 */       double deltaYRot = (getYRot() - this.yRotLast);
/*  277 */       double deltaXRot = (getXRot() - this.xRotLast);
/*      */       
/*  279 */       this.positionReminder++;
/*      */       
/*  281 */       boolean move = (Mth.lengthSquared(deltaX, deltaY, deltaZ) > Mth.square(2.0E-4D) || this.positionReminder >= 20);
/*  282 */       boolean rot = (deltaYRot != 0.0D || deltaXRot != 0.0D);
/*      */       
/*  284 */       if (move && rot) {
/*  285 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(position(), getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  286 */       } else if (move) {
/*  287 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.Pos(position(), onGround(), this.horizontalCollision));
/*  288 */       } else if (rot) {
/*  289 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(getYRot(), getXRot(), onGround(), this.horizontalCollision));
/*  290 */       } else if (this.lastOnGround != onGround() || this.lastHorizontalCollision != this.horizontalCollision) {
/*  291 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.StatusOnly(onGround(), this.horizontalCollision));
/*      */       } 
/*      */       
/*  294 */       if (move) {
/*  295 */         this.xLast = getX();
/*  296 */         this.yLast = getY();
/*  297 */         this.zLast = getZ();
/*  298 */         this.positionReminder = 0;
/*      */       } 
/*  300 */       if (rot) {
/*  301 */         this.yRotLast = getYRot();
/*  302 */         this.xRotLast = getXRot();
/*      */       } 
/*  304 */       this.lastOnGround = onGround();
/*  305 */       this.lastHorizontalCollision = this.horizontalCollision;
/*      */       
/*  307 */       this.autoJumpEnabled = (Boolean)this.minecraft.options.autoJump().get();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendIsSprintingIfNeeded() {
/*  312 */     boolean isSprinting = isSprinting();
/*  313 */     if (isSprinting != this.wasSprinting) {
/*  314 */       ServerboundPlayerCommandPacket.Action action = isSprinting ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
/*  315 */       this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, action));
/*  316 */       this.wasSprinting = isSprinting;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean drop(boolean all) {
/*  321 */     ServerboundPlayerActionPacket.Action action = all ? ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS : ServerboundPlayerActionPacket.Action.DROP_ITEM;
/*  322 */     ItemStack prediction = getInventory().removeFromSelected(all);
/*  323 */     this.connection.send((Packet)new ServerboundPlayerActionPacket(action, BlockPos.ZERO, Direction.DOWN));
/*  324 */     return !prediction.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public void swing(InteractionHand hand) {
/*  329 */     super.swing(hand);
/*  330 */     this.connection.send((Packet)new ServerboundSwingPacket(hand));
/*      */   }
/*      */   
/*      */   public void respawn() {
/*  334 */     this.connection.send((Packet)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
/*  335 */     KeyMapping.resetToggleKeys();
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeContainer() {
/*  340 */     this.connection.send((Packet)new ServerboundContainerClosePacket(this.containerMenu.containerId));
/*  341 */     clientSideCloseContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clientSideCloseContainer() {
/*  346 */     super.closeContainer();
/*  347 */     this.minecraft.setScreen(null);
/*      */   }
/*      */   
/*      */   public void hurtTo(float newHealth) {
/*  351 */     if (this.flashOnSetHealth) {
/*  352 */       float dmg = getHealth() - newHealth;
/*  353 */       if (dmg <= 0.0F) {
/*  354 */         setHealth(newHealth);
/*  355 */         if (dmg < 0.0F) {
/*  356 */           this.invulnerableTime = 10;
/*      */         }
/*      */       } else {
/*  359 */         this.lastHurt = dmg;
/*  360 */         this.invulnerableTime = 20;
/*  361 */         setHealth(newHealth);
/*  362 */         this.hurtDuration = 10;
/*  363 */         this.hurtTime = this.hurtDuration;
/*      */       } 
/*      */     } else {
/*  366 */       setHealth(newHealth);
/*  367 */       this.flashOnSetHealth = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdateAbilities() {
/*  373 */     this.connection.send((Packet)new ServerboundPlayerAbilitiesPacket(getAbilities()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setReducedDebugInfo(boolean reducedDebugInfo) {
/*  378 */     super.setReducedDebugInfo(reducedDebugInfo);
/*  379 */     this.minecraft.debugEntries.rebuildCurrentList();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLocalPlayer() {
/*  384 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSuppressingSlidingDownLadder() {
/*  389 */     return (!(getAbilities()).flying && super.isSuppressingSlidingDownLadder());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canSpawnSprintParticle() {
/*  394 */     return (!(getAbilities()).flying && super.canSpawnSprintParticle());
/*      */   }
/*      */   
/*      */   protected void sendRidingJump() {
/*  398 */     this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.START_RIDING_JUMP, Mth.floor(getJumpRidingScale() * 100.0F)));
/*      */   }
/*      */   
/*      */   public void sendOpenInventory() {
/*  402 */     this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
/*      */   }
/*      */   
/*      */   public StatsCounter getStats() {
/*  406 */     return this.stats;
/*      */   }
/*      */   
/*      */   public ClientRecipeBook getRecipeBook() {
/*  410 */     return this.recipeBook;
/*      */   }
/*      */   
/*      */   public void removeRecipeHighlight(RecipeDisplayId recipe) {
/*  414 */     if (this.recipeBook.willHighlight(recipe)) {
/*  415 */       this.recipeBook.removeHighlight(recipe);
/*  416 */       this.connection.send((Packet)new ServerboundRecipeBookSeenRecipePacket(recipe));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public PermissionSet permissions() {
/*  422 */     return this.permissions;
/*      */   }
/*      */   
/*      */   public void setPermissions(PermissionSet permissions) {
/*  426 */     this.permissions = permissions;
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayClientMessage(Component component, boolean overlayMessage) {
/*  431 */     this.minecraft.getChatListener().handleSystemMessage(component, overlayMessage);
/*      */   }
/*      */   
/*      */   private void moveTowardsClosestSpace(double x, double z) {
/*  435 */     BlockPos pos = BlockPos.containing(x, getY(), z);
/*      */     
/*  437 */     if (!suffocatesAt(pos)) {
/*      */       return;
/*      */     }
/*      */     
/*  441 */     double xd = x - pos.getX();
/*  442 */     double zd = z - pos.getZ();
/*      */     
/*  444 */     Direction dir = null;
/*  445 */     double closest = Double.MAX_VALUE;
/*      */     
/*  447 */     Direction[] directions = { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH };
/*  448 */     for (Direction direction : directions) {
/*  449 */       double axisDistance = direction.getAxis().choose(xd, 0.0D, zd);
/*  450 */       double distanceToEdge = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0D - axisDistance) : axisDistance;
/*  451 */       if (distanceToEdge < closest && !suffocatesAt(pos.relative(direction))) {
/*  452 */         closest = distanceToEdge;
/*  453 */         dir = direction;
/*      */       } 
/*      */     } 
/*      */     
/*  457 */     if (dir != null) {
/*      */       
/*  459 */       Vec3 oldMovement = getDeltaMovement();
/*  460 */       if (dir.getAxis() == Direction.Axis.X) {
/*  461 */         setDeltaMovement(0.1D * dir.getStepX(), oldMovement.y, oldMovement.z);
/*      */       } else {
/*  463 */         setDeltaMovement(oldMovement.x, oldMovement.y, 0.1D * dir.getStepZ());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean suffocatesAt(BlockPos pos) {
/*  470 */     AABB boundingBox = getBoundingBox();
/*  471 */     AABB testArea = new AABB(pos.getX(), boundingBox.minY, pos.getZ(), pos.getX() + 1.0D, boundingBox.maxY, pos.getZ() + 1.0D).deflate(1.0E-7D);
/*  472 */     return level().collidesWithSuffocatingBlock((Entity)this, testArea);
/*      */   }
/*      */   
/*      */   public void setExperienceValues(float experienceProgress, int totalExp, int experienceLevel) {
/*  476 */     if (experienceProgress != this.experienceProgress) {
/*  477 */       setExperienceDisplayStartTickToTickCount();
/*      */     }
/*  479 */     this.experienceProgress = experienceProgress;
/*  480 */     this.totalExperience = totalExp;
/*  481 */     this.experienceLevel = experienceLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setExperienceDisplayStartTickToTickCount() {
/*  486 */     if (this.experienceDisplayStartTick == Integer.MIN_VALUE) {
/*  487 */       this.experienceDisplayStartTick = -2147483647;
/*      */     } else {
/*  489 */       this.experienceDisplayStartTick = this.tickCount;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  495 */     switch (id) { case 24:
/*  496 */         setPermissions(PermissionSet.NO_PERMISSIONS); break;
/*  497 */       case 25: setPermissions((PermissionSet)LevelBasedPermissionSet.MODERATOR); break;
/*  498 */       case 26: setPermissions((PermissionSet)LevelBasedPermissionSet.GAMEMASTER); break;
/*  499 */       case 27: setPermissions((PermissionSet)LevelBasedPermissionSet.ADMIN); break;
/*  500 */       case 28: setPermissions((PermissionSet)LevelBasedPermissionSet.OWNER); break;
/*  501 */       default: super.handleEntityEvent(id);
/*      */         break; }
/*      */   
/*      */   }
/*      */   public void setShowDeathScreen(boolean show) {
/*  506 */     this.showDeathScreen = show;
/*      */   }
/*      */   
/*      */   public boolean shouldShowDeathScreen() {
/*  510 */     return this.showDeathScreen;
/*      */   }
/*      */   
/*      */   public void setDoLimitedCrafting(boolean value) {
/*  514 */     this.doLimitedCrafting = value;
/*      */   }
/*      */   
/*      */   public boolean getDoLimitedCrafting() {
/*  518 */     return this.doLimitedCrafting;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSound(SoundEvent sound, float volume, float pitch) {
/*  523 */     level().playLocalSound(getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void startUsingItem(InteractionHand hand) {
/*  528 */     ItemStack itemStack = getItemInHand(hand);
/*  529 */     if (itemStack.isEmpty() || isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/*  533 */     super.startUsingItem(hand);
/*      */     
/*  535 */     this.startedUsingItem = true;
/*  536 */     this.usingItemHand = hand;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUsingItem() {
/*  541 */     return this.startedUsingItem;
/*      */   }
/*      */   
/*      */   private boolean isSlowDueToUsingItem() {
/*  545 */     return (isUsingItem() && !((UseEffects)this.useItem.getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT)).canSprint());
/*      */   }
/*      */   
/*      */   private float itemUseSpeedMultiplier() {
/*  549 */     return ((UseEffects)this.useItem.getOrDefault(DataComponents.USE_EFFECTS, UseEffects.DEFAULT)).speedMultiplier();
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopUsingItem() {
/*  554 */     super.stopUsingItem();
/*  555 */     this.startedUsingItem = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionHand getUsedItemHand() {
/*  561 */     return Objects.<InteractionHand>requireNonNullElse(this.usingItemHand, InteractionHand.MAIN_HAND);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  566 */     super.onSyncedDataUpdated(accessor);
/*      */     
/*  568 */     if (DATA_LIVING_ENTITY_FLAGS.equals(accessor)) {
/*  569 */       boolean serverUsingItem = (((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 0x1) > 0);
/*  570 */       InteractionHand serverUsingHand = (((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
/*      */       
/*  572 */       if (serverUsingItem && !this.startedUsingItem) {
/*  573 */         startUsingItem(serverUsingHand);
/*  574 */       } else if (!serverUsingItem && this.startedUsingItem) {
/*  575 */         stopUsingItem();
/*      */       } 
/*      */     } 
/*  578 */     if (DATA_SHARED_FLAGS_ID.equals(accessor) && 
/*  579 */       isFallFlying() && !this.wasFallFlying) {
/*  580 */       this.minecraft.getSoundManager().play((SoundInstance)new ElytraOnPlayerSoundInstance(this));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public PlayerRideableJumping jumpableVehicle() {
/*  586 */     Entity entity = getControlledVehicle(); if (entity instanceof PlayerRideableJumping) { PlayerRideableJumping playerRideableJumping = (PlayerRideableJumping)entity; if (playerRideableJumping.canJump()); }  return null;
/*      */   }
/*      */   
/*      */   public float getJumpRidingScale() {
/*  590 */     return this.jumpRidingScale;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/*  595 */     return this.minecraft.isTextFilteringEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {
/*  600 */     if (sign instanceof HangingSignBlockEntity) { HangingSignBlockEntity hangingSign = (HangingSignBlockEntity)sign;
/*  601 */       this.minecraft.setScreen((Screen)new HangingSignEditScreen((SignBlockEntity)hangingSign, isFrontText, this.minecraft.isTextFilteringEnabled())); }
/*      */     else
/*  603 */     { this.minecraft.setScreen((Screen)new SignEditScreen(sign, isFrontText, this.minecraft.isTextFilteringEnabled())); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void openMinecartCommandBlock(MinecartCommandBlock commandBlock) {
/*  609 */     this.minecraft.setScreen((Screen)new MinecartCommandBlockEditScreen(commandBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity commandBlock) {
/*  614 */     this.minecraft.setScreen((Screen)new CommandBlockEditScreen(commandBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openStructureBlock(StructureBlockEntity structureBlock) {
/*  619 */     this.minecraft.setScreen((Screen)new StructureBlockEditScreen(structureBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openTestBlock(TestBlockEntity testBlock) {
/*  624 */     this.minecraft.setScreen((Screen)new TestBlockEditScreen(testBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openTestInstanceBlock(TestInstanceBlockEntity testInstanceBlock) {
/*  629 */     this.minecraft.setScreen((Screen)new TestInstanceBlockEditScreen(testInstanceBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openJigsawBlock(JigsawBlockEntity jigsawBlock) {
/*  634 */     this.minecraft.setScreen((Screen)new JigsawBlockEditScreen(jigsawBlock));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openDialog(Holder<Dialog> dialog) {
/*  639 */     this.connection.showDialog(dialog, this.minecraft.screen);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {
/*  644 */     WritableBookContent content = (WritableBookContent)itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
/*  645 */     if (content != null) {
/*  646 */       this.minecraft.setScreen((Screen)new BookEditScreen(this, itemStack, hand, content));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void crit(Entity entity) {
/*  652 */     this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.CRIT);
/*      */   }
/*      */ 
/*      */   
/*      */   public void magicCrit(Entity entity) {
/*  657 */     this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.ENCHANTED_HIT);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShiftKeyDown() {
/*  662 */     return this.input.keyPresses.shift();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCrouching() {
/*  667 */     return this.crouching;
/*      */   }
/*      */   
/*      */   public boolean isMovingSlowly() {
/*  671 */     return (isCrouching() || isVisuallyCrawling());
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyInput() {
/*  676 */     if (isControlledCamera()) {
/*  677 */       Vec2 modifiedInput = modifyInput(this.input.getMoveVector());
/*  678 */       this.xxa = modifiedInput.x;
/*  679 */       this.zza = modifiedInput.y;
/*  680 */       this.jumping = this.input.keyPresses.jump();
/*  681 */       this.yBobO = this.yBob;
/*  682 */       this.xBobO = this.xBob;
/*  683 */       this.xBob += (getXRot() - this.xBob) * 0.5F;
/*  684 */       this.yBob += (getYRot() - this.yBob) * 0.5F;
/*      */     } else {
/*  686 */       super.applyInput();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Vec2 modifyInput(Vec2 input) {
/*  702 */     if (input.lengthSquared() == 0.0F) {
/*  703 */       return input;
/*      */     }
/*      */     
/*  706 */     Vec2 newInput = input.scale(0.98F);
/*  707 */     if (isUsingItem() && !isPassenger()) {
/*  708 */       newInput = newInput.scale(itemUseSpeedMultiplier());
/*      */     }
/*      */     
/*  711 */     if (isMovingSlowly()) {
/*  712 */       float sneakingMovementFactor = (float)getAttributeValue(Attributes.SNEAKING_SPEED);
/*  713 */       newInput = newInput.scale(sneakingMovementFactor);
/*      */     } 
/*      */     
/*  716 */     return modifyInputSpeedForSquareMovement(newInput);
/*      */   }
/*      */   
/*      */   private static Vec2 modifyInputSpeedForSquareMovement(Vec2 input) {
/*  720 */     float length = input.length();
/*  721 */     if (length <= 0.0F) {
/*  722 */       return input;
/*      */     }
/*      */     
/*  725 */     Vec2 direction = input.scale(1.0F / length);
/*  726 */     float distanceToUnitSquare = distanceToUnitSquare(direction);
/*      */     
/*  728 */     float modifiedLength = Math.min(length * distanceToUnitSquare, 1.0F);
/*  729 */     return direction.scale(modifiedLength);
/*      */   }
/*      */   
/*      */   private static float distanceToUnitSquare(Vec2 direction) {
/*  733 */     float directionX = Math.abs(direction.x);
/*  734 */     float directionY = Math.abs(direction.y);
/*  735 */     float tan = (directionY > directionX) ? (directionX / directionY) : (directionY / directionX);
/*  736 */     return Mth.sqrt(1.0F + Mth.square(tan));
/*      */   }
/*      */   
/*      */   protected boolean isControlledCamera() {
/*  740 */     return (this.minecraft.getCameraEntity() == this);
/*      */   }
/*      */   
/*      */   public void resetPos() {
/*  744 */     setPose(Pose.STANDING);
/*  745 */     if (level() != null) {
/*  746 */       double testY = getY();
/*  747 */       while (testY > level().getMinY() && testY <= level().getMaxY()) {
/*  748 */         setPos(getX(), testY, getZ());
/*  749 */         if (level().noCollision((Entity)this)) {
/*      */           break;
/*      */         }
/*  752 */         testY++;
/*      */       } 
/*  754 */       setDeltaMovement(Vec3.ZERO);
/*  755 */       setXRot(0.0F);
/*      */     } 
/*      */     
/*  758 */     setHealth(getMaxHealth());
/*  759 */     this.deathTime = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  764 */     if (this.sprintTriggerTime > 0) {
/*  765 */       this.sprintTriggerTime--;
/*      */     }
/*      */     
/*  768 */     if (!(this.minecraft.screen instanceof net.minecraft.client.gui.screens.LevelLoadingScreen)) {
/*  769 */       handlePortalTransitionEffect((getActivePortalLocalTransition() == Portal.Transition.CONFUSION));
/*  770 */       processPortalCooldown();
/*      */     } 
/*      */     
/*  773 */     boolean wasJumping = this.input.keyPresses.jump();
/*  774 */     boolean wasShiftKeyDown = this.input.keyPresses.shift();
/*  775 */     boolean hasForwardImpulse = this.input.hasForwardImpulse();
/*      */     
/*  777 */     Abilities abilities = getAbilities();
/*  778 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  783 */       .crouching = (!abilities.flying && !isSwimming() && !isPassenger() && canPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING) && (isShiftKeyDown() || (!isSleeping() && !canPlayerFitWithinBlocksAndEntitiesWhen(Pose.STANDING))));
/*      */     
/*  785 */     this.input.tick();
/*  786 */     this.minecraft.getTutorial().onInput(this.input);
/*      */     
/*      */     boolean wasAutoJump = false;
/*  789 */     if (this.autoJumpTime > 0) {
/*  790 */       this.autoJumpTime--;
/*  791 */       wasAutoJump = true;
/*  792 */       this.input.makeJump();
/*      */     } 
/*      */     
/*  795 */     if (!this.noPhysics) {
/*  796 */       moveTowardsClosestSpace(getX() - getBbWidth() * 0.35D, getZ() + getBbWidth() * 0.35D);
/*  797 */       moveTowardsClosestSpace(getX() - getBbWidth() * 0.35D, getZ() - getBbWidth() * 0.35D);
/*  798 */       moveTowardsClosestSpace(getX() + getBbWidth() * 0.35D, getZ() - getBbWidth() * 0.35D);
/*  799 */       moveTowardsClosestSpace(getX() + getBbWidth() * 0.35D, getZ() + getBbWidth() * 0.35D);
/*      */     } 
/*      */     
/*  802 */     if (wasShiftKeyDown || (isSlowDueToUsingItem() && !isPassenger()) || this.input.keyPresses.backward()) {
/*  803 */       this.sprintTriggerTime = 0;
/*      */     }
/*      */     
/*  806 */     if (canStartSprinting()) {
/*  807 */       if (!hasForwardImpulse) {
/*  808 */         if (this.sprintTriggerTime > 0) {
/*  809 */           setSprinting(true);
/*      */         } else {
/*  811 */           this.sprintTriggerTime = (Integer)this.minecraft.options.sprintWindow().get();
/*      */         } 
/*      */       }
/*      */       
/*  815 */       if (this.input.keyPresses.sprint()) {
/*  816 */         setSprinting(true);
/*      */       }
/*      */     } 
/*      */     
/*  820 */     if (isSprinting()) {
/*  821 */       if (isSwimming()) {
/*  822 */         if (shouldStopSwimSprinting()) {
/*  823 */           setSprinting(false);
/*      */         }
/*  825 */       } else if (shouldStopRunSprinting()) {
/*  826 */         setSprinting(false);
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean justToggledCreativeFlight = false;
/*  831 */     if (abilities.mayfly) {
/*  832 */       if (this.minecraft.gameMode.isSpectator()) {
/*  833 */         if (!abilities.flying) {
/*  834 */           abilities.flying = true;
/*  835 */           justToggledCreativeFlight = true;
/*  836 */           onUpdateAbilities();
/*      */         } 
/*  838 */       } else if (!wasJumping && this.input.keyPresses.jump() && !wasAutoJump) {
/*  839 */         if (this.jumpTriggerTime == 0) {
/*  840 */           this.jumpTriggerTime = 7;
/*  841 */         } else if (!isSwimming() && (getVehicle() == null || jumpableVehicle() != null)) {
/*  842 */           abilities.flying = !abilities.flying;
/*      */           
/*  844 */           if (abilities.flying && onGround()) {
/*  845 */             jumpFromGround();
/*      */           }
/*  847 */           justToggledCreativeFlight = true;
/*  848 */           onUpdateAbilities();
/*  849 */           this.jumpTriggerTime = 0;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  854 */     if (this.input.keyPresses.jump() && !justToggledCreativeFlight && !wasJumping && !onClimbable() && 
/*  855 */       tryToStartFallFlying()) {
/*  856 */       this.connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
/*      */     }
/*      */     
/*  859 */     this.wasFallFlying = isFallFlying();
/*      */     
/*  861 */     if (isInWater() && this.input.keyPresses.shift() && isAffectedByFluids()) {
/*  862 */       goDownInWater();
/*      */     }
/*      */     
/*  865 */     if (isEyeInFluid(FluidTags.WATER)) {
/*  866 */       int speed = isSpectator() ? 10 : 1;
/*  867 */       this.waterVisionTime = Mth.clamp(this.waterVisionTime + speed, 0, 600);
/*  868 */     } else if (this.waterVisionTime > 0) {
/*  869 */       isEyeInFluid(FluidTags.WATER);
/*  870 */       this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
/*      */     } 
/*      */     
/*  873 */     if (abilities.flying && isControlledCamera()) {
/*      */       
/*  875 */       int inputYa = 0;
/*      */       
/*  877 */       if (this.input.keyPresses.shift()) {
/*  878 */         inputYa--;
/*      */       }
/*  880 */       if (this.input.keyPresses.jump()) {
/*  881 */         inputYa++;
/*      */       }
/*      */       
/*  884 */       if (inputYa != 0) {
/*  885 */         setDeltaMovement(getDeltaMovement().add(0.0D, (inputYa * abilities.getFlyingSpeed() * 3.0F), 0.0D));
/*      */       }
/*      */     } 
/*      */     
/*  889 */     PlayerRideableJumping jumpableVehicle = jumpableVehicle();
/*  890 */     if (jumpableVehicle != null && jumpableVehicle.getJumpCooldown() == 0) {
/*  891 */       if (this.jumpRidingTicks < 0) {
/*  892 */         this.jumpRidingTicks++;
/*  893 */         if (this.jumpRidingTicks == 0)
/*      */         {
/*  895 */           this.jumpRidingScale = 0.0F;
/*      */         }
/*      */       } 
/*  898 */       if (wasJumping && !this.input.keyPresses.jump()) {
/*      */         
/*  900 */         this.jumpRidingTicks = -10;
/*  901 */         jumpableVehicle.onPlayerJump(Mth.floor(getJumpRidingScale() * 100.0F));
/*  902 */         sendRidingJump();
/*  903 */       } else if (!wasJumping && this.input.keyPresses.jump()) {
/*      */         
/*  905 */         this.jumpRidingTicks = 0;
/*  906 */         this.jumpRidingScale = 0.0F;
/*  907 */       } else if (wasJumping) {
/*      */         
/*  909 */         this.jumpRidingTicks++;
/*  910 */         if (this.jumpRidingTicks < 10) {
/*  911 */           this.jumpRidingScale = this.jumpRidingTicks * 0.1F;
/*      */         } else {
/*  913 */           this.jumpRidingScale = 0.8F + 2.0F / (this.jumpRidingTicks - 9) * 0.1F;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  917 */       this.jumpRidingScale = 0.0F;
/*      */     } 
/*      */     
/*  920 */     super.aiStep();
/*  921 */     if (onGround() && abilities.flying && !this.minecraft.gameMode.isSpectator()) {
/*  922 */       abilities.flying = false;
/*  923 */       onUpdateAbilities();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldStopRunSprinting() {
/*  928 */     return (!isSprintingPossible((getAbilities()).flying) || 
/*  929 */       !this.input.hasForwardImpulse() || (this.horizontalCollision && !this.minorHorizontalCollision));
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean shouldStopSwimSprinting() {
/*  934 */     return (!isSprintingPossible(true) || 
/*  935 */       !isInWater() || (
/*  936 */       !this.input.hasForwardImpulse() && !onGround() && !this.input.keyPresses.shift()));
/*      */   }
/*      */   
/*      */   public Portal.Transition getActivePortalLocalTransition() {
/*  940 */     return (this.portalProcess == null) ? Portal.Transition.NONE : this.portalProcess.getPortalLocalTransition();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void tickDeath() {
/*  946 */     this.deathTime++;
/*  947 */     if (this.deathTime == 20) {
/*  948 */       remove(Entity.RemovalReason.KILLED);
/*      */     }
/*      */   }
/*      */   
/*      */   private void handlePortalTransitionEffect(boolean active) {
/*  953 */     this.oPortalEffectIntensity = this.portalEffectIntensity;
/*  954 */     float step = 0.0F;
/*      */     
/*  956 */     if (active && this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
/*  957 */       if (this.minecraft.screen != null && !this.minecraft.screen.isAllowedInPortal()) {
/*  958 */         if (this.minecraft.screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen) {
/*  959 */           closeContainer();
/*      */         }
/*  961 */         this.minecraft.setScreen(null);
/*      */       } 
/*      */       
/*  964 */       if (this.portalEffectIntensity == 0.0F) {
/*  965 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRIGGER, this.random.nextFloat() * 0.4F + 0.8F, 0.25F));
/*      */       }
/*  967 */       step = 0.0125F;
/*  968 */       this.portalProcess.setAsInsidePortalThisTick(false);
/*  969 */     } else if (this.portalEffectIntensity > 0.0F) {
/*  970 */       step = -0.05F;
/*      */     } 
/*      */     
/*  973 */     this.portalEffectIntensity = Mth.clamp(this.portalEffectIntensity + step, 0.0F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/*  978 */     super.rideTick();
/*  979 */     this.handsBusy = false;
/*      */     
/*  981 */     Entity entity = getControlledVehicle(); if (entity instanceof AbstractBoat) { AbstractBoat boat = (AbstractBoat)entity;
/*  982 */       boat.setInput(this.input.keyPresses.left(), this.input.keyPresses.right(), this.input.keyPresses.forward(), this.input.keyPresses.backward());
/*      */       
/*  984 */       this.handsBusy |= (this.input.keyPresses.left() || this.input.keyPresses.right() || this.input.keyPresses.forward() || this.input.keyPresses.backward()) ? true : false; }
/*      */   
/*      */   }
/*      */   
/*      */   public boolean isHandsBusy() {
/*  989 */     return this.handsBusy;
/*      */   }
/*      */ 
/*      */   
/*      */   public void move(MoverType moverType, Vec3 delta) {
/*  994 */     double prevX = getX();
/*  995 */     double prevZ = getZ();
/*  996 */     super.move(moverType, delta);
/*      */     
/*  998 */     float deltaX = (float)(getX() - prevX);
/*  999 */     float deltaZ = (float)(getZ() - prevZ);
/* 1000 */     updateAutoJump(deltaX, deltaZ);
/*      */     
/* 1002 */     addWalkedDistance(Mth.length(deltaX, deltaZ) * 0.6F);
/*      */   }
/*      */   
/*      */   public boolean isAutoJumpEnabled() {
/* 1006 */     return this.autoJumpEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldRotateWithMinecart() {
/* 1011 */     return (Boolean)this.minecraft.options.rotateWithMinecart().get();
/*      */   }
/*      */   
/*      */   protected void updateAutoJump(float xa, float za) {
/* 1015 */     if (!canAutoJump()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1050 */     Vec3 moveBegin = position();
/* 1051 */     Vec3 moveEnd = moveBegin.add(xa, 0.0D, za);
/* 1052 */     Vec3 moveDiff = new Vec3(xa, 0.0D, za);
/*      */ 
/*      */     
/* 1055 */     float currentSpeed = getSpeed();
/* 1056 */     float moveDistSq = (float)moveDiff.lengthSqr();
/* 1057 */     if (moveDistSq <= 0.001F) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1062 */       Vec2 move = this.input.getMoveVector();
/* 1063 */       float inputXa = currentSpeed * move.x;
/* 1064 */       float inputZa = currentSpeed * move.y;
/*      */ 
/*      */       
/* 1067 */       float sin = Mth.sin((getYRot() * 0.017453292F));
/* 1068 */       float cos = Mth.cos((getYRot() * 0.017453292F));
/* 1069 */       moveDiff = new Vec3((inputXa * cos - inputZa * sin), moveDiff.y, (inputZa * cos + inputXa * sin));
/*      */       
/* 1071 */       moveDistSq = (float)moveDiff.lengthSqr();
/*      */ 
/*      */       
/* 1074 */       if (moveDistSq <= 0.001F) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1080 */     float moveDistInverted = Mth.invSqrt(moveDistSq);
/* 1081 */     Vec3 moveDir = moveDiff.scale(moveDistInverted);
/*      */ 
/*      */ 
/*      */     
/* 1085 */     Vec3 facingDir3 = getForward();
/* 1086 */     float facingVsMovingDotProduct2 = (float)(facingDir3.x * moveDir.x + facingDir3.z * moveDir.z);
/* 1087 */     if (facingVsMovingDotProduct2 < -0.15F) {
/*      */       return;
/*      */     }
/*      */     
/* 1091 */     CollisionContext context = CollisionContext.of((Entity)this);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1096 */     BlockPos ceilingPos = BlockPos.containing(getX(), (getBoundingBox()).maxY, getZ());
/* 1097 */     BlockState aboveBlock1 = level().getBlockState(ceilingPos);
/* 1098 */     if (!aboveBlock1.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1102 */     ceilingPos = ceilingPos.above();
/* 1103 */     BlockState aboveBlock2 = level().getBlockState(ceilingPos);
/* 1104 */     if (!aboveBlock2.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1115 */     float lookAheadSteps = 7.0F;
/* 1116 */     float jumpHeight = 1.2F;
/* 1117 */     if (hasEffect(MobEffects.JUMP_BOOST)) {
/* 1118 */       jumpHeight += (getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F;
/*      */     }
/*      */ 
/*      */     
/* 1122 */     float lookAheadDist = Math.max(currentSpeed * 7.0F, 1.0F / moveDistInverted);
/*      */ 
/*      */     
/* 1125 */     Vec3 segBegin = moveBegin;
/* 1126 */     Vec3 segEnd = moveEnd.add(moveDir.scale(lookAheadDist));
/*      */ 
/*      */ 
/*      */     
/* 1130 */     float playerWidth = getBbWidth();
/* 1131 */     float playerHeight = getBbHeight();
/* 1132 */     AABB testBox = new AABB(segBegin, segEnd.add(0.0D, playerHeight, 0.0D)).inflate(playerWidth, 0.0D, playerWidth);
/*      */ 
/*      */ 
/*      */     
/* 1136 */     segBegin = segBegin.add(0.0D, 0.5099999904632568D, 0.0D);
/* 1137 */     segEnd = segEnd.add(0.0D, 0.5099999904632568D, 0.0D);
/*      */ 
/*      */ 
/*      */     
/* 1141 */     Vec3 rightDir = moveDir.cross(new Vec3(0.0D, 1.0D, 0.0D));
/* 1142 */     Vec3 rightOffset = rightDir.scale((playerWidth * 0.5F));
/*      */ 
/*      */     
/* 1145 */     Vec3 leftSegBegin = segBegin.subtract(rightOffset);
/* 1146 */     Vec3 leftSegEnd = segEnd.subtract(rightOffset);
/* 1147 */     Vec3 rightSegBegin = segBegin.add(rightOffset);
/* 1148 */     Vec3 rightSegEnd = segEnd.add(rightOffset);
/*      */ 
/*      */     
/* 1151 */     Iterable<VoxelShape> collisions = level().getCollisions((Entity)this, testBox);
/* 1152 */     Iterator<AABB> shape = StreamSupport.stream(collisions.spliterator(), false).flatMap(s -> s.toAabbs().stream()).iterator();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1157 */     float obstacleHeight = Float.MIN_VALUE;
/*      */     
/* 1159 */     while (shape.hasNext()) {
/* 1160 */       AABB box = shape.next();
/* 1161 */       if (!box.intersects(leftSegBegin, leftSegEnd) && !box.intersects(rightSegBegin, rightSegEnd)) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1168 */       obstacleHeight = (float)box.maxY;
/* 1169 */       Vec3 obstacleShapeCenter = box.getCenter();
/* 1170 */       BlockPos obstacleBlockPos = BlockPos.containing((Position)obstacleShapeCenter);
/*      */ 
/*      */       
/* 1173 */       for (int steps = 1; steps < jumpHeight; steps++) {
/* 1174 */         BlockPos abovePos1 = obstacleBlockPos.above(steps);
/* 1175 */         BlockState aboveBlock = level().getBlockState(abovePos1); VoxelShape blockShape;
/* 1176 */         if (!(blockShape = aboveBlock.getCollisionShape((BlockGetter)level(), abovePos1, context)).isEmpty()) {
/* 1177 */           obstacleHeight = (float)blockShape.max(Direction.Axis.Y) + abovePos1.getY();
/* 1178 */           if (obstacleHeight - getY() > jumpHeight) {
/*      */             return;
/*      */           }
/*      */         } 
/* 1182 */         if (steps > 1) {
/* 1183 */           ceilingPos = ceilingPos.above();
/* 1184 */           BlockState aboveBlock3 = level().getBlockState(ceilingPos);
/* 1185 */           if (!aboveBlock3.getCollisionShape((BlockGetter)level(), ceilingPos, context).isEmpty()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1196 */     if (obstacleHeight == Float.MIN_VALUE) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     float ydelta = (float)(obstacleHeight - getY());
/* 1206 */     if (ydelta <= 0.5F || ydelta > jumpHeight) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1211 */     this.autoJumpTime = 1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isHorizontalCollisionMinor(Vec3 movement) {
/* 1216 */     float yRotInRadians = getYRot() * 0.017453292F;
/* 1217 */     double yRotSin = Mth.sin(yRotInRadians);
/* 1218 */     double yRotCos = Mth.cos(yRotInRadians);
/* 1219 */     double globalXA = this.xxa * yRotCos - this.zza * yRotSin;
/* 1220 */     double globalZA = this.zza * yRotCos + this.xxa * yRotSin;
/* 1221 */     double aLengthSquared = Mth.square(globalXA) + Mth.square(globalZA);
/* 1222 */     double movementLengthSquared = Mth.square(movement.x) + Mth.square(movement.z);
/* 1223 */     if (aLengthSquared < 9.999999747378752E-6D || movementLengthSquared < 9.999999747378752E-6D) {
/* 1224 */       return false;
/*      */     }
/* 1226 */     double dotProduct = globalXA * movement.x + globalZA * movement.z;
/* 1227 */     double angleBetweenDesiredAndActualMovement = Math.acos(dotProduct / Math.sqrt(aLengthSquared * movementLengthSquared));
/* 1228 */     return (angleBetweenDesiredAndActualMovement < 0.13962633907794952D);
/*      */   }
/*      */   
/*      */   private boolean canAutoJump() {
/* 1232 */     return (isAutoJumpEnabled() && this.autoJumpTime <= 0 && 
/*      */       
/* 1234 */       onGround() && 
/* 1235 */       !isStayingOnGroundSurface() && 
/* 1236 */       !isPassenger() && 
/* 1237 */       isMoving() && 
/* 1238 */       getBlockJumpFactor() >= 1.0D);
/*      */   }
/*      */   
/*      */   private boolean isMoving() {
/* 1242 */     return (this.input.getMoveVector().lengthSquared() > 0.0F);
/*      */   }
/*      */   
/*      */   private boolean isSprintingPossible(boolean allowedInShallowWater) {
/* 1246 */     return (!isMobilityRestricted() && (
/* 1247 */       isPassenger() ? vehicleCanSprint(getVehicle()) : hasEnoughFoodToDoExhaustiveManoeuvres()) && (allowedInShallowWater || 
/* 1248 */       !isInShallowWater()));
/*      */   }
/*      */   
/*      */   private boolean canStartSprinting() {
/* 1252 */     return (!isSprinting() && 
/* 1253 */       this.input.hasForwardImpulse() && 
/* 1254 */       isSprintingPossible((getAbilities()).flying) && 
/* 1255 */       !isSlowDueToUsingItem() && (
/* 1256 */       !isFallFlying() || isUnderWater()) && (
/* 1257 */       !isMovingSlowly() || isUnderWater()));
/*      */   }
/*      */   
/*      */   private boolean vehicleCanSprint(Entity vehicle) {
/* 1261 */     return (vehicle.canSprint() && vehicle.isLocalInstanceAuthoritative());
/*      */   }
/*      */   
/*      */   public float getWaterVision() {
/* 1265 */     if (!isEyeInFluid(FluidTags.WATER)) {
/* 1266 */       return 0.0F;
/*      */     }
/* 1268 */     float max = 600.0F;
/* 1269 */     float mid = 100.0F;
/* 1270 */     if (this.waterVisionTime >= 600.0F) {
/* 1271 */       return 1.0F;
/*      */     }
/* 1273 */     float a = Mth.clamp(this.waterVisionTime / 100.0F, 0.0F, 1.0F);
/* 1274 */     float b = (this.waterVisionTime < 100.0F) ? 0.0F : Mth.clamp((this.waterVisionTime - 100.0F) / 500.0F, 0.0F, 1.0F);
/* 1275 */     return a * 0.6F + b * 0.39999998F;
/*      */   }
/*      */   
/*      */   public void onGameModeChanged(GameType gameType) {
/* 1279 */     if (gameType == GameType.SPECTATOR)
/*      */     {
/* 1281 */       setDeltaMovement(getDeltaMovement().with(Direction.Axis.Y, 0.0D));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUnderWater() {
/* 1287 */     return this.wasUnderwater;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean updateIsUnderwater() {
/* 1292 */     boolean oldIsUnderwater = this.wasUnderwater;
/* 1293 */     boolean newIsUnderwater = super.updateIsUnderwater();
/*      */     
/* 1295 */     if (isSpectator()) {
/* 1296 */       return this.wasUnderwater;
/*      */     }
/*      */     
/* 1299 */     if (!oldIsUnderwater && newIsUnderwater) {
/* 1300 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/* 1301 */       this.minecraft.getSoundManager().play((SoundInstance)new UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance(this));
/*      */     } 
/*      */     
/* 1304 */     if (oldIsUnderwater && !newIsUnderwater) {
/* 1305 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*      */     }
/*      */     
/* 1308 */     return this.wasUnderwater;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 1313 */     if (this.minecraft.options.getCameraType().isFirstPerson()) {
/* 1314 */       float yRot = Mth.lerp(partialTickTime * 0.5F, getYRot(), this.yRotO) * 0.017453292F;
/* 1315 */       float xRot = Mth.lerp(partialTickTime * 0.5F, getXRot(), this.xRotO) * 0.017453292F;
/* 1316 */       double handDir = (getMainArm() == HumanoidArm.RIGHT) ? -1.0D : 1.0D;
/* 1317 */       Vec3 offset = new Vec3(0.39D * handDir, -0.6D, 0.3D);
/* 1318 */       return offset.xRot(-xRot).yRot(-yRot).add(getEyePosition(partialTickTime));
/*      */     } 
/* 1320 */     return super.getRopeHoldPosition(partialTickTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTutorialInventoryAction(ItemStack itemCarried, ItemStack itemInSlot, ClickAction clickAction) {
/* 1325 */     this.minecraft.getTutorial().onInventoryAction(itemCarried, itemInSlot, clickAction);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getVisualRotationYInDegrees() {
/* 1330 */     return getYRot();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCreativeModeItemDrop(ItemStack stack) {
/* 1335 */     this.minecraft.gameMode.handleCreativeModeItemDrop(stack);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canDropItems() {
/* 1340 */     return this.dropSpamThrottler.isUnderThreshold();
/*      */   }
/*      */   
/*      */   public TickThrottler getDropSpamThrottler() {
/* 1344 */     return this.dropSpamThrottler;
/*      */   }
/*      */   
/*      */   public Input getLastSentInput() {
/* 1348 */     return this.lastSentInput;
/*      */   }
/*      */   
/*      */   public HitResult raycastHitResult(float a, Entity cameraEntity) {
/* 1352 */     ItemStack itemStack = getActiveItem();
/* 1353 */     AttackRange itemAttackRange = (AttackRange)itemStack.get(DataComponents.ATTACK_RANGE);
/* 1354 */     double blockInteractionRange = blockInteractionRange();
/* 1355 */     HitResult hitResult = null;
/* 1356 */     if (itemAttackRange != null) {
/* 1357 */       hitResult = itemAttackRange.getClosesetHit(cameraEntity, a, EntitySelector.CAN_BE_PICKED);
/* 1358 */       if (hitResult instanceof BlockHitResult) {
/* 1359 */         hitResult = filterHitResult(hitResult, cameraEntity.getEyePosition(a), blockInteractionRange);
/*      */       }
/*      */     } 
/*      */     
/* 1363 */     if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
/* 1364 */       double entityInteractionRange = entityInteractionRange();
/* 1365 */       hitResult = pick(cameraEntity, blockInteractionRange, entityInteractionRange, a);
/*      */     } 
/* 1367 */     return hitResult;
/*      */   }
/*      */ 
/*      */   
/*      */   private static HitResult pick(Entity cameraEntity, double blockInteractionRange, double entityInteractionRange, float partialTicks) {
/* 1372 */     double maxDistance = Math.max(blockInteractionRange, entityInteractionRange);
/* 1373 */     double maxDistanceSq = Mth.square(maxDistance);
/*      */     
/* 1375 */     Vec3 from = cameraEntity.getEyePosition(partialTicks);
/*      */     
/* 1377 */     HitResult blockHitResult = cameraEntity.pick(maxDistance, partialTicks, false);
/* 1378 */     double blockDistanceSq = blockHitResult.getLocation().distanceToSqr(from);
/*      */ 
/*      */     
/* 1381 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 1382 */       maxDistanceSq = blockDistanceSq;
/* 1383 */       maxDistance = Math.sqrt(maxDistanceSq);
/*      */     } 
/*      */     
/* 1386 */     Vec3 direction = cameraEntity.getViewVector(partialTicks);
/* 1387 */     Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
/*      */     
/* 1389 */     float overlap = 1.0F;
/* 1390 */     AABB box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0D, 1.0D, 1.0D);
/* 1391 */     EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, maxDistanceSq);
/*      */     
/* 1393 */     if (entityHitResult != null && entityHitResult.getLocation().distanceToSqr(from) < blockDistanceSq) {
/* 1394 */       return filterHitResult((HitResult)entityHitResult, from, entityInteractionRange);
/*      */     }
/* 1396 */     return filterHitResult(blockHitResult, from, blockInteractionRange);
/*      */   }
/*      */ 
/*      */   
/*      */   private static HitResult filterHitResult(HitResult hitResult, Vec3 from, double maxRange) {
/* 1401 */     Vec3 hitLocation = hitResult.getLocation();
/* 1402 */     if (!hitLocation.closerThan((Position)from, maxRange)) {
/* 1403 */       Vec3 location = hitResult.getLocation();
/* 1404 */       Direction direction = Direction.getApproximateNearest(location.x - from.x, location.y - from.y, location.z - from.z);
/* 1405 */       return (HitResult)BlockHitResult.miss(location, direction, BlockPos.containing((Position)location));
/*      */     } 
/* 1407 */     return hitResult;
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/LocalPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */