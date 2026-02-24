/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class PropertyValueList extends Record {
/*    */   private final List<Property.Value<?>> values;
/*    */   
/* 11 */   public PropertyValueList(List<Property.Value<?>> values) { this.values = values; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/blockstates/PropertyValueList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/client/data/models/blockstates/PropertyValueList; } public List<Property.Value<?>> values() { return this.values; }
/*    */    public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/blockstates/PropertyValueList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/blockstates/PropertyValueList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 14 */   } public static final PropertyValueList EMPTY = new PropertyValueList(List.of()); private static final Comparator<Property.Value<?>> COMPARE_BY_NAME;
/*    */   static {
/* 16 */     COMPARE_BY_NAME = Comparator.comparing(p -> p.property().getName());
/*    */   }
/*    */   public PropertyValueList extend(Property.Value<?> element) {
/* 19 */     return new PropertyValueList(net.minecraft.util.Util.copyAndAdd(this.values, element));
/*    */   }
/*    */   
/*    */   public PropertyValueList extend(PropertyValueList other) {
/* 23 */     return new PropertyValueList((List<Property.Value<?>>)com.google.common.collect.ImmutableList.builder().addAll(this.values).addAll(other.values).build());
/*    */   }
/*    */   
/*    */   public static PropertyValueList of(Property.Value<?>... values) {
/* 27 */     return new PropertyValueList(List.of(values));
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 31 */     return this.values.stream().sorted(COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 36 */     return getKey();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/blockstates/PropertyValueList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */