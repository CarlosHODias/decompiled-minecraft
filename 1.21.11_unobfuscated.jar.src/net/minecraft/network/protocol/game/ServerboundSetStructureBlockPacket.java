/*     */ package net.minecraft.network.protocol.game;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ 
/*     */ public class ServerboundSetStructureBlockPacket implements Packet<ServerGamePacketListener> {
/*  16 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetStructureBlockPacket> STREAM_CODEC = Packet.codec(ServerboundSetStructureBlockPacket::write, ServerboundSetStructureBlockPacket::new);
/*     */   
/*     */   private static final int FLAG_IGNORE_ENTITIES = 1;
/*     */   
/*     */   private static final int FLAG_SHOW_AIR = 2;
/*     */   private static final int FLAG_SHOW_BOUNDING_BOX = 4;
/*     */   private static final int FLAG_STRICT = 8;
/*     */   private final BlockPos pos;
/*     */   private final StructureBlockEntity.UpdateType updateType;
/*     */   private final StructureMode mode;
/*     */   private final String name;
/*     */   private final BlockPos offset;
/*     */   private final Vec3i size;
/*     */   private final Mirror mirror;
/*     */   private final Rotation rotation;
/*     */   private final String data;
/*     */   private final boolean ignoreEntities;
/*     */   private final boolean strict;
/*     */   private final boolean showAir;
/*     */   private final boolean showBoundingBox;
/*     */   private final float integrity;
/*     */   private final long seed;
/*     */   
/*     */   public ServerboundSetStructureBlockPacket(BlockPos pos, StructureBlockEntity.UpdateType updateType, StructureMode mode, String name, BlockPos offset, Vec3i size, Mirror mirror, Rotation rotation, String data, boolean ignoreEntities, boolean strict, boolean showAir, boolean showBoundingBox, float integrity, long seed) {
/*  40 */     this.pos = pos;
/*  41 */     this.updateType = updateType;
/*  42 */     this.mode = mode;
/*  43 */     this.name = name;
/*  44 */     this.offset = offset;
/*  45 */     this.size = size;
/*  46 */     this.mirror = mirror;
/*  47 */     this.rotation = rotation;
/*  48 */     this.data = data;
/*  49 */     this.ignoreEntities = ignoreEntities;
/*  50 */     this.strict = strict;
/*  51 */     this.showAir = showAir;
/*  52 */     this.showBoundingBox = showBoundingBox;
/*  53 */     this.integrity = integrity;
/*  54 */     this.seed = seed;
/*     */   }
/*     */   
/*     */   private ServerboundSetStructureBlockPacket(FriendlyByteBuf input) {
/*  58 */     this.pos = input.readBlockPos();
/*  59 */     this.updateType = (StructureBlockEntity.UpdateType)input.readEnum(StructureBlockEntity.UpdateType.class);
/*  60 */     this.mode = (StructureMode)input.readEnum(StructureMode.class);
/*  61 */     this.name = input.readUtf();
/*  62 */     int maxOffset = 48;
/*  63 */     this.offset = new BlockPos(Mth.clamp(input.readByte(), -48, 48), Mth.clamp(input.readByte(), -48, 48), Mth.clamp(input.readByte(), -48, 48));
/*  64 */     int maxSize = 48;
/*  65 */     this.size = new Vec3i(Mth.clamp(input.readByte(), 0, 48), Mth.clamp(input.readByte(), 0, 48), Mth.clamp(input.readByte(), 0, 48));
/*  66 */     this.mirror = (Mirror)input.readEnum(Mirror.class);
/*  67 */     this.rotation = (Rotation)input.readEnum(Rotation.class);
/*  68 */     this.data = input.readUtf(128);
/*  69 */     this.integrity = Mth.clamp(input.readFloat(), 0.0F, 1.0F);
/*  70 */     this.seed = input.readVarLong();
/*  71 */     int flags = input.readByte();
/*  72 */     this.ignoreEntities = ((flags & 0x1) != 0);
/*  73 */     this.strict = ((flags & 0x8) != 0);
/*  74 */     this.showAir = ((flags & 0x2) != 0);
/*  75 */     this.showBoundingBox = ((flags & 0x4) != 0);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/*  79 */     output.writeBlockPos(this.pos);
/*  80 */     output.writeEnum((Enum)this.updateType);
/*  81 */     output.writeEnum((Enum)this.mode);
/*  82 */     output.writeUtf(this.name);
/*  83 */     output.writeByte(this.offset.getX());
/*  84 */     output.writeByte(this.offset.getY());
/*  85 */     output.writeByte(this.offset.getZ());
/*  86 */     output.writeByte(this.size.getX());
/*  87 */     output.writeByte(this.size.getY());
/*  88 */     output.writeByte(this.size.getZ());
/*  89 */     output.writeEnum((Enum)this.mirror);
/*  90 */     output.writeEnum((Enum)this.rotation);
/*  91 */     output.writeUtf(this.data);
/*  92 */     output.writeFloat(this.integrity);
/*  93 */     output.writeVarLong(this.seed);
/*     */     
/*  95 */     int flags = 0;
/*  96 */     if (this.ignoreEntities) {
/*  97 */       flags |= 0x1;
/*     */     }
/*  99 */     if (this.showAir) {
/* 100 */       flags |= 0x2;
/*     */     }
/* 102 */     if (this.showBoundingBox) {
/* 103 */       flags |= 0x4;
/*     */     }
/* 105 */     if (this.strict) {
/* 106 */       flags |= 0x8;
/*     */     }
/* 108 */     output.writeByte(flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ServerboundSetStructureBlockPacket> type() {
/* 113 */     return GamePacketTypes.SERVERBOUND_SET_STRUCTURE_BLOCK;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ServerGamePacketListener listener) {
/* 118 */     listener.handleSetStructureBlock(this);
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 122 */     return this.pos;
/*     */   }
/*     */   
/*     */   public StructureBlockEntity.UpdateType getUpdateType() {
/* 126 */     return this.updateType;
/*     */   }
/*     */   
/*     */   public StructureMode getMode() {
/* 130 */     return this.mode;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 134 */     return this.name;
/*     */   }
/*     */   
/*     */   public BlockPos getOffset() {
/* 138 */     return this.offset;
/*     */   }
/*     */   
/*     */   public Vec3i getSize() {
/* 142 */     return this.size;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 146 */     return this.mirror;
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 150 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public String getData() {
/* 154 */     return this.data;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreEntities() {
/* 158 */     return this.ignoreEntities;
/*     */   }
/*     */   
/*     */   public boolean isStrict() {
/* 162 */     return this.strict;
/*     */   }
/*     */   
/*     */   public boolean isShowAir() {
/* 166 */     return this.showAir;
/*     */   }
/*     */   
/*     */   public boolean isShowBoundingBox() {
/* 170 */     return this.showBoundingBox;
/*     */   }
/*     */   
/*     */   public float getIntegrity() {
/* 174 */     return this.integrity;
/*     */   }
/*     */   
/*     */   public long getSeed() {
/* 178 */     return this.seed;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSetStructureBlockPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */