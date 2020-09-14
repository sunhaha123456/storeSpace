package com.store.common.util.excel;

import com.store.common.util.DateUtil;
import com.store.common.util.ReflectUtil;
import com.store.common.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能：用于97-2003之间（.xls）
 */
public class HssfExcelUtil extends ExcelUtil {

    private final static String FILE_TYPE = ".xls";

    private final static String PASSWORD = "123456111";

    private static HSSFCellStyle contentStyleDefault;

    private static HSSFCellStyle contentStyleDate;

    private static HSSFCellStyle contentStyleDouble;

    public HssfExcelUtil() {}

    public HssfExcelUtil(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public <T> List<T> readExcel(Class<T> clazz, int sheetNo, boolean hasTitle, String group) throws Exception {
        List<T> dataModels = new ArrayList<>();
        //String[] fieldNames = getClassFieldByExcelImport(clazz, null);
        String[] fieldNames = getField(clazz, group);
        // 获取excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(sheetNo);
        int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0); // 如果有标题则从第二行开始
        for (int i = start; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
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
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                if (isDateType(clazz, fieldName)) {
                    String date = DateUtil.formatDate2Date(cell.getDateCellValue());
                    ReflectUtil.invokeSetter(target, fieldName, date);
                } else {
                    String content = getCellContent(cell);
                    Field field = clazz.getDeclaredField(fieldName);
                    Object object = parseValueWithType(content, field.getType());
                    ReflectUtil.invokeSetter(target, fieldName, object);
                }
            }
            dataModels.add(target);
        }
        return dataModels;
    }

    @Override
    public <T> void writeExcel(HttpServletResponse response, String filename, String sheetName, List<T> list, String groupName, String password) throws Exception {
        // reponse init
        response.setContentType("octets/stream");
        response.addHeader("Content-Type", "octets/stream; charset=utf-8");
        filename = new String(filename.getBytes("UTF-8"), "iso8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename + FILE_TYPE);
        OutputStream outputStream = response.getOutputStream();
        // 声明一个工作薄
        contentStyleDefault = null;
        contentStyleDate = null;
        contentStyleDouble = null;
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 字段中文名
        List<String> headers = new ArrayList<>();
        // 字段名
        List<FieldObject> fields = new ArrayList<>();
        boolean isLock = getExcelExportData(list.get(0).getClass(), headers, fields, groupName);
        if (headers.size() == 0) {
            throw new Exception("该类没有Excel导出注解请检查!");
        }
        setSheet(workbook, sheetName, headers, list, fields, isLock);
        if (StringUtil.isEmpty(password)) {
            Biff8EncryptionKey.setCurrentUserPassword(null);
        } else {
            Biff8EncryptionKey.setCurrentUserPassword(password);
        }
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
    }

    // 功能：获取单元格的内容
    // @param cell 单元格
    // @return 返回单元格内容
    private String getCellContent(HSSFCell cell) {
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

    /**
     * 功能：获取要到处数据的头部标题，以及标题下的数据
     * @param c
     * @param headers
     * @param fields
     * @param groupName
     * @return 导出的Excel是否需要进行不可修改的锁定
     */
    private boolean getExcelExportData(Class<?> c, List<String> headers, List<FieldObject> fields, String groupName) {
        boolean isLock = false;
        Field[] allFields = c.getDeclaredFields();
        for (Field field : allFields) {
            if (!field.isAnnotationPresent(ExcelExport.class)) {
                continue;
            }
            ExcelExport excel = field.getAnnotation(ExcelExport.class);
            FieldObject fieldObject;
            if (groupName == null || "".equals(groupName) || "".equals(excel.group()[0]) || ArrayUtils.contains(excel.group(), groupName)) {
                fieldObject = new FieldObject();
                if (excel.lockBoolean()) {
                    isLock = true;
                }
                fieldObject.setLockBoolean(excel.lockBoolean());
                fieldObject.setName(field.getName());
                fieldObject.setFormat(excel.format());
                fields.add(fieldObject);
                headers.add(excel.name());
            }
        }
        return isLock;
    }

    /**
     * 功能：表格赋值，对标题以及标题下的内容进行赋值
     * @param workbook
     * @param sheetName
     * @param headers
     * @param list
     * @param fields
     * @param isLock
     * @param <T>
     */
    private <T> void setSheet(HSSFWorkbook workbook, String sheetName, List<String> headers, List<T> list, List<FieldObject> fields, boolean isLock) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        //sheet.setDefaultColumnWidth(30);
        // 表头样式
        HSSFCellStyle headerStyle = getHeadStyle(workbook);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        if (isLock) {
            sheet.protectSheet(PASSWORD);
        }
        HSSFRichTextString text;
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
            //设置列宽度自适应
            sheet.setColumnWidth(i, headers.get(i).getBytes().length * 256);
        }
        // 循环赋值
        int rowCount = 1;
        for (int i = 0; i < list.size(); i++) {
            // 正文内容样式
            row = sheet.createRow(rowCount++);
//            HSSFCell cell = row.createCell(rowCount);
//            cell.setCellStyle(contentStyle);
            setRowValue(workbook, list.get(i), fields, row);
        }
    }

    // 标题样式
    private HSSFCellStyle getHeadStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = getCellBaseStyle(workbook);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    //基本样式
    private HSSFCellStyle getCellBaseStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        return contentStyle;
    }

    // 填写sheet的每行的值
    private void setRowValue(HSSFWorkbook workbook, Object obj, List<FieldObject> fields, HSSFRow row) {
//        WritableCellFormat doubleFormat = new WritableCellFormat(NumberFormats.FLOAT);
//        WritableCellFormat intFormat = new WritableCellFormat(NumberFormats.INTEGER);
//        WritableCellFormat percentFlaot = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
        Class<?> c = obj.getClass();
//        Field[] fields = c.getDeclaredFields();
        Object value;
//        jxl.write.Number number = null;
//        Label label = null;
        PropertyDescriptor pd;
        HSSFDataFormat format = workbook.createDataFormat();
        for (int i = 0; i < fields.size(); i++) {
            try {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(getCellStyle(workbook, format, fields.get(i).getFormat()));
                HSSFCellStyle cellStyle = cell.getCellStyle();
                cellStyle.setLocked(fields.get(i).getLockBoolean());
                pd = new PropertyDescriptor(fields.get(i).getName(), c);
                Method getMethod = pd.getReadMethod();// 获得get方法
                value = getMethod.invoke(obj);
                if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                } else {
                    cell.setCellValue(value == null ? "" : value.toString());
                }
                //sheet.autoSizeColumn(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private CellStyle getCellStyle(HSSFWorkbook workbook, HSSFDataFormat format, ExcelDataEnums enums) {
        switch (enums) {
            case YYYYMM:
                if (contentStyleDate == null) {
                    contentStyleDate = getContentStyle(workbook);
                    contentStyleDate.setDataFormat(format.getFormat("yyyy-mm"));
                }
                return contentStyleDate;
            case DOUBLE:
                if (contentStyleDouble == null) {
                    contentStyleDouble = getContentStyle(workbook);
                    contentStyleDouble.setDataFormat(format.getFormat("0.00"));
                }
                return contentStyleDouble;
            default:
                if (contentStyleDefault == null) {
                    contentStyleDefault = getContentStyle(workbook);
                }
                return contentStyleDefault;
        }
    }

    //内容样式
    private HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = getCellBaseStyle(workbook);
        contentStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        contentStyle.setFont(font);
        return contentStyle;
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

    // --- 待删除 start ---
    /*
    private <T> boolean checkIsAddList(T target, List<String> andNullFiled) throws Exception {
        if (andNullFiled.size() > 0) {
            for (String fieldName : andNullFiled) {
                Object o = ReflectUtil.invokeGetter(target, fieldName);
                if (o != null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    */
}