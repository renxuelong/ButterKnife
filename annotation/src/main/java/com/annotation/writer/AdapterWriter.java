package com.annotation.writer;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.VariableElement;

/**
 * Created by renxl
 * On 2017/7/22 16:05.
 */

public interface AdapterWriter {
    public void generate(Map<String, List<VariableElement>> map);
}
