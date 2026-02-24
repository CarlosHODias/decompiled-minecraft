/*   */ package net.minecraft.client.gui.screens.worldselection;
/*   */ 
/*   */ public final class InitialWorldCreationOptions extends Record {
/*   */   private final WorldCreationUiState.SelectedGameMode selectedGameMode;
/*   */   private final net.minecraft.world.level.gamerules.GameRuleMap gameRuleOverwrites;
/*   */   private final net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset> flatLevelPreset;
/*   */   
/* 8 */   public InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode selectedGameMode, net.minecraft.world.level.gamerules.GameRuleMap gameRuleOverwrites, net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset> flatLevelPreset) { this.selectedGameMode = selectedGameMode; this.gameRuleOverwrites = gameRuleOverwrites; this.flatLevelPreset = flatLevelPreset; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions; } public WorldCreationUiState.SelectedGameMode selectedGameMode() { return this.selectedGameMode; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.gamerules.GameRuleMap gameRuleOverwrites() { return this.gameRuleOverwrites; } public net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset> flatLevelPreset() { return this.flatLevelPreset; }
/*   */ 
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/InitialWorldCreationOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */