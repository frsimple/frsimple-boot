import axios from 'axios';
import { MessagePlugin } from 'tdesign-vue-next';
import proxy from '../config/proxy';
import router from '@/router';
import { useUserStore, getUserStore } from '@/store';
import { TOKEN_NAME } from '@/config/global';

declare module 'axios' {
  interface AxiosInstance {
    (config: AxiosRequestConfig): Promise<any>;
  }
}

const env = import.meta.env.MODE || 'development';

const host = env === 'development' ? '' : proxy[env].host;
// const host = env === 'mock' ? '/' : proxy[env].host; // 如果是mock模式 就不配置host 会走本地Mock拦截

const CODE = {
  LOGIN_TIMEOUT: 10000,
  REQUEST_SUCCESS: 0,
  REQUEST_FOBID: 1001,
};

const instance = axios.create({
  baseURL: host,
  timeout: 10000,
  withCredentials: true,
});

instance.interceptors.request.use((config) => {
  if (
    (config.method === 'post' || config.method === 'delete') &&
    config.url !== '/auth/oauth/token' &&
    env === 'release' &&
    config.url.indexOf('/plan') == -1
  ) {
    MessagePlugin.warning('演示环境，禁止操作');
    return;
  }
  const userStore = getUserStore();
  const { token } = userStore;
  const isToken = (config.headers || {})['isAuth'] === false;
  if (!isToken && token) {
    config.headers[TOKEN_NAME] = `${token}`;
  }
  return config;
});

instance.defaults.timeout = 10000;

instance.interceptors.response.use(
  (response) => {
    if (response.status < 200 || response.status > 300) {
      // MessagePlugin.error('系统错误')
      return Promise.reject('error');
    }
    if (response.data.code === 403 || response.data.code === 401) {
      MessagePlugin.error('请求未被授权');
    }
    return response.data;
  },
  (err) => {
    const { config } = err;
    if (err.response && err.response.status === 401) {
      if (config.url !== '/auth/oauth/token') {
        const userStrore = useUserStore();
        //userStrore.logout();
        router.push({ path: '/login' });
      }
      return Promise.reject(err.response);
    }

    if (err.response && err.response.status === 403) {
      MessagePlugin.error('无权访问');
      return Promise.reject(err.response);
    }
    if (err.response && err.response.status === 500) {
      MessagePlugin.error('系统错误');
      return Promise.reject(err.response);
    }
    if (err.response && err.response.status === 404) {
      MessagePlugin.error('访问的资源不存在');
      return Promise.reject(err.response);
    }

    if (!config || !config.retry) return Promise.reject(err);

    config.retryCount = config.retryCount || 0;

    if (config.retryCount >= config.retry) {
      return Promise.reject(err);
    }

    config.retryCount += 1;

    const backoff = new Promise((resolve) => {
      setTimeout(() => {
        resolve(null);
      }, config.retryDelay || 1);
    });

    return backoff.then(() => instance(config));
  },
);

export default instance;
