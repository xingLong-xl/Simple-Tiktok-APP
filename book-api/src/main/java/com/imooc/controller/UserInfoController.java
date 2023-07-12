package com.imooc.controller;

import com.com.imooc.enums.FileTypeEnum;
import com.com.imooc.enums.UserInfoModifyType;
import com.imooc.MinIOConfig;
import com.imooc.base.BaseInfoPropertise;
import com.imooc.bo.UpdatedUserBo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.Stu;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.MinIOUtils;
import com.imooc.utils.SMSUtils;
import com.imooc.vo.UsersVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api(tags = "UserInfoController 用户信息接口模块")
@RestController
// url: serverUrl + "/userInfo/query?userId=" + myUserId,
@RequestMapping("userInfo")
public class UserInfoController extends BaseInfoPropertise {

    @Autowired
    private UserService userService;
    @ApiOperation(value = "query的测试路由")
    @GetMapping("query")
    public Object query(@RequestParam String userId) throws Exception {
        Users user = userService.getUser(userId);
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties( user, usersVo);

        // 我的关注博主总数量
        String  myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数
        String  myFansCountsStr = redis.get( REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视屏博主(点赞/喜欢的总和)
       // String  likedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String  likedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer  likedVlogerCounts = 0;
        Integer  likedVlogCounts = 0;
        Integer  myFansCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
       /* if (StringUtils.isNotBlank(likedVlogCountsStr)) {
           likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
        }*/
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVo.setMyFollowsCounts(myFollowsCounts);
        usersVo.setMyFansCounts(myFansCounts);
        usersVo.setTotalLikeMeCounts(totalLikeMeCounts);;
        return GraceJSONResult.ok(usersVo);
    }


    //url: serverUrl + "/userInfo/modifyUserInfo?type=5",
    @ApiOperation(value = "这是一个modifyUserInfo的测试路由")
    @PostMapping("modifyUserInfo")
    public Object modifyUserInfo(@RequestBody UpdatedUserBo updatedUserBo,
                                 @RequestParam Integer type)
            throws Exception {
        UserInfoModifyType.checkUserInfoTypeIsRight(type);
        Users newUserInfo = userService.updateUserInfo(updatedUserBo, type);
        return GraceJSONResult.ok(newUserInfo);
    }

    @Autowired
    private MinIOConfig minIOConfig;
    //url: serverUrl + "/userInfo/modifyImage?userId=" + userId + "&type=1"
    @PostMapping("modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file) throws Exception{

        if (type != FileTypeEnum.BGIMG.type && type != FileTypeEnum.FACE.type) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String filename = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,file.getInputStream());

        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + filename;
        // 修改图片地址到数据库
        UpdatedUserBo updatedUserBo = new UpdatedUserBo();
        updatedUserBo.setId(userId);
        if (type == FileTypeEnum.BGIMG.type) {
            updatedUserBo.setBgImg(imgUrl);
        } else {
            updatedUserBo.setFace(imgUrl);
        }
        Users users = userService.updateUserInfo(updatedUserBo);
        return GraceJSONResult.ok(users);
    }
}
