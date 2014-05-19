(function(){



var AdReqPg = window.ZTRAdRequestUrl ? window.ZTRAdRequestUrl : 'http://core.ad20.net/adm',
	CommonFilesDir = 'http://content.ad20.net/CommonFiles',
	TplDir = 'http://content.ad20.net/LibTplBE/',
	Preview = false,

	PgId = (typeof X1_PgId_R == 'function' ? X1_PgId_R() : parseInt(Math.random() * Math.pow(10, 17))),
	TagIdx = 0,
	Tags = [],
	Time = (new Date()).getTime(),
	Rnd = Math.random()*Math.pow(10,17),
	Info = {UA:1, WIN:1, OP:1, IE:1, IE50:1, IE55:1, IE6:1, IE7:1, IE8:1, IE9:0, IE10:0, IE11:0, FF:1, CH:1, ShortUrls:1, EncUrl:1, EncRef:1, Cookie:1, FlashVer:1, 
				FixedPos:1, InitWidth:1, InitHeight:1, Dbg:1, SelectsMask:0, UseFn:true, EngineVer:0, AS3OpenMode:0},
	DbgData = {},
	ZIdxLevels = {normal:10, html3rdParty:12, expandable:20, peel:30000, sticky:50, underline:999000, floating:1999888777, toolbar:2000000000, background:-100000, debug:2100000000}
	;

if (typeof window.X1_PgId == 'undefined' || window.X1_PgId != PgId)
	window.X1_PgId = PgId;




/* Begin - General use functions */


function Init()
{
	var i,
		u = document.URL?document.URL:null,
		r = document.referrer?document.referrer:null,
		d = new Date(),
		a = navigator.userAgent?navigator.userAgent.toLowerCase():'';
	
	
	if (u && Trim(u.toLowerCase()).indexOf("file://") != 0)
	{
		u=u.replace(/ /g, "%20").replace(/\+/g, "%2b");
		i=u.indexOf("#");if(i>-1)u=u.substring(0,i);
	}
	
	if (r && Trim(r.toLowerCase()).indexOf("file://") != 0)
	{
		r=r.replace(/ /g, "%20").replace(/\+/g, "%2b");
		i=r.indexOf("#");if(i>-1)r=r.substring(0,i);
	}
	
	if (u == null)
		u = '';
		
	if (r == null)
		r = '';
	
	
	d.setTime(d.getTime()+1000);
	document.cookie = 'ZTRtestck=1; expires=' + d.toGMTString() + '; path=/';

	
	
	Info = 
	{
		UA:a,
		WIN:a.indexOf("windows")>=0,
		OP:a.indexOf('opera')>=0,
		EncUrl:Enc(u),
		EncRef:Enc(r),
		Cookie:document.cookie && document.cookie.indexOf('ZTRtestck') > -1,
		FlashVer:FVer(),
		FixedPos:typeof(document.compatMode)!='undefined'&&document.compatMode.toLowerCase()!='backcompat'&&(new RegExp("msie 7|msie 8|msie 9|msie 10|rv\:11|firefox|chrome|opera/9|opera/10")).test(a),
		InitWidth:1,
		InitHeight:1,
		ShortUrls:1,
		Dbg: a.indexOf("windows nt") > -1 && (new RegExp("msie|rv\:11|firefox|chrome|opera|trident|mozilla")).test(a),
		UseFn:true,
		AS3OpenMode:0
	};

	
	if (!Info.OP)
	{
		Info.IE = a.indexOf('msie')>=0 || a.indexOf('rv\:11')>=0;
		if (Info.IE)
		{
			Info.IE50 = a.indexOf('msie 5.0') >= 0;
			Info.IE55 = a.indexOf('msie 5.5') >= 0;
			Info.IE6 = a.indexOf('msie 6.') >= 0;
			Info.IE7 = a.indexOf('msie 7.') >= 0;
			Info.IE8 = a.indexOf('msie 8.') >= 0;
			Info.IE9 = a.indexOf('msie 9.') >= 0;
			Info.IE10 = a.indexOf('msie 10.') >= 0;
			Info.IE11 = a.indexOf('rv\:11.') >= 0;

			if (Info.IE50)
				Info.SelectsMask = 1;
			else
				if (Info.IE55 || Info.IE6)
					Info.SelectsMask = location.protocol == "http:" ? 2 : 1;
			
			if (!Info.IE9 && !Info.IE10 && !Info.IE11)
				Info.UseFn = false;
			
			if (document.documentMode)
			{
				Info.EngineVer = document.documentMode;
			}
			else
			{
				Info.EngineVer = 5;

				if (document.compatMode)
				{
					if (document.compatMode == "CSS1Compat")
						Info.EngineVer = 7;
				}
			}
		}
		else
		{
			if (a.indexOf('chrome')>=0)
				Info.CH = true;
			else
				Info.FF = true;
		}
	}
	
	if (Info.IE)
		Info.AS3OpenMode = 1;
	else 
		if (a.indexOf('firefox') > -1)
			Info.AS3OpenMode = 2;
	
	
	Info.InitWidth = GetWinW();
	Info.InitHeight = GetWinH();
	
	Dbg_Init();


	for (var k in ZTRADS)
	{
		Tags.push
		(
			{
				ContId: k,
				AdParams: ZTRADS[k].params,
				SiteParams: ZTRADS[k].custom ? ZTRADS[k].custom : {}
			}
		);
	}
	

	

	var pa, sP, sAd = "", i, sQ, sIfrTags = window == top ? 0 : CountTags(), adPub, adSite, adSection;


	if (typeof(PageParams) == "object")
	{
		adPub = PageParams.pub;
		adSite = PageParams.site;
		adSection = PageParams.section;
	}
	else
	{
		var ads = [], j, adFound;
		
		for (i = 0; i < Tags.length; i++)
		{
			if (typeof(Tags[i].AdParams) != 'object')
				continue;
		
			pa = Tags[i].AdParams;
		
			adFound = false;
			
			for (j = 0; j < ads.length; j++)
			{
				if (ads[j].pub == pa.pub && ads[j].site == pa.site && ads[j].section == pa.section)
				{
					ads[j].count++;
					adFound = true;
					break;
				}
			}
			
			if (!adFound)
				ads.push({pub:pa.pub, site:pa.site, section:pa.section, count:1});
		}
		
		i = 0;
		
		for (j = 0; j < ads.length; j++)
		{
			if (ads[j].count > ads[i].count)
				i = j;
		}
		
		adPub = ads[i].pub;
		adSite = ads[i].site;
		adSection = ads[i].section;
	}

	
	sAd += "&pub=" + Enc(adPub) + "&site=" + Enc(adSite) + "&section=" + Enc(adSection) + "&size=0x0";
	
	
	var tag, sAdZone = '', sZ, sK, oZA, vS, oZV, zk2, nvi2;
	
	for (i = 0; i < Tags.length; i++)
	{
		tag = Tags[i];
		
		if (typeof(tag.AdParams) != 'object')
			continue;
		
		oZA = {k:null, w:0, h:0, c:null};
		
		sZ = '';
		
		for (sK in tag.AdParams)
		{
			if (sZ != '')
				sZ += '&';
			
			oZV = tag.AdParams[sK];
		
			switch (sK.toString().toLowerCase())
			{
				case 'pub':
					//if (!PageParams.pub || PageParams.pub != oZV)
					//	sZ += 'pub=' + Enc(oZV);
					break;
			
				case 'site':
					//if (!AdParams.site || AdParams.site != oZV)
					//	sZ += 'site=' + Enc(oZV);
					break;

				case 'section':
					sZ += 'section=' + Enc(oZV);
					break;
				
				case 'zone':
					sZ += 'z=' + Enc(oZV);
					oZA.k = Trim(oZV.toString().toLowerCase());
					break;
				
				case 'size':
					sZ += 's=' + Enc(oZV);
					vS = oZV.toString().toLowerCase().split("x");
					if (vS.length > 1)
					{
						oZA.w = parseInt(vS[0]);
						oZA.h = parseInt(vS[1]);
					}
					break;
				
				default:
					sZ += Enc(sK) + '=' + Enc(oZV);
					break;
			}
		}
		
		if (sZ != '')
			sAdZone += (sAdZone==''?'':'::') + sZ;
		
		Tags[i].Zone = oZA;
		
		if (Tags[i].SiteParams.destinationClickUrl)
			Tags[i].DCU = Tags[i].SiteParams.destinationClickUrl;
	}
	
	sAd += "&zone=" + Enc(sAdZone);
	
	if (location.protocol == "https:")
		AdReqPg = AdReqPg.replace(/http\:/i, 'https:');
	
	sQ = AdReqPg + '?snocache=' + CacheBuster() + '&spgid=' + PgId + '&sww=' + Info.InitWidth + '&swh=' + Info.InitHeight +
		'&sck=' + (Info.Cookie ? 'y' : 'n') + '&sfver=' + Info.FlashVer + '&sifr=' + sIfrTags + '&f1pgad=0' + sAd;

	
	if (Info.ShortUrls && sQ.length + Info.EncUrl.length > 1900)
		sQ += '&surl=';
	else 
		sQ += '&surl=' + Info.EncUrl;

	
	if (Info.ShortUrls && sQ.length + Info.EncRef.length > 1900)
		sQ += '&sref=';
	else
		sQ += '&sref=' + Info.EncRef;

	
	
	var callAd = true;

	if (window.ZTRAdRequestTime)
	{
		var diff = (new Date()) - window.ZTRAdRequestTime;
		
		if (!isNaN(diff) && diff > 15000)
			callAd = false;
	}

	if (callAd)
		document.write('<scr'+'ipt type="text/javascript" src="'+ sQ +'"><\/scr'+'ipt>');
}








function FVer()
{
	var o, i, ver = null, n = window.navigator, FFS = "Shockwave Flash", IES = "ShockwaveFlash.ShockwaveFlash";

	if (!n)
		n = navigator;


	if (n && n.plugins && n.plugins.length)
	{
		for (i = 0; i < n.plugins.length; i++)
		{
			o = n.plugins[i];
			
			if (o.name.indexOf(FFS) > -1)
				ver = parseInt(o.description.split(FFS)[1], 10);
		}
	}


	if (ver == null && window.ActiveXObject)
	{
		try
		{
			o = new ActiveXObject(IES + ".7");
			
			if (!o)
			{
				o = new ActiveXObject(IES + ".6");
				o.Lc = "always";
			}
			
			if (!o)
				o = new ActiveXObject(IES);
			
			ver = parseInt(o.GetVariable("$version").split(" ")[1], 10);
		}
		catch(e) {}
	}
	
	
	if (ver == null)
		ver = 0;
	
	return ver;
}


window.MX1_Zone = function(key)
{
	window.ZTRANB = true;

	var o = null, i, k = Trim(key.toString().toLowerCase());
	
	for (i = 0; i < Tags.length; i++)
	{
		if (Tags[i].Zone.k == k)
			TagIdx = i;
	}
}


function GetElem(s){return document.getElementById(s);}
function Debug(s){if(typeof(window.Debug)=="function")window.Debug(s);}
function Enc(s){return window.encodeURIComponent?encodeURIComponent(s):escape(s);}
function Show(id){var o=GetElem(id);if(o)o.style.display='';}
function Hide(id){var o=GetElem(id);if(o)o.style.display='none';}


function FSCmd(id)
{
	var a=navigator.appName,u=navigator.userAgent;
	if(a&&a.indexOf("Microsoft")>=0&&u.indexOf("Windows")>=0&&u.indexOf("3.1")<0)
	{
		return "<script language=VBScript>on error resume next\n Sub "+id+"_FSCommand(ByVal c,ByVal a)\n call "+id+"Fn(c, a)\n end sub<\/script>";
	}
	return "";
}


function HideSelects(z)
{
	var i,o,v=document.getElementsByTagName('SELECT');
	for(i=0;i<v.length;i++)
	{
		o=v[i];if(!o||!o.offsetParent)continue;
		if(z){o.ztr_pv=o.style.visibility;o.style.visibility='hidden';}
		else{o.style.visibility=o.ztr_pv;}
	}
}


function CacheBuster()
{
	return escape(Time+'_'+Rnd);
}


function Trim(s)
{
	if(typeof(s)=="undefined")return "";
	else return s.toString().replace(/^\s+|\s+$/g, '');
}


function JsEncLight(s)
{
	if (typeof(s) == "undefined" || !s) return "";
	
	var i, iLen = s.length, c, iC, v = [];

	for (i = 0; i < iLen; i++)
	{
		c = s.charAt(i);
		iC = s.charCodeAt(i);

		if (c == "'")	v[v.length] = "\\'";	else
		if (c == "\"")	v[v.length] = "\\\"";	else
		if (c == "\b")	v[v.length] = "\\b";	else
		if (c == "\t")	v[v.length] = "\\t";	else
		if (c == "\n")	v[v.length] = "\\n";	else
		if (c == "\r")	v[v.length] = "\\r";	else
		
		v[v.length] = c;
	}
	
	return v.join("");
}


function HtmlEncode(s)
{
	if (typeof(s) == "number") return s.toString();
	if (typeof(s) == "undefined" || !s) return "";

	var i, iLen = s.length, c, iC, v = [];

	for (i = 0; i < iLen; i++)
	{
		c = s.charAt(i);
		iC = s.charCodeAt(i);

		if (c == "<")	v[v.length] = "&#60;";	else
		if (c == ">")	v[v.length] = "&#62;";	else
		if (c == "'")	v[v.length] = "&#39;";	else
		if (c == "\"")	v[v.length] = "&#34;";	else
		if (c == "&")	v[v.length] = "&#38;";	else
		if (iC <= 126)	v[v.length] = c;
		else
			v[v.length] = "&#"+ iC +";";
	}
	
	return v.join("");
}


function HtmlEncodeCustom1(s)
{
	if (typeof(s) == "undefined" || !s) return "";
	
	return HtmlEncode(s).replace(/  /g, ' ').replace(/\n\r/g, '<br/>').replace(/\r\n/g, '<br/>').replace(/[\n|\r]/g, '<br/>');
}


function HtmlEncodeCustom2(s)
{
	if (typeof(s) == "undefined" || !s) return "";

	var i, iLen = s.length, c, iC, v = [];

	for (i = 0; i < iLen; i++)
	{
		c = s.charAt(i);
		iC = s.charCodeAt(i);

		if (c == "<")	v[v.length] = "&#60;";	else
		if (iC <= 126)	v[v.length] = c;
		else
			v[v.length] = "&#"+ iC +";";
	}
	
	var s = 
		v.join("").
		replace(/&#60;b>/gi,'<b>').replace(/&#60;\/b>/gi,'</b>').replace(/&#60;b /gi,'<b ').
		replace(/&#60;strong>/gi,'<strong>').replace(/&#60;\/strong>/gi,'</strong>').replace(/&#60;strong /gi,'<strong ').
		replace(/&#60;i>/gi,'<i>').replace(/&#60;\/i>/gi,'</i>').replace(/&#60;i /gi,'<i ').
		replace(/&#60;table/gi,'<table').replace(/&#60;\/table/gi,'</table').
		replace(/&#60;tr/gi,'<tr').replace(/&#60;\/tr/gi,'</tr').
		replace(/&#60;td/gi,'<td').replace(/&#60;\/td/gi,'</td').
		replace(/&#60;div/gi,'<div').replace(/&#60;\/div/gi,'</div').
		replace(/&#60;span/gi,'<span').replace(/&#60;\/span/gi,'</span').
		replace(/\s+on[a-z0-9]+\s*=/gi,' EvNA=').
		replace(/"+on[a-z0-9]+\s*=/gi,' EvNA=').
		replace(/'+on[a-z0-9]+\s*=/gi,' EvNA=').
		replace(/  /g, '&nbsp; ').replace(/\n\r/g, '<br/>').replace(/\r\n/g, '<br/>').replace(/[\n|\r]/g, '<br/>')
	;
	
	return s;
}


function TrkHtmlCode(idx, S, html)
{
	var clickPrefix = GetClickPrefix(S.click, S.params),
		tag = Tags[idx];

	html = html.
		replace(/\$creative_id\$/gi, S.id).
		replace(/\$display_time\$/gi, Time).
		replace(/\$display_random\$/gi, Rnd).
		replace(/\$files_dir\$/gi, S.dir).
		replace(/\$click_tracking_url\$/gi, clickPrefix).
		replace(/\$ad_params_site\$/gi, escape(tag.AdParams.site)).
		replace(/\$ad_params_zone\$/gi, escape(tag.AdParams.zone))
	;
	
	var i, vUrls = html.split('$click_url_begin$'), v, sCU;
	
	if (vUrls.length > 1)
	{
		for (i = 0; i < vUrls.length; i++)
		{
			v = vUrls[i].split('$click_url_end$');
			
			if (v.length > 1)
			{
				sCU = v[0];
				sCU = tag.DCU ? tag.DCU.replace(/\$destination_click_url\$/gi, escape(sCU)) : sCU;
				vUrls[i] = clickPrefix + escape(sCU) + v[1];
			}
		}
		html = vUrls.join('');
	}
	
	
	vUrls = html.split('$encode_begin$');
	
	if (vUrls.length > 1)
	{
		for (i = 0; i < vUrls.length; i++)
		{
			v = vUrls[i].split('$encode_end$');
			
			if (v.length > 1)
				vUrls[i] = escape(v[0]) + v[1];
		}
		html = vUrls.join('');
	}


	vUrls = html.split('$decode_begin$');
	
	if (vUrls.length > 1)
	{
		for (i = 0; i < vUrls.length; i++)
		{
			v = vUrls[i].split('$decode_end$');
			
			if (v.length > 1)
				vUrls[i] = unescape(v[0]) + v[1];
		}
		html = vUrls.join('');
	}
	
	return(html);
}


function Params(sQ,sP)
{
	sP='&'+sP;
	if(sQ.length+sP.length>2000)
	{
		sP=sP.replace(/\&sref=[^\&]+/gi,'&sref=');
		if(sQ.length+sP.length>2000)
		{
			sP=sP.replace(/\&surl=[^\&]+/gi,'&surl=');
			if(sQ.length+sP.length>2000)sP='&s2000c=1';
		}
	}
	return sP.substr(1);
}


function ParamsE(sQ,sP)
{
	sP='%26'+sP;
	if(sQ.length+sP.length>2000)
	{
		sP=escape(unescape(sP).replace(/\&sref=[^\&]+/gi,'&sref='));
		if(sQ.length+sP.length>2000)
		{
			sP=escape(unescape(sP).replace(/\&surl=[^\&]+/gi,'&surl='));
			if(sQ.length+sP.length>2000)sP=escape('&s2000c=1');
		}
	}
	return sP.substr(3);
}

function GetFileUrl(dir, file)
{
	return file.indexOf('://') > 0 ? file : dir + file;
}

function GetClickPrefix(click, params)
{
	var q = click + '?scprnd=' + escape(Math.random());
	return q + '&' + Params(q, params) + '&sclickurl=';
}

function ReplaceRules(idx, url)
{
	var t = Tags[idx],
		s = url.
			replace(/\$ad_params_site\$/gi, escape(t.AdParams.site)).
			replace(/\$ad_params_zone\$/gi, escape(t.AdParams.zone))
	;
	
	if (t.DCU)
		s = t.DCU.replace(/\$destination_click_url\$/gi, escape(s));
	
	return s;
}
	
function GetClickUrl(idx, u, S)
{
	var ru = ReplaceRules(idx, u),
		clickPrefix = GetClickPrefix(S.click, S.params),
		clickTAG = clickPrefix + escape(ru);
	
	return clickTAG;
}

function GetFlashVars(idx, fid, u, ut, S, setFn, bnrId)
{
	var ru = ReplaceRules(idx, u),
		clickPrefix = GetClickPrefix(S.click, S.params),
		clickTAG = clickPrefix + escape(ru),
		c = escape(clickTAG),
		t = Tags[idx];

	if (typeof(bnrId) == 'undefined' || !bnrId || bnrId == '')
		bnrId = fid;
	
	var	f = 'clickTAG=' + c +
			'&Scp=' + escape(clickPrefix) +
			'&Stg=' + ut +
			'&Sfn=' + (setFn ? fid + 'Fn' : '') +
			'&Sdir=' + escape(S.dir) +
			'&SAdParamsSite=' + escape(t.AdParams.site) +
			'&SAdParamsZone=' + escape(t.AdParams.zone) +
			'&SPgId=' + escape(PgId) +
			'&SBnrId=' + escape(bnrId) +
			'&SAS3OM=' + Info.AS3OpenMode +
			'&SAS3CMDFN=' + fid + 'Fn' +
			'&ClickTAG=' + c +
			'&ClickTag=' + c +
			'&clickTag=' + c +
			'&clicktag=' + c;

	return f;
}

function GetFlashHtml(id, file, width, height, flashVars, sceneScale, sceneAlign)
{
	var f = (Info.FlashVer < 6 ? '' : flashVars),
		q = (Info.FlashVer > 5 ? '' : flashVars),
		ss = (sceneScale ? sceneScale : 'showall'),
		sa = (sceneAlign ? sceneAlign : 'lt'),
		s,
		idx = file.lastIndexOf('/'),
		base = idx > -1 ? file.substring(0, idx + 1) : '';
		
	if (Info.IE)
	{
		s =
			(Info.IE11 ?
				'<object id="' + id + '" name="' + id + '" width="' + width + '" height="' + height + '" data="' + file + q + '" type="application/x-shockwave-flash">' :
				'<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="' + id + '" width="' + width + '" height="' + height + '"><param name="movie" value="' + file + q + '" />'
			) +
		' <param name="flashvars" value="' + f + '" />' +
		' <param name="quality" value="high" />' +
		' <param name="wmode" value="transparent" />' +
		' <param name="allowScriptAccess" value="always" />' +
		' <param name="scale" value="' + ss + '" />' +
		' <param name="salign" value="' + sa + '" />' +
		' <param name="base" value="' + base + '" />' +
		'</object>';
	}
	else
	{
		s =
		' <embed type="application/x-shockwave-flash" id="' + id + '" width="' + width + '" height="' + height + '" ' + 
		'  src="' + file + q + '" flashvars="' + f + '" ' + 
		'  quality="high" wmode="transparent" allowScriptAccess="always" scale="' + ss + '" salign="' + sa + '" base="' + base + '"></embed>';
	}
	
	return s;
}

function GetImgHtml(id, file, width, height, clickCode, style, title)
{
	var s = 
		'<img ' + 
		(id ? 'id="' + id + '" ' : '') +
		'src="' + file + '" width="' + width + '" height="' + height + '" border="0" alt="' + HtmlEncode(title) + '" ' +
		(clickCode ? 'onClick="' + JsEncLight(clickCode) + '" ' : '') +
		(style ? 'style="' + style + '"' : '') + 
		'/>';
	
	return s;
}

var DTCallCount = 0;

function DT(S)
{
	if (S.trk < 1)
		return;
	
	/*
	if (S.id == '0')
	{
		DTCallCount++;
		setTimeout(function(){DT2(S)}, 2000 + (DTCallCount * 250));
		return;
	}
	*/
	
	var sQ=S.trkimg+'?sdtrnd='+escape(Math.random());
	document.write('<img src="'+(sQ+'&'+Params(sQ,S.params))+'" width="1" height="1" border="0" style="position:absolute !important; left:-100px !important; top:-100px !important; width:1px !important; height:1px !important;"/>');
	
	
	var sETrkC = '';
	
	if (typeof(S.etrkm) != "undefined" && typeof(S.etrkc) != "undefined")
	{
		var t = Trim(S.etrkc);
		if (t == '') return;
	
		switch (S.etrkm)
		{
			case 1:
				var v = t.split(/\s*[\n|\r]\s*/), i, sLow, sSrc;
				for (i = 0; i < v.length; i++)
				{
					sLow = v[i].toLowerCase();
					if (sLow.indexOf('http://') == 0 || sLow.indexOf('https://') == 0)
					{
						sSrc = v[i].replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
						sETrkC = sETrkC + '<img src="'+sSrc+'" width="1" height="1" border="0"/>';
					}
				}
				document.write('<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">'+ sETrkC +'</div>');
				break;
			
			case 2:
				t = t.replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
				
				if (t.toLowerCase().indexOf('noscript') > -1)
				{
					t = t.replace(/<noscript.*?>[\s\S]*?<\/.*?noscript>/gi,'').replace(/<.*?noscript.*?>/gi,'').replace(/<.*?noscript/gi,'');
				}
				
				document.write('<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">'+t+'</div>');
				break;			
			
			default:
				break;
		}
	}
}


function DT2(S)
{
	if(S.trk<1)return;
	
	var sETrkC = '';
	
	if (typeof(S.etrkm) != "undefined" && typeof(S.etrkc) != "undefined")
	{
		var t = Trim(S.etrkc);
		if (t != '')
		{
			switch (S.etrkm)
			{
				case 1:
					var v = t.split(/\s*[\n|\r]\s*/), i, sLow, sSrc;
					for (i = 0; i < v.length; i++)
					{
						sLow = v[i].toLowerCase();
						if (sLow.indexOf('http://') == 0 || sLow.indexOf('https://') == 0)
						{
							sSrc = v[i].replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
							sETrkC = sETrkC + '<img src="'+sSrc+'" width="1" height="1" border="0"/>';
						}
					}
					break;
				
				default:
					sETrkC = t.replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
					break;
			}
		}
	}
	
	
	var oD = document.createElement("DIV");
	if (oD)
	{
		oD.style.position = 'absolute';
		oD.style.top = '0px';
		oD.style.left = '0px';
		oD.style.width = '1px';
		oD.style.height = '1px';
		oD.style.display = '';
		 				
		var sQ=S.trkimg+'?sdtrnd='+escape(Math.random());
		var sTrkC='<img src="'+(sQ+'&'+Params(sQ,S.params))+'" width="1" height="1" border="0"/>';
		oD.innerHTML = '<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">'+ sTrkC + sETrkC +'</div>';
		
		document.body.insertBefore(oD, document.body.firstChild)
	}
}


window.X1_EDT = function(m,t)
{
	t = Trim(t); 
	if (t == '') return;
	
	if (m == 1)
	{
		if (t.toLowerCase().indexOf('http://') == 0 || t.toLowerCase().indexOf('https://') == 0)
		{
			t = t.replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
			document.write('<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;"><img src="'+t+'" width="1" height="1" border="0"></div>');
		}
	}
	else
	{
		t = t.replace(/\$display_time\$/gi, Time).replace(/\$display_random\$/gi, Rnd);
		document.write('<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">'+t+'</div>');
	}
}


function UQ(idx, B,S)
{
	var tag = Tags[idx],
		sQ = S.dir + B.f +
		'?id='+escape(S.id)+'&dir='+escape(S.dir)+'&trk='+escape(S.trk)+'&trkimg='+escape(S.trkimg)+'&click='+escape(S.click)+
		'&site='+escape(tag.AdParams.site)+'&zone='+escape(tag.AdParams.zone);
	
	if (tag.DCU)
	{
		var sEDCU = escape(tag.DCU);
		if (sQ.length + sEDCU.length < 2000)
			sQ = sQ + '&dcu=' + sEDCU;
	}
	
	return sQ+'&params='+ParamsE(sQ,escape(S.params));
}


function GetXY(ObjRefOrId)
{
	var oR = {x:-10000, y:-10000};

	var o = typeof(ObjRefOrId) == 'string' ? document.getElementById(ObjRefOrId) : ObjRefOrId;
	if(!o) return oR;
	

	if(o.getBoundingClientRect && !document.getBoxObjectFor)
	{
		var r = o.getBoundingClientRect(),
			x = document.documentElement.scrollLeft || document.body.scrollLeft,
			y = document.documentElement.scrollTop || document.body.scrollTop;

		oR = {x: r.left + x, y: r.top + y};

		if (Info.IE && Info.EngineVer < 8)
		{
			oR.x = oR.x - 2;
			oR.y = oR.y - 2;
		}
	}
	else
	{
		if(document.getBoxObjectFor)
		{
			var r = document.getBoxObjectFor(o);
			oR = {x: r.x, y: r.y};
		}
		else
		{
			if (o.offsetParent)
			{
				oR.x = o.offsetLeft;
				oR.y = o.offsetTop;
				
				while (o = o.offsetParent)
				{					
					oR.x += o.offsetLeft;
					oR.y += o.offsetTop;
				}
			}
		}
	}

	return oR;
}


function GetWH(ObjRefOrId)
{
	var oR = {w:0, h:0}, o = typeof(ObjRefOrId) == 'string' ? document.getElementById(ObjRefOrId) : ObjRefOrId;
	if(!o) return oR;
	
	if(o.style.display == 'none')
	{
		o.style.display = '';
		oR.w = o.offsetWidth;
		oR.h = o.offsetHeight;
		o.style.display = 'none';
	}
	else
	{
		oR.w = o.offsetWidth;
		oR.h = o.offsetHeight;
	}

	return oR;
}


function GetWinW()
{
	var w = window.innerWidth||document.documentElement.clientWidth||document.body.clientWidth,
		e = document.documentElement;
	
	if (!(Info.IE6 || Info.IE7 || Info.IE8) && e && e.scrollHeight && e.clientHeight && e.scrollHeight > e.clientHeight)
		w = w - 18;
	
	return w;
}

function GetWinH()
{
	var h = window.innerHeight||document.documentElement.clientHeight||document.body.clientHeight,
		e = document.documentElement;
	
	if (!(Info.IE6 || Info.IE7 || Info.IE8) && e && e.scrollWidth && e.clientWidth && e.scrollWidth > e.clientWidth)
		h = h - 17;

	return h;
}


function GetComputedZIndex(ObjRefOrId)
{
	var iR = 0;
	var o = typeof(ObjRefOrId) == 'string' ? document.getElementById(ObjRefOrId) : ObjRefOrId;
	if(!o) return iR;
	
	var iSig = 0;
	var s = '';
	
	while (o && o != document.body && iSig < 100)
	{
		s = GetStyleValue(o, 'z-index');
		
		if (!isNaN(parseInt(s)))
		{
			iR = parseInt(s);
			break;
		}
		
		o = o.parentNode;
		iSig++;
	}

	return iR;
}


function GetStyleValue(ObjRefOrId, HPName)
{
	var sV = '', oDV, oCS, o = typeof(ObjRefOrId) == 'string' ? document.getElementById(ObjRefOrId) : ObjRefOrId;
	if(!o) return '';
	
	var v = /(-[a-z])/i.exec(HPName), CPName = HPName.replace(RegExp.$1, RegExp.$1.substr(1).toUpperCase());

	if (o.currentStyle && o.currentStyle[CPName])
	{
		sV = o.currentStyle[CPName];
	}
	else
	{
		oDV = document.defaultView;
		
		if (oDV && oDV.getComputedStyle)
		{
			oCS = oDV.getComputedStyle(o, '');

			if (oCS && oCS.getPropertyValue)
				sV = oCS.getPropertyValue(HPName);
		}
	}

	if (sV == null)
		sV = '';

	return sV;
}


function SetStyles(o, w, h, bgColor, setSize, isEmptyZone)
{
	if (typeof(w) == 'undefined') w = 0;
	if (typeof(h) == 'undefined') h = 0;
	if (typeof(setSize) == 'undefined') setSize = true;
	if (typeof(isEmptyZone) == 'undefined') isEmptyZone = false;

	if (setSize)
	{
		o.style.width = w + 'px';
		o.style.height = h + 'px';
	}
	
	o.style.marginLeft = 'auto';
	o.style.marginRight = 'auto';
	
	if (bgColor)
		o.style.backgroundColor = bgColor;

	
	var cn = '',
		ocn = o.className ? Trim(o.className.toString()) : null,
		oid = o.id ? o.id.toString() : null;
	
	
	if (isEmptyZone)
	{
		if (ocn)
			cn += 
				ocn + ' ' +
				ocn + '_empty '
			;
		
		cn += 
			'z_empty '
		;
		
		if (oid)
			cn += 
				oid + '_empty '
			;
	}
	else
	{
		if (ocn)
			cn += 
				ocn + ' ' +
				ocn + '_full ' +
				ocn + '_w' + w + ' ' +
				ocn + '_h' + h + ' ' +
				ocn + '_' + w + 'x' + h + ' '
			;
		
		cn += 
			'z_full ' +
			'z_w' + w + ' ' +
			'z_h' + h + ' ' +
			'z_' + w + 'x' + h + ' '
		;
		
		if (oid)
			cn += 
				oid + '_full ' +
				oid + '_w' + w + ' ' +
				oid + '_h' + h + ' ' +
				oid + '_' + w + 'x' + h + ' '
			;
	}

	o.className = cn;
	
	
	var c = GetElem("zc" + o.id.substring(1));
	if (!c)
		return;
	
	cn = (' ' + cn).replace(/\sz_/gi," zc_");
	
	c.className = cn.substring(1);
}

function StripBR(s)
{
	if (typeof(s) == "undefined" || !s) return "";
	return s.replace(/\<br\/\>/gi, ' ');
}


function GetColorCode(s,d)
{
	if (typeof(s) == 'undefined') return d;
	s = Trim(s);
	return (new RegExp("[^#a-zA-Z0-9]")).test(s) || s.length < 4 || s.length > 250 ? d : s;
}

var Dbg_New_callNumber = 0;

function Dbg_New(i, t, b, s, idx, ib)
{
	if (!Info.Dbg) return;
	
	DbgData[i] = {idx:Dbg_New_callNumber, t:t, b:b, s:s, tagIdx:idx, ib:ib, m:[{d:new Date(), s:'Init'}]};
	Dbg_New_callNumber++;
}

function Dbg_Msg(i, s)
{
	if (!Info.Dbg) return;
	
	if (window.ZTR_Dbg_MsgL)
	{
		ZTR_Dbg_MsgL(i, s);
	}
	else if (window.ZTR_DbgP_MsgL)
	{
		ZTR_DbgP_MsgL(i, s);
	}
	else
	{
		var o = DbgData[i];
		if (o)
			o.m.push({d:new Date(), s:s});
	}
}

function Dbg_Init()
{
	if (!Info.Dbg) return;
	
	if (document.body && document.body.firstChild)
	{
		var o = document.createElement("DIV");
		if (o)
		{
			o.id = 'ZTRDbgActivateMask';
			o.style.position = 'absolute';
			o.style.left = '0px';
			o.style.top = '0px';
			o.style.width = '10px';
			o.style.width = '10px';
			o.style.display = 'block';
			o.style.overflow = 'hidden';
			o.style.zIndex = ZIdxLevels.debug;
			o.innerHTML = '<div style="width:10px; height:10px; background-color:white; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01; " onclick="ZTR_Dbg_Load();"></div>';
			o = document.body.insertBefore(o, document.body.firstChild);
			
			if (Preview)
				setTimeout('ZTR_Dbg_Load();', 2000);
		}
	}
	else
	{
		setTimeout(Dbg_Init, 1000);
	}
}

window.ZTR_Dbg_Load = function()
{
	if (typeof(ZTR_Dbg_Open) == 'function')
	{
		ZTR_Dbg_Open();
		return;
	}
	
	var v, s = document.createElement('script');
	if (s)
	{
		s.type = 'text/javascript';
		s.src = TplDir + 'Dbg.js';
		
		v = document.getElementsByTagName('head');
		if (v.length > 0)
		{
			v[0].appendChild(s);
		}
		else if (document.body && document.body.firstChild)
		{
			document.body.insertBefore(s, document.body.firstChild);
		}
	}
}

window.ZTR_DbgP_Load = function()
{
    window.ZTRCOMMONREF.PrvZones = arguments || [];
	if (typeof(ZTR_DbgP_Open) == 'function')
	{
		ZTR_DbgP_Open();
		return;
	}
	
	var v, s = document.createElement('script');
	if (s)
	{
		s.type = 'text/javascript';
		s.src = TplDir + 'DbgP.js';
		
		v = document.getElementsByTagName('head');
		if (v.length > 0)
		{
			v[0].appendChild(s);
		}
		else if (document.body && document.body.firstChild)
		{
			document.body.insertBefore(s, document.body.firstChild);
		}
	}
}

function SendMsgToFlash(sId, sMsg)
{
	var o = document.embeds && document.embeds[sId] ? document.embeds[sId] : document.getElementById(sId);
	if(o)
	{
		try
		{
			if (typeof(o.PercentLoaded) != 'undefined')
			{
				if (o.PercentLoaded() < 20)
					setTimeout('SendMsgToFlash("'+ sId +'", "' + JsEncLight(sMsg) + '");', 50);
				else
					o.SetVariable("JSMsg",sMsg);
			}
			else
				o.SetVariable("JSMsg",sMsg);
		}
		catch (ex){}
	}
}

function GetFlashRef(sId)
{
	var o = document.embeds && document.embeds[sId] ? document.embeds[sId] : document.getElementById(sId);

	return o;		
}

function ExecuteCallbackCode(idx, id, w, h)
{
	var tag = Tags[idx];
	
	if (!w && !h)
	{
		if (tag.SiteParams.emptyCallbackCode)
		{
			Dbg_Msg(id, 'Call callback code for empty ad zone.');
			
			if (typeof(tag.SiteParams.emptyCallbackCode) == 'function')
				tag.SiteParams.emptyCallbackCode();
			else
				eval(tag.SiteParams.emptyCallbackCode);
		}
	}
	else
	{
		if (tag.SiteParams.callbackCode)
		{
			Dbg_Msg(id, 'Call callback code for ad zone.');
			
			if (typeof(tag.SiteParams.callbackCode) == 'function')
				tag.SiteParams.callbackCode(w, h);
			else
				eval(tag.SiteParams.callbackCode);
		}
	}
}


var TraverseTags_count = 1;

function TraverseTags(oN)
{
	var i, iL, oT;
	
	if (oN.hasChildNodes())
	{
		iL = oN.childNodes.length;
		
		for (i = 0; i < iL; i++)
		{
			oT = oN.childNodes[i];
			if (oT)
			{
				TraverseTags_count++;
				TraverseTags(oT);
			}
		}
	}
}


function CountTags()
{
	TraverseTags(document.body);

	return TraverseTags_count;
}


function AddWinEvt(evtName, fncRef)
{
	if (window.attachEvent)
	{
		window.attachEvent("on" + evtName, fncRef);
	}
	else if (window.addEventListener)
	{
		window.addEventListener(evtName, fncRef, false);
	}
}


function CreateClickMask(id, width, height, clickCode, zIndex)
{
	o = document.createElement("DIV");
	if (o)
	{
		o.id = id;
		o.style.position = 'absolute';
		o.style.left = '-10000px';
		o.style.top = '-10000px';
		o.style.width = width + 'px';
		o.style.height = height + 'px';
		o.style.display = 'block';
		o.style.visibility = 'visible';
		o.style.overflow = 'hidden';
		o.style.zIndex = zIndex;
		o.innerHTML = '<div style="width:' + width + 'px !important; height:' + height + 'px !important; border:solid 1px gray; cursor:pointer !important; background-color:white; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01; " onClick="' + clickCode + '"></div>';
		o = document.body.insertBefore(o, document.body.firstChild);
	}			
}


function SendAction(S, actionName)
{
	var u = S.trkimg + '?sdtrnd=' + escape(Rnd);
	
	u = u + '&' + Params(u, S.params) + '&suref=' + escape(actionName);
	
	setTimeout(function(){SendActionLate(u);}, 200);
}

function SendActionLate(url)
{
	var oD = document.createElement("DIV");
	if (oD)
	{
		oD.style.position = 'absolute';
		oD.style.top = '0px';
		oD.style.left = '0px';
		oD.style.width = '1px';
		oD.style.height = '1px';
		oD.style.display = 'block';
		 				
		oD.innerHTML =
			'<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; z-index:1 !important;' +
			' overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">' +
			' <img src="' + url + '" width="1" height="1" border="0"/>' +
			'</div>';
		
		document.body.insertBefore(oD, document.body.firstChild)
	}
}

function SendActionForBackup(S)
{
	var ver = Info.FlashVer == 0 ? 'flash_player_not_installed' : 'old_flash_player_version';
	
	SendAction(S, ver);
}

function RecordCmd(S, actionName)
{
	var trki = parseInt(S.trki);
	
	if (isNaN(trki) || trki == 0)
		return;
	
	SendAction(S, 'x1_sys_' + actionName.toString().toLowerCase());
}

function GetZIdx(tag, key)
{
	return tag.SiteParams.zIndexLevels && tag.SiteParams.zIndexLevels[key] ? tag.SiteParams.zIndexLevels[key] : ZIdxLevels[key];
}


function Reports_Init(S, id, bnrId, w, h)
{
	var trki = parseInt(S.trki);
	
	if (isNaN(trki) || trki == 0)
		return;

	
	window['TVW' + id] = false;
	var viewDuration = 0,
		viewTotalDuration = 0,
		
		isInteractReqSent = false,
		interactTotalDuration = 0,
		
		pageViewDisplayDate = new Date(),
		
		pageInteractTotalDuration = 0,
		isPageInteractInLastSecond = false,
		pageInteractLastST = 0,
		
		adPosition = 0,
		
		trkUrl = S.trkimg.replace(/:\/\/core[0-9]*\./i, "://track.") + '?sdtrnd=' + escape(Math.random())
	;


	trkUrl = trkUrl + '&' + Params(trkUrl, S.params);
	
	
	function CheckView()
	{
		var o = GetElem(bnrId),
			co = GetXY(o),
			st = document.pageYOffset?document.pageYOffset:document.documentElement.scrollTop||document.body.scrollTop,
			sh = document.documentElement.scrollHeight,
			winh = GetWinH(),
			disp = GetStyleValue(o, 'display')
		;
		
		if (co.y >= st && co.y <= st + winh && disp != 'none')
		{
			viewDuration++;
			
			if ((typeof window['TVW' + id] == 'undefined' || !window['TVW' + id]) && viewDuration >= 3)
			{
				window['TVW' + id] = true;
				Reports_Req(trkUrl + '&tvw=1');
			}
		}
		else
		{
			if (viewDuration >= 3)
				viewTotalDuration += viewDuration;
			
			viewDuration = 0;
		}
		
		
		adPosition = Math.ceil(co.y / 700);
		
		
		if (pageInteractLastST != st)
			isPageInteractInLastSecond = true;
		
		pageInteractLastST = st;
		
		
		if (o && typeof(o.X1EventsHanddlerAdded) != 'boolean')
		{
			CheckInteract();
		}
	}
	
	
	function SendInteractReq()
	{
		if (isInteractReqSent)
			return;
		
		isInteractReqSent = true;
		
		Reports_Req(trkUrl + '&tic=1');
	}
	
	
	function CheckInteract()
	{
		var o = GetElem(bnrId),
			interactTimeoutId,
			mouseOverDate
		;
		
		if (!o)
			return;


		function X1InteractMouseOver()
		{
			mouseOverDate = new Date();
		
			if (!isInteractReqSent)
				interactTimeoutId = setTimeout(SendInteractReq, 500);
		}

		function X1InteractMouseOut()
		{
			var sec = Math.round(((new Date()).getTime() - mouseOverDate.getTime()) / 1000);
			
			interactTotalDuration += sec;
		
			if (!isInteractReqSent)
				clearTimeout(interactTimeoutId);
		}

		if (o.attachEvent)
		{
			o.attachEvent("onmouseover", X1InteractMouseOver);
			o.attachEvent("onmouseout", X1InteractMouseOut);
		}
		else if (o.addEventListener)
		{
			o.addEventListener("mouseover", X1InteractMouseOver, false);
			o.addEventListener("mouseout", X1InteractMouseOut, false);
		}
		
		o.X1EventsHanddlerAdded = true;
	}
	
	
	function CheckPageInteract()
	{
		function MouseMove()
		{
			isPageInteractInLastSecond = true;
		}
		
		function CountAndReset()
		{
			if (isPageInteractInLastSecond)
				pageInteractTotalDuration++;
			
			isPageInteractInLastSecond = false;
		}
	
		if (document.attachEvent)
		{
			document.attachEvent("onmousemove", MouseMove);
		}
		else if (document.addEventListener)
		{
			document.addEventListener("mousemove", MouseMove, false);
		}
		
		setInterval(CountAndReset, 1000);
	}
	
	
	function CreateSendVideoPlayFnc()
	{
		window[id + 'SendVideoPlay'] = function(videoName)
		{
			var tavn = (typeof(videoName) == 'undefined' || videoName == null ? 'video_01' : videoName);
		
			Reports_Req(trkUrl + '&tapl=1&tavl=0&tavn=' + Enc(tavn));
		}
	
		window[id + 'SendVideoPercentPlayed'] = function(videoName, percent, totalTime)
		{
			var tapl = parseInt(percent),
				tavl = parseInt(totalTime),
				tavn = (typeof(videoName) == 'undefined' || videoName == null ? 'video_01' : videoName)
			;
		
			tapl = isNaN(tapl) ? 0 : Math.ceil(tapl);
			tavl = isNaN(tavl) ? 0 : Math.ceil(tavl);
		
			Reports_Req(trkUrl + '&tapl=' + tapl + '&tavl=' + tavl + '&tavn=' + Enc(tavn));
		}
		
		window[id + 'SendVideoComplete'] = function(videoName)
		{
			var tavn = (typeof(videoName) == 'undefined' || videoName == null ? 'video_01' : videoName);
		
			Reports_Req(trkUrl + '&tapl=100&tavl=0&tavn=' + Enc(tavn));
		}
	}
	
	
	function BeforeUnloadReq()
	{
		var tpv = Math.round(((new Date()).getTime() - pageViewDisplayDate.getTime()) / 1000),
			tav = viewTotalDuration + viewDuration,
			img = new Image(1, 1);
		
		if (adPosition < 1)
			adPosition = 1;
		
		if (adPosition > 5)
			adPosition = 5;
		
		img.src = trkUrl + '&taps=' + adPosition + '&tav=' + tav + '&tai=' + interactTotalDuration + '&tpv=' + tpv + '&tpi=' + pageInteractTotalDuration;
	}
	
	
	setInterval(CheckView, 1000);
	CheckInteract();
	CheckPageInteract();
	CreateSendVideoPlayFnc();
	AddWinEvt("beforeunload", BeforeUnloadReq);
}


function Reports_Req(url)
{
	var o = document.createElement("DIV");
	if (o)
	{
		o.style.position = 'absolute';
		o.style.top = '0px';
		o.style.left = '0px';
		o.style.width = '1px';
		o.style.height = '1px';
		o.style.display = 'block';
		 				
		o.innerHTML =
			'<div style="position:absolute !important; top:0px; left:0px; width:1px !important; height:1px !important; z-index:1 !important;' +
			' overflow:hidden !important; display:block; visibility:visible; filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;">' +
			' <img src="' + url + '" width="1" height="1" border="0"/>' +
			'</div>';
		
		document.body.insertBefore(o, document.body.firstChild)
	}
}



/* End - General use functions */



















/* Begin - In-Page: Image Ad */


function Img(B,S)
{
	var idx = TagIdx,
		id = 'ZTRImg' + idx,
		tag = Tags[idx],
		clickUrl = GetClickUrl(idx, B.u, S),
		imgFile = GetFileUrl(S.dir, B.f),
		isEmptyZone = B.w < 1 && B.h < 1;

	Dbg_New(id, 'Image', B, S, idx, tag.ContId);
	ExecuteCallbackCode(idx, id, B.w, B.h, isEmptyZone);
	
	window[id + 'Click'] = function(){window.open(clickUrl, B.ut);}
	
	
	var c = GetElem(tag.ContId);
	if (c)
	{
		if (isEmptyZone)
		{
			SetStyles(c, 0, 0, null, false, true);
		}
		else
		{
			SetStyles(c, B.w, B.h);
			c.innerHTML = GetImgHtml(null, imgFile, B.w, B.h, (B.u ? 'window.'+id+'Click();' : ''), (B.u ? 'cursor:pointer;' : ''), B.tit);
		}
	}
	
	Dbg_Msg(id, 'Send display tracking request.');
	DT(S);
	
	if (B.backup)
		SendActionForBackup(S);
	
	if (!isEmptyZone)
	{
		Reports_Init(S, id, tag.ContId, B.w, B.h);
	}
}


/* End - In-Page: Image ad */










/* Begin - In-Page: Flash ad */


function Flash(B,S)
{
	if (!B.u) B.u = unescape(B.fvars.clickTAG.substr(4));
	if (!B.ut) B.ut = B.fvars.Stg;
		

	if (B.backupimg && Info.FlashVer < B.fv)
	{
		Img({backup:1, f:B.backupimg, w:B.w, h:B.h, tit:'', u:B.u, ut:B.ut}, S);
		return;
	}

	var idx = TagIdx,
		id = 'ZTRFla' + idx,
		tag = Tags[idx],
		clickUrl = GetClickUrl(idx, B.u, S),
		flashUrl = GetFileUrl(S.dir, B.f),
		flashVars = GetFlashVars(idx, id, B.u, B.ut, S, true),
		ZIdxM = GetZIdx(tag, 'normal')
	;

	Dbg_New(id, 'Flash', B, S, idx, tag.ContId);
	ExecuteCallbackCode(idx, id, B.w, B.h);
	

	if (B.prfstrg)
		flashVars = flashVars + '&prfstrg=' + Enc(B.prfstrg);

	
	var c = GetElem(tag.ContId);
	if (c)
	{
		SetStyles(c, B.w, B.h);
		c.innerHTML = GetFlashHtml(id, flashUrl, B.w, B.h, flashVars);
		//setTimeout(function(){c.innerHTML = GetFlashHtml(id, flashUrl, B.w, B.h, flashVars);}, 1000);
	}


	if (B.cm)
	{
		Dbg_Msg(id, 'Enable click mask.');
		window[id + 'Click'] = function() {window.open(clickUrl, B.ut);}
		CreateClickMask(id + 'M', B.w, B.h, 'window.' + id + 'Click();', ZIdxM);

		window[id + 'Pos'] = function()
		{
			var b = GetElem(tag.ContId), m = GetElem(id + 'M'), co;
			
			if (!b || !m) return;
			
			co = GetXY(b);
			
			m.style.left = co.x + 'px';
			m.style.top = co.y + 'px';
			m.style.zIndex = ZIdxM;
		}
		
		AddWinEvt('resize', window[id + 'Pos']);
		AddWinEvt('scroll', window[id + 'Pos']);
		setInterval('window.' + id + 'Pos();', 500);
	}

	Dbg_Msg(id, 'Send display tracking request.');
	DT(S);
	
	Reports_Init(S, id, (B.cm?id+'M':tag.ContId), B.w, B.h);
}


/* End - In-Page: Flash Ad */










/* Begin - Over-the-Page: Expandable Flash Ad */


function ExpandableFlash(B,S)
{
	if (!B.u) B.u = unescape(B.fvars.clickTAG.substr(4));
	if (!B.ut) B.ut = B.fvars.Stg;

	
	var idx = TagIdx,
		id = 'ZTRExpFla' + idx,
		tag = Tags[idx],
		clickUrl = GetClickUrl(idx, B.u, S),
		flashUrl = GetFileUrl(S.dir, B.f),
		flashVars = GetFlashVars(idx, id, B.u, B.ut, S, Info.UseFn),
		Open = B.start.toLowerCase() == 'open',
		ZIdxClose = GetZIdx(tag, 'normal'),
		ZIdxOpen = GetZIdx(tag, 'expandable'),
		iSM = B.sm ? B.sm : 0,
		zFixedPos = Info.FixedPos && iSM == 3,
		ws = GetWinW(),
		hs = GetWinH(),
		showBackup = (B.backupimg && B.backupimg != '' && Info.FlashVer < B.fv)
	;

	Dbg_New(id, 'Expandable Flash', B, S, idx, id + 'E');
	ExecuteCallbackCode(idx, id, B.wclose, B.hclose);
	
	
	if (B.minw && B.minw >= ws)
	{
		Dbg_Msg(id, 'This banner is not showed because the browser width is too small.');
		return;
	}
	if (B.minh && B.minh >= hs)
	{
		Dbg_Msg(id, 'This banner is not showed because the browser height is too small.');
		return;
	}

	
	var RClose='',ROpen='auto auto auto auto',ETop=0,ELeft=0;

	switch(B.di.toLowerCase())
	{
		case 'up':
			ETop=B.hclose-B.hopen;
			RClose=(B.hopen-B.hclose) +'px auto auto auto';
			break;
		
		case 'left':
			ELeft=B.wclose-B.wopen;
			RClose='auto auto auto '+ (B.wopen - B.wclose) + 'px';
			break;
		
		case 'down':
			RClose='auto auto '+ B.hclose +'px auto';
			break;

		case 'right':
			RClose='auto '+ B.wclose +'px auto auto';
			break;
		
		case 'all':
			ETop=-B.yclose;
			ELeft=-B.xclose;
			RClose=B.yclose +'px '+ (B.xclose + B.wclose) +'px '+ (B.yclose + B.hclose) +'px '+ B.xclose +'px ';
			break;
		
		
		default:
			break;
	}


	var c = GetElem(tag.ContId);
	if (c)
	{
		SetStyles(c, B.wclose, B.hclose);
	}
	
	if (Info.IE && !showBackup && !Info.UseFn)
		document.write(FSCmd(id));
	
	
	
	window[id+'CB'] = function()
	{
		var oD;
		
		if (Info.SelectsMask == 2)
		{
			oD = document.createElement("DIV");
			if (oD)
			{
				oD.id = id + 'I';
				oD.style.position = zFixedPos?'fixed':'absolute';
				oD.style.left = '-10000px';
				oD.style.top = '-10000px';
				oD.style.width = B.wopen + 'px';
				oD.style.height = B.hopen + 'px';
				oD.style.clip = 'rect('+ (Open ? ROpen : RClose) +')';
				oD.style.display = '';
				oD.style.overflow = 'visible';
				oD.style.zIndex = (Open ? ZIdxOpen : ZIdxClose) - 1;
				oD.innerHTML = '<iframe src="" style="position:absolute; top:0px; left:0px; width:'+B.wopen+'px; height:'+B.hopen+'px; z-index: '+(oD.style.zIndex)+'; visibility:visible; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe>';
				oD = document.body.insertBefore(oD, document.body.firstChild);
			}
		}
		
		
		var htmlCode = '';
		
		
		
		if (showBackup)
		{
			var hasCloseImg = B.backupimgclose && B.backupimgclose != '';
		
			window[id + 'BIOOvr'] = function()
			{
				if (!hasCloseImg)
					window[id + 'Fn']('open', '');
			}
		
			window[id + 'BIOOut'] = function()
			{
				if (hasCloseImg)
				{
					Show(id + 'bic');
					Hide(id + 'bio');
				}
			
				window[id + 'Fn']('close', '');
			}
			
			window[id + 'BICOvr'] = function()
			{
				Hide(id + 'bic');
				Show(id + 'bio');
					
				window[id + 'Fn']('open', '');
			}
		
			window[id + 'BICOut'] = function()
			{
			}

			window[id + 'BIClick'] = function()
			{
				window.open(clickUrl, B.ut);
			}

			
			htmlCode = 
				'<img id="' + id + 'bio" src="' + GetFileUrl(S.dir, B.backupimg) + '" width="' + B.wopen + '" height="' + B.hopen + '" border="0" alt="" ' +
				' style="cursor:pointer; ' + (hasCloseImg ? 'display:none;' : '') + '" ' +
				' onMouseOver="window.' + id + 'BIOOvr();" onMouseOut="window.' + id + 'BIOOut();" onClick="window.' + id + 'BIClick();"/>'
			;
			
			if (hasCloseImg)
				htmlCode += 
					'<img id="' + id + 'bic" src="' + GetFileUrl(S.dir, B.backupimgclose) + '" width="' + B.wclose + '" height="' + B.hclose + '" border="0" alt="" style="cursor:pointer;" ' +
					' onMouseOver="window.' + id + 'BICOvr();" onMouseOut="window.' + id + 'BICOut();" onClick="window.' + id + 'BIClick();"/>';
			
			SendActionForBackup(S);
		}
		else
		{
			htmlCode = GetFlashHtml(id, flashUrl, B.wopen, B.hopen, flashVars);
		}

		

		oD = document.createElement("DIV");
		if (oD)
		{
			oD.id = id + 'E';
			oD.style.position = zFixedPos?'fixed':'absolute';
			oD.style.left = '-10000px';
			oD.style.top = '-10000px';
			oD.style.width = B.wopen + 'px';
			oD.style.height = B.hopen + 'px';
			oD.style.clip = 'rect('+ (Open ? ROpen : RClose) +')';
			oD.style.display = '';
			oD.style.overflow = 'visible';
			oD.style.zIndex = Open ? ZIdxOpen : ZIdxClose;
			oD.innerHTML = htmlCode;
			oD = document.body.insertBefore(oD, document.body.firstChild);
		}
	}

	window[id+'Fn'] = function(c,a)
	{
		if (a != 'auto')
			Dbg_Msg(id, 'Cmd: '+ c + ' ' + a);
	
		var oE = GetElem(id+'E'), oI = GetElem(id+'I');
		
		if (oE)
		{
			c=c.toLowerCase();
			
			if (!(typeof(a) == "string" && a.toLowerCase() == "auto"))
				RecordCmd(S, c);
			
			
			if (c=="open")
			{
				window[id+'FnOpen'] = true;
				
				if (Info.SelectsMask == 1)
				{
					HideSelects(true);
				}
				else if (Info.SelectsMask == 2)
				{
					oI.style.clip = 'rect('+ROpen+')';
					oI.style.zIndex = ZIdxOpen - 1;
				}

				oE.style.clip = 'rect('+ROpen+')';
				oE.style.zIndex = ZIdxOpen;
				
				if(B.opentime>0)
				{
					if(window[id+'TId'])clearTimeout(window[id+'TId']);
					window[id+'TId']=setTimeout('window["'+id+'Fn"]("close","");',B.opentime*1000);
					Dbg_Msg(id, 'Switch to close state in ' + B.opentime + ' seconds.');
				}
			}
			else if (c=="close")
			{
				window[id+'FnOpen'] = false;
				
				if(window[id+'TId'])clearTimeout(window[id+'TId']);
				
				oE.style.zIndex = ZIdxClose;
				oE.style.clip = 'rect('+RClose+')';

				if (Info.SelectsMask == 1)
				{
					HideSelects(false);
				}
				else if (Info.SelectsMask == 2)
				{
					oI.style.clip = 'rect('+RClose+')';
					oI.style.zIndex = ZIdxClose - 1;
				}
			}
		}
	}


	window[id+'Pos'] = function()
	{
		var o = GetElem(tag.ContId);
		if (!o) return;
		
		
		var ws = GetWinW();
		var hs = GetWinH();
		var oXY = GetXY(o);
		
		if (B.minw || B.minh)
		{
			if (B.minw >= ws || B.minh >= hs)
			{
				if (!window[id+'BannerBecomeInvisible'])
				{
					Dbg_Msg(id, 'This banner is no more visible because the browser is too small.');
					window[id+'BannerBecomeInvisible'] = true;
				}
				o.style.display = 'none';
			}
			else
			{
				if (window[id+'BannerBecomeInvisible'])
				{
					Dbg_Msg(id, 'This banner is visible again.');
					window[id+'BannerBecomeInvisible'] = false;
				}
			
				o.style.display = '';
			}
		}
		
		if (oXY.x >= -1000 && oXY.y >= -1000)
		{
			var iL = 0, iT = 0, zOpen = window[id+'FnOpen'] ? true : false, iSM = B.sm ? B.sm : 0;
			
			if (iSM > 0 && ((iSM == 1 && zOpen) || (iSM == 2 && !zOpen) || iSM == 3))
			{
				if (!zFixedPos)
				{
					iL = document.pageXOffset?document.pageXOffset:document.documentElement.scrollLeft||document.body.scrollLeft;
					iT = document.pageYOffset?document.pageYOffset:document.documentElement.scrollTop||document.body.scrollTop;
				}
			}
			
			
			o = GetElem(id+'I');
			if (o)
			{
				o.style.left = iL + ELeft + oXY.x + 'px';
				o.style.top = iT + ETop + oXY.y + 'px';
				
				if (B.minw || B.minh)
				{
					o.style.display = B.minw >= ws || B.minh >= hs ? 'none' : '';
				}
			}
			
			o = GetElem(id+'E');
			if (o)
			{
				o.style.left = iL + ELeft + oXY.x + 'px';
				o.style.top = iT + ETop + oXY.y + 'px';
				
				if (B.minw || B.minh)
				{
					o.style.display = B.minw >= ws || B.minh >= hs ? 'none' : '';
				}
			}
		}
	}
	
	
	window[id+'Load'] = function()
	{
		SendMsgToFlash(id, "window_onload_event");
	}
	
	
	window[id+'CB']();
	
	
	if (Open)
	{
		Dbg_Msg(id, 'Start in open state.');
		setTimeout('window.'+id+'Fn("open","auto");',100);
	}
	else
	{
		Dbg_Msg(id, 'Start in close state.');
		setTimeout('window.'+id+'Fn("close","auto");',100);
	}
	
	if (iSM > 0)
		Dbg_Msg(id, 'Enable sticky mode.');
	

	setTimeout('window.'+id+'Pos();',100);
	
	AddWinEvt('load', function(){setTimeout('window.'+id+'Load();',2000);});
	AddWinEvt('resize', window[id+'Pos']);
	AddWinEvt('scroll', window[id+'Pos']);
	setInterval('window.'+id+'Pos();',200);

	Dbg_Msg(id, 'Send display tracking request.');
	DT(S);
	
	Reports_Init(S, id, id + 'E', B.wclose, B.hclose);
}


/* End - Over-the-Page: Expandable Flash Ad */










/* Begin - Over-the-Page: Floating Flash Ad */


function FloatingFlash(B,S)
{
	if (!B.u) B.u = unescape(B.fvars.clickTAG.substr(4));
	if (!B.ut) B.ut = B.fvars.Stg;


	var idx = TagIdx,
		id = 'ZTRFloatFla' + idx,
		tag = Tags[idx],
		clickUrl = GetClickUrl(idx, B.u, S),
		flashUrl = GetFileUrl(S.dir, B.f),
		flashVars = GetFlashVars(idx, id, B.u, B.ut, S, Info.UseFn),
		ZIdxOpen = GetZIdx(tag, 'floating'),
		iSM = B.scroll ? B.scroll : 0,
		zFixedPos = Info.FixedPos && iSM > 0,
		iCen = 0,
		showBackup = (B.backupimg && B.backupimg != '' && Info.FlashVer < B.fv),
		isClosed = false,
		isShowedAfterLanding = (B.land && B.land.show && (B.land.show == 2 || B.land.show == 3)),

        showBackupClose = typeof(B.cbi) != 'undefined' && B.cbi != '',
        closeImgUrl = ''
	;


	Dbg_New(id, 'Floating Flash', B, S, idx, id + 'B');


	if (showBackup && B.fsc)
	{
		B.mrg = {h:2,v:2,hp:0,vp:0};
		
		switch (B.fsc.h)
		{
			case 'l':B.mrg.h=1;B.mrg.hp=B.fsc.l;break;
			case 'c':break;
			case 'r':B.mrg.h=3;B.mrg.hp=B.fsc.r;break;
		}

		switch (B.fsc.v)
		{
			case 't':B.mrg.v=1;B.mrg.vp=B.fsc.t;break;
			case 'm':break;
			case 'b':B.mrg.v=3;B.mrg.vp=B.fsc.b;break;
		}			
		
		B.fsc = null;
	}

    if( showBackupClose )
    {
        closeImgUrl = GetFileUrl(S.dir, B.cbi);
    }

	window[id+'PosDim'] = function()
	{
		var w=0,h=0,t=0,l=0;

		var ws = GetWinW();
		var hs = GetWinH();
		
		if(B.cen)
		{
			l=parseInt((ws-B.w)/2);
			t=parseInt((hs-B.h)/2);
			w=B.w;
			h=B.h;
		}
		else if(B.fsc)
		{
			l=B.fsc.l;
			t=B.fsc.t;
			w=ws-B.fsc.r-B.fsc.l;
			h=hs-B.fsc.t-B.fsc.b;
		}
		else if(B.mrg)
		{
			switch(B.mrg.h)
			{
				case 1:l=B.mrg.hp;break;
				case 2:l=parseInt((ws-B.w)/2); iCen++; break;
				case 3:l=ws-B.w-B.mrg.hp;break;
			}

			switch(B.mrg.v)
			{
				case 1:t=B.mrg.vp;break;
				case 2:t=parseInt((hs-B.h)/2); iCen++; break;
				case 3:t=hs-B.h-B.mrg.vp;break;
			}

			w=B.w;
			h=B.h;
		}

		if (!zFixedPos)
		{
			l+=document.pageXOffset?document.pageXOffset:document.documentElement.scrollLeft||document.body.scrollLeft;
			t+=document.pageYOffset?document.pageYOffset:document.documentElement.scrollTop||document.body.scrollTop;
		}

		return {w:w,h:h,t:t,l:l};
	}


	
	var oD = document.createElement("DIV"), pd = window[id+'PosDim'](), salign='lt', sHtml = '', w = pd.w + 'px', h = pd.h + 'px', scale = 'showall';
	if (oD)
	{
		if (Info.IE && !showBackup && !Info.UseFn)
			document.write(FSCmd(id));
		
		oD.id = id + 'B';
		oD.style.position = zFixedPos?'fixed':'absolute';
		oD.style.left = pd.l + 'px';
		oD.style.top = pd.t + 'px';
		oD.style.width = w;
		oD.style.height = h;
		oD.style.display = isShowedAfterLanding ? 'none' : 'block';
		oD.style.overflow = 'visible';
		oD.style.zIndex = ZIdxOpen;
		
		if(B.fsc){w=h="100%";salign=B.fsc.h+B.fsc.v;scale='noscale';}

		if (Info.SelectsMask == 2)
		{
			if (B.mm && (iCen == 2 || B.cen))
				sHtml = '<iframe src="" id="'+id+'I" style="position:absolute; left:-80px; top:-80px; width:'+(pd.w+160)+'px; height:'+(pd.h+160)+'px; visibility:visible; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe>';
			else
				sHtml = '<iframe src="" id="'+id+'I" style="position:absolute; left:0px; top:0px; width:'+w+'; height:'+h+'; visibility:visible; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe>';
		}
		
		if (B.mm && (iCen == 2 || B.cen))
			sHtml = sHtml + '<div style="position:absolute; left:-80px; top:-80px; width:'+(pd.w+160)+'px; height:'+(pd.h+160)+'px; visibility:visible; display:block; background-color:#000000; filter:alpha(opacity=50); -moz-opacity:0.50; opacity:0.50;"></div>';
		
		
		var htmlCode = '';
		
		if (showBackup)
		{
			window[id + 'BIClose'] = function(){window[id + 'Fn']('close', '');}
			window[id + 'BIClick'] = function(){window.open(clickUrl, B.ut);}
			
			htmlCode = 
				'<img src="' + GetFileUrl(S.dir, B.backupimg) + '" width="' + B.w + '" height="' + B.h + '" border="0" alt="" ' +
				' style="cursor:pointer; position:absolute; left:0px; top:0px;" onClick="window.' + id + 'BIClick();"/>' +
				
				'<div style="width:' + B.bkw + 'px; height:' + B.bkh + 'px; position:absolute; left:' + B.bkx + 'px; top:' + B.bky + 'px; ' +
				' overflow:hidden; display:block; visibility:visible; cursor:pointer; background-color:#ffffff; ' + 
				(Preview ? ' filter:alpha(opacity=70); -moz-opacity:0.7; opacity:0.7;' : ' filter:alpha(opacity=1); -moz-opacity:0.01; opacity:0.01;') +
				'font-size:9px;" onClick="window.' + id + 'BIClose();">' +
				(Preview ? 'This close button mask is shown only in Preview.' : '') +
				'</div>'
			;
		
			SendActionForBackup(S);
		}
		else
		{
			htmlCode = GetFlashHtml(id, flashUrl, w, h, flashVars, scale, salign);
		}
		
		
		
		oD.innerHTML = sHtml + 
			'<div id="'+id+'E" style="position:absolute; left:0px; top:0px; width:'+w+'; height:'+h+'; visibility:visible;">' +
				htmlCode +
			'</div>' +
            ( 
            showBackupClose?
            '<div onclick="window.'+id+'Fn(\'close\');" style="position:absolute; left:'+B.cbl+'px; top:'+B.cbt+'px; width:'+B.cbw+'px; height:'+B.cbh+'px; cursor:pointer; overflow:hidden;">' + 
                '<img src="'+closeImgUrl+'" width="'+B.cbw+'" height="'+B.cbh+'" border="0"/>' +
            '</div>'
            :
            ''
            );
		
		oD = document.body.insertBefore(oD, document.body.firstChild);
	}
	

	if(B.land)
	{
		Dbg_Msg(id, 'Enable Landing Zone.');
		
	
		var htmlCode = '';
		
		if (showBackup)
		{
			window[id + 'LZBIOpen'] = function(){window[id + 'LZFn']('open', '');}
			window[id + 'LZBIClose'] = function(){window[id + 'LZFn']('close', '');}
			window[id + 'LZBIClick'] = function(){window.open(clickUrl, B.ut);}
			
			htmlCode = 
				'<img src="' + GetFileUrl(S.dir, B.land.backupimg) + '" width="' + B.land.w + '" height="' + B.land.h + '" border="0" alt="" ' +
				' style="cursor:pointer; positionx:absolute; left:0px; top:0px;" onClick="window.' + id + 'LZBIOpen();"/>'
			;
		}
		else
		{
			if (Info.IE && !Info.UseFn)
				document.write(FSCmd(id+'LZ'));
				
			flashUrl = GetFileUrl(S.dir, B.land.f);
			flashVars = GetFlashVars(idx, id + 'LZ', B.u, B.ut, S, Info.UseFn);
			
			htmlCode = GetFlashHtml(id+'LZ', flashUrl, B.land.w, B.land.h, flashVars);
		}


		var c = GetElem(tag.ContId);
		if (c)
		{
			SetStyles(c, B.land.w, B.land.h);
			c.style.visibility = B.land.show ? 'visible' : 'hidden';
			c.innerHTML = htmlCode;
		}
	}


	window[id+'Fn'] = function(c,a)
	{
		Dbg_Msg(id, 'Cmd: ' + c + ' ' + a);

		var oB = GetElem(id+'B');
		
		if (!oB)
			return;
			
		c=c.toLowerCase();
		
		if (!(typeof(a) == "string" && a.toLowerCase() == "auto"))
			RecordCmd(S, c);
		
		
		if (B.land)
		{
			if(c=="open")
			{
				if (B.land.show == 0 || B.land.show == 2)
				{
					var oL = GetElem(tag.ContId);
					if (oL)
						oL.style.visibility = "hidden";
				}
				
				if (B.land.show == 2 || B.land.show == 3)
				{
					oB.style.display = 'block';
				}
			
				if (Info.SelectsMask == 1)
					HideSelects(true);

				oB.style.zIndex=ZIdxOpen;
				oB.style.display="";
			}
			else if(c=="close"||c=="close_both")
			{
				clearInterval(window[id+'RFnIntervalId']);
				
				oB.style.display = "none";
				
				if (Info.SelectsMask == 1)
					HideSelects(false);

				if (B.land.show == 0 || B.land.show == 2)
				{
					var oL = GetElem(tag.ContId);
					if (oL)
						oL.style.visibility = "visible";
				}
				
				if (B.land.show == 2 || B.land.show == 3)
				{
					oB.style.display = 'none';
				}
			}
		}
		else
		{
			if(c=="open" && isClosed == false)
			{
				if (Info.SelectsMask == 1)
					HideSelects(true);

				oB.style.zIndex=ZIdxOpen;
				oB.style.display="";
			}
			else if(c=="close"||c=="close_both")
			{
				isClosed = true;
				
				clearInterval(window[id+'RFnIntervalId']);
				
				oB.style.display = "none";
				oB.innerHTML = "&nbsp;";
				
				if (Info.SelectsMask == 1)
					HideSelects(false);
			}
		}
	}

	window[id+'LZFn'] = function(c,a)
	{
		window[id+'Fn'](c,a);	
	}

	window[id+'RFn'] = function()
	{
		var o = GetElem(id+'B'), p;
		
		if (o && o.style.display != "none")
		{
			p=window[id+'PosDim']();
			o.style.width=p.w+'px';o.style.height=p.h+'px';o.style.left=p.l+'px';o.style.top=p.t+'px';
		}
	}

	if (!isShowedAfterLanding)
		setTimeout(function () { window[id + 'Fn']('open', 'auto') }, 500);

	AddWinEvt('resize', window[id + 'RFn']);
	
	if (B.scroll)
	{
		Dbg_Msg(id, 'Enable sticky mode.');
		window[id + 'RFnIntervalId'] = setInterval('window.'+ id +'RFn();', 500);
		AddWinEvt('scroll', window[id + 'RFn']);
	}

	if (!isShowedAfterLanding && B.secs > 0)
	{
		Dbg_Msg(id, 'Switch to close state in ' + B.secs + ' seconds.');
		setTimeout('window.' + id + 'Fn("close", "auto");', B.secs * 1000);
	}

	Dbg_Msg(id, 'Send display tracking request.');
	DT(S);
	
	Reports_Init(S, id, id + 'B', B.w, B.h);
}


/* End - Over-the-Page: Floating Flash Ad */







/* Begin - In-Page: Html Ad */

var Html_Scripts = [];
var Html_ScriptsIdx = -1;


function Html(B,S)
{
	var idx = TagIdx,
		id = 'ZTRHtml' + idx,
		tag = Tags[idx],
		ZIdx = GetZIdx(tag, 'html3rdParty');
	
	if (!B.width) B.width = 0;
	if (!B.height) B.height = 0;
	
	
	if (Info.FlashVer < B.fv && B.backupimg && B.backupimg != '' && B.width > 0 && B.height > 0)
	{
		Img({backup:1, f:B.backupimg, w:B.width, h:B.height, tit:'', u:B.url, ut:'_blank'}, S);
		return;
	}
	
	
	ExecuteCallbackCode(idx, id, B.width, B.height);
	
	Dbg_New(id, 'Html Code', B, S, idx, tag.ContId);
	
	var c = GetElem(tag.ContId);
	if (!c)
	{
		Dbg_Msg(id, 'The DIV container was not found.');
		return;
	}	
	
	Dbg_Msg(id, 'Send display tracking request.');
	DT(S);

	
	var re = /(\<script|\<scr)/i,
		thirdParty = re.test(B.h);
	
	if (!thirdParty)
	{
		SetStyles(c, B.width, B.height, null, false);
		c.innerHTML = TrkHtmlCode(idx, S, B.h);
		
		return;
	}
	
	
	SetStyles(c, B.width, B.height);


	if (B.width == 0 && B.height == 0)
		ZIdx++;

	
	Html_Scripts.push({B:B, S:S, Idx:idx});


	var cRef, bRef, lastHeight;

	
	function Pos()
	{
		var c = GetElem(tag.ContId), b = GetElem(id + 'B');
		
		cRef = c;
		bRef = b;
		
		if (!c || !b) return;

		var co = GetXY(c);
		
		b.style.left = co.x + 'px';
		b.style.top = co.y + 'px';
		b.style.zIndex = ZIdx;
	}
	
	
	AddWinEvt('resize', Pos);
	AddWinEvt('scroll', Pos);
	//setTimeout(Pos, 100);
	//setTimeout(Pos, 1000);
	setInterval(Pos, 200);
	
	
	if (B.gm && B.gm == 1)
	{
		lastHeight = B.height;
		
		function updateContHeight()
		{
			if (!cRef || !bRef || !bRef.scrollHeight) return;

			bRef.style.overflow = "scroll";

			var sh = bRef.scrollHeight;

			bRef.style.overflow = "visible";
			
			if (lastHeight == sh || sh < B.height)
				return;
			
			lastHeight = sh;
			cRef.style.height = lastHeight + 'px';
		}
		
		setInterval(updateContHeight, 25);
	}
	
	Reports_Init(S, id, tag.ContId, B.width, B.height);
}

window.ZTRADW = function()
{
	Html_ScriptsIdx++;
	
	var sc = Html_Scripts[Html_ScriptsIdx];
	if (!sc)
	{
		var id2 = 'ZTRHtml' + Html_ScriptsIdx;
		
		document.write('<div id="' + id2 + 'BSEmpty" style="display:none !important;"></div>');
		
		function HideAndRemoveEmpty(i)
		{
			var bs = GetElem(i + 'BSEmpty');
			
			if (bs && bs.parentNode)
			{
				bs.parentNode.style.display = 'none';
				
				if (bs.parentNode.parentNode && bs.parentNode.parentNode.removeChild)
					bs.parentNode.parentNode.removeChild(bs.parentNode);
			}
		}
		
		setTimeout(function(){HideAndRemoveEmpty(id2);}, 1000);
		
		return;
	}
		

	var idx = sc.Idx,
		id = 'ZTRHtml' + idx,
		tag = Tags[idx],
		ZIdx = GetZIdx(tag, 'html3rdParty'),
		ZHC = sc.B.h,
		ZPosition = 'absolute',
		ZWidth = sc.B.width,
		ZHeight = sc.B.height;

	if (sc.Sfx)
	{
		id = 'ZtrLTRBra' + idx + sc.Sfx;
		ZHC = sc.B.hc;
		if (typeof sc.B.sticky != 'undefined' && sc.B.sticky && Info.FixedPos)
			ZPosition = 'fixed';
		ZWidth = sc.B.w;
		ZHeight = sc.B.h;
	}

	document.write('<div id="' + id + 'BS" style="display:none !important;"></div>');
	document.write(TrkHtmlCode(idx, sc.S, ZHC));
	
	
	function Abs()
	{
		var o, bs = GetElem(id + 'BS');
		
		if (!bs || !bs.parentNode)
		{
			setTimeout('window.' + id + 'ChangeId();', 100);
			return;
		}
		
		o = bs.parentNode;
		
		//if (sc.B.width == 0)
		//	return;
		
		o.id = id + 'B';
		o.style.position = ZPosition;
		o.style.left = '-10000px';
		o.style.top = '-10000px';
		o.style.width = ZWidth + 'px';
		if (!sc.B.gm || sc.B.gm != 1)
			o.style.height = ZHeight + 'px';
		o.style.display = 'block';
		o.style.visibility = 'visible';
		o.style.overflow = 'visible';
		o.style.zIndex = ZIdx;
	}
	
	Abs();
}


/* End - In-Page: Html Ad */










/* Begin - In-Page: Iframe Ad */


function Iframe(B,S)
{
	var idx = TagIdx,
		id = 'ZTRIframe' + idx,
		tag = Tags[idx];


	if (Info.FlashVer < B.fv && B.backupimg && B.backupimg != '')
	{
		Img({backup:1, f:B.backupimg, w:B.w, h:B.h, tit:'', u:B.url, ut:'_blank'}, S);
		return;
	}


	ExecuteCallbackCode(idx, id, B.w, B.h);
	
	Dbg_New(id, 'Html Restricted Code (IFRAME)', B, S, idx, tag.ContId);
	Dbg_Msg(id, 'Display tracking request is sent by the IFRAME.');

	var c = GetElem(tag.ContId);
	if (c)
	{
		SetStyles(c, B.w, B.h);
		c.innerHTML = '<iframe src="' + UQ(idx, B, S) + '" width="' + B.w + '" height="' + B.h + '" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe>';
	}
}


/* End - In-Page: Iframe Ad */










/* Begin - In-Page: Pop Ad */


function Pop(B,S)
{
	var idx = TagIdx,
		id = 'ZTRPop' + idx;

	Dbg_New(id, 'Pop', B, S, idx);
	Dbg_Msg(id, 'Display tracking request is sent by the Pop window. When opening the window is blocked request is not sent.');


	var ws=screen.availWidth,hs=screen.availHeight,l=0,t=0;

	switch(B.p.h)
	{
		case 1:l=B.p.hp;break;
		case 2:l=Math.round((ws-B.w-10)/2);break;
		case 3:l=ws-B.w-B.p.hp-10;break;
	}

	switch(B.p.v)
	{
		case 1:t=B.p.vp;break;
		case 2:t=Math.round((hs-B.h-30)/2);break;
		case 3:t=hs-B.h-B.p.vp-30;break;
	}

	var a=B.attr,s="width="+B.w+",height="+B.h+",left="+l+",top="+t+",location="+a.l+",menubar="+a.m+",resizable="+a.r+",scrollbars="+a.b+",status="+a.s+",toolbar="+a.t;

	window.open(UQ(idx, B, S), 'popid' + parseInt(Math.random() * Math.pow(10,17)), s);
	if (!B.t)window.focus();
}


/* End - In-Page: Pop Ad */










/* Begin - In-Page: Transitional Ad */


function Transitional(B,S)
{
	var idx = TagIdx,
		id = 'ZTRTrans' + idx,
		tag = Tags[idx],
		clk = S.click.toLowerCase(),
		vDU = [], vN = [], vM = [], sD='__',
		oHrefRE = /\.as|\.js|\.swf|\.flv|\.zip|\.rar|\.dll|\.exe|\.m3u|\.mp3|\.wav|\.mpeg|\.jpg|\.jpeg|\.gif|\.png/,
		oClassRE = /ztr_skip_this_link|skip_this_link/,
		oSkipFunction = null;
	
	Dbg_New(id, 'Transitional', B, S, idx);
	Dbg_Msg(id, 'Display tracking request is sent when the transitional page is seen.');

	
	if (tag.SiteParams.Transitional && typeof(tag.SiteParams.Transitional.SkipFunction) == 'function')
		oSkipFunction = tag.SiteParams.Transitional.SkipFunction;
	

	var Q = UQ(idx, B, S) + '&dtime=' + escape(B.dtime);
	
	
	window[id+'Ov'] = function(){window.status=this[id+'h'];return true;}
	window[id+'Ou'] = function(){window.status="";return true;}
	window[id+'M']	= function(u)
	{
		var i,z,d='',v=u.split('://');
		
		if(v.length>1){v=v[1].split('/');d=Trim(v[0]);}
		
		if(!d)return false;
		
		if(d==sD&&document.URL&&u.indexOf(document.URL.toLowerCase()+'#')>-1)return false;
		
		for(i=0;i<vN.length;i++){if(d.indexOf(vN[i])>-1)return false;}
		for(i=0;i<vM.length;i++){if(d.indexOf(vM[i])>-1)return true;}
		
		switch(parseInt(B.mode))
		{
			case 1:z=d.indexOf(sD)>-1;break;
			case 2:z=d.indexOf(sD)<0;break;
			default:z=true;break;
		}
		
		return z;
	}


	window[id+'L'] = function(o)
	{
		if(!o.href)return;

		var u=o.href.toLowerCase(),h,cn;
		if(u.indexOf("javascript:")<0&&u.indexOf(clk)<0)
		{
			if (oSkipFunction)
			{
				if (oSkipFunction(o))return;
			}
			else
			{
				if (oHrefRE.test(u))return;
				if (o.className && o.className.toLowerCase && oClassRE.test(o.className.toLowerCase()))return;
			}
		
			if(window[id+'M'](u))
			{
				vDU[vDU.length] = '<br/>' + u + ' &nbsp; &nbsp; &nbsp; &nbsp; ' + o.innerHTML;
				
				if(Info.IE)h=o.innerHTML;
				o[id+'h']=o.href;
				o.href=Q+'&url='+escape(o.href);
				if(Info.IE&&h!="")o.innerHTML=h;
				if(!o.onmouseover)o.onmouseover=window[id+'Ov'];
				if(!o.onmouseout)o.onmouseout=window[id+'Ou'];
			}
		}
	}

	window[id+'A'] = function()
	{
		Dbg_Msg(id, 'Page loading is completed. Start replacing URLs.');
	
		var i,v,o,s;
		
		if(document.URL){v=document.URL.split('://');if(v.length>1){v=v[1].split('/');sD=Trim(v[0]).toLowerCase();}}
		
		if(B.no){v=B.no.toLowerCase().split(/\s+/);		for(i=0;i<v.length;i++)vN[vN.length]=v[i];}
		if(B.must){v=B.must.toLowerCase().split(/\s+/);	for(i=0;i<v.length;i++)vM[vM.length]=v[i];}
		
		v=document.getElementsByTagName("A");for(i=0;i<v.length;i++)window[id+'L'](v[i]);
		v=document.getElementsByTagName("AREA");for(i=0;i<v.length;i++)window[id+'L'](v[i]);
		
		Dbg_Msg(id, 'URLs that display the transitional page:<br/>' + vDU.join('') + '<br/><br/>');
	}

	AddWinEvt('load', window[id+'A']);
}


/* End - In-Page: Transitional Ad */











/* Dynamic loaded templates */
var DLT = {};

function DL(name, B, S, B2)
{
	var send = false;

	if (!DLT[name])
	{
		DLT[name] = [];
		send = true;
	}
	
	if (S && typeof(S.etrkc) != "undefined")
	{
		var re = /(\<script|\<scr)/i, scriptTrack = re.test(S.etrkc);
		if (scriptTrack)
		{
			DT(S);
			S.trk = 0;
		}
	}
	
	DLT[name].push({B:B, B2:B2, S:S, AdTagIdx:TagIdx});

	if (send)
		DLI(name);
}

function DLF(name, B, S, B2)
{
	var send = false;

	if (!DLT[name])
	{
		DLT[name] = [];
		send = true;
	}
	
	if (S && typeof(S.etrkc) != "undefined")
	{
		var re = /(\<script|\<scr)/i, scriptTrack = re.test(S.etrkc);
		if (scriptTrack)
		{
			DT(S);
			S.trk = 0;
		}
	}

	DLT[name].push({B:B, B2:B2, S:S, AdTagIdx:TagIdx, CId:'fake_for_ti'});
	
	if (send)
		DLI(name);
}

function DLF2(name, B, S, B2)
{
	var idx = TagIdx,
		tag = Tags[idx],
		ZIdx = GetZIdx(tag, 'normal'),
		send = false,
		create = typeof B.L.hc != 'undefined' && B.L.hc.length > 0 &&
					typeof B.T.hc != 'undefined' && B.T.hc.length > 0 &&
					typeof B.R.hc != 'undefined' && B.R.hc.length > 0 &&
					Info.FlashVer >= B.fv;

	if (!DLT[name])
	{
		DLT[name] = [];
		send = true;
	}

	DLT[name].push({ B: B, B2: B2, S: S, AdTagIdx: TagIdx, CId: 'fake_for_ti' });

	if (create)
	{
		Html_Scripts.push({ B: B.R, S: S, Idx: idx, Sfx: 'R' });
		Html_Scripts.push({ B: B.T, S: S, Idx: idx, Sfx: 'T' });
		Html_Scripts.push({ B: B.L, S: S, Idx: idx, Sfx: 'L' });
	}

	if (send)
		DLI(name);
}

function DLI(name)
{
	var v, s = document.createElement('script');
	if (s)
	{
		s.type = 'text/javascript';
		s.src = TplDir + name + '.js';
		
		v = document.getElementsByTagName('head');
		if (v.length > 0)
		{
			v[0].appendChild(s);
		}
		else if (document.body && document.body.firstChild)
		{
			document.body.insertBefore(s, document.body.firstChild);
		}
	}
}



window.MX1_DL = DL;
window.MX1_DLF = DLF;
window.MX1_DLF2 = DLF2;

window.MX1_Image = Img;
window.MX1_Flash = Flash;
window.MX1_ExpandableFlash = ExpandableFlash;
window.MX1_FloatingFlash = FloatingFlash;
window.MX1_Html = Html;
window.MX1_Iframe = Iframe;
window.MX1_Pop = Pop;
window.MX1_Transitional = Transitional;
window.MX1_UnderlineMultipleLinks = function(B,S){B.load = 1;DL('Underline',B,S);}
window.MX1_Underline = function(B,S){DL('Underline',B,S);}
window.MX1_Text = function(B,S){DL('Text',B,S);}
window.MX1_TextBegin = function(){DL('Text',null,null);}
window.MX1_TextEnd = function(){DLF('Text',null,null);}
window.MX1_PeelAway = function(B,S){DL('PeelAway',B,S);}
window.MX1_FloatingExpandableFlash = function(B,S){DL('FloatingExpandableFlash',B,S);}
window.MX1_SiteSplitFlash = function(B,S){DLF('SiteSplitFlash',B,S);}
window.MX1_FormRequiredField = function(B,S){DLF('FormRequiredField',B,S);}
window.MX1_FloatingImage = function(B,S){DL('FloatingImage',B,S);}
window.MX1_FloatingHtml = function(B,S){DL('FloatingHtml',B,S);}
window.MX1_ExpandableFlashKick = function(B,S){DLF('ExpandableFlashKick',B,S);}
window.MX1_ExpandableIframe = function(B,S){DLF('ExpandableIframe',B,S);}
window.MX1_ScrollingFloatingFlash = function(B,S){DL('ScrollingFloatingFlash',B,S);}
window.MX1_ToolbarFloatingFlash = function(B,S){DL('ToolbarFloatingFlash',B,S);}
window.MX1_TextAndImage = function(B,S){DL('TextAndImage',B,S);}
window.MX1_TextAndImageBegin = function(){DL('TextAndImage',null,null);}
window.MX1_TextAndImageEnd = function(){DLF('TextAndImage',null,null);}
window.MX1_LeftTopRightBranding = function(B,S){DLF('LeftTopRightBranding',B,S);}
window.MX1_LeftTopRightBrandingHTML = function(B,S){DLF2('LeftTopRightBrandingHtml',B,S);}
window.MX1_LeftTopRightBrandingFull = function(B,S){DLF('LeftTopRightBrandingFull',B,S);}
window.MX1_LeftTopRightBrandingTakeover = function(B,S){DLF('LeftTopRightBrandingTakeover',B,S);}
window.MX1_LTRBrandingFloatingFlash = function(B,S){DLF('LTRBrandingFloatingFlash',B,S);}
window.MX1_FloatingFlash2 = function(B,S){DL('FloatingFlash2',B,S);}
window.MX1_TwoFloatingFlash = function(B,B2,S){DL('TwoFloatingFlash',B,S,B2);}
window.MX1_Sidekick = function(B,S){DLF('Sidekick',B,S);}



Init();




window.ZTRCOMMONREF = 
{
    //Dbg: Dbg,
    DLT: DLT,
    Tags: Tags,
    Info: Info,
    Dbg_New: Dbg_New,
    Dbg_Msg: Dbg_Msg,
	DbgData: DbgData,
    ExecuteCallbackCode: ExecuteCallbackCode,
    GetElem: GetElem,
    GetFileUrl: GetFileUrl,
    GetFlashVars: GetFlashVars,
    GetFlashHtml: GetFlashHtml,
    GetImgHtml: GetImgHtml,
    GetWinW: GetWinW,
    GetWinH: GetWinH,
    GetXY: GetXY,
    GetWH: GetWH,
    GetZIdx: GetZIdx,
    GetColorCode: GetColorCode,
    SendMsgToFlash: SendMsgToFlash,
    GetFlashRef: GetFlashRef,    
    SendAction: SendAction,
    SendActionForBackup: SendActionForBackup,
    DT: DT,
    DT2: DT2,
    ZIdxLevels: ZIdxLevels,
    GetClickUrl: GetClickUrl,
    GetStyleValue: GetStyleValue,
    CommonFilesDir: CommonFilesDir,
    Preview: Preview,
    JsEncLight: JsEncLight,
    HtmlEncodeCustom1: HtmlEncodeCustom1,
    HtmlEncodeCustom2: HtmlEncodeCustom2,
    HtmlEncode: HtmlEncode,
    ReplaceRules: ReplaceRules,
    Enc: Enc,
    TrkHtmlCode: TrkHtmlCode,
    Trim: Trim,
    GetClickPrefix: GetClickPrefix,
    AddWinEvt: AddWinEvt,
    SetStyles: SetStyles,
    Debug: Debug,
    Reports_Init: Reports_Init,
    RecordCmd: RecordCmd
};

})();