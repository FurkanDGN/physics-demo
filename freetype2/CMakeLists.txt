cmake_minimum_required(VERSION 3.11)
project(freetype-jni)

set(JAVA_HOME /Library/Java/JavaVirtualMachines/jdk-17.0.3.1.jdk/Contents/Home)

set(JAVA_AWT_INCLUDE_PATH NotNeeded)
set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_INCLUDE_PATH2 ${JAVA_HOME}/include/darwin)
set(JAVA_INCLUDE_PATH ${JAVA_HOME}/include)
set(JAVA_JVM_LIBRARY ${JAVA_HOME}/lib/server/libjvm.so)

find_package(Java REQUIRED)
find_package(JNI  REQUIRED)

include(UseJava)

add_subdirectory(freetype-remote)

add_library(freetype-jni SHARED jni/freetype_jni.cpp)

set(NATIVE_HEADERS ${CMAKE_BINARY_DIR}/native)
set(CLASSES_BIN ${CMAKE_BINARY_DIR}/bin)

file(MAKE_DIRECTORY ${CLASSES_BIN})

add_custom_target(freetype-jni-headers
    COMMAND "${Java_JAVAC_EXECUTABLE}" -d "${CLASSES_BIN}" -h "${NATIVE_HEADERS}" "${CMAKE_CURRENT_SOURCE_DIR}/src/main/java/me/furkandgn/freetype2/*.java"
	DEPENDS freetype
)
add_dependencies(freetype-jni freetype-jni-headers)

include_directories(${JNI_INCLUDE_DIRS})
include_directories(${NATIVE_HEADERS})
include_directories(${freetype_SOURCE_DIR}/include)

target_link_libraries(freetype-jni freetype)

add_custom_command(TARGET freetype-jni POST_BUILD
    COMMAND cd "${CLASSES_BIN}" && "${Java_JAR_EXECUTABLE}" cvf ../freetype-jni.jar .
)

add_custom_command(TARGET freetype-jni POST_BUILD
    COMMAND "${CMAKE_COMMAND}" -E make_directory "${CLASSES_BIN}/natives/"
    COMMAND "${CMAKE_COMMAND}" -E copy $<TARGET_FILE:freetype-jni> "${CLASSES_BIN}/natives/"
    COMMAND cd "${CLASSES_BIN}" && "${Java_JAR_EXECUTABLE}" uvf ../freetype-jni.jar natives/libfreetype-jni.dylib
    DEPENDS freetype-jni
)