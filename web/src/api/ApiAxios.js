import axios from 'axios';
import {useRouter} from 'vue-router';

// 定义axios请求函数
export default function apiAxios(axiosConfig) {
    // 使用useRouter获取路由实例
    const router = useRouter();
    // 创建axios实例
    const service = axios.create({
        baseURL: 'http://127.0.0.1:8080', // 基础URL
        timeout: 10000, // 请求超时时间
    });

    // 请求拦截器
    service.interceptors.request.use(
        // 请求发送前的处理
        (config) => {
            // 获取本地存储中的token
            const token = localStorage.getItem('token');
            // 如果存在token，则在请求头中添加token字段
            if (token) {
                config.headers['token'] = token;
            }
            return config;
        },
        // 请求发送失败时的处理
        (error) => {
            return Promise.reject(error);
        }
    );

    // 响应拦截器
    service.interceptors.response.use(
        // 响应成功时的处理
        (response) => {
            // 如果响应中包含状态码和数据，并且状态码为0且消息为"NOT_LOGIN"
            if (response.status && response.data && response.data.code === 0 && response.data.msg === "NOT_LOGIN") {
                // 重定向到登录页
                router.push("/");
            }
            return response;
        },
        // 响应失败时的处理
        (error) => {
            return Promise.reject(error);
        }
    );

    // 返回axios请求的promise对象
    return service(axiosConfig);
}
