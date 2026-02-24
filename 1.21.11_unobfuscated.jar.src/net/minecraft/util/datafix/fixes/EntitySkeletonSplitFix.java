/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntitySkeletonSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntitySkeletonSplitFix(Schema outputSchema, boolean changesType) {
/* 11 */     super("EntitySkeletonSplitFix", outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String name, Dynamic<?> tag) {
/* 16 */     if (Objects.equals(name, "Skeleton")) {
/* 17 */       int type = tag.get("SkeletonType").asInt(0);
/* 18 */       if (type == 1) {
/* 19 */         name = "WitherSkeleton";
/* 20 */       } else if (type == 2) {
/* 21 */         name = "Stray";
/*    */       } 
/*    */     } 
/* 24 */     return Pair.of(name, tag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntitySkeletonSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */