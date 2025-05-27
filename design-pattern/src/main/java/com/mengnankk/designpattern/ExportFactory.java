package com.mengnankk.designpattern;

// 工厂类
public class ExportFactory {
    public static ExportService getExport(String type) {
        if ("excel".equalsIgnoreCase(type)) {
            return new ExcelExportService();
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }
}


interface ExportService {
    void export();
}


class ExcelExportService implements ExportService {
    public void export() {
        System.out.println("导出Excel");
    }
}
