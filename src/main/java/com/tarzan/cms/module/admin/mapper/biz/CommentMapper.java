package com.tarzan.cms.module.admin.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.vo.CommentConditionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    List<Comment> selectComments(@Param("page") IPage<Comment> page, @Param("vo") CommentConditionVo vo);

}
