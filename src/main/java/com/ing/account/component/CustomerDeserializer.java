/**
 * 
 */
package com.ing.account.component;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.model.CustomerDto;

/**
 * @author Nailesh
 *
 */
public class CustomerDeserializer implements Deserializer<CustomerDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDeserializer.class);
	private static final String UTF8 = "UTF-8";
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public CustomerDto deserialize(String topic, byte[] data) {
		try {
			if (data == null) {
				LOGGER.info("Null received at deserializing");
				throw new ResourceNotFoundException("Null message received for deserialization....");
			}
			LOGGER.info("Deserializing...");
			return objectMapper.readValue(new String(data, UTF8), CustomerDto.class);
		} catch (Exception e) {
			throw new SerializationException("Error when deserializing byte[] to customerDto");
		}
	}

}
