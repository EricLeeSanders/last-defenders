call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\lo" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo" uiskin
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\med" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med" uiskin
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\hi" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi" uiskin

REM Copy uiskin.json from files to android assets
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo\uiskin.json
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med\uiskin.json
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\uiskin.json C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi\uiskin.json

REM Copy font files from files to android assets
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\lo\*.fnt C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\lo\
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\med\*.fnt C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\med\
xcopy /s /i /y C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\skin\hi\*.fnt C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\skin\hi\