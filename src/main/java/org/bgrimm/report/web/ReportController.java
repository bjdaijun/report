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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.DocxServlet;
import net.sf.jasperreports.j2ee.servlets.PdfServlet;
import net.sf.jasperreports.j2ee.servlets.PptxServlet;
import net.sf.jasperreports.j2ee.servlets.XlsxServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import org.bgrimm.report.DateUtil;
import org.bgrimm.report.domain.AlarmRecord;
import org.bgrimm.report.domain.AlarmRecordDTO;
import org.bgrimm.report.domain.BeachCrestHeight;
import org.bgrimm.report.domain.BeachFreeHeight;
import org.bgrimm.report.domain.BeachLength;
import org.bgrimm.report.domain.DeformInternal;
import org.bgrimm.report.domain.DeformSurface;
import org.bgrimm.report.domain.DeformSurfaceDTO;
import org.bgrimm.report.domain.DeformSurfaceValue;
import org.bgrimm.report.domain.Rainfall;
import org.bgrimm.report.domain.RainfallDTO;
import org.bgrimm.report.domain.Saturation;
import org.bgrimm.report.domain.SaturationDTO;
import org.bgrimm.report.domain.Seepage;
import org.bgrimm.report.domain.SeepageDTO;
import org.bgrimm.report.domain.TDMPoint;
import org.bgrimm.report.domain.TDMType;
import org.bgrimm.report.domain.WaterLevel;
import org.bgrimm.report.domain.WaterLevelDTO;
import org.bgrimm.report.service.AlarmrecordService;
import org.bgrimm.report.service.BeachFreeHeightService;
import org.bgrimm.report.service.BeachcrestheightService;
import org.bgrimm.report.service.BeachlengthService;
import org.bgrimm.report.service.DeforminternalService;
import org.bgrimm.report.service.DeformsurfaceService;
import org.bgrimm.report.service.RainfallService;
import org.bgrimm.report.service.SaturationService;
import org.bgrimm.report.service.SeepageService;
import org.bgrimm.report.service.TDMPointService;
import org.bgrimm.report.service.TDMTypeService;
import org.bgrimm.report.service.WaterLevelService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ReportController {

	@Autowired
	private TDMTypeService tdmTypeService;

	@Autowired
	private TDMPointService pointService;

	@Autowired
	private SaturationService saturationService;

	@Autowired
	private DeformsurfaceService deformsurfaceService;

	@Autowired
	private DeforminternalService deforminternalService;

	@Autowired
	private WaterLevelService waterlevelService;

	@Autowired
	private RainfallService rainfallService;
	@Autowired
	private BeachlengthService beachlengthService;

	@Autowired
	private BeachcrestheightService beachcrestheightService;

	@Autowired
	private BeachFreeHeightService beachfreeheightService;

	@Autowired
	private SeepageService seepageServcie;

	@Autowired
	private AlarmrecordService alarmService;

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	@RequestMapping("{reportTitle}/{startTime}/{endTime}/{type}")
	public void report(HttpServletRequest req, HttpServletResponse rep,
			@PathVariable Date startTime, @PathVariable Date endTime,
			@PathVariable String reportTitle, @PathVariable String type)
			throws Exception {
		rep.setContentType("text/html");
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("reportTitle", reportTitle);
		parameters.put("startTime", DateUtil.date2String(startTime));
		parameters.put("endTime", DateUtil.date2String(endTime));

		JRBeanCollectionDataSource ds = createMasterDS();
		// 表面位移
		addDeformsurfaceReport(parameters, startTime, endTime);
		// 浸润线
		addSaturationReport(parameters, startTime, endTime);
		// 库水位
		addWaterlevelReport(parameters, startTime, endTime);
		// 降雨量
		addRainfallReport(parameters, startTime, endTime);
		// 干滩长度
		addBeachlengthReport(parameters, startTime, endTime);
		// 滩顶高程
		addBeachcrestheightReport(parameters, startTime, endTime);
		// 安全超高
		addBeachfreeheightReport(parameters, startTime, endTime);
		// 渗流量
		addSeepageReport(parameters, startTime, endTime);
		// 报警
		addAlarmrecordReport(parameters, startTime, endTime);
		// 内部位移
		addDeforminternalReport(parameters, startTime, endTime);

		InputStream is = ReportController.class
				.getResourceAsStream("/reports/Master.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(is, parameters,
				ds);
		req.getSession().setAttribute(
				BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
				jasperPrint);
		if ("pdf".equals(type)) {
			PdfServlet s = new PdfServlet();
			s.service(req, rep);
		} else if ("html".equals(type)) {
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(
					rep.getOutputStream());
			output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
			exporter.setExporterOutput(output);

			exporter.exportReport();
		} else if ("xlsx".equals(type)) {
			XlsxServlet servlet = new XlsxServlet();
			servlet.service(req, rep);
		} else if ("docx".equals(type)) {
			DocxServlet s = new DocxServlet();
			s.service(req, rep);
		} else if ("pptx".equals(type)) {
			PptxServlet s = new PptxServlet();
			s.service(req, rep);
		}

	}

	private void addDeforminternalReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/deforminternal.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getDeforminternalDatasource(startTime,
				endTime);
		parameters.put("dsDeforminternal", ds);
		parameters.put("deforminternalReport", sub2);

	}

	private JRBeanCollectionDataSource getDeforminternalDatasource(
			Date startTime, Date endTime) {
		TDMType type = tdmTypeService.findByType("INTERNAL_DISP");

		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			DeformInternal s = deforminternalService
					.findLatestByPosition((int) point.getId());
			SaturationDTO dto = new SaturationDTO();
			dto.setName(point.getName());
			dto.setDateTime(DateUtil.date2String(s.getDateTime()));
			dto.setValue(s.getValue().toString());

			List<DeformInternal> allList = deforminternalService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);

			calculateDeforminternal(dto, allList);
			result.add(dto);
		}
//		if (result.size() == 0) {
//			SaturationDTO dto = new SaturationDTO();
//			dto.setName("无记录");
//			result.add(dto);
//		}
//		System.out.println(result);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateDeforminternal(SaturationDTO dto,
			List<DeformInternal> allList) {
		if (allList.size() == 0) {
			return;
		}
		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (DeformInternal s : allList) {
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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(allList.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));

	}

	private void addAlarmrecordReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {
		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/alarmrecord.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getAlarmrecordDatasource(startTime,
				endTime);
		parameters.put("dsAlarmrecord", ds);
		parameters.put("alarmrecordReport", sub2);

	}

	private void addSeepageReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/seepage.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getSeepageDatasource(startTime, endTime);
		parameters.put("dsSeepage", ds);
		parameters.put("seepageReport", sub2);

	}

	private void addBeachfreeheightReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/beachfreeheight.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getBeachFreeHeightDatasource(startTime,
				endTime);
		parameters.put("dsBeachfreeheight", ds);
		parameters.put("beachfreeheightReport", sub2);

	}

	private JRBeanCollectionDataSource getBeachFreeHeightDatasource(
			Date startTime, Date endTime) {
		TDMType type = tdmTypeService.findByType("BEACH_FREE_HEIGHT");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			BeachFreeHeight b = beachfreeheightService
					.findLatestByPosition((int) point.getId());
			// //equals beachlengthdto
			SaturationDTO dto = new SaturationDTO();
			dto.setName(point.getName());
			dto.setDateTime(DateUtil.date2String(b.getDateTime()));
			dto.setValue(b.getValue().toString());
			List<BeachFreeHeight> list = beachfreeheightService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);
			calculateBeachFreeHeight(dto, list);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;

	}

	private void calculateBeachFreeHeight(SaturationDTO dto,
			List<BeachFreeHeight> list) {
		if (list.size() == 0) {
			return;
		}
		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (BeachFreeHeight s : list) {
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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(list.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));

	}

	private void addBeachcrestheightReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/beachcrestheight.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getBeachCrestHeightDatasource(
				startTime, endTime);
		parameters.put("dsBeachcrestheight", ds);
		parameters.put("beachcrestheightReport", sub2);

	}

	private JRBeanCollectionDataSource getSeepageDatasource(Date startTime,
			Date endTime) {

		TDMType type = tdmTypeService.findByType("SEEPAGE_FLOW");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			Seepage s = seepageServcie
					.findLatestByPosition((int) point.getId());
			SeepageDTO dto = new SeepageDTO();
			dto.setName(point.getName());
			dto.setValue(s.getValue().toString());

			List list = seepageServcie.findByPositionAndDateTimeBewteen(
					(int) point.getId(), startTime, endTime);

			calculateSeepage(dto, list, startTime, endTime);
			result.add(dto);
		}

//		if (result.size() == 0) {
//			SeepageDTO dto = new SeepageDTO();
//			dto.setName("无记录");
//			result.add(dto);
//		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateSeepage(SeepageDTO dto, List<Seepage> list,
			Date startTime, Date endTime) {
		if (list.size() == 0) {
			return;
		}
		BigDecimal max = null;
		BigDecimal min = null;
		Date maxDateTime = null;
		Date minDateTime = null;
		BigDecimal total = null;
		for (Seepage s : list) {
			if (max == null) {
				max = s.getValue();
				maxDateTime = s.getDateTime();
			} else {
				if (max.compareTo(s.getValue()) < 0) {
					max = s.getValue();
					maxDateTime = s.getDateTime();
				}
			}
			if (min == null) {
				min = s.getValue();
				minDateTime = s.getDateTime();
			} else {
				if (min.compareTo(s.getValue()) > 0) {
					min = s.getValue();
					minDateTime = s.getDateTime();
				}
			}
			if (total == null) {
				total = s.getValue();
			} else {
				total = total.add(s.getValue());
			}
		}

		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDateTime));
		dto.setMinDateTime(DateUtil.date2String(minDateTime));
		dto.setMinValue(min.toString());

		dto.setAvgValue(total.divide(new BigDecimal(list.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).toString());

		DateTime d1 = new DateTime(startTime);
		DateTime d2 = new DateTime(endTime);
		long seconds = (d2.getMillis() - d1.getMillis()) / 1000L;
		dto.setTotalValue((total.divide(new BigDecimal(list.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(seconds)))
				.toString());
	}

	private JRBeanCollectionDataSource getAlarmrecordDatasource(Date startTime,
			Date endTime) {

		List<AlarmRecord> list = alarmService.findAlarmRecordByTimeBetween(
				startTime, endTime);
		int i = 0;
		List<AlarmRecordDTO> result = new ArrayList();
		for (AlarmRecord a : list) {
			AlarmRecordDTO dto = new AlarmRecordDTO();
			dto.setInd(++i);
			dto.setType(a.getType().getName());
			dto.setPoint(a.getPoint().getName());
			dto.setTime(DateUtil.date2String(a.getEventTime()));
			// <style backcolor="yellow" isBold="true" isItalic="true">Styled
			// text</style>
			// dto.setName("<style backcolor=\"yellow\" isBold=\"true\" isItalic=\"true\">"
			// + point.getName()+"</style>");
			dto.setLevel("<style backcolor=\"" + a.getLevel().getCode() + "\">"
					+ a.getLevel().getName() + "</style>");
			dto.setValue(a.getAlarmValue().toString());
			dto.setThreshold("");
			dto.setHandle("");
			result.add(dto);
			System.out.println(dto);
		}
//		if (result.size() == 0) {
//			AlarmRecordDTO dto = new AlarmRecordDTO();
//			dto.setInd(1);
//			dto.setType("无记录");
//			result.add(dto);
//		}

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private JRBeanCollectionDataSource getBeachCrestHeightDatasource(
			Date startTime, Date endTime) {
		TDMType type = tdmTypeService.findByType("BEACH_CREST_HEIGHT");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			BeachCrestHeight b = beachcrestheightService
					.findLatestByPosition((int) point.getId());
			// //equals beachlengthdto
			SaturationDTO dto = new SaturationDTO();
			dto.setName(point.getName());
			dto.setDateTime(DateUtil.date2String(b.getDateTime()));
			dto.setValue(b.getValue().toString());
			List<BeachCrestHeight> list = beachcrestheightService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);
			calculateBeachCrestHeight(dto, list);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateBeachCrestHeight(SaturationDTO dto,
			List<BeachCrestHeight> list) {
		if (list.size() == 0) {
			return;
		}
		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (BeachCrestHeight s : list) {
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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(list.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));

	}

	private void addBeachlengthReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/beachlength.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getBeachlengthDatasource(startTime,
				endTime);
		parameters.put("dsBeachlength", ds);
		parameters.put("beachlengthReport", sub2);

	}

	private JRBeanCollectionDataSource getBeachlengthDatasource(Date startTime,
			Date endTime) {
		TDMType type = tdmTypeService.findByType("BEACH_LENGTH");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			BeachLength b = beachlengthService.findLatestByPosition((int) point
					.getId());
			// //equals beachlengthdto
			SaturationDTO dto = new SaturationDTO();
			dto.setName(point.getName());
			dto.setDateTime(DateUtil.date2String(b.getDateTime()));
			dto.setValue(b.getValue().toString());
			List<BeachLength> list = beachlengthService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);

			calculateBeachLength(dto, list);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateBeachLength(SaturationDTO dto, List<BeachLength> list) {
		if (list.size() == 0) {
			return;
		}

		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (BeachLength s : list) {
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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(list.size()), 2,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));
	}

	private void addRainfallReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {
		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/rainfall.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getRainfallDatasource(startTime,
				endTime);
		parameters.put("dsRainfall", ds);
		parameters.put("rainfallReport", sub2);

	}

	private void addWaterlevelReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {
		InputStream is2 = ReportController.class
				.getResourceAsStream("/reports/waterlevel.jasper");

		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is2);

		JRBeanCollectionDataSource ds = getWaterlevelDatasource(startTime,
				endTime);
		parameters.put("dsWaterlevel", ds);
		parameters.put("waterlevelReport", sub2);
	}

	private void addDeformsurfaceReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		InputStream is = ReportController.class
				.getResourceAsStream("/reports/deformsurface.jasper");
		JasperReport sub2 = (JasperReport) JRLoader.loadObject(is);
		JRBeanCollectionDataSource ds = getDeformsurfaceDatasource(startTime,
				endTime);

		parameters.put("dsDeformsurface", ds);
		parameters.put("deformsurfaceReport", sub2);

	}

	private JRBeanCollectionDataSource getWaterlevelDatasource(Date startTime,
			Date endTime) {
		TDMType type = tdmTypeService.findByType("WATER_LEVEL");
		List<TDMPoint> pointList = pointService.findByType(type);
		List<WaterLevelDTO> dtoList = new ArrayList();
		for (TDMPoint point : pointList) {
			WaterLevel w = waterlevelService.findLatestByPosition((int) point
					.getId());
			WaterLevelDTO dto = new WaterLevelDTO();
			dto.setName(point.getName());
			dto.setValue(w.getValue().toString());
			dto.setDateTime(DateUtil.date2String(w.getDateTime()));
			List<WaterLevel> list = waterlevelService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);
			calculateWaterLevel(dto, list);
			dtoList.add(dto);
			System.out.println(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dtoList,
				false);
		return ds;
	}

	private JRBeanCollectionDataSource getDeformsurfaceDatasource(
			Date startTime, Date endTime) {
		TDMType type = tdmTypeService.findByType("SURFACE_DISP");
		List<TDMPoint> pointList = pointService.findByType(type);

		List<DeformSurfaceDTO> dtoList = new ArrayList();
		for (TDMPoint point : pointList) {
			DeformSurface deformSurface = deformsurfaceService
					.findLatestByPosition((int) point.getId());
			DeformSurfaceDTO dto = new DeformSurfaceDTO();
			// <style backcolor="yellow" isBold="true" isItalic="true">Styled
			// text</style>
			// dto.setName("<style backcolor=\"yellow\" isBold=\"true\" isItalic=\"true\">"
			// + point.getName()+"</style>");
			dto.setName(point.getName());
			dto.setDE(deformSurface.getdE().toString());
			dto.setDN(deformSurface.getdN().toString());
			dto.setDH(deformSurface.getdH().toString());
			dto.setDateTime(DateUtil.date2String(deformSurface.getDateTime()));

			List<DeformSurface> list = deformsurfaceService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);
			calculateDeformsurface(dto, list);
			System.out.println(dto);
			dtoList.add(dto);
		}

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dtoList,
				false);
		return ds;
	}

	private JRBeanCollectionDataSource getRainfallDatasource(Date startTime,
			Date endTime) {
		TDMType type = tdmTypeService.findByType("RAINFALL");
		List<TDMPoint> pointList = pointService.findByType(type);
		List result = new ArrayList();
		for (TDMPoint point : pointList) {
			Rainfall rain = rainfallService.findLatestByPosition((int) point
					.getId());
			RainfallDTO dto = new RainfallDTO();

			dto.setName(point.getName());
			dto.setValue(rain.getValue().toString());
			dto.setDateTime(DateUtil.date2String(rain.getDateTime()));
			dto.setTotalTime(new DateTime(startTime)
					.toString("yyyy-MM-dd HH:mm:ss")
					+ "-"
					+ new DateTime(endTime).toString("yyyy-MM-dd HH:mm:ss"));
			List<Rainfall> list = rainfallService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);

			calculateRainfall(dto, list);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateRainfall(RainfallDTO dto, List<Rainfall> list) {
		BigDecimal total = null;
		if (list.size() == 0) {
			return;
		}
		Map<String, BigDecimal> map = new HashMap();
		for (Rainfall rain : list) {
			DateTime t = new DateTime(rain.getDateTime());
			String timeStr = t.toString("yyyy-MM-dd HH");
			BigDecimal v = map.get(timeStr);
			if (v == null) {
				map.put(timeStr, rain.getValue());
			} else {
				v.add(rain.getValue());
			}

			if (total == null) {
				total = rain.getValue();
			} else {
				total = total.add(rain.getValue());
			}

		}

		dto.setTotalValue(total.toString());

		BigDecimal max = null;
		String maxDateTime = null;
		for (String t : map.keySet()) {
			if (max == null) {
				max = map.get(t);
				maxDateTime = t + ":00:00";
			} else {
				if (max.compareTo(map.get(t)) < 0) {
					max = map.get(t);
					maxDateTime = t + ":00:00";
				}
			}

		}
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(maxDateTime);
		System.out.println(dto);
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
			dto.setName(point.getName());
			dto.setDateTime(DateUtil.date2String(s.getDateTime()));
			dto.setValue(s.getValue().toString());

			List<Saturation> allList = saturationService
					.findByPositionAndDateTimeBewteen((int) point.getId(),
							startTime, endTime);

			calculateSaturation(dto, allList);
			result.add(dto);
		}
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(result,
				false);
		return ds;
	}

	private void calculateDeformsurface(DeformSurfaceDTO dto,
			List<DeformSurface> list) {
		if (list.size() == 0) {
			dto.setAvgDE("");
			dto.setAvgDN("");
			dto.setAvgDH("");

			dto.setMaxDE("");
			dto.setMaxDN("");
			dto.setMaxDH("");
			dto.setMaxDateTime("");

			dto.setMinDE("");
			dto.setMinDN("");
			dto.setMinDH("");
			dto.setMinDateTime("");
			return;
		}
		DeformSurfaceValue cur = null;
		DeformSurfaceValue max = null;
		DeformSurfaceValue min = null;
		DeformSurfaceValue total = null;
		for (DeformSurface obj : list) {

			if (total == null) {
				total = new DeformSurfaceValue();
				total.setDe(obj.getdE());
				total.setDh(obj.getdH());
				total.setDn(obj.getdN());
			} else {
				total.setDe(total.getDe().add(obj.getdE()));
				total.setDn(total.getDn().add(obj.getdN()));
				total.setDh(total.getDh().add(obj.getdH()));
			}
			// compare DN
			if (max == null) {
				max = new DeformSurfaceValue();
				max.setDe(obj.getdE());
				max.setDh(obj.getdH());
				max.setDn(obj.getdN());
				max.setDateTime(obj.getDateTime());
			} else {
				BigDecimal a = obj.getdN();
				BigDecimal b = max.getDn();
				if (a.abs().compareTo(b.abs()) > 0) {
					max.setDn(a);
					max.setDe(obj.getdE());
					max.setDh(obj.getdH());
					max.setDateTime(obj.getDateTime());
				}
			}
			if (min == null) {
				min = new DeformSurfaceValue();
				min.setDe(obj.getdE());
				min.setDh(obj.getdH());
				min.setDn(obj.getdN());
				min.setDateTime(obj.getDateTime());
			} else {
				BigDecimal a = obj.getdN();
				BigDecimal b = max.getDn();
				if (a.abs().compareTo(b.abs()) < 0) {
					min.setDn(a);
					min.setDe(obj.getdE());
					min.setDh(obj.getdH());
					min.setDateTime(obj.getDateTime());
				}
			}

		}
		// ///
		dto.setAvgDE(total
				.getDe()
				.divide(new BigDecimal(list.size()), 1,
						BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setAvgDN(total
				.getDn()
				.divide(new BigDecimal(list.size()), 1,
						BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setAvgDH(total
				.getDh()
				.divide(new BigDecimal(list.size()), 1,
						BigDecimal.ROUND_HALF_DOWN).toString());

		dto.setMaxDE(max.getDe().toString());
		dto.setMaxDN(max.getDn().toString());
		dto.setMaxDH(max.getDh().toString());
		dto.setMaxDateTime(DateUtil.date2String(max.getDateTime()));

		dto.setMinDE(min.getDe().toString());
		dto.setMinDN(min.getDn().toString());
		dto.setMinDH(min.getDh().toString());
		dto.setMinDateTime(DateUtil.date2String(min.getDateTime()));
	}

	private void addSaturationReport(Map<String, Object> parameters,
			Date startTime, Date endTime) throws JRException {

		JRBeanCollectionDataSource saturationDs = getSaturationDatasource(
				startTime, endTime);
		InputStream is1 = ReportController.class
				.getResourceAsStream("/reports/saturation.jasper");

		JasperReport sub1 = (JasperReport) JRLoader.loadObject(is1);
		parameters.put("dsSaturation", saturationDs);
		parameters.put("saturationReport", sub1);
		// /
	}

	private JRBeanCollectionDataSource createMasterDS() {
		List dsList = new ArrayList();
		Map m = new HashMap();
		m.put("ds", "ds");
		dsList.add(m);
		dsList.add(m);
		dsList.add(m);
		dsList.add(m);
		dsList.add(m);
		dsList.add(m);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dsList);
		return ds;
	}

	private void calculateWaterLevel(WaterLevelDTO dto, List<WaterLevel> list) {
		if (list.size() == 0) {
			return;
		}
		BigDecimal total = null;
		BigDecimal max = null;
		Date maxDate = null;
		BigDecimal min = null;
		Date minDate = null;

		for (WaterLevel s : list) {
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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(list.size()), 3,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));

	}

	private void calculateSaturation(SaturationDTO dto, List<Saturation> allList) {

		if (allList.size() == 0) {
			return;
		}

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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(allList.size()), 3,
				BigDecimal.ROUND_HALF_DOWN).toString());
		dto.setMaxValue(max.toString());
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min.toString());
		dto.setMinDateTime(DateUtil.date2String(minDate));
	}


}
