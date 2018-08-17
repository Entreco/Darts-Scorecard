# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Databinding
-keepclasseswithmembernames interface android.databinding.DataBindingComponent
-dontwarn android.databinding.DataBindingComponent
-dontnote android.databinding.DataBindingComponent
-keep class android.databinding.DataBinderMapper
-dontwarn android.databinding.DataBinderMapper
-dontnote android.databinding.DataBinderMapper

#GMS
-keep class com.google.android.gms.common.api.internal.BasePendingResult$ReleasableResultGuardian
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keepnames @pcom.google.android.gms.common.annotation.KeepName class *

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

