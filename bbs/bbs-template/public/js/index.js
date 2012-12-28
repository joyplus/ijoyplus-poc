// search
var iSearch = {
	init: function(){
		var iSearchObj = $("#indexSearch"),
			 iSpan     =  iSearchObj.find("span"),
			 iTxt      =  $("#indexTxtSearch"),
			 spanHide  = function(){
				iSpan.hide();
				iTxt.focus();
		     };

		iSpan.click(spanHide);
		iTxt.click(spanHide);
		iTxt.val("");
	},
	post: function(){
		var itext =  $("#indexTxtSearch"), txt = itext.val();
		if (txt.length === 0 ){
			alert("请您输入内容！");
			itext.focus();
			return;
		};
		//location.href = "http://bbs.joyplus.tv/search.php?srchtxt=" + txt;
		window.open("http://bbs.joyplus.tv/search.php?srchtxt=" + txt);
	}
};

//面板切换
var indexPlayMISwitch = function(){
	var ipMI  = $("#indexPlayMI"),
	     head  = ipMI.find(".head"),
		 pa    = head.find("dd"),
		 intro = ipMI.find(".intro")
		 curIndex = 0;
	
	head.delegate("dd", "mouseenter", function(){
		pa.eq(curIndex).attr("className","");
		intro.eq(curIndex).hide();
		
		curIndex = this.getAttribute("index");
		this.className = "sel";
		intro.eq(curIndex).show();
	});
};

//面板切换
var indexAppDownSwitch = function(){
	var ipMI  = $("#indexAppDown"),
		 head  = ipMI.find("dl"),
		 pa    = head.find("dd"),
		 intro = ipMI.find(".content")
		 dci = 0;
	
	head.delegate("dd", "mouseenter", function(){
		pa.eq(dci).attr("className","");
		intro.eq(dci).hide();
		
		dci = this.getAttribute("index");
		this.className = "sel";
		intro.eq(dci).show();
	});
	
	
	
};

//面板切换
var indexAppCommentSwitch = function(){
	var ipMI  = $("#indexAppComment"),
		 head  = ipMI.find("h5>ul"),
		 pa    = head.find("li"),
		 intro = ipMI.find(".appList")
		 aci = 0;
	
	head.delegate("li", "mouseenter", function(){
		pa.eq(aci).attr("className","");
		intro.eq(aci).hide();
		
		aci = this.getAttribute("index");
		this.className = "sel";
		intro.eq(aci).show();
	});
};

// 下载图文推荐翻
var indexAppPicCommentSwitch = function(){
	var iapc  = $("#indexAppPicComment"),
	     bimg  = iapc.find(".bigImage"), 
	     curLi = iapc.find("li").eq(0);
	iapc.delegate("li img", "mouseenter", function(){
		var imgUrl = this.getAttribute("bigimg"),
			 linkUrl = this.getAttribute("link");
		curLi.attr("className", "");
		curLi = $(this).parent().parent();
		curLi.attr("className", "sel");
		bimg.attr("src", imgUrl);
		bimg.parent().attr("href", linkUrl);		
	});
	
};

// 首页导航
var indexNavBottom = function(){
	var iNav = $("#navSite");
	iNav.delegate("li>a", "mouseenter", function(){
		$(this).parent().attr("className", "sel");
	});
	
	iNav.delegate("li>a", "mouseleave", function(){
		$(this).parent().attr("className", "");
	});
};

// 随手拍翻页
var photoPage = {
		page: 1,
		total: 1,
		list: $("#indexPicList"),
		init: function(){
			var pList = this.list, o = pList.find("ol"), ol = o.size() ? o.size() : 1, totalWidth = 960 * ol;
			this.total = ol;
			pList.width(totalWidth);
		},
		prev: function(){
			if (this.page > 1){
				
				var curLeft = parseFloat(this.list.css("marginLeft")) + 960;
				this.list.animate({marginLeft: curLeft + "px"});
				this.page = parseInt(this.page - 1);
			};
		},
		next: function(){
			if (this.page < this.total){
				var curLeft = parseFloat(this.list.css("marginLeft")) - 960;
				console.log(curLeft);
				this.list.animate({marginLeft: curLeft + "px"});
				this.page = parseInt(this.page + 1);
			};
		}
};
