微信抢红包插件
===================

这个Android插件可以帮助你在微信群聊抢红包时战无不胜。当检测到红包时，插件会自动点击屏幕，和人工点击的速度不可同日而语。

你正在查看的是[**stable分支**](https://github.com/geeeeeeeeek/WeChatLuckyMoney/tree/stable)，前往[Release](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/)下载最新可用版本。根据目前的测试，稳定版本抢到红包的概率为100%。

> **注：** 你还可以切换到[**dev分支**](https://github.com/geeeeeeeeek/WeChatLuckyMoney/tree/dev)，查看更多实验性的修改。dev分支在stable分支的基础上尝试了大量修改和优化，能使用但无法保证稳定性。
  
下面的文档仅针对**stable分支**。


最新版本
-------------
[**v1.0**](https://github.com/geeeeeeeeek/WeChatLuckyMoney/releases/tag/v1.0)

1. 从dev分支并入了新版UI和红包节点的hash算法。

2. 增加了红包重复判断，不再不停点击最新的红包，极大优化了性能和体验。


实现原理
-------------------
[dev分支文档](https://github.com/geeeeeeeeek/WeChatLuckyMoney/blob/dev/README.md)中包含了详细的介绍。


版权说明
-------------------

本项目源自小米今年秋季发布会时演示的抢红包测试[源码](https://github.com/XiaoMi/LuckyMoneyTool)。stable分支基于此代码继续开发，dev分支重写了几乎所有的逻辑代码。应用的包名com.miui.hongbao未变。

由于插件可能会改变自然的微信交互方式，这份代码仅可用于教学目的，不得更改后用于其他用途。

项目使用MIT许可证。在上述前提下，你可以将代码用于任何用途。
