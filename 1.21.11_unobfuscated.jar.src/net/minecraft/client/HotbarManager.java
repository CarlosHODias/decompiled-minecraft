/*    */ package net.minecraft.client;
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.player.inventory.Hotbar;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class HotbarManager {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final int NUM_HOTBAR_GROUPS = 9;
/*    */   private final Path optionsFile;
/*    */   private final DataFixer fixerUpper;
/* 23 */   private final Hotbar[] hotbars = new Hotbar[9];
/*    */   private boolean loaded;
/*    */   
/*    */   public HotbarManager(Path workingDirectory, DataFixer fixerUpper) {
/* 27 */     this.optionsFile = workingDirectory.resolve("hotbar.nbt");
/* 28 */     this.fixerUpper = fixerUpper;
/*    */     
/* 30 */     for (int i = 0; i < 9; i++) {
/* 31 */       this.hotbars[i] = new Hotbar();
/*    */     }
/*    */   }
/*    */   
/*    */   private void load() {
/*    */     try {
/* 37 */       CompoundTag tag = NbtIo.read(this.optionsFile);
/* 38 */       if (tag == null) {
/*    */         return;
/*    */       }
/*    */ 
/*    */       
/* 43 */       int version = NbtUtils.getDataVersion(tag, 1343);
/* 44 */       tag = DataFixTypes.HOTBAR.updateToCurrentVersion(this.fixerUpper, tag, version);
/*    */       
/* 46 */       for (int i = 0; i < 9; i++) {
/* 47 */         this.hotbars[i] = Hotbar.CODEC.parse((DynamicOps)NbtOps.INSTANCE, tag.get(String.valueOf(i)))
/* 48 */           .resultOrPartial(error -> LOGGER.warn("Failed to parse hotbar: {}", error))
/* 49 */           .orElseGet(Hotbar::new);
/*    */       }
/* 51 */     } catch (Exception e) {
/* 52 */       LOGGER.error("Failed to load creative mode options", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void save() {
/*    */     try {
/* 58 */       CompoundTag tag = NbtUtils.addCurrentDataVersion(new CompoundTag());
/* 59 */       for (int i = 0; i < 9; i++) {
/* 60 */         Hotbar hotbar = get(i);
/* 61 */         DataResult<Tag> result = Hotbar.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, hotbar);
/* 62 */         tag.put(String.valueOf(i), (Tag)result.getOrThrow());
/*    */       } 
/* 64 */       NbtIo.write(tag, this.optionsFile);
/* 65 */     } catch (Exception e) {
/* 66 */       LOGGER.error("Failed to save creative mode options", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Hotbar get(int id) {
/* 71 */     if (!this.loaded) {
/* 72 */       load();
/* 73 */       this.loaded = true;
/*    */     } 
/* 75 */     return this.hotbars[id];
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/HotbarManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */