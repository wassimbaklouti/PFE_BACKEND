package com.pfe.backend.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.pfe.backend.constant.EmailConstant.*;


@Service
public class EmailService {
	
	public void sendNewPasswordEmail (String firstName , String password ,String email ,String username)  {
		Message message;

		try {
			message = createMail(firstName , password , email ,username);
			SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL) ;
			smtpTransport.connect(GMAIL_SMTP_SERVER,USERNAME,PASSWORD);
			smtpTransport.sendMessage(message, message.getAllRecipients());
			smtpTransport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private Message createMail(String firstName , String password , String email, String username) throws  MessagingException {
		MimeMessage message = new MimeMessage (getEmailSession());

		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email,false));
		message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(CC_EMAIL , false));
		message.setSubject(EMAIL_SUBJECT);String htmlContent = "<div class=\"\"><div class=\"aHl\"></div><div id=\":m7\" tabindex=\"-1\"></div><div id=\":n3\" class=\"ii gt\" jslog=\"20277; u014N:xr6bB; 1:WyIjdGhyZWFkLWY6MTc5Mjc5ODQ3MzkyNDA3Mjk3OSJd; 4:WyIjbXNnLWY6MTc5Mjc5ODQ3MzkyNDA3Mjk3OSJd\"><div id=\":my\" class=\"a3s aiL msg-290301008761648123\"><div class=\"adM\">\n" +
				"  \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"    \n" +
				"  \n" +
				"  </div><div style=\"word-spacing:normal;background-color:#efefef\"><div class=\"adM\">\n" +
				"</div><div style=\"background-color:#efefef\"><div class=\"adM\">\n" +
				"</div><div style=\"margin:0px auto;max-width:600px\"><div class=\"adM\">\n" +
				"</div>\n" +
				"</div>\n" +
				"\n" +
				"<div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px\">\n" +
				"<table style=\"background:#ffffff;background-color:#ffffff;width:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"direction:ltr;font-size:0px;padding:5px 5px 5px 5px;text-align:center\">\n" +
				"<div style=\"margin:0px auto;max-width:590px\">\n" +
				"<table style=\"width:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"direction:ltr;font-size:0px;padding:5px 5px 5px 5px;text-align:center\">\n" +
				"<div class=\"m_-290301008761648123mj-column-per-100\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%\">\n" +
				"<table style=\"vertical-align:top\" role=\"presentation\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"font-size:0px;padding:5px 5px 10px 5px;word-break:break-word\" align=\"left\">\n" +
				"<div style=\"font-family:BinancePlex,Arial,PingFangSC-Regular,'Microsoft YaHei',sans-serif;font-size:20px;font-weight:900;line-height:25px;text-align:left;color:#000000\">\n" +
				"<div>we are all really excited to welcome you to our team "+firstName+" </div>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"background:#ffffff;font-size:0px;padding:5px 5px 5px 5px;word-break:break-word\" align=\"left\">\n" +
				"<div style=\"font-family:BinancePlex,Arial,PingFangSC-Regular,'Microsoft YaHei',sans-serif;font-size:14px;line-height:20px;text-align:left;color:#000000\">\n" +
				"<div>Here are your login credentials :</div>\n" +
				"<div>&nbsp;</div>\n" +
				"<div><strong>Your User Name :</strong>"+ username +"</div>\n" +
				"<div><strong>Your Password :</strong> "+password+  "</div>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"font-size:0px;padding:10px 5px 10px 5px;word-break:break-word\" align=\"left\">\n" +
				"<table style=\"border-collapse:separate;line-height:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"border:none;border-radius:3px;background:#fcd535\" role=\"presentation\" align=\"center\" valign=\"middle\" bgcolor=\"#FCD535\"><a data-saferedirecturl=\"https://www.google.com/url?q=https://app.binance.com/en/my/security/account-activity?utm_source%3Dcrm%26utm_medium%3Demail%26utm_campaign%3Dtransactional%26utm_content%3Demail_login_ip%26_dp%3DL2FjY291bnQvYWNjb3VudEFjdGl2aXR5&amp;source=gmail&amp;ust=1709871784474000&amp;usg=AOvVaw3DBpzVTYQd9i5E_Vvvtvi7\" target=\"_blank\" style=\"display:inline-block;background:#fcd535;color:#000000;font-family:BinancePlex,Arial,PingFangSC-Regular,'Microsoft YaHei',sans-serif;font-size:14px;font-weight:900;line-height:15px;margin:0;text-decoration:none;text-transform:none;padding:10px 25px;border-radius:3px\" rel=\"noopener\" href=\"http://localhost:4200/acceuil\"> Check Your Account </a></td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"font-size:0px;padding:5px 5px 5px 5px;word-break:break-word\" align=\"left\">\n" +
				"<div style=\"font-family:BinancePlex,Arial,PingFangSC-Regular,'Microsoft YaHei',sans-serif;font-size:14px;line-height:20px;text-align:left;color:#000000\">\n" +
				"\n" +
				"<div>&nbsp;</div>\n" +
				"<div><em>This is an automated message, please do not reply.&nbsp; </em></div>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"\n" +
				"<div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px\">\n" +
				"<table style=\"background:#ffffff;background-color:#ffffff;width:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"border:0 none #000000;direction:ltr;font-size:0px;padding:5px 5px 5px 5px;text-align:center\">\n" +
				"<div style=\"margin:0px auto;max-width:590px\">\n" +
				"<table style=\"width:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"direction:ltr;font-size:0px;padding:5px 5px 5px 5px;text-align:center\">\n" +
				"<div class=\"m_-290301008761648123mj-column-per-100\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%\">\n" +
				"<table style=\"vertical-align:top\" role=\"presentation\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"font-size:0px;padding:5px 5px 5px 5px;word-break:break-word\" align=\"center\">\n" +
				"<p style=\"border-top:solid 1px #f0b90b;font-size:1px;margin:0px auto;width:100%\">&nbsp;</p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"\n" +
				"<tr>\n" +
				"<td class=\"m_-290301008761648123mj-group-full-width\" style=\"font-size:0px;word-break:break-word\">\n" +
				"<div class=\"m_-290301008761648123mj-column-per-25 m_-290301008761648123mj-group-full-width\" style=\"font-size:0;line-height:0;text-align:left;display:inline-block;width:100%;direction:ltr\">\n" +
				"<div class=\"m_-290301008761648123mj-column-per-100\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%\">\n" +
				"<table style=\"vertical-align:top\" role=\"presentation\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"<tbody>\n" +
				"\n" +
				"\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"\n" +
				"<div style=\"margin:0px auto;max-width:590px\">\n" +
				"<table style=\"width:100%\" role=\"presentation\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"direction:ltr;font-size:0px;padding:5px 5px 5px 5px;text-align:center\">\n" +
				"<div class=\"m_-290301008761648123mj-column-per-100\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%\">\n" +
				"<table style=\"vertical-align:top\" role=\"presentation\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"font-size:0px;padding:5px 5px 5px 5px;word-break:break-word\" align=\"center\">\n" +
				"<div style=\"font-family:BinancePlex,Arial,PingFangSC-Regular,'Microsoft YaHei',sans-serif;font-size:11px;line-height:15px;text-align:center;color:#000000\">© 2024 DataGov.com, All Rights Reserved.</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"</div>\n" +
				"</div>\n" +
				"</div>\n" +
				"<img src=\"https://ci3.googleusercontent.com/meips/ADKq_NavZttC3yQ8pjrAylGStLlGeRDohLF3cqSU9OvL1tXisrMUp1fJmhuoALT6jq9rX_H5zqlWpheEHHSn9RvVSd40jc6ckHECW-GVY0f2qFA9TA7c0eKweUtVrN1kdoo7c9TGxGvHjukjkI8QlLGskjTte5jNu_mcrq7-an_SScjPzsfgiqiERviWWG9_rYOxsVN9_LH0s84Iwbejk6iJwoxavuX3s2cMZ9c=s0-d-e1-ft#https://www.google-analytics.com/collect?v=1&amp;t=event&amp;tid=UA-237515680-1&amp;cid=19b769f4-1d97-4423-8d74-18dae0f859cb&amp;ec=email-mp&amp;ea=电子邮件打开\" class=\"CToWUd\" data-bit=\"iit\"></div><div class=\"yj6qo\"></div></div><div class=\"hi\"></div><div class=\"WhmR8e\" data-hash=\"0\"></div></div>" ;


		// Set the content of the email as HTML
		message.setContent(htmlContent, "text/html");message.setSentDate(new Date ());
		message.saveChanges();
		 return message;

	}
	
	private Session getEmailSession() {
		Properties properties = System.getProperties();
		properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_PORT, DEFAULT_PORT);
		properties.put(SMTP_STARTTLS_ENABLE, true);
		properties.put(SMTP_STARTTLS_REQUIRED , true);
		 return Session. getInstance(properties, null);

	}

}
