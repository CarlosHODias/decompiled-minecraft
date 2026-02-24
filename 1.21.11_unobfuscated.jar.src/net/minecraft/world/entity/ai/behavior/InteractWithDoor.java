/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InteractWithDoor
/*     */ {
/*     */   private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 20;
/*     */   private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 3.0D;
/*     */   private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2.0D;
/*     */   
/*     */   public static BehaviorControl<LivingEntity> create() {
/*  45 */     MutableObject<Node> lastCheckedNode = new MutableObject();
/*  46 */     MutableInt remainingCooldown = new MutableInt(0);
/*     */     
/*  48 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.PATH), (App)i.registered(MemoryModuleType.DOORS_TO_CLOSE), (App)i.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel level, LivingEntity body, Node movingFromNode, Node movingToNode, Set<GlobalPos> doors, Optional<List<LivingEntity>> nearestEntities) {
/*  98 */     Iterator<GlobalPos> iterator = doors.iterator();
/*  99 */     while (iterator.hasNext()) {
/* 100 */       GlobalPos doorGlobalPos = iterator.next();
/* 101 */       BlockPos doorPos = doorGlobalPos.pos();
/*     */ 
/*     */       
/* 104 */       if (movingFromNode != null && movingFromNode.asBlockPos().equals(doorPos)) {
/*     */         continue;
/*     */       }
/* 107 */       if (movingToNode != null && movingToNode.asBlockPos().equals(doorPos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 111 */       if (isDoorTooFarAway(level, body, doorGlobalPos)) {
/* 112 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 115 */       BlockState state = level.getBlockState(doorPos);
/* 116 */       if (!state.is(BlockTags.MOB_INTERACTABLE_DOORS, s -> s.getBlock() instanceof DoorBlock)) {
/* 117 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 120 */       DoorBlock block = (DoorBlock)state.getBlock();
/* 121 */       if (!block.isOpen(state)) {
/* 122 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 125 */       if (areOtherMobsComingThroughDoor(body, doorPos, nearestEntities)) {
/* 126 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 129 */       block.setOpen((Entity)body, (Level)level, state, doorPos, false);
/* 130 */       iterator.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areOtherMobsComingThroughDoor(LivingEntity body, BlockPos doorPos, Optional<List<LivingEntity>> nearestEntities) {
/* 135 */     if (nearestEntities.isEmpty()) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     return ((List)nearestEntities.get()).stream()
/* 140 */       .filter(otherMob -> (otherMob.getType() == body.getType()))
/* 141 */       .filter(otherMob -> doorPos.closerToCenterThan((Position)otherMob.position(), 2.0D))
/* 142 */       .anyMatch(otherMob -> isMobComingThroughDoor(otherMob.getBrain(), doorPos));
/*     */   }
/*     */   
/*     */   private static boolean isMobComingThroughDoor(Brain<?> otherBrain, BlockPos doorPos) {
/* 146 */     if (!otherBrain.hasMemoryValue(MemoryModuleType.PATH)) {
/* 147 */       return false;
/*     */     }
/* 149 */     Path path = otherBrain.getMemory(MemoryModuleType.PATH).get();
/* 150 */     if (path.isDone())
/*     */     {
/* 152 */       return false;
/*     */     }
/*     */     
/* 155 */     Node movingFromNode = path.getPreviousNode();
/* 156 */     if (movingFromNode == null) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     Node movingToNode = path.getNextNode();
/* 161 */     return (doorPos.equals(movingFromNode.asBlockPos()) || doorPos.equals(movingToNode.asBlockPos()));
/*     */   }
/*     */   
/*     */   private static boolean isDoorTooFarAway(ServerLevel level, LivingEntity body, GlobalPos doorGlobalPos) {
/* 165 */     return (doorGlobalPos.dimension() != level.dimension() || 
/* 166 */       !doorGlobalPos.pos().closerToCenterThan((Position)body.position(), 3.0D));
/*     */   }
/*     */   
/*     */   private static Optional<Set<GlobalPos>> rememberDoorToClose(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> doorsMemory, Optional<Set<GlobalPos>> doors, ServerLevel level, BlockPos doorPos) {
/* 170 */     GlobalPos globalDoorPos = GlobalPos.of(level.dimension(), doorPos);
/*     */     
/* 172 */     return Optional.of(doors.<Set<GlobalPos>>map(set -> {
/*     */             set.add(globalDoorPos);
/*     */             return set;
/* 175 */           }).orElseGet(() -> {
/*     */             Set<GlobalPos> set = Sets.newHashSet((Object[])new GlobalPos[] { globalDoorPos });
/*     */             doorsMemory.set(set);
/*     */             return set;
/*     */           }));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/InteractWithDoor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */