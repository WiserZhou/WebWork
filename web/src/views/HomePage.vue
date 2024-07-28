<template>
  <!-- 主体布局 -->
  <el-container class="common-layout">
    <!-- 头部 -->
    <el-header class="header">
      <!-- 修改密码按钮 -->
      <div class="button" @click="changePassword">
        <el-icon>
          <EditPen/>
        </el-icon>
        <span>修改密码</span>
      </div>
      <!-- 退出登录按钮 -->
      <div class="button" @click="logout">
        <el-icon>
          <SwitchButton/>
        </el-icon>
        <span>退出登录</span>
      </div>
    </el-header>
    <!-- 内容区域 -->
    <el-container class="container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="aside">
        <el-col class="aside-content">
          <!-- 垂直菜单 -->
          <el-menu class="vertical-menu" default-active="1">
            <!-- 班级学员管理 -->
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <UserFilled/>
                </el-icon>
                <span>班级学员管理</span>
              </template>
              <!-- 班级管理 -->
              <el-menu-item index="1-1" @click="goto('/classManage')">班级管理</el-menu-item>
              <!-- 学员管理 -->
              <el-menu-item index="1-2" @click="goto('/studentManage')">学员管理</el-menu-item>
            </el-sub-menu>
            <!-- 系统信息管理 -->
            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <Tools/>
                </el-icon>
                <span>系统信息管理</span>
              </template>
              <!-- 部门管理 -->
              <el-menu-item index="2-1" @click="goto('/departmentManage')">部门管理</el-menu-item>
              <!-- 员工管理 -->
              <el-menu-item index="2-2" @click="goto('/employeeManage')">员工管理</el-menu-item>
            </el-sub-menu>
            <!-- 数据统计管理 -->
            <el-sub-menu index="3">
              <template #title>
                <el-icon>
                  <TrendCharts/>
                </el-icon>
                <span>数据统计管理</span>
              </template>
              <!-- 员工信息统计 -->
              <el-menu-item index="3-1" @click="goto('/empInfoManage')">员工信息统计</el-menu-item>
              <!-- 学员信息统计 -->
              <el-menu-item index="3-2" @click="goto('/stuInfoManage')">学员信息统计</el-menu-item>
            </el-sub-menu>
            <!-- 课程教务管理 -->
            <el-sub-menu index="4">
              <template #title>
                <el-icon>
                  <Management/>
                </el-icon>
                <span>课程教务管理</span>
              </template>
              <!-- 课程管理 -->
              <el-menu-item index="4-1" @click="goto('/curriculumManage')">课程管理</el-menu-item>
              <!-- 课程安排 -->
              <el-menu-item index="4-2" @click="goto('/curriculumArrange')">课程安排</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-col>
      </el-aside>
      <!-- 主要内容 -->
      <el-main class="main-content">
        <!-- 路由视图 -->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import {
  UserFilled,
  Tools,
  TrendCharts,
  Management,
  SwitchButton,
  EditPen
} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'

const router = useRouter()

// 跳转至指定路由
function goto(path) {
  router.push(path)
}

// 修改密码
function changePassword() {
  // 在此处实现修改密码的逻辑
}

// 退出登录
function logout() {
  // 清除本地存储的用户信息等操作
  localStorage.removeItem('token')
  // 跳转至登录页面
  router.push('/')
}
</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
  width: 100vw;
  height: 100vh;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background: linear-gradient(135deg, #4db7b7 0%, #0797ba 100%);
}

.button {
  display: flex;
  align-items: center;
  font-size: 1.2rem;
  cursor: pointer;
  margin-left: 15px;
}

.button:hover {
  color: #0b6acc;
}

.aside {
  background-color: rgba(217, 232, 229, 0.2);
}

.vertical-menu {
  font-size: 1.3rem;
}

.main-content {
  flex: 1;
}

.button p {
  margin: 30px;
}
</style>
