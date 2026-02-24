/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ 
/*    */ public final class CustomAll extends Record implements Action {
/*    */   private final net.minecraft.resources.Identifier id;
/*    */   private final Optional<CompoundTag> additions;
/*    */   public static final com.mojang.serialization.MapCodec<CustomAll> MAP_CODEC;
/*    */   
/* 12 */   public CustomAll(net.minecraft.resources.Identifier id, Optional<CompoundTag> additions) { this.id = id; this.additions = additions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CustomAll;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll; } public net.minecraft.resources.Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CustomAll;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CustomAll;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CustomAll;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<CompoundTag> additions() { return this.additions; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.resources.Identifier.CODEC.fieldOf("id").forGetter(CustomAll::id), (com.mojang.datafixers.kinds.App)CompoundTag.CODEC.optionalFieldOf("additions").forGetter(CustomAll::additions)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomAll::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CustomAll> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<net.minecraft.network.chat.ClickEvent> createAction(java.util.Map<String, Action.ValueGetter> parameters) {
/* 28 */     CompoundTag tag = this.additions.<CompoundTag>map(CompoundTag::copy).orElseGet(CompoundTag::new);
/* 29 */     parameters.forEach((key, value) -> tag.put(key, value.asTag()));
/* 30 */     return (Optional)Optional.of(new net.minecraft.network.chat.ClickEvent.Custom(this.id, Optional.of(tag)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/action/CustomAll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */