/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Vec3Argument implements ArgumentType<Coordinates> {
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5" });
/*    */   
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos3d.incomplete"));
/* 25 */   public static final SimpleCommandExceptionType ERROR_MIXED_TYPE = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos.mixed"));
/*    */   
/*    */   private final boolean centerCorrect;
/*    */   
/*    */   public Vec3Argument(boolean centerCorrect) {
/* 30 */     this.centerCorrect = centerCorrect;
/*    */   }
/*    */   
/*    */   public static Vec3Argument vec3() {
/* 34 */     return new Vec3Argument(true);
/*    */   }
/*    */   
/*    */   public static Vec3Argument vec3(boolean centerCorrect) {
/* 38 */     return new Vec3Argument(centerCorrect);
/*    */   }
/*    */   
/*    */   public static Vec3 getVec3(CommandContext<CommandSourceStack> context, String name) {
/* 42 */     return ((Coordinates)context.getArgument(name, Coordinates.class)).getPosition((CommandSourceStack)context.getSource());
/*    */   }
/*    */   
/*    */   public static Coordinates getCoordinates(CommandContext<CommandSourceStack> context, String name) {
/* 46 */     return (Coordinates)context.getArgument(name, Coordinates.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 51 */     if (reader.canRead() && reader.peek() == '^') {
/* 52 */       return LocalCoordinates.parse(reader);
/*    */     }
/* 54 */     return WorldCoordinates.parseDouble(reader, this.centerCorrect);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 60 */     if (context.getSource() instanceof SharedSuggestionProvider) {
/* 61 */       Collection<SharedSuggestionProvider.TextCoordinates> suggestedCoordinates; String remainder = builder.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 65 */       if (!remainder.isEmpty() && remainder.charAt(0) == '^') {
/* 66 */         suggestedCoordinates = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 68 */         suggestedCoordinates = ((SharedSuggestionProvider)context.getSource()).getAbsoluteCoordinates();
/*    */       } 
/*    */       
/* 71 */       return SharedSuggestionProvider.suggestCoordinates(remainder, suggestedCoordinates, builder, Commands.createValidator(this::parse));
/*    */     } 
/* 73 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 79 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/coordinates/Vec3Argument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */