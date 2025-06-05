package com.wolfhouse.journeydaily.common.constant;


/**
 * @author linexsong
 */
public class JourneyConstant {
    public static final char[] REMOVE_CONTENT = {' ', '\n'};

    public static final Integer VISIBILITY_PUBLIC = 1;
    public static final Integer VISIBILITY_PRIVATE = 0;

    public static final String POST_FAILED = "发布失败";
    public static final String JOURNEY_NOT_EXIST = "日记不存在";
    public static final String UPDATE_FAILED = "日记更新失败";
    public static final String JOURNEY_DELETED_FAILED = "日记删除失败";
    public static final String JOURNEY_RECOVERY_FAILED = "日记恢复失败";
    public static final String JOURNEY_DRAFT_FAILED = "日记暂存失败";
    public static final String JOURNEY_DRAFT_CANNOT_REMOVE = "无法移除日记暂存";
    public static final String JOURNEY_DRAFT_NOT_EXISTS = "无暂存日记";
    public static final String JOURNEY_DRAFT_EXISTS = "存在未处理的暂存日记";
    public static final String PARTITION_TITLE_FIELD = "title";
    public static final String PARTITION_ADD_FAILED = "分区添加失败";
    public static final String PARTITION_UPDATE_FAILED = "分区更新失败";
    public static final String PARTITION_NOT_EXIST = "分区不存在";
    public static final String PARTITION_PARENT_NOT_EXIST = "父级分区不存在";
    public static final String PARTITION_PARENT_CANNOT_BE_SELF = "父级分区不得为自身";
    public static final String PARTITION_TITLE_ALREADY_EXIST = "分区标题已存在";
    public static final String PARTITION_CIRCLED = "分区存在循环";
    public static final String PARTITION_DELETE_FAILED = "分区删除失败";

    // ES

    public static final String ORDER_FIELD_NOT_ALLOWED = "排序参数不被接受";


    // 字段常量

    public static final String POST_TIME = "postTime";
    public static final String POST_TIME_DB = "post_time";
    public static final String EDIT_TIME = "editTime";
    public static final String EDIT_TIME_DB = "edit_time";
    public static final String JOURNEY_ID = "journeyId";
    public static final String JOURNEY_ID_DB = "journey_id";
    public static final String AUTHOR_ID = "authorId";
    public static final String AUTHOR_ID_DB = "author_id";
    public static final String AUTHOR = "author";
    public static final String SUMMARY = "summary";
    public static final String VISITABLE = "visibility";
    public static final String PARTITION = "partition";
    public static final String PARTITION_ID = "partitionId";
    public static final String PARTITION_ID_DB = "partition_id";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";
}
