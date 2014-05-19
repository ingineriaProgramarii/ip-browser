function readCookie(name) 
  {
      var nameEQ = name + "=";
      var ca = document.cookie.split(';');
      for(var i=0;i < ca.length;i++) 
      {
          var c = ca[i];
          while (c.charAt(0)==' ') c = c.substring(1,c.length);
          if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
      }
      return null;
  }

function createCookie(name, value, days) 
{
  document.cookie = name+"="+value+"; path=/";
}

function eraseCookie(name) 
{
    createCookie(name, "", -1);
}
    
function parse_url( param )
{
    param = param.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS  = "[\\?&]"+param+"=([^&#]*)";
    var regex   = new RegExp( regexS );
    var results = regex.exec( window.location.href );
    if( results == null ) {
      return '';
    } else {
      return results[1];
    }
}
  
function detectmobile() 
{ 
    if( navigator.userAgent.match(/Android/i)
    || navigator.userAgent.match(/webOS/i)
    || navigator.userAgent.match(/iPhone/i)
    || navigator.userAgent.match(/iPad/i)
    || navigator.userAgent.match(/iPod/i)
    || navigator.userAgent.match(/BlackBerry/i)
    || navigator.userAgent.match(/Windows Phone/i)
    ){
        return true;
    }else {
        return false;
    }
}
    
function mobile_redirect() 
{
    var is_mobile = detectmobile();
    var initial_path = window.location.pathname;
    
    if( is_mobile ) //if is mobile
    {
        var force_desktop = parse_url( 'force-desktop' ); 
        
        if ( readCookie('force-desktop') ===  null )
        {    
            if( parseInt(force_desktop) == 1 ) 
            {
                createCookie('force-desktop', '1');                    
            } 
            else {
                window.location = 'http://m.digisport.ro/' + initial_path;
            }               
        } 
    }
    return '';
}  
  
mobile_redirect();