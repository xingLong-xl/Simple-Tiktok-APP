package com.imooc.mapper;
import com.imooc.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VlogMapperCustom{
    /**
     * 查询首页的vlog列表
     * @param map
     * @return
     */
    public List<IndexVlogVO> getIndexVlogList(@Param("paramMap") Map<String,Object> map);

    /**
     * 根据视频主键查询vlog
     * @param map
     * @return
     */

    public List<IndexVlogVO> getVlogDetailById(@Param("paramMap") Map<String,Object> map);

    public List<IndexVlogVO> getMyLikedVlogList(@Param("paramMap") Map<String,Object> map);

    public List<IndexVlogVO> getMyFollowVlogList(@Param("paramMap") Map<String,Object> map);

    public List<IndexVlogVO> getMyFriendVlogList(@Param("paramMap") Map<String,Object> map);

}