package net.minecraft.gametest.framework;

public interface GameTestListener {
  void testStructureLoaded(GameTestInfo paramGameTestInfo);
  
  void testPassed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testFailed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testAddedForRerun(GameTestInfo paramGameTestInfo1, GameTestInfo paramGameTestInfo2, GameTestRunner paramGameTestRunner);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */