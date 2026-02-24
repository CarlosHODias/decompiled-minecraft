/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class CauldronRenameFix extends DataFix {
/*    */   public CauldronRenameFix(Schema outputSchema, boolean changesType) {
/* 13 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fix(Dynamic<?> tag) {
/* 17 */     Optional<String> name = tag.get("Name").asString().result();
/* 18 */     if (name.equals(Optional.of("minecraft:cauldron"))) {
/* 19 */       Dynamic<?> properties = tag.get("Properties").orElseEmptyMap();
/* 20 */       if (properties.get("level").asString("0").equals("0")) {
/* 21 */         return tag.remove("Properties");
/*    */       }
/* 23 */       return tag.set("Name", tag.createString("minecraft:water_cauldron"));
/*    */     } 
/* 25 */     return tag;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 30 */     return fixTypeEverywhereTyped("cauldron_rename_fix", getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), CauldronRenameFix::fix));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/CauldronRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */