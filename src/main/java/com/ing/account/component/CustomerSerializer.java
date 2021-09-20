/**
 * 
 */
package com.ing.account.component;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.model.CustomerDto;

/**
 * @author Nailesh
 *
 */
public class CustomerSerializer implements Serializer<CustomerDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSerializer.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String topic, CustomerDto data) {
		try {
			if (data == null) {
				LOGGER.info("Null received at serializing");
				throw new ResourceNotFoundException("Null message recived to seralize....");
			}
			LOGGER.info("Serializing...");
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error when serializing MessageDto to byte[]");
		}
	}
}
