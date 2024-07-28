<template>
  <!-- 自定义登录表单 -->
  <div class="custom-wrapper">
    <div class="custom-login-box">
      <h3>培训管理系统</h3>
      <!-- 用户名输入框 -->
      <div class="custom-user-style">
        <span style="margin-right: 10px">用户名</span>
        <el-input v-model="username" style="width: 240px" placeholder="请输入用户名"
                  :class="{'error': isUsernameError}" @input="clearErrors"/>
        <!-- 用户名错误提示 -->
        <div v-if="isUsernameError" class="custom-error-text">用户名不能为空!</div>
      </div>
      <!-- 密码输入框 -->
      <div class="custom-password-style">
        <span style="margin-right: 15px">密码</span>
        <el-input v-model="password" style="width: 240px" type="password" placeholder="请输入密码" show-password
                  :class="{'error': isPasswordError}" @input="clearErrors"/>
        <!-- 密码错误提示 -->
        <div v-if="isPasswordError" class="custom-error-text">密码不能为空!</div>
      </div>
      <!-- 按钮区域 -->
      <div class="custom-button-box">
        <!-- 登录按钮 -->
        <el-button class="el-button" type="primary" round @click="handleLogin" :disabled="isLoggingIn">{{
            isLoggingIn ? '登录中...' : '登录'
          }}
        </el-button>
        <!-- 取消按钮 -->
        <el-button class="el-button" type="info" round @click="handleCancel">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router';
import {ref} from 'vue';
import apiAxios from '@/api/ApiAxios.js';
import {ElMessage} from 'element-plus';

// 初始化路由
const router = useRouter();

// 用户名和密码的响应式变量
const username = ref("");
const password = ref("");

// 用户名和密码是否错误的标志位
const isUsernameError = ref(false);
const isPasswordError = ref(false);

// 是否正在登录的标志位
const isLoggingIn = ref(false);

// 清除错误提示信息
function clearErrors() {
  isUsernameError.value = false;
  isPasswordError.value = false;
}

// 表单验证
function validateForm() {
  clearErrors();
  let isValid = true;
  if (!username.value.trim()) {
    isUsernameError.value = true;
    isValid = false;
  }
  if (!password.value.trim()) {
    isPasswordError.value = true;
    isValid = false;
  }
  return isValid;
}

// 处理登录
function handleLogin() {
  if (validateForm()) {
    isLoggingIn.value = true;
    // 发起登录请求
    apiAxios({
      url: "/login",
      method: "post",
      data: {
        username: username.value,
        password: password.value
      }
    }).then(res => {
      isLoggingIn.value = false;
      if (res.data.code === 200) {
        // 登录成功，存储token并跳转页面
        localStorage.setItem("token", res.data.data);
        ElMessage({
          message: '登录成功！',
          type: 'success',
        });
        router.push('/classManage');
      } else {
        // 登录失败，显示错误信息
        ElMessage.error(res.data.msg);
      }
    }).catch(error => {
      isLoggingIn.value = false;
      console.error('Login error:', error);
    });
  }
}

// 处理取消操作
function handleCancel() {
  // 清空用户名和密码以及错误提示信息
  username.value = "";
  password.value = "";
  clearErrors();
}
</script>

<style scoped>
.custom-wrapper {
  display: flex;
  width: 100vw;
  height: 100vh;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #4db7b7 0%, #0797ba 100%);
}

h3 {
  font: 400 40px 'Lato', sans-serif;
  color: #000000;
  margin-bottom: 40px;
}

.custom-login-box {
  display: flex;
  flex-direction: column;
  width: 520px;
  height: 400px;
  background-color: #fff;
  border: 1px solid rgba(215, 215, 215, 0.5);
  border-radius: 20px;
  justify-content: center;
  align-items: center;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.custom-user-style, .custom-password-style {
  position: relative;
  margin: 10px;
}

.custom-error-text {
  position: absolute;
  top: calc(100% + 5px);
  color: #ff4d4f;
  font-size: 12px;
}

.custom-button-box {
  margin-top: 20px;
  display: flex;
  width: 220px;
  justify-content: space-between;
}

.el-button {
  width: 80px;
  height: 40px;
}

.el-button:disabled {
  opacity: 0.7;
}

.el-button:hover {
  transform: translateY(-2px);
}

.el-input.error input {
  border-color: #ff4d4f !important;
}
</style>
