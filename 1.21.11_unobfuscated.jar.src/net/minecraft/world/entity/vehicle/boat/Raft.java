/*    */ package net.minecraft.world.entity.vehicle.boat;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Raft
/*    */   extends AbstractBoat {
/*    */   public Raft(EntityType<? extends Raft> type, Level level, Supplier<Item> dropItem) {
/* 12 */     super((EntityType)type, level, dropItem);
/*    */   }
/*    */ 
/*    */   
/*    */   protected double rideHeight(EntityDimensions dimensions) {
/* 17 */     return (dimensions.height() * 0.8888889F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/boat/Raft.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */