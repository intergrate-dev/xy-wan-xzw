package com.founder.mobileinternet.cmsinterface.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UpdateSpringMVCDefaultEncode
{
	private AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter;
	private final MediaType TXT_UTF8 = MediaType.parseMediaType("text/plain;charset=UTF-8");
	private final MediaType ANY = MediaType.parseMediaType("*/*");
	private final MediaType APP_JSON_UTF8 = MediaType.parseMediaType("application/json;charset=UTF-8");

	@Autowired
	public UpdateSpringMVCDefaultEncode(AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter) throws InstantiationException, IllegalAccessException
	{
		this.annotationMethodHandlerAdapter = annotationMethodHandlerAdapter;

		updateDefaultEncode(StringHttpMessageConverter.class, TXT_UTF8);
		removeDefaultEncode(StringHttpMessageConverter.class, ANY);
		removeConverter(MappingJacksonHttpMessageConverter.class);
		updateDefaultEncode(JsonConverterWithContentLength.class, APP_JSON_UTF8);

		System.out.println("修改spring MVC默认编码完毕");
	}

	private <T extends AbstractHttpMessageConverter> void updateDefaultEncode(Class<T> classz, MediaType newMediaType) throws InstantiationException, IllegalAccessException
	{
		boolean isHaveConverter = false;
		for (HttpMessageConverter httpConverter : annotationMethodHandlerAdapter.getMessageConverters())
		{
			if (httpConverter.getClass().equals(classz))
			{
				isHaveConverter = true;
				// boolean isHaveMedia = false;
				List<MediaType> mediaTypes = new ArrayList<MediaType>();
				mediaTypes.addAll(httpConverter.getSupportedMediaTypes());

				int tmpIndex = -1;
				for (int i = mediaTypes.size() - 1; i >= 0; i--)
				{
					MediaType mediaType = mediaTypes.get(i);
					if (mediaType.getType().equalsIgnoreCase(newMediaType.getType()) && mediaType.getSubtype().equalsIgnoreCase(newMediaType.getSubtype()))
					{
						tmpIndex = i;
						mediaTypes.remove(mediaType);
					}
				}

				if (tmpIndex > -1)
					mediaTypes.add(tmpIndex, newMediaType);
				else
					mediaTypes.add(newMediaType);
				((AbstractHttpMessageConverter) httpConverter).setSupportedMediaTypes(mediaTypes);
			}
		}
		if (!isHaveConverter)
		{
			HttpMessageConverter<?>[] oldArray = annotationMethodHandlerAdapter.getMessageConverters();
			HttpMessageConverter<?>[] newArray = new HttpMessageConverter<?>[oldArray.length + 1];
			System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(newMediaType);
			T newConvert = classz.newInstance();
			newConvert.setSupportedMediaTypes(mediaTypes);

			newArray[oldArray.length] = newConvert;

			annotationMethodHandlerAdapter.setMessageConverters(newArray);
		}

		System.out.println("修改spring MVC默认编码..." + classz.getCanonicalName());
	}
	
	private <T extends AbstractHttpMessageConverter> void removeConverter(Class<T> classz) throws InstantiationException, IllegalAccessException
	{
		
		HttpMessageConverter<?>[] oldArray = annotationMethodHandlerAdapter.getMessageConverters();
		List<HttpMessageConverter<?>> newArray = new ArrayList<HttpMessageConverter<?>>(oldArray.length);

		for (HttpMessageConverter httpConverter : oldArray)
		{
			if (!httpConverter.getClass().equals(classz))
			{
				newArray.add(httpConverter);
			}
			else
			{
				System.out.println("移除spring MVC HttpMessageConverter..." + classz.getCanonicalName());
			}
		}
		HttpMessageConverter<?>[] newConverters = new HttpMessageConverter<?>[newArray.size()];

		annotationMethodHandlerAdapter.setMessageConverters(newArray.toArray(newConverters));
	}
	
	private <T extends AbstractHttpMessageConverter> void removeDefaultEncode(Class<T> classz, MediaType newMediaType) throws InstantiationException, IllegalAccessException
	{
		for (HttpMessageConverter httpConverter : annotationMethodHandlerAdapter.getMessageConverters())
		{
			if (httpConverter.getClass().equals(classz))
			{
				List<MediaType> mediaTypes = new ArrayList<MediaType>();
				mediaTypes.addAll(httpConverter.getSupportedMediaTypes());

				for (int i = mediaTypes.size() - 1; i >= 0; i--)
				{
					MediaType mediaType = mediaTypes.get(i);
					if (mediaType.getType().equalsIgnoreCase(newMediaType.getType()) && mediaType.getSubtype().equalsIgnoreCase(newMediaType.getSubtype()))
					{
						mediaTypes.remove(mediaType);
					}
				}

				((AbstractHttpMessageConverter) httpConverter).setSupportedMediaTypes(mediaTypes);
			}
		}

		System.out.println("修改spring MVC默认编码..." + classz.getCanonicalName());
	}
}
