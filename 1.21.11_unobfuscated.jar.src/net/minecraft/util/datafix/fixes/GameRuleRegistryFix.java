/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GameRuleRegistryFix extends DataFix {
/*     */   public GameRuleRegistryFix(Schema outputSchema) {
/*  12 */     super(outputSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  17 */     return fixTypeEverywhereTyped("GameRuleRegistryFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue) {
/* 105 */     return convertInteger(oldValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min) {
/* 109 */     return convertInteger(oldValue, min, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min, int max) {
/* 113 */     String stringValue = oldValue.asString("");
/*     */     try {
/* 115 */       int parsedValue = Integer.parseInt(stringValue);
/* 116 */       return oldValue.createInt(Mth.clamp(parsedValue, min, max));
/* 117 */     } catch (NumberFormatException ignored) {
/* 118 */       return oldValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBoolean(Dynamic<?> oldValue) {
/* 123 */     return oldValue.createBoolean(Boolean.parseBoolean(oldValue.asString("")));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBooleanInverted(Dynamic<?> oldValue) {
/* 127 */     return oldValue.createBoolean(!Boolean.parseBoolean(oldValue.asString("")));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/GameRuleRegistryFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */