/*    */ package net.minecraft.server.commands.data;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Locale;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.CommandStorage;
/*    */ 
/*    */ public class StorageDataAccessor implements DataAccessor {
/*    */   static {
/* 25 */     SUGGEST_STORAGE = ((c, p) -> SharedSuggestionProvider.suggestResource(getGlobalTags(c).keys(), p));
/*    */   }
/*    */   
/*    */   private static final SuggestionProvider<CommandSourceStack> SUGGEST_STORAGE;
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = arg -> new DataCommands.DataProvider() { public DataAccessor access(CommandContext<CommandSourceStack> context) {
/* 31 */         return new StorageDataAccessor(StorageDataAccessor.getGlobalTags(context), IdentifierArgument.getId(context, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
/* 36 */         return parent.then(Commands.literal("storage").then(function.apply(Commands.argument(arg, (ArgumentType)IdentifierArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
/*    */       } }
/*    */   ;
/*    */   
/*    */   private static CommandStorage getGlobalTags(CommandContext<CommandSourceStack> context) {
/* 41 */     return ((CommandSourceStack)context.getSource()).getServer().getCommandStorage();
/*    */   }
/*    */   
/*    */   private final CommandStorage storage;
/*    */   private final Identifier id;
/*    */   
/*    */   private StorageDataAccessor(CommandStorage storage, Identifier id) {
/* 48 */     this.storage = storage;
/* 49 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag tag) {
/* 54 */     this.storage.set(this.id, tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getData() {
/* 59 */     return this.storage.get(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 64 */     return (Component)Component.translatable("commands.data.storage.modified", new Object[] { Component.translationArg(this.id) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag data) {
/* 69 */     return (Component)Component.translatable("commands.data.storage.query", new Object[] { Component.translationArg(this.id), NbtUtils.toPrettyComponent(data) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath path, double scale, int value) {
/* 74 */     return (Component)Component.translatable("commands.data.storage.get", new Object[] { path.asString(), Component.translationArg(this.id), String.format(Locale.ROOT, "%.2f", new Object[] { scale }), value });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/data/StorageDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */