package com.infy.mqsunix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.jms.JMSException;
import javax.jms.Queue;

import com.ibm.mq.jms.MQQueueConnectionFactory;

public class sendMessageList {

	public static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	public static void main(String[] args) {
		String environment = args[0];
		int portNumber = Integer.parseInt(FilePropertyManager
				.getProperty("PORT_NUM_" + environment));
		String QueueManager = FilePropertyManager.getProperty("QUEUE_MANAGER_"
				+ environment);
		String Channel = FilePropertyManager.getProperty("CHANNEL_"
				+ environment);
		String HostName = FilePropertyManager.getProperty("HOST_IP_ADDRESS_"
				+ environment);
		String QueueName = FilePropertyManager.getProperty("QUEUE_NAME_"
				+ environment);
		String ReplyToQueueName = FilePropertyManager
				.getProperty("REPLY_TO_QUEUE_" + environment);
		String path = FilePropertyManager.getProperty("MESSAGE_FILE_"
				+ environment);
		//System.out.println(portNumber);
		//System.out.println(ReplyToQueueName);

		try {
			MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

			factory.setTransportType(1);
			factory.setPort(portNumber);
			factory.setQueueManager(QueueManager);
			factory.setChannel(Channel);
			factory.setHostName(HostName);

			String QueueAddr = "queue:///" + QueueName + "?targetClient=1";
			javax.jms.QueueSession session = null;
			javax.jms.QueueSender sender = null;
			javax.jms.QueueConnection m_qConn = factory.createQueueConnection();
			session = m_qConn.createQueueSession(false,
					javax.jms.Session.AUTO_ACKNOWLEDGE);
			Queue sender_queue = session.createQueue(QueueAddr);
			sender = session.createSender(sender_queue);

			long startTime = +System.currentTimeMillis();

			System.out.println("Start Time:" + startTime);
			javax.jms.Message message = session.createTextMessage();

			if ((ReplyToQueueName == "")
					|| (ReplyToQueueName.equalsIgnoreCase(""))) {
				//System.out.println("Reply to Queue default.");
			} else {
				Queue ReplyToQ = session.createQueue(ReplyToQueueName);
				message.setJMSReplyTo(ReplyToQ);
			}
			System.out.println("File:" +path);
			String SwiftMessageFile = readFile(path);
			String[] SwiftMessageList = (String[]) null;
			//System.out.println("XXX" + SwiftMessageFile.contains("$"));
			SwiftMessageList = SwiftMessageFile.split("\\$");
			/*
			 * if (SwiftMessageFile.contains("$")){ SwiftMessageList =
			 * SwiftMessageFile.split("\\$"); }else {
			 * System.out.println("YYY"+SwiftMessageFile+"ZZZ");
			 * SwiftMessageList[0]=SwiftMessageFile; }
			 */

			// String SwiftMessage =readFile(path);
			String SwiftMessage = null;
			System.out.println("No of messages:"+SwiftMessageList.length);
			for (int i = 0; i < SwiftMessageList.length; i++) {
				SwiftMessage = SwiftMessageList[i] + "\n";
				if(SwiftMessage!="" && !SwiftMessage.equals(null))
				System.out.println("MESSAGE "+(i+1)+" TO BE SENT:");
				System.out.println(SwiftMessage + "\n");

				((javax.jms.TextMessage) message).setText(SwiftMessage);
				m_qConn.start();
				sender.send(message);
			}
			sender.close();
			m_qConn.close();
			long stopTime = +System.currentTimeMillis();
			System.out.println("Stop Time:" + stopTime);
			System.out.println("Total Time in milliseconds ="
					+ (stopTime - startTime));
			System.out.println("Message sent to queue successfully...: ");
		} catch (JMSException jms) {
			//jms.getLinkedException().printStackTrace();
			jms.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
