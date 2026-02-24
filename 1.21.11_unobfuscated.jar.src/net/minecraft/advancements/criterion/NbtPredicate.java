/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.storage.TagValueOutput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class NbtPredicate extends Record {
/*    */   private final CompoundTag tag;
/*    */   
/* 23 */   public NbtPredicate(CompoundTag tag) { this.tag = tag; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/NbtPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/NbtPredicate; } public CompoundTag tag() { return this.tag; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/NbtPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/NbtPredicate; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/NbtPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/NbtPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 26 */   } private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/* 27 */   public static final Codec<NbtPredicate> CODEC = TagParser.LENIENT_CODEC.xmap(NbtPredicate::new, NbtPredicate::tag);
/* 28 */   public static final StreamCodec<ByteBuf, NbtPredicate> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.COMPOUND_TAG.map(NbtPredicate::new, NbtPredicate::tag);
/*    */   
/*    */   public static final String SELECTED_ITEM_TAG = "SelectedItem";
/*    */   
/*    */   public boolean matches(DataComponentGetter components) {
/* 33 */     CustomData data = (CustomData)components.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.EMPTY);
/* 34 */     return data.matchedBy(this.tag);
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity) {
/* 38 */     return matches((Tag)getEntityTagToCompare(entity));
/*    */   }
/*    */   
/*    */   public boolean matches(Tag tag) {
/* 42 */     return (tag != null && net.minecraft.nbt.NbtUtils.compareNbt((Tag)this.tag, tag, true));
/*    */   }
/*    */   
/*    */   public static CompoundTag getEntityTagToCompare(Entity entity) {
/* 46 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); try {
/* 47 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 48 */       entity.saveWithoutId((net.minecraft.world.level.storage.ValueOutput)output);
/* 49 */       if (entity instanceof Player) { Player player = (Player)entity;
/* 50 */         ItemStack selected = player.getInventory().getSelectedItem();
/* 51 */         if (!selected.isEmpty()) {
/* 52 */           output.store("SelectedItem", ItemStack.CODEC, selected);
/*    */         } }
/*    */       
/* 55 */       CompoundTag compoundTag = output.buildResult();
/* 56 */       reporter.close();
/*    */       return compoundTag;
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         reporter.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/NbtPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */