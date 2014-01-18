package mvc.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1255164908678343718L;
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		process(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		process(request, response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String servletPath = request.getServletPath();
		String actionPath = servletPath.substring(1, servletPath.lastIndexOf("."));
		@SuppressWarnings("unchecked")
		Map<String, ActionModel> map = (Map<String, ActionModel>)request.getServletContext().getAttribute("actionMapInMvc");
		ActionModel actionModel = map.get(actionPath);
		
		String formType = actionModel.getFormType();
		ActionForm form = this.fillFormWithPara(request, formType);
		
		String actionType = actionModel.getActionType();
		Action action = this.getActionObject(actionType);
		String resultName = action.execute(request, response, form);
		
		String url = actionModel.getResults().get(resultName);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}
	
	private Action getActionObject(String actionType){
		Action action = null;
		try {
			Class actionClazz = Class.forName(actionType);
			action = (Action)actionClazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return action;
	}
	
	private ActionForm fillFormWithPara(HttpServletRequest request, String formType){
		ActionForm actionForm = null;
		try {
			Class formClazz = Class.forName(formType);
			actionForm = (ActionForm)formClazz.newInstance();
			Field[] fields = formClazz.getDeclaredFields();
			for(Field field : fields){
				String fieldName = field.getName();
				String firstUpperName = new StringBuffer().append(Character.toUpperCase(fieldName.charAt(0))).append(fieldName.substring(1)).toString();
				@SuppressWarnings("unchecked")
				Method setterMethod = formClazz.getDeclaredMethod("set" + firstUpperName, String.class);
				setterMethod.invoke(actionForm, request.getParameter(fieldName));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return actionForm;
	}

}
