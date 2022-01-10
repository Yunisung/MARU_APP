package com.pgmate.app.export;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgmate.app.util.CPUtil;
import com.pgmate.lib.dao.RecordSet;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
public class XlsExport {

	private static Logger logger = LoggerFactory.getLogger(com.pgmate.app.export.XlsExport.class);
	private CPDocument doc = null;
	private String filePath = "webexport";
	private String outputFileName = "";
	private String author = "system";
	private String url = "";
	private boolean rotate = false;
	ArrayList<String> numberArray = new ArrayList<String>() {{
		add("amount");
		add("vat");
		/*add("bin");*/
		/*add("last4");*/
		add("stlAmount");
		add("stlFee");
		add("stlFeeVat");
		add("stlDistFee");
		add("stlDistFeeVat");
		add("stlInterFee");
		add("stlInterFeeVat");
		add("stlAgencyFee");
		add("stlAgencyFeeVat");
		add("stlSalesFee");
		add("stlSalesFeeVat");
		add("stlDiffDistFee");
		add("stlDiffAgencyFee");
		add("stlDiffSalesFee");
		add("stlVanFee");
		add("stlVanInterFee");
		add("stlVanAmount");
		add("stlDiffAmt");
		add("stlDiffVanAmt");
		add("benefit");

		add("saleAmount");
		add("saleCount");
		add("saleStlAmount");
		add("saleStlFee");
		add("saleBenefit");
		add("rfdAmount");
		add("rfdCount");
		add("rfdStlAmount");
		add("rfdStlFee");
		add("rfdBenefit");
		add("rfdedAmount");
		add("rfdedCount");
		add("rfdedStlAmount");
		add("rfdedStlFee");
		add("rfdedBenefit");
		
		add("payAmt");
		add("payFee");
		add("payVat");
		add("payCnt");
		add("rfdAmt");
		add("rfdFee");
		add("rfdVat");
		add("rfdCnt");
		add("totalCnt");
		add("holdCnt");
		add("holdAmt");
		add("totalAmt");
		add("totalFee");
		add("totalInterFee");
		add("vanFee");
		add("vanInterFee");
		add("totalDistFee");
		add("minusAmt");
		add("deductAmt");
		add("stlFee");
		add("stlDiffFee");
		add("stlAmt");
		add("payOutAmt");
		
		add("calcAmount");
		add("collectAmount");
		add("deductAmount");
		
		add("diffAmount");
		add("diffVanAmount");
		add("diffSettleAmount");

		add("limitOnce");
		
		add("payOutFee");
		add("payOutFeeVat");
		add("payOutAmount");
		
		add("totCnt");
		add("totAmt");
		add("totPayOutFee");
		add("bankFee");
		add("diffAmt");
	}};
	
	ArrayList<String> doubleArray = new ArrayList<String>() {{
		
	}};
	
	ArrayList<String> percentArray = new ArrayList<String>() {{
		add("stlRate");
		add("stlInterRate");
		add("stlLoanRate");
		add("stlDistRate");
		add("stlAgencyRate");
		add("stlDiffDistRate");
		add("stlDiffAgencyRate");
		add("stlDiffSalesRate");
		add("stlSalesRate");
		add("stlVanRate");
		add("stlVanInterRate");
		add("rate");
		add("tmnRate");
		add("distRate");
		add("agencyRate");
		add("salesRate");
		add("diff0DistRate");
		add("diff1DistRate");
		add("diff2DistRate");
		add("diff3DistRate");
		add("diff0CheckDistRate");
		add("diff1CheckDistRate");
		add("diff2CheckDistRate");
		add("diff3CheckDistRate");
		add("diff0AgencyRate");
		add("diff1AgencyRate");
		add("diff2AgencyRate");
		add("diff3AgencyRate");
		add("diff0CheckAgencyRate");
		add("diff1CheckAgencyRate");
		add("diff2CheckAgencyRate");
		add("diff3CheckAgencyRate");
		add("diff0SalesRate");
		add("diff1SalesRate");
		add("diff2SalesRate");
		add("diff3SalesRate");
		add("diff0CheckSalesRate");
		add("diff1CheckSalesRate");
		add("diff2CheckSalesRate");
		add("diff3CheckSalesRate");
	}};
	
	public XlsExport(CPDocument doc) {
		url = CPUtil.CP_UPLOAD_DIR + "/" + filePath + "/" + CommonUtil.getCurrentDate("yyyyMMdd") + "/";
		filePath = CPUtil.getCanonicalWebPath() + File.separator + CPUtil.CP_UPLOAD_DIR + File.separator + filePath + File.separator;
		CPUtil.setTemplateDirectory(filePath);
		filePath = filePath + File.separator + CommonUtil.getCurrentDate("yyyyMMdd") + File.separator;

		if (doc != null) {
			this.doc = doc;
		} else {
			doc = new CPDocument("AutoExport", "Export DATA", "SYSTEM");
		}
	}

	public String makeExcel(LinkedHashMap<String, String> thead, RecordSet rset, boolean showHeader, boolean headerStyle) {
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
		CellStyle percentCellStyle = workbook.createCellStyle();
		CellStyle titleCellStyle = workbook.createCellStyle();
		CellStyle contentCellStyle = workbook.createCellStyle();
		Sheet sheet = workbook.createSheet("Sheet1");
		Row row;
		Cell cell;

		int rowCnt = 0;
		int columnCnt = 0;
		// 헤더 생성
		if (showHeader) {
/*			row = sheet.createRow(rowCnt++);
			row.setHeight((short)500);
			for (int i = 0; i < thead.size(); i++) {
				cell = row.createCell(columnCnt++);
				cell.setCellValue("");
}
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, thead.size()-1));*/

			row = sheet.createRow(rowCnt++);
			columnCnt = 0;

			if (headerStyle) {
				row.setHeight((short) 500);
				titleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				titleCellStyle.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
				titleCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
				titleCellStyle.setBorderRight(CellStyle.BORDER_NONE);
				titleCellStyle.setBorderTop(CellStyle.BORDER_NONE);
				titleCellStyle.setAlignment(CellStyle.VERTICAL_CENTER);
				Font headerFont = workbook.createFont();
				headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
				headerFont.setFontName("Arial");
				headerFont.setFontHeightInPoints((short)10);
				titleCellStyle.setFont(headerFont);
				titleCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
				headerFont.setColor(IndexedColors.WHITE.getIndex());
				titleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			}

			for (Entry<String, String> entry : thead.entrySet()) {
				cell = row.createCell(columnCnt++);
				if (headerStyle) {
					cell.setCellStyle(titleCellStyle);
				}
				cell.setCellValue(entry.getValue());
			}

			if (headerStyle) {
				contentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				contentCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
				contentCellStyle.setBorderRight(CellStyle.BORDER_NONE);
				contentCellStyle.setBorderTop(CellStyle.BORDER_NONE);
				contentCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
				contentCellStyle.setAlignment(CellStyle.VERTICAL_CENTER);
				percentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
				percentCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
				percentCellStyle.setBorderRight(CellStyle.BORDER_NONE);
				percentCellStyle.setBorderTop(CellStyle.BORDER_NONE);
				percentCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
				percentCellStyle.setAlignment(CellStyle.VERTICAL_CENTER);
			}
		}

		Font bodyFont = workbook.createFont();
		bodyFont.setFontName("Arial");
		bodyFont.setFontHeightInPoints((short)10);
		contentCellStyle.setFont(bodyFont);
		percentCellStyle.setFont(bodyFont);
		// 바디 생성
		for (SharedMap<String, Object> datas : rset.getRows()) {
			row = sheet.createRow(rowCnt++);
			row.setHeight((short) 400);
			columnCnt = 0;

			for (String key : thead.keySet()) {
				cell = row.createCell(columnCnt++);
				cell.setCellStyle(contentCellStyle);
				Object data = datas.get(key);
				if(numberArray.indexOf(key) > -1) {
					cell.setCellValue(CommonUtil.parseLong(data));
				} else if (doubleArray.indexOf(key) > -1) {
					cell.setCellValue(CommonUtil.parseDouble(data));
				} else if (percentArray.indexOf(key) > -1) {
					percentCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));
					cell.setCellStyle(percentCellStyle);
					cell.setCellValue(CommonUtil.parseDouble(data));
				} else if (data instanceof java.lang.Integer) {
					cell.setCellValue(CommonUtil.parseInt(data));
				} else if (data instanceof java.lang.Long) {
					cell.setCellValue(CommonUtil.parseLong(data));
				} else if (data instanceof java.lang.Double) {
					cell.setCellValue(CommonUtil.parseDouble(data));
				} else if (data instanceof java.sql.Timestamp) {
					cell.setCellValue(CommonUtil.timestampToString((Timestamp) data, "yyyy/MM/dd HH:mm:ss"));
				} else {
					cell.setCellValue(CommonUtil.toString(data));
				}
			}
		}

		for (int i = 0; i < columnCnt; i++) {
			sheet.autoSizeColumn((short) i);
			if(sheet.getColumnWidth(i) > 1000) {
				sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 512); // 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
			}else {
				sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1024); // 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
			}
		}

		TplExport export = new TplExport();
		String fileLink = "";
		try {
			fileLink = export.textToExl(doc.title, workbook);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileLink;
	}

	public String makeXlsFile(String text, HSSFWorkbook wb) throws Exception {
		outputFileName = text + ".xls";

		try {
			FileOutputStream fileOut = new FileOutputStream(filePath + outputFileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url + outputFileName;
	}

}
