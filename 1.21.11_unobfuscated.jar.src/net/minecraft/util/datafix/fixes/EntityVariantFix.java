/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ 
/*    */ public class EntityVariantFix
/*    */   extends NamedEntityFix {
/*    */   private final String fieldName;
/*    */   private final IntFunction<String> idConversions;
/*    */   
/*    */   public EntityVariantFix(Schema outputSchema, String name, DSL.TypeReference type, String entityName, String fieldName, IntFunction<String> idConversions) {
/* 18 */     super(outputSchema, false, name, type, entityName);
/* 19 */     this.fieldName = fieldName;
/* 20 */     this.idConversions = idConversions;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateAndRename(Dynamic<T> input, String oldKey, String newKey, Function<Dynamic<T>, Dynamic<T>> function) {
/* 24 */     return input.map(v -> {
/*    */           DynamicOps<T> ops = input.getOps();
/*    */           Function<T, T> liftedFunction = ();
/*    */           return ops.get(v, oldKey).map(()).result().orElse(v);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> typed) {
/* 36 */     return typed.update(DSL.remainderFinder(), remainder -> updateAndRename(remainder, this.fieldName, "variant", ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityVariantFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */