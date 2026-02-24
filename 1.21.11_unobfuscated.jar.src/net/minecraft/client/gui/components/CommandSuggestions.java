/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ParsedArgument;
/*     */ import com.mojang.brigadier.context.SuggestionContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestion;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ClientSuggestionProvider;
/*     */ import net.minecraft.client.renderer.Rect2i;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandSuggestions
/*     */ {
/*  51 */   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
/*     */   
/*  53 */   private static final Style UNPARSED_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);
/*  54 */   private static final Style LITERAL_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY);
/*  55 */   private static final List<Style> ARGUMENT_STYLES = (List<Style>)Stream.<ChatFormatting>of(new ChatFormatting[] { ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD }).map(Style.EMPTY::withColor).collect(ImmutableList.toImmutableList()); static { Objects.requireNonNull(Style.EMPTY); }
/*     */ 
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   private final Screen screen;
/*     */   private final EditBox input;
/*     */   private final Font font;
/*     */   private final boolean commandsOnly;
/*     */   private final boolean onlyShowIfCursorPastError;
/*     */   private final int lineStartOffset;
/*     */   private final int suggestionLineLimit;
/*     */   private final boolean anchorToBottom;
/*     */   private final int fillColor;
/*  68 */   private final List<FormattedCharSequence> commandUsage = Lists.newArrayList();
/*     */   private int commandUsagePosition;
/*     */   private int commandUsageWidth;
/*     */   private ParseResults<ClientSuggestionProvider> currentParse;
/*     */   private CompletableFuture<Suggestions> pendingSuggestions;
/*     */   private SuggestionsList suggestions;
/*     */   private boolean allowSuggestions;
/*     */   private boolean keepSuggestions;
/*     */   private boolean allowHiding = true;
/*     */   
/*     */   public CommandSuggestions(Minecraft minecraft, Screen screen, EditBox input, Font font, boolean commandsOnly, boolean onlyShowIfCursorPastError, int lineStartOffset, int suggestionLineLimit, boolean anchorToBottom, int fillColor) {
/*  79 */     this.minecraft = minecraft;
/*  80 */     this.screen = screen;
/*  81 */     this.input = input;
/*  82 */     this.font = font;
/*  83 */     this.commandsOnly = commandsOnly;
/*  84 */     this.onlyShowIfCursorPastError = onlyShowIfCursorPastError;
/*  85 */     this.lineStartOffset = lineStartOffset;
/*  86 */     this.suggestionLineLimit = suggestionLineLimit;
/*  87 */     this.anchorToBottom = anchorToBottom;
/*  88 */     this.fillColor = fillColor;
/*     */     
/*  90 */     input.addFormatter(this::formatChat);
/*     */   }
/*     */   
/*     */   public void setAllowSuggestions(boolean allowSuggestions) {
/*  94 */     this.allowSuggestions = allowSuggestions;
/*  95 */     if (!allowSuggestions) {
/*  96 */       this.suggestions = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAllowHiding(boolean allowHiding) {
/* 101 */     this.allowHiding = allowHiding;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 105 */     boolean isVisible = (this.suggestions != null);
/*     */     
/* 107 */     if (isVisible && this.suggestions.keyPressed(event))
/* 108 */       return true; 
/* 109 */     if (this.screen.getFocused() == this.input && event.isCycleFocus() && (!this.allowHiding || isVisible)) {
/* 110 */       showSuggestions(true);
/* 111 */       return true;
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double scroll) {
/* 117 */     return (this.suggestions != null && this.suggestions.mouseScrolled(Mth.clamp(scroll, -1.0D, 1.0D)));
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event) {
/* 121 */     return (this.suggestions != null && this.suggestions.mouseClicked((int)event.x(), (int)event.y()));
/*     */   }
/*     */   
/*     */   public void showSuggestions(boolean immediateNarration) {
/* 125 */     if (this.pendingSuggestions != null && this.pendingSuggestions.isDone()) {
/* 126 */       Suggestions suggestions = this.pendingSuggestions.join();
/* 127 */       if (!suggestions.isEmpty()) {
/* 128 */         int maxSuggestionWidth = 0;
/* 129 */         for (Suggestion suggestion : (Iterable<Suggestion>)suggestions.getList()) {
/* 130 */           maxSuggestionWidth = Math.max(maxSuggestionWidth, this.font.width(suggestion.getText()));
/*     */         }
/*     */         
/* 133 */         int x = Mth.clamp(this.input.getScreenX(suggestions.getRange().getStart()), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - maxSuggestionWidth);
/* 134 */         int y = this.anchorToBottom ? (this.screen.height - 12) : 72;
/* 135 */         this.suggestions = new SuggestionsList(x, y, maxSuggestionWidth, sortSuggestions(suggestions), immediateNarration);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 141 */     return (this.suggestions != null);
/*     */   }
/*     */   
/*     */   public Component getUsageNarration() {
/* 145 */     if (this.suggestions != null && this.suggestions.tabCycles) {
/* 146 */       if (this.allowHiding) {
/* 147 */         return (Component)Component.translatable("narration.suggestion.usage.cycle.hidable");
/*     */       }
/* 149 */       return (Component)Component.translatable("narration.suggestion.usage.cycle.fixed");
/*     */     } 
/*     */     
/* 152 */     if (this.allowHiding) {
/* 153 */       return (Component)Component.translatable("narration.suggestion.usage.fill.hidable");
/*     */     }
/* 155 */     return (Component)Component.translatable("narration.suggestion.usage.fill.fixed");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void hide() {
/* 161 */     this.suggestions = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Suggestion> sortSuggestions(Suggestions suggestions) {
/* 166 */     String partialCommand = this.input.getValue().substring(0, this.input.getCursorPosition());
/* 167 */     int lastWordIndex = getLastWordIndex(partialCommand);
/* 168 */     String lastWord = partialCommand.substring(lastWordIndex).toLowerCase(Locale.ROOT);
/*     */     
/* 170 */     List<Suggestion> suggestionList = Lists.newArrayList();
/* 171 */     List<Suggestion> partial = Lists.newArrayList();
/* 172 */     for (Suggestion suggestion : (Iterable<Suggestion>)suggestions.getList()) {
/* 173 */       if (suggestion.getText().startsWith(lastWord) || suggestion.getText().startsWith("minecraft:" + lastWord)) {
/* 174 */         suggestionList.add(suggestion); continue;
/*     */       } 
/* 176 */       partial.add(suggestion);
/*     */     } 
/*     */     
/* 179 */     suggestionList.addAll(partial);
/* 180 */     return suggestionList;
/*     */   }
/*     */   
/*     */   public void updateCommandInfo() {
/* 184 */     String command = this.input.getValue();
/*     */     
/* 186 */     if (this.currentParse != null && !this.currentParse.getReader().getString().equals(command)) {
/* 187 */       this.currentParse = null;
/*     */     }
/*     */     
/* 190 */     if (!this.keepSuggestions) {
/* 191 */       this.input.setSuggestion(null);
/* 192 */       this.suggestions = null;
/*     */     } 
/*     */     
/* 195 */     this.commandUsage.clear();
/* 196 */     StringReader reader = new StringReader(command);
/* 197 */     boolean startsWithSlash = (reader.canRead() && reader.peek() == '/');
/* 198 */     if (startsWithSlash) {
/* 199 */       reader.skip();
/*     */     }
/* 201 */     boolean isCommand = (this.commandsOnly || startsWithSlash);
/*     */     
/* 203 */     int cursorPosition = this.input.getCursorPosition();
/* 204 */     if (isCommand) {
/* 205 */       CommandDispatcher<ClientSuggestionProvider> commands = this.minecraft.player.connection.getCommands();
/*     */       
/* 207 */       if (this.currentParse == null) {
/* 208 */         this.currentParse = commands.parse(reader, this.minecraft.player.connection.getSuggestionsProvider());
/*     */       }
/*     */       
/* 211 */       int parseStart = this.onlyShowIfCursorPastError ? reader.getCursor() : 1;
/*     */       
/* 213 */       if (cursorPosition >= parseStart && (this.suggestions == null || !this.keepSuggestions)) {
/* 214 */         this.pendingSuggestions = commands.getCompletionSuggestions(this.currentParse, cursorPosition);
/* 215 */         this.pendingSuggestions.thenRun(() -> {
/*     */               if (!this.pendingSuggestions.isDone()) {
/*     */                 return;
/*     */               }
/*     */               updateUsageInfo();
/*     */             });
/*     */       } 
/*     */     } else {
/* 223 */       String partialCommand = command.substring(0, cursorPosition);
/* 224 */       int lastWord = getLastWordIndex(partialCommand);
/* 225 */       Collection<String> nonCommandSuggestions = this.minecraft.player.connection.getSuggestionsProvider().getCustomTabSugggestions();
/* 226 */       this.pendingSuggestions = SharedSuggestionProvider.suggest(nonCommandSuggestions, new SuggestionsBuilder(partialCommand, lastWord));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getLastWordIndex(String text) {
/* 231 */     if (Strings.isNullOrEmpty(text)) {
/* 232 */       return 0;
/*     */     }
/*     */     
/* 235 */     int result = 0;
/*     */     
/* 237 */     Matcher matcher = WHITESPACE_PATTERN.matcher(text);
/* 238 */     while (matcher.find()) {
/* 239 */       result = matcher.end();
/*     */     }
/*     */     
/* 242 */     return result;
/*     */   }
/*     */   
/*     */   private static FormattedCharSequence getExceptionMessage(CommandSyntaxException e) {
/* 246 */     Component message = ComponentUtils.fromMessage(e.getRawMessage());
/* 247 */     String context = e.getContext();
/* 248 */     if (context == null) {
/* 249 */       return message.getVisualOrderText();
/*     */     }
/* 251 */     return Component.translatable("command.context.parse_error", new Object[] { message, e.getCursor(), context }).getVisualOrderText();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateUsageInfo() {
/*     */     boolean trailingCharacters = false;
/* 257 */     if (this.input.getCursorPosition() == this.input.getValue().length()) {
/* 258 */       if (((Suggestions)this.pendingSuggestions.join()).isEmpty() && !this.currentParse.getExceptions().isEmpty()) {
/* 259 */         int literals = 0;
/* 260 */         for (Map.Entry<CommandNode<ClientSuggestionProvider>, CommandSyntaxException> entry : (Iterable<Map.Entry<CommandNode<ClientSuggestionProvider>, CommandSyntaxException>>)this.currentParse.getExceptions().entrySet()) {
/* 261 */           CommandSyntaxException exception = entry.getValue();
/* 262 */           if (exception.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
/* 263 */             literals++; continue;
/*     */           } 
/* 265 */           this.commandUsage.add(getExceptionMessage(exception));
/*     */         } 
/*     */         
/* 268 */         if (literals > 0) {
/* 269 */           this.commandUsage.add(getExceptionMessage(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(this.currentParse.getReader())));
/*     */         }
/* 271 */       } else if (this.currentParse.getReader().canRead()) {
/* 272 */         trailingCharacters = true;
/*     */       } 
/*     */     }
/*     */     
/* 276 */     this.commandUsagePosition = 0;
/* 277 */     this.commandUsageWidth = this.screen.width;
/*     */     
/* 279 */     if (this.commandUsage.isEmpty() && 
/* 280 */       !fillNodeUsage(ChatFormatting.GRAY))
/*     */     {
/* 282 */       if (trailingCharacters) {
/* 283 */         this.commandUsage.add(getExceptionMessage(Commands.getParseException(this.currentParse)));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 288 */     this.suggestions = null;
/* 289 */     if (this.allowSuggestions && (Boolean)this.minecraft.options.autoSuggestions().get()) {
/* 290 */       showSuggestions(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean fillNodeUsage(ChatFormatting color) {
/* 295 */     CommandContextBuilder<ClientSuggestionProvider> rootContext = this.currentParse.getContext();
/* 296 */     SuggestionContext<ClientSuggestionProvider> suggestionContext = rootContext.findSuggestionContext(this.input.getCursorPosition());
/* 297 */     Map<CommandNode<ClientSuggestionProvider>, String> usage = this.minecraft.player.connection.getCommands().getSmartUsage(suggestionContext.parent, this.minecraft.player.connection.getSuggestionsProvider());
/* 298 */     List<FormattedCharSequence> lines = Lists.newArrayList();
/* 299 */     int longest = 0;
/* 300 */     Style usageFormat = Style.EMPTY.withColor(color);
/*     */     
/* 302 */     for (Map.Entry<CommandNode<ClientSuggestionProvider>, String> entry : usage.entrySet()) {
/* 303 */       if (!(entry.getKey() instanceof com.mojang.brigadier.tree.LiteralCommandNode)) {
/* 304 */         lines.add(FormattedCharSequence.forward(entry.getValue(), usageFormat));
/* 305 */         longest = Math.max(longest, this.font.width(entry.getValue()));
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     if (!lines.isEmpty()) {
/* 310 */       this.commandUsage.addAll(lines);
/* 311 */       this.commandUsagePosition = Mth.clamp(this.input.getScreenX(suggestionContext.startPos), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - longest);
/* 312 */       this.commandUsageWidth = longest;
/* 313 */       return true;
/*     */     } 
/* 315 */     return false;
/*     */   }
/*     */   
/*     */   private FormattedCharSequence formatChat(String text, int offset) {
/* 319 */     if (this.currentParse != null) {
/* 320 */       return formatText(this.currentParse, text, offset);
/*     */     }
/* 322 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String calculateSuggestionSuffix(String contents, String suggestion) {
/* 327 */     if (suggestion.startsWith(contents)) {
/* 328 */       return suggestion.substring(contents.length());
/*     */     }
/*     */     
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static FormattedCharSequence formatText(ParseResults<ClientSuggestionProvider> currentParse, String text, int offset) {
/* 336 */     List<FormattedCharSequence> parts = Lists.newArrayList();
/* 337 */     int unformattedStart = 0;
/* 338 */     int nextColor = -1;
/*     */     
/* 340 */     CommandContextBuilder<ClientSuggestionProvider> context = currentParse.getContext().getLastChild();
/* 341 */     for (ParsedArgument<ClientSuggestionProvider, ?> argument : (Iterable<ParsedArgument<ClientSuggestionProvider, ?>>)context.getArguments().values()) {
/* 342 */       if (++nextColor >= ARGUMENT_STYLES.size()) {
/* 343 */         nextColor = 0;
/*     */       }
/*     */       
/* 346 */       int start = Math.max(argument.getRange().getStart() - offset, 0);
/* 347 */       if (start >= text.length()) {
/*     */         break;
/*     */       }
/* 350 */       int end = Math.min(argument.getRange().getEnd() - offset, text.length());
/* 351 */       if (end <= 0) {
/*     */         continue;
/*     */       }
/* 354 */       parts.add(FormattedCharSequence.forward(text.substring(unformattedStart, start), LITERAL_STYLE));
/* 355 */       parts.add(FormattedCharSequence.forward(text.substring(start, end), ARGUMENT_STYLES.get(nextColor)));
/* 356 */       unformattedStart = end;
/*     */     } 
/* 358 */     if (currentParse.getReader().canRead()) {
/* 359 */       int start = Math.max(currentParse.getReader().getCursor() - offset, 0);
/* 360 */       if (start < text.length()) {
/* 361 */         int end = Math.min(start + currentParse.getReader().getRemainingLength(), text.length());
/* 362 */         parts.add(FormattedCharSequence.forward(text.substring(unformattedStart, start), LITERAL_STYLE));
/* 363 */         parts.add(FormattedCharSequence.forward(text.substring(start, end), UNPARSED_STYLE));
/* 364 */         unformattedStart = end;
/*     */       } 
/*     */     } 
/* 367 */     parts.add(FormattedCharSequence.forward(text.substring(unformattedStart), LITERAL_STYLE));
/* 368 */     return FormattedCharSequence.composite(parts);
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY) {
/* 372 */     if (!renderSuggestions(graphics, mouseX, mouseY)) {
/* 373 */       renderUsage(graphics);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean renderSuggestions(GuiGraphics graphics, int mouseX, int mouseY) {
/* 378 */     if (this.suggestions != null) {
/* 379 */       this.suggestions.render(graphics, mouseX, mouseY);
/* 380 */       return true;
/*     */     } 
/* 382 */     return false;
/*     */   }
/*     */   
/*     */   public void renderUsage(GuiGraphics graphics) {
/* 386 */     int y = 0;
/* 387 */     for (FormattedCharSequence line : this.commandUsage) {
/* 388 */       int lineY = this.anchorToBottom ? (this.screen.height - 14 - 13 - 12 * y) : (72 + 12 * y);
/* 389 */       graphics.fill(this.commandUsagePosition - 1, lineY, this.commandUsagePosition + this.commandUsageWidth + 1, lineY + 12, this.fillColor);
/* 390 */       graphics.drawString(this.font, line, this.commandUsagePosition, lineY + 2, -1);
/* 391 */       y++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 396 */     if (this.suggestions != null) {
/* 397 */       return (Component)CommonComponents.NEW_LINE.copy().append(this.suggestions.getNarrationMessage());
/*     */     }
/* 399 */     return CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   public class SuggestionsList {
/*     */     private final Rect2i rect;
/*     */     private final String originalContents;
/*     */     private final List<Suggestion> suggestionList;
/*     */     private int offset;
/*     */     private int current;
/* 408 */     private Vec2 lastMouse = Vec2.ZERO;
/*     */     private boolean tabCycles;
/*     */     private int lastNarratedEntry;
/*     */     
/*     */     private SuggestionsList(int x, int y, int width, List<Suggestion> suggestionList, boolean immediateNarration) {
/* 413 */       int listX = x - (CommandSuggestions.this.input.isBordered() ? 0 : 1);
/* 414 */       int listY = CommandSuggestions.this.anchorToBottom ? (y - 3 - Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12) : (y - (CommandSuggestions.this.input.isBordered() ? 1 : 0));
/* 415 */       this.rect = new Rect2i(listX, listY, width + 1, Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12);
/* 416 */       this.originalContents = CommandSuggestions.this.input.getValue();
/* 417 */       this.lastNarratedEntry = immediateNarration ? -1 : 0;
/* 418 */       this.suggestionList = suggestionList;
/* 419 */       select(0);
/*     */     }
/*     */     
/*     */     public void render(GuiGraphics graphics, int mouseX, int mouseY) {
/* 423 */       int limit = Math.min(this.suggestionList.size(), CommandSuggestions.this.suggestionLineLimit);
/* 424 */       int unselectedColor = -5592406;
/* 425 */       boolean hasPrevious = (this.offset > 0);
/* 426 */       boolean hasNext = (this.suggestionList.size() > this.offset + limit);
/* 427 */       boolean limited = (hasPrevious || hasNext);
/* 428 */       boolean mouseMoved = (this.lastMouse.x != mouseX || this.lastMouse.y != mouseY);
/*     */       
/* 430 */       if (mouseMoved) {
/* 431 */         this.lastMouse = new Vec2(mouseX, mouseY);
/*     */       }
/*     */       
/* 434 */       if (limited) {
/* 435 */         graphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), CommandSuggestions.this.fillColor);
/* 436 */         graphics.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, CommandSuggestions.this.fillColor);
/* 437 */         if (hasPrevious) {
/* 438 */           for (int x = 0; x < this.rect.getWidth(); x++) {
/* 439 */             if (x % 2 == 0) {
/* 440 */               graphics.fill(this.rect.getX() + x, this.rect.getY() - 1, this.rect.getX() + x + 1, this.rect.getY(), -1);
/*     */             }
/*     */           } 
/*     */         }
/* 444 */         if (hasNext) {
/* 445 */           for (int x = 0; x < this.rect.getWidth(); x++) {
/* 446 */             if (x % 2 == 0) {
/* 447 */               graphics.fill(this.rect.getX() + x, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + x + 1, this.rect.getY() + this.rect.getHeight() + 1, -1);
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/*     */       boolean hovered = false;
/* 454 */       for (int i = 0; i < limit; i++) {
/* 455 */         Suggestion suggestion = this.suggestionList.get(i + this.offset);
/* 456 */         graphics.fill(this.rect.getX(), this.rect.getY() + 12 * i, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * i + 12, CommandSuggestions.this.fillColor);
/* 457 */         if (mouseX > this.rect.getX() && mouseX < this.rect.getX() + this.rect.getWidth() && mouseY > this.rect.getY() + 12 * i && mouseY < this.rect.getY() + 12 * i + 12) {
/* 458 */           if (mouseMoved) {
/* 459 */             select(i + this.offset);
/*     */           }
/* 461 */           hovered = true;
/*     */         } 
/* 463 */         graphics.drawString(CommandSuggestions.this.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * i, (i + this.offset == this.current) ? -256 : -5592406);
/*     */       } 
/*     */       
/* 466 */       if (hovered) {
/* 467 */         Message tooltip = ((Suggestion)this.suggestionList.get(this.current)).getTooltip();
/* 468 */         if (tooltip != null) {
/* 469 */           graphics.setTooltipForNextFrame(CommandSuggestions.this.font, ComponentUtils.fromMessage(tooltip), mouseX, mouseY);
/*     */         }
/*     */       } 
/*     */       
/* 473 */       if (this.rect.contains(mouseX, mouseY)) {
/* 474 */         graphics.requestCursor(CursorTypes.POINTING_HAND);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(int x, int y) {
/* 479 */       if (!this.rect.contains(x, y)) {
/* 480 */         return false;
/*     */       }
/*     */       
/* 483 */       int line = (y - this.rect.getY()) / 12 + this.offset;
/* 484 */       if (line >= 0 && line < this.suggestionList.size()) {
/* 485 */         select(line);
/* 486 */         useSuggestion();
/*     */       } 
/*     */       
/* 489 */       return true;
/*     */     }
/*     */     
/*     */     public boolean mouseScrolled(double scroll) {
/* 493 */       int mouseX = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledXPos(CommandSuggestions.this.minecraft.getWindow());
/* 494 */       int mouseY = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledYPos(CommandSuggestions.this.minecraft.getWindow());
/*     */       
/* 496 */       if (this.rect.contains(mouseX, mouseY)) {
/* 497 */         this.offset = Mth.clamp((int)(this.offset - scroll), 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 498 */         return true;
/*     */       } 
/*     */       
/* 501 */       return false;
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 505 */       if (event.isUp()) {
/* 506 */         cycle(-1);
/* 507 */         this.tabCycles = false;
/* 508 */         return true;
/* 509 */       }  if (event.isDown()) {
/* 510 */         cycle(1);
/* 511 */         this.tabCycles = false;
/* 512 */         return true;
/* 513 */       }  if (event.isCycleFocus()) {
/* 514 */         if (this.tabCycles) {
/* 515 */           cycle(event.hasShiftDown() ? -1 : 1);
/*     */         }
/* 517 */         useSuggestion();
/* 518 */         return true;
/* 519 */       }  if (event.isEscape()) {
/* 520 */         CommandSuggestions.this.hide();
/* 521 */         CommandSuggestions.this.input.setSuggestion(null);
/*     */         
/* 523 */         return true;
/*     */       } 
/*     */       
/* 526 */       return false;
/*     */     }
/*     */     
/*     */     public void cycle(int direction) {
/* 530 */       select(this.current + direction);
/* 531 */       int first = this.offset;
/* 532 */       int last = this.offset + CommandSuggestions.this.suggestionLineLimit - 1;
/* 533 */       if (this.current < first) {
/* 534 */         this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 535 */       } else if (this.current > last) {
/* 536 */         this.offset = Mth.clamp(this.current + CommandSuggestions.this.lineStartOffset - CommandSuggestions.this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void select(int index) {
/* 541 */       this.current = index;
/*     */       
/* 543 */       if (this.current < 0) {
/* 544 */         this.current += this.suggestionList.size();
/*     */       }
/* 546 */       if (this.current >= this.suggestionList.size()) {
/* 547 */         this.current -= this.suggestionList.size();
/*     */       }
/*     */       
/* 550 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 551 */       CommandSuggestions.this.input.setSuggestion(CommandSuggestions.calculateSuggestionSuffix(CommandSuggestions.this.input.getValue(), suggestion.apply(this.originalContents)));
/*     */       
/* 553 */       if (this.lastNarratedEntry != this.current) {
/* 554 */         CommandSuggestions.this.minecraft.getNarrator().saySystemNow(getNarrationMessage());
/*     */       }
/*     */     }
/*     */     
/*     */     public void useSuggestion() {
/* 559 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 560 */       CommandSuggestions.this.keepSuggestions = true;
/* 561 */       CommandSuggestions.this.input.setValue(suggestion.apply(this.originalContents));
/* 562 */       int end = suggestion.getRange().getStart() + suggestion.getText().length();
/* 563 */       CommandSuggestions.this.input.setCursorPosition(end);
/* 564 */       CommandSuggestions.this.input.setHighlightPos(end);
/* 565 */       select(this.current);
/* 566 */       CommandSuggestions.this.keepSuggestions = false;
/* 567 */       this.tabCycles = true;
/*     */     }
/*     */     
/*     */     private Component getNarrationMessage() {
/* 571 */       this.lastNarratedEntry = this.current;
/* 572 */       Suggestion suggestion = this.suggestionList.get(this.current);
/* 573 */       Message tooltip = suggestion.getTooltip();
/* 574 */       if (tooltip != null) {
/* 575 */         return (Component)Component.translatable("narration.suggestion.tooltip", new Object[] { this.current + 1, this.suggestionList.size(), suggestion.getText(), Component.translationArg(tooltip) });
/*     */       }
/* 577 */       return (Component)Component.translatable("narration.suggestion", new Object[] { this.current + 1, this.suggestionList.size(), suggestion.getText() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/CommandSuggestions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */