/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.netcrackerteam.applicationForm;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.mail.ByteArrayDataSource;
import ua.netcrackerteam.controller.StudentData;


/**
 *
 * @author tanya
 */
public class ApplicationForm{   
    
    /**
     * Create from (pdf-format)
     */
    public void generateFormPDF(OutputStream memory) {
             
        try {
                       
            BaseFont font = BaseFont.createFont("src\\main\\java\\times.ttf", "cp1251", BaseFont.EMBEDDED);
            PdfReader reader = new PdfReader("src\\main\\java\\Template.pdf");
            PdfStamper stamper = new PdfStamper(reader, memory);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(font);
            fillFormData(form);          
            PdfContentByte content = stamper.getOverContent(1);
            content.addImage(reciveImage());        
            stamper.close();
            
        } catch (DocumentException ex) {
            Logger.getLogger(ApplicationForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationForm.class.getName()).log(Level.SEVERE, null, ex);
        }
              
      
    }
    
    public void fillFormData(AcroFields form) throws IOException, DocumentException{
        
        StudentData studentData = new StudentData();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        
        form.setField("info1", studentData.getStudentLastName());
        form.setField("info2", studentData.getStudentFirstName());
        form.setField("info3", studentData.getStudentMiddleName());
        form.setField("info4", String.valueOf(studentData.getStudentInstitute()));
        form.setField("info5", String.valueOf(studentData.getStudentInstituteCourse()));
        form.setField("info6", String.valueOf(studentData.getStudentFaculty()));
        form.setField("info7", String.valueOf(studentData.getStudentCathedra()));
        form.setField("info8", String.valueOf(studentData.getStudentInstituteGradYear()));
        form.setField("year1", String.valueOf(currentYear));
        form.setField("year2", String.valueOf(currentYear));
        form.setField("id1", String.valueOf(studentData.getIdForm()));
        form.setField("id2", String.valueOf(studentData.getIdForm()));
        form.setField("email 1", studentData.getStudentEmailFirst());
        form.setField("email 2", studentData.getStudentEmailSecond());
        form.setField("interestDevelopment", studentData.getStudentInterestDevelopment());
        form.setField("interestOther", studentData.getStudentInterestOther());
        form.setField("interestStudy", studentData.getStudentInterestStudy());
        form.setField("interestWork", studentData.getStudentInterestWork());
        form.setField("tel", studentData.getStudentTelephone());
        form.setField("otherContacts", studentData.getStudentOtherContact());
        form.setField("typeWorkDifferent", studentData.getStudentWorkTypeVarious());
        form.setField("typeWorkLead", studentData.getStudentWorkTypeManagement());
        form.setField("typeWorkOther", studentData.getStudentWorkTypeOther());
        form.setField("typeWorkSales", studentData.getStudentWorkTypeSale());
        form.setField("typeWorkSpeciality", studentData.getStudentWorkTypeDeepSpec());
        form.setField("technology1", String.valueOf(studentData.getStudentKnowledgeNetwork()));
        form.setField("technology2", String.valueOf(studentData.getStudentKnowledgeEfficientAlgorithms()));
        form.setField("technology3", String.valueOf(studentData.getStudentKnowledgeOOP()));
        form.setField("technology4", String.valueOf(studentData.getStudentKnowledgeDB()));
        form.setField("technology5", String.valueOf(studentData.getStudentKnowledgeWeb()));
        form.setField("technology6", String.valueOf(studentData.getStudentKnowledgeGUI()));
        form.setField("technology9", String.valueOf(studentData.getStudentKnowledgeProgramDesign()));
        form.setField("technology8_1", studentData.getStudentKnowledgeOther1() + " " + studentData.getStudentKnowledgeOther1Mark());
        form.setField("technology8_2", studentData.getStudentKnowledgeOther2() + " " + studentData.getStudentKnowledgeOther2Mark());
        form.setField("technology8_3", studentData.getStudentKnowledgeOther3() + " " + studentData.getStudentKnowledgeOther3Mark());
        form.setField("technology7", String.valueOf(studentData.getStudentKnowledgeNetworkProgramming()));
        form.setField("language1", studentData.getStudentLanguage1());
        form.setField("language2", studentData.getStudentLanguage2());
        form.setField("language3", studentData.getStudentLanguage3());
        form.setField("mark1", String.valueOf(studentData.getStudentCPlusPlusMark()));
        form.setField("mark2", String.valueOf(studentData.getStudentJavaMark()));
        form.setField("mark3", String.valueOf(studentData.getStudentLanguage1Mark()));
        form.setField("mark4", String.valueOf(studentData.getStudentLanguage2Mark()));
        form.setField("mark5", String.valueOf(studentData.getStudentLanguage3Mark()));
        form.setField("project", studentData.getStudentExperienceProjects());
        form.setField("promises", studentData.getStudentReasonOffer());
        form.setField("english1", String.valueOf(studentData.getStudentEnglishWriteMark()));
        form.setField("english2", String.valueOf(studentData.getStudentEnglishReadMark()));
        form.setField("english3", String.valueOf(studentData.getStudentEnglishSpeakMark()));
        //form.setField("aboutCenter", studentData.getStudentHowHearAboutCentre());
        form.setField("additional", studentData.getStudentSelfAdditionalInformation());
        
       
    }
    public Image reciveImage() throws BadElementException, MalformedURLException, IOException{
        
        Image img = Image.getInstance("src\\main\\java\\1.jpg");
        img.setAbsolutePosition(70f, 615f);
        img.scaleAbsolute(150, 140);
        
        return img;
    }
    
    public String readHTMLContent(){
        return  "Test";
    }
    
    public void sendPDFToStudent(String mailStudent, String nameStudent) throws MessagingException, IOException{
    	                 
	        String sender = "NetcrackerTeamOdessaOspu@gmail.com"; 
	        String recipient = "klitna.tetiana@gmail.com"; 
	       
	        String subject = "Учебный Центр NetCracker при ОНПУ"; 
	         	                    
                Properties properties = new Properties();
                properties.put("mail.transport.protocol", "smtp");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", "465");
                properties.put("mail.debug", "false");
                properties.put("mail.smtp.ssl.enable", "true");
          
                Authenticator auth = new SMTPAuthenticator();
	        Session session = Session.getDefaultInstance(properties, auth);                              
	                       
	       	
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = readHTMLContent();
                messageBodyPart.setContent(htmlText, "text/html");	        
                
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        generateFormPDF(outputStream);
	        byte[] bytes = outputStream.toByteArray();	             
	        DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
	        MimeBodyPart pdfBodyPart = new MimeBodyPart();
	        pdfBodyPart.setDataHandler(new DataHandler(dataSource));
	        pdfBodyPart.setFileName("FormForInterview.pdf");                    
                                                         
	        MimeMultipart mimeMultipart = new MimeMultipart();
                mimeMultipart.addBodyPart(messageBodyPart);	       
	        mimeMultipart.addBodyPart(pdfBodyPart);	             
	        
	        InternetAddress iaSender = new InternetAddress(sender);
	        InternetAddress iaRecipient = new InternetAddress(recipient);	             
	        
	        MimeMessage mimeMessage = new MimeMessage(session);
	        mimeMessage.setSender(iaSender);
	        mimeMessage.setSubject(subject);
	        mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);           
	        mimeMessage.setContent(mimeMultipart);                  	             
	     
               Transport transport = session.getTransport();
               transport.connect();
	       transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
               transport.close();
                
                
// -------------------------------------------------

                 /*InternetAddress iaSender = new InternetAddress(sender);
	        InternetAddress iaRecipient = new InternetAddress(recipient);
                
MimeMessage message = new MimeMessage(session);
message.setSender(iaSender);
message.setSubject(subject);
message.setRecipient(Message.RecipientType.TO, iaRecipient); 

message.setSubject(subject);

// Add html content

// Specify the cid of the image to include in the email

  MimeMultipart multipart = new MimeMultipart("related");

        // first part  (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "< <h1>Здравствуйте, Татьяна!</h1>img src=\"cid:image\">";
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);
        
        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("src\\\\main\\\\java\\\\logotip.png");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID","<image>");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // put everything together
        message.setContent(multipart);

// Send the message
Transport.send(message);*/
     
    }
    
     private class SMTPAuthenticator extends Authenticator {
         
         @Override
        public PasswordAuthentication getPasswordAuthentication() {
           String username = "NetcrackerTeamOdessaOspu@gmail.com";
           String password = "12345odessa";
           return new PasswordAuthentication(username, password);
           
        }
    }

    public static void main(String[] args){       
        try {
             ApplicationForm form = new ApplicationForm();
             form.sendPDFToStudent("klitna.tetiana@gmail.com", "Tanya");
        } catch (MessagingException ex) {
            Logger.getLogger(ApplicationForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}