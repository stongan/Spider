import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//https://pf.amac.org.cn/open/comNotice/list
public class PrivateEquityRetrive {


	public static void main(String[] args) {
		List<Map<String, String>> allmon = new ArrayList<Map<String, String>>();
		allmon = getAllMoney();
		try{
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File("test.xls")); 
			jxl.write.WritableSheet ws = wwb.createSheet("TestSheet1", 0);  
	        jxl.write.WritableCell label = new jxl.write.Label(0, 0, "test");
	        ws.addCell(label);
	        for(int i = 0; i < allmon.size(); i++){
	        	label = new jxl.write.Label(0, i, allmon.get(i).keySet().toString()); 
	        	ws.addCell(label);
	        	label = new jxl.write.Label(1, i, allmon.get(i).values().toString());
	        	ws.addCell(label);
	        }  
	        //写入Excel对象   
	        wwb.write();   
	        wwb.close();   
	    } catch (Exception e) {   
	    	e.printStackTrace();   
		}   
	}
	
	public static List<Map<String, String>> getAllMoney() {
		String pageUrl = "https://pf.amac.org.cn/open/comNotice/list?currentPage=";
		String userUrl = "https://pf.amac.org.cn/open/comNotice/view?userId=";
		List<Map<String, String>> allmon = new ArrayList<Map<String, String>>();
		
		Map<String, Integer> tmp = new HashMap<String, Integer>();
		
		Map<String, String> one = new HashMap<String, String>();
		
		for(Integer i = 1; i <= 48; i++){
			String currentPage = "";
			currentPage = pageUrl + i.toString();
			
			tmp.clear();
			tmp = getIdFromPage(currentPage);
			Set<Map.Entry<String, Integer>> set = tmp.entrySet();
			//System.out.println(set);
			for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
		        //System.out.println(entry.getKey() + "--->" + entry.getValue());
				String currentId = "";
				currentId = userUrl + entry.getKey();
				
				//one.clear();
				one = getInfo(currentId, entry.getValue());
				
				allmon.add(one);
				//System.out.println(allmon);
		    }
		}
		return allmon;
	}
	
	public static Map<String, Integer> getIdFromPage(String url) {
		Map<String, Integer> userid = new HashMap<String, Integer>();
		String viewCom = "viewCom";
		try {
			Connection con = Jsoup
					.connect(url)
					// .data("query", "Java") // 请求参数
					.userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置																							// User-Agent
					.timeout(30000); // 设置连接超时时间
					/*
					 * .ignoreHttpErrors(true) .followRedirects(true)
					 */
			Connection.Response resp = con.execute();
			Document doc = null;
			if (resp.statusCode() == 200) {
				doc = con.get();
			}
			Elements list = doc.select("a[href*=viewCom]");
			//list.select("a").unwrap();
			int flag = 0;
			for (int i = 0; i < list.size(); i++) {
				flag = 0;
				String value = "";
				//System.out.println(list.get(i));
				
				String tmpStr = list.get(i).toString();
				if(tmpStr.contains("*"))
					flag = 1;
				
				int begin = tmpStr.indexOf("(");
				int end = tmpStr.indexOf(")");
				value = tmpStr.substring(begin+1, end);
			
				userid.put(value, flag);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userid;
	}
	
	public static Map<String, String> getInfo(String url, Integer flag) {
		String value = "";
		Map<String, String> data = new HashMap<String, String>();
		String wangyuan = "万元";
		String reg = "\\d+";
		try {
			Connection con = Jsoup
					.connect(url)
					// .data("query", "Java") // 请求参数
					.userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)") // 设置																							// User-Agent
					.timeout(30000); // 设置连接超时时间
					/*
					 * .ignoreHttpErrors(true) .followRedirects(true)
					 */
			Connection.Response resp = con.execute();
			Document doc = null;
			if (resp.statusCode() == 200) {
				doc = con.get();
			}
			Elements namelist = doc.select("td[width=75%]");
			//System.out.println(namelist);
		
			//System.out.println(namelist);
//			if(namelist.hasAttr("span"))
//				flag = true;
			String name = namelist.toString();
			int nbegin = name.indexOf(">");
			int nend = name.lastIndexOf("<");
			String curname = ""; 
			curname = name.substring(nbegin+1, nend);
			if(flag == 1) {
				curname = "*" + curname;
			}
			//System.out.println(curname);
			
			Elements list = doc.select("tr:contains(" + wangyuan + ")");
			//System.out.println(list);
			Elements element = list.select("td:matchesOwn(" + reg + ")");
			
			String tmpStr = element.toString();
			int begin = tmpStr.indexOf(">");
			int end = tmpStr.lastIndexOf("<");
			value = tmpStr.substring(begin+1, end);
			//System.out.println(value);
			data.put(curname, value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
