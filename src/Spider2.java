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

public class Spider2 {
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
		(new Spider2()).getInfo();
	}

	private List<String> urlSet = new ArrayList<String>();
	private final String fsPre = "multiret";
	private static int ipnow = 0;
	public static int count = 0;

	public void getInfo() throws RowsExceededException, WriteException,
			IOException, InterruptedException {
		String fileName = "urlRentSetALL4.txt";
		String line = "";
		Random random = new Random();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String retfileName = "retall3.txt";
			line = in.readLine();
			while (line != null) {
				urlSet.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Integer i = 0; i <= 20; i++) {
			// 0~700 701~1400 1401~2100 ...
			new Thread("" + i) {
				final Integer pja = Integer.parseInt(getName());

				public void run() {
					FileWriter writer = null;
					try {
						writer = new FileWriter(fsPre + getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (int j = pja * 700; (j < (pja + 1) * 700 - 1)
							&& (j < urlSet.size()); j++) {
						Map<String, String> ret = new HashMap<String, String>();
						StringBuffer nowString = gethelper(urlSet.get(j),
								myip[ipnow], myport[ipnow]);
						ipnow++;
						if (ipnow >= numip) {
							ipnow %= numip;
						}
						Document doc = Jsoup.parse(nowString.toString());
						ret = getContent(doc);

						try {
							writer.write(count + "|");
							for (Map.Entry<String, String> entity : ret
									.entrySet()) {
								writer.write(entity.getValue() + "|");
							}
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

	public StringBuffer gethelper(String urlgrap, String ip, int port) {
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

}
