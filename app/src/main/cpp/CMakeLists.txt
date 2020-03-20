cmake_minimum_required(VERSION 3.4.1)

set(SOURCES)

file(GLOB_RECURSE SOURCES ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)


add_library(native-lib
        SHARED
        ${SOURCES})

set(LIBS)

list(APPEND LIBS
        jnigraphics
        log
        )


target_link_libraries(
        native-lib
        ${LIBS})


#add_library(
#        native-lib
#
#        SHARED
#
#        src/main/cpp/native-lib.cpp
#        src/main/cpp/giflib/dgif_lib.c
#        src/main/cpp/giflib/gif_err.c
#        src/main/cpp/giflib/gifalloc.c
#)


#find_library(
#        log-lib
#        log )


#target_link_libraries(
#        native-lib
#        jnigraphics
#        ${log-lib} )