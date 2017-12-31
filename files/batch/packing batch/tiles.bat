REM pack the tiles from the files directory to the assets directory
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\lo\unpacked" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\lo" tiles
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\med\unpacked" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\med" tiles
call java -cp gdx.jar;gdx-tools.jar com.badlogic.gdx.tools.texturepacker.TexturePacker "C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\hi\unpacked" "C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\hi" tiles

REM Now we need to move the newly packed assets to the files directory so that the maps in the files directory can be used
REM remove all files in the directory but leave the subfolders
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\hi
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\med
DEL /q C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\lo

xcopy /s C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\lo C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\lo 
xcopy /s C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\med C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\med
xcopy /s C:\Users\Eric\Documents\GitHub\last-defenders\android\assets\game\levels\tiles\hi C:\Users\Eric\Documents\GitHub\last-defenders\files\assets\game\levels\tiles\hi