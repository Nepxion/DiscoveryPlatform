@echo on
@echo =============================================================
@echo $                                                           $
@echo $                     Nepxion Discovery                     $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Studio All Right Reserved                        $
@echo $  Copyright (C) 2017-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Discovery
@color 0a

call mvn clean deploy -DskipTests -e -P release -pl discovery-platform-common/discovery-platform-starter-common-dingding,discovery-platform-common/discovery-platform-starter-common-mail,discovery-platform-server/discovery-platform-starter-server-h2,discovery-platform-server/discovery-platform-starter-server-ldap,discovery-platform-server/discovery-platform-starter-server-ldap,discovery-platform-server/discovery-platform-starter-server-mysql,discovery-platform-server/discovery-platform-starter-server-ui,discovery-platform-client/discovery-platform-starter-client -am

pause