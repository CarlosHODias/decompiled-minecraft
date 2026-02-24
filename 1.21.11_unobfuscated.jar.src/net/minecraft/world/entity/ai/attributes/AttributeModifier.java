/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class AttributeModifier extends Record {
/*    */   private final Identifier id;
/*    */   private final double amount;
/*    */   private final Operation operation;
/*    */   public static final com.mojang.serialization.MapCodec<AttributeModifier> MAP_CODEC;
/*    */   
/* 15 */   public AttributeModifier(Identifier id, double amount, Operation operation) { this.id = id; this.amount = amount; this.operation = operation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public double amount() { return this.amount; } public Operation operation() { return this.operation; }
/*    */   
/* 17 */   public enum Operation implements net.minecraft.util.StringRepresentable { ADD_VALUE("add_value", 0),
/* 18 */     ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
/* 19 */     ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);
/*    */     
/* 21 */     public static final java.util.function.IntFunction<Operation> BY_ID = net.minecraft.util.ByIdMap.continuous(Operation::id, (Object[])values(), net.minecraft.util.ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 23 */     public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, Operation> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.idMapper(BY_ID, Operation::id);
/*    */     
/* 25 */     public static final Codec<Operation> CODEC = (Codec<Operation>)net.minecraft.util.StringRepresentable.fromEnum(Operation::values);
/*    */     
/*    */     private final String name;
/*    */     private final int id;
/*    */     
/*    */     Operation(String name, int id) {
/* 31 */       this.name = name;
/* 32 */       this.id = id;
/*    */     }
/*    */     
/*    */     public int id() {
/* 36 */       return this.id;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 41 */       return this.name;
/*    */     } }
/*    */   
/*    */   static {
/* 45 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(AttributeModifier::id), (App)Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::amount), (App)Operation.CODEC.fieldOf("operation").forGetter(AttributeModifier::operation)).apply((com.mojang.datafixers.kinds.Applicative)i, AttributeModifier::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static final Codec<AttributeModifier> CODEC = MAP_CODEC.codec();
/*    */   
/* 52 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, AttributeModifier> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, net.minecraft.network.codec.ByteBufCodecs.DOUBLE, AttributeModifier::amount, Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean is(Identifier id) {
/* 60 */     return id.equals(this.id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/attributes/AttributeModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */