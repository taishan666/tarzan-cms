package com.tarzan.cms.cache;

import com.tarzan.cms.modules.admin.model.biz.Category;
import com.tarzan.cms.modules.admin.service.biz.CategoryService;
import com.tarzan.cms.utils.SpringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan
 */
public class CategoryCache {

    private static final CategoryService CATEGORY_SERVICE;
    private static Map<Integer, String> categoryMap = new HashMap<>();

    static {
        CATEGORY_SERVICE = SpringUtil.getBean(CategoryService.class);
    }

    public static void initCategory() {
        List<Category> list = CATEGORY_SERVICE.lambdaQuery().select(Category::getId, Category::getName).list();
        categoryMap = list.stream().collect(Collectors.toMap(Category::getId, Category::getName));
    }

    public static String getName(Integer id) {
        String name = categoryMap.get(id);
        if (name == null) {
            name = CATEGORY_SERVICE.getById(id).getName();
            categoryMap.put(id, name);
        }
        return name;
    }

    public static void save(Category category) {
        categoryMap.put(category.getId(), category.getName());
    }

    public static void delete(Integer id) {
        categoryMap.remove(id);
    }
}
