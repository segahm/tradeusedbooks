package server;
import java.io.*;
public class sendSpamCron{
	private static final int batch_size = 6;
	private static final int batch_timeout = 60; //seconds
	public static void main(String[] args){
		BufferedReader reader = null;
		try{
			log.writeLog("send began");
			//create reader to be used continuosly
			reader = new BufferedReader(
				new FileReader(StringInterface.rootPath+"emails.txt"));
			boolean finished = false;
			//while not finished naturally continue
			while(!finished){
				try{
					send(reader);
					finished = true;
				}catch(Exception e){
					log.writeException(e.getMessage());
					//sleep for 5 minutes
					try{
						Thread.sleep(5*60*1000);
					}catch(InterruptedException ignore){
					}
				}
			}
		}catch(Exception e){
			log.writeException(e.getMessage());
		}finally{
			log.writeLog("send ended");
		}
	}
	private static void send(BufferedReader reader) throws Exception{
		SendMassMail mail = null;
		try{
			mail = new SendMassMail();
			//final String subject = "test";
			final String subject = "how to sell textbooks at your college this quarter";
			String message = null;
			int messages_count = 0;
			String line = reader.readLine();
			while(line!=null){
				String name = line.split(",")[0].trim();
				String email = line.split(",")[1].trim();
				if (messages_count == batch_size){
					mail.close();
					messages_count = 0;
					try{
						Thread.sleep(batch_timeout*1000);
					}catch(InterruptedException e){
					}
					mail = new SendMassMail();
					log.writeLog("batch sent\nlast email: "+email);
				}
				message = "Dear "+name+",\n"
					+"This letter was sent to you from http://www.tradeusedbooks.com because we believe that you might be interested in selling your used textbooks. TradeUsedBooks.com is a web site that allows college students like you to trade textbooks within the college community. We are dedicated to promoting textbook exchange at colleges all around the country and thus it is our great pleasure to invite you to join our community by posting your textbooks today. And the best part is - we make selling and buying textbooks easy, convenient, and free...\n"
					+"sincerely,\n"
					+"tradeusedbooks.com team\n"
					+"Click on the link to unsubscribe from this announcement list - http://www.tradeusedbooks.com/emailremove?"+email+" or contact us at 15111 N Hayden Rd., Suite 160 PMB353 Scottsdale, Arizona 85260 United States";
				
				messages_count++;
				mail.send(email,message,subject);
				line = reader.readLine();
			}
		}catch(Exception e){
			throw e;
		}finally{
			mail.close();
		}
	}
}