package com.epam.service.index;

import java.util.List;

public interface TemplateService<T> {

    List<T> search(String keyword);

}
