/*     */ package net.minecraft.client.gui.spectator.categories;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.PlayerFaceRenderer;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenuItem;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ public class TeleportToTeamMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {
/*  26 */   private static final Identifier TELEPORT_TO_TEAM_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_team");
/*  27 */   private static final Component TELEPORT_TEXT = (Component)Component.translatable("spectatorMenu.team_teleport");
/*  28 */   private static final Component TELEPORT_PROMPT = (Component)Component.translatable("spectatorMenu.team_teleport.prompt");
/*     */   
/*     */   private final List<SpectatorMenuItem> items;
/*     */   
/*     */   public TeleportToTeamMenuCategory() {
/*  33 */     Minecraft minecraft = Minecraft.getInstance();
/*  34 */     this.items = createTeamEntries(minecraft, minecraft.level.getScoreboard());
/*     */   }
/*     */   
/*     */   private static List<SpectatorMenuItem> createTeamEntries(Minecraft minecraft, Scoreboard scoreboard) {
/*  38 */     return scoreboard.getPlayerTeams().stream().flatMap(team -> TeamSelectionItem.create(minecraft, team).stream()).toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SpectatorMenuItem> getItems() {
/*  43 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrompt() {
/*  48 */     return TELEPORT_PROMPT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void selectItem(SpectatorMenu menu) {
/*  53 */     menu.selectCategory(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  58 */     return TELEPORT_TEXT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/*  63 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_TEAM_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/*  68 */     return !this.items.isEmpty();
/*     */   }
/*     */   
/*     */   private static class TeamSelectionItem implements SpectatorMenuItem {
/*     */     private final PlayerTeam team;
/*     */     private final Supplier<PlayerSkin> iconSkin;
/*     */     private final List<PlayerInfo> players;
/*     */     
/*     */     private TeamSelectionItem(PlayerTeam team, List<PlayerInfo> players, Supplier<PlayerSkin> iconSkin) {
/*  77 */       this.team = team;
/*  78 */       this.players = players;
/*  79 */       this.iconSkin = iconSkin;
/*     */     }
/*     */     
/*     */     public static Optional<SpectatorMenuItem> create(Minecraft minecraft, PlayerTeam team) {
/*  83 */       List<PlayerInfo> players = new ArrayList<>();
/*  84 */       for (String name : (Iterable<String>)team.getPlayers()) {
/*  85 */         PlayerInfo info = minecraft.getConnection().getPlayerInfo(name);
/*     */ 
/*     */         
/*  88 */         if (info != null && info.getGameMode() != GameType.SPECTATOR) {
/*  89 */           players.add(info);
/*     */         }
/*     */       } 
/*     */       
/*  93 */       if (players.isEmpty()) {
/*  94 */         return Optional.empty();
/*     */       }
/*     */       
/*  97 */       PlayerInfo playerInfo = players.get(RandomSource.create().nextInt(players.size()));
/*  98 */       Objects.requireNonNull(playerInfo); return Optional.of(new TeamSelectionItem(team, players, playerInfo::getSkin));
/*     */     }
/*     */ 
/*     */     
/*     */     public void selectItem(SpectatorMenu menu) {
/* 103 */       menu.selectCategory(new TeleportToPlayerMenuCategory(this.players));
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getName() {
/* 108 */       return this.team.getDisplayName();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderIcon(GuiGraphics graphics, float brightness, float alpha) {
/* 113 */       Integer teamColor = this.team.getColor().getColor();
/*     */       
/* 115 */       if (teamColor != null) {
/* 116 */         float red = (teamColor >> 16 & 0xFF) / 255.0F;
/* 117 */         float green = (teamColor >> 8 & 0xFF) / 255.0F;
/* 118 */         float blue = (teamColor & 0xFF) / 255.0F;
/* 119 */         graphics.fill(1, 1, 15, 15, ARGB.colorFromFloat(alpha, red * brightness, green * brightness, blue * brightness));
/*     */       } 
/*     */       
/* 122 */       PlayerFaceRenderer.draw(graphics, this.iconSkin.get(), 2, 2, 12, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled() {
/* 127 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/categories/TeleportToTeamMenuCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */