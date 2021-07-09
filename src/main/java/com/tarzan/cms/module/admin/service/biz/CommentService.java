package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.module.admin.mapper.biz.CommentMapper;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.vo.CommentConditionVo;
import org.springframework.stereotype.Service;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    public IPage<Comment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        IPage<Comment> page = new Page<>(pageNumber, pageSize);
        page.setRecords(baseMapper.selectComments(page, vo));
        return page;
    }

    public boolean deleteBatch(Integer[] ids) {
        return remove(Wrappers.<Comment>lambdaQuery().in(Comment::getId,ids).or().in(Comment::getPid,ids));
    }
}
