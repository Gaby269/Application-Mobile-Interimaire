@ECHO OFF
SET com=
SET /p com="Entrez un commentaire : " 
git add *
git commit -m "%com%"
git push
Pause