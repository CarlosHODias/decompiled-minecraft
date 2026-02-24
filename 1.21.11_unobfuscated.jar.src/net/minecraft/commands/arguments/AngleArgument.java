/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AngleArgument implements ArgumentType<AngleArgument.SingleAngle> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "~", "~-5" });
/* 18 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)Component.translatable("argument.angle.incomplete"));
/* 19 */   public static final SimpleCommandExceptionType ERROR_INVALID_ANGLE = new SimpleCommandExceptionType((Message)Component.translatable("argument.angle.invalid"));
/*    */   
/*    */   public static AngleArgument angle() {
/* 22 */     return new AngleArgument();
/*    */   }
/*    */   
/*    */   public static float getAngle(CommandContext<CommandSourceStack> context, String name) {
/* 26 */     return ((SingleAngle)context.getArgument(name, SingleAngle.class)).getAngle((CommandSourceStack)context.getSource());
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleAngle parse(StringReader reader) throws CommandSyntaxException {
/* 31 */     if (!reader.canRead()) {
/* 32 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     }
/*    */     
/* 35 */     boolean isRelative = WorldCoordinate.isRelative(reader);
/* 36 */     float value = (reader.canRead() && reader.peek() != ' ') ? reader.readFloat() : 0.0F;
/* 37 */     if (Float.isNaN(value) || Float.isInfinite(value)) {
/* 38 */       throw ERROR_INVALID_ANGLE.createWithContext(reader);
/*    */     }
/* 40 */     return new SingleAngle(value, isRelative);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 45 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static final class SingleAngle {
/*    */     private final float angle;
/*    */     private final boolean isRelative;
/*    */     
/*    */     private SingleAngle(float angle, boolean isRelative) {
/* 53 */       this.angle = angle;
/* 54 */       this.isRelative = isRelative;
/*    */     }
/*    */     
/*    */     public float getAngle(CommandSourceStack sender) {
/* 58 */       return Mth.wrapDegrees(this.isRelative ? (this.angle + (sender.getRotation()).y) : this.angle);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/AngleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */