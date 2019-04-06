package com.infy.mqsunix;
import java.util.Enumeration;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import com.ibm.mq.jms.MQQueueConnectionFactory;

public class QueueDepth {
 
 private static MQQueueConnectionFactory factory;
 private static QueueConnection connection;
 private static QueueSession session;
 private static Queue queue;
 private static QueueBrowser browser;
 
 
 public static void main(String[] args) 
 {
  String environment = args[0];
	 	 System.out.println("\tPolling Queue in "+ environment);
	 pollMQQueue(environment);
 }
 
 /**
  * Fetches the latest 5 messages from the Q.
  * @author Rohith
  */

 
 private static void pollMQQueue(String environment){
 
  try{
	 
		int portNumber = Integer.parseInt(FilePropertyManager.getProperty("PORT_NUM_"+environment));
		String QueueManager = FilePropertyManager.getProperty("QUEUE_MANAGER_"+environment);
		String Channel = FilePropertyManager.getProperty("CHANNEL_"+environment);
		String HostName = FilePropertyManager.getProperty("HOST_IP_ADDRESS_"+environment);
		String QueueName = FilePropertyManager.getProperty("QUEUE_NAME_"+environment);
		String ReplyToQueueName = FilePropertyManager.getProperty("REPLY_TO_QUEUE_"+environment);
		String path=FilePropertyManager.getProperty("MESSAGE_FILE_"+environment);
		//System.out.println(portNumber);
		//System.out.println(ReplyToQueueName);
		String mqQueue = "queue:///"+QueueName+"?targetClient=1";

   factory = new MQQueueConnectionFactory();
   factory.setQueueManager(QueueManager);
   factory.setHostName(HostName);
   factory.setPort(portNumber);
   factory.setChannel(Channel);
   factory.setTransportType(1);
 
   try{
    // create a connection
    connection = factory.createQueueConnection();
    // start the connection to enable receives
    connection.start();
    // create a session
    session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    queue = session.createQueue(mqQueue);
   }catch(Exception ex){
    System.err.println("##Exception while getting ["+ mqQueue +"] queue connection## >>"
      + ex.getMessage());
    ex.printStackTrace();
   }
   //Creating the q browser
   browser = session.createBrowser(queue);
   //Getting the queueDepth
   Enumeration qEnum = browser.getEnumeration();
   int qDepth = 0;
   while (qEnum.hasMoreElements()) {
    qDepth++;
    qEnum.nextElement();
   }
   System.out.println("qDepth: ["+ qDepth +"]");
   
   //Browsing through the queueMessages
   Enumeration browEnum = browser.getEnumeration();
   int count = 1;
   while (browEnum.hasMoreElements() && (count<=qDepth)) {
    Message message = (Message) browEnum.nextElement();
 
    System.out.println("---------------------------------------------------------");
    String mesType = null;
    if(message instanceof TextMessage){
     mesType = "TextMessage";
    }
    if(message instanceof BytesMessage){
     mesType = "BytesMessage";
    }
    if(message instanceof ObjectMessage){
     mesType = "ObjectMessage";
    }
    if(message instanceof MapMessage){
     mesType = "MapMessage";
    }
    if(message instanceof StreamMessage){
     mesType = "StreamMessages";     
    }
 
    javax.jms.TextMessage msg1 = (javax.jms.TextMessage)message;
    String msgText = msg1.getText();
    
    System.out.println("##"+ mesType +"["+ count +"/"+ qDepth +"]##>>\n" + msgText
      /*+ "\n##JMSMessageID## >>\t" + message.getJMSMessageID()
      + "\n##JMSCorrelationID## >>\t" + message.getJMSCorrelationID()*/);
    count++;
   }
   
   if(qDepth>5){
    System.out.println("\n\n" +
      "#####################################################\n" +
      "# qDepth exceeded default browse limit[5]. Program  #\n" +
      "# terminated after fetching the latest 5 messsages. #\n" +
      "#####################################################");
   }
 
  }catch(JMSException jmsEx){
   System.err.println("##JMSException Occured## >>"+ jmsEx.getMessage());
   jmsEx.printStackTrace();
  }
  closeQueueConnection();
 }
 
private static void closeQueueConnection() {
  try {
   session.close();
   connection.close();
  } catch (JMSException e) {
   e.printStackTrace();
  }
 }
}


