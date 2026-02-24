/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ 
/*    */ public class ItemArgument implements ArgumentType<ItemInput> {
/* 16 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "stick{foo=bar}" });
/*    */   
/*    */   private final ItemParser parser;
/*    */   
/*    */   public ItemArgument(CommandBuildContext context) {
/* 21 */     this.parser = new ItemParser((HolderLookup.Provider)context);
/*    */   }
/*    */   
/*    */   public static ItemArgument item(CommandBuildContext context) {
/* 25 */     return new ItemArgument(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemInput parse(StringReader reader) throws CommandSyntaxException {
/* 30 */     ItemParser.ItemResult result = this.parser.parse(reader);
/* 31 */     return new ItemInput(result.item(), result.components());
/*    */   }
/*    */   
/*    */   public static <S> ItemInput getItem(CommandContext<S> context, String name) {
/* 35 */     return (ItemInput)context.getArgument(name, ItemInput.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 40 */     return this.parser.fillSuggestions(builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 45 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/item/ItemArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */