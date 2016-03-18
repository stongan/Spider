import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

import jxl.Workbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider3 {
	private static final int numip = 27;
	private static final String myip[] = { "122.193.14.109", "218.106.96.211",
			"36.251.248.183", "218.106.96.211", "122.193.14.85",

			"218.106.96.204", "122.193.14.114", "122.193.14.108",
			"218.106.96.203", "218.106.96.200",

			"124.202.181.186", "218.106.96.199", "122.193.14.102",
			"218.106.96.198", "122.193.14.85",

			"218.106.96.199", "218.106.96.203", "203.195.162.96",
			"218.106.96.198",

			"218.106.96.196", "218.244.140.99", "120.198.231.22",
			"218.106.96.196", "218.106.96.200",

			"218.106.96.195", "218.106.96.202", "183.88.73.15" };
	private static final int myport[] = { 82, 80, 3128, 81, 80,

	80, 82, 81, 81, 83,

	8118, 81, 80, 80, 83,

	83, 83, 8080, 83,

	81, 8888, 8081, 80, 82,

	80, 82, 3182 };

	public static void main(String[] args) throws IOException,
			RowsExceededException, WriteException, InterruptedException {
		(new Spider3()).getInfo();
//		 (new Spider3()).getTest();
	}

	private List<String> urlSet = new ArrayList<String>();
	private final String fsPre = "th0316";
	private static int ipnow = 0;
	public static int count = 0;
	public static int conticnt = 0;

	public void getTest() {
		String fileName = "urlRentSetALL.txt";
		String line = "";
		Random random = new Random();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			line = in.readLine();
			while (line != null) {
				urlSet.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String fileName2 = "iplib0307";
		String line2 = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName2));
			line2 = in.readLine();
			while (line2 != null) {
				String[] arr = line2.split("\\|");
				System.out.println(arr[0] + "|" + arr[1]);
				iplibset.add(arr[0]);
				portlibset.add(Integer.parseInt(arr[1]));
				line2 = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter("ret0307an", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < urlSet.size(); i++) {
			Map<String, String> ret = new HashMap<String, String>();
			StringBuffer nowString = gethelper(urlSet.get(i),
					iplibset.get(ipnow), portlibset.get(ipnow));
			ipnow++;
			if (ipnow >= iplibset.size()) {
				ipnow %= iplibset.size(); 
			}
			if(nowString == null ||
					nowString.toString() == ""){
				System.out.println(conticnt++ + "continue");
				continue;
			}
			
			Document doc = Jsoup.parse(nowString.toString());
			ret = getContent2(doc);
//			String print106 = "";
//			for (int kk = 0; kk < ATTR.length; kk++) {
//				print106 += ret.get(ATTR[kk]) + "|";
//			}
//			System.out.println(print106);
			
			try {
				writer.write(count + "|");
				for (int kk = 0; kk < ATTR.length; kk++) {
					writer.write(ret.get(ATTR[kk]) + "|");
				}
				writer.write(urlSet.get(i));
				writer.write("\n");
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			System.out.println(count + "done!");
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, String> getContent2(Document doc) {
		Map<String, String> ret = new HashMap<String, String>();
		Elements list = doc.select("h2[class*=txt-medium txt-bold]");
		System.out.println("pja:" + list);

		for (int j = 0; j < list.size(); j++) {
			Element now = list.get(j);
			Element next = now.nextElementSibling();

			String tmp = "";
			System.out.println("now:" + now.hasText());
			if (now.hasText()) {
				tmp = now.unwrap().toString();
			}
			System.out.println("tmp:" + tmp);

			if (tmp.contains("Comment")) {
				continue;
			} else if (tmp.contains("Price")) {
				String tmpv = "";
				String pricetmp[] = { "price", "unitprice" };
				int priceidx = 0;
				// System.out.println(next);
				for (Element next_sub : next.children()) {
					if (next_sub.hasText()) {
						tmpv = next_sub.unwrap().toString();
						ret.put(pricetmp[priceidx++], tmpv);
					}
					if (priceidx >= 2)
						break;
				}
			} else if (tmp.contains("Specific")) {
				String tmpv = "";
				String charatmp[] = { "area", "bedroom", "wc", "condition" };
				int charaidx = 0;
				for (Element next_sub : next.children()) {
					if (next_sub.hasText()) {
						tmpv = next_sub.unwrap().toString();
						if (charaidx >= 4) {
							charaidx--;
							ret.put(charatmp[charaidx],
									ret.get(charatmp[charaidx]) + tmpv);
						} else {
							ret.put(charatmp[charaidx], tmpv);
							charaidx++;
						}
					}
				}
			} else if (tmp.contains("Construction")) {
				String tmpv = "";
				String charatmp[] = { "floor", "lift", "energycondition" };
				int charaidx = 0;
				for (Element next_sub : next.children()) {
					if (next_sub.hasText()) {
						tmpv = next_sub.unwrap().toString();
						ret.put(charatmp[charaidx++], tmpv);
					}
				}
			} else if (tmp.contains("Equipment")) {
				String tmpv = "";
				String idx = "equipment";
				for (Element next_sub : next.children()) {
					if (next_sub.hasText()) {
						tmpv += next_sub.unwrap().toString();
					}
				}
				ret.put(idx, tmpv);
			} else if (tmp.contains("Extra")) {
				continue;
			} else if (tmp.contains("listing")) {
				ret.put("listing", tmp);
			} else {// addr
				String tmpv = "";
				for (Element next_sub : next.children()) {
					if (next_sub.hasText()) {
						tmpv += next_sub.unwrap().toString();
					}
				}
				ret.put("addr", tmp + tmpv);
			}
		}

		list = doc.select("div[class*=info-tags]");
		// System.out.println("list1:" + list);
		if (list.size() > 0) {
			String tmpv = "";
			for (Element next_sub : list.get(0).children()) {
				for (Element next_sub2 : next_sub.children()) {
					if (next_sub2.hasText()) {
						tmpv += next_sub2.unwrap().toString();
					}
				}
			}
			ret.put("pricechange", tmpv);
		} else {
			ret.put("pricechange", "none");
		}

		list = doc.select("a[class*=privacy-link]");
		// System.out.println("list2:" + list);
		// System.out.println(":" + list.get(0));
		if (list.size() > 0) {
			String tmpv = "";
			if (list.get(0).hasText()) {
				tmpv += list.get(0).unwrap().toString();
			}
			ret.put("adtype", tmpv);
		} else {
			ret.put("adtype", "none");
		}

		return ret;
	}

	public void resetIPPort(List<String> iplibset, List<Integer> portlibset){
		iplibset.clear();
		portlibset.clear();
		String ip222[] = {
			"218.106.96.203", "124.200.100.50",	"61.174.13.12", "218.106.96.203	",
			"218.106.96.202", "218.106.96.202", "218.106.96.202", "218.106.96.202",
			"218.106.96.200", "218.106.96.200", "218.106.96.196", "121.193.143.249",
			"203.195.162.96", "218.106.96.203", "218.106.96.203", "218.106.96.196",
			"218.106.96.198", "180.97.29.57", "124.200.33.146", "183.234.48.201",
			"112.25.41.111", "120.198.231.22", "111.12.83.150", "218.106.96.200",
			"120.198.231.23", "120.198.231.21", "120.198.231.24", "120.198.231.21",
			"120.198.231.23", "120.198.231.22", "120.198.231.23", "120.198.231.22",
			"120.198.231.24", "120.198.231.21", "120.198.231.21", "120.198.231.21",
			"120.198.231.23", "120.198.231.24", "120.198.231.23", "120.198.231.24",
			"111.12.83.150", "120.198.231.24", "111.12.83.150", "120.198.231.22",
			"120.198.231.24", "120.198.231.24", "120.198.231.23", "120.198.231.22",
			"120.198.231.22", "211.143.146.230", "61.235.125.26", "211.143.146.230",
			"112.25.41.136", "111.12.83.150", "101.226.249.237", "183.61.236.53",
			"203.195.160.14", "203.195.172.147", "183.61.236.54"
		};
		Integer port222[] = {
				81, 8080, 80, 80,
				83, 82, 81, 80,
				83, 82, 82, 80,
				8080, 83, 82, 80,
				80, 80, 8118, 8080,
				80, 82, 80, 80,
				83, 8080, 8080, 8081,
				8080, 81, 85, 84,
				86, 80, 81, 84,
				80, 85, 8081, 80,
				101, 8081, 8080, 83,
				84, 83, 84, 85,
				8081, 80, 81, 81,
				80, 105, 80, 3128,
				8080, 8080, 3128
		};
		for(int i = 0; i < ip222.length; i++){
			iplibset.add(ip222[i]);
			portlibset.add(port222[i]);
		}
	}
	
	private static final String ATTR[] = { "price", "unitprice", "area",
			"bedroom", "wc", "condition", "floor", "lift", "energycondition",
			"equipment", "addr", "listing", "pricechange", "adtype" };

	private List<String> iplibset = new ArrayList<String>();
	private List<Integer> portlibset = new ArrayList<Integer>();

	public void getInfo() throws RowsExceededException, WriteException,
			IOException, InterruptedException {
		String fileName = "urlRentSetALL.txt";
		String line = "";
		Random random = new Random();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			line = in.readLine();
			while (line != null) {
				urlSet.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String fileName2 = "iplib0316";
		String line2 = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName2));
			line2 = in.readLine();
			while (line2 != null) {
				String[] arr = line2.split("\\|");
				System.out.println(arr[0] + "|" + arr[1]);
				iplibset.add(arr[0]);
				portlibset.add(Integer.parseInt(arr[1]));
				line2 = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		resetIPPort(iplibset, portlibset);

		for (Integer i = 0; i <= 20; i++) {
			// 0~700 701~1400 1401~2100 ...
			new Thread("" + i) {
				final Integer pja = Integer.parseInt(getName());

				public void run() {
					FileWriter writer = null;
					try {
						writer = new FileWriter(fsPre + getName(), true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (int j = pja * 700; (j < (pja + 1) * 700 - 1)
							&& (j < urlSet.size()); j++) {
						Map<String, String> ret = new HashMap<String, String>();
						//StringBuffer nowString = gethelper(urlSet.get(j), ipnow);
						StringBuffer nowString = gethelper(urlSet.get(j), iplibset.get(ipnow), portlibset.get(ipnow));
						if(nowString.toString() == ""){
//							iplibset.remove(ipnow);
//							portlibset.remove(ipnow);
							continue;
						}
						
						ipnow++;
						if (ipnow >= iplibset.size()) {
							ipnow %= iplibset.size();
						}
						Document doc = Jsoup.parse(nowString.toString());
						ret = getContent2(doc);
//						ret.put("url", urlSet.get(j));
						try {
							writer.write(count + "|");
							for (int kk = 0; kk < ATTR.length; kk++) {
								writer.write(ret.get(ATTR[kk]) + "|");
							}
							writer.write(urlSet.get(j));
							writer.write("\n");
							writer.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						count++;
						System.out.println(count + "done!");
					}
				}
			}.start();

		}
	}

	public StringBuffer gethelper(String urlgrap, int ipidx) {
		while (true) {
			try {
				URL url = new URL(urlgrap);
				// 创建代理服务器
				InetSocketAddress addr = new InetSocketAddress(
						iplibset.get(ipidx), portlibset.get(ipidx));
				// Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
				Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
				// Authenticator.setDefault(new MyAuthenticator("username",
				// "password"));// 设置代理的用户和密码
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection(proxy);// 设置代理访问
				connection
						.setRequestProperty("User-Agent",
								"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				connection.setConnectTimeout(10000);
				if (connection.getResponseCode() == 200) {
					return gethelper(urlgrap, iplibset.get(ipidx), portlibset.get(ipidx));
//					break;
				}
			} catch (IOException e) {
				ipidx++;
				if (ipidx >= iplibset.size()) {
					ipidx %= iplibset.size();
				}
				return gethelper(urlgrap, ipidx);
				//e.printStackTrace();
			}
		}
		//return null;
	}

	public StringBuffer gethelper(String urlgrap, String ip, int port) {
		String sall = "";

		// while (true) {
		try {
			URL url = new URL(urlgrap);
			// 创建代理服务器
			InetSocketAddress addr = new InetSocketAddress(ip, port);
			// Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
			// Authenticator.setDefault(new MyAuthenticator("username",
			// "password"));// 设置代理的用户和密码
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection(proxy);// 设置代理访问
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			connection.setConnectTimeout(10000);
			// if (connection.getResponseCode() == 200) {
			//
			// }
			InputStreamReader in = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String s = "";
			while ((s = reader.readLine()) != null) {
				sall += s;
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new StringBuffer(sall);
		}
		// }
		return new StringBuffer(sall);
	}

	public Map<String, String> getContent(Document doc) {
		Map<String, String> ret = new HashMap<String, String>();
		Elements list = doc.select("h2[class*=txt-medium txt-bold]");

		int pre = 0;
		for (int j = 0; j < list.size() - 1; j++) {
			Element now = list.get(j);
			Element next = now.nextElementSibling();

			// String tmp = "";
			// if(now != null)
			// tmp = now.unwrap().toString();

			String tmpv = "";
			for (Element next_sub : next.children())
				tmpv += next_sub.unwrap().toString();
			// url.add(tmp);
			if (j < list.size() - 2)
				ret.put(pre + "", tmpv);
			else {
				String tmp = "";
				if (now.hasText())
					tmp = now.unwrap().toString();
				ret.put("addr", tmp + tmpv);
			}
			System.out.println(tmpv);
			System.out.println("===========");
			pre++;
		}

		return ret;
	}

	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}

	private static final String FFF = "";
}

/*
 * 
 * condition: Fully furnished and equipped adtype: Privacy policy equipment: Air
 * conditioning Swimming pool addr:Plaza de españa, 18 Subdistrict Argüelles
 * District Moncloa Madrid Madrid capital, Madrid bedroom: 2 bedrooms
 * energycondition: Energy certification: in progress unitprice: 33,75 eur/m²
 * price: 2.970 euros/month - area: 88 m² constructed floor: Floor 20 exterior
 * pricechange:Has dropped by (10%) listing:listing updated on 21 of February
 * lift: With lift wc: 1 wc
 */