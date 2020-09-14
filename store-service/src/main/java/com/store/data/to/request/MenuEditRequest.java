package com.store.data.to.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MenuEditRequest {
    @NotNull(groups = BaseInfo.class)
    private Long roleGroupId; // 角色id
    @NotNull(groups = BaseInfo.class)
    private List<Long> menuIds; // 菜单list
    public interface BaseInfo{}
}