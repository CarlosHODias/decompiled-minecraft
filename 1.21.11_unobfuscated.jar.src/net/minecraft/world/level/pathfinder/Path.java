/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Path {
/*     */   public static final StreamCodec<FriendlyByteBuf, Path> STREAM_CODEC;
/*     */   private final List<Node> nodes;
/*     */   private DebugData debugData;
/*     */   private int nextNodeIndex;
/*     */   private final BlockPos target;
/*     */   private final float distToTarget;
/*     */   private final boolean reached;
/*     */   
/*     */   static {
/*  24 */     STREAM_CODEC = StreamCodec.of((output, value) -> value.writeToStream(output), Path::createFromStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(List<Node> nodes, BlockPos target, boolean reached) {
/*  36 */     this.nodes = nodes;
/*  37 */     this.target = target;
/*     */     
/*  39 */     this.distToTarget = nodes.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target);
/*     */     
/*  41 */     this.reached = reached;
/*     */   }
/*     */   
/*     */   public void advance() {
/*  45 */     this.nextNodeIndex++;
/*     */   }
/*     */   
/*     */   public boolean notStarted() {
/*  49 */     return (this.nextNodeIndex <= 0);
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  53 */     return (this.nextNodeIndex >= this.nodes.size());
/*     */   }
/*     */   
/*     */   public Node getEndNode() {
/*  57 */     if (!this.nodes.isEmpty()) {
/*  58 */       return this.nodes.get(this.nodes.size() - 1);
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public Node getNode(int i) {
/*  64 */     return this.nodes.get(i);
/*     */   }
/*     */   
/*     */   public void truncateNodes(int index) {
/*  68 */     if (this.nodes.size() > index) {
/*  69 */       this.nodes.subList(index, this.nodes.size()).clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceNode(int index, Node replaceWith) {
/*  74 */     this.nodes.set(index, replaceWith);
/*     */   }
/*     */   
/*     */   public int getNodeCount() {
/*  78 */     return this.nodes.size();
/*     */   }
/*     */   
/*     */   public int getNextNodeIndex() {
/*  82 */     return this.nextNodeIndex;
/*     */   }
/*     */   
/*     */   public void setNextNodeIndex(int nextNodeIndex) {
/*  86 */     this.nextNodeIndex = nextNodeIndex;
/*     */   }
/*     */   
/*     */   public Vec3 getEntityPosAtNode(Entity entity, int index) {
/*  90 */     Node node = this.nodes.get(index);
/*  91 */     double x = node.x + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  92 */     double y = node.y;
/*  93 */     double z = node.z + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  94 */     return new Vec3(x, y, z);
/*     */   }
/*     */   
/*     */   public BlockPos getNodePos(int index) {
/*  98 */     return ((Node)this.nodes.get(index)).asBlockPos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getNextEntityPos(Entity entity) {
/* 105 */     return getEntityPosAtNode(entity, this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   public BlockPos getNextNodePos() {
/* 109 */     return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos();
/*     */   }
/*     */   
/*     */   public Node getNextNode() {
/* 113 */     return this.nodes.get(this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   public Node getPreviousNode() {
/* 117 */     return (this.nextNodeIndex > 0) ? this.nodes.get(this.nextNodeIndex - 1) : null;
/*     */   }
/*     */   
/*     */   public boolean sameAs(Path path) {
/* 121 */     return (path != null && this.nodes.equals(path.nodes));
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*     */     Path path;
/* 126 */     if (obj instanceof Path) { path = (Path)obj; }
/* 127 */     else { return false; }
/*     */     
/* 129 */     return (this.nextNodeIndex == path.nextNodeIndex && this.debugData == path.debugData && this.reached == path.reached && 
/*     */ 
/*     */       
/* 132 */       this.target.equals(path.target) && 
/* 133 */       this.nodes.equals(path.nodes));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     return this.nextNodeIndex + this.nodes.hashCode() * 31;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canReach() {
/* 145 */     return this.reached;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   void setDebug(Node[] openSet, Node[] closedSet, Set<Target> targets) {
/* 150 */     this.debugData = new DebugData(openSet, closedSet, targets);
/*     */   }
/*     */   
/*     */   public DebugData debugData() {
/* 154 */     return this.debugData;
/*     */   }
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf buffer) {
/* 158 */     if (this.debugData == null || this.debugData.targetNodes.isEmpty()) {
/* 159 */       throw new IllegalStateException("Missing debug data");
/*     */     }
/*     */     
/* 162 */     buffer.writeBoolean(this.reached);
/* 163 */     buffer.writeInt(this.nextNodeIndex);
/* 164 */     buffer.writeBlockPos(this.target);
/* 165 */     buffer.writeCollection(this.nodes, (out, node) -> node.writeToStream(out));
/* 166 */     this.debugData.write(buffer);
/*     */   }
/*     */   
/*     */   public static Path createFromStream(FriendlyByteBuf buffer) {
/* 170 */     boolean reached = buffer.readBoolean();
/* 171 */     int indexStream = buffer.readInt();
/* 172 */     BlockPos target = buffer.readBlockPos();
/* 173 */     List<Node> nodes = buffer.readList(Node::createFromStream);
/* 174 */     DebugData debugData = DebugData.read(buffer);
/*     */     
/* 176 */     Path path = new Path(nodes, target, reached);
/* 177 */     path.debugData = debugData;
/* 178 */     path.nextNodeIndex = indexStream;
/*     */     
/* 180 */     return path;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return "Path(length=" + this.nodes.size() + ")";
/*     */   }
/*     */   
/*     */   public BlockPos getTarget() {
/* 189 */     return this.target;
/*     */   }
/*     */   
/*     */   public float getDistToTarget() {
/* 193 */     return this.distToTarget;
/*     */   }
/*     */   
/*     */   private static Node[] readNodeArray(FriendlyByteBuf input) {
/* 197 */     Node[] nodes = new Node[input.readVarInt()];
/* 198 */     for (int i = 0; i < nodes.length; i++) {
/* 199 */       nodes[i] = Node.createFromStream(input);
/*     */     }
/* 201 */     return nodes;
/*     */   }
/*     */   
/*     */   private static void writeNodeArray(FriendlyByteBuf output, Node[] nodes) {
/* 205 */     output.writeVarInt(nodes.length);
/* 206 */     for (Node node : nodes) {
/* 207 */       node.writeToStream(output);
/*     */     }
/*     */   }
/*     */   
/*     */   public Path copy() {
/* 212 */     Path result = new Path(this.nodes, this.target, this.reached);
/* 213 */     result.debugData = this.debugData;
/* 214 */     result.nextNodeIndex = this.nextNodeIndex;
/* 215 */     return result;
/*     */   }
/*     */   public static final class DebugData extends Record { private final Node[] openSet; private final Node[] closedSet; private final Set<Target> targetNodes;
/* 218 */     public DebugData(Node[] openSet, Node[] closedSet, Set<Target> targetNodes) { this.openSet = openSet; this.closedSet = closedSet; this.targetNodes = targetNodes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 218 */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData; } public Node[] openSet() { return this.openSet; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/* 218 */       //   0	8	1	o	Ljava/lang/Object; } public Node[] closedSet() { return this.closedSet; } public Set<Target> targetNodes() { return this.targetNodes; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 225 */       output.writeCollection(this.targetNodes, (out, target) -> target.writeToStream(out));
/* 226 */       Path.writeNodeArray(output, this.openSet);
/* 227 */       Path.writeNodeArray(output, this.closedSet);
/*     */     }
/*     */     
/*     */     public static DebugData read(FriendlyByteBuf input) {
/* 231 */       HashSet<Target> targets = (HashSet<Target>)input.readCollection(HashSet::new, Target::createFromStream);
/* 232 */       Node[] openSet = Path.readNodeArray(input);
/* 233 */       Node[] closedSet = Path.readNodeArray(input);
/* 234 */       return new DebugData(openSet, closedSet, targets);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/pathfinder/Path.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */