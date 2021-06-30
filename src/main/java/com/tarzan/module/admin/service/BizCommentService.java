package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.module.admin.mapper.BizCommentMapper;
import com.tarzan.module.admin.model.BizComment;
import com.tarzan.module.admin.vo.CommentConditionVo;
import org.springframework.stereotype.Service;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizCommentService extends ServiceImpl<BizCommentMapper, BizComment> {

    public IPage<BizComment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        IPage<BizComment> page = new Page<>(pageNumber, pageSize);
        page.setRecords(baseMapper.selectComments(page, vo));
        return page;
    }

    public int deleteBatch(Integer[] ids) {
        return baseMapper.delete(Wrappers.<BizComment>lambdaQuery().in(BizComment::getId,ids).or().in(BizComment::getPid,ids));
    }
}
