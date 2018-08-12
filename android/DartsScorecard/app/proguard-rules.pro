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

-keepattributes Signature

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Google Play Services
-keep public class com.google.android.gms.* { public *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.**
-keepnames @com.google.android.gms.common.annotation.KeepName class
    com.google.android.gms.**,
    com.google.ads.**

-keepclassmembernames class
    com.google.android.gms.**,
    com.google.ads.** {
    @com.google.android.gms.common.annotation.KeepName *;
}

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
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

# WebRtc
-keep class org.webrtc.** { *; }
-dontwarn org.chromium.build.**
-dontwarn org.webrtc.Logging**
