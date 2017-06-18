package com.nlp.dto;

import com.nlp.util.JSONUtils;

public class QueueDTO {
	private String name;
	private long queueSize;
	private long consumerCount;
	private long enqueueCount;
	private long dequeueCount;
	public QueueDTO() {
	}
	public QueueDTO(String name, long queueSize, long consumerCount, long enqueueCount, long dequeueCount) {
		this.name = name;
		this.queueSize = queueSize;
		this.consumerCount = consumerCount;
		this.enqueueCount = enqueueCount;
		this.dequeueCount = dequeueCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}
	public long getConsumerCount() {
		return consumerCount;
	}
	public void setConsumerCount(long consumerCount) {
		this.consumerCount = consumerCount;
	}
	public long getEnqueueCount() {
		return enqueueCount;
	}
	public void setEnqueueCount(long enqueueCount) {
		this.enqueueCount = enqueueCount;
	}
	public long getDequeueCount() {
		return dequeueCount;
	}
	public void setDequeueCount(long dequeueCount) {
		this.dequeueCount = dequeueCount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
