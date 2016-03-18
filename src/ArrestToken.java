import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArrestToken {

	public final String[] ATTRCons = { "姓 名", "方言", "性 别", "现在身份",
			"出生日期", "身份证号", "身 高", "其它证件", "脸 型", "通缉日期", 
			"体型", "通缉编号", "曾用姓名", "户籍住址", "现在住址", 
			"身体标记", "简要案情" };
	public final  String[] ATTR = {"name", "accent", "gender", "nowIdentity", 
			"birthday", "identityCard", "height", "otherCert", "faceFeature", "arrestDate", 
			"bodyFeature", "arrestNum", "usedName", "address", "nowAddress", 
			"bodyMark", "case"};

	public String getValue(String tmp) {
		int begin = tmp.indexOf(">");
		int end = tmp.indexOf("<", tmp.indexOf(">"));
		return tmp.substring(begin + 1, end);
	}

	public  Document getConnection(String url) throws IOException {
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
		return doc;
	}

	public  Map<String, String> getInfo(String personURL)
			throws IOException {
		Document doc = null;
		doc = getConnection(personURL);

		Elements list = doc.select("table[width=95%]");
		list = list.select("td");
		list.select("font").unwrap();
		list.select("p").unwrap();
		list.select("SPAN").unwrap();

		Map<String, String> data = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			String tmp = list.get(i).toString();
			for (int j = 0; j < ATTRCons.length; j++) {
				if (getValue(tmp).contains(ATTRCons[j]) && i+1 < list.size()) {
					String tmp2 = list.get(i + 1).toString();
					tmp2 = tmp2.replaceAll("&nbsp;", "");
					data.put(ATTR[j], getValue(tmp2));
					break;
				}
			}
		}
		return data;
	}

	public  List<String> getPage(String url) throws IOException {
		List<String> page = new ArrayList<String>();

		Document doc = null;
		doc = getConnection(url);

		Elements list = doc.select("option");
		for (int i = 0; i < list.size(); i++) {
			Element tmp = list.get(i);
			String tmpStr = tmp.attr("value");
			if(tmpStr.contains("_")){
				int begin = tmpStr.lastIndexOf("_");
				int end = tmpStr.lastIndexOf(".");
				String pageStr = tmpStr.substring(begin + 1, end);
				page.add(pageStr);
			}			
		}
		return page;
	}

	public  List<String> getPerUrl(String url) throws IOException {
		List<String> perUrl = new ArrayList<String>();

		Set<String> set = new HashSet<String>();
		Document doc = null;
		doc = getConnection(url);

		Elements list = doc.select("a");
		for (int i = 0; i < list.size(); i++) {
			Element tmp = list.get(i);
			String tmpStr = tmp.attr("href");
			int begin = tmpStr.lastIndexOf("/");
			int end = tmpStr.lastIndexOf(".");
			String pageStr = tmpStr.substring(begin + 1, end);
			if (pageStr.matches("\\d+")) {
				set.add(pageStr);
			}
		}
		perUrl.addAll(set);
		return perUrl;
	}

	public  List<String> getAllPerUrl(String url) throws IOException {
		List<String> allPerUrl = new ArrayList<String>();

		List<String> tmp = new ArrayList<String>();
		tmp = getPerUrl(url);
		allPerUrl.addAll(tmp);
		// System.out.println("in all" + allPerUrl);
		List<String> page = new ArrayList<String>();
		page = getPage(url);
		String urlTmp = null;
		int indextmp = url.lastIndexOf(".");
		for (int i = 0; i < page.size(); i++) {
			urlTmp = null;
			urlTmp = url.substring(0, indextmp);
			urlTmp += "_";
			urlTmp += page.get(i);
			urlTmp += url.substring(indextmp);
			allPerUrl.addAll(getPerUrl(urlTmp));
		}
		return allPerUrl;
	}

	public  List<Map<String, String>> getAllInfo(String url)
			throws IOException {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		List<String> allPerUrl = new ArrayList<String>();
		allPerUrl = getAllPerUrl(url);

		String tmpUrl = null;
		for (int i = 0; i < allPerUrl.size(); i++) {
			tmpUrl = url;
			tmpUrl = tmpUrl.replaceAll("index", allPerUrl.get(i));
			data.add(getInfo(tmpUrl));
		}
		return data;
	}
}
