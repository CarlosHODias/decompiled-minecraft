/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public class SignText {
/*     */   static {
/*  22 */     LINES_CODEC = net.minecraft.network.chat.ComponentSerialization.CODEC.listOf().comapFlatMap(input -> Util.fixedSize(input, 4).map(()), components -> List.of(components[0], components[1], components[2], components[3]));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  27 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)LINES_CODEC.fieldOf("messages").forGetter(()), (App)LINES_CODEC.lenientOptionalFieldOf("filtered_messages").forGetter(SignText::filteredMessages), (App)DyeColor.CODEC.fieldOf("color").orElse(DyeColor.BLACK).forGetter(()), (App)Codec.BOOL.fieldOf("has_glowing_text").orElse(false).forGetter(())).apply((Applicative)i, SignText::load));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Codec<Component[]> LINES_CODEC;
/*     */   
/*     */   public static final Codec<SignText> DIRECT_CODEC;
/*     */   public static final int LINES = 4;
/*     */   private final Component[] messages;
/*     */   private final Component[] filteredMessages;
/*     */   private final DyeColor color;
/*     */   private final boolean hasGlowingText;
/*     */   private FormattedCharSequence[] renderMessages;
/*     */   private boolean renderMessagedFiltered;
/*     */   
/*     */   public SignText() {
/*  43 */     this(emptyMessages(), emptyMessages(), DyeColor.BLACK, false);
/*     */   }
/*     */   
/*     */   public SignText(Component[] messages, Component[] filteredMessages, DyeColor color, boolean hasGlowingText) {
/*  47 */     this.messages = messages;
/*  48 */     this.filteredMessages = filteredMessages;
/*  49 */     this.color = color;
/*  50 */     this.hasGlowingText = hasGlowingText;
/*     */   }
/*     */   
/*     */   private static Component[] emptyMessages() {
/*  54 */     return new Component[] { CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY };
/*     */   }
/*     */   
/*     */   private static SignText load(Component[] messages, Optional<Component[]> filteredMessages, DyeColor color, boolean hasGlowingText) {
/*  58 */     return new SignText(messages, filteredMessages.orElse(Arrays.<Component>copyOf(messages, messages.length)), color, hasGlowingText);
/*     */   }
/*     */   
/*     */   public boolean hasGlowingText() {
/*  62 */     return this.hasGlowingText;
/*     */   }
/*     */   
/*     */   public SignText setHasGlowingText(boolean hasGlowingText) {
/*  66 */     if (hasGlowingText == this.hasGlowingText) {
/*  67 */       return this;
/*     */     }
/*  69 */     return new SignText(this.messages, this.filteredMessages, this.color, hasGlowingText);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/*  73 */     return this.color;
/*     */   }
/*     */   
/*     */   public SignText setColor(DyeColor color) {
/*  77 */     if (color == getColor()) {
/*  78 */       return this;
/*     */     }
/*  80 */     return new SignText(this.messages, this.filteredMessages, color, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public Component getMessage(int index, boolean shouldFilter) {
/*  84 */     return getMessages(shouldFilter)[index];
/*     */   }
/*     */   
/*     */   public SignText setMessage(int index, Component message) {
/*  88 */     return setMessage(index, message, message);
/*     */   }
/*     */   
/*     */   public SignText setMessage(int index, Component rawMessage, Component filteredMessage) {
/*  92 */     Component[] messages = Arrays.<Component>copyOf(this.messages, this.messages.length);
/*  93 */     Component[] filteredMessages = Arrays.<Component>copyOf(this.filteredMessages, this.filteredMessages.length);
/*  94 */     messages[index] = rawMessage;
/*  95 */     filteredMessages[index] = filteredMessage;
/*  96 */     return new SignText(messages, filteredMessages, this.color, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public boolean hasMessage(Player player) {
/* 100 */     return Arrays.<Component>stream(getMessages(player.isTextFilteringEnabled())).anyMatch(component -> !component.getString().isEmpty());
/*     */   }
/*     */   
/*     */   public Component[] getMessages(boolean shouldFilter) {
/* 104 */     return shouldFilter ? this.filteredMessages : this.messages;
/*     */   }
/*     */   
/*     */   public FormattedCharSequence[] getRenderMessages(boolean shouldFilter, Function<Component, FormattedCharSequence> prepare) {
/* 108 */     if (this.renderMessages == null || this.renderMessagedFiltered != shouldFilter) {
/* 109 */       this.renderMessagedFiltered = shouldFilter;
/* 110 */       this.renderMessages = new FormattedCharSequence[4];
/* 111 */       for (int i = 0; i < 4; i++) {
/* 112 */         this.renderMessages[i] = prepare.apply(getMessage(i, shouldFilter));
/*     */       }
/*     */     } 
/* 115 */     return this.renderMessages;
/*     */   }
/*     */   
/*     */   private Optional<Component[]> filteredMessages() {
/* 119 */     for (int i = 0; i < 4; i++) {
/* 120 */       if (!this.filteredMessages[i].equals(this.messages[i])) {
/* 121 */         return (Optional)Optional.of(this.filteredMessages);
/*     */       }
/*     */     } 
/* 124 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public boolean hasAnyClickCommands(Player player) {
/* 128 */     for (Component message : getMessages(player.isTextFilteringEnabled())) {
/* 129 */       Style style = message.getStyle();
/* 130 */       ClickEvent event = style.getClickEvent();
/* 131 */       if (event != null && event.action() == ClickEvent.Action.RUN_COMMAND) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/SignText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */