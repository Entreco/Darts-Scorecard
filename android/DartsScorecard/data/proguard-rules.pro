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

# Base Settings from Dev
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes LineNumberTable,SourceFile,Signature,*Annotation*,Exceptions,InnerClasses

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Okio
-dontwarn okio.**
-dontnote okio.**

# OkHttp
-dontwarn okhttp3.**

# Billing
-keep class com.android.vending.billing.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Databinding
-keepclasseswithmembernames interface android.databinding.DataBindingComponent
-dontwarn android.databinding.DataBindingComponent
-dontnote android.databinding.DataBindingComponent
-keep class android.databinding.DataBinderMapper
-dontwarn android.databinding.DataBinderMapper
-dontnote android.databinding.DataBinderMapper

# WebRtc
-keep class org.webrtc.** { *; }
-dontnote org.apache.http.**
-dontwarn org.apache.http.**
-dontnote android.net.http.*
-dontwarn android.net.http.**
-dontnote org.apache.commons.codec.**
-dontwarn org.apache.commons.codec.**

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

#GMS
-keep class com.google.android.gms.common.api.internal.BasePendingResult$ReleasableResultGuardian
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keepnames @pcom.google.android.gms.common.annotation.KeepName class *

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

