/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CopperGolemWeatherStateFix extends NamedEntityFix {
/*    */   public CopperGolemWeatherStateFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "CopperGolemWeatherStateFix", References.ENTITY, "minecraft:copper_golem");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("weather_state", CopperGolemWeatherStateFix::fixWeatherState));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWeatherState(Dynamic<?> value) {
/* 19 */     switch (value.asInt(0)) { case 1: case 2: case 3: default: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 23 */       value.createString("unaffected");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/CopperGolemWeatherStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */