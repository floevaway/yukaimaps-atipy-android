YukaimApp
=========

Android App for the Yukaimaps suite, forked from 
[StreetComplete](https://github.com/streetcomplete/StreetComplete).

For the the original README, see 
[README.StreetComplete.md](./README.StreetComplete.md)


Develop
-------

You will need to [install Android Studio](https://developer.android.com/studio)

Then add the following lines to your $HOME/.bash_profile or $HOME/.bashrc (if 
you are using zsh then ~/.zprofile or ~/.zshrc) config file:

```sh
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Build dev app

Some env vars can be defined to override app
at build time (below with their default values)

```
export API_ROOT=https://dev.yukaimaps.someware.fr/osm

export OIDC_SERVER=https://dev.yukaimaps.someware.fr/auth/realms/yukaimaps

export OAUTH_CLIENT_ID=dev.yukaimaps.someware.fr

export OIDC_INSECURE=false
```

The default config build the app for using with
https://dev.yukaimaps.someware.fr

To build the app for using it with the dev seed :

```
cp env.local.example.sh env.local.sh

# edit env.local.sh to tweak the values...
# ... and source the new file
source env.local.sh
```

Debug build

```
./gradlew buildDebug
```

Install on a connected device or emulator

```
./gradlew installDebug
```
