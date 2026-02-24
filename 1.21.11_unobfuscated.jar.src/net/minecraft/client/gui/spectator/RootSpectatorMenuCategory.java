/*    */ package net.minecraft.client.gui.spectator;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
/*    */ import net.minecraft.client.gui.spectator.categories.TeleportToTeamMenuCategory;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RootSpectatorMenuCategory
/*    */   implements SpectatorMenuCategory {
/* 11 */   private static final Component PROMPT_TEXT = (Component)Component.translatable("spectatorMenu.root.prompt");
/* 12 */   private final List<SpectatorMenuItem> items = Lists.newArrayList();
/*    */   
/*    */   public RootSpectatorMenuCategory() {
/* 15 */     this.items.add(new TeleportToPlayerMenuCategory());
/* 16 */     this.items.add(new TeleportToTeamMenuCategory());
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SpectatorMenuItem> getItems() {
/* 21 */     return this.items;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrompt() {
/* 26 */     return PROMPT_TEXT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/RootSpectatorMenuCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */