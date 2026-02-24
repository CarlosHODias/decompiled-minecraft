/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GameTestEnvironments
/*    */ {
/*    */   public static final String DEFAULT = "default";
/* 14 */   public static final ResourceKey<TestEnvironmentDefinition> DEFAULT_KEY = create("default");
/*    */   
/*    */   private static ResourceKey<TestEnvironmentDefinition> create(String name) {
/* 17 */     return ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<TestEnvironmentDefinition> context) {
/* 21 */     context.register(DEFAULT_KEY, new TestEnvironmentDefinition.AllOf(List.of()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestEnvironments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */