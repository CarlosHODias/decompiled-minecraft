/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityRedundantChanceTagsFix extends com.mojang.datafixers.DataFix {
/* 13 */   private static final Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
/*    */   
/*    */   public EntityRedundantChanceTagsFix(Schema outputSchema, boolean changesType) {
/* 16 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     return fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", getInputSchema().getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isZeroList(OptionalDynamic<?> element, int size) {
/* 34 */     Objects.requireNonNull(FLOAT_LIST_CODEC); return (Boolean)element.flatMap(FLOAT_LIST_CODEC::parse).map(floats -> (floats.size() == size && floats.stream().allMatch(()))).result().orElse(false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityRedundantChanceTagsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */