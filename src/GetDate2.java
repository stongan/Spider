import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class GetDate2 {
	
	public final static String [] ATTRCons = {"姓 名", "方言", "性 别", "现在身份", "出生日期", "身份证号", "身 高", 
			  "其它证件", "脸 型", "通缉日期", "体型", "通缉编号", "曾用姓名", "户籍住址",
			  "现在住址", "身体标记", "简要案情"};
	public final static String [] ATTR = {};

		public static String getValue(String tmp){
			int begin = tmp.indexOf(">");
        	int end = tmp.indexOf("<", tmp.indexOf(">"));
        	return tmp.substring(begin+1, end);
		}
		
		public static Document getConnection(String url) throws IOException{
			//目标页面
	        //String url = "http://www.mps.gov.cn/n16/n1237/n1417/n456851/2054144.html";
	        //使用Jsoup连接目标页面,并执行请求,获取服务器响应内容
	        //String html = Jsoup.connect(url).execute().body();
	        
	        Connection con= Jsoup.connect(url) 
	        		//.data("query", "Java")   // 请求参数
	        		.userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置 User-Agent 
	        		.timeout(30000); // 设置连接超时时间
	        		/*.ignoreHttpErrors(true)
	        		.followRedirects(true)*/
	        
	        //Jsoup.connect(url).
	        //打印页面内容
	        Connection.Response resp = con.execute();
	        Document doc = null; 
	        if (resp.statusCode() == 200){ 
	        	doc = con.get(); 
	        }
	        return doc;
		}
		//String personURL = "http://www.mps.gov.cn/n16/n1237/n1417/n456851/2054144.html";
		public static Map<String, String> getInfo(String personURL) throws IOException{
	        Document doc = null; 
	        doc = getConnection(personURL);
	        
	        Elements listeles = doc.select("td");
	        listeles.select("table").remove();
	        listeles.select("script").remove();
	        listeles.select("a").remove();
	        listeles.select("noscript").remove();
	        listeles.select("href").remove();
	        listeles.removeAttr("align");
	        listeles.removeAttr("valign");
	        listeles.removeAttr("weight");
	        listeles.removeAttr("height");
	        listeles.removeAttr("width");
	        listeles.removeAttr("bgcolor");
	        listeles.removeAttr("style");
	        listeles.removeAttr("colspan");
	        listeles.removeAttr("strong");
	        listeles.removeAttr("background");
	        listeles.removeAttr("?");
	        listeles.select("font").unwrap();
	        listeles.select("p").unwrap();
	        
	        Map<String, String> data = new HashMap<String, String>();
	        for(int i = 0; i < listeles.size(); i++){
	        	//System.out.println(listeles.get(i).toString());
	        	String tmp = listeles.get(i).toString();
	        	for(int j = 0; j < ATTRCons.length; j++){
	        		if(getValue(tmp).contains(ATTRCons[j])){
	        			//data[ATTRCons[j]] = getValue(listeles.get(i+1).toString());
	        			String tmp2 = listeles.get(i+1).toString();
	        			data.put(ATTRCons[j], getValue(tmp2));
	        			break;
	        		}
	        	}
	         }
	        System.out.println(data);	        
	        return data;
		}
		
		public static Map<String, String> getInfoV2(String personURL) throws IOException{
			 Document doc = null; 
		     doc = getConnection(personURL);
		     
		     Elements list = doc.select("table[width=95%]");
		     list = list.select("td");
		     list.select("font").unwrap();
		     System.out.println(list);
		     
		     Map<String, String> data = new HashMap<String, String>();
		     for(int i = 0; i < list.size(); i++){
		        	String tmp = list.get(i).toString();
		        	for(int j = 0; j < ATTRCons.length; j++){
		        		if(getValue(tmp).contains(ATTRCons[j])){
		        			String tmp2 = list.get(i+1).toString();
		        			data.put(ATTRCons[j], getValue(tmp2));
		        			break;
		        		}
		        	}
		         }
		     return data;
		}
		
		public static List<String> getPage(String url) throws IOException{
			 List<String> page = new ArrayList<String>();
			 
			 Document doc = null; 
		     doc = getConnection(url);
		        
		     Elements list = doc.select("a[target=_self]");
		     for(int i = 0; i < list.size(); i++){
		    	 Element tmp = list.get(i);
		    	 String tmpStr = tmp.attr("href");
		    	 int begin = tmpStr.lastIndexOf("_");
		    	 int end = tmpStr.lastIndexOf(".");
		    	 String pageStr = tmpStr.substring(begin+1, end);
		    	 page.add(pageStr);
		     }
			 return page;
		}
		
		public static List<String> getPerUrl(String url) throws IOException{
			List<String> perUrl = new ArrayList<String>();
			
			Set<String> set = new HashSet<String>();
			Document doc = null; 
	        doc = getConnection(url);
			
	        Elements list = doc.select("a[target=_blank]");
	        for(int i = 0; i < list.size(); i++){
		    	 Element tmp = list.get(i);
		    	 String tmpStr = tmp.attr("href");
		    	 int begin = tmpStr.lastIndexOf("/");
		    	 int end = tmpStr.lastIndexOf(".");
		    	 String pageStr = tmpStr.substring(begin+1, end);
		    	 if(pageStr.matches("\\d+")){
		    		 set.add(pageStr);
		    	 }
		     }
	        perUrl.addAll(set);
			return perUrl;
		}
		
		public static List<String> getAllPerUrl(String url) throws IOException{
			List<String> allPerUrl = new ArrayList<String>();
			
			List<String> tmp = new ArrayList<String>();
			tmp = getPerUrl(url);
			allPerUrl.addAll(tmp);
			//System.out.println("in all" + allPerUrl);			
			List<String> page = new ArrayList<String>();
			page = getPage(url);		
			String urlTmp = null;
			int indextmp = url.lastIndexOf(".");
			for(int i = 0; i < page.size(); i++){
				urlTmp = null;
				urlTmp = url.substring(0, indextmp);
				urlTmp += "_";
				urlTmp += page.get(i);
				urlTmp += url.substring(indextmp);
				allPerUrl.addAll( getPerUrl(urlTmp) );
			}
			return allPerUrl;
		}
		
		public static List<Map<String, String>> getAllInfo(String url) throws IOException{
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			 
			 List<String> allPerUrl = new ArrayList<String>();
			 allPerUrl = getAllPerUrl(url);
			 
			 String tmpUrl = null;
			 for(int i = 0; i < allPerUrl.size(); i++){
				 tmpUrl = url;
				 //System.out.println(allPerUrl.get(i));
				 tmpUrl = tmpUrl.replaceAll("index", allPerUrl.get(i));
				 //System.out.println(tmpUrl);
				 data.add(getInfo(tmpUrl));
			 }
			 return data;
		}  
		
		static ArrestToken at = new ArrestToken();
		
	    public static void main(String[] args) throws IOException {
	    	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	    	//data = getAllInfo("http://www.mps.gov.cn/n16/n1237/n1417/n456851/index.html");//A级通缉令
	    	data.addAll( at.getAllInfo("http://www.mps.gov.cn/n16/n1237/n1417/n456866/index.html") );
	    	//System.out.println(data);
	    	try{
	    		jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File("blacklistB.xls")); 
	            jxl.write.WritableSheet ws = wwb.createSheet("公安部B级通缉名单", 0);  
	            jxl.write.WritableCell label = new jxl.write.Label(0, 0, "test");
	            ws.addCell(label);
	            for(int i = 0; i < at.ATTRCons.length; i++) {
	            	label = new jxl.write.Label(i, 0, at.ATTRCons[i]);
	            	ws.addCell(label);
	            }
	            for(int i = 0; i < data.size(); i++){
	            	for(int j = 0; j < data.get(i).size(); j++) {
	            		label = new jxl.write.Label(j, i+1, data.get(i).get(at.ATTR[j]));
		            	ws.addCell(label);
	            	}
	            }   
	            wwb.write();   
	            wwb.close();   
	    	    } catch (Exception e) {   
	    	        e.printStackTrace();   
	    	    }   
	    	
	    	//page is ok
	    	//List<String> tmp = at.getPage("http://www.mps.gov.cn/n16/n1237/n1417/n456866/index.html");
	    	//System.out.println(tmp);
	    	
	    	//perInfo is ok
//	    	Map<String, String> data = new HashMap<String, String>();
//	    	data = at.getInfo("http://www.mps.gov.cn/n16/n1237/n1417/n456866/2772086.html");
//	    	System.out.println(data);
	    	
	    	//perUrl is ok
	    	//List<String> tmp = at.getPerUrl("http://www.mps.gov.cn/n16/n1237/n1417/n456866/index.html");
	    	//System.out.println(tmp);
	    }
}
	  
//wuzhi de changshi	        
//old	        
//	        //System.out.println(doc.body());
//	        //doc.body()
//	        Node node = doc.body().child(4).childNode(1).childNode(0).childNode(5).childNode(5).childNode(1);
//	        //nodes.select("p:contains(姓 名)");
//	       String name = null;
//	        //System.out.println(  node.childNode(0).childNode(1).unwrap());
//	        //System.out.println(  node.childNode(0).childNode(1).previousSibling());
//	        //if(node.childNode(0).childNode(1).baseUri())
//	        String NAME = "姓 名：";
//	        char[] name2 = {'a', 'b', 'c'};
//	        //System.out.println(NAME);
//	        System.out.println(node);
//	        //node.select()
//	        System.out.println(node.childNode(0).childNode(3).unwrap());
//	        
//	        for(int i = 0; i < node.childNodeSize(); i++){
//	        	for(int j = 0; j < node.childNode(i).childNodeSize()-1; j++){
//	        		//node.childNode(i).childNode(j).unwrap();
//	        	}
//	        }
//	        
//	        for(int i = 0; i < node.childNodeSize(); i++){
//	        	for(int j = 0; j < node.childNode(i).childNodeSize()-1; j++){
//	        		if(node.childNode(i).childNode(j).unwrap() == null){
//	        			//System.out.println(i + " " + j + " null");
//	        			continue;
//	        		} else {
//	        			//if(node.childNode(i).childNode(j).unwrap().toString().indexOf(NAME) > -1){
//	        				//System.out.println(i + " " + j + " " + node.childNode(i).childNode(j).unwrap() );
//	        			//}
//	        		}
//	        	}
//	        }
