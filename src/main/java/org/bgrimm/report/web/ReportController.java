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
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.PdfServlet;

import org.bgrimm.report.DateUtil;
import org.bgrimm.report.domain.BeachCrestHeight;
import org.bgrimm.report.domain.BeachFreeHeight;
import org.bgrimm.report.domain.BeachLength;
import org.bgrimm.report.domain.DeformSurface;
import org.bgrimm.report.domain.DeformSurfaceDTO;
import org.bgrimm.report.domain.DeformSurfaceValue;
import org.bgrimm.report.domain.Rainfall;
import org.bgrimm.report.domain.RainfallDTO;
import org.bgrimm.report.domain.Saturation;
import org.bgrimm.report.domain.SaturationDTO;
import org.bgrimm.report.domain.TDMPoint;
import org.bgrimm.report.domain.TDMType;
import org.bgrimm.report.domain.WaterLevel;
import org.bgrimm.report.domain.WaterLevelDTO;
import org.bgrimm.report.service.BeachFreeHeightService;
import org.bgrimm.report.service.BeachcrestheightService;
import org.bgrimm.report.service.BeachlengthService;
import org.bgrimm.report.service.DeformsurfaceService;
import org.bgrimm.report.service.RainfallService;
import org.bgrimm.report.service.SaturationService;
import org.bgrimm.report.service.SeepageService;
import org.bgrimm.report.service.TDMPointService;
import org.bgrimm.report.service.TDMTypeService;
import org.bgrimm.report.service.WaterLevelService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
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

	@Autowired
	private DeformsurfaceService deformsurfaceService;

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
		parameters.put("startTime", DateUtil.date2String(startTime));
		parameters.put("endTime", DateUtil.date2String(endTime));

		JRBeanCollectionDataSource ds = createMasterDS();
		//表面位移
		addDeformsurfaceReport(parameters, startTime, endTime);
		//浸润线
		addSaturationReport(parameters, startTime, endTime);
		//库水位
		addWaterlevelReport(parameters, startTime, endTime);
		//降雨量
		addRainfallReport(parameters, startTime, endTime);
		//干滩长度
		addBeachlengthReport(parameters, startTime, endTime);
		//滩顶高程
		addBeachcrestheightReport(parameters, startTime, endTime);
		//安全超高
		addBeachfreeheightReport(parameters, startTime, endTime);

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
			dto.setValue(b.getValue());
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
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min);
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
			dto.setValue(b.getValue());
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
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min);
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
			dto.setValue(b.getValue());
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
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min);
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
			dto.setValue(w.getValue());
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

			dto.setName(point.getName());
			dto.setDE(deformSurface.getdE());
			dto.setDN(deformSurface.getdN());
			dto.setDH(deformSurface.getdH());
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
			dto.setValue(rain.getValue());
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

		dto.setTotalValue(total);

		BigDecimal max = null;
		String maxDateTime = null;
		for (String t : map.keySet()) {
			if (max == null) {
				max = map.get(t);
				maxDateTime = t+":00:00";
			} else {
				if (max.compareTo(map.get(t)) < 0) {
					max = map.get(t);
					maxDateTime = t+":00:00";
				}
			}

		}
		dto.setMaxValue(max);
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
			dto.setValue(s.getValue());

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
		dto.setAvgDE(total.getDe().divide(new BigDecimal(list.size()), 1,
				BigDecimal.ROUND_HALF_DOWN));
		dto.setAvgDN(total.getDn().divide(new BigDecimal(list.size()), 1,
				BigDecimal.ROUND_HALF_DOWN));
		dto.setAvgDH(total.getDh().divide(new BigDecimal(list.size()), 1,
				BigDecimal.ROUND_HALF_DOWN));

		dto.setMaxDE(max.getDe());
		dto.setMaxDN(max.getDn());
		dto.setMaxDH(max.getDh());
		dto.setMaxDateTime(DateUtil.date2String(max.getDateTime()));

		dto.setMinDE(min.getDe());
		dto.setMinDN(min.getDn());
		dto.setMinDH(min.getDh());
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
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min);
		dto.setMinDateTime(DateUtil.date2String(minDate));

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
				if (v.compareTo(min) < 0) {
					min = v;
					minDate = s.getDateTime();
				}
			}

		}
		dto.setAvgValue(total.divide(new BigDecimal(allList.size()), 3,
				BigDecimal.ROUND_HALF_DOWN));
		dto.setMaxValue(max);
		dto.setMaxDateTime(DateUtil.date2String(maxDate));
		dto.setMinValue(min);
		dto.setMinDateTime(DateUtil.date2String(minDate));
	}

	@RequestMapping("pdf")
	public void pdf() {

	}

}
