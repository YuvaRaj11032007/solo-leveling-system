# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to the flags specified
# in getDefaultProguardFile(...)

-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}
