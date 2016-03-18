import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class GetData {

	public static void captureHtml(String ip) throws Exception {
		String strURL = "http://ip.chinaz.com/?IP=" + ip;
		URL url = new URL(strURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		InputStreamReader input = new InputStreamReader(httpConn
				.getInputStream(), "utf-8");
		BufferedReader bufReader = new BufferedReader(input);
		String line = "";
		StringBuilder contentBuf = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			contentBuf.append(line);
		}
		String buf = contentBuf.toString();
		int beginIx = buf.indexOf("查询结果[");
		int endIx = buf.indexOf("上面三项依次显示的是");
		String result = buf.substring(beginIx, endIx);
		System.out.println("captureHtml()的结果：\n" + result);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			captureHtml("111.142.55.73");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
