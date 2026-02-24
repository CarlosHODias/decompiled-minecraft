/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class EnumProperty<T extends Enum<T> & StringRepresentable>
/*    */   extends Property<T> {
/*    */   private final List<T> values;
/*    */   private final Map<String, T> names;
/*    */   private final int[] ordinalToIndex;
/*    */   
/*    */   private EnumProperty(String name, Class<T> clazz, List<T> values) {
/* 19 */     super(name, clazz);
/*    */     
/* 21 */     if (values.isEmpty()) {
/* 22 */       throw new IllegalArgumentException("Trying to make empty EnumProperty '" + name + "'");
/*    */     }
/*    */     
/* 25 */     this.values = List.copyOf(values);
/* 26 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/* 27 */     this.ordinalToIndex = new int[arrayOfEnum.length];
/* 28 */     for (Enum enum_ : arrayOfEnum) {
/* 29 */       this.ordinalToIndex[enum_.ordinal()] = values.indexOf(enum_);
/*    */     }
/*    */     
/* 32 */     ImmutableMap.Builder<String, T> names = ImmutableMap.builder();
/* 33 */     for (Enum enum_ : values) {
/* 34 */       String key = ((StringRepresentable)enum_).getSerializedName();
/* 35 */       names.put(key, enum_);
/*    */     } 
/* 37 */     this.names = (Map<String, T>)names.buildOrThrow();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> getPossibleValues() {
/* 42 */     return this.values;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> getValue(String name) {
/* 47 */     return Optional.ofNullable(this.names.get(name));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName(T value) {
/* 52 */     return ((StringRepresentable)value).getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInternalIndex(T value) {
/* 57 */     return this.ordinalToIndex[value.ordinal()];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 62 */     if (this == o) {
/* 63 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 67 */     if (o instanceof EnumProperty) { EnumProperty<?> that = (EnumProperty)o; if (super.equals(o)) {
/* 68 */         return this.values.equals(that.values);
/*    */       } }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int generateHashCode() {
/* 76 */     int result = super.generateHashCode();
/* 77 */     result = 31 * result + this.values.hashCode();
/* 78 */     return result;
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz) {
/* 82 */     return create(name, clazz, t -> true);
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, Predicate<T> filter) {
/* 86 */     return create(name, clazz, (List<T>)Arrays.<T>stream(clazz.getEnumConstants()).filter(filter).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, T... values) {
/* 91 */     return create(name, clazz, List.of(values));
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String name, Class<T> clazz, List<T> values) {
/* 95 */     return new EnumProperty<>(name, clazz, values);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/EnumProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */