package com.imooc.service;
import com.imooc.bo.VlogBO;
import com.imooc.pojo.Vlog;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;
import org.springframework.stereotype.Repository;


@Repository
public interface VlogService {

    /**
     * 新增vlog视屏
     * @param vlogBO
     * @return
     */
    public void createVlog(VlogBO vlogBO);

    /**
     * 查询首页/搜索的vlog列表
     * @param
     * @return
     */
    public PagedGridResult getIndexVlogList(String search ,
                                            String userId ,
                                            Integer page,
                                            Integer pageSize);


    /**
     * 根据视频主键查询vlog
     * @param
     * @return
     */
    public IndexVlogVO getVlogDetailById(String userId,String vlogId);

    // 用户把视频改为公开/私密的视频
    public void changeToPrivateOrPublic(String userId,
                                        String vlogId,
                                        Integer yerOrNo);


    // 查询用户的私密/公开的视频列表
    public PagedGridResult queryMyVlogList(String userId,
                                Integer page,
                                Integer pageSize,
                                Integer yesOrNo);

    // 用户点赞/喜欢视屏
    public void userLikeVlog(String userId,String vlogId);

    public void userUnLikeVlog(String userId,String vlogId);

    // 获得用户点赞视频的总数
    public Integer getVlogBeLikedCounts(String vlogId);

    // 查询用户点赞过的短视频
    public PagedGridResult getMyLikedVlogList(String userId ,
                                   Integer page,
                                   Integer pageSize);

    // 查询用户关注的博主的视频
    public PagedGridResult getMyFollowVlogList(String myId ,
                                               Integer page,
                                               Integer pageSize);


    // 查询朋友关注的短视频列表
    public PagedGridResult getMyFriendVlogList(String myId ,
                                               Integer page,
                                               Integer pageSize);

    // 根据主键查询vlog
    public Vlog getvlog(String id);
}
