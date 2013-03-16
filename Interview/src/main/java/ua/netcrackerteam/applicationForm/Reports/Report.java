/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.netcrackerteam.applicationForm.Reports;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.JFreeChart;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D; 
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import ua.netcrackerteam.applicationForm.ClassPath;




/**
 * Template for generation report
 * @author tanya
 */
public class Report {

    private final  String pathTimesTTF = "resources/times.ttf";
    private final  String pathImage =    "resources/Logotip.png";
    private String path = ClassPath.getInstance().getWebInfPath();
    
    private String[][] report;
    JFreeChart chart;
    
    public Report(String[][] report, JFreeChart chart){
        this.report = report;
        this.chart = chart;
    }
    
    public Report(String[][] report){
      this.report = report;  
    }
       
    public ByteArrayOutputStream createTemplate(String title, float[] sizeTable){
        
       ByteArrayOutputStream memory = null;
       Document document = new Document();
       PdfWriter writer = null;  
 
        try {
            
           BaseFont bf = BaseFont.createFont(path + pathTimesTTF, "cp1251", BaseFont.EMBEDDED); 
           Font fontTitle = new Font(bf, 16, Font.BOLDITALIC);
           Font fontCurrentDate = new Font(bf, 12, Font.BOLDITALIC);
           
           memory =  new ByteArrayOutputStream(); 
           
           writer = PdfWriter.getInstance(document , memory);   
           document.addCreationDate();
           document.addProducer();
           document.addTitle(title);
           document.setPageSize(PageSize.A4);
           document.open();  
           
           PdfPTable table = new PdfPTable(2);    
           //Image
           PdfPCell cellImage = new PdfPCell(Image.getInstance(path + pathImage)); 
           cellImage.setBorder(Rectangle.NO_BORDER);
           cellImage.setHorizontalAlignment(Element.ALIGN_RIGHT);
           //Create data
           DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy  HH:mm");
           String currentDate = dateFormat.format(java.util.Calendar.getInstance().getTime());
           PdfPCell cellDateCreate = new PdfPCell(new Phrase(currentDate, fontCurrentDate));
           cellDateCreate.setBorder(Rectangle.NO_BORDER);
           cellDateCreate.setVerticalAlignment(Element.ALIGN_TOP);
           //title
           PdfPCell cellTitle = new PdfPCell(new Phrase(title, fontTitle));
           cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
           cellTitle.setBorder(Rectangle.NO_BORDER);
           cellTitle.setColspan(2);
           //table
           PdfPCell cellTable = new PdfPCell();      
           cellTable.addElement(createTable(sizeTable));  
           cellTable.setColspan(2);
           cellTable.setBorder(Rectangle.NO_BORDER);
                     
           table.addCell(cellDateCreate);  
           table.addCell(cellImage);                                 
           table.addCell(cellTitle);            
           table.addCell(cellTable);
           
           document.add(table);
           
           //add chart to report
           if(chart != null){                     
                PdfContentByte cb = writer.getDirectContent();
                float width = PageSize.A4.getWidth()*2/3;
                float height = PageSize.A4.getHeight()/2; 
                PdfTemplate bar = cb.createTemplate(width, height);
                Graphics2D g2d2 = new PdfGraphics2D(bar, width, height); 
                Rectangle2D r2d2 = new Rectangle2D.Double(0, 0, width, height);        
                chart.draw(g2d2, r2d2);       
                g2d2.dispose();                
      
                Image chartImage = Image.getInstance(bar);
                document.add(chartImage);
           }  
           
           
        }catch (DocumentException dex){
           dex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally{
            if (document != null){                          
                document.close();}
            if (writer != null){                          
                writer.close();}
        } 
        
        return memory;
    }   
    
          
private void insertCell(PdfPTable table, String text, int align,
                        int colspan, Font font, 
                        BaseColor foregroudColor, BaseColor backgroundColor, BaseColor borderColor){   
        
        font.setColor(foregroudColor);
        
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font)); 
        
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(align);        
        cell.setColspan(colspan);   
        cell.setBorderColor(borderColor);
        
        if(text.trim().equalsIgnoreCase("")){
         cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
   
   } 

private PdfPTable createTable(float[] sizeTable) throws DocumentException, IOException{
           
           BaseColor fColor = BaseColor.BLACK;
           BaseColor bColor = WebColors.getRGBColor("#99CC99");
           BaseColor borderColor = WebColors.getRGBColor("#999966");           
           BaseColor bColorTableLine1 = WebColors.getRGBColor("#99CCCC");            
           BaseColor bColorTableLine2 = WebColors.getRGBColor("#CCFFCC");
           
           BaseFont font = BaseFont.createFont(path + pathTimesTTF, "cp1251", BaseFont.EMBEDDED);   
           Font bfBold12 = new Font(font, 11, Font.BOLDITALIC, new BaseColor(0, 0, 0)); 
           Font bf12 = new Font(font, 10, Font.ITALIC);              
            
           PdfPTable table = new PdfPTable(sizeTable); 
           table.setWidthPercentage(100f);                         
           //Header table
           for(int j = 0; j < report[0].length; j++){
            insertCell(table, report[0][j], Element.ALIGN_CENTER, 1, bfBold12, fColor, bColor, borderColor);
           }
           table.setHeaderRows(1); 
           //Content table     
           for(int i = 1; i < report.length - 1; i++){         
                  
                    BaseColor bColorLine = (i%2 == 0? bColorTableLine1: bColorTableLine2);                          
                    for(int j = 0; j < report[i].length; j++){         
                        insertCell(table, report[i][j], Element.ALIGN_CENTER, 1, bf12, fColor, bColorLine, borderColor);
                    }               
           }
           //Footer table
           for(int j = 0; j < report[report.length - 1].length; j++){
              insertCell(table,report[report.length - 1][j], Element.ALIGN_CENTER, 1, bfBold12, fColor, bColor, borderColor);}
       
           return  table;
    }
}