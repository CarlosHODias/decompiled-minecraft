/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.KeyMapping;
/*    */ 
/*    */ public final class IsKeybindDown extends Record implements ConditionalItemModelProperty {
/*    */   private final KeyMapping keybind;
/*    */   private static final com.mojang.serialization.Codec<KeyMapping> KEYBIND_CODEC;
/*    */   public static final com.mojang.serialization.MapCodec<IsKeybindDown> MAP_CODEC;
/*    */   
/* 14 */   public IsKeybindDown(KeyMapping keybind) { this.keybind = keybind; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown; } public KeyMapping keybind() { return this.keybind; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/IsKeybindDown;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } static { KEYBIND_CODEC = com.mojang.serialization.Codec.STRING.comapFlatMap(id -> { KeyMapping mapping = KeyMapping.get(id); return (mapping != null) ? DataResult.success(mapping) : DataResult.error(()); }, KeyMapping::getName);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)KEYBIND_CODEC.fieldOf("keybind").forGetter(IsKeybindDown::keybind)).apply((com.mojang.datafixers.kinds.Applicative)i, IsKeybindDown::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean get(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner, int seed, net.minecraft.world.item.ItemDisplayContext displayContext) {
/* 29 */     return this.keybind.isDown();
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<IsKeybindDown> type() {
/* 34 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/IsKeybindDown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */