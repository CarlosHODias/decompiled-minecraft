/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public class CacheableFunction {
/* 11 */   public static final Codec<CacheableFunction> CODEC = Identifier.CODEC.xmap(CacheableFunction::new, CacheableFunction::getId);
/*    */   
/*    */   private final Identifier id;
/*    */   private boolean resolved;
/* 15 */   private Optional<CommandFunction<CommandSourceStack>> function = Optional.empty();
/*    */   
/*    */   public CacheableFunction(Identifier id) {
/* 18 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Optional<CommandFunction<CommandSourceStack>> get(ServerFunctionManager manager) {
/* 22 */     if (!this.resolved) {
/* 23 */       this.function = manager.get(this.id);
/* 24 */       this.resolved = true;
/*    */     } 
/* 26 */     return this.function;
/*    */   }
/*    */   
/*    */   public Identifier getId() {
/* 30 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 35 */     if (obj == this) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (obj instanceof CacheableFunction) { CacheableFunction cacheableFunction = (CacheableFunction)obj; if (getId().equals(cacheableFunction.getId())); }  return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/CacheableFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */