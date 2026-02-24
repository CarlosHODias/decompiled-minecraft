/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public abstract class SimpleEntityRenameFix extends EntityRenameFix {
/*    */   public SimpleEntityRenameFix(String name, Schema outputSchema, boolean changesType) {
/* 11 */     super(name, outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 16 */     Pair<String, Dynamic<?>> pair = getNewNameAndTag(name, (Dynamic)entity.getOrCreate(DSL.remainderFinder()));
/* 17 */     return Pair.of(pair.getFirst(), entity.set(DSL.remainderFinder(), pair.getSecond()));
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/SimpleEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */