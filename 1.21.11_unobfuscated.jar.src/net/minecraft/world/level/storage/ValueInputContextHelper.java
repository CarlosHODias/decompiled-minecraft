/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ public class ValueInputContextHelper
/*     */ {
/*     */   private final HolderLookup.Provider lookup;
/*     */   private final DynamicOps<Tag> ops;
/*     */   
/*  18 */   private final ValueInput.ValueInputList emptyChildList = new ValueInput.ValueInputList(this)
/*     */     {
/*     */       public boolean isEmpty() {
/*  21 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public Stream<ValueInput> stream() {
/*  26 */         return Stream.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<ValueInput> iterator() {
/*  31 */         return Collections.emptyIterator();
/*     */       }
/*     */     };
/*     */   
/*  35 */   private final ValueInput.TypedInputList<Object> emptyTypedList = new ValueInput.TypedInputList<Object>(this)
/*     */     {
/*     */       public boolean isEmpty() {
/*  38 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public Stream<Object> stream() {
/*  43 */         return Stream.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<Object> iterator() {
/*  48 */         return Collections.emptyIterator();
/*     */       }
/*     */     };
/*     */   
/*  52 */   private final ValueInput empty = new ValueInput()
/*     */     {
/*     */       public <T> Optional<T> read(String name, Codec<T> codec) {
/*  55 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> Optional<T> read(MapCodec<T> codec) {
/*  60 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<ValueInput> child(String name) {
/*  65 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public ValueInput childOrEmpty(String name) {
/*  70 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<ValueInput.ValueInputList> childrenList(String name) {
/*  75 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public ValueInput.ValueInputList childrenListOrEmpty(String name) {
/*  80 */         return ValueInputContextHelper.this.emptyChildList;
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> Optional<ValueInput.TypedInputList<T>> list(String name, Codec<T> codec) {
/*  85 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> ValueInput.TypedInputList<T> listOrEmpty(String name, Codec<T> codec) {
/*  90 */         return ValueInputContextHelper.this.emptyTypedList();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean getBooleanOr(String name, boolean defaultValue) {
/*  95 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public byte getByteOr(String name, byte defaultValue) {
/* 100 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public int getShortOr(String name, short defaultValue) {
/* 105 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Integer> getInt(String name) {
/* 110 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public int getIntOr(String name, int defaultValue) {
/* 115 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getLongOr(String name, long defaultValue) {
/* 120 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Long> getLong(String name) {
/* 125 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public float getFloatOr(String name, float defaultValue) {
/* 130 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public double getDoubleOr(String name, double defaultValue) {
/* 135 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<String> getString(String name) {
/* 140 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public String getStringOr(String name, String defaultValue) {
/* 145 */         return defaultValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public HolderLookup.Provider lookup() {
/* 150 */         return ValueInputContextHelper.this.lookup;
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<int[]> getIntArray(String name) {
/* 155 */         return (Optional)Optional.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   public ValueInputContextHelper(HolderLookup.Provider lookup, DynamicOps<Tag> ops) {
/* 160 */     this.lookup = lookup;
/* 161 */     this.ops = (DynamicOps<Tag>)lookup.createSerializationContext(ops);
/*     */   }
/*     */   
/*     */   public DynamicOps<Tag> ops() {
/* 165 */     return this.ops;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider lookup() {
/* 169 */     return this.lookup;
/*     */   }
/*     */   
/*     */   public ValueInput empty() {
/* 173 */     return this.empty;
/*     */   }
/*     */   
/*     */   public ValueInput.ValueInputList emptyList() {
/* 177 */     return this.emptyChildList;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ValueInput.TypedInputList<T> emptyTypedList() {
/* 182 */     return (ValueInput.TypedInputList)this.emptyTypedList;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/ValueInputContextHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */