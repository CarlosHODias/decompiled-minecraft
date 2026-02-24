/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class DeathScreen
/*     */   extends Screen
/*     */ {
/*     */   private static final int TITLE_SCALE = 2;
/*  26 */   private static final Identifier DRAFT_REPORT_SPRITE = Identifier.withDefaultNamespace("icon/draft_report");
/*     */   
/*     */   private int delayTicker;
/*     */   private final Component causeOfDeath;
/*     */   private final boolean hardcore;
/*     */   private final LocalPlayer player;
/*     */   private final Component deathScore;
/*  33 */   private final List<Button> exitButtons = Lists.newArrayList();
/*     */   
/*     */   private Button exitToTitleButton;
/*     */   
/*     */   public DeathScreen(Component causeOfDeath, boolean hardcore, LocalPlayer player) {
/*  38 */     super((Component)Component.translatable(hardcore ? "deathScreen.title.hardcore" : "deathScreen.title"));
/*  39 */     this.causeOfDeath = causeOfDeath;
/*  40 */     this.hardcore = hardcore;
/*  41 */     this.player = player;
/*  42 */     MutableComponent mutableComponent = Component.literal(Integer.toString(player.getScore())).withStyle(ChatFormatting.YELLOW);
/*  43 */     this.deathScore = (Component)Component.translatable("deathScreen.score.value", new Object[] { mutableComponent });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  48 */     this.delayTicker = 0;
/*  49 */     this.exitButtons.clear();
/*  50 */     MutableComponent mutableComponent = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");
/*  51 */     this.exitButtons.add(addRenderableWidget(Button.builder((Component)mutableComponent, button -> {
/*     */               this.player.respawn();
/*     */               button.active = false;
/*  54 */             }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
/*     */     
/*  56 */     this.exitToTitleButton = addRenderableWidget(Button.builder((Component)Component.translatable("deathScreen.titleScreen"), button -> this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true))
/*     */         
/*  58 */         .bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
/*  59 */     this.exitButtons.add(this.exitToTitleButton);
/*     */     
/*  61 */     setButtonsActive(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   private void handleExitToTitleScreen() {
/*  70 */     if (this.hardcore) {
/*  71 */       exitToTitleScreen();
/*     */       return;
/*     */     } 
/*  74 */     ConfirmScreen confirm = new TitleConfirmScreen(result -> {
/*     */           if (result) {
/*     */             exitToTitleScreen();
/*     */           } else {
/*     */             this.player.respawn();
/*     */             this.minecraft.setScreen(null);
/*     */           } 
/*  81 */         }, (Component)Component.translatable("deathScreen.quit.confirm"), CommonComponents.EMPTY, (Component)Component.translatable("deathScreen.titleScreen"), (Component)Component.translatable("deathScreen.respawn"));
/*  82 */     this.minecraft.setScreen(confirm);
/*  83 */     confirm.setDelay(20);
/*     */   }
/*     */   
/*     */   private void exitToTitleScreen() {
/*  87 */     if (this.minecraft.level != null) {
/*  88 */       this.minecraft.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
/*     */     }
/*  90 */     this.minecraft.disconnectWithSavingScreen();
/*  91 */     this.minecraft.setScreen(new TitleScreen());
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  96 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  98 */     visitText(graphics.textRenderer(GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR));
/*     */     
/* 100 */     if (this.exitToTitleButton != null && this.minecraft.getReportingContext().hasDraftReport()) {
/* 101 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_SPRITE, this.exitToTitleButton.getX() + this.exitToTitleButton.getWidth() - 17, this.exitToTitleButton.getY() + 3, 15, 15);
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitText(ActiveTextCollector output) {
/* 106 */     ActiveTextCollector.Parameters normalParameters = output.defaultParameters();
/*     */     
/* 108 */     int middleLine = this.width / 2;
/* 109 */     output.defaultParameters(normalParameters.withScale(2.0F));
/* 110 */     output.accept(TextAlignment.CENTER, middleLine / 2, 30, this.title);
/* 111 */     output.defaultParameters(normalParameters);
/*     */     
/* 113 */     if (this.causeOfDeath != null) {
/* 114 */       output.accept(TextAlignment.CENTER, middleLine, 85, this.causeOfDeath);
/*     */     }
/*     */     
/* 117 */     output.accept(TextAlignment.CENTER, middleLine, 100, this.deathScore);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 122 */     renderDeathBackground(graphics, this.width, this.height);
/*     */   }
/*     */   
/*     */   private static void renderDeathBackground(GuiGraphics graphics, int width, int height) {
/* 126 */     graphics.fillGradient(0, 0, width, height, 1615855616, -1602211792);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 131 */     ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(getFont(), (int)event.x(), (int)event.y());
/* 132 */     visitText((ActiveTextCollector)finder);
/* 133 */     Style clickedStyle = finder.result();
/* 134 */     if (clickedStyle != null) { ClickEvent clickEvent = clickedStyle.getClickEvent(); if (clickEvent instanceof ClickEvent.OpenUrl) { ClickEvent.OpenUrl openUrl = (ClickEvent.OpenUrl)clickEvent;
/* 135 */         return clickUrlAction(this.minecraft, this, openUrl.uri()); }
/*     */        }
/* 137 */      return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 152 */     super.tick();
/*     */     
/* 154 */     this.delayTicker++;
/* 155 */     if (this.delayTicker == 20) {
/* 156 */       setButtonsActive(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setButtonsActive(boolean isActive) {
/* 161 */     for (Button button : this.exitButtons)
/* 162 */       button.active = isActive; 
/*     */   }
/*     */   
/*     */   public static class TitleConfirmScreen
/*     */     extends ConfirmScreen {
/*     */     public TitleConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButton, Component noButton) {
/* 168 */       super(callback, title, message, yesButton, noButton);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 173 */       DeathScreen.renderDeathBackground(graphics, this.width, this.height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/DeathScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */