# 更新日志

**[v3.0 (2016.08.02)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v3.0.1)**

- 优化 重新设计UI，风格更加简洁明快。提升“社区”和“设置”的视觉优先级，开启插件更顺手。
- 新增 Uber免费乘车优惠。
- 新增 适配微信6.3.22。
- 新增 Android N (牛轧糖) 适配（预计v3.1完成）
- 修复 Bugly高优先级的问题。

**[v2.3 (2016.02.07)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v2.3)**

- 优化 不打开拜年红包
- 紧急修复了Bugly上几个高优先级的问题，减少Crash出现

**[v2.2 (2016.02.04)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v2.2)**

- 新增 延时拆开红包的可选项
- 新增 跳转至浏览器打开的入口
- 新增 接入腾讯Bugly
- 新增 自动回复的可选项(实验中)
- 优化 手动拆开的红包不返回
- 优化 适配三星等坑爹机型 (感谢 [@firesunCN](https://github.com/firesunCN) 对这项修改的贡献)
- 修复 拆开红包后的多次返回问题


**[v2.1 (2016.01.30)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v2.1.1)**
- 新增 屏蔽指定红包文字的可选项
- 新增 息屏抢红包的可选项
- 新增 不拆开自己发的红包的可选项
- 新增 内置红包攻略
- 新增 所有链接默认用内置WebView打开
- 优化 仅在WiFi环境下回到应用时检测更新
- 优化 抢红包的逻辑流程，减少了一些误判情形
- 优化 适配了Lollipop以上版本的透明Status Bar
- 修复 插件开启后按钮仍然显示“开启插件”
- 修复 监听SharedPreferences变化无效的问题 (感谢 @act262 对这项修改的贡献)
- 修复 在其他界面会做额外的操作的问题 (感谢 @sxyy 对这项修改的贡献)
- 修复 响应Notification进入聊天后，可能误判不抢的问题 (感谢 @tttony3 对这项修改的贡献)

**[v2.0.1 (2016.01.23)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v2.0.1)**

- 修复了用户第一次使用，偏好设置未加载导致的插件失效。 Fix issue #50, #51.



**[v2.0 (2016.01.23)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v2.0)** 

- 更自由的监视选项. Give user the freedom to choose what to watch, chat/list/notifications available at choices.

- 更优化的重复红包判定. Optimize algorithm for duplicate hongbao detection.

- 自动更新机制. Add auto update module.

- 更新UI设计、应用图标. Update UI and app icon.

- 增加辅助服务的说明. Add description for accessibility service.

- 增加了反馈issue的快捷方式. Add quick link to Github issues.

- 替换包名. Substitute the original package name.

详细的release notes请见https://github.com/geeeeeeeeek/WeChatLuckyMoney/issues/48。


**[v1.4 (2016.01.16)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.4)** 

- 修复了微信新版(6.3.9.48_refecd3e)红包UI调整导致的功能失效。Fix failure caused by new WeChat Hongbao UI. Related issue #41.


**[v1.3 (2015.12.29)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.3)** 

- 修复了红包频繁点击的bug。Fix repeatedly opening envelopes. Related issue #27 .

- 并入了从通知栏进入抢红包的代码(#28)，充分测试后加入下一个版本。Add watch for WeChat notification, from which enter the chat activity. This feature will not be available in this version, until well tested.

**[v1.2 (2015.12.17)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.2)** 

该版本的红包识别代码存在Bug，会出现频繁点击，建议跳过该版本。

- 增加了微信语言 英语 的支持，修复了由此导致的一系列问题. Add support for the English language. Fix related issues.

- 修改了红包重复判断的逻辑，戳“名称与缓存不一致的红包”和“名称一致且间隔大于5秒”的红包。Change the logic of detecting duplicate red envelopes. Only those name unmatched with cached or those name matched but with a duration > 5 secs will be touched.


**[v1.1 (2015.10.28)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.1)**

- 服务运行时防止息屏。Keep screen on when service is running.

**[v1.0 (2015.10.07)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.0)**

- 从dev分支并入了新版UI和红包节点的hash算法。

- 增加了红包重复判断，不再不停点击最新的红包，极大优化了性能和体验。


**[Preview (2015.08.17)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1-beta1)**

- 对源码进行了少量修改。