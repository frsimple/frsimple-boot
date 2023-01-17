import { defineComponent, PropType, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { prefix } from '@/config/global';
import pgk from '../../../package.json';
import MenuContent from './MenuContent';
import tLogo from '@/assets/assets-t-logo.svg?component';
import tLogoFull from '@/assets/assets-logo-full.svg?component';
import { useSettingStore } from '@/store';
import { getActive } from '@/router';

const MIN_POINT = 992 - 1;

const useComputed = (props) => {
  const collapsed = computed(() => useSettingStore().isSidebarCompact);

  const active = computed(() => getActive());

  //计算打开的菜单
  const openMenu = computed(() => {
    return active.value.substring(0, active.value.lastIndexOf('/'));
  });

  const sideNavCls = computed(() => {
    const { isCompact } = props;
    return [
      `${prefix}-sidebar-layout`,
      {
        [`${prefix}-sidebar-compact`]: isCompact,
      },
    ];
  });

  const menuCls = computed(() => {
    const { showLogo, isFixed, layout } = props;
    return [
      'sp-left-menu',
      `${prefix}-side-nav`,
      {
        [`${prefix}-side-nav-no-logo`]: !showLogo,
        [`${prefix}-side-nav-no-fixed`]: !isFixed,
        [`${prefix}-side-nav-mix-fixed`]: layout === 'mix' && isFixed,
      },
    ];
  });

  const layoutCls = computed(() => {
    const { layout } = props;
    return [`${prefix}-side-nav-${layout}`, `${prefix}-sidebar-layout`];
  });

  return {
    active,
    openMenu,
    collapsed,
    sideNavCls,
    menuCls,
    layoutCls,
  };
};

export default defineComponent({
  name: 'SideNav',
  components: {
    tLogoFull,
    tLogo,
  },
  props: {
    menu: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    showLogo: {
      type: Boolean as PropType<boolean>,
      default: true,
    },
    isFixed: {
      type: Boolean as PropType<boolean>,
      default: true,
    },
    layout: {
      type: String as PropType<string>,
      default: '',
    },
    headerHeight: {
      type: String as PropType<string>,
      default: '64px',
    },
    theme: {
      type: String as PropType<string>,
      default: 'light',
    },
    isCompact: {
      type: Boolean as PropType<boolean>,
      default: false,
    },
  },
  setup(props) {
    const router = useRouter();
    const settingStore = useSettingStore();

    const changeCollapsed = () => {
      settingStore.updateConfig({
        isSidebarCompact: !settingStore.isSidebarCompact,
      });
    };

    const autoCollapsed = () => {
      const isCompact = window.innerWidth <= MIN_POINT;
      settingStore.updateConfig({
        isSidebarCompact: isCompact,
      });
    };

    onMounted(() => {
      autoCollapsed();
      window.onresize = () => {
        autoCollapsed();
      };
    });

    const goHome = () => {
      router.push('/');
    };

    return {
      prefix,
      ...useComputed(props),
      autoCollapsed,
      changeCollapsed,
      goHome,
    };
  },
  render() {
    return (
      <div class={this.sideNavCls}>
        <t-menu
          class={this.menuCls}
          theme={this.theme}
          value={this.active}
          defaultExpanded={[this.openMenu]}
          collapsed={this.collapsed}
          v-slots={{
            logo: () =>
              this.showLogo && (
                <span class={`${prefix}-side-nav-logo-wrapper`} onClick={this.goHome}>
                  {this.collapsed ? (
                    <tLogo class={`${prefix}-side-nav-logo-t-logo`} />
                  ) : (
                    <div style="display:flex;flex-direction: row;align-items: center;">
                      <tLogo class={`${prefix}-side-nav-logo-t-logo1`} />
                      <h2 class={`${prefix}-side-nav-logo-tdesign-logo1`}>FrSimpleBoot</h2>
                    </div>
                  )}
                </span>
              ),
            operations: () => !this.collapsed && <span class="version-container">FrSimpleBoot v1.0.0</span>,
          }}
        >
          <MenuContent navData={this.menu} />
        </t-menu>
        <div class={`${prefix}-side-nav-placeholder${this.collapsed ? '-hidden' : ''}`}></div>
      </div>
    );
  },
});
