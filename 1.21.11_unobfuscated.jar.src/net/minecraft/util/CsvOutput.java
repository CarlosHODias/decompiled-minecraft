/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ 
/*    */ public class CsvOutput
/*    */ {
/*    */   private static final String LINE_SEPARATOR = "\r\n";
/*    */   private static final String FIELD_SEPARATOR = ",";
/*    */   private final Writer output;
/*    */   private final int columnCount;
/*    */   
/*    */   private CsvOutput(Writer output, List<String> headers) throws IOException {
/* 20 */     this.output = output;
/* 21 */     this.columnCount = headers.size();
/* 22 */     writeLine(headers.stream());
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 26 */     return new Builder();
/*    */   }
/*    */   
/*    */   public void writeRow(Object... values) throws IOException {
/* 30 */     if (values.length != this.columnCount) {
/* 31 */       throw new IllegalArgumentException("Invalid number of columns, expected " + this.columnCount + ", but got " + values.length);
/*    */     }
/*    */     
/* 34 */     writeLine(Stream.of(values));
/*    */   }
/*    */   
/*    */   private void writeLine(Stream<? extends Object> values) throws IOException {
/* 38 */     this.output.write((String)values.<CharSequence>map(CsvOutput::getStringValue).collect(Collectors.joining(",")) + "\r\n");
/*    */   }
/*    */   
/*    */   private static String getStringValue(Object value) {
/* 42 */     return StringEscapeUtils.escapeCsv((value != null) ? value.toString() : "[null]");
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 46 */     private final List<String> headers = Lists.newArrayList();
/*    */     
/*    */     public Builder addColumn(String header) {
/* 49 */       this.headers.add(header);
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public CsvOutput build(Writer writer) throws IOException {
/* 54 */       return new CsvOutput(writer, this.headers);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/CsvOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */