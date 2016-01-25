# 更新日志

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



**[preview (2015.08.17)](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1-beta1)**

- 对源码进行了少量修改。