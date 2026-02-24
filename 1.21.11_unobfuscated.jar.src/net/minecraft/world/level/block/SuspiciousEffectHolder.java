/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface SuspiciousEffectHolder
/*    */ {
/*    */   SuspiciousStewEffects getSuspiciousEffects();
/*    */   
/*    */   static List<SuspiciousEffectHolder> getAllEffectHolders() {
/* 17 */     return (List<SuspiciousEffectHolder>)BuiltInRegistries.ITEM.stream().map(SuspiciousEffectHolder::tryGet).filter(Objects::nonNull).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   static SuspiciousEffectHolder tryGet(ItemLike item) {
/* 21 */     Item item2 = item.asItem(); if (item2 instanceof BlockItem) { BlockItem blockItem = (BlockItem)item2; Block block = blockItem.getBlock(); if (block instanceof SuspiciousEffectHolder) { SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)block;
/* 22 */         return effectHolder; }
/*    */        }
/* 24 */      Item item1 = item.asItem(); if (item1 instanceof SuspiciousEffectHolder) { SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)item1;
/* 25 */       return effectHolder; }
/*    */     
/* 27 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SuspiciousEffectHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */