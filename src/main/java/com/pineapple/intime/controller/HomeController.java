package com.pineapple.intime.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import com.pineapple.intime.dao.DAOEmpleado;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView newContact(ModelAndView model) {
		model.setViewName("home");
		return model;
	}
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("index");
		return model;
	}
	
	@RequestMapping(value = "/cerrarSesion", method = RequestMethod.GET)
	protected void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        
        //Cerrar sesion
        sesion.invalidate();
        
        //Redirecciono a index.jsp
        response.sendRedirect("index.jsp");
    }

	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginProcess(@ModelAttribute("email") String email, @ModelAttribute("password") String password, HttpServletRequest request) throws IOException {
		String pagina = null;
		HttpSession session = request.getSession(true);
		Document doc = DAOEmpleado.autenticar(email,password);
		if(doc.get("email").equals("error")) {
			pagina = "error";
		}
		if(doc.get("email").equals(email)) {
			session.setAttribute("emailSession",doc.get("email"));
			if(doc.get("rol").equals("admin")){
				session.setAttribute("rolSession",doc.get("rol"));
				pagina = "admin";
			}
			if(doc.get("rol").equals("user")){
				session.setAttribute("rol",doc.get("rol"));
				pagina = "user";
			}
			if(doc.get("rol").equals("incid")){
				session.setAttribute("rol",doc.get("rol"));
				pagina = "incid";
			}
		}
		return pagina;
	
	}
}
