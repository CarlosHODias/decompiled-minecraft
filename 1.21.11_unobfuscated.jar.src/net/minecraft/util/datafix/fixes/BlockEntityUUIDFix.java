/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityUUIDFix extends AbstractUUIDFix {
/*    */   public BlockEntityUUIDFix(Schema outputSchema) {
/*  9 */     super(outputSchema, References.BLOCK_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 14 */     return fixTypeEverywhereTyped("BlockEntityUUIDFix", getInputSchema().getType(this.typeReference), input -> {
/*    */           input = updateNamedChoice(input, "minecraft:conduit", this::updateConduit);
/*    */           return updateNamedChoice(input, "minecraft:skull", this::updateSkull);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkull(Dynamic<?> tag) {
/* 22 */     return tag.get("Owner").get().map(ownerTag -> (Dynamic)replaceUUIDString(ownerTag, "Id", "Id").orElse(ownerTag))
/*    */       
/* 24 */       .map(ownerTag -> tag.remove("Owner").set("SkullOwner", ownerTag))
/*    */       
/* 26 */       .result().orElse(tag);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateConduit(Dynamic<?> tag) {
/* 30 */     return replaceUUIDMLTag(tag, "target_uuid", "Target").orElse(tag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockEntityUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */