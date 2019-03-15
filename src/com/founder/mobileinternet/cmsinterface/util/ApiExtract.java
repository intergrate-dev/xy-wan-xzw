package com.founder.mobileinternet.cmsinterface.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 外网接口自动识别
 * @author han.xf
 *
 */
public class ApiExtract{
	public static void main(String[] args) throws Exception{
		JSONObject apis = new JSONObject() ;
		JSONArray arr = new JSONArray() ;
		
		// 扫描指定包名称下的所有定义的controller
		List<Class<?>> beans = new ArrayList<>() ; 
		String packageName = "com.founder.mobileinternet.cmsinterface.ui.controller" ;
		beans = findBeanClassByPackageName(packageName,beans) ;
		// 处理每个controller
		Iterator<Class<?>> iter = beans.iterator() ;
		while(iter.hasNext()) { 
			arr.add(dealBean(iter.next())) ;
		}
		
		apis.put("groups", arr) ;
		
		String result = formatJson("var api_groups = " + apis.toString());
		
		String savePath = "../../WEB/xy/system/script/api.json" ;
		print(savePath, result) ;
		System.out.println("Done!");
	}
	
	/**
	 * 递归扫描指定包名称下的 *.class 文件,得到所有定义的Controller
	 * @param packageName 包名称
	 * @param beans 存储扫描到的Controller
	 * @return 返回List集合
	 */
	public static List<Class<?>> findBeanClassByPackageName(String filePath,List<Class<?>> beans){
		File packageFile ;
		try {
			packageFile = new ClassPathResource(filePath.replace(".", File.separator)).getFile() ;
			File[] dirfiles = packageFile.listFiles(new FileFilter() {
				public boolean accept(File file) {  
	                return (true && file.isDirectory())  
	                        || (file.getName().endsWith(".class")) ;   
	            }
			}) ;
			for (File file : dirfiles) {
				if (file.isDirectory()) {
					findBeanClassByPackageName(filePath + "." + file.getName(),beans) ;
				}else{
					String className = file.getName().substring(0,file.getName().length() - 6) ;
					//Class<?> cls =Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className) ;
					Class<?> cls = Class.forName(filePath + "." + className) ;
					if(cls.isAnnotationPresent(Controller.class)) {
						beans.add(cls) ;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace() ;
		} catch (IOException e1) {
			e1.printStackTrace() ;
		}
		return beans ;
	}
	
	/**
	 * 处理单个 bean
	 */
	public static JSONObject dealBean(Class<?> cls){
		JSONObject obj = new JSONObject() ;
		if(cls.isAnnotationPresent(XYComment.class)){
			XYComment xyApi = cls.getAnnotation(XYComment.class) ;
			obj.put("name", xyApi.name()) ; //控制器描述
			if(xyApi.comment() != null && !"".equals(xyApi.comment())){
				obj.put("comment", xyApi.comment()) ;
			}
		}else{
			obj.put("name", cls.getSimpleName()) ; //控制器描述,如果没使用注解,先使用类名
		}
		obj.put("className", cls.getSimpleName()) ;
		JSONArray arr = new JSONArray() ;
		Method[] methods = cls.getMethods() ;
		for(Method method : methods) {
			if(method.isAnnotationPresent(RequestMapping.class)) {
				JSONObject _obj = dealMethod(method) ;
				arr.add(_obj) ;
			}
		}
		obj.put("list", arr) ; 
		return obj ;
	}
	
	/**
	 * 处理bean中的单个方法
	 */
	public static JSONObject dealMethod(Method method){
		JSONObject obj = new JSONObject() ;
		
		//判断是否使用XYApi注解
		if(method.isAnnotationPresent(XYComment.class)) {
			XYComment xyApi = method.getAnnotation(XYComment.class) ;
			obj.put("name", xyApi.name()) ; //方法描述
		}else{
			obj.put("name", method.getName()) ; //方法描述,如果没有使用XYComment注解,暂时使用方法名称
		}
		obj.put("methodName", method.getName()) ; //方法名称
		
		//取得方法路径,请求方式
		RequestMapping mapping = method.getAnnotation(RequestMapping.class) ;
		obj.put("url",JSONArray.fromObject(mapping.value())) ; //方法路径
		
		//方法请求方式
		if(mapping.method().length > 0) {
			obj.put("method", JSONArray.fromObject(mapping.method())) ;
		}
		
		//java反射包没有提供得到方法参数列表的方法,使用spring提供的方法,扫描的class文件生成的时候需要开启debug
		LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer= new LocalVariableTableParameterNameDiscoverer() ;
		String[] paramterNames = parameterNameDiscoverer.getParameterNames(method) ; // 参数名称
		Class<?>[] paramterTypes = method.getParameterTypes() ; // 参数类型
		Annotation[][] paramterAnnotations = method.getParameterAnnotations() ; //方法参数上的注解
		
		//处理方法参数
		JSONArray arr = new JSONArray() ;
		for(int i = 0 ; i < paramterTypes.length ; i++) {
			//不记录request,response
			if(HttpServletRequest.class == paramterTypes[i] || HttpServletResponse.class == paramterTypes[i]) continue ; 
			
			dealParamter(arr,paramterTypes[i],paramterNames[i],paramterAnnotations[i]) ;
		}
		obj.put("params", arr) ;
		return obj ;
	}
	
	/**
	 * 处理方法参数
	 * @param paramterType  参数类型
	 * @param paramterName  参数形参名称
	 * @param paramterAnnotations 参数注解
	 * @return
	 */
	public static void dealParamter(JSONArray arr,Class<?> paramterType,String paramterName,Annotation[] paramterAnnotations){
		
		//参数类型是VO类,扫描项目工程的包名
		if(paramterType.getName().contains("com.founder")){
			dealPOJO(arr, paramterType);
		}else{
			JSONObject obj = new JSONObject() ;

			obj.put("name", paramterName) ; // 参数 形参 名称
			obj.put("value", paramterName) ; //实参 参数 名
			obj.put("type", format(paramterType)) ; //参数类型 
			obj.put("required", true) ; //必须传递参数

			//处理参数注解
			for(int i = 0 ; i < paramterAnnotations.length ; i++) {
				if(paramterAnnotations[i] instanceof RequestParam){
					RequestParam requestParam = (RequestParam)paramterAnnotations[i] ;
					obj.put("required", requestParam.required()) ;
					if(requestParam.value() != null && !"".equals(requestParam.value())) {
						obj.put("value", requestParam.value()) ; //指定实参参数名
					}
					//如果没有定义默认值,必须要传递参数
					if(requestParam.defaultValue() != null && !requestParam.defaultValue().contains("\t")) {
						obj.put("default", requestParam.defaultValue()) ; //默认值
						obj.put("required", false) ;
					}
					
				}else if(paramterAnnotations[i] instanceof XYComment){
					XYComment xyComment = (XYComment)paramterAnnotations[i] ;
					obj.put("comment", xyComment.comment()) ; //参数的中文描述
				}
			}
			arr.add(obj) ;
		}
	}
	
	public static void dealPOJO(JSONArray arr,Class<?> paramterType){
		Field[] fields = paramterType.getDeclaredFields() ;
		
		for(int n = 0 ; n < fields.length ; n++) {
			JSONObject _obj = new JSONObject() ;
			Field field = fields[n] ;
			_obj.put("name", field.getName()) ; // 参数名称
			
			if(field.isAnnotationPresent(XYComment.class)){
				XYComment xyComment = field.getAnnotation(XYComment.class) ;
				//有注解时都读到comment中，因为不能覆盖参数名
				_obj.put("comment", xyComment.name() + " " + xyComment.comment()) ; //参数的中文描述
			}
			_obj.put("type", format(field)) ; //参数类型
			arr.add(_obj) ;
		}
	}
	
	/**
	 * 
	 */
	public static String format(Field field){
		
		Type type = field.getGenericType() ;
		if(type instanceof ParameterizedType){ // 是泛型参数的类型
			ParameterizedType pt = (ParameterizedType) type ;
			
			String name = pt.toString();
			name = name.replace("java.util.", "");
			name = name.replace("java.lang.", "");
			return name ;
		}
		
		return format(field.getType()) ;
	}
	
	/**
	 * 1.包装类字符串类型 类型名称转换为基础类型
	 * 2.字符串类型去掉包名称
	 */
	public static String format(Class<?> paramterType){
		// 基础类型
		if(paramterType.isPrimitive() || paramterType.equals(String.class)){ 
			return paramterType.getSimpleName() ;
		}
		
		// 包装类
		if(paramterType.equals(Byte.class)
				|| paramterType.equals(Short.class)
				|| paramterType.equals(Integer.class) 
				|| paramterType.equals(Long.class)
				|| paramterType.equals(Float.class)
				|| paramterType.equals(Character.class)
				|| paramterType.equals(Boolean.class)){
			try {
				Field field = paramterType.getDeclaredField("TYPE") ;
				return field.get(null).toString() ;
			} catch (Exception e) {
				return paramterType.getName() ;
			}
		}
		
		return paramterType.getName();
	}
	
	/**
	 * 格式化json
	 */
	public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    /**
	 * 输出到指定文件
	 * @param json
	 */
	public static void print(String savePath,String json){
		PrintStream ps = null ;
		try {
			ps = new PrintStream(new File(savePath)) ;
			ps.print(json) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace() ;
		}finally{
			if(ps != null)
				ps.close() ;
		}
	}
	
	public static JSONObject scanner(String savePath) {
		Scanner scanner = null ;
		StringBuffer buffer = new StringBuffer() ;
		try {
			 scanner = new Scanner(new File(savePath)) ;
			 if(scanner.hasNextLine()){
				buffer.append(scanner.nextLine()) ;
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace()  ;
		}finally{
			if(scanner != null)
				scanner.close() ; 
		}
		return JSONObject.fromObject(buffer.toString()) ;
	}
}
