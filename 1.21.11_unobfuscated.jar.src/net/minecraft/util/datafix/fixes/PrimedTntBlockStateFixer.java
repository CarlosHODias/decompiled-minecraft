/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PrimedTntBlockStateFixer
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/*    */   public PrimedTntBlockStateFixer(Schema outputSchema) {
/* 12 */     super(outputSchema, true, "PrimedTnt BlockState fixer", References.ENTITY, "minecraft:tnt");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> renameFuse(Dynamic<T> input) {
/* 16 */     Optional<Dynamic<T>> fuseValue = input.get("Fuse").get().result();
/* 17 */     if (fuseValue.isPresent()) {
/* 18 */       return input.set("fuse", fuseValue.get());
/*    */     }
/* 20 */     return input;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> insertBlockState(Dynamic<T> input) {
/* 24 */     return input.set("block_state", input.createMap(Map.of(
/* 25 */             input.createString("Name"), input.createString("minecraft:tnt"))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 31 */     return renameFuse(insertBlockState(input));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/PrimedTntBlockStateFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */