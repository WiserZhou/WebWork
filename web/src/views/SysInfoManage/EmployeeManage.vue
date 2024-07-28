<template>
  <div class="mainBox">
    <!-- 员工管理标题 -->
    <ContentHeader content="员工管理"></ContentHeader>

    <!-- 查询条件 -->
    <div class="query">
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="姓名">
          <!-- 员工姓名输入框 -->
          <el-input v-model="queryForm.name" placeholder="请输入员工姓名" style="width: 150px;" clearable/>
        </el-form-item>
        <el-form-item label="性别">
          <!-- 性别选择框 -->
          <el-select
              v-model="queryForm.gender"
              filterable
              clearable
              placeholder="请选择"
              style="width: 100px;">
            <!-- 性别选项 -->
            <el-option
                v-for="item in genderOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="入职时间">
          <!-- 入职时间选择器 -->
          <el-date-picker
              v-model="queryForm.entryDate"
              type="daterange"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              :default-value="[new Date(), new Date()]"
              value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item>
          <!-- 查询按钮 -->
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 新增员工按钮 -->
    <el-button type="primary" @click="handleAdd">+ 新增员工</el-button>
    <!-- 批量删除按钮 -->
    <el-button type="primary" @click="handleBatchDelete">- 批量删除</el-button>

    <!-- 员工信息表格 -->
    <div class="info-table">
      <el-table :data="tableData" style="width: 100%;height: calc(100vh - 300px);" :fit="true"
                @selection-change="handleSelectionChange">
        <!-- 多选框列 -->
        <el-table-column type="selection" width="55" align="center"/>
        <!-- 姓名列 -->
        <el-table-column property="name" label="姓名" align="center"/>
        <!-- 照片列 -->
        <el-table-column label="照片" align="center" width="250">
          <template #default="scope">
            <img :src="scope.row.image" alt="用户头像"
                 style="width: 100px; height: 100px;">
          </template>
        </el-table-column>
        <!-- 性别列 -->
        <el-table-column property="genderName" label="性别" align="center"/>
        <!-- 职位列 -->
        <el-table-column property="jobName" label="职位" align="center"/>
        <!-- 入职日期列 -->
        <el-table-column property="entryDate" label="入职日期" align="center"/>
        <!-- 最后操作时间列 -->
        <el-table-column property="updateTime" label="最后操作时间" :formatter="dateFormatter" align="center"/>
        <!-- 操作列 -->
        <el-table-column label="操作" align="center" width="250">
          <template #default="scope">
            <!-- 编辑按钮 -->
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <!-- 删除按钮 -->
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 翻页器 -->
    <div class="foot-pagination">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="slot, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        <template #default>
          <!-- 数据总数显示 -->
          <span class="el-pagination__total">共{{ total }}条数据</span>
        </template>
        <template #jumper>
          <!-- 跳转页数提示 -->
          <span>前往{{ jumper }}页</span>
        </template>
      </el-pagination>
    </div>

    <!-- 批量删除确认框 -->
    <el-dialog
        v-model="dialogBatchVisible"
        width="500"
    >
      <ContentHeader content="批量删除员工"></ContentHeader>
      <div class="info-message">您确定要批量删除这些员工的信息吗 ?</div>
      <template #footer>
        <div class="dialog-footer">
          <!-- 确定按钮 -->
          <el-button type="primary" @click="confirmDelete(1)">
            确定
          </el-button>
          <!-- 取消按钮 -->
          <el-button @click="dialogBatchVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 删除确认框 -->
    <el-dialog
        v-model="dialogVisible"
        width="500"
    >
      <ContentHeader content="删除员工"></ContentHeader>
      <div class="info-message">您确定要删除该员工吗 ?</div>
      <template #footer>
        <div class="dialog-footer">
          <!-- 确定按钮 -->
          <el-button type="primary" @click="confirmDelete(0)">
            确定
          </el-button>
          <!-- 取消按钮 -->
          <el-button @click="dialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 表格弹出框 -->
    <el-dialog v-model="dialogFormVisible" width="500">
      <ContentHeader :content="dialogContent"></ContentHeader>
      <!-- 表单内容 -->
      <el-form :model="form" style="margin-top: 15px;" ref="ruleFormRef" :rules="rules">
        <!-- 用户名输入框 -->
        <el-form-item label="用户名" :label-width="formWidth" prop="username">
          <el-input v-model="form.username" :disabled="username_disabled" placeholder="请输入用户名，2-20字符，不可重复"
                    style="width: 280px;"/>
        </el-form-item>
        <!-- 员工姓名输入框 -->
        <el-form-item label="员工姓名" :label-width="formWidth" prop="name">
          <el-input v-model="form.name" placeholder="请输入员工姓名，2-10个字" style="width: 280px;"/>
        </el-form-item>
        <!-- 性别选择框 -->
        <el-form-item label="性别" :label-width="formWidth" prop="gender">
          <el-select
              v-model="form.gender"
              filterable
              clearable
              placeholder="请选择"
              style="width: 280px;"
          >
            <!-- 性别选项 -->
            <el-option
                v-for="item in genderOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 图片上传 -->
        <el-form-item label="图&nbsp;&nbsp;像" :label-width="formWidth">
          <img :src="form.image" alt="用户头像" @click="inputClick" class="img-style">
          <input type="file" name="image" ref="fileInput" @change="handleUpload" v-show="false">
        </el-form-item>
        <!-- 职位选择框 -->
        <el-form-item label="职位" :label-width="formWidth">
          <el-select
              v-model="form.job"
              filterable
              clearable
              placeholder="请选择"
              style="width: 280px;"
          >
            <!-- 职位选项 -->
            <el-option
                v-for="item in jobOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 入职日期选择器 -->
        <el-form-item label="入职日期" :label-width="formWidth">
          <el-date-picker
              v-model="form.entryDate"
              type="date"
              placeholder="请选择开课时间"
              size="default"
              :default="new Date()"
              style="width: 280px;"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <!-- 归属部门选择框 -->
        <el-form-item label="归属部门" :label-width="formWidth">
          <el-select
              v-model="form.deptId"
              filterable
              clearable
              placeholder="请选择"
              style="width: 280px;"
          >
            <!-- 部门选项 -->
            <el-option
                v-for="item in deptOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <!-- 对话框底部按钮 -->
      <template #footer>
        <div class="dialog-footer">
          <!-- 保存按钮 -->
          <el-button type="primary" @click="handleSave(ruleFormRef)">
            保存
          </el-button>
          <!-- 取消按钮 -->
          <el-button @click="dialogFormVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import ContentHeader from '../../components/ContentHeader.vue';
import {reactive, ref} from 'vue';
import {ElMessage} from 'element-plus';
import apiAxios from '@/api/ApiAxios.js';
import moment from 'moment';

const rules = reactive({
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 2, max: 20, message: '用户名长度应为2-20字符', trigger: 'blur'},
    {validator: validateUsername, trigger: 'blur'}
  ],
  name: [
    {required: true, message: '请输入员工姓名', trigger: 'blur'},
    {min: 2, max: 10, message: '姓名长度应为2-10个字', trigger: 'blur'},
    {validator: validateName, trigger: 'blur'}
  ],
  gender: [
    {required: true, message: '请选择性别', trigger: 'change'}
  ],
  entryDate: [
    {required: true, message: '请选择入职日期', trigger: 'blur'}
  ],
  deptId: [
    {required: true, message: '请选择归属部门', trigger: 'change'}
  ],
});

const ruleFormRef = reactive({
  username: '',
  name: '',
  gender: '',
  entryDate: '',
  deptId: ''
})

// 变量声明
const fileInput = ref();
const username_disabled = ref(true);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(1);
const dialogBatchVisible = ref(false);
const dialogVisible = ref(false);
const dialogContent = ref("新增员工");
const form = ref();
const dialogFormVisible = ref(false);
const formWidth = '140px';
const queryForm = reactive({
  name: null,
  gender: null,
  entryDate: [null, null],
});
const tableData = ref([]);

// 获取部门列表
const deptOptions = reactive([]);

async function getDeptList() {
  try {
    const res = await apiAxios({
      url: '/dept',
      method: 'get',
    });
    deptOptions.push(...res.data.data.map(x => ({
      label: x.name,
      value: x.id
    })));
  } catch (err) {
    console.log(err.message);
  }
}

getDeptList();

// 获取员工列表
async function getEmpList() {
  try {
    if (!queryForm.entryDate) {
      queryForm.entryDate = [null, null];
    }
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      name: queryForm.name,
      gender: queryForm.gender,
      begin: queryForm.entryDate[0],
      end: queryForm.entryDate[1],
    };
    const res = await apiAxios({
      url: '/emp',
      method: 'get',
      params
    });
    tableData.value = res.data.data.rows.map(x => ({
      ...x,
      genderName: genderMap[x.gender],
      jobName: jobMap[x.job]
    }));
    total.value = res.data.data.total;
  } catch (err) {
    console.log(err.message);
  }
}

getEmpList();

// 表单验证方法
function validateUsername(rule, value) {
  const pattern = /^[A-Za-z0-9]+$/;
  return pattern.test(value) ? Promise.resolve() : Promise.reject('用户名只能包含数字和字母');
}

function validateName(rule, value) {
  const pattern = /^[\u4e00-\u9fa5]+$/;
  return pattern.test(value) ? Promise.resolve() : Promise.reject('姓名只能包含汉字');
}

// 日期格式化方法
const dateFormatter = (row) => {
  const updateTime = row.updateTime;
  const date = moment(updateTime);
  return date.format("YYYY-MM-DD HH:mm:ss");
};

// 其他变量声明
let genderOptions = [
  {label: "男", value: 1},
  {label: "女", value: 2}
];
let genderMap = {1: "男", 2: "女"};
let jobOptions = [
  {label: "班主任", value: 1},
  {label: "讲师", value: 2},
  {label: "学工主管", value: 3},
  {label: "教研主管", value: 4},
  {label: "咨询师", value: 5}
];
let jobMap = {1: "班主任", 2: "讲师", 3: "学工主管", 4: "教研主管", 5: "咨询师"};

// 查询方法
const onSubmit = () => {
  getEmpList();
};

// 添加员工方法
function handleAdd() {
  form.value = {
    id: null,
    username: "",
    password: "",
    name: "",
    gender: null,
    image: null,
    job: null,
    entryDate: null,
    deptId: null,
    createTime: null,
    updateTime: null
  };
  username_disabled.value = false;
  dialogContent.value = "新增员工";
  dialogFormVisible.value = true;
}

// 批量删除方法
function handleBatchDelete() {
  dialogBatchVisible.value = true;
}

// 选择改变方法
const multipleSelection = ref([]);
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

// 编辑员工方法
const handleEdit = async (index, row) => {
  try {
    form.value = (await apiAxios({
      url: "/emp/" + row.id,
      method: 'get',
    })).data.data;
  } catch (err) {
    console.log(err.message);
  }
  username_disabled.value = true;
  dialogContent.value = "修改员工";
  dialogFormVisible.value = true;
};

// 删除员工方法
const temp_id = ref(0);
const handleDelete = (index, row) => {
  temp_id.value = row.id;
  dialogVisible.value = true;
};


// 分页大小改变方法
const handleSizeChange = (val) => {
  pageSize.value = val;
  getEmpList();
};
// 当前页数改变方法
const handleCurrentChange = (val) => {
  currentPage.value = val;
  getEmpList();
};

// 文件上传点击事件
const inputClick = () => {
  fileInput.value.click();
};

// 处理文件上传方法
function handleUpload() {
  const file = fileInput.value.files[0];
  const allowedFormats = ['image/png', 'image/jpeg', 'image/jpg'];
  if (!allowedFormats.includes(file.type)) {
    ElMessage.error('图片格式错误，上传失败。仅支持PNG、JPG、JPEG格式。');
    return;
  }
  const maxSize = 2 * 1024 * 1024; // 2M
  if (file.size > maxSize) {
    ElMessage.error('图片过大，上传失败。图片大小不能超过2M。');
    return;
  }
  const formData = new FormData();
  formData.append('image', file);
  apiAxios({
    url: '/emp/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(res => {
    form.value.image = res.data.data;
  }).catch(err => {
    console.log(err.message);
  });
}

// 确认删除方法
function confirmDelete(type) {
  let ids = "";
  if (type) {
    multipleSelection.value.forEach(x => {
      if (ids === "") ids = x.id;
      else ids += "," + x.id;
    });
  } else {
    ids = temp_id.value;
  }
  if (ids === '') {
    ElMessage.error("未选择！")
  } else {
    apiAxios({
      url: "/emp/" + ids,
      method: "delete",
    }).then(res => {
      if (res.data.code === 200) {
        ElMessage({
          message: '删除成功！',
          type: 'success',
        });
        getEmpList();
        dialogVisible.value = false;
        dialogBatchVisible.value = false;
      } else {
        ElMessage.error(res.data.msg);
      }
    });
  }


}

async function handleSave(formEl) {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      let method = '';
      if (dialogContent.value === "新增员工") method = 'post';
      else if (dialogContent.value === "修改员工") method = 'put';

      apiAxios({
        url: '/emp',
        method: 'put',
        data: form.value,
      }).then(res => {
        if (res.data.code === 200) {
          ElMessage({
            message: '修改成功！',
            type: 'success',
          });
          getEmpList();
          dialogFormVisible.value = false;
        } else {
          ElMessage.error(res.data.msg);
        }
      })
    } else {
      console.log('error submit!', fields)
    }
  });

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

.img-style {
  width: 150px;
  height: 150px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  cursor: pointer;
}

.query {
  height: 60px;
}

.demo-form-inline {
  display: flex;
  padding-top: 20px;
  margin-left: 20px;
}

.dialog-footer {
  text-align: center;
  margin-top: 20px;
}
</style>
