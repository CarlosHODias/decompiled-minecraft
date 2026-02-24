/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.tags.DialogTags;
/*    */ 
/*    */ public class DialogTagsProvider
/*    */   extends KeyTagProvider<Dialog> {
/*    */   public DialogTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 13 */     super(output, Registries.DIALOG, lookupProvider);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(DialogTags.PAUSE_SCREEN_ADDITIONS);
/*    */     
/* 21 */     tag(DialogTags.QUICK_ACTIONS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/DialogTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */