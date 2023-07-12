package com.imooc.controller;

import com.com.imooc.enums.YesOrNo;
import com.imooc.base.BaseInfoPropertise;
import com.imooc.bo.VlogBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.VlogService;
import com.imooc.utils.PagedGridResult;
import com.imooc.vo.IndexVlogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@Api(tags = "VlogController 短视屏相关业务的接口")
@RestController
@RequestMapping("vlog")
public class VlogController extends BaseInfoPropertise {

    @Autowired
    private VlogService vlogService;
    //url: serverUrl + "/vlog/publish",
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO){
        // 作业 校验VlogBO
        vlogService.createVlog(vlogBO);
        return GraceJSONResult.ok() ;
    }

    @GetMapping("indexList")
    @ApiOperation(value = "indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }

        // 作业 校验VlogBO
        PagedGridResult gridResult = vlogService.getIndexVlogList(userId, search, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("detail")
    @ApiOperation(value = "detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                   @RequestParam String vlogId){

        IndexVlogVO vlogVO = vlogService.getVlogDetailById(userId,vlogId);
        return GraceJSONResult.ok(vlogVO);
    }

    @PostMapping("changeToPrivate")
    @ApiOperation(value = "changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam String userId,
                                           @RequestParam String vlogId){
        vlogService.changeToPrivateOrPublic(userId,vlogId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    @PostMapping("changeToPublic")
    @ApiOperation(value = "changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam String userId,
                                           @RequestParam String vlogId){
        vlogService.changeToPrivateOrPublic(userId,vlogId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    @GetMapping("myPublicList")
    @ApiOperation(value = "myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                   @RequestParam Integer page,
                                   @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                                                                page,
                                                                pageSize,
                                                                YesOrNo.NO.type);
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("myPrivateList")
    @ApiOperation(value = "myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = vlogService.queryMyVlogList(userId,
                page,
                pageSize,
                YesOrNo.YES.type);
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("myLikedList")
    @ApiOperation(value = "myLikedList")
    public GraceJSONResult myLikedList(@RequestParam String userId,
                                         @RequestParam Integer page,
                                         @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = vlogService.getMyLikedVlogList(userId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }


    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        // 我点赞的视频，关联关系保存到数据库
        vlogService.userLikeVlog(userId,vlogId);
        // 点赞后，视频和视频发布者的获赞都会累加1  230408078MBPAPBC
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        // 我点赞的视频，需要在redis中保存关联关系
        redis.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId,"1");
        return GraceJSONResult.ok();
    }

    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        // 我取消的视频，关联关系保存到数据库
        vlogService.userUnLikeVlog(userId,vlogId);
        // 取消后，视频和视频发布者的获赞都会累减少1
        if(redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId).equalsIgnoreCase("0") && redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId).equalsIgnoreCase("0")){
            redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
            redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        }
        // 我取消点赞的视频，删除关联关系
        redis.del(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);
        return GraceJSONResult.ok();
    }

    @PostMapping("totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId){
        return GraceJSONResult.ok(vlogService.getVlogBeLikedCounts(vlogId));
    }

    @GetMapping("followList")
    @ApiOperation(value = "followList")
    public GraceJSONResult followList(@RequestParam String myId,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = vlogService.getMyFollowVlogList(myId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @GetMapping("friendList")
    @ApiOperation(value = "friendList")
    public GraceJSONResult friendList(@RequestParam String myId,
                                      @RequestParam Integer page,
                                      @RequestParam Integer pageSize){

        if(page == null){
            page = BaseInfoPropertise.COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = BaseInfoPropertise.COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = vlogService.getMyFriendVlogList(myId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }
}
