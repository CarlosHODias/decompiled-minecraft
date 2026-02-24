/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.advancements.AdvancementProvider;
/*    */ 
/*    */ public class VanillaAdvancementProvider
/*    */ {
/*    */   public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 12 */     return new AdvancementProvider(output, registries, 
/*    */ 
/*    */         
/* 15 */         List.of(new VanillaTheEndAdvancements(), new VanillaHusbandryAdvancements(), new VanillaAdventureAdvancements(), new VanillaNetherAdvancements(), new VanillaStoryAdvancements()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/advancements/packs/VanillaAdvancementProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */