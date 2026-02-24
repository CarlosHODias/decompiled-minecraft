/*    */ package net.minecraft.client.data;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import joptsimple.AbstractOptionSpec;
/*    */ import joptsimple.ArgumentAcceptingOptionSpec;
/*    */ import joptsimple.OptionParser;
/*    */ import joptsimple.OptionSet;
/*    */ import joptsimple.OptionSpec;
/*    */ import joptsimple.OptionSpecBuilder;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.SuppressForbidden;
/*    */ import net.minecraft.client.ClientBootstrap;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Main {
/*    */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*    */   public static void main(String[] args) throws IOException {
/* 23 */     SharedConstants.tryDetectVersion();
/*    */     
/* 25 */     OptionParser parser = new OptionParser();
/* 26 */     AbstractOptionSpec abstractOptionSpec = parser.accepts("help", "Show the help menu").forHelp();
/* 27 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("client", "Include client generators");
/* 28 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("all", "Include all generators");
/* 29 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec = parser.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", (Object[])new String[0]);
/* 30 */     OptionSet optionSet = parser.parse(args);
/*    */     
/* 32 */     if (optionSet.has((OptionSpec)abstractOptionSpec) || !optionSet.hasOptions()) {
/* 33 */       parser.printHelpOn(System.out);
/*    */       
/*    */       return;
/*    */     } 
/* 37 */     Path output = Paths.get((String)argumentAcceptingOptionSpec.value(optionSet), new String[0]);
/* 38 */     boolean allOptions = optionSet.has((OptionSpec)optionSpecBuilder2);
/* 39 */     boolean client = (allOptions || optionSet.has((OptionSpec)optionSpecBuilder1));
/*    */     
/* 41 */     Bootstrap.bootStrap();
/* 42 */     ClientBootstrap.bootstrap();
/*    */     
/* 44 */     DataGenerator generator = new DataGenerator(output, SharedConstants.getCurrentVersion(), true);
/* 45 */     addClientProviders(generator, client);
/* 46 */     generator.run();
/* 47 */     Util.shutdownExecutors();
/*    */   }
/*    */   
/*    */   public static void addClientProviders(DataGenerator generator, boolean client) {
/* 51 */     DataGenerator.PackGenerator clientVanillaPack = generator.getVanillaPack(client);
/* 52 */     clientVanillaPack.addProvider(net.minecraft.client.data.models.ModelProvider::new);
/* 53 */     clientVanillaPack.addProvider(net.minecraft.client.data.models.EquipmentAssetProvider::new);
/* 54 */     clientVanillaPack.addProvider(net.minecraft.client.data.models.WaypointStyleProvider::new);
/* 55 */     clientVanillaPack.addProvider(AtlasProvider::new);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */