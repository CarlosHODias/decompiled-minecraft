/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FireResistantToDamageResistantComponentFix extends DataComponentRemainderFix {
/*    */   public FireResistantToDamageResistantComponentFix(Schema outputSchema) {
/*  8 */     super(outputSchema, "FireResistantToDamageResistantComponentFix", "minecraft:fire_resistant", "minecraft:damage_resistant");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 13 */     return input.emptyMap().set("types", input.createString("#minecraft:is_fire"));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/FireResistantToDamageResistantComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */