/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.GuiMessageTag;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineLabel;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.PlayerFaceRenderer;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*     */ import net.minecraft.client.multiplayer.chat.report.ChatReport;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public class ChatSelectionScreen extends Screen {
/*  41 */   private static final Identifier CHECKMARK_SPRITE = Identifier.withDefaultNamespace("icon/checkmark");
/*  42 */   private static final Component TITLE = (Component)Component.translatable("gui.chatSelection.title");
/*  43 */   private static final Component CONTEXT_INFO = (Component)Component.translatable("gui.chatSelection.context");
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final ReportingContext reportingContext;
/*     */   
/*     */   private Button confirmSelectedButton;
/*     */   
/*     */   private MultiLineLabel contextInfoLabel;
/*     */   private ChatSelectionList chatSelectionList;
/*     */   private final ChatReport.Builder report;
/*     */   private final Consumer<ChatReport.Builder> onSelected;
/*     */   private ChatSelectionLogFiller chatLogFiller;
/*     */   
/*     */   public ChatSelectionScreen(Screen lastScreen, ReportingContext reportingContext, ChatReport.Builder report, Consumer<ChatReport.Builder> onSelected) {
/*  58 */     super(TITLE);
/*  59 */     this.lastScreen = lastScreen;
/*  60 */     this.reportingContext = reportingContext;
/*  61 */     this.report = report.copy();
/*  62 */     this.onSelected = onSelected;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  67 */     this.chatLogFiller = new ChatSelectionLogFiller(this.reportingContext, this::canReport);
/*     */     
/*  69 */     this.contextInfoLabel = MultiLineLabel.create(this.font, CONTEXT_INFO, this.width - 16);
/*  70 */     Objects.requireNonNull(this.font); this.chatSelectionList = (ChatSelectionList)addRenderableWidget((GuiEventListener)new ChatSelectionList(this.minecraft, (this.contextInfoLabel.getLineCount() + 1) * 9));
/*     */     
/*  72 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_BACK, b -> onClose())
/*     */         
/*  74 */         .bounds(this.width / 2 - 155, this.height - 32, 150, 20).build());
/*     */     
/*  76 */     this.confirmSelectedButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, b -> {
/*     */             this.onSelected.accept(this.report);
/*     */             onClose();
/*  79 */           }).bounds(this.width / 2 - 155 + 160, this.height - 32, 150, 20).build());
/*  80 */     updateConfirmSelectedButton();
/*     */     
/*  82 */     extendLog();
/*  83 */     this.chatSelectionList.setScrollAmount(this.chatSelectionList.maxScrollAmount());
/*     */   }
/*     */   
/*     */   private boolean canReport(LoggedChatMessage message) {
/*  87 */     return message.canReport(this.report.reportedProfileId());
/*     */   }
/*     */   
/*     */   private void extendLog() {
/*  91 */     int pageSize = this.chatSelectionList.getMaxVisibleEntries();
/*  92 */     this.chatLogFiller.fillNextPage(pageSize, this.chatSelectionList);
/*     */   }
/*     */   
/*     */   private void onReachedScrollTop() {
/*  96 */     extendLog();
/*     */   }
/*     */   
/*     */   private void updateConfirmSelectedButton() {
/* 100 */     this.confirmSelectedButton.active = !this.report.reportedMessages().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 105 */     super.render(graphics, mouseX, mouseY, a);
/* 106 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/*     */     
/* 108 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, -1);
/*     */     
/* 110 */     AbuseReportLimits reportLimits = this.reportingContext.sender().reportLimits();
/* 111 */     int messageCount = this.report.reportedMessages().size();
/* 112 */     int maxMessageCount = reportLimits.maxReportedMessageCount();
/* 113 */     MutableComponent mutableComponent = Component.translatable("gui.chatSelection.selected", new Object[] { messageCount, maxMessageCount });
/* 114 */     graphics.drawCenteredString(this.font, (Component)mutableComponent, this.width / 2, 26, -1);
/*     */     
/* 116 */     int topY = this.chatSelectionList.getFooterTop();
/* 117 */     Objects.requireNonNull(this.font); this.contextInfoLabel.visitLines(TextAlignment.CENTER, this.width / 2, topY, 9, textRenderer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 122 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 127 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), CONTEXT_INFO });
/*     */   }
/*     */   
/*     */   public class ChatSelectionList
/*     */     extends ObjectSelectionList<ChatSelectionList.Entry> implements ChatSelectionLogFiller.Output {
/*     */     public static final int ITEM_HEIGHT = 16;
/*     */     private Heading previousHeading;
/*     */     
/*     */     public ChatSelectionList(Minecraft minecraft, int upperMargin) {
/* 136 */       super(minecraft, ChatSelectionScreen.this.width, ChatSelectionScreen.this.height - upperMargin - 80, 40, 16);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setScrollAmount(double scrollAmount) {
/* 141 */       double prevScrollAmount = scrollAmount();
/* 142 */       super.setScrollAmount(scrollAmount);
/* 143 */       if (maxScrollAmount() > 1.0E-5F && scrollAmount <= 9.999999747378752E-6D && !Mth.equal(scrollAmount, prevScrollAmount)) {
/* 144 */         ChatSelectionScreen.this.onReachedScrollTop();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptMessage(int id, LoggedChatMessage.Player message) {
/* 150 */       boolean canReport = message.canReport(ChatSelectionScreen.this.report.reportedProfileId());
/* 151 */       ChatTrustLevel trustLevel = message.trustLevel();
/* 152 */       GuiMessageTag tag = trustLevel.createTag(message.message());
/* 153 */       Entry entry = new MessageEntry(id, message.toContentComponent(), message.toNarrationComponent(), tag, canReport, true);
/* 154 */       addEntryToTop((AbstractSelectionList.Entry)entry);
/* 155 */       updateHeading(message, canReport);
/*     */     }
/*     */     
/*     */     private void updateHeading(LoggedChatMessage.Player message, boolean canReport) {
/* 159 */       Entry entry = new MessageHeadingEntry(message.profile(), message.toHeadingComponent(), canReport);
/* 160 */       addEntryToTop((AbstractSelectionList.Entry)entry);
/*     */       
/* 162 */       Heading heading = new Heading(message.profileId(), entry);
/* 163 */       if (this.previousHeading != null && this.previousHeading.canCombine(heading)) {
/* 164 */         removeEntryFromTop((AbstractSelectionList.Entry)this.previousHeading.entry());
/*     */       }
/* 166 */       this.previousHeading = heading;
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptDivider(Component text) {
/* 171 */       addEntryToTop((AbstractSelectionList.Entry)new PaddingEntry());
/* 172 */       addEntryToTop((AbstractSelectionList.Entry)new DividerEntry(text));
/* 173 */       addEntryToTop((AbstractSelectionList.Entry)new PaddingEntry());
/* 174 */       this.previousHeading = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 179 */       return Math.min(350, this.width - 50);
/*     */     }
/*     */     
/*     */     public int getMaxVisibleEntries() {
/* 183 */       return Mth.positiveCeilDiv(this.height, 16);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float a, Entry entry) {
/* 188 */       if (shouldHighlightEntry(entry)) {
/* 189 */         boolean selected = (getSelected() == entry);
/* 190 */         int outlineColor = (isFocused() && selected) ? -1 : -8355712;
/* 191 */         renderSelection(graphics, (AbstractSelectionList.Entry)entry, outlineColor);
/*     */       } 
/*     */       
/* 194 */       entry.renderContent(graphics, mouseX, mouseY, (getHovered() == entry), a);
/*     */     }
/*     */     
/*     */     private boolean shouldHighlightEntry(Entry entry) {
/* 198 */       if (entry.canSelect()) {
/* 199 */         boolean entrySelected = (getSelected() == entry);
/* 200 */         boolean nothingSelected = (getSelected() == null);
/* 201 */         boolean entryHovered = (getHovered() == entry);
/* 202 */         return (entrySelected || (nothingSelected && entryHovered && entry.canReport()));
/*     */       } 
/* 204 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Entry nextEntry(ScreenDirection dir) {
/* 209 */       return (Entry)nextEntry(dir, Entry::canSelect);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 214 */       super.setSelected((AbstractSelectionList.Entry)selected);
/* 215 */       Entry entry = nextEntry(ScreenDirection.UP);
/* 216 */       if (entry == null) {
/* 217 */         ChatSelectionScreen.this.onReachedScrollTop();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 223 */       Entry selected = (Entry)getSelected();
/* 224 */       if (selected != null && selected.keyPressed(event)) {
/* 225 */         return true;
/*     */       }
/* 227 */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     public int getFooterTop() {
/* 231 */       Objects.requireNonNull(ChatSelectionScreen.this.font); return getBottom() + 9;
/*     */     }
/*     */     private static final class Heading extends Record { private final UUID sender; private final ChatSelectionScreen.ChatSelectionList.Entry entry;
/* 234 */       private Heading(UUID sender, ChatSelectionScreen.ChatSelectionList.Entry entry) { this.sender = sender; this.entry = entry; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #234	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #234	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #234	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/* 234 */         //   0	8	1	o	Ljava/lang/Object; } public UUID sender() { return this.sender; } public ChatSelectionScreen.ChatSelectionList.Entry entry() { return this.entry; }
/*     */        public boolean canCombine(Heading other) {
/* 236 */         return other.sender.equals(this.sender);
/*     */       } }
/*     */ 
/*     */     
/*     */     public static abstract class Entry
/*     */       extends ObjectSelectionList.Entry<Entry> {
/*     */       public Component getNarration() {
/* 243 */         return CommonComponents.EMPTY;
/*     */       }
/*     */       
/*     */       public boolean isSelected() {
/* 247 */         return false;
/*     */       }
/*     */       
/*     */       public boolean canSelect() {
/* 251 */         return false;
/*     */       }
/*     */       
/*     */       public boolean canReport() {
/* 255 */         return canSelect();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 260 */         return canSelect();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public class MessageEntry
/*     */       extends Entry
/*     */     {
/*     */       private static final int CHECKMARK_WIDTH = 9;
/*     */       
/*     */       private static final int CHECKMARK_HEIGHT = 8;
/*     */       private static final int INDENT_AMOUNT = 11;
/*     */       private static final int TAG_MARGIN_LEFT = 4;
/*     */       private final int chatId;
/*     */       private final FormattedText text;
/*     */       private final Component narration;
/*     */       private final List<FormattedCharSequence> hoverText;
/*     */       private final GuiMessageTag.Icon tagIcon;
/*     */       private final List<FormattedCharSequence> tagHoverText;
/*     */       private final boolean canReport;
/*     */       private final boolean playerMessage;
/*     */       
/*     */       public MessageEntry(int chatId, Component text, Component narration, GuiMessageTag tag, boolean canReport, boolean playerMessage) {
/* 283 */         this.chatId = chatId;
/* 284 */         this.tagIcon = (GuiMessageTag.Icon)Optionull.map(tag, GuiMessageTag::icon);
/* 285 */         this.tagHoverText = (tag != null && tag.text() != null) ? ChatSelectionScreen.this.font.split((FormattedText)tag.text(), ChatSelectionScreen.ChatSelectionList.this.getRowWidth()) : null;
/*     */         
/* 287 */         this.canReport = canReport;
/* 288 */         this.playerMessage = playerMessage;
/* 289 */         FormattedText shortText = ChatSelectionScreen.this.font.substrByWidth((FormattedText)text, getMaximumTextWidth() - ChatSelectionScreen.this.font.width((FormattedText)CommonComponents.ELLIPSIS));
/* 290 */         if (text != shortText) {
/* 291 */           this.text = FormattedText.composite(new FormattedText[] { shortText, (FormattedText)CommonComponents.ELLIPSIS });
/* 292 */           this.hoverText = ChatSelectionScreen.this.font.split((FormattedText)text, ChatSelectionScreen.ChatSelectionList.this.getRowWidth());
/*     */         } else {
/* 294 */           this.text = (FormattedText)text;
/* 295 */           this.hoverText = null;
/*     */         } 
/* 297 */         this.narration = narration;
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 302 */         if (isSelected() && this.canReport) {
/* 303 */           renderSelectedCheckmark(graphics, getContentY(), getContentX(), getContentHeight());
/*     */         }
/*     */         
/* 306 */         int textX = getContentX() + getTextIndent();
/* 307 */         Objects.requireNonNull(ChatSelectionScreen.this.font); int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 308 */         graphics.drawString(ChatSelectionScreen.this.font, net.minecraft.locale.Language.getInstance().getVisualOrder(this.text), textX, textY, this.canReport ? -1 : -1593835521);
/*     */         
/* 310 */         if (this.hoverText != null && hovered) {
/* 311 */           graphics.setTooltipForNextFrame(this.hoverText, mouseX, mouseY);
/*     */         }
/*     */         
/* 314 */         int textWidth = ChatSelectionScreen.this.font.width(this.text);
/* 315 */         renderTag(graphics, textX + textWidth + 4, getContentY(), getContentHeight(), mouseX, mouseY);
/*     */       }
/*     */       
/*     */       private void renderTag(GuiGraphics graphics, int iconLeft, int rowTop, int rowHeight, int mouseX, int mouseY) {
/* 319 */         if (this.tagIcon != null) {
/* 320 */           int iconTop = rowTop + (rowHeight - this.tagIcon.height) / 2;
/* 321 */           this.tagIcon.draw(graphics, iconLeft, iconTop);
/*     */           
/* 323 */           if (this.tagHoverText != null && mouseX >= iconLeft && mouseX <= iconLeft + this.tagIcon.width && mouseY >= iconTop && mouseY <= iconTop + this.tagIcon.height) {
/* 324 */             graphics.setTooltipForNextFrame(this.tagHoverText, mouseX, mouseY);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/*     */       private void renderSelectedCheckmark(GuiGraphics graphics, int rowTop, int rowLeft, int rowHeight) {
/* 330 */         int left = rowLeft;
/* 331 */         int top = rowTop + (rowHeight - 8) / 2;
/* 332 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ChatSelectionScreen.CHECKMARK_SPRITE, left, top, 9, 8);
/*     */       }
/*     */       
/*     */       private int getMaximumTextWidth() {
/* 336 */         int tagMargin = (this.tagIcon != null) ? (this.tagIcon.width + 4) : 0;
/* 337 */         return ChatSelectionScreen.ChatSelectionList.this.getRowWidth() - getTextIndent() - 4 - tagMargin;
/*     */       }
/*     */       
/*     */       private int getTextIndent() {
/* 341 */         return this.playerMessage ? 11 : 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public Component getNarration() {
/* 346 */         return isSelected() ? (Component)Component.translatable("narrator.select", new Object[] { this.narration }) : this.narration;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 351 */         ChatSelectionScreen.ChatSelectionList.this.setSelected((ChatSelectionScreen.ChatSelectionList.Entry)null);
/* 352 */         return toggleReport();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean keyPressed(KeyEvent event) {
/* 357 */         if (event.isSelection()) {
/* 358 */           return toggleReport();
/*     */         }
/* 360 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isSelected() {
/* 365 */         return ChatSelectionScreen.this.report.isReported(this.chatId);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean canSelect() {
/* 370 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean canReport() {
/* 375 */         return this.canReport;
/*     */       }
/*     */       
/*     */       private boolean toggleReport() {
/* 379 */         if (this.canReport) {
/* 380 */           ChatSelectionScreen.this.report.toggleReported(this.chatId);
/* 381 */           ChatSelectionScreen.this.updateConfirmSelectedButton();
/* 382 */           return true;
/*     */         } 
/* 384 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */     public class MessageHeadingEntry
/*     */       extends Entry {
/*     */       private static final int FACE_SIZE = 12;
/*     */       private static final int PADDING = 4;
/*     */       private final Component heading;
/*     */       private final Supplier<PlayerSkin> skin;
/*     */       private final boolean canReport;
/*     */       
/*     */       public MessageHeadingEntry(GameProfile profile, Component heading, boolean canReport) {
/* 397 */         this.heading = heading;
/* 398 */         this.canReport = canReport;
/* 399 */         this.skin = ChatSelectionScreen.ChatSelectionList.this.minecraft.getSkinManager().createLookup(profile, true);
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 404 */         int faceX = getContentX() - 12 + 4;
/* 405 */         int faceY = getContentY() + (getContentHeight() - 12) / 2;
/* 406 */         PlayerFaceRenderer.draw(graphics, this.skin.get(), faceX, faceY, 12);
/*     */         
/* 408 */         Objects.requireNonNull(ChatSelectionScreen.this.font); int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 409 */         graphics.drawString(ChatSelectionScreen.this.font, this.heading, faceX + 12 + 4, textY, this.canReport ? -1 : -1593835521);
/*     */       }
/*     */     }
/*     */     
/*     */     public class DividerEntry extends Entry {
/*     */       private final Component text;
/*     */       
/*     */       public DividerEntry(Component text) {
/* 417 */         this.text = text;
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 422 */         int centerY = getContentYMiddle();
/* 423 */         int rowRight = getContentRight() - 8;
/*     */         
/* 425 */         int textWidth = ChatSelectionScreen.this.font.width((FormattedText)this.text);
/* 426 */         int textLeft = (getContentX() + rowRight - textWidth) / 2;
/* 427 */         Objects.requireNonNull(ChatSelectionScreen.this.font); int textTop = centerY - 9 / 2;
/*     */         
/* 429 */         graphics.drawString(ChatSelectionScreen.this.font, this.text, textLeft, textTop, -6250336);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 434 */         return this.text; } } public static class PaddingEntry extends Entry { public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {} } } private static final class Heading extends Record { private final UUID sender; private final ChatSelectionScreen.ChatSelectionList.Entry entry; private Heading(UUID sender, ChatSelectionScreen.ChatSelectionList.Entry entry) { this.sender = sender; this.entry = entry; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/* 434 */       //   0	8	1	o	Ljava/lang/Object; } public UUID sender() { return this.sender; } public ChatSelectionScreen.ChatSelectionList.Entry entry() { return this.entry; } public boolean canCombine(Heading other) { return other.sender.equals(this.sender); } } public static abstract class Entry extends ObjectSelectionList.Entry<ChatSelectionList.Entry> { public Component getNarration() { return CommonComponents.EMPTY; } public boolean isSelected() { return false; } public boolean canSelect() { return false; } public boolean canReport() { return canSelect(); } public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) { return canSelect(); } } public class MessageEntry extends ChatSelectionList.Entry { private static final int CHECKMARK_WIDTH = 9; private static final int CHECKMARK_HEIGHT = 8; private static final int INDENT_AMOUNT = 11; private static final int TAG_MARGIN_LEFT = 4; private final int chatId; private final FormattedText text; private final Component narration; private final List<FormattedCharSequence> hoverText; private final GuiMessageTag.Icon tagIcon; private final List<FormattedCharSequence> tagHoverText; private final boolean canReport; private final boolean playerMessage; public MessageEntry(int chatId, Component text, Component narration, GuiMessageTag tag, boolean canReport, boolean playerMessage) { this.chatId = chatId; this.tagIcon = (GuiMessageTag.Icon)Optionull.map(tag, GuiMessageTag::icon); this.tagHoverText = (tag != null && tag.text() != null) ? ChatSelectionScreen.this.font.split((FormattedText)tag.text(), this$1.getRowWidth()) : null; this.canReport = canReport; this.playerMessage = playerMessage; FormattedText shortText = ChatSelectionScreen.this.font.substrByWidth((FormattedText)text, getMaximumTextWidth() - ChatSelectionScreen.this.font.width((FormattedText)CommonComponents.ELLIPSIS)); if (text != shortText) { this.text = FormattedText.composite(new FormattedText[] { shortText, (FormattedText)CommonComponents.ELLIPSIS }); this.hoverText = ChatSelectionScreen.this.font.split((FormattedText)text, this$1.getRowWidth()); } else { this.text = (FormattedText)text; this.hoverText = null; }  this.narration = narration; } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { if (isSelected() && this.canReport) renderSelectedCheckmark(graphics, getContentY(), getContentX(), getContentHeight());  int textX = getContentX() + getTextIndent(); Objects.requireNonNull(ChatSelectionScreen.this.font); int textY = getContentY() + 1 + (getContentHeight() - 9) / 2; graphics.drawString(ChatSelectionScreen.this.font, net.minecraft.locale.Language.getInstance().getVisualOrder(this.text), textX, textY, this.canReport ? -1 : -1593835521); if (this.hoverText != null && hovered) graphics.setTooltipForNextFrame(this.hoverText, mouseX, mouseY);  int textWidth = ChatSelectionScreen.this.font.width(this.text); renderTag(graphics, textX + textWidth + 4, getContentY(), getContentHeight(), mouseX, mouseY); } private void renderTag(GuiGraphics graphics, int iconLeft, int rowTop, int rowHeight, int mouseX, int mouseY) { if (this.tagIcon != null) { int iconTop = rowTop + (rowHeight - this.tagIcon.height) / 2; this.tagIcon.draw(graphics, iconLeft, iconTop); if (this.tagHoverText != null && mouseX >= iconLeft && mouseX <= iconLeft + this.tagIcon.width && mouseY >= iconTop && mouseY <= iconTop + this.tagIcon.height) graphics.setTooltipForNextFrame(this.tagHoverText, mouseX, mouseY);  }  } private void renderSelectedCheckmark(GuiGraphics graphics, int rowTop, int rowLeft, int rowHeight) { int left = rowLeft; int top = rowTop + (rowHeight - 8) / 2; graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ChatSelectionScreen.CHECKMARK_SPRITE, left, top, 9, 8); } private int getMaximumTextWidth() { int tagMargin = (this.tagIcon != null) ? (this.tagIcon.width + 4) : 0; return ChatSelectionScreen.ChatSelectionList.this.getRowWidth() - getTextIndent() - 4 - tagMargin; } private int getTextIndent() { return this.playerMessage ? 11 : 0; } public Component getNarration() { return isSelected() ? (Component)Component.translatable("narrator.select", new Object[] { this.narration }) : this.narration; } public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) { ChatSelectionScreen.ChatSelectionList.this.setSelected((ChatSelectionScreen.ChatSelectionList.Entry)null); return toggleReport(); } public boolean keyPressed(KeyEvent event) { if (event.isSelection()) return toggleReport();  return false; } public boolean isSelected() { return ChatSelectionScreen.this.report.isReported(this.chatId); } public boolean canSelect() { return true; } public boolean canReport() { return this.canReport; } private boolean toggleReport() { if (this.canReport) { ChatSelectionScreen.this.report.toggleReported(this.chatId); ChatSelectionScreen.this.updateConfirmSelectedButton(); return true; }  return false; } } public class MessageHeadingEntry extends ChatSelectionList.Entry { private static final int FACE_SIZE = 12; private static final int PADDING = 4; private final Component heading; private final Supplier<PlayerSkin> skin; private final boolean canReport; public MessageHeadingEntry(GameProfile profile, Component heading, boolean canReport) { this.heading = heading; this.canReport = canReport; this.skin = this$1.minecraft.getSkinManager().createLookup(profile, true); } public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) { int faceX = getContentX() - 12 + 4; int faceY = getContentY() + (getContentHeight() - 12) / 2; PlayerFaceRenderer.draw(graphics, this.skin.get(), faceX, faceY, 12); Objects.requireNonNull(ChatSelectionScreen.this.font); int textY = getContentY() + 1 + (getContentHeight() - 9) / 2; graphics.drawString(ChatSelectionScreen.this.font, this.heading, faceX + 12 + 4, textY, this.canReport ? -1 : -1593835521); } } public class DividerEntry extends ChatSelectionList.Entry { public Component getNarration() { return this.text; }
/*     */ 
/*     */     
/*     */     private final Component text;
/*     */     
/*     */     public DividerEntry(Component text) {
/*     */       this.text = text;
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       int centerY = getContentYMiddle();
/*     */       int rowRight = getContentRight() - 8;
/*     */       int textWidth = ChatSelectionScreen.this.font.width((FormattedText)this.text);
/*     */       int textLeft = (getContentX() + rowRight - textWidth) / 2;
/*     */       Objects.requireNonNull(ChatSelectionScreen.this.font);
/*     */       int textTop = centerY - 9 / 2;
/*     */       graphics.drawString(ChatSelectionScreen.this.font, this.text, textLeft, textTop, -6250336);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class PaddingEntry extends ChatSelectionList.Entry {
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {}
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/reporting/ChatSelectionScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */