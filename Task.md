## 任务一（20180809）

### 题目管理（付卫）
    
    所有题目都为单选题，每题有4个选项
    
    
## 任务二（20180809）

### 积分规则管理（吴党）

    只对规则进行增删改查
    
    
    ---
 
-----------------------------------------------
 
 ## 第二课堂分析：
 
 ### 科目（Subject）（如：生命科学基础篇）
 
| 字段名称 | 字段类型 | 字段说明 |
| ------ | ------ | ------ |
| name | varchar | 名称 |
| id | int | 主键 |
| orderNo | int | 序号 |
| remark | longtext | 备注 |
 
 ### 章节（Chapter）（如：生命的特征）（与Subject属于多对一）
 
 | 字段名称 | 字段类型 | 字段说明 |
 | ------ | ------ | ------ |
 | name | varchar | 名称 |
 | id | int | 主键 |
 | orderNo | int | 序号 |
 | subjectId | int | 外键，科目ID |
 | subjectName | varchar | 外键，科目名称 |
 | remark | longtext | 备注 |
 
 ### 课程（Lesson）（如：周五 18:30-19:30）（与Subject属于多对一）
 
| 字段名称 | 字段类型 | 字段说明 |
| ------ | ------ | ------ |
| name | varchar | 名称 |
| id | int | 主键 |
| orderNo | int | 序号 |
| subjectId | int | 外键，科目ID |
| subjectName | varchar | 外键，科目名称 |
| remark | longtext | 备注 |

-----------------------------------------------

### 通讯录（Contacts）

`实现增、删、改、查即可`

| 字段名称 | 字段类型 | 字段说明 |
| :------: | :------: | :------: |
| name | varchar | 名称 |
| id | int | 主键 |
| sex | varchar | 性别 |
| depName | varchar | 单位名称 |
| duty | varchar | 职务 |
| phone | varchar | 联系电话 |
| remark | longtext | 备注 |