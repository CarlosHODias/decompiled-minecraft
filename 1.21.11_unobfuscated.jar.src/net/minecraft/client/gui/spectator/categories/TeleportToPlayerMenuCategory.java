/*    */ package net.minecraft.client.gui.spectator.categories;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*    */ import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
/*    */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class TeleportToPlayerMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {
/*    */   private static final Comparator<PlayerInfo> PROFILE_ORDER;
/* 22 */   private static final Identifier TELEPORT_TO_PLAYER_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_player"); static {
/* 23 */     PROFILE_ORDER = Comparator.comparing(p -> p.getProfile().id());
/* 24 */   } private static final Component TELEPORT_TEXT = (Component)Component.translatable("spectatorMenu.teleport");
/* 25 */   private static final Component TELEPORT_PROMPT = (Component)Component.translatable("spectatorMenu.teleport.prompt");
/*    */   
/*    */   private final List<SpectatorMenuItem> items;
/*    */   
/*    */   public TeleportToPlayerMenuCategory() {
/* 30 */     this(Minecraft.getInstance().getConnection().getListedOnlinePlayers());
/*    */   }
/*    */   
/*    */   public TeleportToPlayerMenuCategory(Collection<PlayerInfo> profiles) {
/* 34 */     this
/*    */ 
/*    */ 
/*    */       
/* 38 */       .items = (List<SpectatorMenuItem>)profiles.stream().filter(p -> (p.getGameMode() != GameType.SPECTATOR)).sorted(PROFILE_ORDER).map(net.minecraft.client.gui.spectator.PlayerMenuItem::new).collect(Collectors.toUnmodifiableList());
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SpectatorMenuItem> getItems() {
/* 43 */     return this.items;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrompt() {
/* 48 */     return TELEPORT_PROMPT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void selectItem(SpectatorMenu menu) {
/* 53 */     menu.selectCategory(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName() {
/* 58 */     return TELEPORT_TEXT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/* 63 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_PLAYER_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled() {
/* 68 */     return !this.items.isEmpty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/categories/TeleportToPlayerMenuCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */