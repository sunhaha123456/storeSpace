package com.store.common.util.excel;

import lombok.Data;

/**
 * 描述：excel 字段属性
 * Created by jay on 2017-9-18.
 */
@Data
public class FieldObject {

    private String name;

    private boolean lockBoolean;

    public boolean getLockBoolean() {
        return lockBoolean;
    }

    private ExcelDataEnums format;

}
