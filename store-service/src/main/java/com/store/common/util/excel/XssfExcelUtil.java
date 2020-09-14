package com.store.common.util.excel;

import com.store.common.exception.BusinessException;
import com.store.common.util.DateUtil;
import com.store.common.util.ReflectUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能：用于2007以上版本（.xlsx）
 */
public class XssfExcelUtil extends ExcelUtil {

    public XssfExcelUtil(){}

	public XssfExcelUtil(InputStream inputStream) {
		this.inputStream = inputStream;
	}

    @Override
    public <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle, String group) throws Exception {
        List<T> dataModels = new ArrayList<>();
        String[] fieldNames = getField(clazz, group);
        //String[] fieldNames = getClassFieldByExcelImport(clazz, group);
        // 获取excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(sheetNo);
        int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0); // 如果有标题则从第二行开始
        for (int i = start, length = sheet.getLastRowNum(); i <= length; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            // 生成实例并通过反射调用setter方法
            T target = clazz.newInstance();
            for (int j = 0; j < fieldNames.length; j++) {
                String fieldName = fieldNames[j];
                if (fieldName == null || SERIALVERSIONUID.equals(fieldName)) {
                    continue; // 过滤serialVersionUID属性
                }
                // 获取excel单元格的内容
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                // 如果属性是日期类型则将内容转换成日期对象
                if (isDateType(clazz, fieldName)) {
                    Date date = null;
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double d = cell.getNumericCellValue();
                        date = HSSFDateUtil.getJavaDate(d);
                    }
                    // 如果属性是日期类型则将内容转换成日期对象
                    ReflectUtil.invokeSetter(target, fieldName, DateUtil.formatDate2Date(date));
                } else {
                    cell.setCellType(CellType.STRING);
                    String content = cell.getStringCellValue();
                    Field field = clazz.getDeclaredField(fieldName);
                    ReflectUtil.invokeSetter(target, fieldName, parseValueWithType(content, field.getType()));
                }
            }
            dataModels.add(target);
        }
        return dataModels;
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list, String groupName, String password) throws Exception {
        throw new BusinessException("不支持2007版本Excel导出！");
    }

	@Override
	protected Object parseValueWithType(String value, Class<?> type) {
		// 由于Excel2007的numeric类型只返回double型，所以对于类型为整型的属性，要提前对numeric字符串进行转换
		if (Byte.TYPE == type || Short.TYPE == type || Short.TYPE == type || Long.TYPE == type) {
			value = String.valueOf((long) Double.parseDouble(value));
		}
		return super.parseValueWithType(value, type);
	}

    //判断需要哪些属性，如果注解的属性是空，则认为是所有的属性
    private String[] getField(Class clazz, String groupName) {
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List<String> fieldAnnotations = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
            if (fields[i].isAnnotationPresent(ExcelImport.class)) {
                ExcelImport excel = fields[i].getAnnotation(ExcelImport.class);
                if ("".equals(groupName) || groupName == null || "".equals(excel.group()[0])) {
                    fieldAnnotations.add(fields[i].getName());
                } else if (ArrayUtils.contains(excel.group(), groupName)) {
                    fieldAnnotations.add(fields[i].getName());
                }
            }
        }
        if (fieldAnnotations.size() > 0) {
            fieldNames = new String[fieldAnnotations.size()];
            return fieldAnnotations.toArray(fieldNames);
        }
        return fieldNames;
    }

    // ------ 待删除 ------
    /**
     * 获取单元格的内容
     *
     * @param cell
     *            单元格
     * @return 返回单元格内容
     */
    /*
	private String getCellContent(XSSFCell cell) {
		Object obj = null;
		switch (cell.getCellTypeEnum()) {
			case NUMERIC : // 数字
				DecimalFormat df = new DecimalFormat("0");
				obj = df.format(cell.getNumericCellValue());
				break;
			case BOOLEAN : // 布尔
				obj = cell.getBooleanCellValue();
				break;
			case FORMULA : // 公式
				obj = cell.getCellFormula() ;
				break;
			case STRING : // 字符串
				obj = cell.getStringCellValue();
				break;
			case BLANK : // 空值
			case ERROR : // 故障
			default :
				break;
		}
		return obj + "";
	}
	*/
}