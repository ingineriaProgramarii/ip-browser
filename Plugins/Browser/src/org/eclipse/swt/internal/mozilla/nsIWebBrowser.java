
//Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 13.05.2014 0:34:16
//Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
//Decompiler options: packimports(3) 

package org.eclipse.swt.internal.mozilla;


//Referenced classes of package org.eclipse.swt.internal.mozilla:
//         nsISupports, nsID, XPCOM

public class nsIWebBrowser extends nsISupports
{

 public nsIWebBrowser(long l)
 {
     super(l);
 }

 public int AddWebBrowserListener(long l, nsID nsid)
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), l, nsid);
 }

 public int RemoveWebBrowserListener(long l, nsID nsid)
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), l, nsid);
 }

 public int GetContainerWindow(long al[])
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), al);
 }

 public int SetContainerWindow(long l)
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), l);
 }

 public int GetParentURIContentListener(long al[])
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), al);
 }

 public int SetParentURIContentListener(long l)
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), l);
 }

 public int GetContentDOMWindow(long al[])
 {
     return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), al);
 }

 public int GetIsActive(int ai[])
 {
     if(!IsXULRunner10 && !IsXULRunner17)
         return 1;
     else
         return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), ai);
 }

 public int SetIsActive(int i)
 {
     if(!IsXULRunner10 && !IsXULRunner17)
         return 1;
     else
         return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), i);
 }

 static final int LAST_METHOD_ID;
 public static final String NS_IWEBBROWSER_IID_STR = "69e5df00-7b8b-11d3-af61-00a024ffc08c";
 public static final String NS_IWEBBROWSER_10_IID_STR = "33e9d001-caab-4ba9-8961-54902f197202";
 public static final nsID NS_IWEBBROWSER_IID = new nsID("69e5df00-7b8b-11d3-af61-00a024ffc08c");
 public static final nsID NS_IWEBBROWSER_10_IID = new nsID("33e9d001-caab-4ba9-8961-54902f197202");

 static 
 {
     LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (!IsXULRunner10 && !IsXULRunner17 ? 7 : 9);
 }
}
