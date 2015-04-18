# Ludumdare 32!

[![Travis build status](https://travis-ci.org/bploeckelman/LudumDare32.svg)](https://travis-ci.org/bploeckelman/LudumDare32)

## Build Requirements

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Android SDK](https://developer.android.com/sdk/index.html#Other)

## Setup

### Mac OS X

The easy way to setup Mac OS X to do libGDX game dev is to utilize [homebrew](http://brew.sh)

Homebrew requires [xcode](https://developer.apple.com/xcode/downloads/).

Install Homebrew:

    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

If you need to install `git` and clone the project, do that first:

    brew install git
    mkdir ~/code && cd ~/code
    git clone git@github.com:bploeckelman/LudumDare32.git
    cd LudumDare32

Install Build requirements:

    brew install caskroom/cask/brew-cask
    brew cask install java
    brew install android-sdk
    echo yes | android update sdk -a --filter "platform-tools,build-tools-20.0.0,android-20" --no-ui --force
    echo "sdk.dir=/usr/local/opt/android-sdk" > local.properties

Run the game!

    ./gradlew desktop:run

The game should build and run the desktop version.  If you don't have a java
IDE installed, you can easily download one (IntelliJ in this example)
with `brew cask`:

    brew cask install intellij-idea-ce

Eclipse and Netbeans are also available through `brew cask`.

**Other Tasks**

You will probably need to configure your `ANDROID_HOME` in your IDE to be
`/usr/local/opt/android-sdk`.

