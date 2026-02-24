/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class StorageDataSource extends Record implements DataSource {
/*    */   private final Identifier id;
/*    */   public static final com.mojang.serialization.MapCodec<StorageDataSource> MAP_CODEC;
/*    */   
/* 11 */   public StorageDataSource(Identifier id) { this.id = id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource; } public Identifier id() { return this.id; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Identifier.CODEC.fieldOf("storage").forGetter(StorageDataSource::id)).apply((com.mojang.datafixers.kinds.Applicative)i, StorageDataSource::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public java.util.stream.Stream<CompoundTag> getData(net.minecraft.commands.CommandSourceStack sender) {
/* 18 */     CompoundTag tag = sender.getServer().getCommandStorage().get(this.id);
/* 19 */     return java.util.stream.Stream.of(tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<StorageDataSource> codec() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     return "storage=" + String.valueOf(this.id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/contents/data/StorageDataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */