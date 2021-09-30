package com.tarzan.cms.module.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.utils.*;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.model.biz.Love;
import com.tarzan.cms.module.admin.service.biz.ArticleLookService;
import com.tarzan.cms.module.admin.service.biz.CommentService;
import com.tarzan.cms.module.admin.service.biz.LoveService;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.vo.CommentConditionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 给前台页面提供的接口，包括针对文章进行的添加评论、获取评论、增加浏览次数和点赞操作
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@RestController
@RequestMapping("blog/api")
@AllArgsConstructor
public class BlogApiController {

    private final CommentService commentService;
    private final ArticleLookService articleLookService;
    private final LoveService loveService;


    @PostMapping("comments")
    public IPage<Comment> getComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        return commentService.selectComments(vo, pageNumber, pageSize);
    }

    @PostMapping("comment/save")
    public ResponseVo saveComment(HttpServletRequest request, Comment comment) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(comment.getNickname())) {
            return ResultUtil.error("请输入昵称");
        }
        String content = comment.getContent();
        if (!XssKillerUtil.isValid(content)) {
            return ResultUtil.error("内容不合法");
        }
        content = XssKillerUtil.clean(content.trim()).replaceAll("(<p><br></p>)|(<p></p>)", "");
        Date date = new Date();
        comment.setContent(content);
        comment.setIp(IpUtil.getIpAddr(request));
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        if (StringUtils.isNotBlank(comment.getQq())) {
            comment.setAvatar("http://q1.qlogo.cn/g?b=qq&nk=" + comment.getQq() + "&s=100");
        } else if (StringUtils.isNotBlank(comment.getEmail())) {
            String entry = null;
            try {
                entry = MD5Util.md5Hex(comment.getEmail());
            } catch (NoSuchAlgorithmException e) {
                log.error("MD5出现异常{}", e.getMessage(), e);
            }
            comment.setAvatar("http://www.gravatar.com/avatar/" + entry + "?d=mp");
        }
        boolean a = commentService.save(comment);
        if (a) {
            return ResultUtil.success("评论提交成功,系统正在审核");
        } else {
            return ResultUtil.error("评论提交失败");
        }
    }


    @PostMapping("article/look")
    public ResponseVo checkLook(HttpServletRequest request, Integer articleId) {
        /*浏览次数*/
        Date date = new Date();
        String ip = IpUtil.getIpAddr(request);
        int checkCount = articleLookService.checkArticleLook(articleId, ip, DateUtil.addHours(date, -1));
        if (checkCount == 0) {
            ArticleLook articleLook = new ArticleLook();
            articleLook.setArticleId(articleId);
            articleLook.setUserIp(ip);
            articleLook.setLookTime(date);
            articleLook.setCreateTime(date);
            articleLook.setUpdateTime(date);
            articleLookService.save(articleLook);
            return ResultUtil.success("浏览次数+1");
        } else {
            return ResultUtil.error("一小时内重复浏览不增加次数哦");
        }
    }


    @PostMapping("love")
    public ResponseVo love(HttpServletRequest request, Integer bizId, Integer bizType) {
        Date date = new Date();
        String ip = IpUtil.getIpAddr(request);
        Love love = loveService.checkLove(bizId, ip);
        if (love == null) {
            love = new Love();
            love.setBizId(bizId);
            love.setBizType(bizType);
            love.setUserIp(ip);
            love.setStatus(CoreConst.STATUS_VALID);
            love.setCreateTime(date);
            love.setUpdateTime(date);
            loveService.save(love);
            return ResultUtil.success("点赞成功");
        } else {
            return ResultUtil.error("您已赞过了哦~");
        }
    }

}
