/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.GameEventTags;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class GameEventTagsProvider
/*    */   extends KeyTagProvider<GameEvent>
/*    */ {
/*    */   @VisibleForTesting
/* 18 */   static final List<ResourceKey<GameEvent>> VIBRATIONS_EXCEPT_FLAP = List.of((ResourceKey<GameEvent>[])new ResourceKey[] { 
/* 19 */         GameEvent.BLOCK_ATTACH.key(), 
/* 20 */         GameEvent.BLOCK_CHANGE.key(), 
/* 21 */         GameEvent.BLOCK_CLOSE.key(), 
/* 22 */         GameEvent.BLOCK_DESTROY.key(), 
/* 23 */         GameEvent.BLOCK_DETACH.key(), 
/* 24 */         GameEvent.BLOCK_OPEN.key(), 
/* 25 */         GameEvent.BLOCK_PLACE.key(), 
/* 26 */         GameEvent.BLOCK_ACTIVATE.key(), 
/* 27 */         GameEvent.BLOCK_DEACTIVATE.key(), 
/* 28 */         GameEvent.CONTAINER_CLOSE.key(), 
/* 29 */         GameEvent.CONTAINER_OPEN.key(), 
/* 30 */         GameEvent.DRINK.key(), 
/* 31 */         GameEvent.EAT.key(), 
/* 32 */         GameEvent.ELYTRA_GLIDE.key(), 
/* 33 */         GameEvent.ENTITY_DAMAGE.key(), 
/* 34 */         GameEvent.ENTITY_DIE.key(), 
/* 35 */         GameEvent.ENTITY_DISMOUNT.key(), 
/* 36 */         GameEvent.ENTITY_INTERACT.key(), 
/* 37 */         GameEvent.ENTITY_MOUNT.key(), 
/* 38 */         GameEvent.ENTITY_PLACE.key(), 
/* 39 */         GameEvent.ENTITY_ACTION.key(), 
/* 40 */         GameEvent.EQUIP.key(), 
/* 41 */         GameEvent.EXPLODE.key(), 
/*    */         
/* 43 */         GameEvent.FLUID_PICKUP.key(), 
/* 44 */         GameEvent.FLUID_PLACE.key(), 
/* 45 */         GameEvent.HIT_GROUND.key(), 
/* 46 */         GameEvent.INSTRUMENT_PLAY.key(), 
/* 47 */         GameEvent.ITEM_INTERACT_FINISH.key(), 
/* 48 */         GameEvent.LIGHTNING_STRIKE.key(), 
/* 49 */         GameEvent.NOTE_BLOCK_PLAY.key(), 
/* 50 */         GameEvent.PRIME_FUSE.key(), 
/* 51 */         GameEvent.PROJECTILE_LAND.key(), 
/* 52 */         GameEvent.PROJECTILE_SHOOT.key(), 
/* 53 */         GameEvent.SHEAR.key(), 
/* 54 */         GameEvent.SPLASH.key(), 
/* 55 */         GameEvent.STEP.key(), 
/* 56 */         GameEvent.SWIM.key(), 
/* 57 */         GameEvent.TELEPORT.key(), 
/* 58 */         GameEvent.UNEQUIP.key() });
/*    */ 
/*    */   
/*    */   public GameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 62 */     super(output, Registries.GAME_EVENT, lookupProvider);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 67 */     tag(GameEventTags.VIBRATIONS).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.FLAP.key());
/*    */     
/* 69 */     tag(GameEventTags.SHRIEKER_CAN_LISTEN).add(GameEvent.SCULK_SENSOR_TENDRILS_CLICKING.key());
/*    */     
/* 71 */     tag(GameEventTags.WARDEN_CAN_LISTEN).addAll(VIBRATIONS_EXCEPT_FLAP).addAll(VibrationSystem.RESONANCE_EVENTS).add(GameEvent.SHRIEK.key()).addTag(GameEventTags.SHRIEKER_CAN_LISTEN);
/*    */     
/* 73 */     tag(GameEventTags.IGNORE_VIBRATIONS_SNEAKING).add((ResourceKey<GameEvent>[])new ResourceKey[] {
/* 74 */           GameEvent.HIT_GROUND.key(), 
/* 75 */           GameEvent.PROJECTILE_SHOOT.key(), 
/* 76 */           GameEvent.STEP.key(), 
/* 77 */           GameEvent.SWIM.key(), 
/* 78 */           GameEvent.ITEM_INTERACT_START.key(), 
/* 79 */           GameEvent.ITEM_INTERACT_FINISH.key()
/*    */         });
/*    */     
/* 82 */     tag(GameEventTags.ALLAY_CAN_LISTEN).add(GameEvent.NOTE_BLOCK_PLAY.key());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/GameEventTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */