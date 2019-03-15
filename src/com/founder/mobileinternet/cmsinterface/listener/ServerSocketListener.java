package com.founder.mobileinternet.cmsinterface.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;

public class ServerSocketListener implements ServletContextListener {
	
    private Timer timer; //定时器  
	private SocketThread socketThread;
	
    @Override
    public void contextInitialized(ServletContextEvent e) {
        final ServletContext servletContext = e.getServletContext();
        timer = new Timer();
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (socketThread == null) {
		            socketThread = new SocketThread(servletContext);
		            socketThread.start();
		        }
			}
		}, 30000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	if (socketThread != null && socketThread.isInterrupted()) {
            socketThread.interrupt();
        }
    	timer.cancel(); //定时器销毁  
    }

    class SocketThread extends Thread{
        private FileServer server = null;
        public SocketThread(ServletContext servletContext) {
            ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            Configure configure = (Configure) context.getBean("configure");
            HttpPostTool.initVideoSocketParams(configure);
            boolean isStart = HttpPostTool.isSocketStart();
            if (isStart) {
                server = new FileServer(configure);
            }
        }
        public void run() {
            try {
                if(server!=null){
                	System.out.println("upload large file serverSocket start!");
                    server.start();
                }else{
                	System.err.println("upload large file serverSocket error!");
                }
            } catch (Exception ex) {
                System.out.println("SocketThread err:"+ex.getMessage());
            }
        }
    }
}
