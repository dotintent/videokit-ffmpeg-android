# Video processing library based on FFmpeg
## Main purpose
Main purpose of this library is to allow developers to embed FFmpeg into their application with literally one line of code
in gradle.build. Using native code in Java in general and in Android environment especially always related to some problems.
Hopefully VideoKit will resolve some of them.

## Setup
Basic setup just add following line of code to your gradle.build file:    
`compile 'com.infullmobile.android:videokit-release:1.1.2'`   
Also, if you haven't use inFullMobile public maven in your project before, don't forget to add following line to your
repositories:   
`maven { url 'https://maven.infullmobile.com/public' }`     
That's pretty much all.

## Example usage
Module sample is showing usage of VideoKit. Please note, that VideoKit is basically invoking FFmpeg main() with CLI arguments
so you will use commands almost like you use standard FFmpeg on Linux or Windows or MacOS.    
Well, let me show you some code:
```
        final VideoKit videoKit = new VideoKit();
        final Command command = videoKit.createCommand()
                .overwriteOutput()
                .inputPath(path)
                .outputPath(path + POSTFIX)
                .customCommand("-ss 1 -t 3")
                .copyVideoCodec()
                .experimentalFlag()
                .build();
```
Command is basically set of instructions for FFmpeg. Please note, that order in which you adding instructions in
CommandBuilder is quite important. Sometimes, when you mess up order, FFmpeg will not recognize command and end up
with error. After you've built a command you should execute it: either with execute() or you can pass it to
AsyncCommandExecutor.    
Please note, that:   
`command.execute();`   
will be executed on thread on which it was called, while:    
`new AsyncCommandExecutor(command, this).execute();`    
will be executed in background thread and you have to provide implementation of ProcessingListener to get result of
processing either in onSuccess (you will get path to processed file here) or onFailure (you will get error code here).

## Other functions
VideoKit is also providing logging mechanism for FFmpeg. You can set logging level by calling function:    
`videoKit.setLogLevel(level)`    
where level is enum which consists of:
```
NO_LOG
ERRORS_ONLY
FULL
```

## Specific behavior of flags
As I already have mentioned: FFmpeg is quite sensible to the order of flags. To help out with that
to user VideoKit have default behavior for particular flags, namely: input paths, output path and experimental flag.    
The order in which VideoKit is laying those flags is following:    

1. Input paths are always going first (doesn't matter in which place of chain you have added particular path)
2. Output path is always going at the end of command
3. Experimental flag is always just before output path

## Error codes
I've tried to make library as debuggable as it even possible, taking in account that we're working with native code.
If you getting failures from VideoKit and log in not really helpfull try to look into *docs* folder in *ffmpeg_return_codes*
file. In this file you not only have list of possible return codes with which FFMpeg can exit, but also file and number of
line of code in which exit was called.    
Version of FFmpeg is 3.2.4 and changed sources can be found over here:    
[Development Kit](https://github.com/IljaKosynkin/FFmpeg-Development-Kit)

## License
FFmpeg is licensed under LGPL:    
[License](https://ffmpeg.org/legal.html)   
**This software uses libraries from the FFmpeg project under the LGPLv2.1**
