/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   public final int x;
/*     */   public final int y;
/*     */   public final int z;
/*     */   private final int hash;
/*  15 */   public int heapIdx = -1;
/*     */   
/*     */   public float g;
/*     */   
/*     */   public float h;
/*     */   public float f;
/*     */   public Node cameFrom;
/*     */   public boolean closed;
/*     */   public float walkedDistance;
/*     */   public float costMalus;
/*  25 */   public PathType type = PathType.BLOCKED;
/*     */   
/*     */   public Node(int x, int y, int z) {
/*  28 */     this.x = x;
/*  29 */     this.y = y;
/*  30 */     this.z = z;
/*     */     
/*  32 */     this.hash = createHash(x, y, z);
/*     */   }
/*     */   
/*     */   public Node cloneAndMove(int x, int y, int z) {
/*  36 */     Node node = new Node(x, y, z);
/*  37 */     node.heapIdx = this.heapIdx;
/*  38 */     node.g = this.g;
/*  39 */     node.h = this.h;
/*  40 */     node.f = this.f;
/*  41 */     node.cameFrom = this.cameFrom;
/*  42 */     node.closed = this.closed;
/*  43 */     node.walkedDistance = this.walkedDistance;
/*  44 */     node.costMalus = this.costMalus;
/*  45 */     node.type = this.type;
/*  46 */     return node;
/*     */   }
/*     */   
/*     */   public static int createHash(int x, int y, int z) {
/*  50 */     return y & 0xFF | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | ((x < 0) ? Integer.MIN_VALUE : 0) | ((z < 0) ? 32768 : 0);
/*     */   }
/*     */   
/*     */   public float distanceTo(Node to) {
/*  54 */     float xd = (to.x - this.x);
/*  55 */     float yd = (to.y - this.y);
/*  56 */     float zd = (to.z - this.z);
/*  57 */     return Mth.sqrt(xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */   
/*     */   public float distanceToXZ(Node to) {
/*  61 */     float xd = (to.x - this.x);
/*  62 */     float zd = (to.z - this.z);
/*  63 */     return Mth.sqrt(xd * xd + zd * zd);
/*     */   }
/*     */   
/*     */   public float distanceTo(BlockPos pos) {
/*  67 */     float xd = (pos.getX() - this.x);
/*  68 */     float yd = (pos.getY() - this.y);
/*  69 */     float zd = (pos.getZ() - this.z);
/*  70 */     return Mth.sqrt(xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */   
/*     */   public float distanceToSqr(Node to) {
/*  74 */     float xd = (to.x - this.x);
/*  75 */     float yd = (to.y - this.y);
/*  76 */     float zd = (to.z - this.z);
/*  77 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public float distanceToSqr(BlockPos pos) {
/*  81 */     float xd = (pos.getX() - this.x);
/*  82 */     float yd = (pos.getY() - this.y);
/*  83 */     float zd = (pos.getZ() - this.z);
/*  84 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public float distanceManhattan(Node to) {
/*  88 */     float xd = Math.abs(to.x - this.x);
/*  89 */     float yd = Math.abs(to.y - this.y);
/*  90 */     float zd = Math.abs(to.z - this.z);
/*  91 */     return xd + yd + zd;
/*     */   }
/*     */   
/*     */   public float distanceManhattan(BlockPos pos) {
/*  95 */     float xd = Math.abs(pos.getX() - this.x);
/*  96 */     float yd = Math.abs(pos.getY() - this.y);
/*  97 */     float zd = Math.abs(pos.getZ() - this.z);
/*  98 */     return xd + yd + zd;
/*     */   }
/*     */   
/*     */   public BlockPos asBlockPos() {
/* 102 */     return new BlockPos(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public Vec3 asVec3() {
/* 106 */     return new Vec3(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 111 */     if (o instanceof Node) { Node no = (Node)o;
/* 112 */       return (this.hash == no.hash && this.x == no.x && this.y == no.y && this.z == no.z); }
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.hash;
/*     */   }
/*     */   
/*     */   public boolean inOpenSet() {
/* 123 */     return (this.heapIdx >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "Node{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
/*     */   }
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf buffer) {
/* 132 */     buffer.writeInt(this.x);
/* 133 */     buffer.writeInt(this.y);
/* 134 */     buffer.writeInt(this.z);
/* 135 */     buffer.writeFloat(this.walkedDistance);
/* 136 */     buffer.writeFloat(this.costMalus);
/* 137 */     buffer.writeBoolean(this.closed);
/* 138 */     buffer.writeEnum(this.type);
/* 139 */     buffer.writeFloat(this.f);
/*     */   }
/*     */   
/*     */   public static Node createFromStream(FriendlyByteBuf buffer) {
/* 143 */     Node node = new Node(buffer.readInt(), buffer.readInt(), buffer.readInt());
/* 144 */     readContents(buffer, node);
/* 145 */     return node;
/*     */   }
/*     */   
/*     */   protected static void readContents(FriendlyByteBuf buffer, Node node) {
/* 149 */     node.walkedDistance = buffer.readFloat();
/* 150 */     node.costMalus = buffer.readFloat();
/* 151 */     node.closed = buffer.readBoolean();
/* 152 */     node.type = (PathType)buffer.readEnum(PathType.class);
/* 153 */     node.f = buffer.readFloat();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/pathfinder/Node.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */