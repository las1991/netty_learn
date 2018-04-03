package test;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStreamWriter;  
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.InetAddress;  
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;  
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;  
import java.util.ArrayList;  
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
  
/** 
 * һ���򵥵�HTTP�ͻ��ˣ�����HTTP����ģ������� 
 * �ɴ�ӡ���������͹�����HTTP��Ϣ 
 */  
public class SimpleHttpClient {  
    private static String encoding = "UTF-8";  
  
    public static void main(String[] args) { 
    	//����3������������ ���Ϊtrue��ȡ�м��� 
    	
    	System.out.println("����:"+jBaidu("���ǻ�ɴ��Ӱ","www.t1imi.net.cn"));
    	
    }  
    
    /**
     * ����������������
     */
    public static String jBaidu(String keyword,String url){
    	Long startTime1 = System.currentTimeMillis();    //��ȡ��ʼʱ��
    	//������������
    	List<Integer> nList = new ArrayList<Integer>();
    	//��ʼ����ҳ��
    	int p = 0;
    	//��ʼ��������������
    	int n = 0;
    	//ʹ�ñ����д���IP
    	for (int i = 0; i < 10; i++) {
    		String body = sendBaidu("115.47.38.28",999,"seo725","seo725",getURLEncoderString(keyword),p);
    		n = cBidu(body,url,n);
    		
    	    //�ж��Ƿ�ȡ������
    	    if(n!=-1){
    	    	nList.add(n);
    	    	break;
    	    }
    		//���ӷ�ҳ��
    		p=p+10;
    		//�ж����ҳ������10ҳ��������0
    		if (p==100) {
    			nList.add(0);
			}
    		
		}
    	
    	//��ʼ����ҳ��
    	p = 0;
    	//��ʼ��������������
    	n = 0;
    	//ʹ�ý���ʡ���д���IP
    	for (int i = 0; i < 10; i++) {
    		String body = sendBaidu("222.186.11.106",999,"seo725","seo725",getURLEncoderString(keyword),p);
    		n = cBidu(body,url,n);
    		
    	    //�ж��Ƿ�ȡ������
    	    if(n!=-1){
    	    	nList.add(n);
    	    	break;
    	    }
    		//���ӷ�ҳ��
    		p=p+10;
    		//�ж����ҳ������10ҳ��������0
    		if (p==100) {
    			nList.add(0);
			}
    		
		}
    	//��ʼ����ҳ��
    	p = 0;
    	//��ʼ��������������
    	n = 0;
    	//ʹ���Ϻ��д���IP
    	for (int i = 0; i < 10; i++) {
    		String body = sendBaidu("103.21.142.173",999,"seo725","seo725",getURLEncoderString(keyword),p);
    		n = cBidu(body,url,n);
    	    //�ж��Ƿ�ȡ������
    	    if(n!=-1){
    	    	nList.add(n);
    	    	break;
    	    }
    		//���ӷ�ҳ��
    	    p=p+10;
    	    //�ж����ҳ������10ҳ��������0
    		if (p==100) {
    			nList.add(0);
			}
		}
    	//����3������������ ���Ϊtrue��ȡ�м��� 
    	if (hasSame(nList)) {
    		Collections.sort(nList); 
    		n=nList.get(1);
		}else {
			if (nList.get(0)==nList.get(1)) {
				n=nList.get(0);
			}else if (nList.get(0)==nList.get(2)) {
				n=nList.get(0);
			}else if (nList.get(1)==nList.get(2)){
				n=nList.get(1);
			}
		}
    	System.out.println(nList.toString());
		if (n==0) {
			return ">100";
		}
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.err.println(" ִ��ʱ�䣺" + (endTime - startTime1) + "ms");
    	return String.valueOf(n);
    }
    /**
     * �����ٶȷ�������
     */
    public static int cBidu(String body,String url,int n ){
    	//��������
		Document doc = Jsoup.parse(body);
		Elements div = doc.getElementsByClass("c-showurl");
    	 for (Element e : div){
 	        n++;
 	       
 	        if (e.text().indexOf(url) != -1)
 	        {
 	          return n;
 	        }
 	     }
    	 return -1;
    }
    
   /**
    * SocketЭ������Http
    * ����IP
    * ����˿�
    * �����˺�
    * ��������
    * �ؼ���
    * ҳ��
    */
    public static String sendBaidu(String proxyIp,Integer proxyPort,final String proxyName,final String proxyPass,String keyword,Integer page){
    	//��������
        String body= null;
    	 try {  
         	//����Sock5��������
         	Authenticator.setDefault(new Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(proxyName,proxyPass.toCharArray());
                 }
             });
             Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyIp,proxyPort));
             Socket s = new Socket(proxy);
             //������ʵĿ������
             s.connect(new InetSocketAddress("www.baidu.com", 80));
             OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());  
             StringBuffer sb = new StringBuffer();  
             sb.append("GET /s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=2&tn=baiduhome_pg&wd="+keyword+"&pn="+page.toString()+" HTTP/1.1\r\n");  
             sb.append("Host:www.baidu.com\r\n");  
             sb.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36\r\n");  
             sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n"); 
             sb.append("\r\n");  
             osw.write(sb.toString());  
             osw.flush();  
             //--������������ص���Ϣ��ͷ��Ϣ  
             InputStream is = s.getInputStream();  
             String line = null;
             
             int contentLength = 0;//���������ͻ�������Ϣ����  
             // ��ȡ���з��������͹������������ͷ����Ϣ  
             while((line = readLine(is, 0)).indexOf("</html>")==-1)  
             {  
                 body = body + line;
                  
             }  
 
             
             //�ر���  
             is.close();
             osw.close();
             s.close();
             
   
         } catch (SocketException e){
        	 return "��������ʱʧ��";
         } catch (UnknownHostException e) {  
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         }
    	 return body;
    }
    /* 
     * ���������Լ�ģ���ȡһ�У���Ϊ���ʹ��API�е�BufferedReaderʱ�����Ƕ�ȡ��һ���س����к� 
     * �ŷ��أ��������û�ж�ȡ����һֱ������ֱ�ӷ�������ʱ�Զ��ر�Ϊֹ�������ʱ��ʹ��BufferedReader 
     * ����ʱ����Ϊ�������һ��ʱ�����һ�к󲻻��лس����з������Ծͻ�ȴ������ʹ�÷��������ͻ����� 
     * ��Ϣͷ���Content-Length����ȡ��Ϣ�壬�����Ͳ������� 
     *  
     * contentLe ���� ���Ϊ0ʱ����ʾ��ͷ����ʱ���ǻ���һ��һ�еķ��أ������Ϊ0����ʾ����Ϣ�壬 
     * ʱ���Ǹ�����Ϣ��ĳ�����������Ϣ��󣬿ͻ����Զ��ر��������������ȵ���������ʱ���رա� 
     */  
    private static String readLine(InputStream is, int contentLe) throws IOException {  
        ArrayList lineByteList = new ArrayList();  
        byte readByte;  
        int total = 0;  
        if (contentLe != 0) {  
            do {  
                readByte = (byte) is.read();  
                lineByteList.add(Byte.valueOf(readByte));  
                total++;  
            } while (total < contentLe);//��Ϣ�����δ����  
        } else {  
            do {  
                readByte = (byte) is.read();  
                lineByteList.add(Byte.valueOf(readByte));  
            } while (readByte != 10);  
        }  
  
        byte[] tmpByteArr = new byte[lineByteList.size()];  
        for (int i = 0; i < lineByteList.size(); i++) {  
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();  
        }  
        lineByteList.clear();  
  
        return new String(tmpByteArr, encoding);  
    }
    /**
     * �ж�List�����Ƿ�����ͬԪ��
     * @param list
     * @return
     */
    private static boolean hasSame(List<? extends Object> list)  
    {  
        if(null == list)  
            return false;  
        return list.size() == new HashSet<Object>(list).size();  
    }  
    /**
     * Url����
     * @param str
     * @return
     */
    public static String getURLEncoderString(String str)
    {
      String result = "";
      if (str == null) {
        return "";
      }
      try
      {
        result = URLEncoder.encode(str, "utf-8");
      }
      catch (UnsupportedEncodingException e)
      {
        e.printStackTrace();
      }
      return result;
    }
}  