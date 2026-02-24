/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class VillagerDataFix extends NamedEntityFix {
/*    */   public VillagerDataFix(Schema schema, String entityType) {
/* 12 */     super(schema, false, "Villager profession data fix (" + entityType + ")", References.ENTITY, entityType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     Dynamic<?> remainder = (Dynamic)entity.get(DSL.remainderFinder());
/*    */     
/* 19 */     return entity.set(DSL.remainderFinder(), 
/* 20 */         remainder.remove("Profession")
/* 21 */         .remove("Career")
/* 22 */         .remove("CareerLevel")
/* 23 */         .set("VillagerData", 
/* 24 */           remainder.createMap((Map)ImmutableMap.of(
/* 25 */               remainder.createString("type"), remainder.createString("minecraft:plains"), 
/* 26 */               remainder.createString("profession"), remainder.createString(upgradeData(remainder.get("Profession").asInt(0), remainder.get("Career").asInt(0))), 
/* 27 */               remainder.createString("level"), DataFixUtils.orElse(remainder.get("CareerLevel").result(), remainder.createInt(1))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String upgradeData(int profession, int career) {
/* 34 */     if (profession == 0) {
/* 35 */       if (career == 2)
/* 36 */         return "minecraft:fisherman"; 
/* 37 */       if (career == 3)
/* 38 */         return "minecraft:shepherd"; 
/* 39 */       if (career == 4) {
/* 40 */         return "minecraft:fletcher";
/*    */       }
/* 42 */       return "minecraft:farmer";
/*    */     } 
/* 44 */     if (profession == 1) {
/* 45 */       if (career == 2) {
/* 46 */         return "minecraft:cartographer";
/*    */       }
/* 48 */       return "minecraft:librarian";
/*    */     } 
/* 50 */     if (profession == 2)
/* 51 */       return "minecraft:cleric"; 
/* 52 */     if (profession == 3) {
/* 53 */       if (career == 2)
/* 54 */         return "minecraft:weaponsmith"; 
/* 55 */       if (career == 3) {
/* 56 */         return "minecraft:toolsmith";
/*    */       }
/* 58 */       return "minecraft:armorer";
/*    */     } 
/* 60 */     if (profession == 4) {
/* 61 */       if (career == 2) {
/* 62 */         return "minecraft:leatherworker";
/*    */       }
/* 64 */       return "minecraft:butcher";
/*    */     } 
/* 66 */     if (profession == 5) {
/* 67 */       return "minecraft:nitwit";
/*    */     }
/* 69 */     return "minecraft:none";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/VillagerDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */