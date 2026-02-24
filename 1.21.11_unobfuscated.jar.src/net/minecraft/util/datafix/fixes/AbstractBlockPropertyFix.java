/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class AbstractBlockPropertyFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AbstractBlockPropertyFix(Schema outputSchema, String name) {
/* 16 */     super(outputSchema, false);
/* 17 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), this::fixBlockState));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixBlockState(Dynamic<?> tag) {
/* 26 */     Optional<String> blockId = tag.get("Name").asString().result().map(NamespacedSchema::ensureNamespaced);
/* 27 */     if (blockId.isPresent() && shouldFix(blockId.get())) {
/* 28 */       return tag.update("Properties", properties -> fixProperties(blockId.get(), blockId));
/*    */     }
/* 30 */     return tag;
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldFix(String paramString);
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixProperties(String paramString, Dynamic<T> paramDynamic);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/AbstractBlockPropertyFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */