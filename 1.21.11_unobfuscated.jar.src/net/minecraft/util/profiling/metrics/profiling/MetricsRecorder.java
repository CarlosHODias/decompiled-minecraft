package net.minecraft.util.profiling.metrics.profiling;

import net.minecraft.util.profiling.ProfilerFiller;

public interface MetricsRecorder {
  void end();
  
  void cancel();
  
  void startTick();
  
  boolean isRecording();
  
  ProfilerFiller getProfiler();
  
  void endTick();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/metrics/profiling/MetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */