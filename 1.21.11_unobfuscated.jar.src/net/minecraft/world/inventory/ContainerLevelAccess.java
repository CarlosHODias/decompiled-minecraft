/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface ContainerLevelAccess
/*    */ {
/* 11 */   public static final ContainerLevelAccess NULL = new ContainerLevelAccess()
/*    */     {
/*    */       public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> action) {
/* 14 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   static ContainerLevelAccess create(final Level level, final BlockPos pos) {
/* 19 */     return new ContainerLevelAccess()
/*    */       {
/*    */         public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> action) {
/* 22 */           return Optional.of(action.apply(level, pos));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> paramBiFunction);
/*    */   
/*    */   default <T> T evaluate(BiFunction<Level, BlockPos, T> action, T defaultValue) {
/* 30 */     return evaluate(action).orElse(defaultValue);
/*    */   }
/*    */   
/*    */   default void execute(BiConsumer<Level, BlockPos> action) {
/* 34 */     evaluate((level, pos) -> {
/*    */           action.accept(level, pos);
/*    */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ContainerLevelAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */