/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class StructureFeatureIndexSavedData extends net.minecraft.world.level.saveddata.SavedData {
/*    */   private final LongSet all;
/* 16 */   private static final Codec<LongSet> LONG_SET = Codec.LONG_STREAM.xmap(LongOpenHashSet::toSet, LongCollection::longStream); private final LongSet remaining; public static final Codec<StructureFeatureIndexSavedData> CODEC;
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LONG_SET.fieldOf("All").forGetter(()), (App)LONG_SET.fieldOf("Remaining").forGetter(())).apply((Applicative)i, StructureFeatureIndexSavedData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static SavedDataType<StructureFeatureIndexSavedData> type(String id) {
/* 24 */     return new SavedDataType(id, StructureFeatureIndexSavedData::new, CODEC, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES);
/*    */   }
/*    */   
/*    */   private StructureFeatureIndexSavedData(LongSet all, LongSet remaining) {
/* 28 */     this.all = all;
/* 29 */     this.remaining = remaining;
/*    */   }
/*    */   
/*    */   public StructureFeatureIndexSavedData() {
/* 33 */     this((LongSet)new LongOpenHashSet(), (LongSet)new LongOpenHashSet());
/*    */   }
/*    */   
/*    */   public void addIndex(long chunkPosKey) {
/* 37 */     this.all.add(chunkPosKey);
/* 38 */     this.remaining.add(chunkPosKey);
/* 39 */     setDirty();
/*    */   }
/*    */   
/*    */   public boolean hasStartIndex(long chunkPosKey) {
/* 43 */     return this.all.contains(chunkPosKey);
/*    */   }
/*    */   
/*    */   public boolean hasUnhandledIndex(long chunkPosKey) {
/* 47 */     return this.remaining.contains(chunkPosKey);
/*    */   }
/*    */   
/*    */   public void removeIndex(long chunkPosKey) {
/* 51 */     if (this.remaining.remove(chunkPosKey)) {
/* 52 */       setDirty();
/*    */     }
/*    */   }
/*    */   
/*    */   public LongSet getAll() {
/* 57 */     return this.all;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/StructureFeatureIndexSavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */