    object application {
    val appId = "com.vic.villz.ride"
    }


    object Versions{

        val kotlin = "1.3.31"
        val rxjava = "2.2.6"
        val rxandroid = "2.1.1"
        val appcompat = "1.0.0-beta01"
        val constraints = "1.1.3"
        val junit = "4.12"
        val testRunner = "1.1.0-alpha4"
        val espresso = "3.1.0-alpha4"
        val ktx = "1.2.0-alpha01"
        val rxbinding = "3.0.0-alpha2"
        val firbasecore = "17.0.1"
        val firebaseAuth = "18.1.0"
        val firebaseMessaging = "19.0.1"
        val crashlytics = "2.10.1"
        val realtimeDatabase = "18.0.0"
        val materialDesign = "1.0.0"
        val glide ="4.9.0"
        val lifeCycle ="2.0.0"
        val location= "16.0.0"
    }

    object AndroidLibraries {
        val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraints}"
    }

    object KotlinLibraries {
        val kotlin_extensions = "androidx.core:core-ktx:${Versions.ktx}"
        val kotlin_version = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    }

    object TestLibraries {
        val junit = "junit:junit:${Versions.junit}"
        val testRunner = "androidx.test:runner:${Versions.testRunner}"
        val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Libraries {
        val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
        val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
        val rxbindings = "com.jakewharton.rxbinding3:rxbinding:${Versions.rxbinding}"
        val rxbindings_compat = "com.jakewharton.rxbinding3:rxbinding-appcompat:${Versions.rxbinding}"
        val firebasecore = "com.google.firebase:firebase-core:${Versions.firbasecore}"
        val firebaseauth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
        val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"
        val firebaseDatabase = "com.google.firebase:firebase-database:${Versions.realtimeDatabase}"
        val firebaseCrashylitics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
        val viewModelAndLiveData = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycle}"
        val material = "com.google.android.material:material:${Versions.materialDesign}"
        val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        val glideAnnotationProcessor = "com.github.bumptech.glide:compiler:${Versions.glide}"
        val location = "com.google.android.gms:play-services-location:${Versions.location}"
    }