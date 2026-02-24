/*    */ package net.minecraft.references;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public class Items {
/*  9 */   public static final ResourceKey<Item> PUMPKIN_SEEDS = createKey("pumpkin_seeds");
/* 10 */   public static final ResourceKey<Item> MELON_SEEDS = createKey("melon_seeds");
/*    */   
/*    */   private static ResourceKey<Item> createKey(String name) {
/* 13 */     return ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/references/Items.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */