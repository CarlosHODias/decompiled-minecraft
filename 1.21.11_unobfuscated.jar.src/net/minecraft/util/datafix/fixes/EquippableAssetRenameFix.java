/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EquippableAssetRenameFix extends DataFix {
/*    */   public EquippableAssetRenameFix(Schema outputSchema) {
/* 12 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> componentsType = getInputSchema().getType(References.DATA_COMPONENTS);
/* 18 */     OpticFinder<?> equippableField = componentsType.findField("minecraft:equippable");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("equippable asset rename fix", componentsType, components -> components.updateTyped(equippableField, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EquippableAssetRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */