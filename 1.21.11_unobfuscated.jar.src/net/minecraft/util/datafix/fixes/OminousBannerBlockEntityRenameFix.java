/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public class OminousBannerBlockEntityRenameFix extends NamedEntityFix {
/*    */   public OminousBannerBlockEntityRenameFix(Schema outputSchema, boolean changesType) {
/* 11 */     super(outputSchema, changesType, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 16 */     OpticFinder<?> customNameF = entity.getType().findField("CustomName");
/*    */     
/* 18 */     OpticFinder<Pair<String, String>> textComponentF = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 19 */     return entity.updateTyped(customNameF, customName -> customName.update(textComponentF, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OminousBannerBlockEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */