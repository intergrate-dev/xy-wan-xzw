package com.founder.mobileinternet.cmsinterface.util;

import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class JsonConverterWithContentLength extends MappingJacksonHttpMessageConverter
{
	private ObjectMapper objectMapper = new ObjectMapper();
	private ThreadLocal<byte[]> outObject = new ThreadLocal<byte[]>();

	public JsonConverterWithContentLength()
	{
		super.setObjectMapper(objectMapper);
	}

	@Override
	protected Long getContentLength(Object t, MediaType contentType)
	{
		try
		{
			byte[] bytes = this.objectMapper.writeValueAsBytes(t);
			outObject.set(bytes);
			return (long) (bytes.length);
		} catch (JsonGenerationException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException
	{
		OutputStream os = outputMessage.getBody();
		os.write(outObject.get());
		os.flush();
		os.close();
	}
}
