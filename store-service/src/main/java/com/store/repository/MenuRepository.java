package com.store.repository;

import com.store.data.entity.TbMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends CrudRepository<TbMenu, Long> {
    @Query(value = "select count(1) from tb_menu where menu_name = :menuName and dir_level = :dirLevel and parent_id = :parentId", nativeQuery = true)
    long countByMenuName(@Param(value = "menuName") String menuName, @Param("dirLevel") Integer dirLevel, @Param("parentId") Long parentId);

    @Query(value = "select count(1) from tb_menu where menu_name = :menuName and id != :id and dir_level = :dirLevel and parent_id = :parentId", nativeQuery = true)
    long countByMenuName(@Param("menuName") String menuName, @Param("id") Long id, @Param("dirLevel") Integer dirLevel, @Param("parentId") Long parentId);

    @Query(value = "select * from tb_menu where dir_level = :dirLevel", nativeQuery = true)
    List<TbMenu> listByDirLevel(@Param("dirLevel") Integer dirLevel);
}