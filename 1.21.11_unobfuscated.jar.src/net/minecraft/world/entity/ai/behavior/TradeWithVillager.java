/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class TradeWithVillager
/*     */   extends Behavior<Villager> {
/*  24 */   private Set<Item> trades = (Set<Item>)ImmutableSet.of();
/*     */   
/*     */   public TradeWithVillager() {
/*  27 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  35 */     return BehaviorUtils.targetIsValid(body.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  40 */     return checkExtraStartConditions(level, body);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager myBody, long timestamp) {
/*  45 */     Villager target = myBody.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  46 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)myBody, (LivingEntity)target, 0.5F, 2);
/*     */     
/*  48 */     this.trades = figureOutWhatIAmWillingToTrade(myBody, target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  53 */     Villager target = body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */     
/*  55 */     if (body.distanceToSqr((Entity)target) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)target, 0.5F, 2);
/*     */     
/*  61 */     body.gossip(level, target, timestamp);
/*     */     
/*  63 */     boolean isFarmer = body.getVillagerData().profession().is(VillagerProfession.FARMER);
/*  64 */     if (body.hasExcessFood() && (isFarmer || target.wantsMoreFood())) {
/*  65 */       throwHalfStack(body, Villager.FOOD_POINTS.keySet(), (LivingEntity)target);
/*     */     }
/*     */     
/*  68 */     if (isFarmer && body.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getDefaultMaxStackSize() / 2) {
/*  69 */       throwHalfStack(body, (Set<Item>)ImmutableSet.of(Items.WHEAT), (LivingEntity)target);
/*     */     }
/*     */     
/*  72 */     if (!this.trades.isEmpty() && body.getInventory().hasAnyOf(this.trades)) {
/*  73 */       throwHalfStack(body, this.trades, (LivingEntity)target);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/*  79 */     body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Item> figureOutWhatIAmWillingToTrade(Villager myBody, Villager target) {
/*  85 */     ImmutableSet<Item> targetItems = ((VillagerProfession)target.getVillagerData().profession().value()).requestedItems();
/*  86 */     ImmutableSet<Item> selfItems = ((VillagerProfession)myBody.getVillagerData().profession().value()).requestedItems();
/*  87 */     return (Set<Item>)targetItems.stream().filter(entry -> !selfItems.contains(entry)).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwHalfStack(Villager villager, Set<Item> items, LivingEntity target) {
/*  94 */     SimpleContainer inventory = villager.getInventory();
/*     */     
/*  96 */     ItemStack toThrow = ItemStack.EMPTY;
/*  97 */     for (int i = 0; i < inventory.getContainerSize(); i++) {
/*  98 */       ItemStack itemStack = inventory.getItem(i);
/*  99 */       if (!itemStack.isEmpty()) {
/* 100 */         Item item = itemStack.getItem();
/* 101 */         if (items.contains(item)) {
/*     */           int count;
/* 103 */           if (itemStack.getCount() > itemStack.getMaxStackSize() / 2) {
/* 104 */             count = itemStack.getCount() / 2;
/* 105 */           } else if (itemStack.getCount() > 24) {
/* 106 */             count = itemStack.getCount() - 24;
/*     */           } else {
/*     */             continue;
/*     */           } 
/* 110 */           itemStack.shrink(count);
/* 111 */           toThrow = new ItemStack((ItemLike)item, count);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 117 */     if (!toThrow.isEmpty())
/* 118 */       BehaviorUtils.throwItem((LivingEntity)villager, toThrow, target.position()); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/TradeWithVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */