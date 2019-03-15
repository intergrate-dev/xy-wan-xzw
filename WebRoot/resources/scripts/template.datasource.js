//最后一条评论的ID
var lastCommentId=0;

//获取参数
var customerId;//客户ID
var siteId;//站点ID
var articleId;//稿件ID
var paraString = location.pathname.split("/");
if(paraString.length > 3)
{
	customerId = paraString[paraString.length-3];
	siteId = paraString[paraString.length-2];
	articleId = paraString[paraString.length-1];
}
//是否至少有一条评论
var isHaveOneComment = false;
//所有已经读取的评论的条数
var allReadComment = 0;
//从服务端获取到的评论的总条数
var allComment = 0;

function showArticle(article)
{
	//处理title
	$(document).attr("title", article.title);
	var articleTitle = $("#articleTitle");
	if(articleTitle.length)
	{
		articleTitle.html(article.title);
	}
	else
	{
		//console.log("未找到描述标题 的标签,该标签id应该是 articleTitle");
	}
			
	//处理摘要
	var articleAttAbstract = $("#articleAttAbstract");
	if(articleAttAbstract.length)
	{
		articleAttAbstract.html(article.attAbstract);
	}
	else
	{
		//console.log("未找到描述 摘要信息 的标签,该标签id应该是 articleAttAbstract");
	}

	//处理发布时间
	var articlePublishtime = $("#articlePublishtime");
	if(articlePublishtime.length)
	{
		articlePublishtime.html(article.publishtime);
	}
	else
	{
		//console.log("未找到描述 发布时间 的标签,该标签id应该是 articlePublishtime");
	}
			
	//处理来源
	var articleSource = $("#articleSource");
	if(articleSource.length)
	{
		articleSource.html(article.source);
	}
	else
	{
		//console.log("未找到描述 稿件来源 的标签,该标签id应该是 articleSource");
	}
	
	//处理作者
	var articleAuthor = $("#articleAuthor");
	if(articleAuthor.length)
	{
		articleAuthor.html(article.author);
	}
	else
	{
		//console.log("未找到描述 稿件作者 的标签,该标签id应该是 articleAuthor");
	}
	
	//处理编辑
	var articleEditor = $("#articleEditor");
	if(articleEditor.length)
	{
		articleEditor.html(article.editor);
	}
	else
	{
		//console.log("未找到描述 稿件编辑 的标签,该标签id应该是 articleEditor");
	}
	
	//处理副标题
	var articleSubtitle = $("#articleSubtitle");
	if(articleSubtitle.length)
	{
		articleSubtitle.html(article.subtitle);
	}
	else
	{
		//console.log("未找到描述 稿件副标题 的标签,该标签id应该是 articleSubtitle");
	}
	
	//处理引题
	var articleIntrotitle = $("#articleIntrotitle");
	if(articleIntrotitle.length)
	{
		articleIntrotitle.html(article.introtitle);
	}
	else
	{
		//console.log("未找到描述 稿件引题 的标签,该标签id应该是 articleIntrotitle");
	}
	
	//处理正文
	var articleContent = $("#articleContent");
	if(articleContent.length)
	{
		//处理图片附件
		var articleImageTemplate = $("#articleImageTemplate");
		
		var newContent = article.content;
		
		if(articleImageTemplate.length)
		{
			$(article.images).each(function(i)
			{
				var imagesItem = article.images[i];
				var imgs = "";
				$(imagesItem.imagearray).each(function(j)
				{
					var img = imagesItem.imagearray[j];

					articleImageTemplate.find("#articleImg").attr("src", img.imageUrl);
					articleImageTemplate.find("#articleImg").attr("title", img.title==null ? "" : img.title);
					articleImageTemplate.find("#articleImgAbstract").html(img.fileAbstract==null ? "" : img.fileAbstract);

					imgs += articleImageTemplate.html();
				});
				newContent = newContent.replace(imagesItem.ref, imgs);
			});
		}
		else
		{
			//console.log("未找到描述 稿件图片模板 的标签,该标签id应该是 articleImageTemplate");
		}
		
		//处理视频附件
		var articleVideoTemplate = $("#articleVideoTemplate");
		
		if(articleVideoTemplate.length)
		{
			$(article.videos).each(function(i)
			{
				var videosItem = article.videos[i];
				var videos = "";
				$(videosItem.videoarray).each(function(j)
				{
					var video = videosItem.videoarray[j];

					articleVideoTemplate.find("#articleVideoHref").attr("href", video.videoUrl);
					articleVideoTemplate.find("#articleVideoImg").attr("title", video.title==null ? "" : video.title);
					articleVideoTemplate.find("#articleVideoImg").attr("src", video.imageUrl==null ? "" : video.imageUrl);
					articleVideoTemplate.find("#articleVideoAbstract").html(video.attAbstract==null ? "" : video.attAbstract);

					videos += articleVideoTemplate.html();
				});
				newContent = newContent.replace(videosItem.ref, videos);
			});
		}
		else
		{
			//console.log("未找到描述 稿件视频模板 的标签,该标签id应该是 articleVideoTemplate");
		}
		
		articleContent.html(newContent);
	}
	else
	{
		//console.log("未找到描述 稿件正文 的标签,该标签id应该是 articleContent");
	}
	//增加评论字数提示
	$("#submitComment").after("<div style='clear: both;font-size: 12px;padding: 4px 0 0 0;float:right' >400字以内,还可输入<span id='commentlengthspan' >400</span>个字符</div>");
	
	$("#commentInfo").keydown(function(){
	 	$("#commentlengthspan").html(400-$("#commentInfo").val().length);
	});
	
	$("#commentInfo").keyup(function(){
	 	$("#commentlengthspan").html(400-$("#commentInfo").val().length);
	});	
	$("#commentInfo").blur(function(){
		$("#commentlengthspan").html(400-$("#commentInfo").val().length);
	});
	$("#commentInfo").focus(function(){
		$("#commentlengthspan").html(400-$("#commentInfo").val().length);
	});	
	
	//判断是否显示评论信息
	var comment = $("#comment");
	if(comment.length == 0)
	{
		//console.log("未找到描述 评论信息 的标签,该标签id应该是 comment");
		return;
	}
	else
	{
		comment.show();
		
		getComment();
		
		$("#moreTopComment").click(function(event)
		{
			event.preventDefault();
			getComment();
		});
		
		//提交评论的url
		var postCommentUrl = "remoteProxy?targetUrl=" + encodeURIComponent(comment_if + "comment?articleId=" + articleId + "&articleTitle=" + article.title + "&deviceId=13149265&articleUrl=" + location.href + "&siteSource=sharelink&siteId=" + siteId + "&columnSource=NA&mobileType=browser&customerId=" + customerId);

		$("#submitComment").click(function()
		{
			if($("#userName").val() == "")
			{
				alert("请输入昵称");
				$("#userName").focus();
				return;
			}
			if($("#commentInfo").val() == "")
			{
				alert("请输入评论信息");
				$("#commentInfo").focus();
				return;
			}
			if($("#commentInfo").val().length > 400){
				alert("输入评论字数不能超过400字");
				$("#commentInfo").focus();
				return;
			}
			$.post(postCommentUrl, $("#commentform").serialize(), function(result)
			{
				//console.log(result);
				if(result.success)
				{
					alert("提交成功,请刷新页面查看,如未看到新发表的评论请等待审核.");
				}
				else
				{
					alert("提交失败,请稍候再试");
				}
			});
		});
	}
}

function getComment()
{
	//获取评论的url
	var getCommentUrl = "remoteProxy?targetUrl=" + encodeURIComponent(comment_if + "getComments?lastCommentId=" + lastCommentId + "&count=" + commentPageSize + "&rowNumber="+allReadComment+"&articleId=" + articleId);

	//取评论
	$.getJSON(getCommentUrl, showComment);
}

function showComment(comment)
{
	if(comment == null)
		return;
	var commentTemplate = $("#commentTemplate");
	//console.log(comment.totalCount);
	$(comment.comments).each(function(i)
	{
		var singleComment = comment.comments[i];
	
		var newDiv = commentTemplate.clone(true);
		newDiv.attr("id", newDiv.attr("id") + "_" + singleComment.commentId);
		newDiv.find("#user").html(singleComment.userName);
		newDiv.find("#commentTime").html(singleComment.commentTime);
		newDiv.find("#content").html(singleComment.content);

		if(isHaveOneComment == false)
		{
			commentTemplate.after(newDiv);
			isHaveOneComment = true;
			allComment = comment.totalCount;
		}
		else
		{
			$("#commentTemplate_" + lastCommentId).after(newDiv);
		}
		
		lastCommentId = singleComment.commentId;
		allReadComment++;

		newDiv.show();
	});
	
	if(allReadComment>0 && allReadComment<allComment)
	{
		$("#moreTopComment").show();
	}
	else
	{
		$("#moreTopComment").hide();
	}
}