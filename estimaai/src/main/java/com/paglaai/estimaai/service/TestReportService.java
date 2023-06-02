//package com.paglaai.estimaai.service;
//
//import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
//import net.sf.dynamicreports.report.builder.DynamicReports;
//import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
//import net.sf.dynamicreports.report.builder.style.StyleBuilder;
//import net.sf.dynamicreports.report.datasource.ListDataSource;
//import net.sf.dynamicreports.report.exception.DRException;
//import net.sf.jasperreports.engine.JREmptyDataSource;
//import net.sf.jasperreports.engine.JRDataSource;
//
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DynamicReportExample {
//    public static void main(String[] args) {
//        List<DataItem> dataList = createDataList();
//
//        // Create a list data source
//        JRDataSource dataSource = new ListDataSource<>(dataList);
//
//        try {
//            // Create a report
//            JasperReportBuilder report = DynamicReports.report();
//
//            // Define styles for report elements
//            StyleBuilder boldStyle = DynamicReports.stl.style().bold();
//            StyleBuilder headerStyle = DynamicReports.stl.style().setBackgroundColor(Color.LIGHT_GRAY).bold();
//
//            // Define columns for the main table
//            TextColumnBuilder<String> nameColumn = DynamicReports.col.column("Name", "name", DynamicReports.type.stringType());
//            TextColumnBuilder<Integer> ageColumn = DynamicReports.col.column("Age", "age", DynamicReports.type.integerType());
//
//            // Add columns to the report
//            report.columns(nameColumn, ageColumn)
//                    .title(DynamicReports.cmp.text("Main Table").setStyle(boldStyle))
//                    .pageFooter(DynamicReports.cmp.pageXofY())
//                    .setDataSource(dataSource);
//
//            // Export the report to PDF
//            report.toPdf("output.pdf");
//        } catch (DRException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static List<DataItem> createDataList() {
//        List<DataItem> dataList = new ArrayList<>();
//        dataList.add(new DataItem("John", 25));
//        dataList.add(new DataItem("Mary", 30));
//        dataList.add(new DataItem("David", 28));
//        return dataList;
//    }
//}
//
//class DataItem {
//    private String name;
//    private int age;
//
//    public DataItem(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//}
