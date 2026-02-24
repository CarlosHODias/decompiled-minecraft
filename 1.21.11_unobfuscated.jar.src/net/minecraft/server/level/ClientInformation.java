/*    */ package net.minecraft.server.level;public final class ClientInformation extends Record { private final String language; private final int viewDistance; private final net.minecraft.world.entity.player.ChatVisiblity chatVisibility; private final boolean chatColors;
/*    */   private final int modelCustomisation;
/*    */   private final net.minecraft.world.entity.HumanoidArm mainHand;
/*    */   private final boolean textFilteringEnabled;
/*    */   private final boolean allowsListing;
/*    */   private final ParticleStatus particleStatus;
/*    */   public static final int MAX_LANGUAGE_LENGTH = 16;
/*    */   
/*  9 */   public ClientInformation(String language, int viewDistance, net.minecraft.world.entity.player.ChatVisiblity chatVisibility, boolean chatColors, int modelCustomisation, net.minecraft.world.entity.HumanoidArm mainHand, boolean textFilteringEnabled, boolean allowsListing, ParticleStatus particleStatus) { this.language = language; this.viewDistance = viewDistance; this.chatVisibility = chatVisibility; this.chatColors = chatColors; this.modelCustomisation = modelCustomisation; this.mainHand = mainHand; this.textFilteringEnabled = textFilteringEnabled; this.allowsListing = allowsListing; this.particleStatus = particleStatus; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ClientInformation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation; } public String language() { return this.language; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ClientInformation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ClientInformation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ClientInformation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ClientInformation;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int viewDistance() { return this.viewDistance; } public net.minecraft.world.entity.player.ChatVisiblity chatVisibility() { return this.chatVisibility; } public boolean chatColors() { return this.chatColors; } public int modelCustomisation() { return this.modelCustomisation; } public net.minecraft.world.entity.HumanoidArm mainHand() { return this.mainHand; } public boolean textFilteringEnabled() { return this.textFilteringEnabled; } public boolean allowsListing() { return this.allowsListing; } public ParticleStatus particleStatus() { return this.particleStatus; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientInformation(net.minecraft.network.FriendlyByteBuf input) {
/* 24 */     this(
/* 25 */         input.readUtf(16), 
/* 26 */         input.readByte(), (net.minecraft.world.entity.player.ChatVisiblity)
/* 27 */         input.readEnum(net.minecraft.world.entity.player.ChatVisiblity.class), 
/* 28 */         input.readBoolean(), 
/* 29 */         input.readUnsignedByte(), (net.minecraft.world.entity.HumanoidArm)
/* 30 */         input.readEnum(net.minecraft.world.entity.HumanoidArm.class), 
/* 31 */         input.readBoolean(), 
/* 32 */         input.readBoolean(), (ParticleStatus)
/* 33 */         input.readEnum(ParticleStatus.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(net.minecraft.network.FriendlyByteBuf output) {
/* 38 */     output.writeUtf(this.language);
/* 39 */     output.writeByte(this.viewDistance);
/* 40 */     output.writeEnum((Enum)this.chatVisibility);
/* 41 */     output.writeBoolean(this.chatColors);
/* 42 */     output.writeByte(this.modelCustomisation);
/* 43 */     output.writeEnum((Enum)this.mainHand);
/* 44 */     output.writeBoolean(this.textFilteringEnabled);
/* 45 */     output.writeBoolean(this.allowsListing);
/* 46 */     output.writeEnum(this.particleStatus);
/*    */   }
/*    */   
/*    */   public static ClientInformation createDefault() {
/* 50 */     return new ClientInformation("en_us", 2, net.minecraft.world.entity.player.ChatVisiblity.FULL, true, 0, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false, ParticleStatus.ALL);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ClientInformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */