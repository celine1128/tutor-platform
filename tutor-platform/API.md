# 接口文档

## 基础信息
请求地址: http://localhost:8080/api
数据格式: JSON

## 1. 用户注册

URL: /user/register
方法: POST

参数:
- phone: 手机号 (必填)
- password: 密码 (必填)
- role: STUDENT或TEACHER (必填)
- nickname: 昵称 (必填)

请求示例:
{"phone":"13800138000","password":"123456","role":"STUDENT","nickname":"张三"}

响应示例:
成功: "注册成功！"
失败: "注册失败，手机号已存在！"

前端调用:
axios.post('http://localhost:8080/api/user/register', {
    phone: '13800138000',
    password: '123456',
    role: 'STUDENT',
    nickname: '张三'
})

## 2. 用户登录

URL: /user/login
方法: POST

参数:
- phone: 手机号 (必填)
- password: 密码 (必填)

请求示例:
{"phone":"13800138000","password":"123456"}

响应示例 (成功):
{"success":true,"message":"登录成功！","userId":1,"phone":"13800138000","role":"STUDENT","nickname":"张三"}

响应示例 (失败):
{"success":false,"message":"登录失败，手机号或密码错误！"}

前端调用:
axios.post('http://localhost:8080/api/user/login', {
    phone: '13800138000',
    password: '123456'
}).then(res => {
    if(res.data.success) {
        localStorage.setItem('user', JSON.stringify(res.data))
    }
})

## 3. 测试接口

URL: /hello
方法: GET
响应: "Hello, 项目正常工作！"

前端调用:
axios.get('http://localhost:8080/hello')