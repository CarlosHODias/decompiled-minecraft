/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.color.block.BlockColors;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RenderShape;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ModelGroupCollector
/*    */ {
/*    */   static final int SINGLETON_MODEL_GROUP = -1;
/*    */   private static final int INVISIBLE_MODEL_GROUP = 0;
/*    */   
/*    */   public static Object2IntMap<BlockState> build(BlockColors blockColors, BlockStateModelLoader.LoadedModels input) {
/* 24 */     Map<Block, List<Property<?>>> coloringPropertiesCache = new HashMap<>();
/* 25 */     Map<GroupKey, Set<BlockState>> modelGroups = new HashMap<>();
/*    */     
/* 27 */     input.models().forEach((state, loadedModel) -> {
/*    */           List<Property<?>> coloringProperties = coloringPropertiesCache.computeIfAbsent(state.getBlock(), ());
/*    */ 
/*    */           
/*    */           GroupKey key = GroupKey.create(state, loadedModel, coloringProperties);
/*    */           
/*    */           ((Set<BlockState>)modelGroups.computeIfAbsent(key, ())).add(state);
/*    */         });
/*    */     
/* 36 */     int nextModelGroup = 1;
/* 37 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 38 */     object2IntOpenHashMap.defaultReturnValue(-1);
/*    */     
/* 40 */     for (Set<BlockState> states : modelGroups.values()) {
/* 41 */       Iterator<BlockState> it = states.iterator();
/* 42 */       while (it.hasNext()) {
/* 43 */         BlockState state = it.next();
/* 44 */         if (state.getRenderShape() != RenderShape.MODEL) {
/* 45 */           it.remove();
/* 46 */           object2IntOpenHashMap.put(state, 0);
/*    */         } 
/*    */       } 
/*    */       
/* 50 */       if (states.size() > 1) {
/* 51 */         int modelGroup = nextModelGroup++;
/* 52 */         states.forEach(blockState -> result.put(blockState, modelGroup));
/*    */       } 
/*    */     } 
/*    */     
/* 56 */     return (Object2IntMap<BlockState>)object2IntOpenHashMap;
/*    */   }
/*    */   private static final class GroupKey extends Record { private final Object equalityGroup; private final List<Object> coloringValues;
/* 59 */     private GroupKey(Object equalityGroup, List<Object> coloringValues) { this.equalityGroup = equalityGroup; this.coloringValues = coloringValues; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 59 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey; } public Object equalityGroup() { return this.equalityGroup; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;
/* 59 */       //   0	8	1	o	Ljava/lang/Object; } public List<Object> coloringValues() { return this.coloringValues; }
/*    */      public static GroupKey create(BlockState state, BlockStateModel.UnbakedRoot model, List<Property<?>> coloringProperties) {
/* 61 */       List<Object> coloringValues = getColoringValues(state, coloringProperties);
/* 62 */       Object equalityGroup = model.visualEqualityGroup(state);
/* 63 */       return new GroupKey(equalityGroup, coloringValues);
/*    */     }
/*    */     
/*    */     private static List<Object> getColoringValues(BlockState state, List<Property<?>> coloringProperties) {
/* 67 */       Object[] coloringValues = new Object[coloringProperties.size()];
/* 68 */       for (int i = 0; i < coloringProperties.size(); i++) {
/* 69 */         coloringValues[i] = state.getValue(coloringProperties.get(i));
/*    */       }
/* 71 */       return List.of(coloringValues);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelGroupCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */