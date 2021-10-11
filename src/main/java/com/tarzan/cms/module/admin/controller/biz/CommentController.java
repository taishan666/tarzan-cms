package com.tarzan.cms.module.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.service.biz.CommentService;
import com.tarzan.cms.module.admin.vo.CommentConditionVo;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 后台评论管理
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@RestController
@RequestMapping("comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("list")
    public PageResultVo loadNotify(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        IPage<Comment> commentPage = commentService.selectComments(vo, pageNumber, pageSize);
        return ResultUtil.table(commentPage.getRecords(), commentPage.getTotal());
    }

    @PostMapping("/delete")
    public ResponseVo delete(Integer id) {
       return deleteBatch(Arrays.asList(id));
    }

    @PostMapping("/batch/delete")
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = commentService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除评论成功");
        } else {
            return ResultUtil.error("删除评论失败");
        }
    }

    @PostMapping("/audit")
    public ResponseVo audit(Comment bizComment, String replyContent) {
        try {
            commentService.updateById(bizComment);
            if (StringUtils.isNotBlank(replyContent)) {
                Comment replyComment = new Comment();
                replyComment.setPid(bizComment.getId());
                replyComment.setSid(bizComment.getSid());
                replyComment.setContent(replyContent);
                commentService.replyComment(replyComment);
            }
            return ResultUtil.success("审核成功");
        } catch (Exception e) {
            return ResultUtil.success("审核失败");
        }
    }


}
