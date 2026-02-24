/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JigsawPropertiesFix extends NamedEntityFix {
/*    */   public JigsawPropertiesFix(Schema schema, boolean changesType) {
/* 10 */     super(schema, changesType, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> tag) {
/* 14 */     String oldName = tag.get("attachement_type").asString("minecraft:empty");
/* 15 */     String oldPool = tag.get("target_pool").asString("minecraft:empty");
/* 16 */     return 
/* 17 */       tag.set("name", tag.createString(oldName))
/* 18 */       .set("target", tag.createString(oldName))
/* 19 */       .remove("attachement_type")
/* 20 */       .set("pool", tag.createString(oldPool))
/* 21 */       .remove("target_pool");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 26 */     return entity.update(DSL.remainderFinder(), JigsawPropertiesFix::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/JigsawPropertiesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */