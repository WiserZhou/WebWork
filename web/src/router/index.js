import {createRouter, createWebHistory} from 'vue-router';
import SignUpView from '@/views/SignUpView.vue';
import HomePage from '@/views/HomePage.vue';
import ClassManage from '@/views/ClassStudentManage/ClassManage.vue';
import StudentManage from '@/views/ClassStudentManage/StudentManage.vue';
import DepartmentManage from '@/views/SysInfoManage/DepartmentManage.vue';
import EmployeeManage from '@/views/SysInfoManage/EmployeeManage.vue';
import EmpInfoManage from '@/views/DataStatisticsManage/EmpInfoManage.vue';
import StuInfoManage from '@/views/DataStatisticsManage/StuInfoManage.vue';
import CurriculumManage from '@/views/CurriculumAcademicManage/CurriculumManage.vue';
import CurriculumArrange from '@/views/CurriculumAcademicManage/CurriculumArrange.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: SignUpView
        },
        {
            path: '/homePage',
            name: 'homePage',
            component: HomePage,
            children: [
                {
                    path: '/classManage',
                    component: ClassManage,
                },
                {
                    path: '/studentManage',
                    component: StudentManage,
                },
                {
                    path: '/departmentManage',
                    component: DepartmentManage,
                },
                {
                    path: '/employeeManage',
                    component: EmployeeManage,
                },
                {
                    path: '/empInfoManage',
                    component: EmpInfoManage,
                },
                {
                    path: '/stuInfoManage',
                    component: StuInfoManage,
                },
                {
                    path: '/curriculumManage',
                    component: CurriculumManage,
                },
                {
                    path: '/curriculumArrange',
                    component: CurriculumArrange,
                }
            ]
        },
    ]
})

export default router
