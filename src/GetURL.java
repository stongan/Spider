import java.io.*; 
import java.net.*;  
public class GetURL {  
	public static void main(String[] args) { 
		InputStream in = null;  
		OutputStream out = null; 
		try {  
			// 检查命令行参数  
			if ((args.length != 1)&& (args.length != 2))   
				throw new IllegalArgumentException("Wrong number of args");    
			URL url = new URL(args[0]); //创建 URL  
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			in = url.openStream(); // 打开到这个URL的流 
			if (args.length == 2) // 创建一个适当的输出流 
				out = new FileOutputStream(args[1]); 
			else out = System.out;   // 复制字节到输出流 
			byte[] buffer = new byte[4096]; 
			int bytes_read;  
			while((bytes_read = in.read(buffer)) != -1) 
				out.write(buffer, 0, bytes_read); 
			}   catch (Exception e) { 
				System.err.println(e); 
				System.err.println("Usage: java GetURL <URL> [<filename>]"); 
			}  finally { 
				//无论如何都要关闭流  
				try { 
					in.close(); out.close();
				} catch (Exception e) {}
			}  
	}
}