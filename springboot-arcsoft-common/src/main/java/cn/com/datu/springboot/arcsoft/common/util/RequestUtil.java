package cn.com.datu.springboot.arcsoft.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * request工具类
 * Created by ZhangShuzheng on 2016/12/12.
 */
public class RequestUtil {

	private static Logger _log = LoggerFactory.getLogger(RequestUtil.class);
	/**
	 * 移除request指定参数
	 * @param request
	 * @param paramName
	 * @return
	 */
	public String removeParam(HttpServletRequest request, String paramName) {
		String queryString = "";
		Enumeration keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.equals(paramName)) {
				continue;
			}
			if (queryString.equals("")) {
				queryString = key + "=" + request.getParameter(key);
			} else {
				queryString += "&" + key + "=" + request.getParameter(key);
			}
		}
		return queryString;
	}

	/**
	 * 获取请求basePath
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		StringBuffer basePath = new StringBuffer();
		String scheme = request.getScheme();
		String domain = request.getServerName();
		int port = request.getServerPort();
		basePath.append(scheme);
		basePath.append("://");
		basePath.append(domain);
		if("http".equalsIgnoreCase(scheme) && 80 != port) {
			basePath.append(":").append(String.valueOf(port));
		} else if("https".equalsIgnoreCase(scheme) && port != 443) {
			basePath.append(":").append(String.valueOf(port));
		}
		return basePath.toString();
	}

	/**
	 * 判断uri是否包含某个字符
	 * @param prefix
	 * @return
	 */
	public static Boolean isContainUriPrefix(String prefix){
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		if(null!= request){
			String uri = request.getRequestURI();
			if(StringUtils.isNotBlank(uri)&&uri.contains(prefix)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取ip工具类，除了getRemoteAddr，其他ip均可伪造
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");    // 网宿cdn的真实ip
		if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");   // 蓝讯cdn的真实ip
		}
		if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");  // 获取代理ip
		}
		if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP"); // 获取代理ip
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP"); // 获取代理ip
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr(); // 获取真实ip
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteHost();
			if(ip != null && (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1"))){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if(inet != null) {
					ip= inet.getHostAddress();
				}
			}
		}
		if (ip != null && ip.contains(",")) {
			return ip.split(",")[0];
		} else {
			return ip;
		}
	}

	/**
	 * 校验请求参数合法性
	 * @param str
	 * @return
	 */
	public static boolean reqValidate(String str) {
		str = str.toLowerCase();//统一转为小写
		//过滤掉的sql关键字，可以手动添加
		String badStr = "and,exec,execute,insert,create,drop,table,from,grant,group_concat,column_name," +
				"information_schema.columns,table_schema,union,where,select,delete,update,order,count,*," +
				"chr,mid,master,truncate,char,declare,||,--,like,%,#";
		String[] badStrs = badStr.split(",");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) !=-1) {
				_log.info("The matched character is：{}",badStrs[i]);
				return true;
			}
		}
		return false;
	}

}
