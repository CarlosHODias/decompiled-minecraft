/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class CustomData {
/* 16 */   public static final CustomData EMPTY = new CustomData(new CompoundTag());
/*    */   public static final Codec<CustomData> CODEC;
/* 18 */   public static final Codec<CompoundTag> COMPOUND_TAG_CODEC = Codec.withAlternative(CompoundTag.CODEC, TagParser.FLATTENED_CODEC); static {
/* 19 */     CODEC = COMPOUND_TAG_CODEC.xmap(CustomData::new, data -> data.tag);
/*    */ 
/*    */ 
/*    */     
/* 23 */     STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(CustomData::new, data -> data.tag);
/*    */   }
/*    */   @Deprecated
/*    */   public static final StreamCodec<ByteBuf, CustomData> STREAM_CODEC; private final CompoundTag tag;
/*    */   
/*    */   private CustomData(CompoundTag tag) {
/* 29 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public static CustomData of(CompoundTag tag) {
/* 33 */     return new CustomData(tag.copy());
/*    */   }
/*    */   
/*    */   public boolean matchedBy(CompoundTag expectedTag) {
/* 37 */     return NbtUtils.compareNbt((Tag)expectedTag, (Tag)this.tag, true);
/*    */   }
/*    */   
/*    */   public static void update(DataComponentType<CustomData> component, ItemStack itemStack, Consumer<CompoundTag> consumer) {
/* 41 */     CustomData newData = ((CustomData)itemStack.getOrDefault(component, EMPTY)).update(consumer);
/* 42 */     if (newData.tag.isEmpty()) {
/* 43 */       itemStack.remove(component);
/*    */     } else {
/* 45 */       itemStack.set(component, newData);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void set(DataComponentType<CustomData> component, ItemStack itemStack, CompoundTag tag) {
/* 50 */     if (!tag.isEmpty()) {
/* 51 */       itemStack.set(component, of(tag));
/*    */     } else {
/* 53 */       itemStack.remove(component);
/*    */     } 
/*    */   }
/*    */   
/*    */   public CustomData update(Consumer<CompoundTag> consumer) {
/* 58 */     CompoundTag newTag = this.tag.copy();
/* 59 */     consumer.accept(newTag);
/* 60 */     return new CustomData(newTag);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 64 */     return this.tag.isEmpty();
/*    */   }
/*    */   
/*    */   public CompoundTag copyTag() {
/* 68 */     return this.tag.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     if (obj == this) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (obj instanceof CustomData) { CustomData customData = (CustomData)obj;
/* 77 */       return this.tag.equals(customData.tag); }
/*    */     
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 84 */     return this.tag.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return this.tag.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/CustomData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */