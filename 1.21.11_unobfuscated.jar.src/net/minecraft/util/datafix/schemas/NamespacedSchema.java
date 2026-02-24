/*    */ package net.minecraft.util.datafix.schemas;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.Const;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class NamespacedSchema extends Schema {
/*    */   public NamespacedSchema(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public static String ensureNamespaced(String input) {
/* 18 */     Identifier identifier = Identifier.tryParse(input);
/* 19 */     if (identifier != null) {
/* 20 */       return identifier.toString();
/*    */     }
/* 22 */     return input;
/*    */   }
/*    */   
/* 25 */   public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC = new PrimitiveCodec<String>()
/*    */     {
/*    */       public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
/* 28 */         return 
/* 29 */           ops.getStringValue(input)
/* 30 */           .map(NamespacedSchema::ensureNamespaced);
/*    */       }
/*    */ 
/*    */       
/*    */       public <T> T write(DynamicOps<T> ops, String value) {
/* 35 */         return (T)ops.createString(value);
/*    */       }
/*    */ 
/*    */       
/*    */       public String toString() {
/* 40 */         return "NamespacedString";
/*    */       }
/*    */     };
/*    */   
/* 44 */   private static final Type<String> NAMESPACED_STRING = (Type<String>)new Const.PrimitiveType((Codec)NAMESPACED_STRING_CODEC);
/*    */   
/*    */   public static Type<String> namespacedString() {
/* 47 */     return NAMESPACED_STRING;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<?> getChoiceType(DSL.TypeReference type, String choiceName) {
/* 52 */     return super.getChoiceType(type, ensureNamespaced(choiceName));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/NamespacedSchema.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */