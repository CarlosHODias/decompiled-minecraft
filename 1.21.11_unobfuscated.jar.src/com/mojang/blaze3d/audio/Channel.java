/*     */ package com.mojang.blaze3d.audio;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import net.minecraft.client.sounds.AudioStream;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.lwjgl.openal.AL10;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Channel
/*     */ {
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int QUEUED_BUFFER_COUNT = 4;
/*     */   public static final int BUFFER_DURATION_SECONDS = 1;
/*     */   private final int source;
/*  22 */   private final AtomicBoolean initialized = new AtomicBoolean(true);
/*     */   
/*  24 */   private int streamingBufferSize = 16384;
/*     */   
/*     */   private AudioStream stream;
/*     */   
/*     */   static Channel create() {
/*  29 */     int[] newId = new int[1];
/*     */     
/*  31 */     AL10.alGenSources(newId);
/*  32 */     if (OpenAlUtil.checkALError("Allocate new source")) {
/*  33 */       return null;
/*     */     }
/*  35 */     return new Channel(newId[0]);
/*     */   }
/*     */   
/*     */   private Channel(int src) {
/*  39 */     this.source = src;
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  43 */     if (this.initialized.compareAndSet(true, false)) {
/*  44 */       AL10.alSourceStop(this.source);
/*  45 */       OpenAlUtil.checkALError("Stop");
/*  46 */       if (this.stream != null) {
/*     */         try {
/*  48 */           this.stream.close();
/*  49 */         } catch (IOException e) {
/*  50 */           LOGGER.error("Failed to close audio stream", e);
/*     */         } 
/*  52 */         removeProcessedBuffers();
/*  53 */         this.stream = null;
/*     */       } 
/*     */       
/*  56 */       AL10.alDeleteSources(new int[] { this.source });
/*  57 */       OpenAlUtil.checkALError("Cleanup");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void play() {
/*  62 */     AL10.alSourcePlay(this.source);
/*     */   }
/*     */   
/*     */   private int getState() {
/*  66 */     if (!this.initialized.get()) {
/*  67 */       return 4116;
/*     */     }
/*  69 */     return AL10.alGetSourcei(this.source, 4112);
/*     */   }
/*     */   
/*     */   public void pause() {
/*  73 */     if (getState() == 4114) {
/*  74 */       AL10.alSourcePause(this.source);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unpause() {
/*  79 */     if (getState() == 4115) {
/*  80 */       AL10.alSourcePlay(this.source);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop() {
/*  85 */     if (this.initialized.get()) {
/*  86 */       AL10.alSourceStop(this.source);
/*  87 */       OpenAlUtil.checkALError("Stop");
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean playing() {
/*  92 */     return (getState() == 4114);
/*     */   }
/*     */   
/*     */   public boolean stopped() {
/*  96 */     return (getState() == 4116);
/*     */   }
/*     */   
/*     */   public void setSelfPosition(Vec3 newPosition) {
/* 100 */     AL10.alSourcefv(this.source, 4100, new float[] { (float)newPosition.x, (float)newPosition.y, (float)newPosition.z });
/*     */   }
/*     */   
/*     */   public void setPitch(float pitch) {
/* 104 */     AL10.alSourcef(this.source, 4099, pitch);
/*     */   }
/*     */   
/*     */   public void setLooping(boolean looping) {
/* 108 */     AL10.alSourcei(this.source, 4103, looping ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void setVolume(float volume) {
/* 112 */     AL10.alSourcef(this.source, 4106, volume);
/*     */   }
/*     */   
/*     */   public void disableAttenuation() {
/* 116 */     AL10.alSourcei(this.source, 53248, 0);
/*     */   }
/*     */   
/*     */   public void linearAttenuation(float maxDistance) {
/* 120 */     AL10.alSourcei(this.source, 53248, 53251);
/* 121 */     AL10.alSourcef(this.source, 4131, maxDistance);
/* 122 */     AL10.alSourcef(this.source, 4129, 1.0F);
/* 123 */     AL10.alSourcef(this.source, 4128, 0.0F);
/*     */   }
/*     */   
/*     */   public void setRelative(boolean relative) {
/* 127 */     AL10.alSourcei(this.source, 514, relative ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void attachStaticBuffer(SoundBuffer buffer) {
/* 131 */     buffer.getAlBuffer().ifPresent(bufferId -> AL10.alSourcei(this.source, 4105, bufferId));
/*     */   }
/*     */   
/*     */   public void attachBufferStream(AudioStream stream) {
/* 135 */     this.stream = stream;
/* 136 */     AudioFormat format = stream.getFormat();
/* 137 */     this.streamingBufferSize = calculateBufferSize(format, 1);
/* 138 */     pumpBuffers(4);
/*     */   }
/*     */   
/*     */   private static int calculateBufferSize(AudioFormat format, int seconds) {
/* 142 */     return (int)((seconds * format.getSampleSizeInBits()) / 8.0F * format.getChannels() * format.getSampleRate());
/*     */   }
/*     */   
/*     */   private void pumpBuffers(int size) {
/* 146 */     if (this.stream != null) {
/*     */       try {
/* 148 */         for (int i = 0; i < size; i++) {
/* 149 */           ByteBuffer buffer = this.stream.read(this.streamingBufferSize);
/* 150 */           if (buffer != null)
/*     */           {
/* 152 */             new SoundBuffer(buffer, this.stream.getFormat()).releaseAlBuffer().ifPresent(bufferId -> AL10.alSourceQueueBuffers(this.source, new int[] { bufferId }));
/*     */           }
/*     */         } 
/* 155 */       } catch (IOException e) {
/* 156 */         LOGGER.error("Failed to read from audio stream", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateStream() {
/* 162 */     if (this.stream != null) {
/* 163 */       int processedBuffers = removeProcessedBuffers();
/* 164 */       pumpBuffers(processedBuffers);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int removeProcessedBuffers() {
/* 169 */     int processed = AL10.alGetSourcei(this.source, 4118);
/*     */     
/* 171 */     if (processed > 0) {
/* 172 */       int[] ids = new int[processed];
/* 173 */       AL10.alSourceUnqueueBuffers(this.source, ids);
/* 174 */       OpenAlUtil.checkALError("Unqueue buffers");
/* 175 */       AL10.alDeleteBuffers(ids);
/* 176 */       OpenAlUtil.checkALError("Remove processed buffers");
/*     */     } 
/*     */     
/* 179 */     return processed;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/Channel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */