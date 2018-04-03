package gov.grantsolutions.export.rest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jmohanta
 */
@CrossOrigin
@RestController
public class ExportControllerRest {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/remotecall", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> someAction(String userName,HttpServletResponse response) throws InterruptedException, DocumentException, IOException {
        Thread.sleep(6000); // simulated delay
        //messagingTemplate.convertAndSendToUser(userName,"/queue/notify",new Notification("hello "+userName));
        //messagingTemplate.send("/queue/notify", new GenericMessage("Hello"+userName));
        String filename = getDownloadPDF(response);
        messagingTemplate.convertAndSend("/queue/notify/"+userName, new Notification(userName+"-> "+filename));
        //messagingTemplate.convertAndSend("/queue/notify/"+userName, "hello "+userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public String getDownloadPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        response.setContentType("application/pdf");
        List<User> users = userService.findAllUsers();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING);
        document.open();
        document.add(new Paragraph("Generated Users " + LocalDate.now()));

        PdfPTable table = new PdfPTable(users.stream().findAny().get().getColumnCount());
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);

        // define font for table header row
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Age", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Job Title", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Company", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("City", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Country", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Phone Number", font));
        table.addCell(cell);

        for(User user : users){
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getAge().toString());
            table.addCell(user.getJobTitle());
            table.addCell(user.getCompany());
            table.addCell(user.getAddress());
            table.addCell(user.getCity());
            table.addCell(user.getCountry());
            table.addCell(user.getPhoneNumber());

        }
        document.add(table);
        document.close();
        
		response.setContentLength(baos.size());
                
		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
        return "my-pdf-file.pdf";
    }
    
    @RequestMapping(value="/downloadpdftest", method=RequestMethod.GET, produces="application/json")
    public String downloadPDFOutputExl(HttpServletResponse response){
        response.setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        response.setContentType("application/pdf");
        try{
            
        getPDF(userService.findAllUsers(), response);
        

        
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "OK";
    }
    
    public void getPDF(List<User> users, HttpServletResponse response) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING);
        document.open();
        document.add(new Paragraph("Generated Users " + LocalDate.now()));

        PdfPTable table = new PdfPTable(users.stream().findAny().get().getColumnCount());
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);

        // define font for table header row
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Age", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Job Title", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Company", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("City", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Country", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Phone Number", font));
        table.addCell(cell);

        for(User user : users){
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getAge().toString());
            table.addCell(user.getJobTitle());
            table.addCell(user.getCompany());
            table.addCell(user.getAddress());
            table.addCell(user.getCity());
            table.addCell(user.getCountry());
            table.addCell(user.getPhoneNumber());

        }
        document.add(table);
        document.close();
        
		response.setContentLength(baos.size());

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
        
    }
}
