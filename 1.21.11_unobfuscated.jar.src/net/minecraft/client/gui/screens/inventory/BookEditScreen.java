/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*     */ import net.minecraft.server.network.Filterable;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BookEditScreen
/*     */   extends Screen
/*     */ {
/*     */   public static final int TEXT_WIDTH = 114;
/*     */   public static final int TEXT_HEIGHT = 126;
/*     */   public static final int IMAGE_WIDTH = 192;
/*     */   public static final int IMAGE_HEIGHT = 192;
/*     */   public static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   public static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*     */   private static final int MENU_BUTTON_MARGIN = 4;
/*     */   private static final int MENU_BUTTON_SIZE = 98;
/*     */   private static final int PAGE_BUTTON_Y = 157;
/*     */   private static final int PAGE_BACK_BUTTON_X = 43;
/*     */   private static final int PAGE_FORWARD_BUTTON_X = 116;
/*     */   private static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
/*     */   private static final int PAGE_INDICATOR_X_OFFSET = 148;
/*  49 */   private static final Component TITLE = (Component)Component.translatable("book.edit.title");
/*  50 */   private static final Component SIGN_BOOK_LABEL = (Component)Component.translatable("book.signButton");
/*     */   
/*     */   private final Player owner;
/*     */   
/*     */   private final ItemStack book;
/*     */   
/*     */   private final BookSignScreen signScreen;
/*     */   private int currentPage;
/*  58 */   private final List<String> pages = Lists.newArrayList();
/*     */   
/*     */   private PageButton forwardButton;
/*     */   
/*     */   private PageButton backButton;
/*     */   
/*     */   private final InteractionHand hand;
/*  65 */   private Component numberOfPages = CommonComponents.EMPTY;
/*     */   private MultiLineEditBox page;
/*     */   
/*     */   public BookEditScreen(Player owner, ItemStack book, InteractionHand hand, WritableBookContent content) {
/*  69 */     super(TITLE);
/*  70 */     this.owner = owner;
/*  71 */     this.book = book;
/*  72 */     this.hand = hand;
/*     */     
/*  74 */     Objects.requireNonNull(this.pages); content.getPages(Minecraft.getInstance().isTextFilteringEnabled()).forEach(this.pages::add);
/*     */     
/*  76 */     if (this.pages.isEmpty()) {
/*  77 */       this.pages.add("");
/*     */     }
/*     */     
/*  80 */     this.signScreen = new BookSignScreen(this, owner, hand, this.pages);
/*     */   }
/*     */   
/*     */   private int getNumPages() {
/*  84 */     return this.pages.size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  89 */     int left = backgroundLeft();
/*  90 */     int top = backgroundTop();
/*     */     
/*  92 */     int padding = 8;
/*  93 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 101 */       .page = MultiLineEditBox.builder().setShowDecorations(false).setTextColor(-16777216).setCursorColor(-16777216).setShowBackground(false).setTextShadow(false).setX((this.width - 114) / 2 - 8).setY(28).build(this.font, 122, 134, CommonComponents.EMPTY);
/* 102 */     this.page.setCharacterLimit(1024);
/* 103 */     Objects.requireNonNull(this.font); this.page.setLineLimit(126 / 9);
/* 104 */     this.page.setValueListener(value -> this.pages.set(this.currentPage, value));
/* 105 */     addRenderableWidget((GuiEventListener)this.page);
/* 106 */     updatePageContent();
/*     */     
/* 108 */     this.numberOfPages = getPageNumberMessage();
/*     */     
/* 110 */     this.backButton = (PageButton)addRenderableWidget((GuiEventListener)new PageButton(left + 43, top + 157, false, button -> pageBack(), true));
/* 111 */     this.forwardButton = (PageButton)addRenderableWidget((GuiEventListener)new PageButton(left + 116, top + 157, true, button -> pageForward(), true));
/*     */     
/* 113 */     addRenderableWidget((GuiEventListener)Button.builder(SIGN_BOOK_LABEL, button -> this.minecraft.setScreen(this.signScreen))
/*     */         
/* 115 */         .pos(this.width / 2 - 98 - 2, menuControlsTop()).width(98).build());
/* 116 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> {
/*     */             this.minecraft.setScreen(null);
/*     */             saveChanges();
/* 119 */           }).pos(this.width / 2 + 2, menuControlsTop()).width(98).build());
/*     */     
/* 121 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   private int backgroundLeft() {
/* 125 */     return (this.width - 192) / 2;
/*     */   }
/*     */   
/*     */   private int backgroundTop() {
/* 129 */     return 2;
/*     */   }
/*     */   
/*     */   private int menuControlsTop() {
/* 133 */     return backgroundTop() + 192 + 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 138 */     setInitialFocus((GuiEventListener)this.page);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 143 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), getPageNumberMessage() });
/*     */   }
/*     */   
/*     */   private Component getPageNumberMessage() {
/* 147 */     return (Component)Component.translatable("book.pageIndicator", new Object[] { this.currentPage + 1, getNumPages() }).withColor(-16777216).withoutShadow();
/*     */   }
/*     */   
/*     */   private void pageBack() {
/* 151 */     if (this.currentPage > 0) {
/* 152 */       this.currentPage--;
/* 153 */       updatePageContent();
/*     */     } 
/* 155 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   private void pageForward() {
/* 159 */     if (this.currentPage < getNumPages() - 1) {
/* 160 */       this.currentPage++;
/*     */     } else {
/* 162 */       appendPageToBook();
/* 163 */       if (this.currentPage < getNumPages() - 1) {
/* 164 */         this.currentPage++;
/*     */       }
/*     */     } 
/* 167 */     updatePageContent();
/* 168 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   private void updatePageContent() {
/* 172 */     this.page.setValue(this.pages.get(this.currentPage), true);
/* 173 */     this.numberOfPages = getPageNumberMessage();
/*     */   }
/*     */   
/*     */   private void updateButtonVisibility() {
/* 177 */     this.backButton.visible = (this.currentPage > 0);
/*     */   }
/*     */   
/*     */   private void eraseEmptyTrailingPages() {
/* 181 */     ListIterator<String> pagesIt = this.pages.listIterator(this.pages.size());
/* 182 */     while (pagesIt.hasPrevious() && ((String)pagesIt.previous()).isEmpty()) {
/* 183 */       pagesIt.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveChanges() {
/* 188 */     eraseEmptyTrailingPages();
/*     */     
/* 190 */     updateLocalCopy();
/*     */     
/* 192 */     int slot = (this.hand == InteractionHand.MAIN_HAND) ? this.owner.getInventory().getSelectedSlot() : 40;
/* 193 */     this.minecraft.getConnection().send((Packet)new ServerboundEditBookPacket(slot, this.pages, Optional.empty()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateLocalCopy() {
/* 199 */     this.book.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(this.pages.stream().map(Filterable::passThrough).toList()));
/*     */   }
/*     */   
/*     */   private void appendPageToBook() {
/* 203 */     if (getNumPages() >= 100) {
/*     */       return;
/*     */     }
/* 206 */     this.pages.add("");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 216 */     switch (event.key()) {
/*     */       case 266:
/* 218 */         this.backButton.onPress((InputWithModifiers)event);
/* 219 */         return true;
/*     */       case 267:
/* 221 */         this.forwardButton.onPress((InputWithModifiers)event);
/* 222 */         return true;
/*     */     } 
/*     */ 
/*     */     
/* 226 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 231 */     super.render(graphics, mouseX, mouseY, a);
/* 232 */     visitText(graphics.textRenderer());
/*     */   }
/*     */   
/*     */   private void visitText(ActiveTextCollector collector) {
/* 236 */     int left = backgroundLeft();
/* 237 */     int top = backgroundTop();
/*     */     
/* 239 */     collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.numberOfPages);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 244 */     super.renderBackground(graphics, mouseX, mouseY, a);
/* 245 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BookViewScreen.BOOK_LOCATION, backgroundLeft(), backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BookEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */