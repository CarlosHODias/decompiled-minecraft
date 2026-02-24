/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PlayerHeadBlockProfileFix
/*    */   extends NamedEntityFix {
/*    */   public PlayerHeadBlockProfileFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false, "PlayerHeadBlockProfileFix", References.BLOCK_ENTITY, "minecraft:skull");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     return entity.update(DSL.remainderFinder(), this::fix);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> entity) {
/* 21 */     Optional<Dynamic<T>> skullOwner = entity.get("SkullOwner").result();
/* 22 */     Optional<Dynamic<T>> extraType = entity.get("ExtraType").result();
/*    */     
/* 24 */     Optional<Dynamic<T>> profile = skullOwner.or(() -> extraType);
/* 25 */     if (profile.isEmpty()) {
/* 26 */       return entity;
/*    */     }
/* 28 */     entity = entity.remove("SkullOwner").remove("ExtraType");
/* 29 */     entity = entity.set("profile", ItemStackComponentizationFix.fixProfile(profile.get()));
/* 30 */     return entity;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/PlayerHeadBlockProfileFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */