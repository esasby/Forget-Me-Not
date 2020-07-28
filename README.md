<a href="https://github.com/esasby/Forget-Me-Not">
  <img src="./Logo.svg" alt="Forget Me Not Logo" align="left" width="230">
</a>

Forget Me Not
======================

**Forget Me Not** library is an Android SDK for face-recognition API, developed by [ESaS team](https://esas.by/) for own use and for companies clients. It's used for personal identification on video and photo by binding unique IDs to faces.

## Server API
This SDK can only be used with [ESaS](https://esas.by/) server API. To get it or acquire place on our servers, please contact us on _info@esas.by_. 
We are open to cooperation! 

## Capabilities
- Recognize faces on pictures
- Get vectors of the faces
- Acquire unique IDs for each face stored on our servers
- Compare two face vectors
- Manage face vectors and IDs on server

## Dependency

### Gradle
In your root build.gradle file add jitpack.io repository
~~~ Gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
~~~

In app-level build.gradle file add a dependency
~~~ Gradle
dependencies {
  implementation 'com.github.esasby.Forget-Me-Not:ForgetMeNotLib:v1.7'
}
~~~

### Maven
Add jitpack.io repository
~~~ Maven
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
~~~

Add the library dependency
~~~ Maven
<dependency>
  <groupId>com.github.esasby.Forget-Me-Not</groupId>
  <artifactId>ForgetMeNotLib</artifactId>
  <version>v1.7</version>
</dependency>
~~~

## How to use
### Initialization
For initialization use method `initialize` of `ForgetMeNot` class
~~~ Kotlin
ForgetMeNot.initialize("recognition-server-url", "api-server-secret", "api-token-url")
~~~
### Identification
Use `ForgetMeNotIdentifier` class and its methods `identify` and `identifyWithLandmarks`
~~~ Kotlin
val identifierOptions = ForgetMeNotIdentifier.ForgetMeNotIdentifierOptions(Dispatchers.Main, "MyServerGroup")
val forgetMeNotIdentifier = ForgetMeNotIdentifier(identifierOptions)

val allowNewId: Boolean = false
forgetMeNotIdentifier.identify(images, allowNewId) {
  OnComplete{ }
  OnError{ }
}
~~~ 
Same goes for landmarks version of this function

### Management
Use `ForgetMeNotManager` class for face IDs management. One of the cases is deleting IDs from server
~~~ Kotlin
val managerOptions = ForgetMeNotIdentifier.ForgetMeNotManagerOptions(Dispatchers.Main, "MyServerGroup")
val forgetMeNotManager = ForgetMeNotManager(managerOptions)

forgetMeNotManager.deleteId("userId") {
  OnComplete{ }
  OnError{ }
}
~~~

## Firebase
We use [Firebase Face recognition ML-kit](https://firebase.google.com/docs/ml-kit/android/detect-faces) 
together with our library, and we highly advice you to do so too. Offline version will do well. For easier integration with our library
we also created a Firebase extension for it

## Dependency

### Gradle
In app-level build.gradle file add a dependency
~~~ Gradle
dependencies {
  implementation 'com.github.esasby.Forget-Me-Not:ForgetMeNotFirebaseExt:v1.7'
}
~~~

### Maven
Add the library dependency
~~~ Maven
<dependency>
  <groupId>com.github.esasby.Forget-Me-Not</groupId>
  <artifactId>ForgetMeNotFirebaseExt</artifactId>
  <version>v1.7</version>
</dependency>
~~~

## How to use
### Detection
Use `ForgetMeNotFaceDetector` class for facial detection. The output will be easy 
to use for further operations with our libraries
~~~ Kotlin
val detectorLimits = ForgetMeNotFaceDetectorLimits.Builder()
  .setFaceTurnLimit(45f)
  .setFaceTiltLimit(15f)
  .setFaceSizePercentMin(15f)
  .setFaceOutOfScreenPercentLimit(0f)
  .build()

val detectorOptions = ForgetMeNotFaceDetector.ForgetMeNotFaceDetectorOptions(Main, detectorLimits)
val forgetMeNotDetector = ForgetMeNotFaceDetector(detectorOptions)

forgetMeNotDetector.detectFaces(image, imageSize) {
    onComplete { }
    onError { }
}
~~~
