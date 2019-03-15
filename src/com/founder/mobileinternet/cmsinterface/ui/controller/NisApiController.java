package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.EventInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.FavoriteInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.FeedInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.TipoffInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.VoteInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.NisService;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="互动其它接口")
@Controller
public class NisApiController {
	@Autowired
	private NisService nisService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private RedisManager redisManager;

    @XYComment(name="提交互动事件")
	@RequestMapping(value = "event")
	@ResponseBody
	public void event(
	        EventInfo eventInfo, HttpServletRequest request,
	        HttpServletResponse response) {
	    boolean /*info = redisManager.isNotIntercept("app.timer.event." + request.getRemoteAddr(),
	            configure.getTimer_event());
		if(info) */info = nisService.event(eventInfo);

		CommonToolUtil.outputJson(request, response, info+"");
	}

	@XYComment(name="读小红点状态")
    @RequestMapping(value = "redDot")
    @ResponseBody
    public void redDot(HttpServletRequest request, HttpServletResponse response,
            Integer userID) {
        String info = nisService.redDot(userID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="小红点消除")
    @RequestMapping(value = "read")
    @ResponseBody
    public void read(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value="userID",required = true) int userID,int type) {
        boolean result = nisService.read(userID,type);
        CommonToolUtil.outputJson(request,response, String.valueOf(result));
    }

	@XYComment(name="意见反馈")
	@RequestMapping(value = "feed", method = {RequestMethod.POST})
	@ResponseBody
	public void feedback(FeedInfo feedInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if (!info) {
			CommonToolUtil.outputJson(request,response, "false");
			return;
		}
		try {
			String value = new ObjectMapper().writeValueAsString(feedInfo);
			redisManager.addDelayFeedback(value);
			CommonToolUtil.outputJson(response, true);
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(response, false);
		}
	}
	
	@XYComment(name="报料")
	@RequestMapping(value = "tipoff", method = { RequestMethod.POST })
	@ResponseBody
	public void tipoff(TipoffInfo forumInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if (!info) {
			CommonToolUtil.outputJson(response, false);
			return;
		}
		try {
			String value = new ObjectMapper().writeValueAsString(forumInfo);
			redisManager.addDelayTipoff(value);
			CommonToolUtil.outputJson(response, true);
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(response, false);
		}
	}

	@XYComment(name="报料查看")
	@RequestMapping(value = "myTipoff")
	@ResponseBody
	public void myTipoff(@RequestParam("userID") int userID,
			@RequestParam("page") int page,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = nisService.myTipoff(userID, page, siteID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="报料详情")
	@RequestMapping(value="tipoffContent")
	@ResponseBody
	public void tipoffContent(@RequestParam("docID") int docID,
							  @RequestParam("siteID") int siteID,
	                   HttpServletRequest request, HttpServletResponse response){
		String info = nisService.tipoffContent(docID, siteID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="投票")
	@RequestMapping(value = "vote")
	@ResponseBody
	public void vote(VoteInfo voteInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		String returnInfo;
		if(info) {
			returnInfo = voteService.commitVote(voteInfo);
		}else{
			JSONObject json = new JSONObject();
			json.put("result", "false");
			json.put("failureInfo", "您点太快了哦！");
			returnInfo = json.toString();
		}
		CommonToolUtil.outputJson(request,response, returnInfo);
	}

	@XYComment(name="投票数")
	@RequestMapping(value = "voteCount")
	@ResponseBody
	public void voteCount(@RequestParam("id") int id,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = voteService.voteCount(id, siteID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="投票结果")
	@RequestMapping(value = "voteResult")
	@ResponseBody
	public void voteResult(@RequestParam("id") int id,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = voteService.voteResult(id, siteID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name = "收藏", comment = "提交用户收藏接口")
	@RequestMapping(value = "fav")
	@ResponseBody
	public void fav(FavoriteInfo favoriteInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if (!info) {
			CommonToolUtil.outputJson(request,response, "false");
			return;
		}

		favoriteInfo.setCancel(0);
		try {
			String value = new ObjectMapper().writeValueAsString(favoriteInfo);
			redisManager.addDelayFavorite(value);
			CommonToolUtil.outputJson(request, response, "true");
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(request, response, "false");
		}
	}

	@XYComment(name = "取消收藏", comment = "取消用户收藏接口")
	@RequestMapping(value = "favCancel")
	@ResponseBody
	public void favCancel(FavoriteInfo favoriteInfo,
			HttpServletRequest request, HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if (!info) {
			CommonToolUtil.outputJson(request, response, "false");
			return;
		}
		
		favoriteInfo.setCancel(1);
		try {
			String value = new ObjectMapper().writeValueAsString(favoriteInfo);
			redisManager.addDelayFavorite(value);
			CommonToolUtil.outputJson(request, response, "true");
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(request, response, "false");
		}
	}

	@XYComment(name = "我的收藏", comment = "根据用户ID获取我的收藏列表")
	@RequestMapping(value = "myFav")
	@ResponseBody
	public void myFav(@RequestParam("userID") int userID,
			@RequestParam("page") int page,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			HttpServletRequest request,HttpServletResponse response) {
		String info = nisService.getMyFav(userID, page, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name = "是否已收藏", comment = "根据用户ID检查是否已收藏过")
	@RequestMapping(value = "hasFav")
	@ResponseBody
	public void hasFav(@RequestParam("userID") int userID,
			@RequestParam("articleID") long articleID,
			@RequestParam("type") int type,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		boolean info = nisService.hasFav(userID, articleID, type, siteID);
		CommonToolUtil.outputJson(request, response, info + "");
	}

	@XYComment(name = "获取互动计数")
	@RequestMapping(value = "counts")
	@ResponseBody
	public void counts(@RequestParam int id,
			@RequestParam(defaultValue = "0") int source,
			@RequestParam(defaultValue = "1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = nisService.getCounts(id, source, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}
}
