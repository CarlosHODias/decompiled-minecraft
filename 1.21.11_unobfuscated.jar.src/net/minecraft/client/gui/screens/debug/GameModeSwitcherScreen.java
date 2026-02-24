/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.MultiPlayerGameMode;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class GameModeSwitcherScreen extends Screen {
/*  29 */   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/slot");
/*  30 */   private static final Identifier SELECTION_SPRITE = Identifier.withDefaultNamespace("gamemode_switcher/selection");
/*     */   
/*  32 */   private enum GameModeIcon { CREATIVE(Component.translatable("gameMode.creative"), GameType.CREATIVE, new ItemStack((ItemLike)Blocks.GRASS_BLOCK)),
/*  33 */     SURVIVAL(Component.translatable("gameMode.survival"), GameType.SURVIVAL, new ItemStack((ItemLike)Items.IRON_SWORD)),
/*  34 */     ADVENTURE(Component.translatable("gameMode.adventure"), GameType.ADVENTURE, new ItemStack((ItemLike)Items.MAP)),
/*  35 */     SPECTATOR(Component.translatable("gameMode.spectator"), GameType.SPECTATOR, new ItemStack((ItemLike)Items.ENDER_EYE));
/*     */     
/*  37 */     private static final GameModeIcon[] VALUES = values();
/*     */     
/*     */     private static final int ICON_AREA = 16;
/*     */     private static final int ICON_TOP_LEFT = 5;
/*     */     private final Component name;
/*     */     private final GameType mode;
/*     */     private final ItemStack renderStack;
/*     */     
/*     */     GameModeIcon(Component name, GameType mode, ItemStack renderStack) {
/*  46 */       this.name = name;
/*  47 */       this.mode = mode;
/*  48 */       this.renderStack = renderStack;
/*     */     }
/*     */     
/*     */     private void drawIcon(GuiGraphics graphics, int x, int y) {
/*  52 */       graphics.renderItem(this.renderStack, x, y);
/*     */     }
/*     */     
/*     */     private GameModeIcon getNext() {
/*  56 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: case 3: break; }  return 
/*     */ 
/*     */ 
/*     */         
/*  60 */         CREATIVE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static GameModeIcon getFromGameType(GameType gameType) {
/*     */       // Byte code:
/*     */       //   0: getstatic net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$1.$SwitchMap$net$minecraft$world$level$GameType : [I
/*     */       //   3: aload_0
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: tableswitch default -> 40, 1 -> 50, 2 -> 56, 3 -> 62, 4 -> 68
/*     */       //   40: new java/lang/MatchException
/*     */       //   43: dup
/*     */       //   44: aconst_null
/*     */       //   45: aconst_null
/*     */       //   46: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   49: athrow
/*     */       //   50: getstatic net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon.SPECTATOR : Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;
/*     */       //   53: goto -> 71
/*     */       //   56: getstatic net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon.SURVIVAL : Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;
/*     */       //   59: goto -> 71
/*     */       //   62: getstatic net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon.CREATIVE : Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;
/*     */       //   65: goto -> 71
/*     */       //   68: getstatic net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon.ADVENTURE : Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;
/*     */       //   71: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       //   #66	-> 50
/*     */       //   #67	-> 56
/*     */       //   #68	-> 62
/*     */       //   #69	-> 68
/*     */       //   #65	-> 71
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	72	0	gameType	Lnet/minecraft/world/level/GameType;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static final Identifier GAMEMODE_SWITCHER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/gamemode_switcher.png");
/*     */   
/*     */   private static final int SPRITE_SHEET_WIDTH = 128;
/*     */   private static final int SPRITE_SHEET_HEIGHT = 128;
/*     */   private static final int SLOT_AREA = 26;
/*     */   private static final int SLOT_PADDING = 5;
/*     */   private static final int SLOT_AREA_PADDED = 31;
/*     */   private static final int HELP_TIPS_OFFSET_Y = 5;
/*  82 */   private static final int ALL_SLOTS_WIDTH = (GameModeIcon.values()).length * 31 - 5;
/*     */   
/*     */   private final GameModeIcon previousHovered;
/*     */   
/*     */   private GameModeIcon currentlyHovered;
/*     */   
/*     */   private int firstMouseX;
/*     */   private int firstMouseY;
/*     */   private boolean setFirstMousePos;
/*  91 */   private final List<GameModeSlot> slots = Lists.newArrayList();
/*     */   
/*     */   public GameModeSwitcherScreen() {
/*  94 */     super(GameNarrator.NO_TITLE);
/*     */     
/*  96 */     this.previousHovered = GameModeIcon.getFromGameType(getDefaultSelected());
/*  97 */     this.currentlyHovered = this.previousHovered;
/*     */   }
/*     */   
/*     */   private GameType getDefaultSelected() {
/* 101 */     MultiPlayerGameMode gameMode = (Minecraft.getInstance()).gameMode;
/* 102 */     GameType previous = gameMode.getPreviousPlayerMode();
/* 103 */     if (previous != null) {
/* 104 */       return previous;
/*     */     }
/* 106 */     return (gameMode.getPlayerMode() == GameType.CREATIVE) ? GameType.SURVIVAL : GameType.CREATIVE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 111 */     super.init();
/*     */     
/* 113 */     this.slots.clear();
/* 114 */     this.currentlyHovered = this.previousHovered;
/*     */     
/* 116 */     for (int i = 0; i < GameModeIcon.VALUES.length; i++) {
/* 117 */       GameModeIcon icon = GameModeIcon.VALUES[i];
/* 118 */       this.slots.add(new GameModeSlot(icon, this.width / 2 - ALL_SLOTS_WIDTH / 2 + i * 31, this.height / 2 - 31));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 125 */     graphics.drawCenteredString(this.font, this.currentlyHovered.name, this.width / 2, this.height / 2 - 31 - 20, -1);
/*     */ 
/*     */     
/* 128 */     MutableComponent selectKey = Component.translatable("debug.gamemodes.select_next", new Object[] { this.minecraft.options.keyDebugSwitchGameMode.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.AQUA) });
/* 129 */     graphics.drawCenteredString(this.font, (Component)selectKey, this.width / 2, this.height / 2 + 5, -1);
/*     */     
/* 131 */     if (!this.setFirstMousePos) {
/* 132 */       this.firstMouseX = mouseX;
/* 133 */       this.firstMouseY = mouseY;
/*     */       
/* 135 */       this.setFirstMousePos = true;
/*     */     } 
/*     */     
/* 138 */     boolean sameAsFirstMousePos = (this.firstMouseX == mouseX && this.firstMouseY == mouseY);
/*     */     
/* 140 */     for (GameModeSlot slot : this.slots) {
/* 141 */       slot.render(graphics, mouseX, mouseY, a);
/*     */       
/* 143 */       slot.setSelected((this.currentlyHovered == slot.icon));
/*     */       
/* 145 */       if (!sameAsFirstMousePos && slot.isHoveredOrFocused()) {
/* 146 */         this.currentlyHovered = slot.icon;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 153 */     int xo = this.width / 2 - 62;
/* 154 */     int yo = this.height / 2 - 31 - 27;
/* 155 */     graphics.blit(RenderPipelines.GUI_TEXTURED, GAMEMODE_SWITCHER_LOCATION, xo, yo, 0.0F, 0.0F, 125, 75, 128, 128);
/*     */   }
/*     */   
/*     */   private void switchToHoveredGameMode() {
/* 159 */     switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
/*     */   }
/*     */   
/*     */   private static void switchToHoveredGameMode(Minecraft minecraft, GameModeIcon toGameMode) {
/* 163 */     if (!minecraft.canSwitchGameMode()) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     GameModeIcon currentGameMode = GameModeIcon.getFromGameType(minecraft.gameMode.getPlayerMode());
/*     */     
/* 169 */     if (toGameMode != currentGameMode && GameModeCommand.PERMISSION_CHECK.check(minecraft.player.permissions())) {
/* 170 */       minecraft.player.connection.send((Packet)new ServerboundChangeGameModePacket(toGameMode.mode));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 176 */     if (this.minecraft.options.keyDebugSwitchGameMode.matches(event)) {
/* 177 */       this.setFirstMousePos = false;
/* 178 */       this.currentlyHovered = this.currentlyHovered.getNext();
/* 179 */       return true;
/*     */     } 
/*     */     
/* 182 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyReleased(KeyEvent event) {
/* 187 */     if (this.minecraft.options.keyDebugModifier.matches(event)) {
/* 188 */       switchToHoveredGameMode();
/* 189 */       this.minecraft.setScreen(null);
/* 190 */       return true;
/*     */     } 
/*     */     
/* 193 */     return super.keyReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 198 */     if (this.minecraft.options.keyDebugModifier.matchesMouse(event)) {
/* 199 */       switchToHoveredGameMode();
/* 200 */       this.minecraft.setScreen(null);
/* 201 */       return true;
/*     */     } 
/* 203 */     return super.mouseReleased(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 208 */     return false;
/*     */   }
/*     */   
/*     */   public static class GameModeSlot extends AbstractWidget {
/*     */     private final GameModeSwitcherScreen.GameModeIcon icon;
/*     */     private boolean isSelected;
/*     */     
/*     */     public GameModeSlot(GameModeSwitcherScreen.GameModeIcon icon, int x, int y) {
/* 216 */       super(x, y, 26, 26, icon.name);
/* 217 */       this.icon = icon;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 222 */       drawSlot(graphics);
/* 223 */       if (this.isSelected) {
/* 224 */         drawSelection(graphics);
/*     */       }
/* 226 */       this.icon.drawIcon(graphics, getX() + 5, getY() + 5);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateWidgetNarration(NarrationElementOutput output) {
/* 231 */       defaultButtonNarrationText(output);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHoveredOrFocused() {
/* 236 */       return (super.isHoveredOrFocused() || this.isSelected);
/*     */     }
/*     */     
/*     */     public void setSelected(boolean isSelected) {
/* 240 */       this.isSelected = isSelected;
/*     */     }
/*     */     
/*     */     private void drawSlot(GuiGraphics graphics) {
/* 244 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SLOT_SPRITE, getX(), getY(), 26, 26);
/*     */     }
/*     */     
/*     */     private void drawSelection(GuiGraphics graphics) {
/* 248 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SELECTION_SPRITE, getX(), getY(), 26, 26);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */