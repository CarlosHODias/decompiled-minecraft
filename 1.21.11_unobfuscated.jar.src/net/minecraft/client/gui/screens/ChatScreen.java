/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.ChatComponent;
/*     */ import net.minecraft.client.gui.components.CommandSuggestions;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.ChatListener;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class ChatScreen
/*     */   extends Screen
/*     */ {
/*     */   public static final double MOUSE_SCROLL_SPEED = 7.0D;
/*  29 */   private static final Component USAGE_TEXT = (Component)Component.translatable("chat_screen.usage");
/*     */   
/*  31 */   private String historyBuffer = "";
/*  32 */   private int historyPos = -1;
/*     */   
/*     */   protected EditBox input;
/*     */   
/*     */   protected String initial;
/*     */   protected boolean isDraft;
/*  38 */   protected ExitReason exitReason = ExitReason.INTERRUPTED;
/*     */   
/*     */   private CommandSuggestions commandSuggestions;
/*     */   
/*     */   public ChatScreen(String initial, boolean isDraft) {
/*  43 */     super((Component)Component.translatable("chat_screen.title"));
/*  44 */     this.initial = initial;
/*  45 */     this.isDraft = isDraft;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  50 */     this.historyPos = this.minecraft.gui.getChat().getRecentChat().size();
/*  51 */     this.input = new EditBox(this.minecraft.fontFilterFishy, 4, this.height - 12, this.width - 4, 12, (Component)Component.translatable("chat.editBox"))
/*     */       {
/*     */         protected MutableComponent createNarrationMessage() {
/*  54 */           return super.createNarrationMessage().append(ChatScreen.this.commandSuggestions.getNarrationMessage());
/*     */         }
/*     */       };
/*  57 */     this.input.setMaxLength(256);
/*  58 */     this.input.setBordered(false);
/*  59 */     this.input.setValue(this.initial);
/*  60 */     this.input.setResponder(this::onEdited);
/*  61 */     this.input.addFormatter(this::formatChat);
/*  62 */     this.input.setCanLoseFocus(false);
/*  63 */     addRenderableWidget(this.input);
/*     */     
/*  65 */     this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.input, this.font, false, false, 1, 10, true, -805306368);
/*  66 */     this.commandSuggestions.setAllowHiding(false);
/*  67 */     this.commandSuggestions.setAllowSuggestions(false);
/*  68 */     this.commandSuggestions.updateCommandInfo();
/*     */   } @FunctionalInterface
/*     */   public static interface ChatConstructor<T extends ChatScreen> {
/*     */     T create(String param1String, boolean param1Boolean); }
/*     */   protected void setInitialFocus() {
/*  73 */     setInitialFocus((GuiEventListener)this.input);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/*  78 */     this.initial = this.input.getValue();
/*  79 */     init(width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  84 */     this.exitReason = ExitReason.INTENTIONAL;
/*  85 */     super.onClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/*  90 */     this.minecraft.gui.getChat().resetChatScroll();
/*  91 */     this.initial = this.input.getValue();
/*     */     
/*  93 */     if (shouldDiscardDraft() || StringUtils.isBlank(this.initial)) {
/*  94 */       this.minecraft.gui.getChat().discardDraft();
/*  95 */     } else if (!this.isDraft) {
/*  96 */       this.minecraft.gui.getChat().saveAsDraft(this.initial);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean shouldDiscardDraft() {
/* 101 */     return (this.exitReason != ExitReason.INTERRUPTED && (this.exitReason != ExitReason.INTENTIONAL || !((Boolean)this.minecraft.options.saveChatDrafts().get())));
/*     */   }
/*     */   
/*     */   private void onEdited(String value) {
/* 105 */     this.commandSuggestions.setAllowSuggestions(true);
/* 106 */     this.commandSuggestions.updateCommandInfo();
/* 107 */     this.isDraft = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 113 */     if (this.commandSuggestions.keyPressed(event)) {
/* 114 */       return true;
/*     */     }
/*     */     
/* 117 */     if (this.isDraft && event.key() == 259) {
/* 118 */       this.input.setValue("");
/* 119 */       this.isDraft = false;
/* 120 */       return true;
/*     */     } 
/*     */     
/* 123 */     if (super.keyPressed(event)) {
/* 124 */       return true;
/*     */     }
/*     */     
/* 127 */     if (event.isConfirmation()) {
/* 128 */       handleChatInput(this.input.getValue(), true);
/* 129 */       this.exitReason = ExitReason.DONE;
/* 130 */       this.minecraft.setScreen(null);
/* 131 */       return true;
/*     */     } 
/*     */     
/* 134 */     switch (event.key()) { case 265:
/* 135 */         moveInHistory(-1); break;
/* 136 */       case 264: moveInHistory(1); break;
/*     */       case 266:
/* 138 */         this.minecraft.gui.getChat().scrollChat(this.minecraft.gui.getChat().getLinesPerPage() - 1); break;
/*     */       case 267:
/* 140 */         this.minecraft.gui.getChat().scrollChat(-this.minecraft.gui.getChat().getLinesPerPage() + 1); break;
/*     */       default:
/* 142 */         return false; }
/*     */ 
/*     */     
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 150 */     scrollY = Mth.clamp(scrollY, -1.0D, 1.0D);
/* 151 */     if (this.commandSuggestions.mouseScrolled(scrollY)) {
/* 152 */       return true;
/*     */     }
/* 154 */     if (!this.minecraft.hasShiftDown()) {
/* 155 */       scrollY *= 7.0D;
/*     */     }
/* 157 */     this.minecraft.gui.getChat().scrollChat((int)scrollY);
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 163 */     if (this.commandSuggestions.mouseClicked(event)) {
/* 164 */       return true;
/*     */     }
/*     */     
/* 167 */     if (event.button() == 0) {
/* 168 */       int screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
/* 169 */       ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(getFont(), (int)event.x(), (int)event.y())
/* 170 */         .includeInsertions(insertionClickMode());
/* 171 */       this.minecraft.gui.getChat().captureClickableText((ActiveTextCollector)finder, screenHeight, this.minecraft.gui.getGuiTicks(), true);
/* 172 */       Style clicked = finder.result();
/* 173 */       if (clicked != null && handleComponentClicked(clicked, insertionClickMode())) {
/* 174 */         this.initial = this.input.getValue();
/* 175 */         return true;
/*     */       } 
/*     */     } 
/* 178 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   private boolean insertionClickMode() {
/* 182 */     return this.minecraft.hasShiftDown();
/*     */   }
/*     */   
/*     */   private boolean handleComponentClicked(Style clicked, boolean allowInsertions) {
/* 186 */     ClickEvent event = clicked.getClickEvent();
/* 187 */     if (allowInsertions) {
/* 188 */       if (clicked.getInsertion() != null) {
/* 189 */         insertText(clicked.getInsertion(), false);
/*     */       }
/* 191 */     } else if (event != null) {
/* 192 */       if (event instanceof ClickEvent.Custom) { ClickEvent.Custom customEvent = (ClickEvent.Custom)event; if (customEvent.id().equals(ChatComponent.QUEUE_EXPAND_ID))
/* 193 */         { ChatListener chatListener = this.minecraft.getChatListener();
/* 194 */           if (chatListener.queueSize() != 0L) {
/* 195 */             chatListener.acceptNextDelayedMessage();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 200 */           return true; }  }  defaultHandleGameClickEvent(event, this.minecraft, this); return true;
/*     */     } 
/* 202 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertText(String text, boolean replace) {
/* 207 */     if (replace) {
/* 208 */       this.input.setValue(text);
/*     */     } else {
/* 210 */       this.input.insertText(text);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void moveInHistory(int dir) {
/* 215 */     int newPos = this.historyPos + dir;
/* 216 */     int max = this.minecraft.gui.getChat().getRecentChat().size();
/*     */     
/* 218 */     newPos = Mth.clamp(newPos, 0, max);
/* 219 */     if (newPos == this.historyPos) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     if (newPos == max) {
/* 224 */       this.historyPos = max;
/* 225 */       this.input.setValue(this.historyBuffer);
/*     */       
/*     */       return;
/*     */     } 
/* 229 */     if (this.historyPos == max) {
/* 230 */       this.historyBuffer = this.input.getValue();
/*     */     }
/*     */     
/* 233 */     this.input.setValue((String)this.minecraft.gui.getChat().getRecentChat().get(newPos));
/* 234 */     this.commandSuggestions.setAllowSuggestions(false);
/* 235 */     this.historyPos = newPos;
/*     */   }
/*     */   
/*     */   private FormattedCharSequence formatChat(String text, int offset) {
/* 239 */     if (this.isDraft) {
/* 240 */       return FormattedCharSequence.forward(text, Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));
/*     */     }
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 248 */     graphics.fill(2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.options.getBackgroundColor(Integer.MIN_VALUE));
/* 249 */     this.minecraft.gui.getChat().render(graphics, this.font, this.minecraft.gui.getGuiTicks(), mouseX, mouseY, true, insertionClickMode());
/*     */     
/* 251 */     super.render(graphics, mouseX, mouseY, a);
/* 252 */     this.commandSuggestions.render(graphics, mouseX, mouseY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateNarrationState(NarrationElementOutput output) {
/* 272 */     output.add(NarratedElementType.TITLE, getTitle());
/* 273 */     output.add(NarratedElementType.USAGE, USAGE_TEXT);
/* 274 */     String value = this.input.getValue();
/* 275 */     if (!value.isEmpty()) {
/* 276 */       output.nest().add(NarratedElementType.TITLE, (Component)Component.translatable("chat_screen.message", new Object[] { value }));
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleChatInput(String msg, boolean addToRecent) {
/* 281 */     msg = normalizeChatMessage(msg);
/* 282 */     if (msg.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 286 */     if (addToRecent) {
/* 287 */       this.minecraft.gui.getChat().addRecentChat(msg);
/*     */     }
/*     */     
/* 290 */     if (msg.startsWith("/")) {
/* 291 */       this.minecraft.player.connection.sendCommand(msg.substring(1));
/*     */     } else {
/* 293 */       this.minecraft.player.connection.sendChat(msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String normalizeChatMessage(String message) {
/* 298 */     return StringUtil.trimChatMessage(StringUtils.normalizeSpace(message.trim()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum ExitReason
/*     */   {
/* 307 */     INTENTIONAL,
/* 308 */     INTERRUPTED,
/* 309 */     DONE;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ChatScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */