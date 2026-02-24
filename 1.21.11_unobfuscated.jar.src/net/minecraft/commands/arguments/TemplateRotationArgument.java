/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public class TemplateRotationArgument extends StringRepresentableArgument<Rotation> {
/*    */   private TemplateRotationArgument() {
/*  9 */     super(Rotation.CODEC, Rotation::values);
/*    */   }
/*    */   
/*    */   public static TemplateRotationArgument templateRotation() {
/* 13 */     return new TemplateRotationArgument();
/*    */   }
/*    */   
/*    */   public static Rotation getRotation(CommandContext<CommandSourceStack> context, String name) {
/* 17 */     return (Rotation)context.getArgument(name, Rotation.class);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/TemplateRotationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */