package com.example.tutorplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor  // 补充无参构造函数
@AllArgsConstructor // 补充全参构造函数
@TableName("tutor_order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 显式指定数据库字段名，确保与 tutor_order 表中的 demand_id 一致
    @TableField("demand_id")
    private Long demandId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("parent_id")
    private Long parentId;

    /**
     * 订单状态：0-待确认 1-已完成
     */
    private Integer status = 0;

    /**
     * 创建时间：插入时自动填充
     * 注意：没有写 MetaObjectHandler，则直接赋予默认值 LocalDateTime.now()
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime = LocalDateTime.now();

    @TableField("complete_time")
    private LocalDateTime completeTime;
}