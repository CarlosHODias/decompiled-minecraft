/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*    */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*    */ 
/*    */ public final class ClientboundUpdateRecipesPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final java.util.Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets;
/*    */   private final SelectableRecipe.SingleInputSet<net.minecraft.world.item.crafting.StonecutterRecipe> stonecutterRecipes;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/*    */   }
/*    */   
/* 19 */   public ClientboundUpdateRecipesPacket(java.util.Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets, SelectableRecipe.SingleInputSet<net.minecraft.world.item.crafting.StonecutterRecipe> stonecutterRecipes) { this.itemSets = itemSets; this.stonecutterRecipes = stonecutterRecipes; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets() { return this.itemSets; } public SelectableRecipe.SingleInputSet<net.minecraft.world.item.crafting.StonecutterRecipe> stonecutterRecipes() { return this.stonecutterRecipes; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClientboundUpdateRecipesPacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 24 */       net.minecraft.network.codec.ByteBufCodecs.map(java.util.HashMap::new, ResourceKey.streamCodec(RecipePropertySet.TYPE_KEY), RecipePropertySet.STREAM_CODEC), ClientboundUpdateRecipesPacket::itemSets, 
/* 25 */       SelectableRecipe.SingleInputSet.noRecipeCodec(), ClientboundUpdateRecipesPacket::stonecutterRecipes, ClientboundUpdateRecipesPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundUpdateRecipesPacket> type() {
/* 31 */     return GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 36 */     listener.handleUpdateRecipes(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */