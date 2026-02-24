/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.VarInt;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class SingleValuePalette<T>
/*    */   implements Palette<T>
/*    */ {
/*    */   private T value;
/*    */   
/*    */   public SingleValuePalette(List<T> paletteEntries) {
/* 16 */     if (!paletteEntries.isEmpty()) {
/* 17 */       Validate.isTrue((paletteEntries.size() <= 1), "Can't initialize SingleValuePalette with %d values.", paletteEntries.size());
/* 18 */       this.value = paletteEntries.getFirst();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <A> Palette<A> create(int bits, List<A> paletteEntries) {
/* 23 */     return new SingleValuePalette<>(paletteEntries);
/*    */   }
/*    */ 
/*    */   
/*    */   public int idFor(T value, PaletteResize<T> resizeHandler) {
/* 28 */     if (this.value == null || this.value == value) {
/* 29 */       this.value = value;
/* 30 */       return 0;
/*    */     } 
/* 32 */     return resizeHandler.onResize(1, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean maybeHas(Predicate<T> predicate) {
/* 37 */     if (this.value == null) {
/* 38 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 40 */     return predicate.test(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public T valueFor(int index) {
/* 45 */     if (this.value == null || index != 0) {
/* 46 */       throw new IllegalStateException("Missing Palette entry for id " + index + ".");
/*    */     }
/* 48 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/* 53 */     this.value = (T)globalMap.byIdOrThrow(buffer.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/* 58 */     if (this.value == null) {
/* 59 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 61 */     buffer.writeVarInt(globalMap.getId(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSerializedSize(IdMap<T> globalMap) {
/* 66 */     if (this.value == null) {
/* 67 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 69 */     return VarInt.getByteSize(globalMap.getId(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 74 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public Palette<T> copy() {
/* 79 */     if (this.value == null) {
/* 80 */       throw new IllegalStateException("Use of an uninitialized palette");
/*    */     }
/* 82 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/SingleValuePalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */