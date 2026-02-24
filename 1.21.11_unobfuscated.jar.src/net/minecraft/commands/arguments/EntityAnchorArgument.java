/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityAnchorArgument implements ArgumentType<EntityAnchorArgument.Anchor> {
/* 26 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "eyes", "feet" }); private static final DynamicCommandExceptionType ERROR_INVALID; static {
/* 27 */     ERROR_INVALID = new DynamicCommandExceptionType(name -> Component.translatableEscape("argument.anchor.invalid", new Object[] { name }));
/*    */   }
/*    */   public static Anchor getAnchor(CommandContext<CommandSourceStack> context, String name) {
/* 30 */     return (Anchor)context.getArgument(name, Anchor.class);
/*    */   }
/*    */   
/*    */   public static EntityAnchorArgument anchor() {
/* 34 */     return new EntityAnchorArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Anchor parse(StringReader reader) throws CommandSyntaxException {
/* 39 */     int start = reader.getCursor();
/* 40 */     String name = reader.readUnquotedString();
/* 41 */     Anchor anchor = Anchor.getByName(name);
/* 42 */     if (anchor == null) {
/* 43 */       reader.setCursor(start);
/* 44 */       throw ERROR_INVALID.createWithContext(reader, name);
/*    */     } 
/* 46 */     return anchor;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 51 */     return SharedSuggestionProvider.suggest(Anchor.BY_NAME.keySet(), builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 56 */     return EXAMPLES;
/*    */   }
/*    */   public enum Anchor { FEET, EYES; private static final Map<String, Anchor> BY_NAME;
/*    */     static {
/* 60 */       FEET = new Anchor("FEET", 0, "feet", (p, e) -> p);
/* 61 */       EYES = new Anchor("EYES", 1, "eyes", (p, e) -> new Vec3(p.x, p.y + e.getEyeHeight(), p.z));
/*    */     } private final String name; private final BiFunction<Vec3, Entity, Vec3> transform;
/*    */     static {
/* 64 */       BY_NAME = (Map<String, Anchor>)Util.make(Maps.newHashMap(), map -> {
/*    */             for (Anchor anchor : values()) {
/*    */               map.put(anchor.name, anchor);
/*    */             }
/*    */           });
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     Anchor(String name, BiFunction<Vec3, Entity, Vec3> transform) {
/* 74 */       this.name = name;
/* 75 */       this.transform = transform;
/*    */     }
/*    */     
/*    */     public static Anchor getByName(String name) {
/* 79 */       return BY_NAME.get(name);
/*    */     }
/*    */     
/*    */     public Vec3 apply(Entity entity) {
/* 83 */       return this.transform.apply(entity.position(), entity);
/*    */     }
/*    */     
/*    */     public Vec3 apply(CommandSourceStack source) {
/* 87 */       Entity entity = source.getEntity();
/* 88 */       if (entity == null) {
/* 89 */         return source.getPosition();
/*    */       }
/* 91 */       return this.transform.apply(source.getPosition(), entity);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/EntityAnchorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */