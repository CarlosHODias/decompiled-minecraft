/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class EntityDataSource extends Record implements DataSource {
/*    */   private final String selectorPattern;
/*    */   
/* 19 */   public EntityDataSource(String selectorPattern, EntitySelector compiledSelector) { this.selectorPattern = selectorPattern; this.compiledSelector = compiledSelector; } private final EntitySelector compiledSelector; public static final MapCodec<EntityDataSource> MAP_CODEC; public String selectorPattern() { return this.selectorPattern; } public EntitySelector compiledSelector() { return this.compiledSelector; } static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("entity").forGetter(EntityDataSource::selectorPattern)).apply((Applicative)i, EntityDataSource::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDataSource(String selector) {
/* 25 */     this(selector, compileSelector(selector));
/*    */   }
/*    */   
/*    */   private static EntitySelector compileSelector(String selector) {
/*    */     try {
/* 30 */       EntitySelectorParser parser = new EntitySelectorParser(new com.mojang.brigadier.StringReader(selector), true);
/* 31 */       return parser.parse();
/* 32 */     } catch (CommandSyntaxException ex) {
/* 33 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<net.minecraft.nbt.CompoundTag> getData(CommandSourceStack sender) throws CommandSyntaxException {
/* 39 */     if (this.compiledSelector != null) {
/* 40 */       java.util.List<? extends Entity> entities = this.compiledSelector.findEntities(sender);
/* 41 */       return entities.stream().map(net.minecraft.advancements.criterion.NbtPredicate::getEntityTagToCompare);
/*    */     } 
/*    */     
/* 44 */     return Stream.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<EntityDataSource> codec() {
/* 49 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "entity=" + this.selectorPattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: if_acmpne -> 7
/*    */     //   5: iconst_1
/*    */     //   6: ireturn
/*    */     //   7: aload_1
/*    */     //   8: instanceof net/minecraft/network/chat/contents/data/EntityDataSource
/*    */     //   11: ifeq -> 37
/*    */     //   14: aload_1
/*    */     //   15: checkcast net/minecraft/network/chat/contents/data/EntityDataSource
/*    */     //   18: astore_2
/*    */     //   19: aload_0
/*    */     //   20: getfield selectorPattern : Ljava/lang/String;
/*    */     //   23: aload_2
/*    */     //   24: getfield selectorPattern : Ljava/lang/String;
/*    */     //   27: invokevirtual equals : (Ljava/lang/Object;)Z
/*    */     //   30: ifeq -> 37
/*    */     //   33: iconst_1
/*    */     //   34: goto -> 38
/*    */     //   37: iconst_0
/*    */     //   38: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     //   #60	-> 5
/*    */     //   #63	-> 7
/*    */     //   #62	-> 14
/*    */     //   #63	-> 27
/*    */     //   #62	-> 38
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   19	18	2	that	Lnet/minecraft/network/chat/contents/data/EntityDataSource;
/*    */     //   0	39	0	this	Lnet/minecraft/network/chat/contents/data/EntityDataSource;
/*    */     //   0	39	1	o	Ljava/lang/Object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return this.selectorPattern.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/data/EntityDataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */