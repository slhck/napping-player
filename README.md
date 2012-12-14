napping-player
==============

![](http://www.dropbox.com/u/84665/quassumm/nplayer.png)

This is a video player for Android tablets that allows to conduct "Napping" experiments. What are napping experiments?

From Nestrud, Michael A., and Harry T. Lawless. "Recovery of subsampled dimensions and configurations derived from napping data by MFA and MDS." Attention, Perception, & Psychophysics 73.4 (2011): 1266-1278:

> Projective mapping is a relatively new psychological measurement method that was introduced in the mid-1990s […]. It has also been coined “napping” (Pagès, 2005), owing to the French term for tablecloth. The method involves having participants evaluate and physically place stimuli (usually food items) on a large sheet of paper (approximately 60 cm × 60 cm). Stimuli that are similar are placed close together, and stimuli that are dissimilar are placed farther apart. The data collected are the X,Y coordinates of the items with the origin typically being the bottom left of the paper, although it is arbitrary. The X and Y coordinates represent attributes, so we have information on two attributes for each stimulus.



## Features

- Tablet-ready landscape view of the "nap"
- Sequential playing of video material loaded on the device
- Drag video buttons around after seeing them
- Repeat a video any time
- Export coordinates of video buttons
- Export screenshot of the nap
- Group videos and export groups
- Send result e-mail after experiment
- Inspect experiment data on the device itself
- Fully internationalized for English and German (more to come)

## System requirements

- Android tablet with at least 7" screen size or a resolution of about 1200x700 pixels.
- Android 3.0 or later. This app is tested with version 3.x and 4.x.
- Space on the SD card for video material
- A running internet connection if you want to send result files

## Installation

There are two ways to install the Napping Player. Note: You will have to enable debugging and running of unsigned content in the *Developer* section of the system preferences.

1. Download [the APK package](https://dl.dropbox.com/u/84665/quassumm/napping-player.apk) to your device and install it.

2. Download the source code and import it to Eclipse as an Android project. You will have to install the [Android SDK](http://developer.android.com/sdk/index.html) as well as the [ADT plugin](http://developer.android.com/tools/sdk/eclipse-adt.html). Then, click the "Run" button.

## Setting up an experiment

First, prepare your videos according to the [Android Supported Media Formats](http://developer.android.com/guide/appendix/media-formats.html) guide. Use an extension of `.mp4`, `.flv` or `.3gp` — other files will be ignored.

Connect the device to your computer. If the tablet device is recognized as an external storage medium, simply place your videos on the SD card folders mentioned below. If there's no external storage mount, you can push files onto the device via command line. For this to work, make sure you have the [Android SDK](http://developer.android.com/sdk/index.html) installed. Then, run:

    adb shell

Here, enter:

    mkdir -p /mnt/sdcard/NappingMovies
    mkdir -p /mnt/sdcard/NappingLogs

Then,  type `exit`.

Once you have the videos, you can push them to the device like so:

    adb push myvideo.mp4 /mnt/sdcard/NappingMovies/

To this for a number of videos, you have to construct a loop since `adb push` doesn't allow wildcards. For example with Bash:

    for v in *.mp4; do adb push "$v" /mnt/sdcard/NappingMovies/; done


## Conducting an experiment

This is simple. Once the videos are on the device, start the Napping Player. 

1. You will have to enter your name in order to start. The name is associated with the log files later.
2. Click "Start Napping" and you will see the nap.
3. Click "Play Next Video" to start playing the first video.
4. After the video has finished playing, a button will appear in the middle of the screen. Drag it around, wherever you want to place it, according to how similar you think the videos are.
5. Click "Play Next Video" to show the next video, or click the existing button(s) once to show the video again.
6. Repeat steps 4 and 5 until the "Finish" button appears.
7. Click "Finish"
8. If you're prompted to send an e-mail, simply click "Send" in the upper navigation bar.
9. If you are prompted to group the videos, create a new group by pressing the button in the menu.
10. Add videos to a group by clicking them.
11. Create as many new groups as you like, and once done, click "Finish".
12. You're done. Hand the tablet back.

## Data output

You will find the results on the SD card under `NappingLogs`. You can also click the Preferences button in the upper right of the start screen and launch the data explorer to see what files already exist — and delete them if necessary.

For every session, the app will store the following files:

### Video Positions

The file `<date>-<user>-videos.csv` contains all the video IDs, their path (full filename) and the coordinates of the buttons. Example:

      video_id                            file   x   y
    1        0 /mnt/sdcard/NappingMovies/1.mp4 276 169
    2        1 /mnt/sdcard/NappingMovies/2.mp4 913 230

### Video Groups and Keywords

The file `<date>-<user>-groups.csv` contains the groups and their associated videos. Example:

      group_id video_id
    1        0        0
    2        0        1
    3        1        0
    4        2        1

The file `<date>-<user>-keywords.csv` contains the groups and their associated keywords. Example:

      group_id keyword
    1        0     foo
    2        0     baz
    3        1     bar
    4        2     yak

We can easily merge the groups and the list of videos, and then later the keywords, e.g. in R with:

    videos_groups = merge(videos, groups)
    all = merge(videos_groups, keywords)

Then, `all` contains:

      group_id video_id                            file   x   y keyword
    1        0        0 /mnt/sdcard/NappingMovies/1.mp4 276 169     foo
    2        0        0 /mnt/sdcard/NappingMovies/1.mp4 276 169     baz
    3        0        1 /mnt/sdcard/NappingMovies/2.mp4 913 230     foo
    4        0        1 /mnt/sdcard/NappingMovies/2.mp4 913 230     baz
    5        1        0 /mnt/sdcard/NappingMovies/1.mp4 276 169     bar
    6        2        1 /mnt/sdcard/NappingMovies/2.mp4 913 230     yak


### System Config – `<date>-<username>-config.csv`

This file stores basic system information that might be helpful in debugging certain users or relating outcome to devices. At the moment we store the username, the screen width and height as well as the Android version:


    name,           test2
    screen-width,   1280
    screen-height,  800
    release,        3.1

## Development and issues

This app is under development. Things might break. Some stuff might be hard coded.

- If you find any issues or have suggestions for features, please post them on the [GitHub issue tracker](https://github.com/slhck/napping-player/issues). Please mention your Android version and hardware, as well as specific steps to reproduce a bug.
- If you want to help develop this player, please contact me at `slhck` at `me.com` or simply submit pull requests.






