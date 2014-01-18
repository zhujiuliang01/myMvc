package mvc.core;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MvcConfigParseListener implements ServletContextListener {
	
	public static final String actionMapInServletContext = "actionMapInMvc";
	
	private static final String defaultConfigFile = "/WEB-INF/mvc-config.xml";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		String xmlFile = context.getInitParameter("mvcConfig");
		if(xmlFile == null || xmlFile.equals("")){
			xmlFile = defaultConfigFile;
		}
		String realPath = context.getRealPath("\\");
		File file = new File(realPath + xmlFile);
		Map<String, ActionModel> map = ConfigParser.parse(file);
		context.setAttribute(actionMapInServletContext, map);
	}

}
