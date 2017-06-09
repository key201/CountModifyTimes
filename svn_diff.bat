@echo off
cd %3
%4
set revision=%1
set filepath=%2
svn diff -r %revision% %filepath%
