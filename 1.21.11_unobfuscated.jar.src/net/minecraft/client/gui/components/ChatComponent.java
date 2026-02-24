/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.GuiMessage;
/*     */ import net.minecraft.client.GuiMessageTag;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.screens.ChatScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ArrayListDeque;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.player.ChatVisiblity;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector2f;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChatComponent {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_CHAT_HISTORY = 100;
/*     */   
/*     */   private static final int MESSAGE_INDENT = 4;
/*     */   private static final int BOTTOM_MARGIN = 40;
/*     */   private static final int TOOLTIP_MAX_WIDTH = 210;
/*     */   private static final int TIME_BEFORE_MESSAGE_DELETION = 60;
/*  53 */   private static final Component DELETED_CHAT_MESSAGE = (Component)Component.translatable("chat.deleted_marker").withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int MESSAGE_BOTTOM_TO_MESSAGE_TOP = 8;
/*     */ 
/*     */   
/*  60 */   public static final Identifier QUEUE_EXPAND_ID = Identifier.withDefaultNamespace("internal/expand_chat_queue");
/*     */   
/*  62 */   private static final Style QUEUE_EXPAND_TEXT_STYLE = Style.EMPTY.withClickEvent((ClickEvent)new ClickEvent.Custom(QUEUE_EXPAND_ID, Optional.empty()))
/*  63 */     .withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.queue.tooltip")));
/*     */   
/*     */   private final Minecraft minecraft;
/*  66 */   private final ArrayListDeque<String> recentChat = new ArrayListDeque(100);
/*  67 */   private final List<GuiMessage> allMessages = Lists.newArrayList();
/*  68 */   private final List<GuiMessage.Line> trimmedMessages = Lists.newArrayList();
/*     */   
/*     */   private int chatScrollbarPos;
/*     */   
/*     */   private boolean newMessageSinceScroll;
/*     */   private Draft latestDraft;
/*     */   private ChatScreen preservedScreen;
/*  75 */   private final List<DelayedMessageDeletion> messageDeletionQueue = new ArrayList<>();
/*     */   
/*     */   public ChatComponent(Minecraft minecraft) {
/*  78 */     this.minecraft = minecraft;
/*  79 */     this.recentChat.addAll(minecraft.commandHistory().history());
/*     */   }
/*     */   
/*     */   public void tick() {
/*  83 */     if (!this.messageDeletionQueue.isEmpty()) {
/*  84 */       processMessageDeletionQueue();
/*     */     }
/*     */   }
/*     */   
/*     */   private int forEachLine(AlphaCalculator alphaCalculator, LineConsumer lineConsumer) {
/*  89 */     int perPage = getLinesPerPage();
/*     */     
/*  91 */     int count = 0;
/*  92 */     for (int i = Math.min(this.trimmedMessages.size() - this.chatScrollbarPos, perPage) - 1; i >= 0; i--) {
/*  93 */       int messageIndex = i + this.chatScrollbarPos;
/*  94 */       GuiMessage.Line message = this.trimmedMessages.get(messageIndex);
/*  95 */       float alpha = alphaCalculator.calculate(message);
/*  96 */       if (alpha > 1.0E-5F) {
/*  97 */         count++;
/*     */         
/*  99 */         lineConsumer.accept(message, i, alpha);
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return count;
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, Font font, int ticks, int mouseX, int mouseY, boolean isChatting, boolean changeCursorOnInsertions) {
/* 107 */     graphics.pose().pushMatrix();
/* 108 */     render(isChatting ? new DrawingFocusedGraphicsAccess(graphics, font, mouseX, mouseY, changeCursorOnInsertions) : new DrawingBackgroundGraphicsAccess(graphics), graphics.guiHeight(), ticks, isChatting);
/* 109 */     graphics.pose().popMatrix();
/*     */   }
/*     */   
/*     */   public void captureClickableText(ActiveTextCollector activeTextCollector, int screenHeight, int ticks, boolean isChatting) {
/* 113 */     render(new ClickableTextOnlyGraphicsAccess(activeTextCollector), screenHeight, ticks, isChatting);
/*     */   }
/*     */   
/*     */   private void render(final ChatGraphicsAccess graphics, int screenHeight, int ticks, boolean isChatting) {
/* 117 */     if (isChatHidden()) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     int total = this.trimmedMessages.size();
/*     */     
/* 123 */     if (total <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     ProfilerFiller profiler = Profiler.get();
/* 128 */     profiler.push("chat");
/*     */     
/* 130 */     float scale = (float)getScale();
/* 131 */     int maxWidth = Mth.ceil(getWidth() / scale);
/*     */     
/* 133 */     final int chatBottom = Mth.floor((screenHeight - 40) / scale);
/*     */     
/* 135 */     final float textOpacity = ((Double)this.minecraft.options.chatOpacity().get()).floatValue() * 0.9F + 0.1F;
/* 136 */     float backgroundOpacity = ((Double)this.minecraft.options.textBackgroundOpacity().get()).floatValue();
/*     */ 
/*     */ 
/*     */     
/* 140 */     Objects.requireNonNull(this.minecraft.font); final int messageHeight = 9;
/* 141 */     int messageBottomToMessageTop = 8;
/*     */ 
/*     */     
/* 144 */     double chatLineSpacing = (Double)this.minecraft.options.chatLineSpacing().get();
/*     */     
/* 146 */     final int entryHeight = (int)(messageHeight * (chatLineSpacing + 1.0D));
/*     */     
/* 148 */     final int entryBottomToMessageY = (int)Math.round(8.0D * (chatLineSpacing + 1.0D) - 4.0D * chatLineSpacing);
/*     */     
/* 150 */     long queueSize = this.minecraft.getChatListener().queueSize();
/*     */     
/* 152 */     AlphaCalculator alphaCalculator = isChatting ? AlphaCalculator.FULLY_VISIBLE : AlphaCalculator.timeBased(ticks);
/*     */     
/* 154 */     graphics.updatePose(pose -> {
/*     */           pose.scale(scale, scale);
/*     */ 
/*     */           
/*     */           pose.translate(4.0F, 0.0F);
/*     */         });
/*     */ 
/*     */     
/* 162 */     forEachLine(alphaCalculator, (line, lineIndex, alpha) -> {
/*     */           int entryBottom = chatBottom - lineIndex * entryHeight, entryTop = entryBottom - entryHeight;
/*     */ 
/*     */           
/*     */           graphics.fill(-4, entryTop, maxWidth + 4 + 4, entryBottom, ARGB.black(alpha * backgroundOpacity));
/*     */         });
/*     */     
/* 169 */     if (queueSize > 0L) {
/* 170 */       graphics.fill(-2, chatBottom, maxWidth + 4, chatBottom + messageHeight, ARGB.black(backgroundOpacity));
/*     */     }
/*     */ 
/*     */     
/* 174 */     int count = forEachLine(alphaCalculator, new LineConsumer() {
/*     */           boolean hoveredOverCurrentMessage;
/*     */           
/*     */           public void accept(GuiMessage.Line line, int lineIndex, float alpha) {
/*     */             boolean forceIconRendering;
/* 179 */             int entryBottom = chatBottom - lineIndex * entryHeight;
/* 180 */             int entryTop = entryBottom - entryHeight;
/*     */             
/* 182 */             int textTop = entryBottom - entryBottomToMessageY;
/* 183 */             boolean hoveredOverCurrentLine = graphics.handleMessage(textTop, alpha * textOpacity, line.content());
/* 184 */             this.hoveredOverCurrentMessage |= hoveredOverCurrentLine;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 189 */             if (line.endOfEntry()) {
/* 190 */               forceIconRendering = this.hoveredOverCurrentMessage;
/* 191 */               this.hoveredOverCurrentMessage = false;
/*     */             } else {
/* 193 */               forceIconRendering = false;
/*     */             } 
/*     */             
/* 196 */             GuiMessageTag tag = line.tag();
/* 197 */             if (tag != null) {
/*     */               
/* 199 */               graphics.handleTag(-4, entryTop, -2, entryBottom, alpha * textOpacity, tag);
/* 200 */               if (tag.icon() != null) {
/* 201 */                 int iconLeft = line.getTagIconLeft(ChatComponent.this.minecraft.font);
/* 202 */                 int textBottom = textTop + messageHeight;
/*     */                 
/* 204 */                 graphics.handleTagIcon(iconLeft, textBottom, forceIconRendering, tag, tag.icon());
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 210 */     if (queueSize > 0L) {
/* 211 */       int queueLineBottom = chatBottom + messageHeight;
/* 212 */       MutableComponent mutableComponent = Component.translatable("chat.queue", new Object[] { queueSize }).setStyle(QUEUE_EXPAND_TEXT_STYLE);
/* 213 */       graphics.handleMessage(queueLineBottom - 8, 0.5F * textOpacity, mutableComponent.getVisualOrderText());
/*     */     } 
/*     */ 
/*     */     
/* 217 */     if (isChatting) {
/* 218 */       int virtualHeight = total * entryHeight;
/* 219 */       int chatHeight = count * entryHeight;
/* 220 */       int y = this.chatScrollbarPos * chatHeight / total - chatBottom;
/* 221 */       int height = chatHeight * chatHeight / virtualHeight;
/*     */       
/* 223 */       if (virtualHeight != chatHeight) {
/* 224 */         int alpha = (y > 0) ? 170 : 96;
/* 225 */         int color = this.newMessageSinceScroll ? 13382451 : 3355562;
/* 226 */         int scrollBarStartX = maxWidth + 4;
/* 227 */         graphics.fill(scrollBarStartX, -y, scrollBarStartX + 2, -y - height, ARGB.color(alpha, color));
/* 228 */         graphics.fill(scrollBarStartX + 2, -y, scrollBarStartX + 1, -y - height, ARGB.color(alpha, 13421772));
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private boolean isChatHidden() {
/* 236 */     return (this.minecraft.options.chatVisibility().get() == ChatVisiblity.HIDDEN);
/*     */   }
/*     */   
/*     */   public void clearMessages(boolean history) {
/* 240 */     this.minecraft.getChatListener().flushQueue();
/* 241 */     this.messageDeletionQueue.clear();
/* 242 */     this.trimmedMessages.clear();
/* 243 */     this.allMessages.clear();
/* 244 */     if (history) {
/* 245 */       this.recentChat.clear();
/* 246 */       this.recentChat.addAll(this.minecraft.commandHistory().history());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addMessage(Component message) {
/* 251 */     addMessage(message, null, this.minecraft.isSingleplayer() ? GuiMessageTag.systemSinglePlayer() : GuiMessageTag.system());
/*     */   }
/*     */   
/*     */   public void addMessage(Component contents, MessageSignature signature, GuiMessageTag tag) {
/* 255 */     GuiMessage message = new GuiMessage(this.minecraft.gui.getGuiTicks(), contents, signature, tag);
/* 256 */     logChatMessage(message);
/* 257 */     addMessageToDisplayQueue(message);
/* 258 */     addMessageToQueue(message);
/*     */   }
/*     */   
/*     */   private void logChatMessage(GuiMessage message) {
/* 262 */     String messageString = message.content().getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n");
/* 263 */     String logTag = (String)Optionull.map(message.tag(), GuiMessageTag::logTag);
/* 264 */     if (logTag != null) {
/* 265 */       LOGGER.info("[{}] [CHAT] {}", logTag, messageString);
/*     */     } else {
/* 267 */       LOGGER.info("[CHAT] {}", messageString);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addMessageToDisplayQueue(GuiMessage message) {
/* 272 */     int maxWidth = Mth.floor(getWidth() / getScale());
/*     */     
/* 274 */     List<FormattedCharSequence> lines = message.splitLines(this.minecraft.font, maxWidth);
/*     */     
/* 276 */     boolean chatting = isChatFocused();
/* 277 */     for (int i = 0; i < lines.size(); i++) {
/* 278 */       FormattedCharSequence line = lines.get(i);
/* 279 */       if (chatting && this.chatScrollbarPos > 0) {
/* 280 */         this.newMessageSinceScroll = true;
/* 281 */         scrollChat(1);
/*     */       } 
/*     */       
/* 284 */       boolean endOfEntry = (i == lines.size() - 1);
/* 285 */       this.trimmedMessages.addFirst(new GuiMessage.Line(message.addedTime(), line, message.tag(), endOfEntry));
/*     */     } 
/*     */     
/* 288 */     while (this.trimmedMessages.size() > 100) {
/* 289 */       this.trimmedMessages.removeLast();
/*     */     }
/*     */   }
/*     */   
/*     */   private void addMessageToQueue(GuiMessage message) {
/* 294 */     this.allMessages.addFirst(message);
/*     */     
/* 296 */     while (this.allMessages.size() > 100) {
/* 297 */       this.allMessages.removeLast();
/*     */     }
/*     */   }
/*     */   
/*     */   private void processMessageDeletionQueue() {
/* 302 */     int time = this.minecraft.gui.getGuiTicks();
/* 303 */     this.messageDeletionQueue.removeIf(entry -> (time >= time.deletableAfter()) ? ((deleteMessageOrDelay(time.signature()) == null)) : false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteMessage(MessageSignature signature) {
/* 312 */     DelayedMessageDeletion delayedMessage = deleteMessageOrDelay(signature);
/* 313 */     if (delayedMessage != null) {
/* 314 */       this.messageDeletionQueue.add(delayedMessage);
/*     */     }
/*     */   }
/*     */   
/*     */   private DelayedMessageDeletion deleteMessageOrDelay(MessageSignature signature) {
/* 319 */     int time = this.minecraft.gui.getGuiTicks();
/*     */     
/* 321 */     for (ListIterator<GuiMessage> iterator = this.allMessages.listIterator(); iterator.hasNext(); ) {
/* 322 */       GuiMessage message = iterator.next();
/* 323 */       if (signature.equals(message.signature())) {
/* 324 */         int deletableAfter = message.addedTime() + 60;
/* 325 */         if (time >= deletableAfter) {
/* 326 */           iterator.set(createDeletedMarker(message));
/* 327 */           refreshTrimmedMessages();
/* 328 */           return null;
/*     */         } 
/* 330 */         return new DelayedMessageDeletion(signature, deletableAfter);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 335 */     return null;
/*     */   }
/*     */   
/*     */   private GuiMessage createDeletedMarker(GuiMessage message) {
/* 339 */     return new GuiMessage(message.addedTime(), DELETED_CHAT_MESSAGE, null, GuiMessageTag.system());
/*     */   }
/*     */   
/*     */   public void rescaleChat() {
/* 343 */     resetChatScroll();
/* 344 */     refreshTrimmedMessages();
/*     */   }
/*     */   
/*     */   private void refreshTrimmedMessages() {
/* 348 */     this.trimmedMessages.clear();
/* 349 */     for (GuiMessage message : (Iterable<GuiMessage>)Lists.reverse(this.allMessages)) {
/* 350 */       addMessageToDisplayQueue(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayListDeque<String> getRecentChat() {
/* 355 */     return this.recentChat;
/*     */   }
/*     */   
/*     */   public void addRecentChat(String message) {
/* 359 */     if (!message.equals(this.recentChat.peekLast())) {
/* 360 */       if (this.recentChat.size() >= 100) {
/* 361 */         this.recentChat.removeFirst();
/*     */       }
/* 363 */       this.recentChat.addLast(message);
/*     */     } 
/* 365 */     if (message.startsWith("/")) {
/* 366 */       this.minecraft.commandHistory().addCommand(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetChatScroll() {
/* 371 */     this.chatScrollbarPos = 0;
/* 372 */     this.newMessageSinceScroll = false;
/*     */   }
/*     */   
/*     */   public void scrollChat(int dir) {
/* 376 */     this.chatScrollbarPos += dir;
/* 377 */     int max = this.trimmedMessages.size();
/*     */     
/* 379 */     if (this.chatScrollbarPos > max - getLinesPerPage()) {
/* 380 */       this.chatScrollbarPos = max - getLinesPerPage();
/*     */     }
/*     */     
/* 383 */     if (this.chatScrollbarPos <= 0) {
/* 384 */       this.chatScrollbarPos = 0;
/* 385 */       this.newMessageSinceScroll = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isChatFocused() {
/* 390 */     return this.minecraft.screen instanceof ChatScreen;
/*     */   }
/*     */   
/*     */   private int getWidth() {
/* 394 */     return getWidth((Double)this.minecraft.options.chatWidth().get());
/*     */   }
/*     */   
/*     */   private int getHeight() {
/* 398 */     return getHeight(isChatFocused() ? (Double)this.minecraft.options.chatHeightFocused().get() : (Double)this.minecraft.options.chatHeightUnfocused().get());
/*     */   }
/*     */   
/*     */   private double getScale() {
/* 402 */     return (Double)this.minecraft.options.chatScale().get();
/*     */   }
/*     */   
/*     */   public static int getWidth(double pct) {
/* 406 */     int max = 320;
/* 407 */     int min = 40;
/* 408 */     return Mth.floor(pct * 280.0D + 40.0D);
/*     */   }
/*     */   
/*     */   public static int getHeight(double pct) {
/* 412 */     int max = 180;
/* 413 */     int min = 20;
/* 414 */     return Mth.floor(pct * 160.0D + 20.0D);
/*     */   }
/*     */   
/*     */   public static double defaultUnfocusedPct() {
/* 418 */     int max = 180;
/* 419 */     int min = 20;
/* 420 */     return 70.0D / (getHeight(1.0D) - 20);
/*     */   }
/*     */   
/*     */   public int getLinesPerPage() {
/* 424 */     return getHeight() / getLineHeight();
/*     */   }
/*     */   
/*     */   private int getLineHeight() {
/* 428 */     Objects.requireNonNull(this.minecraft.font); return (int)(9.0D * ((Double)this.minecraft.options.chatLineSpacing().get() + 1.0D));
/*     */   }
/*     */   
/*     */   public void saveAsDraft(String text) {
/* 432 */     boolean isCommand = text.startsWith("/");
/* 433 */     this.latestDraft = new Draft(text, isCommand ? ChatMethod.COMMAND : ChatMethod.MESSAGE);
/*     */   }
/*     */   
/*     */   public void discardDraft() {
/* 437 */     this.latestDraft = null;
/*     */   }
/*     */   
/*     */   public <T extends ChatScreen> T createScreen(ChatMethod chatMethod, ChatScreen.ChatConstructor<T> chat) {
/* 441 */     if (this.latestDraft != null && chatMethod.isDraftRestorable(this.latestDraft)) {
/* 442 */       return (T)chat.create(this.latestDraft.text(), true);
/*     */     }
/* 444 */     return (T)chat.create(chatMethod.prefix(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void openScreen(ChatMethod chatMethod, ChatScreen.ChatConstructor<?> chat) {
/* 449 */     this.minecraft.setScreen((Screen)createScreen(chatMethod, chat));
/*     */   }
/*     */   
/*     */   public void preserveCurrentChatScreen() {
/* 453 */     Screen screen = this.minecraft.screen; if (screen instanceof ChatScreen) { ChatScreen chatScreen = (ChatScreen)screen;
/* 454 */       this.preservedScreen = chatScreen; }
/*     */   
/*     */   }
/*     */   
/*     */   public ChatScreen restoreChatScreen() {
/* 459 */     ChatScreen restoredScreen = this.preservedScreen;
/* 460 */     this.preservedScreen = null;
/* 461 */     return restoredScreen;
/*     */   }
/*     */   private static final class DelayedMessageDeletion extends Record { private final MessageSignature signature; private final int deletableAfter;
/* 464 */     private DelayedMessageDeletion(MessageSignature signature, int deletableAfter) { this.signature = signature; this.deletableAfter = deletableAfter; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 464 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion; } public MessageSignature signature() { return this.signature; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #464	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/* 464 */       //   0	8	1	o	Ljava/lang/Object; } public int deletableAfter() { return this.deletableAfter; }
/*     */      }
/*     */   
/*     */   public State storeState() {
/* 468 */     return new State(
/* 469 */         List.copyOf(this.allMessages), 
/* 470 */         List.copyOf((Collection<? extends String>)this.recentChat), 
/* 471 */         List.copyOf(this.messageDeletionQueue));
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreState(State state) {
/* 476 */     this.recentChat.clear();
/* 477 */     this.recentChat.addAll(state.history);
/*     */     
/* 479 */     this.messageDeletionQueue.clear();
/* 480 */     this.messageDeletionQueue.addAll(state.delayedMessageDeletions);
/*     */     
/* 482 */     this.allMessages.clear();
/* 483 */     this.allMessages.addAll(state.messages);
/*     */     
/* 485 */     refreshTrimmedMessages();
/*     */   }
/*     */   
/*     */   public static class State {
/*     */     private final List<GuiMessage> messages;
/*     */     private final List<String> history;
/*     */     private final List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions;
/*     */     
/*     */     public State(List<GuiMessage> messages, List<String> history, List<ChatComponent.DelayedMessageDeletion> delayedMessageDeletions) {
/* 494 */       this.messages = messages;
/* 495 */       this.history = history;
/* 496 */       this.delayedMessageDeletions = delayedMessageDeletions;
/*     */     } }
/*     */   public static final class Draft extends Record { private final String text; private final ChatComponent.ChatMethod chatMethod;
/*     */     
/* 500 */     public Draft(String text, ChatComponent.ChatMethod chatMethod) { this.text = text; this.chatMethod = chatMethod; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/ChatComponent$Draft;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/ChatComponent$Draft;
/* 500 */       //   0	8	1	o	Ljava/lang/Object; } public String text() { return this.text; } public ChatComponent.ChatMethod chatMethod() { return this.chatMethod; }
/*     */      }
/*     */   
/*     */   public enum ChatMethod {
/* 504 */     MESSAGE("")
/*     */     {
/*     */       public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 507 */         return true;
/*     */       }
/*     */     },
/* 510 */     COMMAND("/")
/*     */     {
/*     */       public boolean isDraftRestorable(ChatComponent.Draft draft) {
/* 513 */         return (this == draft.chatMethod);
/*     */       }
/*     */     };
/*     */     
/*     */     private final String prefix;
/*     */     
/*     */     ChatMethod(String prefix) {
/* 520 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public String prefix() {
/* 524 */       return this.prefix;
/*     */     }
/*     */     public abstract boolean isDraftRestorable(ChatComponent.Draft param1Draft);
/*     */   }
/*     */   enum null { public boolean isDraftRestorable(ChatComponent.Draft draft) {
/*     */       return true;
/*     */     } }
/*     */   
/*     */   enum null { public boolean isDraftRestorable(ChatComponent.Draft draft) {
/*     */       return (this == draft.chatMethod);
/*     */     } }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface AlphaCalculator { public static final AlphaCalculator FULLY_VISIBLE = message -> 1.0F;
/*     */     
/*     */     static AlphaCalculator timeBased(int currentTickTime) {
/* 540 */       return message -> {
/*     */           int tickDelta = currentTickTime - message.addedTime();
/*     */           double t = tickDelta / 200.0D;
/*     */           t = 1.0D - t;
/*     */           t *= 10.0D;
/*     */           t = Mth.clamp(t, 0.0D, 1.0D);
/*     */           t *= t;
/*     */           return (float)t;
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     float calculate(GuiMessage.Line param1Line); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DrawingBackgroundGraphicsAccess
/*     */     implements ChatGraphicsAccess
/*     */   {
/*     */     private final GuiGraphics graphics;
/*     */ 
/*     */ 
/*     */     
/*     */     private final ActiveTextCollector textRenderer;
/*     */ 
/*     */     
/*     */     private ActiveTextCollector.Parameters parameters;
/*     */ 
/*     */ 
/*     */     
/*     */     public DrawingBackgroundGraphicsAccess(GuiGraphics graphics) {
/* 575 */       this.graphics = graphics;
/* 576 */       this.textRenderer = graphics.textRenderer(GuiGraphics.HoveredTextEffects.NONE, null);
/* 577 */       this.parameters = this.textRenderer.defaultParameters();
/*     */     }
/*     */ 
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 582 */       updater.accept(this.graphics.pose());
/* 583 */       this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {
/* 588 */       this.graphics.fill(x0, y0, x1, y1, color);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 593 */       this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 594 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 599 */       int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 600 */       this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */   }
/*     */   
/*     */   private static class DrawingFocusedGraphicsAccess
/*     */     implements ChatGraphicsAccess, Consumer<Style>
/*     */   {
/*     */     private final GuiGraphics graphics;
/*     */     private final Font font;
/*     */     private final ActiveTextCollector textRenderer;
/*     */     private ActiveTextCollector.Parameters parameters;
/*     */     private final int globalMouseX;
/*     */     private final int globalMouseY;
/* 616 */     private final Vector2f localMousePos = new Vector2f();
/*     */     
/*     */     private Style hoveredStyle;
/*     */     private final boolean changeCursorOnInsertions;
/*     */     
/*     */     public DrawingFocusedGraphicsAccess(GuiGraphics graphics, Font font, int mouseX, int mouseY, boolean changeCursorOnInsertions) {
/* 622 */       this.graphics = graphics;
/* 623 */       this.font = font;
/* 624 */       this.textRenderer = graphics.textRenderer(GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR, this);
/* 625 */       this.globalMouseX = mouseX;
/* 626 */       this.globalMouseY = mouseY;
/* 627 */       this.changeCursorOnInsertions = changeCursorOnInsertions;
/* 628 */       this.parameters = this.textRenderer.defaultParameters();
/* 629 */       updateLocalMousePos();
/*     */     }
/*     */     
/*     */     private void updateLocalMousePos() {
/* 633 */       this.graphics.pose().invert(new Matrix3x2f()).transformPosition(this.globalMouseX, this.globalMouseY, this.localMousePos);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 638 */       updater.accept(this.graphics.pose());
/* 639 */       this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/* 640 */       updateLocalMousePos();
/*     */     }
/*     */ 
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {
/* 645 */       this.graphics.fill(x0, y0, x1, y1, color);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Style style) {
/* 650 */       this.hoveredStyle = style;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 655 */       this.hoveredStyle = null;
/* 656 */       this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 657 */       if (this.changeCursorOnInsertions && this.hoveredStyle != null && this.hoveredStyle.getInsertion() != null) {
/* 658 */         this.graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */       }
/* 660 */       return (this.hoveredStyle != null);
/*     */     }
/*     */     
/*     */     private boolean isMouseOver(int left, int top, int right, int bottom) {
/* 664 */       return ActiveTextCollector.isPointInRectangle(this.localMousePos.x, this.localMousePos.y, left, top, right, bottom);
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 669 */       int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 670 */       this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/* 671 */       if (isMouseOver(x0, y0, x1, y1)) {
/* 672 */         showTooltip(tag);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {
/* 678 */       int top = bottom - icon.height - 1;
/* 679 */       int right = left + icon.width;
/* 680 */       boolean isMouseOver = isMouseOver(left, top, right, bottom);
/* 681 */       if (isMouseOver) {
/* 682 */         showTooltip(tag);
/*     */       }
/*     */       
/* 685 */       if (forceVisible || isMouseOver) {
/* 686 */         icon.draw(this.graphics, left, top);
/*     */       }
/*     */     }
/*     */     
/*     */     private void showTooltip(GuiMessageTag tag) {
/* 691 */       if (tag.text() != null)
/* 692 */         this.graphics.setTooltipForNextFrame(this.font, this.font.split((FormattedText)tag.text(), 210), this.globalMouseX, this.globalMouseY); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ClickableTextOnlyGraphicsAccess
/*     */     implements ChatGraphicsAccess {
/*     */     private final ActiveTextCollector output;
/*     */     
/*     */     public ClickableTextOnlyGraphicsAccess(ActiveTextCollector output) {
/* 701 */       this.output = output;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updatePose(Consumer<Matrix3x2f> updater) {
/* 706 */       ActiveTextCollector.Parameters defaultParameters = this.output.defaultParameters();
/* 707 */       Matrix3x2f newPose = new Matrix3x2f(defaultParameters.pose());
/* 708 */       updater.accept(newPose);
/* 709 */       this.output.defaultParameters(defaultParameters.withPose((Matrix3x2fc)newPose));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void fill(int x0, int y0, int x1, int y1, int color) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 719 */       this.output.accept(TextAlignment.LEFT, 0, textTop, message);
/* 720 */       return false;
/*     */     }
/*     */     
/*     */     public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {}
/*     */     
/*     */     public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface LineConsumer {
/*     */     void accept(GuiMessage.Line param1Line, int param1Int, float param1Float);
/*     */   }
/*     */   
/*     */   public static interface ChatGraphicsAccess {
/*     */     void updatePose(Consumer<Matrix3x2f> param1Consumer);
/*     */     
/*     */     void fill(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5);
/*     */     
/*     */     boolean handleMessage(int param1Int, float param1Float, FormattedCharSequence param1FormattedCharSequence);
/*     */     
/*     */     void handleTag(int param1Int1, int param1Int2, int param1Int3, int param1Int4, float param1Float, GuiMessageTag param1GuiMessageTag);
/*     */     
/*     */     void handleTagIcon(int param1Int1, int param1Int2, boolean param1Boolean, GuiMessageTag param1GuiMessageTag, GuiMessageTag.Icon param1Icon);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ChatComponent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */