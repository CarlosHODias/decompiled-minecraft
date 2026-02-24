/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class BookSignScreen
/*     */   extends Screen {
/*  24 */   private static final Component EDIT_TITLE_LABEL = (Component)Component.translatable("book.editTitle");
/*  25 */   private static final Component FINALIZE_WARNING_LABEL = (Component)Component.translatable("book.finalizeWarning");
/*  26 */   private static final Component TITLE = (Component)Component.translatable("book.sign.title");
/*  27 */   private static final Component TITLE_EDIT_BOX = (Component)Component.translatable("book.sign.titlebox");
/*     */   
/*     */   private final BookEditScreen bookEditScreen;
/*     */   
/*     */   private final Player owner;
/*     */   
/*     */   private final List<String> pages;
/*     */   
/*     */   private final InteractionHand hand;
/*     */   private final Component ownerText;
/*     */   private EditBox titleBox;
/*  38 */   private String titleValue = "";
/*     */   
/*     */   public BookSignScreen(BookEditScreen bookEditScreen, Player owner, InteractionHand hand, List<String> pages) {
/*  41 */     super(TITLE);
/*  42 */     this.bookEditScreen = bookEditScreen;
/*  43 */     this.owner = owner;
/*  44 */     this.hand = hand;
/*  45 */     this.pages = pages;
/*     */     
/*  47 */     this.ownerText = (Component)Component.translatable("book.byAuthor", new Object[] { owner.getName() }).withStyle(ChatFormatting.DARK_GRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  52 */     Button finalizeButton = Button.builder((Component)Component.translatable("book.finalizeButton"), button -> {
/*     */           saveChanges();
/*     */           this.minecraft.setScreen(null);
/*  55 */         }).bounds(this.width / 2 - 100, 196, 98, 20).build();
/*  56 */     finalizeButton.active = false;
/*     */     
/*  58 */     this.titleBox = (EditBox)addRenderableWidget((GuiEventListener)new EditBox(this.minecraft.font, (this.width - 114) / 2 - 3, 50, 114, 20, TITLE_EDIT_BOX));
/*  59 */     this.titleBox.setMaxLength(15);
/*  60 */     this.titleBox.setBordered(false);
/*  61 */     this.titleBox.setCentered(true);
/*  62 */     this.titleBox.setTextColor(-16777216);
/*  63 */     this.titleBox.setTextShadow(false);
/*  64 */     this.titleBox.setResponder(value -> finalizeButton.active = !StringUtil.isBlank(value));
/*  65 */     this.titleBox.setValue(this.titleValue);
/*     */     
/*  67 */     addRenderableWidget((GuiEventListener)finalizeButton);
/*  68 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */             this.titleValue = this.titleBox.getValue();
/*     */             this.minecraft.setScreen(this.bookEditScreen);
/*  71 */           }).bounds(this.width / 2 + 2, 196, 98, 20).build());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  76 */     setInitialFocus((GuiEventListener)this.titleBox);
/*     */   }
/*     */   
/*     */   private void saveChanges() {
/*  80 */     int slot = (this.hand == InteractionHand.MAIN_HAND) ? this.owner.getInventory().getSelectedSlot() : 40;
/*  81 */     this.minecraft.getConnection().send((Packet)new ServerboundEditBookPacket(slot, this.pages, Optional.of(this.titleBox.getValue().trim())));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  91 */     if (this.titleBox.isFocused() && !this.titleBox.getValue().isEmpty() && event.isConfirmation()) {
/*  92 */       saveChanges();
/*  93 */       this.minecraft.setScreen(null);
/*  94 */       return true;
/*     */     } 
/*  96 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 101 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 103 */     int xo = (this.width - 192) / 2;
/* 104 */     int yo = 2;
/*     */     
/* 106 */     int titleHeaderWidth = this.font.width((FormattedText)EDIT_TITLE_LABEL);
/* 107 */     graphics.drawString(this.font, EDIT_TITLE_LABEL, xo + 36 + (114 - titleHeaderWidth) / 2, 34, -16777216, false);
/*     */     
/* 109 */     int nameWidth = this.font.width((FormattedText)this.ownerText);
/* 110 */     graphics.drawString(this.font, this.ownerText, xo + 36 + (114 - nameWidth) / 2, 60, -16777216, false);
/*     */     
/* 112 */     graphics.drawWordWrap(this.font, (FormattedText)FINALIZE_WARNING_LABEL, xo + 36, 82, 114, -16777216, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 117 */     super.renderBackground(graphics, mouseX, mouseY, a);
/* 118 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BookViewScreen.BOOK_LOCATION, (this.width - 192) / 2, 2, 0.0F, 0.0F, 192, 192, 256, 256);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BookSignScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */