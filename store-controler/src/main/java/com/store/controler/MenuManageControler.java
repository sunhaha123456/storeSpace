package com.store.controler;

import com.store.common.data.base.PageList;
import com.store.data.base.PageParam;
import com.store.data.entity.TbMenu;
import com.store.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * 功能：菜单管理 controller
 * @author sunpeng
 * @date 2018
 */
@Slf4j
@RestController
@RequestMapping("/user/menuManage")
public class MenuManageControler {

    @Inject
	private MenuService menuService;

    /**
     * 功能：获取当前用户下的关联菜单
     * 备注：用户所属组状态：正常，菜单状态：正常，才能返回
     *       即：返回状态正常组的正常菜单
     * @return
     */
    @PostMapping(value = "/getUserMenu")
    public List<TbMenu> getUserMenu() {
        return menuService.getUserMenu();
    }

    /**
     * 功能：一级菜单，分页查询
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value= "/searchRoot")
    public PageList<TbMenu> searchRoot(@RequestBody PageParam param) throws Exception {
        return menuService.searchRoot(param);
    }

    @GetMapping(value= "/getChild")
    public List<TbMenu> getChild(@RequestParam Long id) {
        return menuService.getChild(id);
    }

    /**
     * 功能：新增/修改
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value= "/save")
    public void save(@RequestBody TbMenu param) throws Exception {
        menuService.save(param);
    }

    @GetMapping(value= "/getDetail")
    public TbMenu getDetail(@RequestParam Long id) {
        return menuService.getDetail(id);
    }
}