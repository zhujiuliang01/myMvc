package example.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.formbean.ExampleForm;

import mvc.core.Action;
import mvc.core.ActionForm;

public class ExampleAction implements Action{

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, ActionForm form) {
		ExampleForm f = (ExampleForm)form;
		System.out.println("Example Action username:" + f.getUsername());
		System.out.println("Example Action password:" + f.getPassword());
		return "success";
	}

}
