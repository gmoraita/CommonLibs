package net.allwebdesign.common.lib.utils;


import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;

import net.allwebdesign.common.lib.db.DataResults;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * A class with methods to easily send send emails 
 * @author George Moraitakis
 *
 */
public class SendMail
{
	private JavaMailSender mailSender;
	private SimpleMailMessage simpleMailMessage;
 
	/**
	 * 
	 * @param simpleMailMessage
	 */
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
 
	/**
	 * 
	 * @param mailSender
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * 
	 * @param subject
	 * @param body
	 * @return
	 */
	public boolean sendPreparedMail(String subject, String body) {
		 
		return this.sendPreparedMail(subject, body, null, null);
	}
	
	/**
	 * 
	 * @param subject
	 * @param body
	 * @param attachmentName
	 * @param attachmentContent
	 * @return
	 */
	public boolean sendPreparedMail(String subject, String body, String attachmentName, String attachmentContent) {
 
		return this.send(
				simpleMailMessage.getFrom(),
				simpleMailMessage.getTo(), 
				simpleMailMessage.getCc(), 
				simpleMailMessage.getBcc(), 
				subject, 
				body, 
				attachmentName, 
				attachmentContent);
		
		
	}
	
	/**
	 * Sends an email based on parameters. This variant takes as input data and puts it
	 * on a html table on the e-mail.
	 * @param from the sender address
	 * @param to the recipient address
	 * @param cc the cc recipient address
	 * @param bcc the bcc recipient address
	 * @param subject the e-mail subject
	 * @param text the body of the e-mail
	 * @param dataAnnotation A text to put above the table of data
	 * @param data the data to populate the table with
	 * @param attachmentName the attachment file name
	 * @param attachmentContent the attachment content
	 * @return true if sent successfully or false if not sent
	 */
	public boolean send(
			String from,
			String[] to,
			String[] cc,
			String[] bcc,
			String subject,
			String dataAnnotation,
			DataResults data,
			String attachmentName,
			String attachmentContent
	
	){
		return this.send(from, to, cc, bcc, subject, SendMail.createMailBodyDataTable(data, dataAnnotation), attachmentName, attachmentContent);
	}
	
	
	/**
	 * Sends an email based on parameters
	 * @param from the sender address
	 * @param to the recipient address
	 * @param cc the cc recipient address
	 * @param bcc the bcc recipient address
	 * @param subject the e-mail subject
	 * @param text the body of the e-mail
	 * @param attachmentName the attachment file name
	 * @param attachmentContent the attachment content
	 * @return true if sent successfully or false if not sent
	 */
	public boolean send(
			String from,
			String[] to,
			String[] cc,
			String[] bcc,
			String subject,
			String text,
			String attachmentName,
			String attachmentContent
	
	){
		MimeMessage message = mailSender.createMimeMessage();
		 
		try{
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			if (cc != null ){
				helper.setCc(cc);
			}
			if (bcc != null ){
				helper.setCc(bcc);
			}
			helper.setSubject(subject);
			helper.setText(text,true);
		 	
			if (attachmentName != null && attachmentName.length()>0 && attachmentContent!=null){
				helper.addAttachment(attachmentName, new ByteArrayResource(fromString(attachmentContent)));
			}
		}catch (MessagingException e) {
			e.printStackTrace();
			return false;
			
		}
		mailSender.send(message);
		return true;
	}		
	
	
	private static final String ENCODING = "ISO-8859-7";

	private static byte[] fromString(String str)
	{
		byte[] bytes = null;
		try {
			bytes = str.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * Get the mail message
	 * @return the mail message
	 */
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}
	
	/**
	 * Creates a data table in html form for the body of the email
	 * @param data the data from a query
	 * @param tableAnnotation a simple annotation to appear above the table
	 * @return the html with the table
	 */
	public static String createMailBodyDataTable(DataResults data, String tableAnnotation){
		if (data.size()==0){return "No data found";}

        StringBuffer sb = new StringBuffer();

        Object[] tableHeaders = data.get(0).getFields().keySet().toArray();
        if (tableAnnotation != null){
        	sb.append("<div>").append(tableAnnotation).append("</div>");
        }
        
        sb.append("<TABLE border=\"1\" style=\"font-family:arial; font-size: 8pt; border: solid 1px #888; border-collapse:collapse;padding:0 \"><TR>");

        // Create the header
        for (int h = 0; h < tableHeaders.length; h++){
            String header = (String)tableHeaders[h];
            if (tableHeaders[h] == null){header = "";}
            sb.append("<TH style=\"border:solid 1px #888; background-color: #c1d8fb; padding: 4px; color: 00309c; \">").append(header).append("</TH>");
        }
        sb.append("</TR>");
                  

        // Create the body
        sb.append("<TBODY>");
        for (int i = 0; i < data.size(); i++){
        	sb.append("<TR>");
            Object[] values = data.get(i).getFields().values().toArray();

            for (int j = 0; j < values.length; j++){
                    String value = (String)values[j];
                    if (value == null){value = "";}
                    sb.append("<TD style=\"border: solid 1px #888; text-align: center; padding: 4px;     \">").append(value).append("</TD>");
              }
              sb.append("</TR>");
        }
        sb.append("</TBODY></TABLE>");
        return sb.toString();

	}


	
	
}