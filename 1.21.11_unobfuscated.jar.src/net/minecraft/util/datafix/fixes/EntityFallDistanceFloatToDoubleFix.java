/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityFallDistanceFloatToDoubleFix
/*    */   extends DataFix {
/*    */   public EntityFallDistanceFloatToDoubleFix(Schema outputSchema, DSL.TypeReference type) {
/* 13 */     super(outputSchema, false);
/* 14 */     this.type = type;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("EntityFallDistanceFloatToDoubleFixFor" + this.type.typeName(), getOutputSchema().getType(this.type), EntityFallDistanceFloatToDoubleFix::fixEntity);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixEntity(Typed<?> entity) {
/* 23 */     return entity.update(DSL.remainderFinder(), remainder -> remainder.renameAndFixField("FallDistance", "fall_distance", ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityFallDistanceFloatToDoubleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */