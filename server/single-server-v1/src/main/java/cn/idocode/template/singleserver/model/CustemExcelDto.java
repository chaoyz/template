package cn.idocode.template.singleserver.model;

import lombok.Data;

import java.util.List;

/**
 * 自定义表格操作模板类
 * 此类表示每一个sheet中表格数据
 *
 * @author levicyang
 * 2020-08-08 15:50
 */
@Data
public class CustemExcelDto {

    /**
     * 每一个sheet的自定义名字，可以不填写
     */
    private String sheetName;

    /**
     * 每一个sheet中的第一行标题，如果没有的话则无标题
     */
    private List<String> titles;

    /**
     * 表格中每一行数据，二位数组数据，
     */
    private List<List<Object>> rows;

    /**
     * 保存每行中每个cell中类型数据信息，解析表格时候使用到此字段
     */
    private List<Class<?>> rowType;
}
