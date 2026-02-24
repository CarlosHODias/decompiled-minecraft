/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public final class TestData<EnvironmentType> extends Record {
/*    */   private final EnvironmentType environment;
/*    */   private final net.minecraft.resources.Identifier structure;
/*    */   private final int maxTicks;
/*    */   private final int setupTicks;
/*    */   private final boolean required;
/*    */   
/* 13 */   public TestData(EnvironmentType environment, net.minecraft.resources.Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation, boolean manualOnly, int maxAttempts, int requiredSuccesses, boolean skyAccess) { this.environment = environment; this.structure = structure; this.maxTicks = maxTicks; this.setupTicks = setupTicks; this.required = required; this.rotation = rotation; this.manualOnly = manualOnly; this.maxAttempts = maxAttempts; this.requiredSuccesses = requiredSuccesses; this.skyAccess = skyAccess; } private final Rotation rotation; private final boolean manualOnly; private final int maxAttempts; private final int requiredSuccesses; private final boolean skyAccess; public static final com.mojang.serialization.MapCodec<TestData<net.minecraft.core.Holder<TestEnvironmentDefinition>>> CODEC; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 13 */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestData<TEnvironmentType;>; } public EnvironmentType environment() { return this.environment; } public net.minecraft.resources.Identifier structure() { return this.structure; } public int maxTicks() { return this.maxTicks; } public int setupTicks() { return this.setupTicks; } public boolean required() { return this.required; } public Rotation rotation() { return this.rotation; } public boolean manualOnly() { return this.manualOnly; } public int maxAttempts() { return this.maxAttempts; } public int requiredSuccesses() { return this.requiredSuccesses; } public boolean skyAccess() { return this.skyAccess; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((App)TestEnvironmentDefinition.CODEC.fieldOf("environment").forGetter(TestData::environment), (App)net.minecraft.resources.Identifier.CODEC.fieldOf("structure").forGetter(TestData::structure), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("max_ticks").forGetter(TestData::maxTicks), (App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("setup_ticks", 0).forGetter(TestData::setupTicks), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("required", true).forGetter(TestData::required), (App)Rotation.CODEC.optionalFieldOf("rotation", Rotation.NONE).forGetter(TestData::rotation), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("manual_only", false).forGetter(TestData::manualOnly), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_attempts", 1).forGetter(TestData::maxAttempts), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("required_successes", 1).forGetter(TestData::requiredSuccesses), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("sky_access", false).forGetter(TestData::skyAccess)).apply((com.mojang.datafixers.kinds.Applicative)i, TestData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TestData(EnvironmentType environment, net.minecraft.resources.Identifier structure, int maxTicks, int setupTicks, boolean required, Rotation rotation) {
/* 39 */     this(environment, structure, maxTicks, setupTicks, required, rotation, false, 1, 1, false);
/*    */   }
/*    */   
/*    */   public TestData(EnvironmentType environment, net.minecraft.resources.Identifier structure, int maxTicks, int setupTicks, boolean required) {
/* 43 */     this(environment, structure, maxTicks, setupTicks, required, Rotation.NONE);
/*    */   }
/*    */   
/*    */   public <T> TestData<T> map(java.util.function.Function<EnvironmentType, T> mapper) {
/* 47 */     return new TestData((EnvironmentType)mapper.apply(this.environment), this.structure, this.maxTicks, this.setupTicks, this.required, this.rotation, this.manualOnly, this.maxAttempts, this.requiredSuccesses, this.skyAccess);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/TestData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */