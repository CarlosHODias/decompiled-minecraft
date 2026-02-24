/*     */ package net.minecraft.world.level.block.entity;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class JigsawBlockEntity extends BlockEntity {
/*  26 */   public static final Codec<ResourceKey<StructureTemplatePool>> POOL_CODEC = ResourceKey.codec(Registries.TEMPLATE_POOL); private static final int DEFAULT_PLACEMENT_PRIORITY = 0;
/*     */   private static final int DEFAULT_SELECTION_PRIORITY = 0;
/*  28 */   public static final Identifier EMPTY_ID = Identifier.withDefaultNamespace("empty"); public static final String TARGET = "target"; public static final String POOL = "pool"; public static final String JOINT = "joint"; public static final String PLACEMENT_PRIORITY = "placement_priority";
/*     */   public static final String SELECTION_PRIORITY = "selection_priority";
/*     */   public static final String NAME = "name";
/*     */   public static final String FINAL_STATE = "final_state";
/*     */   public static final String DEFAULT_FINAL_STATE = "minecraft:air";
/*     */   
/*  34 */   public enum JointType implements StringRepresentable { ROLLABLE("rollable"),
/*  35 */     ALIGNED("aligned");
/*     */     
/*  37 */     public static final StringRepresentable.EnumCodec<JointType> CODEC = StringRepresentable.fromEnum(JointType::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     JointType(String name) {
/*  42 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  47 */       return this.name;
/*     */     }
/*     */     
/*     */     public Component getTranslatedName() {
/*  51 */       return (Component)Component.translatable("jigsaw_block.joint." + this.name);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private Identifier name = EMPTY_ID;
/*  70 */   private Identifier target = EMPTY_ID;
/*  71 */   private ResourceKey<StructureTemplatePool> pool = Pools.EMPTY;
/*  72 */   private JointType joint = JointType.ROLLABLE;
/*  73 */   private String finalState = "minecraft:air";
/*  74 */   private int placementPriority = 0;
/*  75 */   private int selectionPriority = 0;
/*     */   
/*     */   public JigsawBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  78 */     super(BlockEntityType.JIGSAW, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public Identifier getName() {
/*  82 */     return this.name;
/*     */   }
/*     */   
/*     */   public Identifier getTarget() {
/*  86 */     return this.target;
/*     */   }
/*     */   
/*     */   public ResourceKey<StructureTemplatePool> getPool() {
/*  90 */     return this.pool;
/*     */   }
/*     */   
/*     */   public String getFinalState() {
/*  94 */     return this.finalState;
/*     */   }
/*     */   
/*     */   public JointType getJoint() {
/*  98 */     return this.joint;
/*     */   }
/*     */   
/*     */   public int getPlacementPriority() {
/* 102 */     return this.placementPriority;
/*     */   }
/*     */   
/*     */   public int getSelectionPriority() {
/* 106 */     return this.selectionPriority;
/*     */   }
/*     */   
/*     */   public void setName(Identifier name) {
/* 110 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void setTarget(Identifier target) {
/* 114 */     this.target = target;
/*     */   }
/*     */   
/*     */   public void setPool(ResourceKey<StructureTemplatePool> pool) {
/* 118 */     this.pool = pool;
/*     */   }
/*     */   
/*     */   public void setFinalState(String finalState) {
/* 122 */     this.finalState = finalState;
/*     */   }
/*     */   
/*     */   public void setJoint(JointType joint) {
/* 126 */     this.joint = joint;
/*     */   }
/*     */   
/*     */   public void setPlacementPriority(int placementPriority) {
/* 130 */     this.placementPriority = placementPriority;
/*     */   }
/*     */   
/*     */   public void setSelectionPriority(int selectionPriority) {
/* 134 */     this.selectionPriority = selectionPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 139 */     super.saveAdditional(output);
/* 140 */     output.store("name", Identifier.CODEC, this.name);
/* 141 */     output.store("target", Identifier.CODEC, this.target);
/* 142 */     output.store("pool", POOL_CODEC, this.pool);
/* 143 */     output.putString("final_state", this.finalState);
/* 144 */     output.store("joint", (Codec)JointType.CODEC, this.joint);
/* 145 */     output.putInt("placement_priority", this.placementPriority);
/* 146 */     output.putInt("selection_priority", this.selectionPriority);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 151 */     super.loadAdditional(input);
/* 152 */     this.name = input.read("name", Identifier.CODEC).orElse(EMPTY_ID);
/* 153 */     this.target = input.read("target", Identifier.CODEC).orElse(EMPTY_ID);
/* 154 */     this.pool = input.read("pool", POOL_CODEC).orElse(Pools.EMPTY);
/* 155 */     this.finalState = input.getStringOr("final_state", "minecraft:air");
/* 156 */     this.joint = input.read("joint", (Codec)JointType.CODEC).orElseGet(() -> StructureTemplate.getDefaultJointType(getBlockState()));
/* 157 */     this.placementPriority = input.getIntOr("placement_priority", 0);
/* 158 */     this.selectionPriority = input.getIntOr("selection_priority", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 163 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 168 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public void generate(ServerLevel level, int levels, boolean keepJigsaws) {
/* 172 */     BlockPos position = getBlockPos().relative(((net.minecraft.core.FrontAndTop)getBlockState().getValue((Property)JigsawBlock.ORIENTATION)).front());
/*     */     
/* 174 */     Registry<StructureTemplatePool> poolRegistry = level.registryAccess().lookupOrThrow(Registries.TEMPLATE_POOL);
/* 175 */     Holder.Reference reference = poolRegistry.getOrThrow(this.pool);
/*     */     
/* 177 */     net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement.generateJigsaw(level, (Holder)reference, this.target, levels, position, keepJigsaws);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/JigsawBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */