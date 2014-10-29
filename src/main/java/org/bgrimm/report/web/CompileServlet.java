/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bgrimm.report.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.PdfServlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reporttest")
public class CompileServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */

	@RequestMapping("compile")
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			// JasperCompileManager.compileReportToFile(context
			// .getRealPath("/reports/WebappReport.jrxml"));
			JasperReport report = JasperCompileManager
					.compileReport(CompileServlet.class
							.getResourceAsStream("/reports/WebappReport.jrxml"));
			System.out.println(report);
		} catch (JRException e) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");

			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
			out.println("<pre>");

			e.printStackTrace(out);

			out.println("</pre>");

			out.println("</body>");
			out.println("</html>");

			return;
		}

		out.println("<html>");
		out.println("<head>");
		out.println("<title>JasperReports - Web Application Sample</title>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
		out.println("</head>");

		out.println("<body bgcolor=\"white\">");

		out.println("<span class=\"bold\">The JRXML report design was successfully compiled.</span>");

		out.println("</body>");
		out.println("</html>");
	}

	@RequestMapping("fill")
	public void fill(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
		try {
			JasperReport report = JasperCompileManager
					.compileReport(CompileServlet.class
							.getResourceAsStream("/reports/WebappReport.jrxml"));
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", "Address Report");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report,
					parameters, new WebappDataSource());
			request.getSession().setAttribute(
					BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
					jasperPrint);
			
//			HtmlExporter exporter = new HtmlExporter();
//
//			request.getSession().setAttribute(
//					ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
//					jasperPrint);
//
//			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//			SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
//			output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
//			exporter.setExporterOutput(output);
//
//			exporter.exportReport();
			
			PdfServlet s=new PdfServlet();
			s.service(request, response);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping("html")
	public void html(HttpServletRequest request, HttpServletResponse response) {

	}
}
