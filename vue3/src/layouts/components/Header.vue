<template>
  <div :class="layoutCls">
    <t-head-menu :class="menuCls" :theme="theme" expand-type="popup" :value="active">
      <template #logo>
        <span v-if="showLogo" class="header-logo-container">
          <logo-full class="t-logo" />
        </span>
        <div v-else class="header-operate-left">
          <t-button theme="default" shape="square" variant="text" @click="changeCollapsed">
            <t-icon v-if="isSidebarCompact" class="collapsed-icon" name="menu-fold" />
            <t-icon v-if="!isSidebarCompact" class="collapsed-icon" name="menu-unfold" />
          </t-button>
          <layout-breadcrumb v-if="showBreadcrumb" />
          <search :layout="layout" />
        </div>
      </template>
      <menu-content v-show="layout !== 'side'" class="header-menu" :nav-data="menu" />
      <template #operations>
        <div class="operations-container">
          <!-- 搜索框 -->
          <search v-if="layout !== 'side'" :layout="layout" />
          <t-popup placement="left-bottom">
            <t-button theme="primary">
              <template #icon>
                <t-icon name="logo-wecom"></t-icon>
              </template>
              扫码进入交流群</t-button
            >
            <template #content>
              <t-space>
                <div style="display: flex; flex-direction: column; align-items: center">
                  <t-image
                    style="height: 185px; width: 185px"
                    src="https://pengpengyu-test.oss-cn-zhangjiakou.aliyuncs.com/image/wx.jpg"
                  ></t-image>
                  <span>扫码进入微信交流群</span>
                </div>
                <div style="display: flex; flex-direction: column; align-items: center">
                  <t-image
                    style="height: 185px; width: 185px"
                    src="https://pengpengyu-test.oss-cn-zhangjiakou.aliyuncs.com/image/qq.jpg"
                  ></t-image>
                  <span>扫码进入QQ交流群</span>
                </div>
              </t-space>
            </template>
          </t-popup>
          <!-- 全局通知 -->
          <t-tooltip placement="bottom" content="系统设置">
            <t-button theme="default" shape="square" variant="text">
              <t-icon name="setting" @click="toggleSettingPanel" />
            </t-button>
          </t-tooltip>
          <!-- <t-popup class="placement bottom center" placement="bottom" show-arrow destroy-on-close>
            <template #content>
              <div style="display: flex; flex-direction: column; text-align: center; color: red">
                <span>联系作者</span>
                <img
                  style="width: 200px; heigth: 200px"
                  src="https://pengpengyu-test.oss-cn-zhangjiakou.aliyuncs.com/image/3101656383387_.pic.jpg"
                />
              </div>
            </template>
            <t-button theme="default" shape="square" variant="text">
              <t-icon name="help-circle" />
            </t-button>
          </t-popup> -->
          <notice />
          <!-- <t-tooltip placement="bottom" content="代码仓库">
            <t-button theme="default" shape="square" variant="text" @click="navToGitHub">
              <t-icon name="logo-github" />
            </t-button>
          </t-tooltip> -->
          <t-dropdown :min-column-width="135" trigger="click">
            <template #dropdown>
              <t-dropdown-menu>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleNav('/user/center')">
                  <t-icon name="setting"></t-icon>设置
                </t-dropdown-item>
                <t-dropdown-item class="operations-dropdown-container-item" @click="handleLogout">
                  <t-icon name="poweroff"></t-icon>退出登录
                </t-dropdown-item>
              </t-dropdown-menu>
            </template>
            <t-button class="header-user-btn" theme="default" variant="text">
              <div class="header-user-account">
                {{ curUser && curUser.nickName ? curUser.nickName : '游客' }}
                <t-icon name="chevron-down" />
              </div>
            </t-button>
          </t-dropdown>
          <t-avatar v-if="!curUser || !curUser.avatar" style="margin-left: 5px">
            <template #icon>
              <t-icon name="user-circle" />
            </template>
          </t-avatar>
          <t-avatar v-else style="margin-left: 5px" :image="curUser.avatar" />
        </div>
      </template>
    </t-head-menu>
  </div>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue';
import { useRouter } from 'vue-router';
import { DialogPlugin } from 'tdesign-vue-next';
import { useSettingStore, useUserStore, getPermissionStore, getUserStore } from '@/store';
import { getActive } from '@/router';
import { prefix } from '@/config/global';
import LogoFull from '@/assets/assets-logo-full.svg?component';
import { MenuRoute } from '@/interface';
import LayoutBreadcrumb from './Breadcrumb.vue';

import Notice from './Notice.vue';
import Search from './Search.vue';
import MenuContent from './MenuContent';

const permissionStore1 = getPermissionStore();
const userStore1 = getUserStore();

const userStore = useUserStore();
const curUser = computed(() => {
  return userStore.curUser;
});
console.log(curUser);

const goQQ = () => {
  window.open('http://wpa.qq.com/msgrd?v=3&uin=2827916671&site=qq&menu=yes');
};

const props = defineProps({
  theme: {
    type: String,
    default: '',
  },
  layout: {
    type: String,
    default: 'top',
  },
  showLogo: {
    type: Boolean,
    default: true,
  },
  menu: {
    type: Array as PropType<MenuRoute[]>,
    default: () => [],
  },
  isFixed: {
    type: Boolean,
    default: false,
  },
  isCompact: {
    type: Boolean,
    default: false,
  },
  maxLevel: {
    type: Number,
    default: 3,
  },
});

const router = useRouter();
const settingStore = useSettingStore();
const showBreadcrumb = computed(() => {
  return settingStore.showBreadcrumb;
});
const isSidebarCompact = computed(() => {
  return settingStore.isSidebarCompact;
});

const toggleSettingPanel = () => {
  settingStore.updateConfig({
    showSettingPanel: true,
  });
};

const active = computed(() => getActive());

const layoutCls = computed(() => [`${prefix}-header-layout`]);

const menuCls = computed(() => {
  const { isFixed, layout, isCompact } = props;
  return [
    {
      [`${prefix}-header-menu`]: !isFixed,
      [`${prefix}-header-menu-fixed`]: isFixed,
      [`${prefix}-header-menu-fixed-side`]: layout === 'side' && isFixed,
      [`${prefix}-header-menu-fixed-side-compact`]: layout === 'side' && isFixed && isCompact,
    },
  ];
});

const changeCollapsed = () => {
  settingStore.updateConfig({
    isSidebarCompact: !settingStore.isSidebarCompact,
  });
};

const handleNav = (url) => {
  router.push(url);
};

const handleLogout = () => {
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: '是否确认退出系统?',
    confirmBtn: '立即退出',
    // cancelBtn: '暂不',
    onConfirm: ({ e }) => {
      confirmDia.hide();
      userStore1.logout();
      permissionStore1.restore();
      // router.push(`/login?redirect=${router.currentRoute.value.fullPath}`);
      router.push('/login');
    },
    onClose: ({ e, trigger }) => {
      confirmDia.hide();
    },
  });
};

const navToGitHub = () => {
  window.open('https://github.com/tencent/tdesign-vue-next-starter');
};

const navToHelper = () => {
  window.open('http://tdesign.tencent.com/starter/docs/get-started');
};
</script>
<style lang="less" scoped>
@import '@/style/variables.less';
.@{prefix}-header {
  &-layout {
    height: 64px;
  }

  &-menu-fixed {
    position: fixed;
    top: 0;
    z-index: 1001;

    &-side {
      left: 237px;
      right: 0;
      z-index: 100;
      width: auto;
      transition: all 0.3s;
      &-compact {
        left: 69px;
      }
    }
  }

  &-logo-container {
    cursor: pointer;
    display: inline-flex;
    height: 64px;
  }
}
.header-menu {
  flex: 1 1 1;
  display: inline-flex;

  :deep(.t-menu__item) {
    min-width: unset;
    padding: 0px 16px;
  }
}

.operations-container {
  display: flex;
  align-items: center;
  margin-right: 12px;

  .t-popup__reference {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .t-button {
    margin: 0 8px;
    &.header-user-btn {
      margin: 0;
    }
  }

  .t-icon {
    font-size: 20px;
    &.general {
      margin-right: 16px;
    }
  }
}

.header-operate-left {
  display: flex;
  margin-left: 20px;
  align-items: normal;
  line-height: 0;

  .collapsed-icon {
    font-size: 24px;
  }
}

.header-logo-container {
  width: 184px;
  height: 26px;
  display: flex;
  margin-left: 24px;
  color: var(--tdvns-text-color-primary);

  .t-logo {
    width: 100%;
    height: 100%;
    &:hover {
      cursor: pointer;
    }
  }

  &:hover {
    cursor: pointer;
  }
}

.header-user-account {
  display: inline-flex;
  align-items: center;
  color: var(--tdvns-text-color-primary);
  .t-icon {
    margin-left: 4px;
    font-size: 16px;
  }
}

:deep(.t-head-menu__inner) {
  //border-bottom: 1px solid var(--tdvns-border-level-1-color);
}

.t-menu--light {
  .header-user-account {
    color: var(--tdvns-text-color-primary);
  }
}
.t-menu--dark {
  // .t-head-menu__inner {
  //   border-bottom: 1px solid var(--tdvns-gray-color-10);
  // }
  .header-user-account {
    color: rgba(255, 255, 255, 0.55);
  }
  .t-button {
    --ripple-color: var(--tdvns-gray-color-10) !important;
    &:hover {
      background: var(--tdvns-gray-color-12) !important;
    }
  }
}

.operations-dropdown-container-item {
  width: 100%;
  display: flex;
  align-items: center;

  .t-icon {
    margin-right: 8px;
  }

  :deep(.t-dropdown__item) {
    .t-dropdown__item__content {
      display: flex;
      justify-content: center;
    }
    .t-dropdown__item__content__text {
      display: flex;
      align-items: center;
      font-size: 14px;
    }
  }

  :deep(.t-dropdown__item) {
    width: 100%;
    margin-bottom: 0px;
  }
  &:last-child {
    :deep(.t-dropdown__item) {
      margin-bottom: 8px;
    }
  }
}
</style>
