package mvc.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigParser {
	
	private static Map<String, String> formMap = new HashMap<String, String>();
	private static Map<String, ActionModel> actionMap = new HashMap<String, ActionModel>();
	
	public static Map<String, ActionModel> parse(File file){
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(file);	
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		parseForm(document);
		parseAction(document);
		return actionMap;
	}
	
	private static void parseAction(Document document){
		Element root = document.getRootElement();
		Element actions = root.element("actions");
		@SuppressWarnings("unchecked")
		List<Element> actionList = actions.elements();
		for(Element action : actionList){
			String name = action.attributeValue("name");
			String type = action.attributeValue("type");
			String path = action.attributeValue("path");
			ActionModel actionModel = new ActionModel();
			actionModel.setName(name);
			actionModel.setActionType(type);
			actionModel.setPath(path);
			String formType = formMap.get(name);
			if(formType != null){
				actionModel.setFormType(formType);
			}
			@SuppressWarnings("unchecked")
			List<Element> resultList = action.elements("result");
			Map<String, String> resultMap = new HashMap<String, String>();
			for(Element result : resultList){
				String resultName = result.attributeValue("name");
				String resultValue = result.attributeValue("value");
				resultMap.put(resultName, resultValue);
			}
			actionModel.setResults(resultMap);
			actionMap.put(path, actionModel);
		}
	}
	
	private static void parseForm(Document document){
		Element root = document.getRootElement();
		Element form = root.element("formbeans");
		@SuppressWarnings("unchecked")
		List<Element> forms = form.elements();
		for(Element f : forms){
			String name = f.attributeValue("name");
			String clazz = f.attributeValue("class");
			formMap.put(name, clazz);
		}
	}
	
}
