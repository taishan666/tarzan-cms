
-- ----------------------------
-- Records of biz_article
-- ----------------------------
INSERT INTO `biz_article`(`id`, `title`, `user_id`, `author`, `cover_image`, `qrcode_path`, `is_markdown`, `content`, `content_md`, `top`, `category_id`, `status`, `recommended`, `slider`, `slider_img`, `original`, `description`, `keywords`, `comment`, `create_time`, `update_time`) VALUES (13, '基于用户的协同过滤算法JAVA实现的推荐系统', '1', NULL, 'http://localhost:80/u/20210628\\src=http___n.sinaimg.cn_sinacn_20171025_bec0-fymzzpw1715641.jpg&refer=http___n.sinaimg_1624849356390.sinaimg.cn_sinacn_20171025_bec0-fymzzpw1715641.jpg&refer=http___n.sinaimg.jpg', NULL, 1, '<h1>JAVA推荐系统</h1>\r\n\r\n<p>文章末附带项目源码地址</p>\r\n\r\n<h2>系统原理</h2>\r\n\r\n<p>该系统使用java编写的基于用户的协同过滤算法（UserCF） 利用统计学的相关系数经常皮尔森（pearson）相关系数计算相关系数来实现千人千面的推荐系统。</p>\r\n\r\n<p>协同过滤推荐算法是诞生最早，并且较为著名的推荐算法。主要的功能是预测和推荐。算法通过对用户历史行为数据的挖掘发现用户的偏好，基于不同的偏好对用户进行群组划分并推荐品味相似的商品。协同过滤推荐算法分为两类，分别是基于用户的协同过滤算法(user-based collaboratIve filtering)，和基于物品的协同过滤算法(item-based collaborative filtering)。简单的说就是：人以类聚，物以群分。</p>\r\n\r\n<h3>皮尔森(pearson)相关系数公式</h3>\r\n\r\n<p><img alt=\"\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/20200802193647612.png\" style=\"height:106px; width:424px\" /></p>\r\n\r\n<p>公式定义为： 两个连续变量(X,Y)的pearson相关性系数(Px,y)等于它们之间的协方差cov(X,Y)除以它们各自标准差的乘积(&sigma;X,&sigma;Y)。系数的取值总是在-1.0到1.0之间，接近0的变量被成为无相关性，接近1或者-1被称为具有强相关性。 通常情况下通过以下取值范围判断变量的相关强度： 相关系数 0.8-1.0 极强相关 0.6-0.8 强相关 0.4-0.6 中等程度相关 0.2-0.4 弱相关 0.0-0.2 极弱相关或无相关</p>\r\n\r\n<h3>java代码实现</h3>\r\n\r\n<pre data-widget=\"codeSnippet\">\r\n<code class=\"hljs\"> /**\r\n     * 方法描述: 皮尔森（pearson）相关系数计算\r\n     *\r\n     * @param xs\r\n     * @param ys \r\n     * @Return {@link Double}\r\n     * @throws \r\n     * @author tarzan\r\n     * @date 2020年07月31日 17:03:20\r\n     */\r\n    public static Double getRelate(List&lt;Integer&gt; xs, List&lt;Integer&gt;  ys){\r\n        int n=xs.size();\r\n        double Ex= xs.stream().mapToDouble(x-&gt;x).sum();\r\n        double Ey=ys.stream().mapToDouble(y-&gt;y).sum();\r\n        double Ex2=xs.stream().mapToDouble(x-&gt;Math.pow(x,2)).sum();\r\n        double Ey2=ys.stream().mapToDouble(y-&gt;Math.pow(y,2)).sum();\r\n        double Exy= IntStream.range(0,n).mapToDouble(i-&gt;xs.get(i)*ys.get(i)).sum();\r\n        double numerator=Exy-Ex*Ey/n;\r\n        double denominator=Math.sqrt((Ex2-Math.pow(Ex,2)/n)*(Ey2-Math.pow(Ey,2)/n));\r\n        if (denominator==0) return 0.0;\r\n        return numerator/denominator;\r\n    }</code></pre>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<h2>软件架构</h2>\r\n\r\n<p>Spring boot单项目</p>\r\n\r\n<h2>安装教程</h2>\r\n\r\n<ol>\r\n	<li>git下载源码</li>\r\n	<li>maven构建依赖</li>\r\n	<li>idea-java运行</li>\r\n</ol>\r\n\r\n<h2>使用说明</h2>\r\n\r\n<ol>\r\n	<li>找到 src / main / java / com / tarzan / recommend / RecommendSystemApplication.java 右键java 运行</li>\r\n</ol>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;<img alt=\"输入图片说明\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/img_convert/f286e55a27d71d0c5b8644a0d7d32ad1.png\" />&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>2.传入不同的用户id，得到不同的推荐数据</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;<img alt=\"输入图片说明\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/img_convert/3d24a5e080cd5cbb4dd355b042c41792.png\" /></p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>3.项目中用到的文件数据集ml-100k 在 src / main / resources目录下</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>gitee源码地址：<a href=\"https://gitee.com/taisan/recommend_system\">https://gitee.com/taisan/recommend_system</a></p>\r\n\r\n<h2>技术交流&amp;问题反馈</h2>\r\n\r\n<pre data-widget=\"codeSnippet\">\r\n<code class=\"language-html hljs\">  刚刚整理的代码还有很多不足之处，如有问题请联系我\r\n\r\n  联系QQ:1334512682 \r\n  微信号：vxhqqh</code></pre>\r\n', '<h1>JAVA推荐系统</h1>\r\n\r\n<p>文章末附带项目源码地址</p>\r\n\r\n<h2>系统原理</h2>\r\n\r\n<p>该系统使用java编写的基于用户的协同过滤算法（UserCF） 利用统计学的相关系数经常皮尔森（pearson）相关系数计算相关系数来实现千人千面的推荐系统。</p>\r\n\r\n<p>协同过滤推荐算法是诞生最早，并且较为著名的推荐算法。主要的功能是预测和推荐。算法通过对用户历史行为数据的挖掘发现用户的偏好，基于不同的偏好对用户进行群组划分并推荐品味相似的商品。协同过滤推荐算法分为两类，分别是基于用户的协同过滤算法(user-based collaboratIve filtering)，和基于物品的协同过滤算法(item-based collaborative filtering)。简单的说就是：人以类聚，物以群分。</p>\r\n\r\n<h3>皮尔森(pearson)相关系数公式</h3>\r\n\r\n<p><img alt=\"\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/20200802193647612.png\" style=\"height:106px; width:424px\" /></p>\r\n\r\n<p>公式定义为： 两个连续变量(X,Y)的pearson相关性系数(Px,y)等于它们之间的协方差cov(X,Y)除以它们各自标准差的乘积(&sigma;X,&sigma;Y)。系数的取值总是在-1.0到1.0之间，接近0的变量被成为无相关性，接近1或者-1被称为具有强相关性。 通常情况下通过以下取值范围判断变量的相关强度： 相关系数 0.8-1.0 极强相关 0.6-0.8 强相关 0.4-0.6 中等程度相关 0.2-0.4 弱相关 0.0-0.2 极弱相关或无相关</p>\r\n\r\n<h3>java代码实现</h3>\r\n\r\n<pre data-widget=\"codeSnippet\">\r\n<code class=\"hljs\"> /**\r\n     * 方法描述: 皮尔森（pearson）相关系数计算\r\n     *\r\n     * @param xs\r\n     * @param ys \r\n     * @Return {@link Double}\r\n     * @throws \r\n     * @author tarzan\r\n     * @date 2020年07月31日 17:03:20\r\n     */\r\n    public static Double getRelate(List&lt;Integer&gt; xs, List&lt;Integer&gt;  ys){\r\n        int n=xs.size();\r\n        double Ex= xs.stream().mapToDouble(x-&gt;x).sum();\r\n        double Ey=ys.stream().mapToDouble(y-&gt;y).sum();\r\n        double Ex2=xs.stream().mapToDouble(x-&gt;Math.pow(x,2)).sum();\r\n        double Ey2=ys.stream().mapToDouble(y-&gt;Math.pow(y,2)).sum();\r\n        double Exy= IntStream.range(0,n).mapToDouble(i-&gt;xs.get(i)*ys.get(i)).sum();\r\n        double numerator=Exy-Ex*Ey/n;\r\n        double denominator=Math.sqrt((Ex2-Math.pow(Ex,2)/n)*(Ey2-Math.pow(Ey,2)/n));\r\n        if (denominator==0) return 0.0;\r\n        return numerator/denominator;\r\n    }</code></pre>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<h2>软件架构</h2>\r\n\r\n<p>Spring boot单项目</p>\r\n\r\n<h2>安装教程</h2>\r\n\r\n<ol>\r\n	<li>git下载源码</li>\r\n	<li>maven构建依赖</li>\r\n	<li>idea-java运行</li>\r\n</ol>\r\n\r\n<h2>使用说明</h2>\r\n\r\n<ol>\r\n	<li>找到 src / main / java / com / tarzan / recommend / RecommendSystemApplication.java 右键java 运行</li>\r\n</ol>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;<img alt=\"输入图片说明\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/img_convert/f286e55a27d71d0c5b8644a0d7d32ad1.png\" />&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>2.传入不同的用户id，得到不同的推荐数据</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>&nbsp;<img alt=\"输入图片说明\" data-widget=\"image\" src=\"https://img-blog.csdnimg.cn/img_convert/3d24a5e080cd5cbb4dd355b042c41792.png\" /></p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>3.项目中用到的文件数据集ml-100k 在 src / main / resources目录下</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>gitee源码地址：<a href=\"https://gitee.com/taisan/recommend_system\">https://gitee.com/taisan/recommend_system</a></p>\r\n\r\n<h2>技术交流&amp;问题反馈</h2>\r\n\r\n<pre data-widget=\"codeSnippet\">\r\n<code class=\"language-html hljs\">  刚刚整理的代码还有很多不足之处，如有问题请联系我\r\n\r\n  联系QQ:1334512682 \r\n  微信号：vxhqqh</code></pre>\r\n', 1, 2, 1, 1, 0, '', 1, '该系统使用java编写的基于用户的协同过滤算法（UserCF） 利用统计学的相关系数经常皮尔森（pearson）相关系数计算相关系数来实现千人千面的推荐系统。', '', 1, '2021-06-28 11:03:14', '2021-06-28 11:03:14');

-- ----------------------------
-- Records of biz_article_look
-- ----------------------------
INSERT INTO `biz_article_look` VALUES (1, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-13 23:30:25', '2021-09-13 23:30:25', '2021-09-13 23:30:25');
INSERT INTO `biz_article_look` VALUES (2, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-19 16:56:59', '2021-09-19 16:56:59', '2021-09-19 16:56:59');
INSERT INTO `biz_article_look` VALUES (3, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-20 00:52:15', '2021-09-20 00:52:15', '2021-09-20 00:52:15');
INSERT INTO `biz_article_look` VALUES (4, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-11 11:32:04', '2021-09-11 11:32:04', '2021-09-11 11:32:04');
INSERT INTO `biz_article_look` VALUES (5, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-11 22:16:09', '2021-09-11 22:16:09', '2021-09-11 22:16:09');
INSERT INTO `biz_article_look` VALUES (6, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-12 09:58:22', '2021-09-12 09:58:22', '2021-09-12 09:58:22');
INSERT INTO `biz_article_look` VALUES (7, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-13 10:05:22', '2021-09-13 10:05:22', '2021-09-13 10:05:22');
INSERT INTO `biz_article_look` VALUES (8, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 10:55:45', '2021-09-14 10:55:45', '2021-09-14 10:55:45');
INSERT INTO `biz_article_look` VALUES (9, 2, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 11:06:49', '2021-09-14 11:06:49', '2021-09-14 11:06:49');
INSERT INTO `biz_article_look` VALUES (10, 3, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 12:42:52', '2021-09-14 12:42:52', '2021-09-14 12:42:52');
INSERT INTO `biz_article_look` VALUES (11, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 15:35:20', '2021-09-14 15:35:20', '2021-09-14 15:35:20');
INSERT INTO `biz_article_look` VALUES (12, 6, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 16:35:31', '2021-09-14 16:35:31', '2021-09-14 16:35:31');
INSERT INTO `biz_article_look` VALUES (13, 4, NULL, '0:0:0:0:0:0:0:1', '2021-09-14 16:35:37', '2021-09-14 16:35:37', '2021-09-14 16:35:37');
INSERT INTO `biz_article_look` VALUES (14, 9, NULL, '0:0:0:0:0:0:0:1', '2021-09-15 17:10:53', '2021-09-15 17:10:53', '2021-09-15 17:10:53');
INSERT INTO `biz_article_look` VALUES (15, 10, NULL, '0:0:0:0:0:0:0:1', '2021-09-18 09:28:39', '2021-09-18 09:28:39', '2021-09-18 09:28:39');
INSERT INTO `biz_article_look` VALUES (16, 1, NULL, '0:0:0:0:0:0:0:1', '2021-09-26 15:47:52', '2021-09-26 15:47:52', '2021-09-26 15:47:52');

-- ----------------------------
-- Records of biz_article_tags
-- ----------------------------
INSERT INTO `biz_article_tags` VALUES (1, 2, 1, '2021-09-13 23:30:20', '2021-09-13 23:30:20');
INSERT INTO `biz_article_tags` VALUES (2, 3, 1, '2021-09-13 23:30:20', '2021-09-13 23:30:20');
INSERT INTO `biz_article_tags` VALUES (3, 4, 1, '2021-09-13 23:30:20', '2021-09-13 23:30:20');
INSERT INTO `biz_article_tags` VALUES (6, 1, 2, '2021-09-14 12:36:21', '2021-09-14 12:36:21');
INSERT INTO `biz_article_tags` VALUES (7, 2, 3, '2021-09-14 12:45:41', '2021-09-14 12:45:41');
INSERT INTO `biz_article_tags` VALUES (11, 3, 6, '2021-09-14 13:10:34', '2021-09-14 13:10:34');
INSERT INTO `biz_article_tags` VALUES (12, 4, 6, '2021-09-14 13:10:34', '2021-09-14 13:10:34');
INSERT INTO `biz_article_tags` VALUES (13, 5, 7, '2021-09-14 13:16:10', '2021-09-14 13:16:10');
INSERT INTO `biz_article_tags` VALUES (14, 2, 8, '2021-09-14 13:19:02', '2021-09-14 13:19:02');
INSERT INTO `biz_article_tags` VALUES (19, 10, 11, '2021-09-14 15:31:55', '2021-09-14 15:31:55');
INSERT INTO `biz_article_tags` VALUES (21, 6, 9, '2021-09-14 15:32:22', '2021-09-14 15:32:22');
INSERT INTO `biz_article_tags` VALUES (22, 8, 9, '2021-09-14 15:32:22', '2021-09-14 15:32:22');
INSERT INTO `biz_article_tags` VALUES (23, 6, 10, '2021-09-14 15:32:32', '2021-09-14 15:32:32');
INSERT INTO `biz_article_tags` VALUES (24, 8, 10, '2021-09-14 15:32:32', '2021-09-14 15:32:32');
INSERT INTO `biz_article_tags` VALUES (25, 7, 5, '2021-09-14 15:34:20', '2021-09-14 15:34:20');
INSERT INTO `biz_article_tags` VALUES (26, 10, 4, '2021-09-14 15:34:32', '2021-09-14 15:34:32');

-- ----------------------------
-- Records of biz_category
-- ----------------------------
INSERT INTO `biz_category` VALUES (1, 0, '前端技术', '主要收集、整理的基础前端类文章', 1, 'fa fa-css3', 1, '2021-01-14 21:34:54', '2021-07-25 17:57:50');
INSERT INTO `biz_category` VALUES (2, 0, '后端技术', '网站中记录的后端类文章，包括Java、Spring、SpringBoot、MySQL、大数据和其他在日常工作学习中所用的后端技术', 10, 'fa fa-coffee', 1, '2021-01-14 21:34:57', '2021-09-14 15:28:24');
INSERT INTO `biz_category` VALUES (3, 0, '其他文章', '记录网站建设以及日常工作、学习中的闲言碎语', 50, 'fa fa-folder-open-o', 1, '2021-01-20 22:28:03', '2021-09-14 15:28:50');
INSERT INTO `biz_category` VALUES (4, 0, '领悟', '记录个人生活等文章', 40, NULL, 1, '2021-08-02 11:20:26', '2021-09-14 15:28:38');
INSERT INTO `biz_category` VALUES (5, 3, '总结', '总结反思', 1, NULL, 1, '2021-09-11 11:28:15', '2021-09-11 11:28:15');
INSERT INTO `biz_category` VALUES (6, 0, '工具资源', '开发工具、服务端工具、中间件', 20, NULL, 1, '2021-09-14 15:26:39', '2021-09-14 15:28:28');

-- ----------------------------
-- Records of biz_link
-- ----------------------------
INSERT INTO `biz_link`(`id`, `name`, `url`, `description`, `img`, `email`, `qq`, `status`, `origin`, `remark`, `create_time`, `update_time`) VALUES (1, 'CSDN', 'https://tarzan.blog.csdn.net', '洛阳泰山博客', 'https://profile.csdnimg.cn/2/A/B/3_weixin_40986713', '1334512682@qq.com', '1334512682', 1, 1, '', '2021-09-13 23:24:25', '2021-06-28 10:51:18');

-- ----------------------------
-- Records of biz_love
-- ----------------------------
INSERT INTO `biz_love` VALUES (1, 1, 1, NULL, '0:0:0:0:0:0:0:1', 1, '2021-09-13 23:31:53', '2021-09-13 23:31:53');

-- ----------------------------
-- Records of biz_site_info
-- ----------------------------
INSERT INTO `biz_site_info` VALUES (1, 'TARZAN-CMS', '热爱编程，热爱学习，喜欢分享，少走弯路。', 'https://tse1-mm.cn.bing.net/th/id/OIP.Ups1Z8igjNjLuDfO38XhTgHaHa?pid=Api&rs=1', '2021-07-22 22:13:26', '2021-07-22 22:13:29');

-- ----------------------------
-- Records of biz_tags
-- ----------------------------
INSERT INTO `biz_tags` VALUES (1, 'Linux', '111', '2021-01-14 21:35:31', '2021-07-25 18:53:34');
INSERT INTO `biz_tags` VALUES (2, 'Java', '222', '2021-01-14 21:35:45', '2021-07-25 18:53:38');
INSERT INTO `biz_tags` VALUES (3, 'Spring', NULL, '2021-01-14 21:35:52', '2021-01-14 21:35:52');
INSERT INTO `biz_tags` VALUES (4, 'Spring Boot', NULL, '2021-01-14 21:36:01', '2021-01-14 21:36:01');
INSERT INTO `biz_tags` VALUES (5, '其他', '其他', '2021-07-25 18:55:06', '2021-07-25 18:55:06');
INSERT INTO `biz_tags` VALUES (6, 'IntelliJ IDEA', 'IntelliJ IDEA', '2021-09-14 15:20:27', '2021-09-14 15:20:27');
INSERT INTO `biz_tags` VALUES (7, '奇闻趣事', '奇闻趣事', '2021-09-14 15:30:40', '2021-09-14 15:30:40');
INSERT INTO `biz_tags` VALUES (8, '工具', '工具', '2021-09-14 15:30:48', '2021-09-14 15:30:48');
INSERT INTO `biz_tags` VALUES (9, '资源', '资源', '2021-09-14 15:30:56', '2021-09-14 15:30:56');
INSERT INTO `biz_tags` VALUES (10, 'JavaScript', 'JavaScript', '2021-09-14 15:31:36', '2021-09-14 15:31:36');

-- ----------------------------
-- Records of biz_theme
-- ----------------------------
INSERT INTO `biz_theme` VALUES (1, 'default', '默认主题', '', 1, '2021-09-19 15:50:45', '2021-09-19 15:50:45');

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG', '{\"type\":4,\"qiniuDomain\":\"http://xxx.com\",\"qiniuPrefix\":\"img/blog\",\"qiniuAccessKey\":\"xxxAccessKey\",\"qiniuSecretKey\":\"xxxSecretKey\",\"qiniuBucketName\":\"blog\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\",\"qcloudRegion\":\"\"}', 1, '云存储配置信息');
INSERT INTO `sys_config` VALUES (5, 'SITE_NAME', '洛阳泰山', 1, '网站名称');
INSERT INTO `sys_config` VALUES (6, 'SITE_KWD', 'Java JavaScript Spring SpringBoot Vue React', 1, '网站关键字');
INSERT INTO `sys_config` VALUES (7, 'SITE_DESC', '个人博客网站，技术交流，经验分享。', 1, '网站描述');
INSERT INTO `sys_config` VALUES (8, 'SITE_PERSON_PIC', 'https://tse1-mm.cn.bing.net/th/id/OIP.Ups1Z8igjNjLuDfO38XhTgHaHa?pid=Api&rs=1', 1, '站长头像');
INSERT INTO `sys_config` VALUES (9, 'SITE_PERSON_NAME', '洛阳泰山', 1, '站长名称');
INSERT INTO `sys_config` VALUES (10, 'SITE_PERSON_DESC', '90后少年，热爱写bug，热爱编程，热爱学习，分享一些个人经验，共同学习，少走弯路。Talk is cheap, show me the code!', 1, '站长描述');
INSERT INTO `sys_config` VALUES (11, 'BAIDU_PUSH_URL', 'http://data.zz.baidu.com/urls?site=#&token=EjnfUGGJ1drKk4Oy', 1, '百度推送地址');

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '工作台', '工作台', '/workdest', 'workdest', 0, 1, 1, 'fas fa-home', 1, '2017-09-27 21:22:02', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (2, '权限管理', '权限管理', NULL, NULL, 0, 0, 4, 'fas fa-user-cog', 1, '2017-07-13 15:04:42', '2021-04-19 19:09:22');
INSERT INTO `sys_menu` VALUES (3, '用户管理', '用户管理', '/users', 'users', 2, 1, 1, 'fas fa-chess-queen', 1, '2017-07-13 15:05:47', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (4, '列表查询', '用户列表查询', '/user/list', 'user:list', 3, 2, 0, NULL, 1, '2017-07-13 15:09:24', '2017-10-09 05:38:29');
INSERT INTO `sys_menu` VALUES (5, '新增', '新增用户', '/user/add', 'user:add', 3, 2, 0, NULL, 1, '2017-07-13 15:06:50', '2021-02-28 17:58:46');
INSERT INTO `sys_menu` VALUES (6, '编辑', '编辑用户', '/user/edit', 'user:edit', 3, 2, 0, NULL, 1, '2017-07-13 15:08:03', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (7, '删除', '删除用户', '/user/delete', 'user:delete', 3, 2, 0, NULL, 1, '2017-07-13 15:08:42', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (8, '批量删除', '批量删除用户', '/user/batch/delete', 'user:batchDelete', 3, 2, 0, '', 1, '2021-07-11 01:53:09', '2021-07-11 01:53:09');
INSERT INTO `sys_menu` VALUES (9, '分配角色', '分配角色', '/user/assign/role', 'user:assignRole', 3, 2, 0, NULL, 1, '2017-07-13 15:09:24', '2017-10-09 05:38:29');
INSERT INTO `sys_menu` VALUES (10, '角色管理', '角色管理', '/roles', 'roles', 2, 1, 2, 'fas fa-chess-queen', 1, '2017-07-17 14:39:09', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (11, '列表查询', '角色列表查询', '/role/list', 'role:list', 10, 2, 0, NULL, 1, '2017-10-10 15:31:36', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (12, '新增', '新增角色', '/role/add', 'role:add', 10, 2, 0, NULL, 1, '2017-07-17 14:39:46', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (13, '编辑', '编辑角色', '/role/edit', 'role:edit', 10, 2, 0, NULL, 1, '2017-07-17 14:40:15', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (14, '删除', '删除角色', '/role/delete', 'role:delete', 10, 2, 0, NULL, 1, '2017-07-17 14:40:57', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (15, '批量删除', '批量删除角色', '/role/batch/delete', 'role:batchDelete', 10, 2, 0, '', 1, '2021-07-10 22:20:43', '2021-07-10 22:20:43');
INSERT INTO `sys_menu` VALUES (16, '分配权限', '分配权限', '/role/assign/Menu', 'role:assignPerms', 10, 2, 0, NULL, 1, '2017-09-26 07:33:05', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (17, '资源管理', '资源管理', '/menus', 'menus', 2, 1, 3, 'fas fa-chess-queen', 1, '2017-09-26 07:33:51', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (18, '列表查询', '资源列表', '/menu/list', 'menu:list', 17, 2, 0, NULL, 1, '2021-07-12 16:25:28', '2021-07-12 16:25:33');
INSERT INTO `sys_menu` VALUES (19, '新增', '新增资源', '/menu/add', 'menu:add', 17, 2, 0, NULL, 1, '2017-09-26 08:06:58', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (20, '编辑', '编辑资源', '/menu/edit', 'menu:edit', 17, 2, 0, NULL, 1, '2017-09-27 21:29:04', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (21, '删除', '删除资源', '/menu/delete', 'menu:delete', 17, 2, 0, NULL, 1, '2017-09-27 21:29:50', '2021-02-27 10:53:14');
INSERT INTO `sys_menu` VALUES (22, '运维管理', '运维管理', NULL, NULL, 0, 0, 7, 'fas fa-people-carry', 1, '2021-07-06 15:19:26', '2021-04-19 19:09:59');
INSERT INTO `sys_menu` VALUES (23, '数据监控', '数据监控', '/database/monitoring', 'database', 22, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');
INSERT INTO `sys_menu` VALUES (24, '系统管理', '系统管理', NULL, NULL, 0, 0, 5, 'fas fa-cog', 1, '2021-07-06 15:20:38', '2021-04-19 19:08:58');
INSERT INTO `sys_menu` VALUES (25, '在线用户', '在线用户', '/online/users', 'onlineUsers', 24, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:21:00', '2021-07-24 14:58:22');
INSERT INTO `sys_menu` VALUES (32, '在线用户查询', '在线用户查询', '/online/user/list', 'onlineUser:list', 25, 2, 0, '', 1, '2021-07-24 15:02:23', '2021-07-24 15:02:23');
INSERT INTO `sys_menu` VALUES (33, '踢出用户', '踢出用户', '/online/user/kickOut', 'onlineUser:kickOut', 25, 2, 0, '', 1, '2021-07-24 15:03:16', '2021-07-24 15:03:16');
INSERT INTO `sys_menu` VALUES (34, '批量踢出', '批量踢出', '/online/user/batch/kickOut', 'onlineUser:batchKickout', 25, 2, 0, '', 1, '2021-07-24 15:04:09', '2021-07-24 15:04:09');
INSERT INTO `sys_menu` VALUES (35, '网站管理', '网站管理', NULL, NULL, 0, 0, 3, 'fas fa-columns', 1, '2021-07-24 15:44:23', '2021-04-19 19:08:46');
INSERT INTO `sys_menu` VALUES (36, '基础信息', '基础设置', '/siteInfo', 'siteInfo', 35, 1, 1, 'fas fa-chess-queen', 1, '2021-07-24 15:48:13', '2021-07-24 17:43:39');
INSERT INTO `sys_menu` VALUES (37, '保存', '基础设置-保存', '/siteInfo/save', 'siteInfo:save', 36, 2, 0, '', 1, '2021-07-24 15:49:12', '2021-07-24 15:49:12');
INSERT INTO `sys_menu` VALUES (38, '系统公告', '系统公告', '/notifies', 'notifies', 35, 1, 2, 'fas fa-chess-queen', 0, '2021-07-24 23:40:45', '2021-09-13 12:34:18');
INSERT INTO `sys_menu` VALUES (39, '查询', '系统公告-查询', '/notify/list', 'notify:list', 38, 2, 0, '', 0, '2021-07-24 23:41:30', '2021-09-13 12:33:19');
INSERT INTO `sys_menu` VALUES (40, '新增', '系统公告-新增', '/notify/add', 'notify:add', 38, 2, 0, '', 0, '2021-07-24 23:42:20', '2021-09-13 12:33:26');
INSERT INTO `sys_menu` VALUES (42, '编辑', '系统公告-编辑', '/notify/edit', 'notify:edit', 38, 2, 0, '', 0, '2021-07-24 23:44:39', '2021-09-13 12:33:52');
INSERT INTO `sys_menu` VALUES (43, '删除', '系统公告-删除', '/notify/delete', 'notify:delete', 38, 2, 0, '', 0, '2021-07-24 23:45:27', '2021-09-13 12:33:57');
INSERT INTO `sys_menu` VALUES (44, '批量删除', '批量删除公告', '/notify/batch/delete', 'notify:batchDelete', 38, 2, 0, '', 0, '2021-07-24 23:46:25', '2021-09-13 12:34:02');
INSERT INTO `sys_menu` VALUES (45, '友链管理', '友情链接', '/links', 'links', 35, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 11:05:49', '2021-07-25 11:07:03');
INSERT INTO `sys_menu` VALUES (46, '查询', '友链-查询', '/link/list', 'link:list', 45, 2, 0, '', 1, '2021-07-25 11:06:44', '2021-07-25 11:06:44');
INSERT INTO `sys_menu` VALUES (47, '新增', '友链-新增', '/link/add', 'link:add', 45, 2, 0, '', 1, '2021-07-25 11:07:46', '2021-07-25 11:07:46');
INSERT INTO `sys_menu` VALUES (48, '编辑', '友链-编辑', '/link/edit', 'link:edit', 45, 2, 0, '', 1, '2021-07-25 11:08:21', '2021-07-25 11:08:21');
INSERT INTO `sys_menu` VALUES (49, '删除', '友链-删除', '/link/delete', 'link:delete', 45, 2, 0, '', 1, '2021-07-25 11:08:53', '2021-07-25 11:08:53');
INSERT INTO `sys_menu` VALUES (50, '批量删除', '友链-批量删除', '/link/batch/delete', 'link:batchDelete', 45, 2, 0, '', 1, '2021-07-25 11:09:40', '2021-07-25 11:09:40');
INSERT INTO `sys_menu` VALUES (51, '审核', '友链-审核', '/link/audit', 'link:audit', 45, 2, 0, '', 1, '2021-07-25 11:42:28', '2021-07-25 11:42:28');
INSERT INTO `sys_menu` VALUES (52, '文章管理', '文章管理', NULL, NULL, 0, 0, 2, 'fas fa-newspaper', 1, '2021-07-25 17:43:12', '2021-04-19 19:04:49');
INSERT INTO `sys_menu` VALUES (53, '分类管理', '分类管理', '/categories', 'categories', 52, 1, 3, 'fas fa-chess-queen', 1, '2021-07-25 17:43:50', '2021-04-19 20:33:27');
INSERT INTO `sys_menu` VALUES (54, '新增', '新增分类', '/category/add', 'category:add', 53, 2, 0, '', 1, '2021-07-25 17:44:28', '2021-07-25 17:44:28');
INSERT INTO `sys_menu` VALUES (55, '编辑', '编辑分类', '/category/edit', 'category:edit', 53, 2, 0, '', 1, '2021-07-25 17:44:52', '2021-07-25 17:44:52');
INSERT INTO `sys_menu` VALUES (56, '删除', '删除分类', '/category/delete', 'category:delete', 53, 2, 0, '', 1, '2021-07-25 17:45:28', '2021-07-25 17:45:28');
INSERT INTO `sys_menu` VALUES (58, '查询', '分类查询', '/category/list', 'category:list', 53, 2, 0, '', 1, '2021-07-25 17:49:43', '2021-07-25 17:49:43');
INSERT INTO `sys_menu` VALUES (59, '标签管理', '标签管理', '/tags', 'tags', 52, 1, 4, 'fas fa-chess-queen', 1, '2021-07-25 18:50:47', '2021-04-19 20:33:35');
INSERT INTO `sys_menu` VALUES (60, '查询', '查询标签列表', '/tag/list', 'tag:list', 59, 2, 0, '', 1, '2021-07-25 18:51:20', '2021-07-25 18:51:20');
INSERT INTO `sys_menu` VALUES (61, '新增', '新增标签', '/tag/add', 'tag:add', 59, 2, 0, '', 1, '2021-07-25 18:51:42', '2021-07-25 18:51:42');
INSERT INTO `sys_menu` VALUES (62, '编辑', '编辑标签', '/tag/edit', 'tag:edit', 59, 2, 0, '', 1, '2021-07-25 18:52:17', '2021-07-25 18:52:17');
INSERT INTO `sys_menu` VALUES (63, '删除', '删除标签', '/tag/delete', 'tag:delete', 59, 2, 0, '', 1, '2021-07-25 18:52:40', '2021-07-25 18:52:40');
INSERT INTO `sys_menu` VALUES (64, '批量删除', '批量删除标签', '/tag/batch/delete', 'tag:batchDelete', 59, 2, 0, '', 1, '2021-07-25 18:53:14', '2021-07-25 18:53:14');
INSERT INTO `sys_menu` VALUES (65, '文章列表', '文章列表', '/articles', 'articles', 52, 1, 2, 'fas fa-chess-queen', 1, '2021-07-29 20:20:23', '2021-04-19 19:23:06');
INSERT INTO `sys_menu` VALUES (66, '查询', '查询文章', '/article/list', 'article:list', 65, 2, 0, '', 1, '2021-07-29 20:20:54', '2021-07-29 20:20:54');
INSERT INTO `sys_menu` VALUES (67, '新增', '新增文章', '/article/add', 'article:add', 65, 2, 0, '', 1, '2021-07-29 20:21:21', '2021-07-29 20:21:21');
INSERT INTO `sys_menu` VALUES (68, '编辑', '编辑文章', '/article/edit', 'article:edit', 65, 2, 0, '', 1, '2021-07-29 20:21:50', '2021-07-29 20:21:50');
INSERT INTO `sys_menu` VALUES (69, '删除', '删除文章', '/article/delete', 'article:delete', 65, 2, 0, '', 1, '2021-07-29 20:23:27', '2021-07-29 20:23:27');
INSERT INTO `sys_menu` VALUES (70, '批量删除', '批量删除文章', '/article/batch/delete', 'article:batchDelete', 65, 2, 0, '', 1, '2021-07-29 20:23:49', '2021-07-29 20:23:49');
INSERT INTO `sys_menu` VALUES (71, '发布文章', '写文章', '/article/add', 'article:add', 52, 1, 1, 'fas fa-chess-queen', 1, '2021-07-29 20:39:49', '2021-04-19 19:16:06');
INSERT INTO `sys_menu` VALUES (72, '评论管理', '评论管理', '/comments', 'comments', 35, 1, 4, 'fas fa-chess-queen', 1, '2021-08-10 09:44:41', '2021-09-19 15:44:13');
INSERT INTO `sys_menu` VALUES (73, '查询', '查询', '/comment/list', 'comment:list', 72, 2, 0, '', 1, '2021-08-10 09:46:54', '2021-08-10 09:46:54');
INSERT INTO `sys_menu` VALUES (74, '审核', '审核评论', '/comment/audit', 'comment:audit', 72, 2, 0, '', 1, '2021-08-10 09:57:11', '2021-08-10 09:57:11');
INSERT INTO `sys_menu` VALUES (75, '回复', '回复评论', '/comment/reply', 'comment:audit', 72, 2, 0, '', 1, '2021-08-10 10:04:28', '2021-08-10 10:04:28');
INSERT INTO `sys_menu` VALUES (76, '删除', '删除评论', '/comment/delete', 'comment:delete', 72, 2, 0, '', 1, '2021-08-10 10:06:27', '2021-08-10 10:06:27');
INSERT INTO `sys_menu` VALUES (77, '批量删除', '批量删除评论', '/comment/batch/delete', 'comment:batchDelete', 72, 2, 0, '', 1, '2021-08-10 10:07:57', '2021-08-10 10:07:57');
INSERT INTO `sys_menu` VALUES (78, '上传管理', '上传管理', NULL, NULL, 0, 0, 6, 'fas fa-cloud-upload-alt', 1, '2021-09-12 17:08:41', '2021-04-19 19:09:35');
INSERT INTO `sys_menu` VALUES (79, '云存储配置', '云存储配置', '/attachment/config', 'upload:config', 78, 1, 1, 'fas fa-chess-queen', 1, '2021-09-12 17:10:09', '2021-03-07 14:31:41');
INSERT INTO `sys_menu` VALUES (80, '保存', '保存云存储配置', '/upload/saveConfig', 'upload:saveConfig', 79, 2, 0, '', 1, '2021-09-12 17:10:42', '2021-09-12 17:10:42');
INSERT INTO `sys_menu` VALUES (81, '主题管理', '主题管理', '/themes', 'themes', 35, 1, 2, 'fas fa-chess-queen', 1, '2021-09-19 15:43:50', '2021-09-19 15:43:50');
INSERT INTO `sys_menu` VALUES (82, '查询', '主题列表', '/theme/list', 'theme:list', 81, 2, 0, '', 1, '2021-09-19 15:44:50', '2021-09-19 15:44:50');
INSERT INTO `sys_menu` VALUES (83, '新增', '新增主题', '/theme/add', 'theme:add', 81, 2, 0, '', 1, '2021-09-19 15:45:34', '2021-09-19 15:45:34');
INSERT INTO `sys_menu` VALUES (84, '启用', '启用主题', '/theme/use', 'theme:use', 81, 2, 0, '', 1, '2021-09-19 15:46:28', '2021-09-19 15:46:28');
INSERT INTO `sys_menu` VALUES (85, '删除', '删除主题', '/theme/delete', 'theme:delete', 81, 2, 0, '', 1, '2021-09-19 15:48:06', '2021-09-19 15:48:06');
INSERT INTO `sys_menu` VALUES (86, '批量删除', '批量删除主题', 'theme/batch/delete', 'theme:batchDelete', 81, 2, 0, '', 1, '2021-09-19 15:48:39', '2021-09-19 15:48:39');
INSERT INTO `sys_menu` VALUES (87, '编辑', '编辑主题', '/theme/edit', 'theme:edit', 81, 2, 0, '', 1, '2021-09-19 15:54:34', '2021-09-19 15:54:34');
INSERT INTO `sys_menu` VALUES (88, '批量推送', '批量推送百度', '/article/batch/push', 'article:batchPush', 65, 2, 0, '', 1, '2021-10-28 15:15:00', '2021-10-28 15:15:00');
INSERT INTO `sys_menu` VALUES (89, '登录日志', '登录日志', '/login/logs', 'loginLogs', 22, 1, 2, 'fas fa-th-list', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (90, '数据备份', '数据备份', '/db/backup', 'dbBackup', 22, 1, 3, 'fas fa-clone', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (91, '错误日志', '错误日志', '/error/logs', 'errorLogs', 22, 1, 4, 'fas fa-skull', 1, '2021-07-20 16:01:23', '2021-07-20 16:01:23');
INSERT INTO `sys_menu` VALUES (92, '服务监控', '服务监控', '/server/monitoring', 'database', 22, 1, 1, 'fas fa-chess-queen', 1, '2021-07-06 15:19:55', '2021-07-06 15:19:55');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '超级管理员', 1, '2017-06-28 20:30:05', '2017-06-28 20:30:10');
INSERT INTO `sys_role` VALUES (2, '管理员', '管理员', 1, '2017-06-30 23:35:19', '2017-10-11 09:32:33');
INSERT INTO `sys_role` VALUES (3, '普通用户', '普通用户', 1, '2017-06-30 23:35:44', '2021-07-13 11:44:06');
INSERT INTO `sys_role` VALUES (4, '数据库管理员', '数据库管理员', 1, '2017-07-12 11:50:22', '2017-10-09 17:38:02');

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (1, 1, 1);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (2, 1, 2);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (3, 1, 3);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4, 1, 4);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (5, 1, 5);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (6, 1, 6);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (7, 1, 7);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (8, 1, 8);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (9, 1, 9);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (10, 1, 10);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (11, 1, 11);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (12, 1, 12);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (13, 1, 13);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (14, 1, 14);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (15, 1, 15);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (16, 1, 16);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (17, 1, 17);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (18, 1, 18);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (19, 1, 19);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (20, 1, 20);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (21, 1, 21);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (22, 1, 22);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (23, 1, 23);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (24, 1, 24);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (25, 1, 25);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (26, 1, 32);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (27, 1, 33);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (28, 1, 34);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (29, 1, 35);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (30, 1, 36);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (31, 1, 37);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (32, 1, 38);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (33, 1, 39);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (34, 1, 40);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (35, 1, 42);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (36, 1, 43);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (37, 1, 44);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (38, 1, 45);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (39, 1, 46);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (40, 1, 47);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (41, 1, 48);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (42, 1, 49);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (43, 1, 50);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (44, 1, 51);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (45, 1, 52);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (46, 1, 53);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (47, 1, 54);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (48, 1, 55);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (49, 1, 56);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (50, 1, 58);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (51, 1, 59);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (52, 1, 60);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (53, 1, 61);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (54, 1, 62);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (55, 1, 63);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (56, 1, 64);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (57, 1, 65);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (58, 1, 66);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (59, 1, 67);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (60, 1, 68);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (61, 1, 69);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (62, 1, 70);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (63, 1, 71);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (64, 1, 72);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (65, 1, 73);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (66, 1, 74);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (67, 1, 75);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (68, 1, 76);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (69, 1, 77);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (70, 1, 78);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (71, 1, 79);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (72, 1, 80);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (73, 1, 81);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (74, 1, 82);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (75, 1, 83);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (76, 1, 84);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (77, 1, 85);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (78, 1, 86);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (79, 1, 87);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (80, 1, 88);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (81, 1, 89);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (82, 1, 90);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (83, 1, 91);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (83, 1, 92);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);