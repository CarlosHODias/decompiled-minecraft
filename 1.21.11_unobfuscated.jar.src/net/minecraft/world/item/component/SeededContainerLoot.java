/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class SeededContainerLoot extends Record implements TooltipProvider {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   private final long seed;
/*    */   
/* 14 */   public SeededContainerLoot(ResourceKey<LootTable> lootTable, long seed) { this.lootTable = lootTable; this.seed = seed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/SeededContainerLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot; } public ResourceKey<LootTable> lootTable() { return this.lootTable; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/SeededContainerLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/SeededContainerLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/SeededContainerLoot;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public long seed() { return this.seed; }
/* 15 */    private static final Component UNKNOWN_CONTENTS = (Component)Component.translatable("item.container.loot_table.unknown"); public static final Codec<SeededContainerLoot> CODEC;
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(SeededContainerLoot::lootTable), (App)Codec.LONG.optionalFieldOf("seed", 0L).forGetter(SeededContainerLoot::seed)).apply((com.mojang.datafixers.kinds.Applicative)i, SeededContainerLoot::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(net.minecraft.world.item.Item.TooltipContext context, java.util.function.Consumer<Component> consumer, net.minecraft.world.item.TooltipFlag flag, net.minecraft.core.component.DataComponentGetter components) {
/* 24 */     consumer.accept(UNKNOWN_CONTENTS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/SeededContainerLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */