[voat-android](https://github.com/voat/voat-android)
=========

Official voat Android app repository

![screenshot](https://github.com/voat/voat-android/raw/master/screenshots/screenshot-1.png)

[![Build Status](https://travis-ci.org/voat/voat-android.svg?branch=master)](https://travis-ci.org/voat/voat-android)

### Building

You should be able to import the project directly to Android Studio by clicking open, then navigating to the outermost build.gralde file. You will then need to create your own Voat API key, as well as a [fabric](https://fabric.io) (though generating a fabric key is completely optional) key and store them either in a gradle.properties file at the root of the project or in your Gradle home directory. It should be placed in your gradle.properties file as so:
```Gradle
VOAT_API_KEY = "YOUR_API_KEY_HERE"
VOAT_FABRIC_KEY = "YOUR_FABRIC_KEY_OR_EMPTY_STRING_IF_YOU_DONT_CARE"
```

### Attributions

The following 3rd party libraries are used:

- Retrofit (http://square.github.io/retrofit/)
- OkHttp (http://square.github.io/okhttp/)
- Otto (http://square.github.io/otto/)
- Glide (https://github.com/bumptech/glide)
- Butter Knife (http://jakewharton.github.io/butterknife/)
- Timber (https://github.com/JakeWharton/timber)
- Material-ish Progress (https://github.com/pnikosis/materialish-progress)
- CircleImageView (https://github.com/hdodenhof/CircleImageView)
- FloatingActionButton (https://github.com/futuresimple/android-floating-action-button)
- PhotoView (https://github.com/chrisbanes/PhotoView)
- PhysicsLayout (https://github.com/Jawnnypoo/PhysicsLayout)
