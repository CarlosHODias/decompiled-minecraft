/*     */ package com.mojang.blaze3d.preprocessor;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GlslPreprocessor
/*     */ {
/*     */   private static final String C_COMMENT = "/\\*(?:[^*]|\\*+[^*/])*\\*+/";
/*     */   private static final String LINE_COMMENT = "//[^\\v]*";
/*  19 */   private static final Pattern REGEX_MOJ_IMPORT = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*moj_import(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(?:\"(.*)\"|<(.*)>))");
/*  20 */   private static final Pattern REGEX_VERSION = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*version(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(\\d+))\\b");
/*  21 */   private static final Pattern REGEX_ENDS_WITH_WHITESPACE = Pattern.compile("(?:^|\\v)(?:\\s|/\\*(?:[^*]|\\*+[^*/])*\\*+/|(//[^\\v]*))*\\z");
/*     */   
/*     */   public List<String> process(String source) {
/*  24 */     Context context = new Context();
/*  25 */     List<String> sourceList = processImports(source, context, "");
/*     */     
/*  27 */     sourceList.set(0, setVersion(sourceList.get(0), context.glslVersion));
/*  28 */     return sourceList;
/*     */   }
/*     */   
/*     */   private List<String> processImports(String source, Context context, String parentPath) {
/*  32 */     int thisSourceId = context.sourceId;
/*     */     
/*  34 */     int previousMatchEnd = 0;
/*     */     
/*  36 */     String lineMacro = "";
/*  37 */     List<String> sourceList = Lists.newArrayList();
/*  38 */     Matcher matcher = REGEX_MOJ_IMPORT.matcher(source);
/*  39 */     while (matcher.find()) {
/*  40 */       if (isDirectiveDisabled(source, matcher, previousMatchEnd)) {
/*     */         continue;
/*     */       }
/*     */       
/*  44 */       String path = matcher.group(2);
/*  45 */       boolean isRelative = (path != null);
/*  46 */       if (!isRelative) {
/*  47 */         path = matcher.group(3);
/*     */       }
/*     */       
/*  50 */       if (path == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  54 */       String sourceBeforeImport = source.substring(previousMatchEnd, matcher.start(1));
/*     */       
/*  56 */       String importPath = parentPath + parentPath;
/*  57 */       String contents = applyImport(isRelative, importPath);
/*  58 */       if (!Strings.isNullOrEmpty(contents)) {
/*  59 */         if (!StringUtil.endsWithNewLine(contents)) {
/*  60 */           contents = contents + contents;
/*     */         }
/*     */         
/*  63 */         int importSourceId = ++context.sourceId;
/*     */         
/*  65 */         List<String> importedSources = processImports(contents, context, isRelative ? FileUtil.getFullResourcePath(importPath) : "");
/*     */ 
/*     */         
/*  68 */         importedSources.set(0, String.format(Locale.ROOT, "#line %d %d\n%s", new Object[] { 0, importSourceId, processVersions(importedSources.get(0), context) }));
/*     */         
/*  70 */         if (!StringUtil.isBlank(sourceBeforeImport)) {
/*  71 */           sourceList.add(sourceBeforeImport);
/*     */         }
/*  73 */         sourceList.addAll(importedSources);
/*     */       } else {
/*     */         
/*  76 */         String disabledImport = isRelative ? String.format(Locale.ROOT, "/*#moj_import \"%s\"*/", new Object[] { path }) : String.format(Locale.ROOT, "/*#moj_import <%s>*/", new Object[] { path });
/*  77 */         sourceList.add(lineMacro + lineMacro + sourceBeforeImport);
/*     */       } 
/*     */ 
/*     */       
/*  81 */       int lineCount = StringUtil.lineCount(source.substring(0, matcher.end(1)));
/*  82 */       lineMacro = String.format(Locale.ROOT, "#line %d %d", new Object[] { lineCount, thisSourceId });
/*     */       
/*  84 */       previousMatchEnd = matcher.end(1);
/*     */     } 
/*     */     
/*  87 */     String remaining = source.substring(previousMatchEnd);
/*  88 */     if (!StringUtil.isBlank(remaining)) {
/*  89 */       sourceList.add(lineMacro + lineMacro);
/*     */     }
/*  91 */     return sourceList;
/*     */   }
/*     */   
/*     */   private String processVersions(String source, Context context) {
/*  95 */     Matcher matcher = REGEX_VERSION.matcher(source);
/*  96 */     if (matcher.find() && isDirectiveEnabled(source, matcher)) {
/*  97 */       context.glslVersion = Math.max(context.glslVersion, Integer.parseInt(matcher.group(2)));
/*     */       
/*  99 */       return source.substring(0, matcher.start(1)) + "/*" + source.substring(0, matcher.start(1)) + "*/" + 
/*     */         
/* 101 */         source.substring(matcher.start(1), matcher.end(1));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 106 */     return source;
/*     */   }
/*     */   
/*     */   private String setVersion(String source, int version) {
/* 110 */     Matcher matcher = REGEX_VERSION.matcher(source);
/* 111 */     if (matcher.find() && isDirectiveEnabled(source, matcher)) {
/* 112 */       return source.substring(0, matcher.start(2)) + source.substring(0, matcher.start(2)) + 
/* 113 */         Math.max(version, Integer.parseInt(matcher.group(2)));
/*     */     }
/*     */     
/* 116 */     return source;
/*     */   }
/*     */   
/*     */   private static boolean isDirectiveEnabled(String source, Matcher matcher) {
/* 120 */     return !isDirectiveDisabled(source, matcher, 0);
/*     */   }
/*     */   
/*     */   private static boolean isDirectiveDisabled(String source, Matcher matcher, int start) {
/* 124 */     int checkLength = matcher.start() - start;
/* 125 */     if (checkLength == 0) {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     Matcher preceedingWhiteSpace = REGEX_ENDS_WITH_WHITESPACE.matcher(source.substring(start, matcher.start()));
/* 130 */     if (!preceedingWhiteSpace.find()) {
/* 131 */       return true;
/*     */     }
/*     */     
/* 134 */     int lineCommentEnd = preceedingWhiteSpace.end(1);
/* 135 */     return (lineCommentEnd == matcher.start());
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract String applyImport(boolean paramBoolean, String paramString);
/*     */   
/*     */   public static String injectDefines(String source, ShaderDefines defines) {
/* 142 */     if (defines.isEmpty()) {
/* 143 */       return source;
/*     */     }
/*     */     
/* 146 */     int versionLineEnd = source.indexOf('\n');
/* 147 */     int injectIndex = versionLineEnd + 1;
/* 148 */     return source.substring(0, injectIndex) + source.substring(0, injectIndex) + "#line 1 0\n" + 
/* 149 */       defines.asSourceDirectives();
/*     */   }
/*     */   
/*     */   private static final class Context {
/*     */     private int glslVersion;
/*     */     private int sourceId;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/preprocessor/GlslPreprocessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */