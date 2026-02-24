/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ 
/*    */ public class TemplateMirrorArgument extends StringRepresentableArgument<Mirror> {
/*    */   private TemplateMirrorArgument() {
/*  9 */     super(Mirror.CODEC, Mirror::values);
/*    */   }
/*    */   
/*    */   public static StringRepresentableArgument<Mirror> templateMirror() {
/* 13 */     return new TemplateMirrorArgument();
/*    */   }
/*    */   
/*    */   public static Mirror getMirror(CommandContext<CommandSourceStack> context, String name) {
/* 17 */     return (Mirror)context.getArgument(name, Mirror.class);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/TemplateMirrorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */