/*    */ package net.minecraft.client.data.models.model;
/*    */ 
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ModelLocationUtils
/*    */ {
/*    */   @Deprecated
/*    */   public static Identifier decorateBlockModelLocation(String id) {
/* 12 */     return Identifier.withDefaultNamespace("block/" + id);
/*    */   }
/*    */   
/*    */   public static Identifier decorateItemModelLocation(String id) {
/* 16 */     return Identifier.withDefaultNamespace("item/" + id);
/*    */   }
/*    */   
/*    */   public static Identifier getModelLocation(Block block, String suffix) {
/* 20 */     Identifier key = BuiltInRegistries.BLOCK.getKey(block);
/* 21 */     return key.withPath(path -> "block/" + path + suffix);
/*    */   }
/*    */   
/*    */   public static Identifier getModelLocation(Block block) {
/* 25 */     Identifier key = BuiltInRegistries.BLOCK.getKey(block);
/* 26 */     return key.withPrefix("block/");
/*    */   }
/*    */   
/*    */   public static Identifier getModelLocation(Item item) {
/* 30 */     Identifier key = BuiltInRegistries.ITEM.getKey(item);
/* 31 */     return key.withPrefix("item/");
/*    */   }
/*    */   
/*    */   public static Identifier getModelLocation(Item item, String suffix) {
/* 35 */     Identifier key = BuiltInRegistries.ITEM.getKey(item);
/* 36 */     return key.withPath(path -> "item/" + path + suffix);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/ModelLocationUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */