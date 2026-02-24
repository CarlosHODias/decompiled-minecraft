/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class BuiltinTestFunctions
/*    */   extends TestFunctionLoader {
/* 12 */   public static final ResourceKey<Consumer<GameTestHelper>> ALWAYS_PASS = create("always_pass");
/* 13 */   public static final Consumer<GameTestHelper> ALWAYS_PASS_INSTANCE = GameTestHelper::succeed;
/*    */   
/*    */   private static ResourceKey<Consumer<GameTestHelper>> create(String name) {
/* 16 */     return ResourceKey.create(Registries.TEST_FUNCTION, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public static Consumer<GameTestHelper> bootstrap(Registry<Consumer<GameTestHelper>> registry) {
/* 20 */     registerLoader(new BuiltinTestFunctions());
/* 21 */     runLoaders(registry);
/* 22 */     return ALWAYS_PASS_INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> register) {
/* 27 */     register.accept(ALWAYS_PASS, ALWAYS_PASS_INSTANCE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/BuiltinTestFunctions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */