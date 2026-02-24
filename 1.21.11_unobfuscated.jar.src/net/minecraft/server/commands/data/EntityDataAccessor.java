/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Locale;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.NbtPredicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.TagValueInput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntityDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 33 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType((Message)Component.translatable("commands.data.entity.invalid"));
/*    */   
/*    */   public static final Function<String, DataCommands.DataProvider> PROVIDER = arg -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 38 */         return new EntityDataAccessor(EntityArgument.getEntity(context, arg));
/*    */       }
/*    */ 
/*    */       
/*    */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
/* 43 */         return parent.then(Commands.literal("entity").then(function.apply(Commands.argument(arg, (ArgumentType)EntityArgument.entity()))));
/*    */       }
/*    */     };
/*    */   
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityDataAccessor(Entity entity) {
/* 50 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag tag) throws CommandSyntaxException {
/* 55 */     if (this.entity instanceof net.minecraft.world.entity.player.Player) {
/* 56 */       throw ERROR_NO_PLAYERS.create();
/*    */     }
/* 58 */     UUID uuid = this.entity.getUUID();
/* 59 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.entity.problemPath(), LOGGER); 
/* 60 */     try { this.entity.load(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.entity.registryAccess(), tag));
/* 61 */       this.entity.setUUID(uuid);
/* 62 */       reporter.close(); }
/*    */     catch (Throwable throwable) { try { reporter.close(); }
/*    */       catch (Throwable throwable1)
/*    */       { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 67 */      } public CompoundTag getData() { return NbtPredicate.getEntityTagToCompare(this.entity); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getModifiedSuccess() {
/* 72 */     return (Component)Component.translatable("commands.data.entity.modified", new Object[] { this.entity.getDisplayName() });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(Tag data) {
/* 77 */     return (Component)Component.translatable("commands.data.entity.query", new Object[] { this.entity.getDisplayName(), NbtUtils.toPrettyComponent(data) });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getPrintSuccess(NbtPathArgument.NbtPath path, double scale, int value) {
/* 82 */     return (Component)Component.translatable("commands.data.entity.get", new Object[] { path.asString(), this.entity.getDisplayName(), String.format(Locale.ROOT, "%.2f", new Object[] { scale }), value });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/data/EntityDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */