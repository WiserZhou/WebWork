<template>
  <div class="mainBox">
    <!-- 内容标题 -->
    <ContentHeader content="课程安排"></ContentHeader>

    <!-- 查询表单 -->
    <div class="query">
      <el-form :inline="true" :model="queryForm" class="demo-form-inline" style="margin-bottom: 10px">
        <el-form-item label="课程名称">
          <el-input v-model="queryForm.name" placeholder="请输入课程名称" style="width: 130px;" clearable/>
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="queryForm.teacher" filterable clearable placeholder="请选择" style="width: 120px;">
            <el-option v-for="item in empList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 批量删除按钮 -->
    <el-button type="primary" @click="handleBatchDelete" style="margin-bottom: 10px;">-&nbsp;批量删除</el-button>

    <!-- 课程信息表格 -->
    <div class="info-table">
      <el-table :data="tableData" style="width: 100%;height: calc(100vh - 330px);" :fit="true"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column property="courseNo" label="课程代码" align="center"/>
        <el-table-column property="courseName" label="课程名称" align="center"/>
        <el-table-column property="classRoom" label="上课地点" align="center"/>
        <el-table-column property="teachers" label="授课教师" align="center"/>
        <el-table-column label="操作" align="center" width="250">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页器 -->
    <div class="foot-pagination">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                     :page-sizes="[10, 20, 50, 100]" layout="slot, sizes, prev, pager, next, jumper" :total="total"
                     @size-change="handleSizeChange" @current-change="handleCurrentChange">
        <template #default>
          <span class="el-pagination__total">共{{ total }}条数据</span>
        </template>
        <template #jumper>
          <span>前往{{ jumper }}页</span>
        </template>
      </el-pagination>
    </div>

    <!-- 批量删除对话框 -->
    <el-dialog v-model="dialogBatchVisible" width="500">
      <ContentHeader content="批量删除课程"></ContentHeader>
      <div class="info-message">您确定要批量删除这些信息吗 ? 如果删除,相应的课程也将删除。</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="confirmDelete(1)">确定</el-button>
          <el-button @click="dialogBatchVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 删除课程对话框 -->
    <el-dialog v-model="dialogVisible" width="500">
      <ContentHeader content="删除课程"></ContentHeader>
      <div class="info-message">您确定要删除该信息吗 ? 如果删除,相应的课程也将删除。</div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="confirmDelete(0)">确定</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 编辑课程对话框 -->
    <el-dialog v-model="dialogFormVisible" width="500">
      <ContentHeader content="编辑"></ContentHeader>
      <el-form :model="form" style="margin-top: 15px;" ref="ruleFormRef" :rules="rules">
        <el-form-item label="* 课程代码" :label-width="formWidth">
          <el-input v-model="form.courseNo" placeholder="请输入课程代码" :disabled="true" style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="* 课程名称" :label-width="formWidth">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" :disabled="true" style="width: 280px;"/>
        </el-form-item>
        <el-form-item label="授课教师" :label-width="formWidth">
          <el-select v-model="form.teachersId" filterable clearable multiple placeholder="请选择" style="width: 280px;"
                     @change="test()">
            <el-option v-for="item in empList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button @click="dialogFormVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import ContentHeader from '../../components/ContentHeader.vue';
import apiAxios from '@/api/ApiAxios.js';
import {ElMessage} from 'element-plus';
import {reactive, ref} from 'vue';

// 翻页器变量
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(1);

// 授课教师下拉框选项
const empList = ref([]);
const empMap = {};

// 获取授课教师列表
async function getEmpList() {
  try {
    const res = await apiAxios({
      url: '/emp/list',
      method: 'get',
    });
    empList.value = res.data.data.map(item => {
      empMap[item.id] = item.name;
      return {label: item.name, value: item.id};
    });
  } catch (err) {
    console.log(err.message);
  }
}

getEmpList();

// 查询表单数据
const queryForm = reactive({
  name: "",
  arranged: null,
  teacher: null,
});

// 课程信息表格数据
const tableData = ref([]);

// 获取课程列表
async function getCourseList() {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      name: queryForm.name,
      arranged: queryForm.arranged,
      teacher: queryForm.teacher
    };
    const res = await apiAxios({
      url: '/courseTeachers',
      method: 'get',
      params
    });
    if (res.data.code === 200) {
      console.log(res.data)
      tableData.value = res.data.data.rows.map(row => {
        if (row.teacherIds) {
          row.teachersId = row.teacherIds.split(",").map(Number);
          row.teachers = row.teachersId.map(id => empMap[id]);
        } else {
          row.teachers = [];
        }
        return row;
      });
      total.value = res.data.data.total;
    } else {
      ElMessage.error(res.data.msg);
    }
  } catch (err) {
    console.log(err.message);
  }
}

getCourseList();

// 批量删除对话框可见性
const dialogBatchVisible = ref(false);

// 删除课程对话框可见性
const dialogVisible = ref(false);

// 编辑课程对话框可见性
const dialogFormVisible = ref(false);

// 编辑课程表单数据
const form = ref({});

// 保存编辑的课程信息
function handleSave() {
  apiAxios({
    url: "/courseTeachers",
    method: 'post',
    data: {
      courseId: form.value.courseId,
      teachersId: form.value.teachersId
    }
  }).then(res => {
    if (res.data.code === 200) {
      console.log(res.data)
      dialogFormVisible.value = false;
      ElMessage({
        message: '保存成功！',
        type: 'success',
      });
      getCourseList();
    } else {
      ElMessage.error(res.data.msg);
    }
  }).catch(err => {
    console.log(err.message);
  });
}

// 查询课程列表
const onSubmit = () => {
  getCourseList();
};

// 批量删除课程
function handleBatchDelete() {
  dialogBatchVisible.value = true;
}

// 处理选中课程的变化
const multipleSelection = ref([]);
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

// 处理编辑课程
const handleEdit = (index, row) => {
  console.log(row)
  form.value = row;
  dialogFormVisible.value = true;
};

// 删除课程
const temp_id = ref(0);
const handleDelete = (index, row) => {
  console.log(row)
  temp_id.value = row.courseId;
  dialogVisible.value = true;
};

// 确认删除课程
function confirmDelete(type) {
  // console.log(ids)
  let ids = "";
  if (type) {
    multipleSelection.value.forEach(x => {
      if (ids === "") ids = x.id;
      else ids += "," + x.id;
    });
  } else {
    ids = temp_id.value;
  }
  console.log(ids)
  console.log(temp_id.value)
  apiAxios({
    url: "/course/" + ids,
    method: "delete",
  }).then(res => {
    if (res.data.code === 200) {
      ElMessage({message: '删除成功！', type: 'success'});
      getCourseList();
      dialogVisible.value = false;
      dialogBatchVisible.value = false;
    }
  });
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  getCourseList();
};

// 处理当前页码变化
const handleCurrentChange = (val) => {
  getCourseList();
};

// 编辑课程对话框中的测试函数
function test() {
  console.log(form.value.teachersId);
}

</script>

<style scoped>
.mainBox {
  position: relative;
  width: 100%;
  height: 100%;
}

.info-message {
  font-size: 18px;
  margin: 20px 20px 10px;
}

.query {
  height: 60px;
  margin-bottom: 20px;
}

.demo-form-inline {
  display: flex;
  padding-top: 20px;
}

.info-table {
  margin-top: 20px;
}

.foot-pagination {
  position: absolute;
  bottom: 5px;
  right: 10px;
}
</style>
