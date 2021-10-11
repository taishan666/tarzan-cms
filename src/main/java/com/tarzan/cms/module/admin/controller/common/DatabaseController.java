package com.tarzan.cms.module.admin.controller.common;

import com.google.common.collect.Lists;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.vo.DbBackupVO;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.DbBackupTools;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;

/**
 * 后台SQL监控
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/database")
@Slf4j
@AllArgsConstructor
public class DatabaseController {

    private  final DbBackupTools dbTools;

    /* 数据监控 */
    @GetMapping(value = "/monitoring")
    public ModelAndView databaseMonitoring() {
        return new ModelAndView(CoreConst.ADMIN_PREFIX + "database/monitoring");
    }

    /* 备份数据库 */
    @PostMapping("backupSQL")
    @ResponseBody
    public ResponseVo backupSQL(){
        if (dbTools.backSql()) {
            return ResultUtil.success("数据备份成功");
        } else {
            return ResultUtil.error("数据备份失败");
        }
    }

    /* 备份文件列表 */
    @PostMapping("backupList")
    @ResponseBody
    public PageResultVo backupList(Integer pageNumber, Integer pageSize){
        File file=new File(dbTools.getSqlBackupPath());
        File[] files= file.listFiles();
        if(files==null){
            return ResultUtil.table(null,null);
        }
        List<DbBackupVO> list= Lists.newArrayList();
        Arrays.asList(files).forEach(e->{
            DbBackupVO vo=new DbBackupVO();
            vo.setFileName(e.getName());
            vo.setSize(e.length());
            vo.setCreateTime(new Date(e.lastModified()));
            list.add(vo);
        });
        Collections.reverse(list);
        int endIndex = Math.min(pageNumber * pageSize, list.size());
        return ResultUtil.table(list.subList((pageNumber - 1) * pageSize, endIndex), (long) list.size());
    }

    /*删除备份*/
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo deleteRole(String fileName) {
        File file=new File(dbTools.getSqlBackupPath()+fileName);
        if (file.delete()) {
            return ResultUtil.success("删除备份成功");
        } else {
            return ResultUtil.error("删除备份失败");
        }
    }

    /*还原备份*/
    @PostMapping("rollback")
    @ResponseBody
    public ResponseVo rollback(String fileName){
        if ( dbTools.rollback(fileName)) {
            return ResultUtil.success("数据恢复成功");
        } else {
            return ResultUtil.error("数据恢复失败");
        }
    }
}
