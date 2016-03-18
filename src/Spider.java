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

public class Spider {
	public static void merger() throws IOException {

		String fileName = "rent0224.txt";
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String pre = "multiret";
		String fileset[] = new String[24];
		for (int i = 0; i < 21; i++) {
			fileset[i] = pre + i;
		}
		fileset[21] = "retall.txt";
		fileset[22] = "retall2.txt";
		fileset[23] = "retall3.txt";

		String line = "";
		for (int i = 0; i < 24; i++) {
			BufferedReader in = new BufferedReader(new FileReader(fileset[i]));
			line = in.readLine();
			while (line != null) {
				writer.write(line + "\n");
				writer.flush();
				line = in.readLine();
			}
			in.close();
		}

		writer.close();
	}

	public static void filetoxls() {
		String fileName = "rent0224.txt";
		String line = "";

		String fsset[] = new String[42];
		for (int i = 0; i <= 20; i++)
			fsset[i] = "fuckyou" + i;
		for (int i = 21; i < 42; i++)
			fsset[i] = "fuckyouTh" + (i - 21);

		BufferedReader in = null;
		try {
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(
					"rent0228.xls"));
			jxl.write.WritableSheet ws = wwb.createSheet("TestSheet1", 0);
			jxl.write.WritableCell label = new jxl.write.Label(0, 0, "test");
			ws.addCell(label);

			int count = 1;
			for (int kk = 0; kk < fsset.length; kk++) {
				in = new BufferedReader(new FileReader(fsset[kk]));
				line = in.readLine();
				while (line != null) {
					String[] arr = line.split("\\|");
					if (arr.length > 3 && arr[1] != null && arr[1] != "null" && !arr[1].equals("null")) {
						System.out.println("line:" + line);
						for (int i = 0; i < arr.length; i++) {
							label = new jxl.write.Label(i, count, arr[i]);
							ws.addCell(label);
						}
						count++;
					}
					//System.out.println(count + "done!");
					line = in.readLine();
				}
			}
			wwb.write();
			wwb.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final int numip = 26;
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

			"218.106.96.195", "218.106.96.202" };
	private static final int myport[] = { 82, 80, 3128, 81, 80,

	80, 82, 81, 81, 83,

	8118, 81, 80, 80, 83,

	83, 83, 8080, 83,

	81, 8888, 8081, 80, 82,

	80, 82 };

	public static void main(String[] args) throws IOException,
			RowsExceededException, WriteException, InterruptedException {
		// String urlgrap =
		// "http://www.idealista.com/en/inmueble/32046885/?xtmc=2_1_madrid&xtcr=0";
		// String ip = "203.195.160.14";
		// int port = 8080;
		// System.out.print(gethelper(urlgrap, ip, port));
		// getInfo();
		// merger();
		filetoxls();
	}

	public static void getInfo() throws RowsExceededException, WriteException,
			IOException, InterruptedException {
		String fileName = "urlRentSetALL3.txt";
		String line = "";
		Random random = new Random();
		int ipnow = 0;
		// List<Map<String, String>> retset = new ArrayList<Map<String,
		// String>>();
		// jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(
		// "rentinfo2.xlsx"));
		// jxl.write.WritableSheet ws = wwb.createSheet("TestSheet1", 0);
		// jxl.write.WritableCell label = new jxl.write.Label(0, 0, "test");
		// ws.addCell(label);
		int count = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String retfileName = "retall3.txt";
			FileWriter writer = null;
			writer = new FileWriter(retfileName);

			line = in.readLine();
			while (line != null) {
				System.out.println(line);
				Map<String, String> ret = new HashMap<String, String>();
				StringBuffer nowString = gethelper(line, myip[ipnow],
						myport[ipnow]);
				ipnow++;
				if (ipnow >= numip) {
					ipnow %= numip;
				}
				Document doc = Jsoup.parse(nowString.toString());
				ret = getContent(doc);

				int j = 1;
				// for (Map.Entry<String, String> entity : ret.entrySet()) {
				// System.out.println(entity.getKey() + ":" +
				// entity.getValue());
				// label = new jxl.write.Label(j, count + 1, entity.getValue());
				// ws.addCell(label);
				// j++;
				// }
				// wwb.write();

				writer.write(count + "|");
				for (Map.Entry<String, String> entity : ret.entrySet()) {
					// System.out.println(entity.getKey() + ":" +
					// entity.getValue());
					writer.write(entity.getValue() + "|");
				}
				writer.write("\n");
				writer.flush();
				count++;
				System.out.println(count + "done!");
				// retset.add(ret);
				// Thread.sleep(random.nextInt(3000) + 1500);
				line = in.readLine();
			}
			in.close();
			writer.close();
		} catch (IOException e) {
			// wwb.write();
			// wwb.close();
			e.printStackTrace();
		}
		// wwb.write();
		// wwb.close();
	}

	public static StringBuffer gethelper(String urlgrap, String ip, int port) {
		String sall = "";
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
			connection.setConnectTimeout(3000);
			InputStreamReader in = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String s = "";
			while ((s = reader.readLine()) != null) {
				sall += s;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new StringBuffer(sall);
		}
		return new StringBuffer(sall);
	}

	public static Map<String, String> getContent(Document doc) {
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

}

// // 直接从URL 中加载页面信息。timeout设置连接超时时间 post提交方式 或者get()
// // Document document = (Document)
// //
// Jsoup.connect("http://haoma.imobile.com.cn/index.php?mob=18710115102").timeout(3000).post();
// System.out.println(doc);