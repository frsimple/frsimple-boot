import { defineStore } from 'pinia';
import { RouteRecordRaw } from 'vue-router';
import router, { allRoutes } from '@/router';
import { store } from '@/store';
import { getCurUserMenu } from '@/api/system/auth';
import Layout from '@/layouts';

const modules = import.meta.glob('../../pages/**');
const filterAsyncRouter = (routers) => {
  // 遍历后台传来的路由字符串，转换为组件对象

  return routers.filter((router) => {
    delete router.id;
    delete router.parentId;
    if (router.component) {
      if (router.component === 'layout') {
        // layout组件特殊处理
        router.component = Layout;
      } else if (router.component === 'link') {
        router.component = null;
      } else if (router.meta && router.meta.single === true) {
        const com = router.component;
        router.children = [];
        router.children.push({
          path: '',
          name: router.name,
          component: modules[`../../${com}.vue`],
        });
        // delete router.path
        router.component = Layout;
      } else {
        delete router.redirect;
        router.name = router.path;
        router.component = modules[`../../${router.component}.vue`];
      }
    }
    if (router.children && router.children.length && !router.meta.single) {
      router.children = filterAsyncRouter(router.children);
    }
    return true;
  });
};
export const usePermissionStore = defineStore('permission', {
  state: () => ({
    whiteListRouters: ['/login'],
    routers: [],
    removeRoutes: [],
  }),
  actions: {
    async initRoutes() {
      let accessedRouters = [];
      const res = await getCurUserMenu();
      const dyRouters = await filterAsyncRouter(res.data);
      accessedRouters = [...allRoutes, ...dyRouters];
      // 全部删除
      router.options.routes.forEach((item: RouteRecordRaw) => {
        if (router.hasRoute(item.name)) {
          router.removeRoute(item.name);
        }
      });
      this.routers = dyRouters;
      accessedRouters.forEach((row) => {
        if (!router.hasRoute(row.name)) {
          router.addRoute(row);
        }
      });
      if (!router.hasRoute('404Page')) {
        router.addRoute({
          path: '/:catchAll(.*)',
          name: '404Page',
          redirect: '/result/404',
        });
      }
    },
    async restore() {
      this.removeRoutes.forEach((item: RouteRecordRaw) => {
        router.addRoute(item);
      });
    },
  },
});

export function getPermissionStore() {
  return usePermissionStore(store);
}
