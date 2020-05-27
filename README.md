# TestShadow 

业务层的宿主和插件
app -> 宿主
commlib -> 公共组件(同平常开发时的公共库) 宿主app和插件都引入
constants -> 常量 宿主app和插件都需要引入 保证某些常量值引用值相等
plugin-manager -> 插件管理器 PluginManager 实现
plugins/agora-app -> agora插件的壳子
plugins/agora-plugin -> agora插件的实现
plugins/agora-lib -> agora插件业务
plugins/loader -> 插件加载器实现
plugins/runtime-plugin -> 插件运行时实现

Shadow源码目录
shadow/sdk
