# 家教平台 API 接口文档

## 基础信息
基础地址: http://localhost:8080/api
请求格式: JSON
响应格式: JSON

## 1. 用户注册

地址: POST /user/register

参数:
- phone: 手机号 (必填)
- password: 密码 (必填)
- role: STUDENT或TEACHER (必填)
- nickname: 昵称 (必填)

请求示例:
{"phone":"13800138000","password":"123456","role":"STUDENT","nickname":"张三"}

响应成功: "注册成功！"
响应失败: "注册失败，手机号已存在！"

前端代码:
axios.post('http://localhost:8080/api/user/register', {
    phone: '13800138000',
    password: '123456',
    role: 'STUDENT',
    nickname: '张三'
})

## 2. 用户登录

地址: POST /user/login

参数:
- phone: 手机号 (必填)
- password: 密码 (必填)

请求示例:
{"phone":"13800138000","password":"123456"}

响应成功:
{"success":true,"message":"登录成功！","userId":1,"phone":"13800138000","role":"STUDENT","nickname":"张三"}

响应失败:
{"success":false,"message":"登录失败，手机号或密码错误！"}

前端代码:
axios.post('http://localhost:8080/api/user/login', {
    phone: '13800138000',
    password: '123456'
}).then(res => {
    if(res.data.success) {
        localStorage.setItem('user', JSON.stringify(res.data))
    }
})

## 3. 获取当前用户 (需要登录)

地址: GET /user/currentUser

响应已登录:
{"success":true,"userId":1,"phone":"13800138000","role":"STUDENT","nickname":"张三"}

响应未登录:
{"success":false,"message":"请先登录"}

前端代码:
axios.get('http://localhost:8080/api/user/currentUser', {
    withCredentials: true
}).then(res => {
    if(res.data.success) {
        console.log('当前用户:', res.data)
    }
})

## 4. 退出登录 (需要登录)

地址: POST /user/logout

响应:
{"success":true,"message":"退出成功！"}

前端代码:
axios.post('http://localhost:8080/api/user/logout', {}, {
    withCredentials: true
}).then(res => {
    if(res.data.success) {
        localStorage.removeItem('user')
    }
})

## 5. 测试接口

地址: GET /hello
响应: "Hello, 项目正常工作！"

## 注意事项

1. 前端请求需要设置 withCredentials: true 来携带 Session
2. 密码用 BCrypt 加密，安全
3. 端口默认 8080，如有变动请修改