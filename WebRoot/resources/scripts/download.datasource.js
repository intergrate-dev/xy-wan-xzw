
//获取参数
var customerId;//客户ID
var siteId;//站点ID
var paraString = location.pathname.split("/");
var $load ;
if(paraString.length > 2)
{
	customerId = paraString[paraString.length-2];
	siteId = paraString[paraString.length-1];
}
var isMobile = {   
	    Android: function() {   
	        return navigator.userAgent.match(/Android/i) ? true : false;   
	    },   
	    BlackBerry: function() {   
	        return navigator.userAgent.match(/BlackBerry/i) ? true : false;   
	    },   
	    iOS: function() {   
	        return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;   
	    },   
	    Windows: function() {   
	        return navigator.userAgent.match(/IEMobile/i) ? true : false;   
	    },   
	    any: function() {   
	        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Windows());   
	    }   
	};   
	
function showDownload(article)
{
	//alert(article);
	//处理title
	var iphone = $("#iphone");
	var android = $("#android");
	var ipad = $("#ipad");
	
	if(article){
//		//console.log("isMobile.Android():"+isMobile.Android());
//		if(isMobile.Android()){
//			$.each(article,function(key,value){
//				if(key==2){
//					window.top.location = value;
//					return;
//				}
//				console.log("我是谁:"+key);
//			});
//			return;
//		}
//		//console.log("isMobile.iOS():"+isMobile.iOS());
//		if(isMobile.iOS()){
//			$.each(article,function(key,value){
//				if(key==1){
//					window.top.location = value;
//					return;
//				}
//				if(key==0){
//					window.top.location = value;
//					return;
//				}
//			});
//			return;
//		}
		$.each(article,function(key,value){
			//console.log(article[key]);
			//console.log(key);
			if(key==0){
				iphone.attr("href",article[key]);
				$("#item_iphone").show();
			}
			if(key==1){
				ipad.attr("href",article[key]);
				$("#item_ipad").show();
			}
			if(key==2){
				if(isMobile)
				android.attr("href",article[key]);
				$("#item_android").show();
			}
		});
	}
	
	
}