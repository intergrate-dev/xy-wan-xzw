package com.founder.mobileinternet.cmsinterface.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.cms.ArticleService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="稿件")
@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;

    /*
    增加稿件筛选条件：默认0-不筛选，1-微信小程序稿件类型筛选
     */
    @XYComment(name="栏目稿件列表", comment="读栏目的稿件列表")
    @RequestMapping(value = "getArticles")
    @ResponseBody
    public void getArticles(
            int columnId,
            @RequestParam(required = false) Integer page,
            @XYComment(name="列表开始位置", comment="旧参数，改用page")
            @RequestParam(value = "rowNumber", required = false) Integer start,
            
            @RequestParam(defaultValue = "0") int version,
            @RequestParam(defaultValue = "1") int siteId,
            @RequestParam(defaultValue = "0") int lastFileId,
            @RequestParam(defaultValue = "0") int typeScreen,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonParseException, JsonMappingException, IOException {
    	
    	System.out.println("url---->"+request.getServletPath());
    	System.out.println("columnId---->" + columnId);
    	System.out.println("page---->" + page);
    	System.out.println("start---->" + start);
    	System.out.println("version---->" + version);
    	System.out.println("siteId---->" + siteId);
    	System.out.println("lastFileId---->" + lastFileId);
    	System.out.println("typeScreen---->" + typeScreen);
    	
    	
        page = CommonToolUtil.getPage(start, page);
        int count = CommonToolUtil.ALIST_COUNT;
        start = page * count;
        
        String info = articleService.getColumnArticles(columnId, 
        		start, count, lastFileId, siteId,typeScreen);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
        CommonToolUtil.outputJson(request, response, info);
    }


    @XYComment(name="热点新闻")
	@RequestMapping(value = "articleHot")
    @ResponseBody
    public void articleHot(
            @XYComment(name="类型", comment="时间范围，默认是24小时内，1是48小时内，2是最近三天，3是最近7天")
            @RequestParam(value = "type", defaultValue = "0") int type,
            @RequestParam(value = "siteId", defaultValue = "1") int siteID,
            @RequestParam(value = "lastFileId", defaultValue = "0") int lastFileId,
            @RequestParam(value = "rowNumber", defaultValue = "0") int start,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String info = articleService.articleHot(siteID, lastFileId, start, type);
        CommonToolUtil.outputJson(request,response, info);
    }

    @XYComment(name="稿件详情")
    @RequestMapping(value = "getArticleContent")
    @ResponseBody
    public void getArticleContent(
            @RequestParam("articleId") int articleId,
            @RequestParam(value = "siteId", defaultValue = "1") int siteID,
            HttpServletRequest request, HttpServletResponse response) {
        int colID = CommonToolUtil.getIntValue(request.getParameter("colID"), 0);
        String info = articleService.getArticleContent(articleId, colID, siteID);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
        CommonToolUtil.outputJson(request, response, info);
    }

	@XYComment(name = "离线API", comment = "离线普通新闻栏目的稿件")
	@RequestMapping(value = "offline")
	@ResponseBody
	public void offline(
	        @RequestParam("columnId") int columnId,
	        @RequestParam(value = "count", defaultValue = "50") int count,
	        @RequestParam(value = "siteId", defaultValue = "1") int siteID,
	        HttpServletRequest request, HttpServletResponse response)
	        throws IOException {
	    String info = articleService.getColumnArticles(columnId, 0, count, 0, siteID,0);
	    CommonToolUtil.outputJson(request, response, info);
	}


	@XYComment(name="稿件检索")
    @RequestMapping(value = "search")
    @ResponseBody
    public void search(
            @RequestParam("columnId") int columnId,
            @RequestParam("key") String key, @RequestParam("start") int start,
            @RequestParam("count") int count,
            @RequestParam(value = "siteId", defaultValue = "1") int siteID,
            HttpServletRequest request, HttpServletResponse response) {
        String info = articleService.search(columnId, key, start, count, siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @RequestMapping(value = "searchAll")
    @ResponseBody
    public void searchAll(
            @RequestParam("columnId") int columnId,
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @RequestParam("key") String key, @RequestParam("start") int start,
            @RequestParam("count") int count, HttpServletRequest request,
            HttpServletResponse response) {
        String info = articleService.searchAll(columnId, key, start, count, siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @RequestMapping(value = "searchWebArticles")
    @ResponseBody
    public void searchWebArticles(
            @RequestParam("columnId") int columnId,
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @RequestParam("key") String key, @RequestParam("start") int start,
            @RequestParam("count") int count, HttpServletRequest request,
            HttpServletResponse response) {
        String info = articleService.searchWebArticles(columnId, key, start, count, siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @XYComment(name="记者稿件列表")
    @RequestMapping(value = {"authorArticles"})
    @ResponseBody
    public void getAuthorArticles(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("id") int id, @RequestParam("start") int start,
            @RequestParam(value = "siteId", defaultValue = "1") int siteID,
            @RequestParam(value = "lastFileId", defaultValue = "0") int lastFileId,
            @RequestParam("count") int count) {
        if (count < 20) count = 20;
        String info = articleService.getAuthorArticles(id, start, count, lastFileId, siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @RequestMapping(value = "articleListRefresh")
    @ResponseBody
    public void articleListRefresh(
            @RequestParam(value = "siteId", defaultValue = "1") int siteID,
            @RequestParam(value = "coID", defaultValue = "0") int coID,
            @RequestParam(value = "colLibID", defaultValue = "0") int colLibID,
            @RequestParam(value = "colID", defaultValue = "0") long colID,
            @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletRequest request, HttpServletResponse response) {

        String info = articleService.articleListRefresh(coID, colLibID, colID, page, siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="标签稿件列表")
    @RequestMapping(value = "tagArticles")
    @ResponseBody
    public void tagArticles(
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @RequestParam(value = "tag") String tag,
            @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletRequest request, HttpServletResponse response) {

        String info = articleService.tagArticles(tag, page, siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="行业分类稿件列表")
    @RequestMapping(value = "tradeArticles")
    @ResponseBody
    public void tradeArticles(
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @RequestParam(value = "tradeIDs") String tradeIDs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletRequest request, HttpServletResponse response) {

        String info = articleService.tradeArticles(tradeIDs, page, siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="记者稿件列表", comment="读记者的稿件列表")
    @RequestMapping(value = "authorArticlesPC")
    @ResponseBody
    public void authorArticles(
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @RequestParam(value = "author") String author,
            @RequestParam(value = "page", defaultValue = "0") int page,
            HttpServletRequest request, HttpServletResponse response) {

        String info = articleService.authorArticles(author, page, siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="推荐模块内容列表")
    @RequestMapping(value = "moduleView")
    @ResponseBody
    public void moduleView(
            HttpServletRequest request, HttpServletResponse response, 
            @XYComment(name="栏目ID")
            @RequestParam String id, 
            @RequestParam(defaultValue = "0") int userID ) {
        String info = articleService.getModuleView(id, userID);
        CommonToolUtil.outputJson(request, response, info);
    }
    
    @XYComment(name="会员收藏稿件列表", comment="会员传过来的稿件ID获取列表信息")
    @RequestMapping(value = "myCollectionList")
    @ResponseBody
    public void myCollectionList(
            HttpServletRequest request, HttpServletResponse response, 
            @RequestParam(required = true) String articleIDs,
            @RequestParam(defaultValue = "1") int ch) {
        String info = articleService.myCollectionList(articleIDs, ch);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="移动采编发布库稿件列表")
    @RequestMapping(value = "getPubArticles")
    @ResponseBody
    public void getPubArticles(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.getPubArticles(userID,data);
            CommonToolUtil.outputJson(request,response,info);
//            request.getHeaderNames();
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编投稿")
    @RequestMapping(value = "addArticle")
    @ResponseBody
    public void addArticle(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.addArticle(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编删除稿件")
    @RequestMapping(value = "delete")
    @ResponseBody
    public void delete(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.delete(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编稿件详情接口")
    @RequestMapping(value = "getArticleDetail")
    @ResponseBody
    public void getArticleDetail(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.getArticleDetail(data, userID);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

    @XYComment(name="移动采编获取图片流接口")
    @RequestMapping(value = "getImage")
    @ResponseBody
    public void getImage(HttpServletRequest request, HttpServletResponse response){

        System.out.println("url ------>"+request.getServletPath());
        System.out.println("path------>"+request.getQueryString());
        InputStream image = articleService.getImage(request.getQueryString());
        OutputStream out = null;
        try {
            //显示图片的header
            String CONTENT_TYPE = "image/jpeg; charset=UTF-8";
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            out = response.getOutputStream();
            IOUtils.copy(image, out);
        } catch (Exception e) {
        } finally {
            try {
                image.close();
                out.close();
            } catch (IOException e) {
            }
        }

    }

    @XYComment(name="移动采编获取主界面接口")
    @RequestMapping(value = "getTabs")
    @ResponseBody
    public void getTabs(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.getTabs(userID,data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编稿件检索接口")
    @RequestMapping(value = "pubSearch")
    @ResponseBody
    public void pubSearch(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.pubSearch(data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

    @XYComment(name="移动采编撤稿中心检索接口")
    @RequestMapping(value = "revokeSearch")
    @ResponseBody
    public void revokeSearch(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.revokeSearch(data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="稿件流程记录接口")
    @RequestMapping(value = "flowRecordList")
    @ResponseBody
    public void flowRecordList(
    		@RequestParam int loginID,
    		@RequestParam int userID,
    		@RequestParam long time,
    		@RequestParam String sign,
    		@RequestParam String data,
    		HttpServletRequest request, HttpServletResponse response){
    	
    	System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
    	System.out.println("userID---->"+userID);
    	System.out.println("time------>"+time);
    	System.out.println("sign------>"+sign);
    	System.out.println("data------>"+data);
    	
    	
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
    	    String info = articleService.flowRecordList(data);
    	    CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }
    	
    }
    
    @XYComment(name="稿件推送客户端接口")
    @RequestMapping(value = "pushApp")
    @ResponseBody
    public void pushApp(
    		@RequestParam int loginID,
    		@RequestParam int userID,
    		@RequestParam long time,
    		@RequestParam String sign,
    		@RequestParam String data,
    		HttpServletRequest request, HttpServletResponse response){
    	
    	System.out.println("url------->"+request.getServletPath());
    	System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
    	System.out.println("time------>"+time);
    	System.out.println("sign------>"+sign);
    	System.out.println("data------>"+data);
    	
    	
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
    	    String info = articleService.pushApp(data, userID);
    	    CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }
    	
    }
    
    @XYComment(name="审核全部稿件列表接口")
    @RequestMapping(value = "getAuditArticles")
    @ResponseBody
    public void getAuditArticles(
    		@RequestParam int loginID,
    		@RequestParam int userID,
    		@RequestParam long time,
    		@RequestParam String sign,
    		@RequestParam String data,
    		HttpServletRequest request, HttpServletResponse response){
    	
    	System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
    	System.out.println("userID---->"+userID);
    	System.out.println("time------>"+time);
    	System.out.println("sign------>"+sign);
    	System.out.println("data------>"+data);
    	
    	
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
    	    String info = articleService.getAuditArticles(data, userID);
    	    CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }
    }

    @XYComment(name="移动采编稿件发布接口")
    @RequestMapping(value = "publishArticle")
    @ResponseBody
    public void publishArticle(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.publishArticle(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

    @XYComment(name="移动采编稿件撤稿接口")
    @RequestMapping(value = "revokeArticle")
    @ResponseBody
    public void revokeArticle(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.revokeArticle(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

    @XYComment(name="移动采编稿件审核通过接口")
    @RequestMapping(value = "transfer")
    @ResponseBody
    public void transfer(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.transfer(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="移动采编稿件审核驳回接口")
    @RequestMapping(value = "reject")
    @ResponseBody
    public void reject(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.reject(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

    @XYComment(name="移动采编彻底删除稿件接口")
    @RequestMapping(value = "revokeDelete")
    @ResponseBody
    public void revokeDelete(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);


        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = articleService.revokeDelete(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="热门栏目稿件")
    @RequestMapping(value = "newArticleHotList")
    @ResponseBody
    public void newArticleHotList(
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @XYComment(name="查询稿件类型", comment="1热门新闻，2热门百格视频")
            @RequestParam(value = "articleType", defaultValue = "1") int articleType,
            @XYComment(name="查询时间类型", comment="1过去6小时，2过去24小时，3过去3天")
            @RequestParam(value = "timeType", defaultValue = "1") int timeType,
            @XYComment(name="排序类型", comment="1点击量降序，2留言数降序，3分享数降序")
            @RequestParam(value = "orderType", defaultValue = "1") int orderType,
            @XYComment(name="稿件类别", comment="0Web稿件,1App稿件")
            @RequestParam(value = "channel", defaultValue = "1") int channel,
            HttpServletRequest request, HttpServletResponse response) {

        String info = articleService.getNewArticleHotList(siteID, articleType, timeType, orderType, channel);
        CommonToolUtil.outputJson(request,response, info);
    }
    
    @XYComment(name="子栏目稿件列表", comment="读每个子栏目的稿件列表的第一篇")
    @RequestMapping(value = "getSubColArticles")
    @ResponseBody
    public void getSubColArticles(
            int columnId,
            @RequestParam(defaultValue = "1") int siteId,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonParseException, JsonMappingException, IOException {
        String info = articleService.getSubColArticles(columnId, siteId);

        CommonToolUtil.outputJson(request, response, info);
    }
    
    @XYComment(name="话题稿件列表", comment="读话题的稿件列表")
    @RequestMapping(value = "getTopicArticles")
    @ResponseBody
    public void getTopicArticles(
            int topicId,
            @RequestParam(required = false) Integer page,
            @XYComment(name="列表开始位置", comment="旧参数，改用page")
            @RequestParam(value = "rowNumber", required = false) Integer start,
            
            @RequestParam(defaultValue = "0") int version,
            @RequestParam(defaultValue = "1") int siteId,
            @RequestParam(defaultValue = "0") int lastFileId,
            @RequestParam(defaultValue = "100") int type,
            @RequestParam(defaultValue = "1") int channel,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonParseException, JsonMappingException, IOException {
        page = CommonToolUtil.getPage(start, page);
        int count = CommonToolUtil.ALIST_COUNT;
        start = page * count;
        
        String info = articleService.getTopicArticles(topicId, 
        		start, count, lastFileId, siteId,type,channel);

        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="获取稿件点击量、分享量、留言量的接口")
    @RequestMapping(value = "getArticleCountInfo")
    @ResponseBody
    public void getArticleCountInfo(@RequestParam long aid,@RequestParam long cid,
    		HttpServletRequest request, HttpServletResponse response) {
    	String info = articleService.getArticleCountInfo(aid,cid);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="百格视频接口")
    @RequestMapping(value = "addArticleNew")
    @ResponseBody
    public void addArticleForBig(
            @RequestParam int eid,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("eid------>"+eid);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        String authResult = CommonToolUtil.checkAuth(eid,time,data,sign,redisManager,configure);
        if(authResult!=null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success",false);
            jsonObject.put("message",authResult);

            CommonToolUtil.outputJson(request,response, jsonObject.toString());
        }else{
            String info = articleService.addArticleForBig(data);

            CommonToolUtil.outputJson(request,response, info);
        }

    }
    
    @XYComment(name="百格视频删除接口")
    @RequestMapping(value = "deleteArticleNew")
    @ResponseBody
    public void deleteArticleForBig(
            @RequestParam int eid,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("eid------>"+eid);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        String authResult = CommonToolUtil.checkAuth(eid,time,data,sign,redisManager,configure);
        if(authResult!=null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success",false);
            jsonObject.put("message",authResult);

            CommonToolUtil.outputJson(request,response, jsonObject.toString());
        }else{
            String info = articleService.deleteArticleForBig(data);

            CommonToolUtil.outputJson(request,response, info);
        }

    }
}