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
# Keep all model classes
##-keep class com.mb.kibeti.goal_tracker.model.** { *; }
##-keep class com.mb.kibeti.goal_tracker.GoalResponse { *; }
##-keep class com.mb.kibeti.goal_tracker.GoalDetails { *; }
##-keep class com.mb.kibeti.goal_tracker.Step { *; }
##
### GSON specific rules
##-keepattributes Signature
##-keepattributes Annotation
###-keep class sun.misc.Unsafe { *; }
##-keep class com.google.gson.** { *; }


# GSON specific rules
-keepattributes Signature
-keepattributes *Annotation*

# Gson core library (this is required)
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class com.google.gson.TypeAdapter
-keep class com.google.gson.TypeAdapterFactory
-keep class com.google.gson.JsonSerializer
-keep class com.google.gson.JsonDeserializer
-keep class com.google.gson.JsonElement
-keep class com.google.gson.Gson { *; }
-keep class com.google.gson.GsonBuilder { *; }

# Keep generic type info for reflection
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# If you use Retrofit with Gson
-keepattributes RuntimeVisibleAnnotations
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}


-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE