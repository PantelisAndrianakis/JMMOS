@set /p parameters=<java.cfg
@echo off
title JMMOS GameServer

:start
echo Starting Game Server.
echo.

java %parameters% -jar GameServer.jar

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo Admin Restarted Game Server.
echo.
goto start

:error
echo.
echo Game Server Terminated Abnormally!
echo.

:end
echo.
echo Game Server Terminated.
echo.
pause