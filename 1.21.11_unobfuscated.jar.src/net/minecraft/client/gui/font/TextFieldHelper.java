/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.StringSplitter;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextFieldHelper
/*     */ {
/*     */   private final Supplier<String> getMessageFn;
/*     */   private final Consumer<String> setMessageFn;
/*     */   private final Supplier<String> getClipboardFn;
/*     */   private final Consumer<String> setClipboardFn;
/*     */   private final Predicate<String> stringValidator;
/*     */   private int cursorPos;
/*     */   private int selectionPos;
/*     */   
/*     */   public TextFieldHelper(Supplier<String> getMessageFn, Consumer<String> setMessageFn, Supplier<String> getClipboardFn, Consumer<String> setClipboardFn, Predicate<String> stringValidator) {
/*  29 */     this.getMessageFn = getMessageFn;
/*  30 */     this.setMessageFn = setMessageFn;
/*  31 */     this.getClipboardFn = getClipboardFn;
/*  32 */     this.setClipboardFn = setClipboardFn;
/*  33 */     this.stringValidator = stringValidator;
/*     */     
/*  35 */     setCursorToEnd();
/*     */   }
/*     */   
/*     */   public static Supplier<String> createClipboardGetter(Minecraft minecraft) {
/*  39 */     return () -> getClipboardContents(minecraft);
/*     */   }
/*     */   
/*     */   public static String getClipboardContents(Minecraft minecraft) {
/*  43 */     return ChatFormatting.stripFormatting(minecraft.keyboardHandler.getClipboard().replaceAll("\\r", ""));
/*     */   }
/*     */   
/*     */   public static Consumer<String> createClipboardSetter(Minecraft minecraft) {
/*  47 */     return text -> setClipboardContents(minecraft, text);
/*     */   }
/*     */   
/*     */   public static void setClipboardContents(Minecraft minecraft, String text) {
/*  51 */     minecraft.keyboardHandler.setClipboard(text);
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/*  55 */     if (event.isAllowedChatCharacter()) {
/*  56 */       insertText(this.getMessageFn.get(), event.codepointAsString());
/*     */     }
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  62 */     if (event.isSelectAll()) {
/*  63 */       selectAll();
/*  64 */       return true;
/*  65 */     }  if (event.isCopy()) {
/*  66 */       copy();
/*  67 */       return true;
/*  68 */     }  if (event.isPaste()) {
/*  69 */       paste();
/*  70 */       return true;
/*  71 */     }  if (event.isCut()) {
/*  72 */       cut();
/*  73 */       return true;
/*     */     } 
/*     */     
/*  76 */     CursorStep cursorStep = event.hasControlDownWithQuirk() ? CursorStep.WORD : CursorStep.CHARACTER;
/*  77 */     if (event.key() == 259) {
/*  78 */       removeFromCursor(-1, cursorStep);
/*  79 */       return true;
/*  80 */     }  if (event.key() == 261)
/*  81 */     { removeFromCursor(1, cursorStep); }
/*  82 */     else { if (event.isLeft()) {
/*  83 */         moveBy(-1, event.hasShiftDown(), cursorStep);
/*  84 */         return true;
/*  85 */       }  if (event.isRight()) {
/*  86 */         moveBy(1, event.hasShiftDown(), cursorStep);
/*  87 */         return true;
/*  88 */       }  if (event.key() == 268) {
/*  89 */         setCursorToStart(event.hasShiftDown());
/*  90 */         return true;
/*  91 */       }  if (event.key() == 269) {
/*  92 */         setCursorToEnd(event.hasShiftDown());
/*  93 */         return true;
/*     */       }  }
/*  95 */      return false;
/*     */   }
/*     */   
/*     */   private int clampToMsgLength(int value) {
/*  99 */     return Mth.clamp(value, 0, ((String)this.getMessageFn.get()).length());
/*     */   }
/*     */   
/*     */   private void insertText(String message, String text) {
/* 103 */     if (this.selectionPos != this.cursorPos) {
/* 104 */       message = deleteSelection(message);
/*     */     }
/*     */     
/* 107 */     this.cursorPos = Mth.clamp(this.cursorPos, 0, message.length());
/* 108 */     String newPageText = new StringBuilder(message).insert(this.cursorPos, text).toString();
/* 109 */     if (this.stringValidator.test(newPageText)) {
/* 110 */       this.setMessageFn.accept(newPageText);
/* 111 */       this.selectionPos = this.cursorPos = Math.min(newPageText.length(), this.cursorPos + text.length());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void insertText(String text) {
/* 116 */     insertText(this.getMessageFn.get(), text);
/*     */   }
/*     */   
/*     */   private void resetSelectionIfNeeded(boolean selecting) {
/* 120 */     if (!selecting) {
/* 121 */       this.selectionPos = this.cursorPos;
/*     */     }
/*     */   }
/*     */   
/*     */   public void moveBy(int count, boolean selecting, CursorStep scope) {
/* 126 */     switch (scope.ordinal()) { case 0:
/* 127 */         moveByChars(count, selecting); break;
/* 128 */       case 1: moveByWords(count, selecting);
/*     */         break; }
/*     */   
/*     */   }
/*     */   public void moveByChars(int count) {
/* 133 */     moveByChars(count, false);
/*     */   }
/*     */   
/*     */   public void moveByChars(int count, boolean selecting) {
/* 137 */     this.cursorPos = Util.offsetByCodepoints(this.getMessageFn.get(), this.cursorPos, count);
/* 138 */     resetSelectionIfNeeded(selecting);
/*     */   }
/*     */   
/*     */   public void moveByWords(int count) {
/* 142 */     moveByWords(count, false);
/*     */   }
/*     */   
/*     */   public void moveByWords(int count, boolean selecting) {
/* 146 */     this.cursorPos = StringSplitter.getWordPosition(this.getMessageFn.get(), count, this.cursorPos, true);
/* 147 */     resetSelectionIfNeeded(selecting);
/*     */   }
/*     */   
/*     */   public void removeFromCursor(int count, CursorStep scope) {
/* 151 */     switch (scope.ordinal()) { case 0:
/* 152 */         removeCharsFromCursor(count); break;
/* 153 */       case 1: removeWordsFromCursor(count);
/*     */         break; }
/*     */   
/*     */   }
/*     */   public void removeWordsFromCursor(int count) {
/* 158 */     int wordPosition = StringSplitter.getWordPosition(this.getMessageFn.get(), count, this.cursorPos, true);
/* 159 */     removeCharsFromCursor(wordPosition - this.cursorPos);
/*     */   }
/*     */   
/*     */   public void removeCharsFromCursor(int count) {
/* 163 */     String message = this.getMessageFn.get();
/* 164 */     if (!message.isEmpty()) {
/*     */       String newMessage;
/* 166 */       if (this.selectionPos != this.cursorPos) {
/* 167 */         newMessage = deleteSelection(message);
/*     */       } else {
/* 169 */         int otherPos = Util.offsetByCodepoints(message, this.cursorPos, count);
/* 170 */         int start = Math.min(otherPos, this.cursorPos);
/* 171 */         int end = Math.max(otherPos, this.cursorPos);
/* 172 */         newMessage = new StringBuilder(message).delete(start, end).toString();
/* 173 */         if (count < 0) {
/* 174 */           this.selectionPos = this.cursorPos = start;
/*     */         }
/*     */       } 
/* 177 */       this.setMessageFn.accept(newMessage);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cut() {
/* 182 */     String message = this.getMessageFn.get();
/* 183 */     this.setClipboardFn.accept(getSelected(message));
/* 184 */     this.setMessageFn.accept(deleteSelection(message));
/*     */   }
/*     */   
/*     */   public void paste() {
/* 188 */     insertText(this.getMessageFn.get(), this.getClipboardFn.get());
/* 189 */     this.selectionPos = this.cursorPos;
/*     */   }
/*     */   
/*     */   public void copy() {
/* 193 */     this.setClipboardFn.accept(getSelected(this.getMessageFn.get()));
/*     */   }
/*     */   
/*     */   public void selectAll() {
/* 197 */     this.selectionPos = 0;
/* 198 */     this.cursorPos = ((String)this.getMessageFn.get()).length();
/*     */   }
/*     */   
/*     */   private String getSelected(String text) {
/* 202 */     int startIndex = Math.min(this.cursorPos, this.selectionPos);
/* 203 */     int endIndex = Math.max(this.cursorPos, this.selectionPos);
/* 204 */     return text.substring(startIndex, endIndex);
/*     */   }
/*     */   
/*     */   private String deleteSelection(String message) {
/* 208 */     if (this.selectionPos == this.cursorPos) {
/* 209 */       return message;
/*     */     }
/* 211 */     int startIndex = Math.min(this.cursorPos, this.selectionPos);
/* 212 */     int endIndex = Math.max(this.cursorPos, this.selectionPos);
/* 213 */     String updatedText = message.substring(0, startIndex) + message.substring(0, startIndex);
/* 214 */     this.selectionPos = this.cursorPos = startIndex;
/* 215 */     return updatedText;
/*     */   }
/*     */   
/*     */   public void setCursorToStart() {
/* 219 */     setCursorToStart(false);
/*     */   }
/*     */   
/*     */   public void setCursorToStart(boolean selecting) {
/* 223 */     this.cursorPos = 0;
/* 224 */     resetSelectionIfNeeded(selecting);
/*     */   }
/*     */   
/*     */   public void setCursorToEnd() {
/* 228 */     setCursorToEnd(false);
/*     */   }
/*     */   
/*     */   public void setCursorToEnd(boolean selecting) {
/* 232 */     this.cursorPos = ((String)this.getMessageFn.get()).length();
/* 233 */     resetSelectionIfNeeded(selecting);
/*     */   }
/*     */   
/*     */   public int getCursorPos() {
/* 237 */     return this.cursorPos;
/*     */   }
/*     */   
/*     */   public void setCursorPos(int value) {
/* 241 */     setCursorPos(value, true);
/*     */   }
/*     */   
/*     */   public void setCursorPos(int value, boolean selecting) {
/* 245 */     this.cursorPos = clampToMsgLength(value);
/* 246 */     resetSelectionIfNeeded(selecting);
/*     */   }
/*     */   
/*     */   public int getSelectionPos() {
/* 250 */     return this.selectionPos;
/*     */   }
/*     */   
/*     */   public void setSelectionPos(int value) {
/* 254 */     this.selectionPos = clampToMsgLength(value);
/*     */   }
/*     */   
/*     */   public void setSelectionRange(int start, int end) {
/* 258 */     int maxSize = ((String)this.getMessageFn.get()).length();
/* 259 */     this.cursorPos = Mth.clamp(start, 0, maxSize);
/* 260 */     this.selectionPos = Mth.clamp(end, 0, maxSize);
/*     */   }
/*     */   
/*     */   public boolean isSelecting() {
/* 264 */     return (this.cursorPos != this.selectionPos);
/*     */   }
/*     */   
/*     */   public enum CursorStep {
/* 268 */     CHARACTER,
/* 269 */     WORD;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/TextFieldHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */