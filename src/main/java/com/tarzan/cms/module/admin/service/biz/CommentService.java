package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.biz.CommentMapper;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.model.biz.Love;
import com.tarzan.cms.module.admin.vo.CommentConditionVo;
import com.tarzan.cms.utils.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    private final LoveService loveService;
    private final ArticleService articleService;

    public IPage<Comment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize) {
        IPage<Comment> page = new Page<>(pageNumber, pageSize);
        page.setRecords(baseMapper.selectComments(page, vo));
        return page;
    }

    public boolean deleteBatch(Integer[] ids) {
        return remove(Wrappers.<Comment>lambdaQuery().in(Comment::getId,ids).or().in(Comment::getPid,ids));
    }

    public int count() {
        return count(Wrappers.<Comment>lambdaQuery().eq(Comment::getStatus, CoreConst.STATUS_VALID));
    }

    public IPage<Comment> selectComments1(CommentConditionVo vo, Integer pageNumber, Integer pageSize){
        IPage<Comment> page = new Page<>(pageNumber, pageSize);
        Comment comment= BeanUtil.copy(vo,Comment.class);
        page=page(page,Wrappers.query(comment));
        List<Comment> comments=page.getRecords();
        if(CollectionUtils.isNotEmpty(comments)){
            List<Integer> ids=comments.stream().map(Comment::getId).collect(Collectors.toList());
            List<Integer> pids=comments.stream().map(Comment::getPid).collect(Collectors.toList());
            List<Integer> sids=comments.stream().map(Comment::getSid).collect(Collectors.toList());
            List<Comment> parentComments=listByIds(pids);
            Map<Integer,Comment> map=parentComments.stream().collect(Collectors.toMap(Comment::getId,e->e));
            List<Love> loves=loveService.list(Wrappers.<Love>lambdaQuery().in(Love::getBizId,ids));
            Map<Integer,Long> loveMap=loves.stream().collect(Collectors.groupingBy(Love::getBizId,Collectors.counting()));
            List<Article> articles= articleService.listByIds(sids);
            Map<Integer,Article> articleMap=articles.stream().collect(Collectors.toMap(Article::getId,e->e));
            comments.forEach(e->{
                e.setParent(map.get(e.getPid()));
                Long count= loveMap.get(e.getId())==null?0L:loveMap.get(e.getId());
                e.setLoveCount(count);
                e.setArticle(articleMap.get(e.getSid()));
            });
            page.setRecords(comments);
        }
        return page;
    }
}
