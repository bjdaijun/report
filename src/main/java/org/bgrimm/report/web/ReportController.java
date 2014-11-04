package org.bgrimm.report.web;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.PdfServlet;

import org.bgrimm.report.domain.Saturation;
import org.bgrimm.report.domain.SaturationDTO;
import org.bgrimm.report.domain.TDMPoint;
import org.bgrimm.report.domain.TDMType;
import org.bgrimm.report.service.SaturationService;
import org.bgrimm.report.service.TDMPointService;
import org.bgrimm.report.service.TDMTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ReportController {

	@Autowired
	private TDMTypeService tdmTypeService;

	@Autowired
	private TDMPointService pointService;

	@Autowired
	private SaturationService saturationService;

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	@RequestMapping("html")
	public void html(HttpServletRequest req, HttpServletResponse rep,
			@RequestParam Date startTime, @RequestParam Date endTime,
			@RequestParam String reportTitle) throws Exception {
		rep.setContentType("text/html");
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("reportTitle", reportTitle);
		parameters.put("startTime", "2012-12-12 11:11:11");
		parameters.put("endTime", "2012-12-12 11:11:11");
		
		JRBeanCollectionDataSource ds = createMasterDS();
		// JasperReport report = JasperCompileManager
		// .compileReport(CompileServlet.class
		// .getResourceAsStream("/reports/Master.jasper"));

		addSaturationReport(parameters,startTime,endTime);
		
		
		
		InputStream is = ReportController.class
				.getResourceAsStream("/reports/Master.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(is, parameters,
				ds);
		req.getSession().setAttribute(
				BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
				jasperPrint);
		PdfServlet s = new PdfServlet();
		s.service(req, rep);
	}

	private void addSaturationReport(Map<String, Object> parameters, Date startTime, Date endTime)
			throws JRException {
		
		JRBeanCollectionDataSource saturationDs = getSaturationDatasource(
				startTime, endTime);
		InputStream is1 = ReportController.class
				.getResourceAsStream("/reports/saturation.jasper");
		JasperReport sub1=(JasperReport) JRLoader.loadObject(is1);
		parameters.put("dsSaturation", saturationDs);
		parameters.put("saturationReport", sub1);
	}

	private JRBeanCollectionDataSource createMasterDS() {
		List dsList=new ArrayList();
		Map m=new HashMap();
		m.put("ds", "ds");
		dsList.add(m);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dsList);
		return ds;
	}

	private JRBeanCollectionDataSource getSaturationDatasource(Date startTime,
			Date endTime) {
		// current data
		// get type by name
		// get points by type
		// get latest value by point
		// get point all value
		// calculate
		TDMType type = tdmTypeService.findByType("SATURATION");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			Saturation s = saturationService.findLatestByPosition((int) point
					.getId());
			SaturationDTO dto = new SaturationDTO();
			dto.setName("test");
			dto.setDateTime(s.getDateTime());
			dto.setValue(s.getLength());

			List<Saturation> allList = saturationService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);

			calculateSaturation(dto, allList);
			System.out.println(dto);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,false);
		return ds;
	}

	private void calculateSaturation(SaturationDTO dto, List<Saturation> allList) {
		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (Saturation s : allList) {
			BigDecimal v = s.getValue();
			if (total != null) {
				total = total.add(v);
			} else {
				total = v;
			}
			if (max == null) {
				max = v;
				maxDate = s.getDateTime();
			} else {
				if (v.compareTo(max) > 0) {
					max = v;
					maxDate = s.getDateTime();
				}
			}
			if (min == null) {
				min = v;
				minDate = s.getDateTime();
			} else {
				if (v.compareTo(max) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(allList.size()), 3,
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(maxDate);
		dto.setMinValue(min);
		dto.setMinDateTime(minDate);
	}

	@RequestMapping("pdf")
	public void pdf() {

	}

}
