/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class JigsawRotationFix
/*    */   extends AbstractBlockPropertyFix {
/* 10 */   private static final Map<String, String> RENAMES = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("down", "down_south")
/* 12 */     .put("up", "up_north")
/* 13 */     .put("north", "north_up")
/* 14 */     .put("south", "south_up")
/* 15 */     .put("west", "west_up")
/* 16 */     .put("east", "east_up")
/* 17 */     .build();
/*    */   
/*    */   public JigsawRotationFix(Schema outputSchema) {
/* 20 */     super(outputSchema, "jigsaw_rotation_fix");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldFix(String blockId) {
/* 25 */     return blockId.equals("minecraft:jigsaw");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixProperties(String blockId, Dynamic<T> properties) {
/* 30 */     String facing = properties.get("facing").asString("north");
/* 31 */     return 
/* 32 */       properties.remove("facing")
/* 33 */       .set("orientation", properties.createString(RENAMES.getOrDefault(facing, facing)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/JigsawRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */