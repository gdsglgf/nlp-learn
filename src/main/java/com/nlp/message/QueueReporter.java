package com.nlp.message;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nlp.dto.QueueDTO;

@Component
public class QueueReporter {
	// jmx服务地址，connectorPath="/jmxrmi" connectorPort="1099"
	public static String JMS_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
	// brokerName和type首字母要小写，brokerName=localhost
	public static String BROKER_NAME = "org.apache.activemq:brokerName=localhost,type=Broker";

	@Value("${jms.queue.doc}")
	private String docQueueName;

	@Value("${jms.queue.text}")
	private String textQueueName;

	@Value("${jms.queue.link}")
	private String linkQueueName;

	public Map<String, QueueDTO> loadQueueMessage() {
		Map<String, QueueDTO> queues = new HashMap<String, QueueDTO>();
		try {
			JMXServiceURL url = new JMXServiceURL(JMS_SERVICE_URL);
			JMXConnector connector = JMXConnectorFactory.connect(url, null);
			connector.connect();
			MBeanServerConnection connection = connector.getMBeanServerConnection();

			ObjectName name = new ObjectName(BROKER_NAME);
			BrokerViewMBean mBean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, name,
					BrokerViewMBean.class, true);

			for (ObjectName na : mBean.getQueues()) {
				QueueViewMBean queueBean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
						na, QueueViewMBean.class, true);

				String qname = queueBean.getName();
				long qsize = queueBean.getQueueSize();
				long consumerCount = queueBean.getConsumerCount();
				long enqCount = queueBean.getEnqueueCount();
				long deqCount = queueBean.getDequeueCount();

				queues.put(qname, new QueueDTO(qname, qsize, consumerCount, enqCount, deqCount));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queues;
	}
	
	public Map<String, Object> getQueueData() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 队列名称
		String[] qname = new String[]{docQueueName, linkQueueName, textQueueName};
		result.put("qname", qname);
		
		Map<String, QueueDTO> queues = loadQueueMessage();
		
		// 队列剩余数量
		long[] qize = new long[qname.length];
		for (int i = 0; i < qize.length; i++) {
			qize[i] = queues.get(qname[i]).getQueueSize();
		}
		result.put("qsize", qize);
		
		// 出队数量
		long[] deqSize = new long[qname.length];
		for (int i = 0; i < deqSize.length; i++) {
			deqSize[i] = queues.get(qname[i]).getDequeueCount();
		}
		result.put("deqSize", deqSize);
		
		return result;
	}
}