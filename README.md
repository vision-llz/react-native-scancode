# react-native-scancode

## Getting started

`$ npm install react-native-scancode --save`

### Mostly automatic installation

`$ react-native link react-native-scancode`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`

- Add `import com.xiaomo.ScancodePackage;` to the imports at the top of the file
- Add `new ScancodePackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:

   ```
   include ':react-native-scancode'
   project(':react-native-scancode').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-scancode/android')
   ```

3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
   ```
   compile project(':react-native-scancode')
   ```

## Usage

```javascript
// TODO: What to do with the module?
import { NativeModules } from "react-native";
const { WmsScanCode } = NativeModules;

//设置扫码选项(当PDA设备能通过广播进行设置的时候可用)
let obj={};
let list=[];
obj['key']='barcode_send_mode';
obj['value']='BROADCAST';
list.push(obj);
WmsScanCode.setBroadcastSetting('com.android.scanner.service_settings',list);

//广播和接收字段(接收的广播名和接收的字段名)
WmsScanCode.getCode('android.intent.action.SCANRESULT','value');

//接收扫描结果
import { DeviceEventEmitter } from "react-native";
componentWillMount() {
  if (this.searchListener) {
    this.searchListener.remove();
  }
  this.searchListener = DeviceEventEmitter.addListener("scannerCodeShow",e => {
    console.log(e.code)
    }
  )
}
//退出页面移除监听
componentWillUnmount() {
  if (this.searchListener) {
    DeviceEventEmitter.removeListener("scannerCodeShow");
    this.searchListener.remove();
    this.searchListener = null;
  }
}


```
