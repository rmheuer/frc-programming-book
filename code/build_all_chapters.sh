#!/bin/bash

build_chapter() {
    cd $1
    echo "*** Building {$1}"
    ./gradlew build
    cd ..
}

build_chapter chapter01
build_chapter chapter03
build_chapter chapter04
build_chapter chapter05
build_chapter chapter06
build_chapter chapter07
