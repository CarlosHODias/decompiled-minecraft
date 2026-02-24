/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class GeneratedTest extends Record {
/*    */   private final java.util.Map<net.minecraft.resources.Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests;
/*    */   private final ResourceKey<java.util.function.Consumer<GameTestHelper>> functionKey;
/*    */   private final java.util.function.Consumer<GameTestHelper> function;
/*    */   
/* 10 */   public GeneratedTest(java.util.Map<net.minecraft.resources.Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests, ResourceKey<java.util.function.Consumer<GameTestHelper>> functionKey, java.util.function.Consumer<GameTestHelper> function) { this.tests = tests; this.functionKey = functionKey; this.function = function; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/GeneratedTest;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest; } public java.util.Map<net.minecraft.resources.Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests() { return this.tests; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/GeneratedTest;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/GeneratedTest; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/GeneratedTest;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/GeneratedTest;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<java.util.function.Consumer<GameTestHelper>> functionKey() { return this.functionKey; } public java.util.function.Consumer<GameTestHelper> function() { return this.function; }
/*    */    public GeneratedTest(java.util.Map<net.minecraft.resources.Identifier, TestData<ResourceKey<TestEnvironmentDefinition>>> tests, net.minecraft.resources.Identifier functionId, java.util.function.Consumer<GameTestHelper> function) {
/* 12 */     this(tests, ResourceKey.create(net.minecraft.core.registries.Registries.TEST_FUNCTION, functionId), function);
/*    */   }
/*    */   
/*    */   public GeneratedTest(net.minecraft.resources.Identifier id, TestData<ResourceKey<TestEnvironmentDefinition>> testData, java.util.function.Consumer<GameTestHelper> function) {
/* 16 */     this(java.util.Map.of(id, testData), id, function);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GeneratedTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */