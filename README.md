# BaseAppApi
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	        compile 'com.github.1053452188:BaseAppApi:d1b41dd9d8'
	}
# 功能介绍
## 1.BasaeActivity
	t(String)   //弹土司
	isExist(ActivityClass) //判断activity是否已经存在
	isActive(ActivityClass) //判断activity是否可见
	keepScreenLight()  //是否保持常亮
## 2.BaseApplication
	在该类中会初始化  
	KLog	//log工具 
	litepal	 //数据库工具
	SPManager //SharedPreferences工具  
## 3.BaseDialog
	自定义dialog可继承该类，方便
## 4.BaseFragment
	t(String)  //弹土司
	setLayoutResId() //抽象方法，返回布局资源id
	initData()	//抽象方法，初始化数据
## 5.网络访问工具 OkHttpUtils 的二次封装
	{
		NetWork.getInstance(context)
			    .post() //请求方法 get/post	
			    .url(url) //请求地址
			    .addParam(key,value) //添加请求参数
			    .build()
			    .execute(NetWorkStringCallBack) //回调函数	
	}
	其他方法
		publicParms(boolean) //是否携带公共参数
		loadingLayout(BaseLoadingLayout) //配合loadinglayout布局使用
		isLog(boolean) //是否打印log(底层使用KLog 工具打印)
		isLogJson(boolean) //是否已json格式打印log 
		isShowExceptionMsg(boolean) //是否toast异常信息
		Tag(Object) //给当前请求任务设置tag
		cancle(tag) //根据设置的tag 取消请求任务
		setPublicParams //静态方法，全局有效
	回调接口NetWorkStringCallBack
		onBefore()//默认空实现
		onError(Call call, Exception e, int id) //请求失败
		onResponse(String json) //请求成功
		onAfter() //默认空实现
## 6.工具类
	AppUtil //打开第三方应用|Activity ,获取apk文件信息
	BluetoothHeadsetUtils	//暂时无用
	DateTimeUtil	//日期时间格式化工具
	DensityUtil	//px,sp,dp 互相转换
	DeviceUtil	//获取设备信息
	FileUtil
	LogUtil
	NetworkUtil
	PermissionUtil //6.0以下权限判断（mic，camera）
	SampleConstants
	SPManager
	StringUtil
## 7.style
	BaseAppTheme 全屏
	PercentLayoutSytle 百分比布局的子空间
## 8.color
	colors_base  常用颜色
## 9.自带依赖dependencies
	{
		compile  'org.litepal.android:core:1.3.2' //数据库依赖，使用时要在自己的app的assets中添加 litepal.xml(详情查看litepal)
		compile  'com.squareup.picasso:picasso:2.5.2' //图片加载工具
		compile 'com.alibaba:fastjson:1.2.38' 
		compile 'com.github.1053452188:LoadingLayout:master-SNAPSHOT' //loadinglayout
		compile 'com.mylhyl:acp:1.1.7' //android 6.0 以上的权限判断
		compile 'com.zhy:percent-support-extends:1.1.1' //百分比布局
	}
	
		
