package com.online.meeting.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import com.online.meeting.R
import com.online.meeting.listener.IDialogCallback
import com.online.meeting.utils.*
import com.online.meeting.widget.TSProgressDialog
import com.online.meeting.widget.TSToolbar
import com.online.meeting.widget.swipe.PageSlidingPaneLayout
import com.online.meeting.widget.TSToolbar.ToolbarClickListener

/**
 * Activity父类
 *
 * @author ding
 * Created by Ding on 2018/8/13.
 */
abstract class BaseActivity : AppCompatActivity(), SlidingPaneLayout.PanelSlideListener, ToolbarClickListener {


    /**
     * 动画时长
     */
    protected var ANIMATION_DURATION = 200

    /**
     * 分解
     */
    private val TRANSITION_EXPLODE = 1

    /**
     * 滑动
     */
    private val TRANSITION_SLIDE = 2

    /**
     * 渐退
     */
    private val TRANSITION_FADE = 3

    protected var mToast: Toast? = null

    protected var mProcessDialog: TSProgressDialog? = null

    protected var mToolbar: TSToolbar? = null

    protected var photoPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        initSwipeBack()

        super.onCreate(savedInstanceState)

        if (isSupportFullScreen()) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        setContentView(R.layout.activity_base)

        // 恢复数据
        restoreData(savedInstanceState)

        init()

        // 转场动画
        if (isSupportTransitionAnimation()) {
            setTransitionAnimation(TRANSITION_SLIDE)
        }

        // 设置状态栏模式
        setMiUIStatusBarDarkText(true)
    }


    // ######################################## callback fun #######################################

    override fun onPanelOpened(panel: View) {
        AppLogger.i("onPanelOpened ")
    }

    override fun onPanelClosed(panel: View) {

        AppLogger.i("onPanelClosed")
        finish()
        overridePendingTransition(0, R.anim.scale_out)
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        AppLogger.i("onPanelSlide " + slideOffset)
    }

    /**
     * Toolbar点击
     * @param type 类型
     */
    override fun onToolbarClick(type: Int) {
        if (TSToolbar.ToolbarClickEnum.LEFT_ICON.ordinal == type) {
            onLeftIconClick()
        } else if (TSToolbar.ToolbarClickEnum.LEFT_TITLE.ordinal == type) {
            onLeftTitleClick()
        } else if (TSToolbar.ToolbarClickEnum.RIGHT_ICON.ordinal == type) {
            onRightIconClick()
        } else if (TSToolbar.ToolbarClickEnum.RIGHT_TITLE.ordinal == type) {
            onRightTitleClick()
        }
    }

    override fun startActivity(intent: Intent) {

        if (isSupportTransitionAnimation()) {
            super.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        } else {
            super.startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState!!.putString("path", photoPath)
    }

    // ######################################## open fun ######################################

    /**
     * 左侧标题被点击
     */
    open fun onLeftTitleClick() {
        finishCompat()
    }

    /**
     * 左侧图标被点击
     */
    open fun onLeftIconClick() {

        finishCompat()
    }

    /**
     * 右侧标题被点击
     */
    open fun onRightTitleClick() {

    }

    /**
     * 右侧图标被点击
     */
    open fun onRightIconClick() {

    }

    /**
     * 取消加载对话框
     */
    open fun onCancelProgressDialog() {

    }

    // ######################################### public fun ########################################


    /**
     * 显示加载对话框
     *
     * @param tip 提示内容
     */
    fun showProgressDialog(tip: String) {

        if (mProcessDialog == null) {
            mProcessDialog = DialogUtil.createProgressDialog(this)
        }

        mProcessDialog?.setCancelable(true)
        mProcessDialog?.setCanceledOnTouchOutside(false)

        if (!mProcessDialog!!.isShowing) {
            mProcessDialog?.show()
        }

        if (!TextUtils.isEmpty(tip)) {
            mProcessDialog?.setTipMessage(tip)
        }

        mProcessDialog?.setCancelBtnVisible(View.GONE)
        mProcessDialog?.setOnCancelListener(DialogInterface.OnCancelListener { onCancelProgressDialog() })

    }

    /**
     * 隐藏加载对话框
     */
    fun hiddenProgressDialog() {

        mProcessDialog?.dismiss()
    }

    override fun onBackPressed() {

        hiddenToast()

        finishCompat()
    }

    /**
     * 隐藏Toast提示框
     */
    fun hiddenToast() {

        mToast?.cancel()
    }

    /**
     * 普通Toast
     *
     * @param resourceID 资源ID
     */
    fun showShortToast(resourceID: Int) {
        Toast.makeText(this, resources.getString(resourceID), Toast.LENGTH_SHORT).show()
    }

    /**
     * 普通Toast
     *
     * @param msg 消息
     */
    fun showShortToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 自定义Toast, 提示完成
     *
     * @param content 内容
     */
    fun showToastFinished(content: String) {

        showToastFinished(content, false)
    }

    /**
     * 自定义Toast, 提示错误
     *
     * @param content 内容
     */
    fun showToastError(content: String) {

        showToastError(content, false)
    }

    /**
     * 自定义Toast, 提示完成
     *
     * @param content        内容
     * @param isLongDuration 时长
     */
    fun showToastFinished(content: String, isLongDuration: Boolean) {

        DialogUtil.showWarningFinished(this, mToast, content, isLongDuration)
    }

    /**
     * 自定义Toast, 提示错误
     *
     * @param content        内容
     * @param isLongDuration 时长
     */
    fun showToastError(content: String, isLongDuration: Boolean) {

        DialogUtil.showWarningError(this, mToast, content, isLongDuration)
    }

    // ######################################## protected fun ######################################


    /**
     * 等待转场动画结束
     */
    protected fun finishCompat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    /**
     * 是否支持全屏
     *
     * @return true 支持、false 不支持
     */
    protected fun isSupportFullScreen(): Boolean {
        return false
    }

    /**
     * 恢复数据
     */
    protected fun restoreData(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            photoPath = savedInstanceState.getString("path")
        }
    }

    /**
     * 是否支持转场动画
     *
     * @return 默认支持
     */
    protected fun isSupportTransitionAnimation(): Boolean {
        return true
    }

    /**
     * 设置转场动画
     */
    protected fun setTransitionAnimation(type: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val enterTransition: Transition
            val exitTransition: Transition

            when (type) {

                TRANSITION_EXPLODE -> {
                    enterTransition = Explode()
                    exitTransition = Explode()
                }
                TRANSITION_SLIDE -> {
                    // ** 不要把Left & Right 改为Start & End
                    enterTransition = Slide(Gravity.RIGHT)
                    exitTransition = Slide(Gravity.LEFT)
                }
                TRANSITION_FADE -> {
                    enterTransition = Fade()
                    exitTransition = Fade()
                }
                else -> {
                    enterTransition = Fade()
                    exitTransition = Fade()
                }
            }

            enterTransition.excludeTarget(android.R.id.statusBarBackground, true)
            enterTransition.excludeTarget(android.R.id.navigationBarBackground, true)

            window.enterTransition = enterTransition.setDuration(ANIMATION_DURATION.toLong())
            window.exitTransition = exitTransition.setDuration(ANIMATION_DURATION.toLong())

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 判断是否权限已授权
     *
     * @param permissionRequest 权限请求
     * @return true 已授权、 false 未授权
     */
    fun checkPermission(permissionRequest: MPermissionUtil.PermissionRequest): Boolean {

        val isPermissionGranted = MPermissionUtil.checkPermission(this, permissionRequest)

        if (!isPermissionGranted) {
            return false
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查并提示用户是否拥有OPS权限
                val isOpsGranted = CheckOpsUtil.checkOrNoteOpPermission(this, CheckOpsUtil.OP_TYPE_NOTE, permissionRequest.getPermissions())

                // 没有Ops权限, 进行提示
                if (!isOpsGranted) {

                    Util.showOpsPermissionDenyDialog(this, getString(R.string.tip), getString(R.string.tip_ops_permission))
                    return false
                }
            }

        }

        return true

    }


    /**
     * 处理权限请求的返回
     *
     * @param requestCode  请求code
     * @param permissions  权限（暂时保留）
     * @param grantResults 权限结果
     */
    fun onPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        AppLogger.i("requestCode=" + requestCode)

        // 拍照
        if (requestCode == MPermissionUtil.PermissionRequest.CAMERA.getRequestCode()) {

            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {

                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_camera_deny_to_settings))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.AUDIO_RECORD.getRequestCode()) {

            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {

                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_audio_deny_to_settings))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.PHONE.getRequestCode()) {
            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {

                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_phone_to_settings))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.READ_WRITE_STORAGE.getRequestCode()) {
            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {
                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_rw_storage_to_settings))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.READ_SMS.getRequestCode()) {
            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {
                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_read_sms))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.VIDEO.getRequestCode()) {
            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {

                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_video))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.LOCATION.getRequestCode()) {
            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {

                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_location))
                return
            }
        } else if (requestCode == MPermissionUtil.PermissionRequest.SAVE_IMAGE.getRequestCode()) {

            // 授权失败
            if (!MPermissionUtil.hasAllPermissionsGranted(grantResults)) {
                showPermissionFailedDialog(getString(R.string.tip), getString(R.string.permission_rw_storage_to_settings))
                return
            }
        }

        // 保存图片
        // 位置
        // 视频
        // 读短信
        // 读写存储设备
        // 电话
        // 录音
        // 到最后，代表权限已获得
        onPermissionGranted(requestCode)
    }

    /**
     * 权限获得
     *
     * @param requestCode 请求code
     */
    protected fun onPermissionGranted(requestCode: Int) {

    }

    /**
     * 当授权失败后，反馈用户，是否进入设置页面
     *
     * @param title   标题
     * @param message 消息
     */
    protected fun showPermissionFailedDialog(title: String, message: String) {

        showPermissionFailedDialog(title, message, null)
    }

    /**
     * 当授权失败后，反馈用户，是否进入设置页面
     *
     * @param title          标题
     * @param message        消息
     * @param dialogCallback 回调
     */
    protected fun showPermissionFailedDialog(title: String, message: String, dialogCallback: IDialogCallback?) {

        Util.showPermissionFailedDialog(this, title, message, dialogCallback)
    }

    /**
     * 初始话toolbar
     */
    private fun initToolbar() {

        mToolbar = findViewById(R.id.tool_bar)
        setSupportActionBar(mToolbar)
        supportActionBar?.title = mToolbar?.title
        mToolbar?.setToolbarClickListener(this@BaseActivity)
    }

    /**
     * 初始化成员
     */
    private fun initVariables() {

        if (mToast == null) {

            mToast = Toast(this)

            val view = LayoutInflater.from(this).inflate(
                    R.layout.bg_custom_toast, null)
            mToast?.view = view
        }
    }

    /**
     * 初始化组件
     */
    protected abstract fun initViews()

    /**
     * 初始化事件
     */
    protected abstract fun initEvent()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 设置Toolbar标题
     */
    protected fun setToolbarTitle(@StringRes resId: Int) {

        mToolbar?.setTitle(getString(resId))
    }

    /**
     * 获取设置contentView
     *
     * @return 控件
     */
    protected abstract fun getContentLayout(): View


    // ######################################## private fun ########################################

    /**
     * 初始化
     */
    private fun init() {

        val frameLayout: FrameLayout = findViewById(R.id.flyt_content)
        frameLayout.addView(getContentLayout())

        initToolbar()

        initVariables()

        initViews()

        initEvent()

        initData()
    }

    /**
     * 是否支持滑动返回
     * @return true 支持滑动返回、false 不支持
     */
    private fun isSupportSwipeBack(): Boolean {
        return false
    }

    /**
     * 初始化滑动返回
     */
    private fun initSwipeBack() {

        if (isSupportSwipeBack()) {

            val slidingPaneLayout = PageSlidingPaneLayout(this)

            try {
                //属性
                val overHang = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
                overHang.isAccessible = true
                overHang.set(slidingPaneLayout, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            slidingPaneLayout.setPanelSlideListener(this@BaseActivity)
            slidingPaneLayout.sliderFadeColor = resources.getColor(android.R.color.transparent)
            slidingPaneLayout.setShadowDrawableLeft(resources.getDrawable(R.drawable.sliding_left_shadow))

            val leftView = View(this)
            leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            slidingPaneLayout.addView(leftView, 0)

            val decor = window.decorView as ViewGroup
            val decorChild = decor.getChildAt(0) as ViewGroup
            decorChild.setBackgroundColor(resources.getColor(android.R.color.white))
            decor.removeView(decorChild)
            decor.addView(slidingPaneLayout)
            slidingPaneLayout.addView(decorChild, 1)
        }
    }

    /**
     * 设置状态栏深色模式还是浅色模式
     *
     * @param isTextDark 是否为深色模式
     */
    private fun setMiUIStatusBarDarkText(isTextDark: Boolean) {

        val clazz = window.javaClass

        try {

            if (Util.isMiUI()) {

                // 旧的方式
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                extraFlagField.invoke(window, if (isTextDark) darkModeFlag else 0, darkModeFlag)

                // 新的方式
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    val window = window

                    if (isTextDark) {

                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        val flag = getWindow().decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        window.decorView.systemUiVisibility = flag
                    }
                }


            }

        } catch (e: Exception) {
            // I know ...
        }

    }


}