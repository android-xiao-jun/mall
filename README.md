# Mall - Android 企业级 Compose 脚手架

> 适用于即时通讯（IM）、社交、语音房、直播、钱包支付、礼物系统、用户中心等场景的大型 Android 商业项目脚手架。

## 项目概览

本项目采用 **Clean Architecture + MVI + 单向数据流** 架构模式，全量 **Jetpack Compose + Material3** 构建 UI，**Hilt** 依赖注入，**多模块化**开发，**Version Catalog** 统一版本管理。适用于多人协作、长期迭代的大型商业 App。

## 技术栈

| 类别 | 技术 |
|---|---|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material3 |
| 架构 | Clean Architecture + MVI + 单向数据流 |
| 状态管理 | StateFlow + SharedFlow + Channel |
| 依赖注入 | Hilt |
| 网络 | Retrofit + OkHttp + Kotlin Serialization |
| 数据库 | Room |
| 本地存储 | DataStore (Preferences) |
| 国际化 | LocaleManager（运行时动态切换语言，7+ 语言） |
| 主题 | ThemeManager（运行时切换 Dark/Light/System） |
| 图片加载 | Coil |
| 分页 | Paging3 |
| 异步 | Coroutines + Flow |
| 导航 | Navigation Compose |
| 日志 | Timber |
| 序列化 | Kotlin Serialization |
| 构建 | Gradle Kotlin DSL + Version Catalog |
| 测试 | JUnit5 + MockK + Turbine + Compose UI Test |

## 模块结构

```
MallDemo/
├── app/                          # 壳工程：Application、MainActivity、导航、Hilt 绑定
├── core/
│   ├── common/                   # 通用工具：Result、Dispatcher、TokenProvider、LocaleManager、ThemeManager
│   ├── ui/                       # Compose 通用组件：BaseActivity、BaseViewModel (MVI)、LoadingPage 等
│   ├── network/                  # 网络层：Retrofit、OkHttp 拦截器、多环境配置
│   ├── database/                 # 数据库层：Room、DAO、Migration
│   ├── datastore/                # 本地存储：DataStore、Token/用户信息/语言/主题持久化
│   ├── navigation/               # 导航路由：Routes、BottomNavItem
│   ├── designsystem/             # 设计系统：Theme（Light/Dark/DynamicColor）、Color、Typography、Shape
│   ├── model/                    # 数据模型：DTO、Entity
│   ├── domain/                   # 领域层：Repository 接口、UseCase
│   └── player/                   # 播放器：ExoPlayer 封装
├── feature/
│   ├── login/                    # 登录：手机验证码、微信、Apple 登录
│   ├── home/                     # 首页：Banner、Tab、直播推荐
│   ├── chat/                     # 聊天：单聊、群聊、消息收发
│   ├── conversation/             # 会话列表：置顶、免打扰、删除
│   ├── voice_room/               # 语音房：上麦、下麦、锁座、静音
│   ├── live/                     # 直播：直播间、弹幕、打赏
│   ├── gift/                     # 礼物系统：礼物列表、发送、动画
│   ├── wallet/                   # 钱包：余额、充值、提现、交易记录
│   ├── profile/                  # 个人中心：用户信息、等级、VIP
│   └── setting/                  # 设置：主题切换、语言切换、缓存清理
├── gradle/
│   └── libs.versions.toml        # Version Catalog：统一版本管理
└── build.gradle.kts              # 根构建配置
```

### 模块依赖规则

- **Feature 模块** 只能依赖 `core:*` 模块，**禁止** Feature 之间直接依赖
- **core:network** 通过 `TokenProvider` / `AuthExpiredHandler` 接口（定义在 `core:common`）解耦对 `core:datastore` 的直接依赖
- **app 壳工程** 负责通过 Hilt `@Binds` 将接口绑定到具体实现

```
app ──→ feature:* ──→ core:*
                      ├── core:common    ← 最底层：Result、Dispatcher、LocaleManager、ThemeManager
                      ├── core:model     ← 纯数据模型，无依赖
                      ├── core:domain    ← 依赖 core:common, core:model
                      ├── core:network   ← 依赖 core:common (接口解耦)
                      ├── core:database  ← 依赖 core:model
                      ├── core:datastore ← 依赖 core:common (实现 TokenProvider、持久化语言/主题)
                      ├── core:ui        ← 依赖 core:common, core:designsystem (BaseActivity)
                      ├── core:navigation← 纯路由定义
                      ├── core:designsystem ← 依赖 core:common (Compose 主题 + ThemeManager 集成)
                      └── core:player    ← 播放器封装
```

## 架构设计

### MVI 单向数据流

```
┌─────────┐   Intent    ┌─────────────┐   State    ┌─────────┐
│   UI     │ ──────────→ │  ViewModel  │ ─────────→ │   UI    │
│ (Screen) │             │ (Reducer)   │            │(Screen) │
└─────────┘             └──────┬──────┘            └─────────┘
     ↑                        │                        │
     │       Effect           │                        │
     │←───────────────────────┘                        │
     │                                                │
     └──────────── 用户交互 ───────────────────────────┘
```

- **UiState**：页面状态，用 `StateFlow` 管理，新订阅者自动获取最新值
- **UiIntent**：用户行为，用 `SharedFlow` 接收
- **UiEffect**：一次性事件（导航、Toast），用 `Channel` 管理，保证只被消费一次

### BaseViewModel 核心代码

```kotlin
abstract class BaseViewModel<INTENT : UiIntent, STATE : UiState, EFFECT : UiEffect> : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect: Flow<EFFECT> = _effect.receiveAsFlow()

    protected abstract fun createInitialState(): STATE

    protected fun setState(reduce: STATE.() -> STATE) {
        _uiState.value = currentState.reduce()
    }

    protected fun sendEffect(effect: EFFECT) {
        viewModelScope.launch { _effect.send(effect) }
    }

    fun sendIntent(intent: INTENT) {
        viewModelScope.launch { _intent.emit(intent) }
    }
}
```

### 统一结果封装

```kotlin
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val code: Int? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}
```

### Clean Architecture 分层

```
┌─────────────────────────────────────────┐
│  Presentation Layer (feature:* 模块)      │
│  Screen → Intent → ViewModel → Effect   │
├─────────────────────────────────────────┤
│  Domain Layer (core:domain 模块)         │
│  Repository 接口 + UseCase              │
├─────────────────────────────────────────┤
│  Data Layer (core:network/database/data) │
│  Repository 实现 + DataSource + API     │
└─────────────────────────────────────────┘
```

## 网络层设计

### 多环境配置

```kotlin
enum class Environment(val baseUrl: String, val wsUrl: String, val envName: String) {
    DEV(baseUrl = "https://dev-api.example.com/", ...),
    TEST(baseUrl = "https://test-api.example.com/", ...),
    PRE(baseUrl = "https://pre-api.example.com/", ...),
    PROD(baseUrl = "https://api.example.com/", ...),
}
```

### OkHttp 拦截器链

| 拦截器 | 职责 |
|---|---|
| `CommonHeaderInterceptor` | 添加公共 Header（版本号、设备信息、语言等） |
| `TokenInterceptor` | 自动注入 Token，通过 `TokenProvider` 接口获取，解耦 DataStore |
| `AuthExpiredInterceptor` | 监听 401，自动 Token 刷新，通过 `AuthExpiredHandler` 接口通知登出 |
| `HttpLoggingInterceptor` | 请求日志，通过 Timber 输出 |

### 接口解耦

`core:network` 不直接依赖 `core:datastore`，而是通过定义在 `core:common` 中的接口解耦：

```
core:common         → 定义 TokenProvider / AuthExpiredHandler 接口
core:datastore      → 实现 TokenProvider 接口
core:network        → 依赖 TokenProvider / AuthExpiredHandler 接口
app                 → @Binds 将 UserDataStore 绑定为 TokenProvider
```

### 双 OkHttpClient

- `@AuthenticatedClient`：携带 Token 的客户端，用于需要鉴权的 API
- `@PublicClient`：无 Token 的客户端，用于登录、注册等公开 API

## 设计系统

### 主题切换（ThemeManager）

支持运行时切换 Dark / Light / System 主题，**无需 recreate Activity**，Compose UI 通过 `StateFlow` 自动响应。

```kotlin
// 方式一：响应式主题（自动订阅 ThemeManager，推荐）
MallTheme(themeManager = themeManager) {
    // 切换主题时 UI 自动刷新，无需手动 recreate
}

// 方式二：手动指定
MallTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = true) {
    // 你的 Compose 内容
}
```

主题状态通过 DataStore 持久化，App 重启后自动恢复用户选择。

### 主题模式

| 模式 | 代码 | 说明 |
|---|---|---|
| System | `ThemeMode.SYSTEM` | 跟随系统深色模式设置 |
| Light | `ThemeMode.LIGHT` | 强制浅色模式 |
| Dark | `ThemeMode.DARK` | 强制深色模式 |

### BaseActivity 集成

所有 Activity 继承 `BaseActivity`（定义在 `core:ui`），自动获得：
- `attachBaseContext` 中应用语言 Context 包装
- `onConfigurationChanged` 中重新应用语言设置
- `themeManager` / `localeManager` 通过 Hilt 注入

```kotlin
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MallTheme(themeManager = themeManager) {
                // content
            }
        }
    }
}
```

### 品牌色板

| 色彩 | Light | Dark |
|---|---|---|
| Primary | `#6C5CE7` 紫色 | `#A29BFE` 浅紫 |
| Secondary | `#00CEC9` 青色 | `#55EFC4` 浅绿 |
| Error | `#E74C3C` 红色 | `#FF6B6B` 浅红 |
| Background | 白色 | `#0F0F1A` 深黑 |

### 业务色

| 名称 | 色值 | 用途 |
|---|---|---|
| LiveRed | `#FF4757` | 直播相关 |
| VipGold | `#FFD700` | VIP 等级 |
| DiamondBlue | `#00B4D8` | 钻石/虚拟货币 |
| CoinOrange | `#FF9F43` | 金币 |
| OnlineGreen | `#2ECC71` | 在线状态 |

## 基础能力框架

### MultiDex 支持

解决 64K 方法数限制问题，确保项目在引入大量第三方库后仍能正常构建和运行。

**Gradle 配置**（`app/build.gradle.kts`）：

```kotlin
android {
    defaultConfig {
        multiDexEnabled = true
    }
}

dependencies {
    implementation(libs.androidx.multidex)
}
```

**Application 初始化**：

```kotlin
@HiltAndroidApp
class MallApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)  // minSdk >= 21 时系统自动支持，显式调用确保兼容
    }
}
```

**主 Dex 优化**（`proguard-rules.pro`）：

```proguard
# 保证启动类优先进入主 dex
-keep class androidx.multidex.** { *; }
-keep class com.example.mall.MallApplication { *; }
-keep class * extends android.app.Application { *; }
-keep class * extends android.content.ContentProvider { *; }
```

### 多语言切换框架（i18n）

支持运行时动态切换语言，**无需强制重启 Activity**，兼容 Android 7 ~ Android 14。

**支持语言**：

| 语言 | 代码 | 方向 |
|---|---|---|
| 跟随系统 | `system` | - |
| 简体中文 | `zh` | LTR |
| English | `en` | LTR |
| Español | `es` | LTR |
| العربية | `ar` | RTL |
| Русский | `ru` | LTR |
| Қазақ | `kk` | LTR |
| ئۇيغۇرچە | `ug` | RTL |

**核心设计 — LocaleManager**（定义在 `core:common`）：

```kotlin
// 切换语言（运行时生效，无需重启）
localeManager.setLanguage(AppLanguage.EN)
localeManager.updateApplicationLocale()

// 获取当前语言
val current = localeManager.getCurrentLanguage()  // AppLanguage.EN

// 观察 Locale 变化
localeManager.localeChangeFlow.collect { locale -> ... }
```

**Context 包装机制**：

```
Activity.attachBaseContext       → localeManager.wrapContext(base)       → 资源加载使用正确语言
Activity.onConfigurationChanged  → localeManager.applyToConfiguration() → 系统配置变化时重新应用
Application.onCreate             → localeManager.updateApplicationLocale() → Toast/Notification 等非 Activity 场景生效
```

**语言持久化**：通过 `UserDataStore`（DataStore Preferences）自动持久化，App 重启后恢复用户选择。

**DI 集成**（`app/di/LocaleModule`）：

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object LocaleModule {
    @Provides @Singleton
    fun provideLocaleManager(appContext: Context, userDataStore: UserDataStore): LocaleManager {
        return LocaleManager(
            context = appContext,
            persistLanguage = { code -> userDataStore.saveLanguage(code) },
            getPersistedLanguageFlow = { userDataStore.getLanguageFlow() },
        )
    }
}
```

**多语言资源**：`app/src/main/res/` 下按标准 Android 资源限定符组织：

```
values/          → 默认（简体中文）
values-en/       → English
values-es/       → Español
values-ar/       → العربية (RTL)
values-ru/       → Русский
values-kk/       → Қазақ
values-ug/       → ئۇيغۇرچە (RTL)
```

### 主题切换框架（Theme System）

支持运行时切换 Dark / Light / System 主题，Compose UI 通过 `StateFlow` 自动响应，**无需 recreate Activity**。

**核心设计 — ThemeManager**（定义在 `core:common`）：

```kotlin
// 切换主题（Compose UI 自动刷新）
themeManager.setThemeMode(ThemeMode.DARK)

// 获取当前主题
val mode = themeManager.currentThemeMode  // ThemeMode.DARK

// 观察主题变化
themeManager.themeModeFlow.collect { mode -> ... }
```

**MallTheme 响应式集成**：

```kotlin
// 自动订阅 ThemeManager，切换主题无需手动 recreate
MallTheme(themeManager = themeManager) {
    Surface(modifier = Modifier.fillMaxSize()) {
        // content
    }
}
```

**主题持久化**：通过 `UserDataStore` 自动持久化，App 重启后恢复用户选择。

**XML 主题适配**（`app/src/main/res/values/`）：

```
values/themes.xml       → Light 主题（透明状态栏/导航栏）
values-night/themes.xml → Dark  主题（透明状态栏/导航栏 + 深色背景）
```

**DI 集成**（`app/di/ThemeModule`）：

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {
    @Provides @Singleton
    fun provideThemeManager(userDataStore: UserDataStore): ThemeManager {
        return ThemeManager(
            persistThemeMode = { code -> userDataStore.saveThemeMode(code) },
            getPersistedThemeModeFlow = { userDataStore.getThemeModeFlow() },
        )
    }
}
```

### 基础能力框架文件清单

| 能力 | 核心文件 | 位置 |
|---|---|---|
| MultiDex | `MallApplication.kt` | `app/` |
| MultiDex | `proguard-rules.pro` | `app/` |
| MultiDex | `libs.versions.toml` | `gradle/` |
| i18n | `LocaleManager.kt` + `AppLanguage` | `core:common/i18n/` |
| i18n | `BaseActivity.kt` | `core:ui/activity/` |
| i18n | `LocaleModule.kt` | `app/di/` |
| i18n | `strings.xml` × 7 | `app/res/values-{locale}/` |
| Theme | `ThemeManager.kt` + `ThemeMode` | `core:common/theme/` |
| Theme | `MallTheme(themeManager)` | `core:designsystem/theme/Theme.kt` |
| Theme | `ThemeModule.kt` | `app/di/` |
| Theme | `themes.xml` (Light/Dark) | `app/res/values{-night}/` |

## Feature 模块规范

每个 Feature 模块遵循统一的包结构：

```
feature/login/
├── di/                            # Hilt Module
├── data/
│   ├── remote/                    # API 接口定义
│   ├── repository/                # Repository 实现
│   └── mapper/                    # DTO → Model 转换
├── domain/
│   └── usecase/                   # UseCase（可选，简单业务可省略）
└── presentation/
    ├── intent/                    # LoginIntent
    ├── state/                     # LoginUiState
    ├── effect/                    # LoginEffect
    ├── viewmodel/                 # LoginViewModel
    └── screen/                    # LoginScreen (Composable)
```

### Feature 模板示例（Login）

```kotlin
// Intent
sealed interface LoginIntent : UiIntent {
    data class PhoneChanged(val phone: String) : LoginIntent
    data class CodeChanged(val code: String) : LoginIntent
    data object SendSmsCode : LoginIntent
    data object Login : LoginIntent
}

// State
data class LoginUiState(
    val phone: String = "",
    val code: String = "",
    val isLoggingIn: Boolean = false,
    val isPolicyAgreed: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val canLogin: Boolean get() = phone.isNotBlank() && code.isNotBlank() && isPolicyAgreed
    val canSendCode: Boolean get() = phone.isNotBlank()
}

// Effect
sealed interface LoginEffect : UiEffect {
    data class ShowToast(val message: String) : LoginEffect
    data class ShowError(val message: String) : LoginEffect
    data object NavigateToHome : LoginEffect
    data object StartSmsCountdown : LoginEffect
}

// ViewModel
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<LoginIntent, LoginUiState, LoginEffect>() {

    override fun createInitialState() = LoginUiState()

    init {
        handleIntent { intent ->
            when (intent) {
                is LoginIntent.PhoneChanged -> handlePhoneChanged(intent.phone)
                is LoginIntent.Login -> handleLogin()
                // ...
            }
        }
    }
}
```

## 快速开始

### 环境要求

- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- Android SDK：compileSdk 35, minSdk 24
- Kotlin 2.0.21

### 构建运行

```bash
# Debug 构建
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# Release 构建（需配置签名）
./gradlew assembleRelease
```

### 签名配置

在 `local.properties` 或环境变量中配置：

```properties
KEYSTORE_PATH=/path/to/keystore.jks
KEYSTORE_PASSWORD=your_password
KEY_ALIAS=your_alias
KEY_PASSWORD=your_key_password
```

### 多环境切换

在 `app/build.gradle.kts` 中通过 `buildConfigField` 配置：

| Build Variant | 环境 | ApplicationId |
|---|---|---|
| debug | DEV | com.example.mall.debug |
| release | PROD | com.example.mall |

## 构建配置

### Version Catalog

所有依赖版本集中在 `gradle/libs.versions.toml` 管理，模块中通过 `libs.xxx` 引用。

> **注意**：Version Catalog 别名中的 `-` 在 Kotlin DSL 中需替换为 `.`，例如：
> - TOML: `androidx-lifecycle-runtime-compose`
> - Kotlin DSL: `libs.androidx.lifecycle.runtime.compose`

### 关键版本

| 依赖 | 版本 |
|---|---|
| AGP | 8.10.1 |
| Kotlin | 2.0.21 |
| KSP | 2.0.21-1.0.27 |
| Compose BOM | 2024.12.01 |
| Hilt | 2.54 |
| Retrofit | 2.11.0 |
| OkHttp | 4.12.0 |
| Room | 2.7.1 |
| DataStore | 1.1.7 |
| Coroutines | 1.10.2 |
| MultiDex | 2.0.1 |

## CI/CD

项目包含 GitHub Actions 配置（`.github/workflows/android-ci.yml`），支持：

- Debug / Release 构建验证
- Lint 检查
- 单元测试执行
- APK 产物上传

## 测试策略

| 层级 | 工具 | 关注点 |
|---|---|---|
| ViewModel | JUnit5 + MockK + Turbine | Intent 处理、State 转换、Effect 发射 |
| Repository | JUnit5 + MockK | 数据源协调、缓存策略 |
| UI | Compose UI Test | 界面渲染、用户交互 |
| 网络 | MockWebServer | API 请求/响应 |

## 新增 Feature 模块 Checklist

1. 在 `settings.gradle.kts` 中 `include(":feature:xxx")`
2. 创建模块目录及 `build.gradle.kts`，应用必要插件（`android.library`, `kotlin.android`, `kotlin.compose`, `ksp`, `hilt`）
3. 按标准包结构创建 `intent/state/effect/viewmodel/screen`
4. 在 `core:domain` 中定义 Repository 接口（如需要）
5. 创建 Repository 实现 + Hilt `@Binds` 绑定
6. 在 `app/build.gradle.kts` 中添加 `implementation(project(":feature:xxx"))`
7. 在 `core:navigation/Routes.kt` 中添加路由定义
8. 在 `app/navigation/MallNavHost.kt` 中添加导航目的地

## License

Private - All Rights Reserved
