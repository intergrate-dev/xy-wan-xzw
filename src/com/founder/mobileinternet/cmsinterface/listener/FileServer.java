package com.founder.mobileinternet.cmsinterface.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.common.util.string.StringUtils;

public class FileServer {
	private ExecutorService executorService;//线程池  
	private Configure configure;
	private int port; //监听端口  
    private boolean quit = false;//退出  
    private ServerSocket server;  
    private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();//存放断点数据  
      
    public FileServer(Configure configure){  
        this.configure = configure;  
        this.port = HttpPostTool.getSocketPort();
        //创建线程池，池中具有(cpu个数*50)条线程  
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 50);  
    }  
    /** 
     * 退出 
     */  
    public void quit(){  
       this.quit = true;  
       try {  
           server.close();  
       } catch (IOException e) {  
       }  
    }  
    /** 
     * 启动服务 
     * @throws Exception 
     */  
    public void start() throws Exception{  
        server = new ServerSocket(port);  
        while(!quit){  
            try {  
              Socket socket = server.accept();  
              //为支持多用户并发访问，采用线程池管理每一个用户的连接请求  
              executorService.execute(new SocketTask(socket));  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    private final class SocketTask implements Runnable{  
       
       private Socket socket = null;  
       public SocketTask(Socket socket) {  
           this.socket = socket;  
       }  
         
       public void run() {  
    	   PushbackInputStream inStream = null;
    	   RandomAccessFile fileOutStream = null;
    	   OutputStream outStream = null;
    	   File file = null;  
           try {  
               System.out.println("accepted connection "+ socket.getInetAddress()+ ":"+ socket.getPort());
               inStream = new PushbackInputStream(socket.getInputStream());
//               //得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=
//               //如果用户初次上传文件，sourceid的值为空。
               String head = readLine(inStream);//存在中文名称乱码
               if(head.equals(new String(head.getBytes("iso8859-1"), "iso8859-1")))
                   head = new String(head.getBytes("iso8859-1"),"utf-8");
               if(null!=head){
            	   System.out.println("开始解析文件头部！");
            	   Map<String,String> items = StringUtils.splitToMap(head, "&", "=");
                   //下面从协议数据中提取各项参数值  
            	   String filename = items.get("filename");
                   String filelength = items.get("Content-Length");  
                   items.remove("Content-Length");
                   String sourceid = items.get("sourceid");;
                   items.remove("sourceid");
                   int loginID = Integer.parseInt(items.get("loginID"));
                   items.remove("loginID");
                   int userID = Integer.parseInt(items.get("userID"));
                   items.remove("userID");
                   long time = Long.parseLong(items.get("time"));
                   items.remove("time");
                   String sign = items.get("sign");
                   items.remove("sign");
                   boolean isAudio = "audio".equals(items.get("flag")); 
                   items.remove("flag");
                   System.out.println("开始验证token！");
                   //校验token
//                   if(!TokenHelper.validToken(userid,token)){
//                       OutputStream outStream1 = socket.getOutputStream();
//                       String response = "error=token校验失败";
//                       outStream1.write(response.getBytes());
//                       outStream1.close();
//                       //强制退出
//                       System.out.println("token校验失败！");
//                       socket.close();
//                       return;
//                   }
                   System.out.println("token校验成功！"+socket.isClosed());
                  
                   long id = System.currentTimeMillis();//生产资源id，如果需要唯一性，可以采用UUID  
                   FileLog log = null;  
                   if(null != sourceid && !"".equals(sourceid)){  
                	   System.out.println("根据sourceid读取保存记录");
                       id = Long.valueOf(sourceid);  
                       log = find(id);//查找上传的文件是否存在上传记录  
                   }  
                
                   int position = 0;  
                   if(log==null){//如果不存在上传记录,为文件添加跟踪记录  
                	   System.out.println("第一次上传该文件："+filename);
                       String path = new SimpleDateFormat("yyyyMM/dd/HHmmss").format(new Date());  
                       File dir = new File(getPath()+ path);  
                       if(!dir.exists()) dir.mkdirs();  
                       file = new File(dir, filename);  
                       if(file.exists()){//如果上传的文件发生重名，然后进行改名  
                           filename = filename.substring(0, filename.indexOf(".")-1)+ dir.listFiles().length+ filename.substring(filename.indexOf("."));  
                           file = new File(dir, filename);  
                       }  
                       save(id, file);  
                       System.out.println("sourceid保存成功");
                   }else{// 如果存在上传记录,读取已经上传的数据长度  
                	   System.out.println("已经存在上传记录");
                       file = new File(log.getPath());//从上传记录中得到文件的路径  
                       if(file.exists()){  
                           File logFile = new File(file.getParentFile(), file.getName()+".log");  
                           if(logFile.exists()){  
                               Properties properties = new Properties();  
                               FileInputStream fileinput = new FileInputStream(logFile);
                               properties.load(fileinput);  
                               position = Integer.valueOf(properties.getProperty("length"));//读取已经上传的数据长度  
                               fileinput.close();
                           }  
                       }  
                   }  
                     
                   outStream = socket.getOutputStream();  
                   String response = "sourceid=" + id + "&" + "position=" + position+ "\r\n";  
                   //服务器收到客户端的请求信息后，给客户端返回响应信息：sourceid=1274773833264;position=0  
                   //sourceid由服务器端生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
                   System.out.println("返回当前位置:"+response);
                   outStream.write(response.getBytes());  
                   outStream.flush();
                   
                   fileOutStream = new RandomAccessFile(file, "rwd");  
                   if(position==0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度  
                   System.out.println("更新记录里的长度位置："+position);
                   fileOutStream.seek(position);//指定从文件的特定位置开始写入数据  
                   byte[] buffer = new byte[1024*1024];  
                   int len = -1;  
                   int length = position;
                   if(length==fileOutStream.length()){
                	   System.out.println("上传完，删除保存记录");
            	       delete(id);
                   }
                   try{
	                   while( (len=inStream.read(buffer)) != -1){//从输入流中读取数据写入到文件中
	                	   System.out.println("开始写流，本次写入："+len);
	                       fileOutStream.write(buffer, 0, len);
	                       length += len;
	                       System.out.println("已写入："+length);
	                       if(length==Integer.parseInt(filelength))
	                    	  socket.shutdownInput();
	                   }
                   }catch(Exception e){
                	   e.printStackTrace();
                   }finally{
                	   Properties properties = new Properties();  
                       properties.put("length", String.valueOf(length));  
                       FileOutputStream logFile = new FileOutputStream(new File(file.getParentFile(), file.getName()+".log"));
                       logFile.flush();
                       properties.store(logFile, null);//实时记录已经接收的文件长度  
                       logFile.close();
                   }
                   
                   System.out.println("是否100%："+length==filelength+"："+length+"=="+filelength);
                   //TODO 上传完成，进行后续操作
                   if(length==fileOutStream.length()){
                	   System.out.println("已接收完毕，开始上传内网！！");
                	   delete(id);
                	   StringBuffer url = new StringBuffer();
                	   if(isAudio){
                		   url.append(configure.getNewAppServerUrl() + "/uploadAudio.do?userID="+userID);
                	   } else {
                		   url.append(configure.getNewAppServerUrl() + "/uploadVideo.do?userID="+userID);
                	   }
                	   Set<String> keySet = items.keySet();
                	   for(String key : keySet){
                		   url.append("&").append(key).append("=").append(items.get(key));
                	   }
                	   
                	   String ret = HttpPostTool.postStream(url.toString(), file);
                	   outStream.write(ret.getBytes());
                	   
                	   //删除本地原文件及日志文件
                	   file.delete();
                	   new File(file.getParentFile(), file.getName()+".log").delete();
                	   file.getParentFile().delete();
                	   
                   }
               }  
           } catch (Exception e) {  
               e.printStackTrace();  
           }finally{  
        	   try {
        		   if(fileOutStream != null)
                       fileOutStream.close();                    
                   if(outStream != null)
                       outStream.close();  
                   if(inStream != null)
					   inStream.close();
                   if (file != null && file.exists()){
                	   file.delete();
                	   file.getParentFile().delete();
                	   file = null;
                   }
                   if(socket!=null && !socket.isClosed()) 
                	   socket.close();  
				} catch (IOException e) {
					e.printStackTrace();
				}  
           }  
       }  
    }  
      
    public FileLog find(Long sourceid){  
        return datas.get(sourceid);  
    }  
    //保存上传记录  
    public void save(Long id, File saveFile){  
        //日后可以改成通过数据库存放  
        datas.put(id, new FileLog(id, saveFile.getAbsolutePath()));  
    }  
    //当文件上传完毕，删除记录  
    public void delete(long sourceid){  
        if(datas.containsKey(sourceid)) datas.remove(sourceid);  
    }  
      
    @SuppressWarnings("unused")
    private class FileLog{  
       private Long id;  
       private String path;  
	public Long getId() {  
           return id;  
       }  
       public void setId(Long id) {  
           this.id = id;  
       }  
       public String getPath() {  
           return path;  
       }  
       public void setPath(String path) {  
           this.path = path;  
       }  
       public FileLog(Long id, String path) {  
           this.id = id;  
           this.path = path;  
       }     
    } 
    
    private String getPath(){
    	String path = this.getClass().getResource("/").getPath();
    	path = path.replace("classes", "upload");
    	File file = new File(path);
    	if(!file.exists()) file.mkdir();
    	return path;
    }
 
    private String readLine(PushbackInputStream in) throws IOException {
		char buf[] = new char[128];
		int room = buf.length;
		int offset = 0;
		int c;
		loop: while (true) {
			switch (c = in.read()) {
			case -1:
			case '\n':
				break loop;
			case '\r':
				int c2 = in.read();
				if ((c2 != '\n') && (c2 != -1))
					in.unread(c2);
				break loop;
			default:
				if (--room < 0) {
					char[] lineBuffer = buf;
					buf = new char[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);

				}
				buf[offset++] = (char) c;
				break;
			}
		}
		if ((c == -1) && (offset == 0))
			return null;
		return String.copyValueOf(buf, 0, offset);
	}
}