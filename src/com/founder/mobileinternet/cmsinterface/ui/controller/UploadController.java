package com.founder.mobileinternet.cmsinterface.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.upload.UploadFactories;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;


@XYComment(name="上传")
@Controller
public class UploadController {
    @Autowired
    private Configure configure;

    @Autowired
	private RedisManager redisManager;

    /**
     * 用于上传图片
     * 两种方式
     * 1. 手机上传文件到appif上，由appif转发给 api。在api端做保存
     * 2. 当location=1时，在appif端做处理
     */
    @XYComment(name="上传")
    @RequestMapping({"upload"})
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response,String fileType, String userId) {
    	String origin = request.getHeader("origin");
	    if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
	    	response.addHeader("Access-Control-Allow-Origin",origin);
	    }
	    
        response.setHeader("Access-Control-Allow-Method", "GET, POST"); 
     	
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String result = null;
        if (isMultipart) {
            try {
                //获得上传的文件
                ServletFileUpload upload = UploadFactories.getFileUpload(userId);
                @SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(request);

                if ("1".equals(request.getParameter("location"))) {
                    //(GuZhaoming)桓台项目：向内网传很慢，改为外网直接保存
                    result = saveFileToDisk(fileType, items);
                } else {
                    result = forwardToAPI(fileType, items);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject json = new JSONObject();
                json.put("errorInfo", e.getMessage());
                json.put("success", false);
                result = json.toString();
            }
        }
        return result;
    }

    @XYComment(name="移动采编图片上传")
    @RequestMapping(value = "uploadImage")
    @ResponseBody
    public void uploadImage(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String fileType,

            HttpServletRequest request, HttpServletResponse response) {
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("fileType------>"+fileType);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);

        if(validResult){
            //跨域处理
            String origin = request.getHeader("origin");
            if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
                response.addHeader("Access-Control-Allow-Origin",origin);
            }

            response.setHeader("Access-Control-Allow-Method", "GET, POST");

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            String info = null;
            if (isMultipart) {
                try {
                    //获得上传的文件
                    ServletFileUpload upload = UploadFactories.getFileUpload(String.valueOf(userID));
                    @SuppressWarnings("unchecked")
                    List<FileItem> items = upload.parseRequest(request);

                    if ("1".equals(request.getParameter("location"))) {
                        //如果向内网传很慢，改为外网直接保存
                        info = saveFileToDisk(fileType, items);
                    } else {
                        info = forwardToNewAPI(fileType, items);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JSONObject json = new JSONObject();
                    json.put("success", false);
                    json.put("errorInfo", "上传失败:"+e.getMessage());
                    json.put("fileList", "[]");
                    info = json.toString();
                }
            }

            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    private String forwardToNewAPI(String fileType, List<FileItem> items) throws IOException, URISyntaxException {
        //把文件的流打包成 Entity
        MultipartEntity entity = getMultipartEntity(items);
        //发送给 api, 并获得结果
        String url = configure.getNewAppServerUrl() + "/uploadImage.do?fileType=" + fileType;
        //发送entity 给 api
        return sendFiles(entity, url);
    }

    private String forwardToAPI(String fileType, List<FileItem> items) throws IOException, URISyntaxException {
        //把文件的流打包成 Entity
        MultipartEntity entity = getMultipartEntity(items);
        //发送给 api, 并获得结果
        String url = configure.getAppServerUrl() + "/upload.do?fileType=" + fileType;
        //发送entity 给 api
        return sendFiles(entity, url);
    }

    /**
     * 把文件打包成 Entity对象以用于发送给api
     *
     * @param items
     * @return
     * @throws IOException TODO: getContentLength 始终为-1，是否考虑使用替代方案
     */
    private MultipartEntity getMultipartEntity(List<FileItem> items) throws IOException {
        Iterator<FileItem> iter = items.iterator();
        MultipartEntity entity = new MultipartEntity();
        int i = 1;
        while (iter.hasNext()) {
            FileItem item = iter.next();
            if (!item.isFormField() && item.getSize() > 0) {
                entity.addPart("file" + i++, new InputStreamBody(item.getInputStream(), item.getName()));
            }
        }
        return entity;
    }

    /**
     * 发送给api并获得返回结果
     *
     * @param entity
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private String sendFiles(MultipartEntity entity, String url) throws URISyntaxException, IOException {
        String result = null;

        ThreadSafeClientConnManager manager = UploadFactories.getConnManager();
        HttpClient httpClient = new DefaultHttpClient(manager);

        HttpPost post = new HttpPost();
        post.setEntity(entity);
        post.setURI(new URI(url));

        HttpResponse response = httpClient.execute(post);
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity, HTTP.UTF_8);
            }
        }
        return result;
    }

    /**
     * 做四件事
     * 1. 获得文件的存储路径
     * 2. 存储
     * 3. 组织json
     * 4. 为抽图做准备
     *
     * @param items
     * @throws Exception
     */
    private String saveFileToDisk(String fileType, List<FileItem> items) throws Exception {
        //1. 获得路径
        String pathInfoStr = getFilePaths();
        if (pathInfoStr == null) {
            pathInfoStr = getFilePaths();
            if (pathInfoStr == null) {
                throw new JSONException("与api服务器连接不通或者无法从redis中获得 app.upload.pathinfo 这个key！");
            }
        }

        //2. 存储 3. 组织json 4. 为抽图做准备
        String result = processFiles(fileType, items, pathInfoStr);
        JSONObject resultJson = new JSONObject();
        resultJson.put("success", true);
        resultJson.put("errorInfo", "");
        resultJson.put("fileList", result);

        return resultJson.toString();
    }

    /**
     * 1. 存储
     * 2. 组织json
     *
     * @param fileType
     * @param items
     * @param pathInfoStr
     * @return
     */
    private String processFiles(String fileType, List<FileItem> items, String pathInfoStr) throws Exception {
        JSONArray resultArray = new JSONArray();
        // 路径相应的信息
        JSONObject pathInfo = JSONObject.fromObject(pathInfoStr);
        // 修改json，获得当前的路径
        resetPathInfo(pathInfo);

        // 遍历文件
        Iterator<FileItem> it = items.iterator();
        while (it.hasNext()) {
            // 获得文件名
            FileItem item = it.next();
            String realName = item.getName();
            String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(realName);

            // 1. 存储
            saveFile(pathInfo.getString("folderPath"), item, fileName);

            // 2. 组织json
            addJsonToArray(resultArray, pathInfo.getString("webPath"), realName, fileName);

            //3 生成 抽图相关的文件夹
            createExtractFiles(pathInfo.getString("extractPath"), pathInfo.getString("extractName"), fileName);
        }

        return resultArray.toString();
    }

    private void saveFile(String folderPath, FileItem item, String fileName) throws Exception {
        if (folderPath == null) {
            throw new JSONException("folderPath does not found in pathInfo");
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 1. 存储
        item.write(new File(folderPath + fileName));
    }

    private void createExtractFiles(String extractPath, String extractName, String fileName) throws IOException {
        if (!"0".equals(extractPath)) {
            // 生成抽图信息文件
            File folder = new File(extractPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(extractPath + extractName + fileName);
            file.createNewFile();
        }
    }

    private void addJsonToArray(JSONArray resultArray, String webPath, String realName, String fileName) {
        JSONObject json;
        json = new JSONObject();
        json.put("url", webPath + fileName);
        json.put("fileName", realName);
        resultArray.add(json);
    }

    /**
     * 重新组织路径，获得当前路径
     *
     * @param pathInfo
     * @return
     */
    private void resetPathInfo(JSONObject pathInfo) {
        String folderPath = pathInfo.getString("folderPath");
        String webPath = pathInfo.getString("webPath");
        String extractPath = pathInfo.getString("extractPath");
        // 获得目录
        Date date = Calendar.getInstance().getTime();
        //子目录
        String subFolder = "/" + new SimpleDateFormat("yyyyMM/dd/").format(date);
        String extractName = new SimpleDateFormat("yyyyMM~dd~").format(date);
        pathInfo.put("folderPath", folderPath + subFolder);
        pathInfo.put("webPath", webPath + subFolder);
        pathInfo.put("extractPath", extractPath);
        pathInfo.put("extractName", extractName);
    }

    /**
     * 获得文件的存储路径
     * 1. 到redis里面去读
     * 2. 如果redis没有，访问api - api把路径信息放到redis里，并且返回pathInfo
     *
     * @return
     */
    private String getFilePaths() {
        //获得json value
        String value = redisManager.get(RedisKey.APP_UPLOAD_PATH);
        if (value == null) {
            String url = configure.getAppServerUrl() + "/getPathInfo.do";
            if (CommonToolUtil.canGetData(url))
                value = redisManager.get(RedisKey.APP_UPLOAD_PATH);
        }

        return value;
    }

}
