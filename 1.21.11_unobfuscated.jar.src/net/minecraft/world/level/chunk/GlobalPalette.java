/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class GlobalPalette<T>
/*    */   implements Palette<T> {
/*    */   private final IdMap<T> registry;
/*    */   
/*    */   public GlobalPalette(IdMap<T> registry) {
/* 12 */     this.registry = registry;
/*    */   }
/*    */ 
/*    */   
/*    */   public int idFor(T value, PaletteResize<T> resizeHandler) {
/* 17 */     int id = this.registry.getId(value);
/*    */     
/* 19 */     return (id == -1) ? 0 : id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean maybeHas(Predicate<T> predicate) {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T valueFor(int index) {
/* 29 */     T value = (T)this.registry.byId(index);
/* 30 */     if (value == null) {
/* 31 */       throw new MissingPaletteEntryException(index);
/*    */     }
/* 33 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf buffer, IdMap<T> globalMap) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {}
/*    */ 
/*    */   
/*    */   public int getSerializedSize(IdMap<T> globalMap) {
/* 46 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 51 */     return this.registry.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public Palette<T> copy() {
/* 56 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/GlobalPalette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */