/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerSetCanPickUpLootFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final String CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*    */   
/*    */   public VillagerSetCanPickUpLootFix(Schema outputSchema) {
/* 16 */     super(outputSchema, true, "Villager CanPickUpLoot default value", References.ENTITY, "Villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 21 */     return entity.update(DSL.remainderFinder(), VillagerSetCanPickUpLootFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 25 */     return tag.set("CanPickUpLoot", tag.createBoolean(true));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/VillagerSetCanPickUpLootFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */