/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BookViewScreen
/*     */   extends Screen
/*     */ {
/*     */   public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
/*     */   public static final int PAGE_TEXT_X_OFFSET = 36;
/*     */   public static final int PAGE_TEXT_Y_OFFSET = 30;
/*     */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*     */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*  43 */   private static final Component TITLE = (Component)Component.translatable("book.view.title");
/*     */   
/*  45 */   private static final Style PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);
/*     */   public static final class BookAccess extends Record { private final List<Component> pages;
/*  47 */     public BookAccess(List<Component> pages) { this.pages = pages; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  47 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess; } public List<Component> pages() { return this.pages; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen$BookAccess;
/*  49 */       //   0	8	1	o	Ljava/lang/Object; } public int getPageCount() { return this.pages.size(); }
/*     */ 
/*     */     
/*     */     public Component getPage(int page) {
/*  53 */       if (page >= 0 && page < getPageCount()) {
/*  54 */         return this.pages.get(page);
/*     */       }
/*  56 */       return CommonComponents.EMPTY;
/*     */     }
/*     */     
/*     */     public static BookAccess fromItem(ItemStack itemStack) {
/*  60 */       boolean filterEnabled = Minecraft.getInstance().isTextFilteringEnabled();
/*  61 */       WrittenBookContent writtenContent = (WrittenBookContent)itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  62 */       if (writtenContent != null) {
/*  63 */         return new BookAccess(writtenContent.getPages(filterEnabled));
/*     */       }
/*  65 */       WritableBookContent writableContent = (WritableBookContent)itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
/*  66 */       if (writableContent != null) {
/*  67 */         return new BookAccess(writableContent.getPages(filterEnabled).map(Component::literal).toList());
/*     */       }
/*  69 */       return null;
/*     */     } }
/*     */ 
/*     */   
/*  73 */   public static final BookAccess EMPTY_ACCESS = new BookAccess(List.of());
/*     */   
/*  75 */   public static final Identifier BOOK_LOCATION = Identifier.withDefaultNamespace("textures/gui/book.png");
/*     */   
/*     */   protected static final int TEXT_WIDTH = 114;
/*     */   
/*     */   protected static final int TEXT_HEIGHT = 128;
/*     */   
/*     */   protected static final int IMAGE_WIDTH = 192;
/*     */   
/*     */   private static final int PAGE_INDICATOR_X_OFFSET = 148;
/*     */   protected static final int IMAGE_HEIGHT = 192;
/*     */   private static final int PAGE_BUTTON_Y = 157;
/*     */   private static final int PAGE_BACK_BUTTON_X = 43;
/*     */   private static final int PAGE_FORWARD_BUTTON_X = 116;
/*     */   private BookAccess bookAccess;
/*     */   private int currentPage;
/*  90 */   private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
/*  91 */   private int cachedPage = -1;
/*  92 */   private Component pageMsg = CommonComponents.EMPTY;
/*     */   
/*     */   private PageButton forwardButton;
/*     */   
/*     */   private PageButton backButton;
/*     */   private final boolean playTurnSound;
/*     */   
/*     */   public BookViewScreen(BookAccess bookAccess) {
/* 100 */     this(bookAccess, true);
/*     */   }
/*     */   
/*     */   public BookViewScreen() {
/* 104 */     this(EMPTY_ACCESS, false);
/*     */   }
/*     */   
/*     */   private BookViewScreen(BookAccess bookAccess, boolean playTurnSound) {
/* 108 */     super(TITLE);
/* 109 */     this.bookAccess = bookAccess;
/* 110 */     this.playTurnSound = playTurnSound;
/*     */   }
/*     */   
/*     */   public void setBookAccess(BookAccess bookAccess) {
/* 114 */     this.bookAccess = bookAccess;
/* 115 */     this.currentPage = Mth.clamp(this.currentPage, 0, bookAccess.getPageCount());
/* 116 */     updateButtonVisibility();
/* 117 */     this.cachedPage = -1;
/*     */   }
/*     */   
/*     */   public boolean setPage(int page) {
/* 121 */     int clampedPage = Mth.clamp(page, 0, this.bookAccess.getPageCount() - 1);
/* 122 */     if (clampedPage != this.currentPage) {
/* 123 */       this.currentPage = clampedPage;
/* 124 */       updateButtonVisibility();
/* 125 */       this.cachedPage = -1;
/* 126 */       return true;
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean forcePage(int page) {
/* 132 */     return setPage(page);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 137 */     createMenuControls();
/* 138 */     createPageControlButtons();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 143 */     return CommonComponents.joinLines(new Component[] { super.getNarrationMessage(), getPageNumberMessage(), this.bookAccess.getPage(this.currentPage) });
/*     */   }
/*     */   
/*     */   private Component getPageNumberMessage() {
/* 147 */     return (Component)Component.translatable("book.pageIndicator", new Object[] { this.currentPage + 1, Math.max(getNumPages(), 1) }).withStyle(PAGE_TEXT_STYLE);
/*     */   }
/*     */   
/*     */   protected void createMenuControls() {
/* 151 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).pos((this.width - 200) / 2, menuControlsTop()).width(200).build());
/*     */   }
/*     */   
/*     */   protected void createPageControlButtons() {
/* 155 */     int left = backgroundLeft();
/* 156 */     int top = backgroundTop();
/*     */     
/* 158 */     this.forwardButton = (PageButton)addRenderableWidget((GuiEventListener)new PageButton(left + 116, top + 157, true, button -> pageForward(), this.playTurnSound));
/* 159 */     this.backButton = (PageButton)addRenderableWidget((GuiEventListener)new PageButton(left + 43, top + 157, false, button -> pageBack(), this.playTurnSound));
/*     */     
/* 161 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   private int getNumPages() {
/* 165 */     return this.bookAccess.getPageCount();
/*     */   }
/*     */   
/*     */   protected void pageBack() {
/* 169 */     if (this.currentPage > 0) {
/* 170 */       this.currentPage--;
/*     */     }
/* 172 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   protected void pageForward() {
/* 176 */     if (this.currentPage < getNumPages() - 1) {
/* 177 */       this.currentPage++;
/*     */     }
/* 179 */     updateButtonVisibility();
/*     */   }
/*     */   
/*     */   private void updateButtonVisibility() {
/* 183 */     this.forwardButton.visible = (this.currentPage < getNumPages() - 1);
/* 184 */     this.backButton.visible = (this.currentPage > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 189 */     if (super.keyPressed(event)) {
/* 190 */       return true;
/*     */     }
/*     */     
/* 193 */     switch (event.key()) {
/*     */       case 266:
/* 195 */         this.backButton.onPress((InputWithModifiers)event);
/*     */ 
/*     */       
/*     */       case 267:
/* 199 */         this.forwardButton.onPress((InputWithModifiers)event);
/*     */       default:
/*     */         break;
/*     */     } 
/*     */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 208 */     super.render(graphics, mouseX, mouseY, a);
/* 209 */     visitText(graphics.textRenderer(GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR), false);
/*     */   }
/*     */   
/*     */   private void visitText(ActiveTextCollector collector, boolean clickableOnly) {
/* 213 */     if (this.cachedPage != this.currentPage) {
/* 214 */       Component component = ComponentUtils.mergeStyles(this.bookAccess.getPage(this.currentPage), PAGE_TEXT_STYLE);
/* 215 */       this.cachedPageComponents = this.font.split((FormattedText)component, 114);
/* 216 */       this.pageMsg = getPageNumberMessage();
/* 217 */       this.cachedPage = this.currentPage;
/*     */     } 
/*     */     
/* 220 */     int left = backgroundLeft();
/* 221 */     int top = backgroundTop();
/*     */     
/* 223 */     if (!clickableOnly) {
/* 224 */       collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.pageMsg);
/*     */     }
/*     */     
/* 227 */     Objects.requireNonNull(this.font); int shownLines = Math.min(128 / 9, this.cachedPageComponents.size());
/* 228 */     for (int i = 0; i < shownLines; i++) {
/* 229 */       FormattedCharSequence component = this.cachedPageComponents.get(i);
/* 230 */       Objects.requireNonNull(this.font); collector.accept(left + 36, top + 30 + i * 9, component);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 236 */     super.renderBackground(graphics, mouseX, mouseY, a);
/* 237 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, backgroundLeft(), backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
/*     */   }
/*     */   
/*     */   private int backgroundLeft() {
/* 241 */     return (this.width - 192) / 2;
/*     */   }
/*     */   
/*     */   private int backgroundTop() {
/* 245 */     return 2;
/*     */   }
/*     */   
/*     */   protected int menuControlsTop() {
/* 249 */     return backgroundTop() + 192 + 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 254 */     if (event.button() == 0) {
/* 255 */       ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(this.font, (int)event.x(), (int)event.y());
/* 256 */       visitText((ActiveTextCollector)finder, true);
/* 257 */       Style clickedStyle = finder.result();
/* 258 */       if (clickedStyle != null && handleClickEvent(clickedStyle.getClickEvent())) {
/* 259 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 263 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleClickEvent(ClickEvent event) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnonnull -> 6
/*     */     //   4: iconst_0
/*     */     //   5: ireturn
/*     */     //   6: aload_0
/*     */     //   7: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   10: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   13: ldc_w 'Player not available'
/*     */     //   16: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   19: checkcast net/minecraft/client/player/LocalPlayer
/*     */     //   22: astore_2
/*     */     //   23: aload_1
/*     */     //   24: dup
/*     */     //   25: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   28: pop
/*     */     //   29: astore_3
/*     */     //   30: iconst_0
/*     */     //   31: istore #4
/*     */     //   33: aload_3
/*     */     //   34: iload #4
/*     */     //   36: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   41: lookupswitch default -> 128, 0 -> 68, 1 -> 97
/*     */     //   68: aload_3
/*     */     //   69: checkcast net/minecraft/network/chat/ClickEvent$ChangePage
/*     */     //   72: astore #5
/*     */     //   74: aload #5
/*     */     //   76: invokevirtual page : ()I
/*     */     //   79: istore #7
/*     */     //   81: iload #7
/*     */     //   83: istore #6
/*     */     //   85: aload_0
/*     */     //   86: iload #6
/*     */     //   88: iconst_1
/*     */     //   89: isub
/*     */     //   90: invokevirtual forcePage : (I)Z
/*     */     //   93: pop
/*     */     //   94: goto -> 137
/*     */     //   97: aload_3
/*     */     //   98: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   101: astore #7
/*     */     //   103: aload #7
/*     */     //   105: invokevirtual command : ()Ljava/lang/String;
/*     */     //   108: astore #9
/*     */     //   110: aload #9
/*     */     //   112: astore #8
/*     */     //   114: aload_0
/*     */     //   115: invokevirtual closeContainerOnServer : ()V
/*     */     //   118: aload_2
/*     */     //   119: aload #8
/*     */     //   121: aconst_null
/*     */     //   122: invokestatic clickCommandAction : (Lnet/minecraft/client/player/LocalPlayer;Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   125: goto -> 137
/*     */     //   128: aload_1
/*     */     //   129: aload_0
/*     */     //   130: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   133: aload_0
/*     */     //   134: invokestatic defaultHandleGameClickEvent : (Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   137: iconst_1
/*     */     //   138: ireturn
/*     */     //   139: astore_3
/*     */     //   140: new java/lang/MatchException
/*     */     //   143: dup
/*     */     //   144: aload_3
/*     */     //   145: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   148: aload_3
/*     */     //   149: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   152: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #267	-> 0
/*     */     //   #268	-> 4
/*     */     //   #271	-> 6
/*     */     //   #273	-> 23
/*     */     //   #274	-> 68
/*     */     //   #275	-> 97
/*     */     //   #276	-> 114
/*     */     //   #279	-> 118
/*     */     //   #280	-> 125
/*     */     //   #281	-> 128
/*     */     //   #284	-> 137
/*     */     //   #275	-> 139
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   85	12	6	page	I
/*     */     //   114	14	8	command	Ljava/lang/String;
/*     */     //   23	116	2	player	Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   0	153	0	this	Lnet/minecraft/client/gui/screens/inventory/BookViewScreen;
/*     */     //   0	153	1	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   76	79	139	java/lang/Throwable
/*     */     //   105	108	139	java/lang/Throwable
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeContainerOnServer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/* 292 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/BookViewScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */