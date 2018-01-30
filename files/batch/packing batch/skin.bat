call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\lo" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo" uiskin
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\med" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med" uiskin
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\hi" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi" uiskin

REM Remove uiskin.json
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo\uiskin.json
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med\uiskin.json
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi\uiskin.json

REM Copy uiskin.json from files to android assets
xcopy /s /i C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo\uiskin.json*
xcopy /s /i C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med\uiskin.json*
xcopy /s /i C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi\uiskin.json*