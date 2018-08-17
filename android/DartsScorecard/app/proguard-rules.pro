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

-keepattributes Signature,InnerClasses

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
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

# WebRtc
-keep class org.webrtc.** { *; }
-dontnote org.apache.http.**
-dontwarn org.apache.http.**
-dontnote android.net.http.*
-dontwarn android.net.http.**
-dontnote org.apache.commons.codec.**
-dontwarn com.google.android.gms.**

