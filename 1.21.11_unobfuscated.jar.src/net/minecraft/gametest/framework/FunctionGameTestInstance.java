/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class FunctionGameTestInstance extends GameTestInstance {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.TEST_FUNCTION).fieldOf("function").forGetter(FunctionGameTestInstance::function), (App)TestData.CODEC.forGetter(GameTestInstance::info)).apply((Applicative)i, FunctionGameTestInstance::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FunctionGameTestInstance> CODEC;
/*    */   
/*    */   private final ResourceKey<Consumer<GameTestHelper>> function;
/*    */   
/*    */   public FunctionGameTestInstance(ResourceKey<Consumer<GameTestHelper>> function, TestData<Holder<TestEnvironmentDefinition>> info) {
/* 23 */     super(info);
/* 24 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper helper) {
/* 29 */     ((Consumer<GameTestHelper>)helper.getLevel().registryAccess().get(this.function)
/* 30 */       .map(Holder.Reference::value)
/* 31 */       .orElseThrow(() -> new IllegalStateException("Trying to access missing test function: " + String.valueOf(this.function.identifier()))))
/* 32 */       .accept(helper);
/*    */   }
/*    */   
/*    */   private ResourceKey<Consumer<GameTestHelper>> function() {
/* 36 */     return this.function;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<FunctionGameTestInstance> codec() {
/* 41 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected net.minecraft.network.chat.MutableComponent typeDescription() {
/* 46 */     return Component.translatable("test_instance.type.function");
/*    */   }
/*    */ 
/*    */   
/*    */   public Component describe() {
/* 51 */     return (Component)describeType()
/* 52 */       .append((Component)descriptionRow("test_instance.description.function", this.function.identifier().toString()))
/* 53 */       .append(describeInfo());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/FunctionGameTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */