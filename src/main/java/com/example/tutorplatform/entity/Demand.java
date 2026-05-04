package com.example.tutorplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("demand")
public class Demand {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String subject;
    private String grade;
    private Integer price;
    private String description;
    private Integer status;  // 0-待接单 1-已接单
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}