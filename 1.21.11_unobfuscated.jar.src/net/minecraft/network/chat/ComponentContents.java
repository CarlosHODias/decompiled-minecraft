/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public interface ComponentContents
/*    */ {
/*    */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 13 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 17 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 21 */     return MutableComponent.create(this);
/*    */   }
/*    */   
/*    */   MapCodec<? extends ComponentContents> codec();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/ComponentContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */