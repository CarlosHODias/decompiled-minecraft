/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class SelectorContents extends Record implements net.minecraft.network.chat.ComponentContents {
/*    */   private final SelectorPattern selector;
/*    */   private final Optional<Component> separator;
/*    */   public static final com.mojang.serialization.MapCodec<SelectorContents> MAP_CODEC;
/*    */   
/* 20 */   public SelectorContents(SelectorPattern selector, Optional<Component> separator) { this.selector = selector; this.separator = separator; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/SelectorContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/SelectorContents; } public SelectorPattern selector() { return this.selector; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/SelectorContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/SelectorContents;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Component> separator() { return this.separator; } static {
/* 21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SelectorPattern.CODEC.fieldOf("selector").forGetter(SelectorContents::selector), (App)ComponentSerialization.CODEC.optionalFieldOf("separator").forGetter(SelectorContents::separator)).apply((Applicative)i, SelectorContents::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SelectorContents> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
/* 33 */     if (source == null) {
/* 34 */       return Component.empty();
/*    */     }
/* 36 */     Optional<? extends Component> resolvedSeparator = ComponentUtils.updateForEntity(source, this.separator, entity, recursionDepth);
/* 37 */     return ComponentUtils.formatList(this.selector.resolved().findEntities(source), resolvedSeparator, Entity::getDisplayName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, net.minecraft.network.chat.Style currentStyle) {
/* 43 */     return output.accept(currentStyle, this.selector.pattern());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 48 */     return output.accept(this.selector.pattern());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return "pattern{" + String.valueOf(this.selector) + "}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/SelectorContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */