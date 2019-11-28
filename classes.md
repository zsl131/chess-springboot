微信扫码登陆：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN

# 科普进校园思路

    流程：
    - 学校提供老师信息
    - 为老师创建用户
    - 老师通过微信平台绑定用户
    - 根据老师所教年级查看课程
    - 课程包含：视频、PPT、学习单
    - 记录老师的访问记录（播放记录，下载记录）
    - 老师评论课程（优秀评论可获得现金奖励，累计100元后方可提现）
    

## 学校（School）
| 字段名称 | 字段类型 | 字段说明 |
| :-------: | :------: | :------: |
| name | varchar | 名称 |
| remark | varchar | 说明 |

## 老师（Teacher）
| 字段名称 | 字段类型 | 字段说明 |
| :-------: | :------: | :------: |
| name | varchar | 姓名 |
| phone | varchar | 电话 |
| sex | varchar | 性别，1-男；2-女 |
| openid | varchar | 微信Openid |
| hasBind | varchar | 是否已与微信手机绑定 |
| schoolId | int | 学校Id，外键 |
| schoolName | varchar | 学校名称，外键 |
| remark | varchar | 备注 |

## 课程体系（ClassSystem）
  
  归类课程所属体系，如：人教版、苏教版、定制版
  
  | 字段名称 | 字段类型 | 字段说明 |
  | :-------: | :------: | :------: |
  | name | varchar | 名称 |
  | remark | varchar | 说明 |
  
## 课程体系内容（ClassSystemContent）（与课程多对一）

| 字段名称 | 字段类型 | 字段说明 |
| :-------: | :------: | :------: |
| grade | varchar | 年级 |
| term | varchar | 上下学期 |
| orderNo | int | 序号，所属第几次课 |
| sysId | int | 体系ID，外键 |
| sysName | varchar | 体系名称，外键 |
| courseId | int | 课程ID，外键 |
| courseTitle | varchar | 课程标题，外键 |

  
## 课程（Course）

| 字段名称 | 字段类型 | 字段说明 |
| :-------: | :------: | :------: |
| title | varchar | 标题 |
| grade | varchar | 年级 |
| term | varchar | 上下学期 |
| age | varchar | 适合年龄段 |
| videoPath | varchar | 视频地址 |
| pptPath | varchar | ppt地址 |
| learnPath | varchar | 学习单地址 |
| content | longtext | 课程内容 |
| remark | longtext | 说明 |
