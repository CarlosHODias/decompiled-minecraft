package net.minecraft.nbt;

public interface TagVisitor {
  void visitString(StringTag paramStringTag);
  
  void visitByte(ByteTag paramByteTag);
  
  void visitShort(ShortTag paramShortTag);
  
  void visitInt(IntTag paramIntTag);
  
  void visitLong(LongTag paramLongTag);
  
  void visitFloat(FloatTag paramFloatTag);
  
  void visitDouble(DoubleTag paramDoubleTag);
  
  void visitByteArray(ByteArrayTag paramByteArrayTag);
  
  void visitIntArray(IntArrayTag paramIntArrayTag);
  
  void visitLongArray(LongArrayTag paramLongArrayTag);
  
  void visitList(ListTag paramListTag);
  
  void visitCompound(CompoundTag paramCompoundTag);
  
  void visitEnd(EndTag paramEndTag);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/TagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */