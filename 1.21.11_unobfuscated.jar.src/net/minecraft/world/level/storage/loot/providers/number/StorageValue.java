/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class StorageValue extends Record implements NumberProvider {
/*    */   private final Identifier storage;
/*    */   private final NbtPathArgument.NbtPath path;
/*    */   public static final com.mojang.serialization.MapCodec<StorageValue> CODEC;
/*    */   
/* 15 */   public StorageValue(Identifier storage, NbtPathArgument.NbtPath path) { this.storage = storage; this.path = path; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue; } public Identifier storage() { return this.storage; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public NbtPathArgument.NbtPath path() { return this.path; }
/*    */ 
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("storage").forGetter(StorageValue::storage), (App)NbtPathArgument.NbtPath.CODEC.fieldOf("path").forGetter(StorageValue::path)).apply((com.mojang.datafixers.kinds.Applicative)i, StorageValue::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootNumberProviderType getType() {
/* 26 */     return NumberProviders.STORAGE;
/*    */   }
/*    */   
/*    */   private Number getNumericTag(LootContext context, Number _default) {
/* 30 */     net.minecraft.nbt.CompoundTag value = context.getLevel().getServer().getCommandStorage().get(this.storage);
/*    */     
/*    */     try {
/* 33 */       java.util.List<Tag> selectedTags = this.path.get((Tag)value);
/* 34 */       if (selectedTags.size() == 1) {
/* 35 */         Tag tag = selectedTags.getFirst(); if (tag instanceof net.minecraft.nbt.NumericTag) { net.minecraft.nbt.NumericTag result = (net.minecraft.nbt.NumericTag)tag;
/* 36 */           return result.box(); }
/*    */       
/*    */       } 
/* 39 */     } catch (com.mojang.brigadier.exceptions.CommandSyntaxException commandSyntaxException) {}
/*    */ 
/*    */     
/* 42 */     return _default;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 47 */     return getNumericTag(context, 0.0F).floatValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt(LootContext context) {
/* 52 */     return getNumericTag(context, 0).intValue();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/number/StorageValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */