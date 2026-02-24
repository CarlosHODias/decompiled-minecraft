/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.util.ArrayListDeque;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class CommandHistory
/*    */ {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final int MAX_PERSISTED_COMMAND_HISTORY = 50;
/*    */   
/*    */   private static final String PERSISTED_COMMANDS_FILE_NAME = "command_history.txt";
/*    */   private final Path commandsPath;
/* 22 */   private final ArrayListDeque<String> lastCommands = new ArrayListDeque(50);
/*    */   
/*    */   public CommandHistory(Path gameFolder) {
/* 25 */     this.commandsPath = gameFolder.resolve("command_history.txt");
/* 26 */     if (Files.exists(this.commandsPath, new java.nio.file.LinkOption[0])) {
/* 27 */       try { BufferedReader reader = Files.newBufferedReader(this.commandsPath, StandardCharsets.UTF_8); 
/* 28 */         try { this.lastCommands.addAll(reader.lines().toList());
/* 29 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/* 30 */       { LOGGER.error("Failed to read {}, command history will be missing", "command_history.txt", exception); }
/*    */     
/*    */     }
/*    */   }
/*    */   
/*    */   public void addCommand(String command) {
/* 36 */     if (!command.equals(this.lastCommands.peekLast())) {
/* 37 */       if (this.lastCommands.size() >= 50) {
/* 38 */         this.lastCommands.removeFirst();
/*    */       }
/* 40 */       this.lastCommands.addLast(command);
/* 41 */       save();
/*    */     } 
/*    */   }
/*    */   private void save() {
/*    */     
/* 46 */     try { BufferedWriter writer = Files.newBufferedWriter(this.commandsPath, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/* 47 */       try { for (String command : this.lastCommands) {
/* 48 */           writer.write(command);
/* 49 */           writer.newLine();
/*    */         } 
/* 51 */         if (writer != null) writer.close();  } catch (Throwable throwable) { if (writer != null) try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException exception)
/* 52 */     { LOGGER.error("Failed to write {}, command history will be missing", "command_history.txt", exception); }
/*    */   
/*    */   }
/*    */   
/*    */   public Collection<String> history() {
/* 57 */     return (Collection<String>)this.lastCommands;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/CommandHistory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */