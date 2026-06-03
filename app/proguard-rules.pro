# ==================== General ====================
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ==================== MultiDex ====================
# 保证 MultiDex 相关类进入主 dex，避免启动时找不到类
-keep class androidx.multidex.** { *; }
# 保证 Application 子类在主 dex 中
-keep class com.example.mall.MallApplication { *; }
# 保证 ContentProvider 相关类在主 dex 中（Hilt 的 ActivityEntryPoint 等）
-keep class * extends android.app.Application { *; }
-keep class * extends android.content.ContentProvider { *; }

# ==================== Kotlin ====================
-keepclassmembers class kotlin.Metadata {
    *;
}
-dontwarn kotlin.**
-keep class kotlin.coroutines.Continuation

# ==================== Kotlin Serialization ====================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.example.mall.**$$serializer { *; }
-keepclassmembers class com.example.mall.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.mall.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ==================== Retrofit ====================
-keepattributes Signature, Exceptions
-keep class retrofit2.** { *; }
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn retrofit2.**

# ==================== OkHttp ====================
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ==================== Hilt ====================
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ==================== Room ====================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ==================== Coroutines ====================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ==================== DataStore ====================
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}

# ==================== Coil ====================
-dontwarn coil.**
-keep class coil.** { *; }

# ==================== Firebase ====================
-keep class com.google.firebase.** { *; }
-keep interface com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ==================== Timber ====================
-dontwarn timber.log.**

# ==================== Gson (if used) ====================
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-dontwarn java.lang.invoke.StringConcatFactory

# ==================== Application Models ====================
-keep class com.example.mall.core.model.** { *; }
-keep class com.example.mall.core.database.entity.** { *; }

# ==================== Native Methods ====================
-keepclasseswithmembernames class * {
    native <methods>;
}

# ==================== Enum ====================
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ==================== Parcelable ====================
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ==================== Serializable ====================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
