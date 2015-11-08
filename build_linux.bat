@echo off
cd panbox-core
call ant
cd ../panbox-common
call ant
cd ../panbox-linux
call ant
move installer\output\panbox.zip ..
echo Finished building PanBox client. The panbox.zip is ready to use.
