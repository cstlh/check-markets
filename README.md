# Market search for discounts
Small test script to search for discounts in apps using Appium/Kotlin, really looking for a reason to brush up on mobile testing.

## Preconditions
- Android device with USB debugging enabled
- Android Studio installed
- Appium installed and running
- Download and set up the first steps in the app

### Commands to find package name and activity name
`adb shell pm list packages | grep "hit"`

Open the app and run the command:

`adb shell dumpsys window | grep "mCurrentFocus"`
