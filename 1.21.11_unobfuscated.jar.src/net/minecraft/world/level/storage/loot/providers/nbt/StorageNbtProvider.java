/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class StorageNbtProvider extends Record implements NbtProvider {
/*    */   private final Identifier id;
/*    */   public static final com.mojang.serialization.MapCodec<StorageNbtProvider> CODEC;
/*    */   
/* 12 */   public StorageNbtProvider(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider; } public Identifier id() { return this.id; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("source").forGetter(StorageNbtProvider::id)).apply((com.mojang.datafixers.kinds.Applicative)i, StorageNbtProvider::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootNbtProviderType getType() {
/* 19 */     return NbtProviders.STORAGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.nbt.Tag get(net.minecraft.world.level.storage.loot.LootContext context) {
/* 24 */     return (net.minecraft.nbt.Tag)context.getLevel().getServer().getCommandStorage().get(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 29 */     return Set.of();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/nbt/StorageNbtProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */