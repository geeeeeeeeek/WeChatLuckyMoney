# WeChat Red Envelopes [中文版本](https://github.com/geeeeeeeeek/WeChatLuckyMoney/tree/stable)

This Android app helps you snatch red envelopes in WeChat group chat. When a red envelope is detected, the service automatically clicks on it, faster than any mankind.

You are now on the [**dev branch**](https://github.com/geeeeeeeeek/WeChatLuckyMoney/blob/dev/README_EN.md). It contains some of the experimental improvements, which may lead to instability. If you wish to have a ready-to-use version, please switch to [**stable branch**](https://github.com/geeeeeeeeek/WeChatLuckyMoney/blob/stable/README_EN.md).

> **Note:** The app on the stable branch only snatches the latest red envelope. According to our test, the stable version is capable of snatching every single red envelope.

The following doc is targeted at **dev branch**.

## Expected Features

1. Snatch every single red envelope on your screen, while similar apps only snatch the latest one.
2. Skip opened red envelopes intelligently, to avoid frequent clicks.
3. Red envelope logs (switched off by default), to review your lucky money.
4. Performance optimization. You can keep it running in the background, and hardly feel its running. 
5. The code is for educational use only. There are detailed comments, docs that suitable for reading.

## Implementation

[The document on dev branch](https://github.com/geeeeeeeeek/WeChatLuckyMoney/blob/dev/README.md) covers every single detail of the implementation. However, it's in Chinese and I'm not gonna do the translation.

## License

The project was heavily inspired by an [app](https://github.com/XiaoMi/LuckyMoneyTool) demonstrated on XiaoMi's news conference for its phone product this autumn. The stable branch is developed after this, while the dev branch rewrites nearly all the code. The package name remains unchanged as `com.miui.hongbao`.

**Note that:** This app might alter the natural way of interactions with WeChat. Thus the code is restricted to educational use only. You are *not* allowed to use it for other purposes. You must understand and be fully responsiable of the potential risks of using the app, including "being forbidden to use WeChat Lucky Money functionality", etc. 