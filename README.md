#termios.h
在jni文件夹下由一个termios.h的文件夹，是因为在打开串口的时候找不到tcgetattr，所有在android 19包内拷贝出来一份，如果你开发中没有出现这种错误，可以直接删除。
#Mainactivity
sendjni()方法是像串口发送数据
#gradle.properties
android.useDeprecatedNdk=true，要添加这句，不然android studio会出现未使用ndk的错误，不能使用native关键字
