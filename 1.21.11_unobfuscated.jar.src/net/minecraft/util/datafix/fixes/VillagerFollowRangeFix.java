/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerFollowRangeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final double ORIGINAL_VALUE = 16.0D;
/*    */   private static final double NEW_BASE_VALUE = 48.0D;
/*    */   
/*    */   public VillagerFollowRangeFix(Schema outputSchema) {
/* 17 */     super(outputSchema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 22 */     return entity.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 26 */     return tag.update("Attributes", attributes -> tag.createList(attributes.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/VillagerFollowRangeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */